import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Runner {
    private Process p;
    private volatile boolean isExecuting = false;
    private Console console, status;

    public Runner(Console console, Console status) {
        this.console = console;
        this.status = status;
    }

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

                    console.append(Character.toString((char) code));
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

    public void kill() {
        p.destroy();
        status.append("Terminated machine.");
    }
}
