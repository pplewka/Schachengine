import java.util.Arrays;

public abstract class Translators {
    private static int[] realTranslationTable = new int[]{26, 27, 28, 29, 30, 31, 32, 33,
            38, 39, 40, 41, 42, 43, 44, 45,
            50, 51, 52, 53, 54, 55, 56, 57,
            62, 63, 64, 65, 66, 67, 68, 69,
            74, 75, 76, 77, 78, 79, 80, 81,
            86, 87, 88, 89, 90, 91, 92, 93,
            98, 99, 100, 101, 102, 103, 104, 105,
            110, 111, 112, 113, 114, 115, 116, 117};

    /**
     * Method to translate 144 format into the Board's 64 format
     *
     * @param calculated field in 144 format
     * @return corresponding field in board's 64 format
     * @throws TranslatorException if calculated is not translatable. means if calculated is in Space
     *                          look for realTranslationTable to see which values are translatable
     */
    public static int translate144to64(int calculated) {
        int index = Arrays.binarySearch(realTranslationTable, calculated);
        if (index < 0) {
            throw new TranslatorException("translate144to64: wrong parameter");
        } else {
            return (byte) index;
        }
    }

    /**
     * Method to translate Board's 64 format into the 144 format of the MoveGeneration
     *
     * @param field field in 64 format
     * @return corresponding field in MoveGeneration's 144 format
     * @throws TranslatorException if field is not translatable. should never happen
     *                                   look for realTranslationTable to see which values are translatable
     */
    public static int translate64To144(int field) {
        try {
            return realTranslationTable[field];
        } catch (IndexOutOfBoundsException e) {
            throw new TranslatorException("translate64To144: wrong parameter");
        }
    }

    public static int translateAlgTo64(String field){
        int multiplier;
        int addition;

        int column =(int) field.charAt(0);
        addition = column - 97;

        int row =(int) field.charAt(1);
        multiplier = row - 49;

        if (addition < 0 || addition > 7 || multiplier < 0 || multiplier > 7) {
            throw new TranslatorException("translateAlgTo64: malformed field");
        }

        return multiplier * 8 + addition;
    }
}
