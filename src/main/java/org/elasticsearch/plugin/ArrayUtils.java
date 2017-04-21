package org.elasticsearch.plugin;

public class ArrayUtils {
    public static boolean contains(String[] array, String textToFind) {
        for (String text : array) {
            if (text.equals(textToFind)) {
                return true;
            }
        }

        return false;
    }
}
