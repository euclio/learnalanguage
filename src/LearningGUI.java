import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class LearningGUI extends JFrame {
    private static final int TERMINAL_COLS = 40;
    private static final int TERMINAL_ROWS = 80;
    private static final String TERMINAL_TEXT = "Welcome to LearnALanguage";
    private static final Font TERMINAL_FONT = new Font("Monospaced", Font.PLAIN, 12);
    
    private static final Dimension WINDOW_DIMENSION = new Dimension(800, 600);
    
    public LearningGUI (String title) {
        super(title);        
        
        JPanel optionsPanel = new JPanel();
        
        JPanel editorPanel = new JPanel();
        
        JPanel consolePanel = new JPanel();
        
        JTextArea console = new JTextArea(TERMINAL_TEXT, TERMINAL_ROWS, TERMINAL_COLS);
        console.setFont(TERMINAL_FONT);
        consolePanel.add(console);
        
        console.setEditable(false);
        
        this.add(optionsPanel, BorderLayout.NORTH);
        this.add(editorPanel, BorderLayout.CENTER);
        this.add(consolePanel, BorderLayout.EAST);
        this.setSize(WINDOW_DIMENSION);
        
        
    }
}
