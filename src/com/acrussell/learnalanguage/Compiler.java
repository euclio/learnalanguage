package com.acrussell.learnalanguage;

import java.io.File;
import java.util.List;

import javax.swing.SwingWorker;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.swing.JButton;

public class Compiler extends SwingWorker<Void, String> {
    private Console status;
    private JavaCompiler theCompiler = ToolProvider.getSystemJavaCompiler();
    private JButton compileButton;
    private List<File> source;

    /**
     * Creates a new Compiler object
     *
     * @param source
     *            The source code to be compiled
     * @param status
     *            The text component that should display the compiler's status
     */
    public Compiler(List<File> source, JButton compileButton, Console status) {
        this.source = source;
        this.status = status;
        this.compileButton = compileButton;
    }

    @Override
    protected Void doInBackground() {
        if (source.isEmpty()) {
            publish("No source to compile.");
            return null;
        }

        for (File f : source) {
            publish(String.format("Compiling %s...", f.getName()));

            // Compiles the source into a .class file
            int statusCode = theCompiler.run(null, status.getStream(),
                    status.getStream(), f.getAbsolutePath());
            if (statusCode == 0) {
                publish("Compilation successful.");
            } else {
                publish(String.format("Compilation failed with error code %d",
                        statusCode));
            }
        }
        return null;
    }

    @Override
    protected void process(List<String> statusMessages) {
        for (String message : statusMessages) {
            status.appendLine(message);
        }
    }

    @Override
    protected void done() {
        compileButton.setEnabled(true);
    }
}
