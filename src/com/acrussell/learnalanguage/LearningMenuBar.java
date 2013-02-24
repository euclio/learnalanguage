package com.acrussell.learnalanguage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class LearningMenuBar extends JMenuBar {
    private LearningGUI gui;
    private Preferences prefs = Preferences.userNodeForPackage(this.getClass());

    public enum MenuChoices {
        // File
        OPEN, EXIT,

        // Help
        ABOUT
    };

    class MenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem) e.getSource();
            String itemText = source.getText().replaceAll("[^a-zA-Z0-9]", "");

            switch (MenuChoices.valueOf(itemText.toUpperCase())) {
            case OPEN:
                open(gui.getEditorPanel());
                break;

            case EXIT:
                exit();
                break;

            // Help Menu
            case ABOUT:
                about();
            }
        }
    }

    public LearningMenuBar(LearningGUI gui) {
        this.gui = gui;
        ActionListener menuListener = new MenuListener();

        // Build the "File" Menu
        JMenu fileMenu = new JMenu("File");

        // Open file option
        JMenuItem open = new JMenuItem("Open...");
        open.addActionListener(menuListener);
        fileMenu.add(open);

        // Add the exit option
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
                prefs.putBoolean(Settings.TERM_SHOULD_CLEAR, source.isSelected());
            }
        });
        clearTerminal.setSelected(prefs.getBoolean(Settings.TERM_SHOULD_CLEAR, false));
        optionsMenu.add(clearTerminal);
        this.add(optionsMenu);

        // Build the "Help" Menu
        JMenu helpMenu = new JMenu("Help");

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(menuListener);
        helpMenu.add(about);

        this.add(helpMenu);
    }

    public void open(EditorPanel ep) {
        JFileChooser chooser = new JFileChooser(new File(""));
        chooser.setFileFilter(new FileNameExtensionFilter("Java Files", "java"));
        int returnVal = chooser.showOpenDialog(chooser.getParent());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            ep.addFile(chooser.getSelectedFile());
        }
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
