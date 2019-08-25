import java.util.Arrays;

public abstract class Translators {
    private static int[] moveGenTable = new int[]{26,  27,  28,  29,  30,  31,  32,  33,
                                                          38,  39,  40,  41,  42,  43,  44,  45,
                                                          50,  51,  52,  53,  54,  55,  56,  57,
                                                          62,  63,  64,  65,  66,  67,  68,  69,
                                                          74,  75,  76,  77,  78,  79,  80,  81,
                                                          86,  87,  88,  89,  90,  91,  92,  93,
                                                          98,  99,  100, 101, 102, 103, 104, 105,
                                                          110, 111, 112, 113, 114, 115, 116, 117};

    private static String[] algTable = new String[]{"a8","b8","c8","d8","e8","f8","g8","h8",
                                                    "a7","b7","c7","d7","e7","f7","g7","h7",
                                                    "a6","b6","c6","d6","e6","f6","g6","h6",
                                                    "a5","b5","c5","d5","e5","f5","g5","h5",
                                                    "a4","b4","c4","d4","e4","f4","g4","h4",
                                                    "a3","b3","c3","d3","e3","f3","g3","h3",
                                                    "a2","b2","c2","d2","e2","f2","g2","h2",
                                                    "a1","b1","c1","d1","e1","f1","g1","h1"};

    /**
     * Method to translate 144 format into the Board's 64 format
     *
     * @param calculated field in 144 format
     * @return corresponding field in board's 64 format
     * @throws TranslatorException if calculated is not translatable. means if calculated is in Space
     *                          look for realTranslationTable to see which values are translatable
     */
    public static int translate144to64(int calculated) {
        int index = Arrays.binarySearch(moveGenTable, calculated);
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
            return moveGenTable[field];
        } catch (IndexOutOfBoundsException e) {
            throw new TranslatorException("translate64To144: wrong parameter");
        }
    }

    /**
     * Method to translate Algebraic format into the Board's 64 format
     *
     * @param field field in Algebraic format
     * @return corresponding field in board's 64 format
     * @throws TranslatorException if field is not translatable into a Board index.
     */
    public static int translateAlgTo64(String field){
        int multiplier;
        int addition;

        int column =(int) field.charAt(0);
        addition = column - 97;

        int row =(int) field.charAt(1);
        multiplier = (row - 48) * (-1) + 8;
        if (addition < 0 || addition > 7 || multiplier < 0 || multiplier > 7) {
            throw new TranslatorException("translateAlgTo64: malformed field");
        }
        return multiplier * 8 + addition;
    }

    /**
     * Method to translate the Board's 64 format into Algebraic format
     *
     * @param field field in 64 format
     * @return corresponding field in algebraic notation
     * @throws TranslatorException if field is not translatable.
     */
    public static String translate64ToAlg(int field){
        try {
            return algTable[field];
        } catch (IndexOutOfBoundsException e) {
            throw new TranslatorException("translate64ToAlg: wrong parameter");
        }
    }
}
