import Exceptions.FixYourConfigFileException;

import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Handles sending and receiving option commands
 */
public class UCIOptionHandler {


    private static final String PROP_OPTION_TAG = "option.";

    private static final String PROP_NAME_SUBTAG = "name.";
    private static final String PROP_TYPE_SUBTAG = "type.";
    private static final String PROP_DEFAULT_SUBTAG = "default.";
    private static final String PROP_VAR_SUBTAG = "var.";
    private static final String PROP_MIN_SUBTAG = "min.";
    private static final String PROP_MAX_SUBTAG = "max.";

    private static final String PROP_NAME_TAG = PROP_OPTION_TAG + PROP_NAME_SUBTAG;
    private static final String PROP_TYPE_TAG = PROP_OPTION_TAG + PROP_TYPE_SUBTAG;
    private static final String PROP_DEFAULT_TAG = PROP_OPTION_TAG + PROP_DEFAULT_SUBTAG;
    private static final String PROP_VAR_TAG = PROP_OPTION_TAG + PROP_VAR_SUBTAG;
    private static final String PROP_MIN_TAG = PROP_OPTION_TAG + PROP_MIN_SUBTAG;
    private static final String PROP_MAX_TAG = PROP_OPTION_TAG + PROP_MAX_SUBTAG;


    private static final String TYPE_CHECK = "check";
    private static final String TYPE_SPIN = "spin";
    private static final String TYPE_COMBO = "combo";
    private static final String TYPE_STRING = "string";
    private static final String TYPE_BUTTON = "button";

    /**
     * Sends all available options to the GUI
     *
     * @param ucioptions the ucioptions property
     */
    public static void sendAvailableOptions(Properties ucioptions) {
        ArrayList<String> allOptionIDs = getAllOptionIDs(ucioptions);
        ArrayList<String> optionCommands = new ArrayList<>();
        for (String optionName : allOptionIDs) {
            optionCommands.add(buildOptionCommand(optionName, ucioptions));
        }
        for (String optionCommand : optionCommands) {
            UCIBridge.getInstance().sendString(optionCommand);
        }
    }

    /**
     * Gets all supported option ids
     *
     * @param ucioptions the ucioptions property
     * @return a List with all supported ids
     */
    private static ArrayList<String> getAllOptionIDs(Properties ucioptions) {
        ArrayList<String> optionIDs = new ArrayList<>();
        for (String property : ucioptions.stringPropertyNames()) {
            if (property.startsWith(PROP_NAME_TAG)) {
                optionIDs.add(property.replaceFirst(PROP_NAME_TAG, ""));
            }
        }
        return optionIDs;
    }

    /**
     * Returns the type of an id
     *
     * @param optionID   the id
     * @param ucioptions the ucioptions property
     * @return the type
     */
    private static OptionType getType(String optionID, Properties ucioptions) {
        String type = ucioptions.getProperty(PROP_TYPE_TAG + optionID);
        switch (type) {
            case TYPE_COMBO:
                return OptionType.COMBO;
            case TYPE_SPIN:
                return OptionType.SPIN;
            case TYPE_BUTTON:
                return OptionType.BUTTON;
            case TYPE_CHECK:
                return OptionType.CHECK;
            case TYPE_STRING:
                return OptionType.STRING;
            default:
                throw new FixYourConfigFileException("unsupported type: " + type);
        }
    }

    /**
     * Builds a command ready to send to the gui describing the option
     * Eg. "option name Threads type spin min 1 max 4 default 1"
     *
     * @param optionID   the id
     * @param ucioptions the ucioptions property
     * @return the command
     */
    private static String buildOptionCommand(String optionID, Properties ucioptions) {
        OptionType type = getType(optionID, ucioptions);
        String displayName = ucioptions.getProperty(PROP_NAME_TAG + optionID);
        String default_;
        if (type != OptionType.BUTTON) {
            default_ = ucioptions.getProperty(PROP_DEFAULT_TAG + optionID);
        } else {
            default_ = null;
        }
        switch (type) {
            case CHECK:
                return String.format("option name %s type check default %s", displayName, default_);

            case SPIN:
                String min = ucioptions.getProperty(PROP_MIN_TAG + optionID);
                String max = ucioptions.getProperty(PROP_MAX_TAG + optionID);
                return String.format(
                        "option name %s type spin default %s min %s max %s", displayName, default_, min, max);

            case COMBO:
                String[] vars = ucioptions.getProperty(PROP_VAR_TAG + optionID).split(";");
                return String.format(
                        "option name %s type combo default %s var %s",
                        displayName, default_, String.join(" var ", vars));

            case STRING:
                return String.format("option name %s type string default %s", displayName, default_);

            case BUTTON:
                return String.format("option name %s type button", displayName);
            default:
                // cant be reached. enum checks all possibilities. just java things
                throw new FixYourConfigFileException("should never happen");
        }
    }

