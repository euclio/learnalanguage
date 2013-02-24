package com.acrussell.learnalanguage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.SwingWorker;

public class Runner extends SwingWorker<String, String> {
    private Preferences prefs = Preferences.userNodeForPackage(this.getClass());
    // private Process p;
    private Console output, status;
    private JButton runButton, stopButton;
    private File mainClass;
    private List<String> parameters;
    private StreamPiper piper;

    /**
     * Creates an object to handle running an executable Java process.
     * 
     * @param terminal
     * @param status
     */
    public Runner(File mainClass, List<String> parameters, JButton runButton,
            JButton stopButton, Console output, Console status) {
        this.output = output;
        this.status = status;
        this.runButton = runButton;
        this.stopButton = stopButton;
        this.mainClass = mainClass;
        this.parameters = parameters;
    }

    @Override
    public String doInBackground() {
        runButton.setEnabled(false);
        stopButton.setEnabled(true);

        List<String> processParams = new ArrayList<String>();

        // Execute with Java
        processParams.add("java");

        // Add directory of main file to classpath
        processParams.add("-cp");
        processParams.add(mainClass.getParent());

        // Extract the name of the class from the file
        String mainClassName = mainClass.getName().split("\\.")[0];
        processParams.add(mainClassName);
        
        // Add the program's runtime parameters
        processParams.addAll(parameters);

        ProcessBuilder pb = new ProcessBuilder(processParams);
        status.appendLine(String.format("Running %s.java...", mainClassName));

        // Execute process
        Process p;
        try {
            p = pb.start();
        } catch (IOException ioe) {
            return "Error: Could not access the program.";
        }

        // Clear the terminal at each execution if the user desires
        if (prefs.getBoolean("terminal_should_clear", false)) {
            output.setText("");
        }

        // Send process output to console
        piper = new StreamPiper(p.getInputStream(), output.getStream());
        piper.start();

        // Thread that notifies GUI that the process has completed
        int statusCode = -1;
        try {
            statusCode = p.waitFor();
            String statusMessage = String.format("Machine returned %d (%s).",
                    statusCode, statusCode == 0 ? "success" : "error");
            publish(statusMessage);
        } catch (InterruptedException e) {
            publish("Interrupting machine...");
            p.destroy();
            piper.interrupt();
            publish("Machine interrupted.");
        }
        return "Execution complete.";
    }

    protected void process(List<String> messages) {
        for (String message : messages) {
            status.appendLine(message);
        }
    }

    @Override
    protected void done() {
        if (!this.isCancelled()) {
            try {
                status.appendLine(get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        runButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    class StreamPiper extends Thread {
        private static final int BUFFER_SIZE = 2048;

        private BufferedReader reader;
        private BufferedWriter writer;

        private boolean isInterrupted = false;
        private char[] buffer = new char[BUFFER_SIZE];

        public StreamPiper(InputStream is, OutputStream os) {
            reader = new BufferedReader(new InputStreamReader(is));
            writer = new BufferedWriter(new OutputStreamWriter(os));
        }

        public void interrupt() {
            isInterrupted = true;
        }

        @Override
        public void run() {
            try {
                while (!isInterrupted) {
                    int charsRead = reader.read(buffer);
                    if (charsRead == -1) {
                        break;
                    }

                    writer.append(new String(buffer, 0, charsRead));
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}