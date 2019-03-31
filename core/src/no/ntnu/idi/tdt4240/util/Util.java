package no.ntnu.idi.tdt4240.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Util {
    public static String readFile(File file) throws IOException {
        StringBuilder fileContents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null)
                fileContents.append(line).append("\n");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found: " + file.toString());
        } catch (IOException e) {
            throw new IOException("Unable to read file: " + file.toString());
        }

        return fileContents.toString();
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
