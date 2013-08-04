package com.acrussell.learnalanguage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import java.util.Scanner;

public class Files {
    private static Preferences prefs = Preferences
            .userNodeForPackage(Files.class);
    private static List<File> openFiles;

    public static String readFile(File f) {
        StringBuilder result = new StringBuilder();
        try (Scanner reader = new Scanner(f)) {
            while (reader.hasNextLine()) {
                result.append(reader.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            System.err.println("The file could not be loaded.");
        }

        return result.toString();
    }

    public static List<File> getOpenFiles() {
        if (openFiles == null) {
            openFiles = new ArrayList<File>();
            String openFileNames = prefs.get(Settings.OPEN_FILES, null);
            if (openFileNames != null) {
                for (String fileName : openFileNames.split(";")) {
                    File f = new File(fileName);
                    if (f.exists()) {
                        openFiles.add(f);
                    }
                }
            }
        }
        return openFiles;
    }

    public static File getLastOpenFile() {
        String fileName = prefs.get(Settings.LAST_OPEN_FILE, null);
        if (fileName == null) {
            return null;
        } else {
            return new File(fileName);
        }
    }

    public static void writeToFile(String text, File f) throws IOException {
        FileWriter writer = new FileWriter(f);

        writer.write(text);
        writer.close();
    }
}
