package com.acrussell.learnalanguage;

import java.util.prefs.Preferences;

public class Settings {
    private static Preferences prefs = Preferences.userRoot();
    
    public static Preferences getSettings() {
        return prefs;
    }
}
