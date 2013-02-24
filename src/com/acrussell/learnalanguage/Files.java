package com.acrussell.learnalanguage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Files {
    private static Preferences prefs = Settings.getSettings();
    private static List<File> openFiles;

    public static String readFile(File f) {
        StringBuilder result = new StringBuilder();
        java.util.Scanner reader = null;
        try {
            reader = new java.util.Scanner(f);
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
            String openFileNames = prefs.get("open_files", null);
            if (openFileNames != null) {
                for (String fileName : openFileNames.split(",")) {
                    openFiles.add(new File(fileName));
                }
            }
        }
        return openFiles;
    }

    public static File getLastOpenFile() {
        String fileName = Settings.getSettings().get("last_file", null);
        if (fileName == null) {
            return null;
        } else {
            return new File(fileName);
        }
    }
}
