import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

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

    public LearningMenuBar() {
        ActionListener menuListener = new MenuListener();

        // Build the "File" Menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem close = new JMenuItem("Exit");
        close.addActionListener(menuListener);
        fileMenu.add(close);

        this.add(fileMenu);

        // Build the "Help" Menu
        JMenu helpMenu = new JMenu("Help");

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(menuListener);
        helpMenu.add(about);

        this.add(helpMenu);
    }

    public void about() {
        JOptionPane.showMessageDialog(this.getParent(),
                "This program is coded by Andy Russell.");
    }

    public void exit() {
        System.exit(0);
    }
}
