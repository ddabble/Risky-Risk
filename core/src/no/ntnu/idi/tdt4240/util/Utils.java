package no.ntnu.idi.tdt4240.util;

import com.badlogic.gdx.math.Vector2;

import java.io.File;

public class Utils {
    public static Vector2 avg(Vector2 v1, Vector2 v2) {
        return new Vector2((v1.x + v2.x) / 2,
                           (v1.y + v2.y) / 2);
    }

    public static String getLinkToLineInFile(File file, int lineNumber) {
        return file.getPath() + "(" + file.getName() + ":" + lineNumber + ")";
    }

    public static String getLinkToCharInFile(File file, String fileContents, int charIndex) {
        int lineNumber = 1 + countOccurences(fileContents.substring(0, charIndex), "\n");
        return getLinkToLineInFile(file, lineNumber);
    }

    public static int countOccurences(String src, String findString) {
        int count = 0;
        int findStringLength = findString.length();

        for (int lastIndex = src.indexOf(findString); lastIndex != -1; ) {
            count++;
            lastIndex = src.indexOf(findString, lastIndex + findStringLength);
        }

        return count;
    }
}
