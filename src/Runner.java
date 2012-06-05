import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Runner {
    private LearningConsole output;
    private Process p;
    private volatile boolean isExecuting = false;

    public Runner(LearningConsole output) {
        this.output = output;
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
                String line;
                while ((line = br.readLine()) != null)
                    System.out.println(line);

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public boolean isExecuting() {
        return isExecuting;
    }

    public void execute(String className, String[] parameters) {
        ProcessBuilder pb = new ProcessBuilder("java", className);
        System.setOut(new PrintStream(output.getStream()));
        System.setErr(new PrintStream(output.getStream()));
        System.out.println("\nRunning " + className + ".java...");
        System.out.println();
        try {
            p = pb.start();
            isExecuting = true;
            (new Thread() {
                public void run() {
                    int statusCode = -1;
                    try {
                        statusCode = p.waitFor();
                    } catch (InterruptedException e) {
                        System.out
                                .println("Error: Machine monitor interrupted.");
                    } finally {
                        isExecuting = false;
                        String status = "\nMachine returned " + statusCode
                                + (statusCode == 0 ? " (success)" : " (error)")
                                + ".";
                        System.out.println(status);
                    }
                }
            }).start();
            StreamPiper output = new StreamPiper(p.getInputStream());
            output.start();
        } catch (IOException e) {
            System.out.println("Error in execution: " + e.getMessage());
        }
    }

    public void stop() {
        p.destroy();
        System.out.println("Terminated machine.");
    }
}
