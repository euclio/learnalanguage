import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Runner {
    private class RunnerThread extends Thread {
        private File f;
        private Class<?> theClass;
        private Method main;

        private String[] parameters;

        public RunnerThread(String className, String[] parameters) {
            System.setOut(new PrintStream(output.getStream()));
            System.setErr(new PrintStream(output.getStream()));
            System.out.println("\nRunning " + className + ".java...");
            System.out.println();

            f = new File(System.getProperty("user.dir"));

            URLClassLoader loader = null;
            try {
                loader = new URLClassLoader(new URL[] { f.toURI().toURL() });
            } catch (MalformedURLException e) {
                System.err
                        .println("Error: There was an error finding the current directory.");
            }

            try {
                theClass = loader.loadClass(className);
            } catch (ClassNotFoundException e) {
                System.err.println("Error: The class could not be found.");
            }

            try {
                main = theClass.getMethod("main", String[].class);
            } catch (NoSuchMethodException e) {
                System.err.println("Error: 'main' method not found.");
            }

            this.parameters = parameters;
        }

        public void run() {
            try {
                main.invoke(null, new Object[] { parameters });
            } catch (IllegalAccessException e) {
                System.err.println("Error: Access to this method is denied.");
            } catch (InvocationTargetException e) {
                System.err.println("ERROR: The program terminated with:\n"
                        + e.getMessage());
            }
        }
    }

    private LearningConsole output;

    public Runner(LearningConsole output) {
        this.output = output;
    }

    public void execute(String className, String[] parameters) {
        new RunnerThread(className, parameters).start();
    }
}
