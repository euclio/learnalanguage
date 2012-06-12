import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class LearningGUI extends JFrame {
    private final JPanel optionsPanel, consoleContainer;
    private final JButton compileButton, runButton, stopButton;
    private final JEditorPane editor;
    private final JScrollPane editorScroll, terminalScroll;
    private final Console terminal, status;

    private final Compiler compiler;
    private final Runner runner;

    private static final int TERMINAL_COLS = 60;
    private static final int TERMINAL_ROWS = 4;
    private static final int STATUS_COLS = TERMINAL_COLS;
    private static final int STATUS_ROWS = 8;
    private static final String TERMINAL_TEXT = "Welcome to LearnALanguage\n";

    private static final Dimension WINDOW_DIMENSION = new Dimension(1024, 768);

    private String className = "Test";
    private String[] args = new String[0];
    // Simple 'Hello World' program
    private String initialProgram = "public class Test {\n"
            + "\tpublic static void main(String [] args) {\n"
            + "\t\tSystem.out.println(\"Hello World!\");\n" + "\t}\n" + "}";

    public LearningGUI(String title) {
        super(title);

        // Initialize the top panel
        optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        compileButton = new JButton("Compile");
        runButton = new JButton("Run!");
        stopButton = new JButton("Stop");

        compileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compiler.compile(className + ".java");
            }
        });
        optionsPanel.add(compileButton);

        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runner.execute(className, args);
            }
        });
        optionsPanel.add(runButton);

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runner.kill();
            }
        });

        // Thread that toggles buttons if the machine is executing
        (new Thread() {
            public void run() {
                while (true) {
                    if (runner != null && runner.isExecuting()) {
                        stopButton.setEnabled(true);
                        compileButton.setEnabled(false);
                    } else {
                        stopButton.setEnabled(false);
                        compileButton.setEnabled(true);
                    }
                }
            }
        }).start();

        optionsPanel.add(stopButton);

        // Create the panel that holds the code editor
        jsyntaxpane.DefaultSyntaxKit.initKit();
        editor = new JEditorPane();

        editorScroll = new JScrollPane(editor);
        editorScroll.setBorder(BorderFactory.createTitledBorder("Editor"));

        editor.setContentType("text/java");
        editor.setText(initialProgram);

        // Create the panel that holds the consoles
        consoleContainer = new JPanel(new BorderLayout());

        terminal = new Console(TERMINAL_TEXT, TERMINAL_ROWS, TERMINAL_COLS);

        terminalScroll = new JScrollPane(terminal);
        terminalScroll.setBorder(BorderFactory.createTitledBorder("Terminal"));

        consoleContainer.add(terminalScroll, BorderLayout.CENTER);

        status = new Console(STATUS_ROWS, STATUS_COLS);
        status.setMaxLines(STATUS_ROWS);

        status.setBorder(BorderFactory.createTitledBorder("VM Status"));

        consoleContainer.add(status, BorderLayout.SOUTH);

        compiler = new Compiler(editor, status);
        runner = new Runner(terminal, status);

        // Add the components to the main window
        this.setJMenuBar(new LearningMenuBar());
        this.add(optionsPanel, BorderLayout.NORTH);
        this.add(editorScroll, BorderLayout.CENTER);
        this.add(consoleContainer, BorderLayout.EAST);

        // Final Options for GUI
        this.setSize(WINDOW_DIMENSION);
        this.setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
