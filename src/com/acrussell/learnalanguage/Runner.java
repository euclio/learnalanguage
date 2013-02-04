package com.acrussell.learnalanguage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
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
    private String mainClass, parameters[];

    /**
     * Creates an object to handle running an executable Java process.
     * 
     * @param terminal
     * @param status
     */
    public Runner(String mainClass, String[] parameters, JButton runButton,
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
        // Create new process to execute user code
        ProcessBuilder pb = new ProcessBuilder("java", mainClass); // TODO add
                                                                   // parameters
        status.appendLine(String.format("Running %s.java...", mainClass));

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
        StreamPiper output = new StreamPiper(p.getInputStream());
        output.start();

        // Thread that notifies GUI that the process has completed
        int statusCode = -1;
        try {
            statusCode = p.waitFor();
            String statusMessage = String.format("Machine returned %d (%s).",
                    statusCode, statusCode == 0 ? "success" : "error");
            publish(statusMessage);
        } catch (InterruptedException e) {
            publish("Machine interrupted.");
            p.destroy();
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

    /**
     * Class to handle piping an output stream of the created process into the
     * input stream of the given status console and output console
     * 
     * @author Andy Russell
     * 
     */
    class StreamPiper extends Thread {
        InputStream is;

        StreamPiper(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                while (true) {
                    // Sleep to avoid hanging the GUI with too many updates
                    // A little hack, there's probably a better solution
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // Do nothing
                    }

                    int code = br.read();

                    // End of stream
                    if (code == -1) {
                        break;
                    }

                    output.append(Character.toString((char) code));
                }

            } catch (IOException ioe) {
                status.appendLine("Error: could not read process output.");
            }
        }
    }
}
