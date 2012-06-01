import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class LearningGUI extends JFrame {
    private static final int EDITOR_COLS = 80;
    private static final int EDITOR_ROWS = 80;
    private static final Font EDITOR_FONT = new Font("Monospaced", Font.PLAIN, 14);
    
    private static final int TERMINAL_COLS = 60;
    private static final int TERMINAL_ROWS = 4;
    private static final String TERMINAL_TEXT = "Welcome to LearnALanguage";
    private static final Font TERMINAL_FONT = new Font("Monospaced",
            Font.PLAIN, 12);

    private static final Dimension WINDOW_DIMENSION = new Dimension(1024, 768);

    public LearningGUI(String title) {
        super(title);

        // Initialize the top panel
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton compileButton = new JButton("Compile");
        optionsPanel.add(compileButton);
        
        JButton run = new JButton("Run!");
        optionsPanel.add(run);
        
        // Create the panel that holds the code editor
        JEditorPane editor = new JEditorPane();
        editor.setContentType("text/java");
        JScrollPane editorPane = new JScrollPane(editor);
        editorPane.setBorder(BorderFactory.createTitledBorder("Editor"));

        // Create the panel that holds the console  
        JTextArea console = new JTextArea(TERMINAL_TEXT, TERMINAL_ROWS, TERMINAL_COLS);
        console.setEditable(false);
        console.setFont(TERMINAL_FONT);
        JScrollPane consolePane = new JScrollPane(console);
        consolePane.setBorder(BorderFactory.createTitledBorder("Console"));

        // Add the components to the main window
        this.add(optionsPanel, BorderLayout.NORTH);
        this.add(editorPane, BorderLayout.CENTER);
        this.add(consolePane, BorderLayout.EAST);
        
        // Final Options for GUI
        this.setSize(WINDOW_DIMENSION);
    }
}
