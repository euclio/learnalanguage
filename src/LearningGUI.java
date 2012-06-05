import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

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
    private final Compiler compiler;
    private final Runner runner;

    private static final int TERMINAL_COLS = 60;
    private static final int TERMINAL_ROWS = 4;
    private static final String TERMINAL_TEXT = "Welcome to LearnALanguage\n";
    private static final Font TERMINAL_FONT = new Font("Monospaced",
            Font.PLAIN, 12);

    private static final Dimension WINDOW_DIMENSION = new Dimension(1024, 768);

    private String className = "Test";
    private String[] args = new String[0];
    private String initialProgram = "public class Test {\n"
            + "\tpublic static void main(String [] args) {\n"
            + "\t\tSystem.out.println(\"Hello World!\");\n" + "\t}\n" + "}";

    public LearningGUI(String title) {
        super(title);

        // Initialize the top panel
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        final JButton compileButton = new JButton("Compile");
        final JButton runButton = new JButton("Run!");
        final JButton stopButton = new JButton("Stop");

        compileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compiler.compile(className + ".java");
            }
        });
        optionsPanel.add(compileButton);

        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runner.execute(className, args);
                //stopButton.setEnabled(true);
            }
        });
        optionsPanel.add(runButton);

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runner.stop();
                //((JButton) e.getSource()).setEnabled(false);
            }
        });

        (new Thread() {
            public void run() {
                while (true) {
                    if (runner != null && runner.isExecuting()) {
                        stopButton.setEnabled(true);
                    } else {
                        stopButton.setEnabled(false);
                    }
                }
            }
        }).start();

        optionsPanel.add(stopButton);

        // Create the panel that holds the code editor
        jsyntaxpane.DefaultSyntaxKit.initKit();
        JEditorPane editor = new JEditorPane();
        JScrollPane editorPane = new JScrollPane(editor);
        editor.setContentType("text/java");
        editor.setText(initialProgram);
        editorPane.setBorder(BorderFactory.createTitledBorder("Editor"));

        // Create the panel that holds the console
        LearningConsole console = new LearningConsole(TERMINAL_TEXT,
                TERMINAL_ROWS, TERMINAL_COLS);
        console.setEditable(false);
        console.setFont(TERMINAL_FONT);
        JScrollPane consolePane = new JScrollPane(console);
        consolePane.setBorder(BorderFactory.createTitledBorder("Console"));
        compiler = new Compiler(editor, console);
        runner = new Runner(console);

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
