import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Runner {
    private LearningConsole output;

    public Runner(LearningConsole output) {
        this.output = output;
    }

    public void run(String className, String[] parameters) {
        try {
            System.setOut(new PrintStream(output.getStream()));
            System.setErr(new PrintStream(output.getStream()));
            System.out.println("\nRunning " + className + ".java...");
            System.out.println();
            URLClassLoader loader = null;
            File f = new File(System.getProperty("user.dir"));
            try {
                loader = new URLClassLoader(new URL[] { f.toURI().toURL() });
            } catch (MalformedURLException e1) {
                System.out.println("ERROR: File not found");
            }
            Class<?> theClass = loader.loadClass(className);
            try {
                Method main = theClass.getMethod("main", String[].class);
                try {
                    main.invoke(null, new Object[] { parameters });
                } catch (IllegalAccessException e) {
                    System.err.println("ERROR: " + e.getMessage());
                } catch (InvocationTargetException e) {
                    System.err.println("ERROR: " + e.getMessage());
                }
            } catch (NoSuchMethodException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
