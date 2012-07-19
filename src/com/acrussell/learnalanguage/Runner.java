package com.acrussell.learnalanguage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.prefs.Preferences;

public class Runner {
    private Preferences prefs = Preferences.userNodeForPackage(this.getClass());
    private Process p;
    private volatile boolean isExecuting = false;
    private Console terminal, status;

    /**
     * Creates an object to handle running an executable Java process.
     * 
     * @param terminal
     * @param status
     */
    public Runner(Console terminal, Console status) {
        this.terminal = terminal;
        this.status = status;
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

                    terminal.append(Character.toString((char) code));
                }

            } catch (IOException ioe) {
                status.appendLine("Error: could not read process output.");
            }
        }
    }

    public boolean isExecuting() {
        return isExecuting;
    }

    public void execute(String className, String[] parameters) {
        // Create new process to execute user code
        ProcessBuilder pb = new ProcessBuilder("java", className);
        status.appendLine("Running " + className + ".java...");

        try {
            p = pb.start();
        } catch (IOException ioe) {
            status.appendLine("Error: Could not access the program.");
            return;
        }

        // Clear the terminal at each execution if the user desires
        if (prefs.getBoolean("terminal_should_clear", false)) {
            terminal.setText("");
        }

        isExecuting = true;

        // Thread that notifies GUI that the process has completed
        (new Thread() {
            public void run() {
                int statusCode = -1;
                try {
                    statusCode = p.waitFor();
                } catch (InterruptedException e) {
                    status.appendLine("Error: Machine monitor interrupted.");
                    kill();
                    statusCode = 1;
                } finally {
                    isExecuting = false;
                    String statusMessage = "Machine returned " + statusCode
                            + (statusCode == 0 ? " (success)" : " (error)")
                            + ".";
                    status.appendLine(statusMessage);
                }
            }
        }).start();

        // Send process output to console
        StreamPiper output = new StreamPiper(p.getInputStream());
        output.start();

    }

    /**
     * Kills the current user program
     */
    public void kill() {
        p.destroy();
        status.append("Terminated machine.");
    }
}