    /**
     * Receives setoption commands from the GUI until isready is send
     * Stores all send options with values and returns them
     *
     * @param ucioptions the ucioptions property
     * @return all options with values, unset options will have the corresponding default value
     */
    public static ArrayList<OptionValuePair> receiveOptions(Properties ucioptions) {

        // receive new options via GUI
        String command = UCIBridge.getInstance().receiveString(false).toLowerCase();
        ArrayList<OptionValuePair> results = new ArrayList<>();
        while (!command.equals(UCICommands.IS_READY)) {
            if (!isValidSetOption(command)) {
                InfoHandler.sendDebugMessage("Found in UCIOptionHandler.recieveOptions");
                UCIBridge.getInstance().sendUnknownCommandMessage(command);
            } else {
                String displaynameAndValue = command.replaceFirst(UCICommands.SET_OPTION + " name", "");
                String[] displaynameAndValueSplitted = displaynameAndValue.split("value");
                String ID = getID(displaynameAndValueSplitted[0].trim(), ucioptions);
                if (isValidID(ID, ucioptions)) {
                    if (isValidValue(ID, displaynameAndValueSplitted, ucioptions)) {
                        if (displaynameAndValueSplitted.length == 1) {
                            results.add(new OptionValuePair(ID, "<empty>"));
                        } else if (displaynameAndValueSplitted.length == 2) {
                            results.add(new OptionValuePair(ID, displaynameAndValueSplitted[1].trim()));
                        } else {
                            UCIBridge.getInstance().sendUnknownCommandMessage(command);
                        }
                    } else {
                        UCIBridge.getInstance().sendUnknownCommandMessage(command + " (value invalid)");
                    }
                } else {
                    UCIBridge.getInstance().sendUnknownCommandMessage(command + " (name invalid)");
                }
            }

            command = UCIBridge.getInstance().receiveString(false).toLowerCase();
        }
        // set default values for unset options
        ArrayList<String> allOptionIDs = getAllOptionIDs(ucioptions);
        for (String optionID : allOptionIDs) {
            boolean found = false;
            for (OptionValuePair result : results) {
                if (result.option.equals(optionID)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                //buttons don't support default values
                if (!ucioptions.getProperty(PROP_TYPE_TAG + optionID).equals(TYPE_BUTTON)) {
                    results.add(new OptionValuePair(optionID, ucioptions.getProperty(PROP_DEFAULT_TAG + optionID)));
                }
            }
        }
        return results;
    }

    /**
     * Check if the give id is supported by the engine
     *
     * @param id         the id
     * @param ucioptions the ucioptions property
     * @return true if supported, false else
     */
    private static boolean isValidID(String id, Properties ucioptions) {
        return ucioptions.getProperty(PROP_NAME_TAG + id) != null;
    }

    /**
     * Check if the given value is supported by the given id
     *
     * @param id                          the id
     * @param displaynameAndValueSplitted the displayname and the value in a String[] (or only the displayname if
     *                                    id is from a button)
     * @param ucioptions                  the ucioptions property
     * @return true if value is supported, else false
     */
    private static boolean isValidValue(String id, String[] displaynameAndValueSplitted, Properties ucioptions) {
        OptionType type = getType(id, ucioptions);
        int len = displaynameAndValueSplitted.length;
        switch (type) {
            case BUTTON:
                return len == 1;
            case STRING:
                return len == 2 || len == 1;
            case CHECK:
                return len == 2 && (displaynameAndValueSplitted[1].trim().equals("true") ||
                        displaynameAndValueSplitted[1].trim().equals("false"));
            case COMBO:
                if (len != 2) {
                    return false;
                }
                String[] possibleValues = ucioptions.getProperty(PROP_VAR_TAG + id).toLowerCase().split(";");
                for (String possibleValue : possibleValues) {
                    if (possibleValue.equals(displaynameAndValueSplitted[1].trim())) {
                        return true;
                    }
                }
                return false;
            case SPIN:
                if (len != 2) {
                    return false;
                }
                Long min, max, value;
                try {
                    min = Long.valueOf(ucioptions.getProperty(PROP_MIN_TAG + id));
                } catch (NumberFormatException e) {
                    throw new FixYourConfigFileException(String.format("min value %s of %s cant be converted",
                            ucioptions.getProperty(PROP_MIN_TAG + id), id));
                }
                try {
                    max = Long.valueOf(ucioptions.getProperty(PROP_MAX_TAG + id));
                } catch (NumberFormatException e) {
                    throw new FixYourConfigFileException(String.format("max value %s of %s cant be converted",
                            ucioptions.getProperty(PROP_MAX_TAG + id), id));
                }
                try {
                    value = Long.valueOf(displaynameAndValueSplitted[1].trim());
                } catch (NumberFormatException e) {
                    return false;
                }
                return min <= value && value <= max;
            default: // cant be reached. but java wants it
                InfoHandler.sendMessage("something is seriously broken");
                return false;
        }
    }

    /**
     * Gets the id corresponding to a displayName
     *
     * @param displayName the name
     * @param ucioptions  the ucioptions property
     * @return the id if displayName is supported, else null
     */
    private static String getID(String displayName, Properties ucioptions) {
        for (String stringPropertyName : ucioptions.stringPropertyNames()) {
            if (stringPropertyName.startsWith(PROP_NAME_TAG)) {
                String ID = stringPropertyName.replaceFirst(PROP_NAME_TAG, "");
                if (ucioptions.getProperty(PROP_NAME_TAG + ID).toLowerCase().equals(displayName.toLowerCase())) {
                    return ID;
                }
            }
        }
        return null;
    }

    /**
     * Check if the given command is a valid setoption command
     * (some invalid commands will pass this check, but there are no false-negatives)
     *
     * @param command the command
     * @return true if command is more or less a valid setoption command, false if it is definitely not one
     */
    private static boolean isValidSetOption(String command) {
        return Pattern.matches(UCICommands.SET_OPTION + " name .+", command);
    }

    /**
     * Enum to describe the possible type of an option
     */
    public enum OptionType {CHECK, SPIN, COMBO, STRING, BUTTON}
}