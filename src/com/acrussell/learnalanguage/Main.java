package com.acrussell.learnalanguage;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            // Do nothing
        } catch (InstantiationException e) {
            // Do nothing
        } catch (IllegalAccessException e) {
            // Do nothing
        } catch (UnsupportedLookAndFeelException e) {
            // Do nothing
        }

        final LearningGUI gui = new LearningGUI("LearnALanguage");
        gui.setVisible(true);
    }
}