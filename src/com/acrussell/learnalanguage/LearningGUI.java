package com.acrussell.learnalanguage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class LearningGUI extends JFrame {
    private static final int TERMINAL_COLS = 60;
    private static final int TERMINAL_ROWS = 4;
    private static final int STATUS_COLS = TERMINAL_COLS;
    private static final int STATUS_ROWS = 8;

    private static final String TERMINAL_TEXT = "Welcome to LearnALanguage\n";
    private static final String COMPILE_BUTTON_TEXT = "Compile";
    private static final String RUN_BUTTON_TEXT = "Run!";
    private static final String STOP_BUTTON_TEXT = "Stop";

    private static final String EDITOR_BORDER_TEXT = "Editor";
    private static final String TERMINAL_BORDER_TEXT = "Terminal";
    private static final String STATUS_BORDER_TEXT = "VM Status";

    private static final Dimension WINDOW_DIMENSION = new Dimension(1024, 768);

    private final JPanel optionsPanel, consoleContainer;
    private final JButton compileButton, runButton, stopButton;
    private final EditorPanel editorPanel;
    private final JScrollPane terminalScroll;
    private final Console terminal, status;

    private Runner runner;
    private List<String> args = new ArrayList<String>();
    private List<File> openFiles = Files.getOpenFiles();

    public LearningGUI(String title) {
        super(title);

        // Initialize the top panel
        optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        compileButton = new JButton(COMPILE_BUTTON_TEXT);
        runButton = new JButton(RUN_BUTTON_TEXT);
        stopButton = new JButton(STOP_BUTTON_TEXT);

        // Button to compile the user's program
        compileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (File f : openFiles) {
                    editorPanel.confirmSave(f);
                }
                new Compiler(openFiles, compileButton, status).execute();
            }
        });
        optionsPanel.add(compileButton);

        // Button to run the user's program
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File selectedFile = editorPanel.getOpenFile();
                if (selectedFile == null) {
                    status.appendLine("Could not run. There is no selected file.");
                    return;
                }
                runner = new Runner(editorPanel.getOpenFile(), args, runButton,
                        stopButton, terminal, status);
                runner.execute();
            }
        });
        optionsPanel.add(runButton);

        // Button to cancel execution of the user's program
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (runner != null) {
                    runner.cancel(true);
                }
            }
        });
        optionsPanel.add(stopButton);

        editorPanel = new EditorPanel();
        editorPanel.setBorder(BorderFactory
                .createTitledBorder(EDITOR_BORDER_TEXT));

        // teachingContainer.add(editorScroll, BorderLayout.CENTER);

        // Create the panel that holds the consoles
        consoleContainer = new JPanel(new BorderLayout());

        terminal = new Console(TERMINAL_TEXT, TERMINAL_ROWS, TERMINAL_COLS);

        terminalScroll = new JScrollPane(terminal,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        terminalScroll.setBorder(BorderFactory
                .createTitledBorder(TERMINAL_BORDER_TEXT));

        consoleContainer.add(terminalScroll, BorderLayout.CENTER);

        status = new Console(STATUS_ROWS, STATUS_COLS);
        status.setMaxLines(STATUS_ROWS);

        JPanel statusPanel = new JPanel();
        statusPanel.add(status);
        statusPanel.setBorder(BorderFactory
                .createTitledBorder(STATUS_BORDER_TEXT));

        consoleContainer.add(statusPanel, BorderLayout.SOUTH);

        // Add the components to the main window
        this.setJMenuBar(new LearningMenuBar(this));
        this.add(optionsPanel, BorderLayout.NORTH);
        this.add(editorPanel, BorderLayout.CENTER);
        this.add(consoleContainer, BorderLayout.EAST);

        // Final Options for GUI
        this.setSize(WINDOW_DIMENSION);
        this.setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                editorPanel.cleanUpFiles();
                System.exit(0);
            }
        });
    }

    public EditorPanel getEditorPanel() {
        return editorPanel;
    }
}
