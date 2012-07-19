package com.acrussell.learnalanguage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class LearningMenuBar extends JMenuBar {
    public enum MenuChoices {
        // File
        EXIT,

        // Help
        ABOUT
    };

    class MenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem) e.getSource();
            String itemText = source.getText().replaceAll("[^a-zA-Z0-9]", "");

            switch (MenuChoices.valueOf(itemText.toUpperCase())) {
            case EXIT:
                exit();
                break;

            // Help Menu
            case ABOUT:
                about();
            }
        }
    }

    private Preferences prefs = Preferences.userRoot();

    public LearningMenuBar() {
        ActionListener menuListener = new MenuListener();

        // Build the "File" Menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem close = new JMenuItem("Exit");
        close.addActionListener(menuListener);
        fileMenu.add(close);

        this.add(fileMenu);

        // Build the "Options" Menu
        JMenu optionsMenu = new JMenu("Options");

        JCheckBoxMenuItem clearTerminal = new JCheckBoxMenuItem(
                "Automatically clear terminal");
        clearTerminal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
                Preferences.userNodeForPackage(this.getClass()).putBoolean(
                        "terminal_should_clear", source.isSelected());
            }
        });
        optionsMenu.add(clearTerminal);
        this.add(optionsMenu);

        // Build the "Help" Menu
        JMenu helpMenu = new JMenu("Help");

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(menuListener);
        helpMenu.add(about);

        this.add(helpMenu);
    }

    public void about() {
        JOptionPane
                .showMessageDialog(
                        this.getParent(),
                        "Coded by Andy Russell 2012.\n"
                                + "This program uses the JSyntaxPane library licensed under Apache 2.0.\n"
                                + "The license may be found at http://www.apache.org/licenses/LICENSE-2.0.html");
    }

    public void exit() {
        System.exit(0);
    }
}
