import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventListener;

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
    private final Compiler compiler = new Compiler();
    
    private static final int EDITOR_COLS = 80;
    private static final int EDITOR_ROWS = 80;
    private static final Font EDITOR_FONT = new Font("Monospaced", Font.PLAIN, 14);
    
    private static final int TERMINAL_COLS = 60;
    private static final int TERMINAL_ROWS = 4;
    private static final String TERMINAL_TEXT = "Welcome to LearnALanguage\n";
    private static final Font TERMINAL_FONT = new Font("Monospaced",
            Font.PLAIN, 12);

    private static final Dimension WINDOW_DIMENSION = new Dimension(1024, 768);

    public LearningGUI(String title) {
        super(title);

        // Initialize the top panel
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton compileButton = new JButton("Compile");
        compileButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               compiler.compile("Test.java");
           }
        });
        optionsPanel.add(compileButton);
        
        JButton run = new JButton("Run!");
        optionsPanel.add(run);
        
        // Create the panel that holds the code editor
        JEditorPane editor = new JEditorPane();
        editor.setContentType("text/java");
        JScrollPane editorPane = new JScrollPane(editor);
        editorPane.setBorder(BorderFactory.createTitledBorder("Editor"));
        compiler.setSource(editor);

        // Create the panel that holds the console  
        JTextArea console = new JTextArea(TERMINAL_TEXT, TERMINAL_ROWS, TERMINAL_COLS);
        console.setEditable(false);
        console.setFont(TERMINAL_FONT);
        JScrollPane consolePane = new JScrollPane(console);
        consolePane.setBorder(BorderFactory.createTitledBorder("Console"));
        compiler.setOutput(console);
        


        // Add the components to the main window
        this.setJMenuBar(new LearningMenuBar());
        this.add(optionsPanel, BorderLayout.NORTH);
        this.add(editorPane, BorderLayout.CENTER);
        this.add(consolePane, BorderLayout.EAST);
        
        // Final Options for GUI
        
        this.setSize(WINDOW_DIMENSION);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
