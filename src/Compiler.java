import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.tools.*;

public class Compiler {
    private JEditorPane source;
    private boolean isCompiled = false;
    private JavaCompiler theCompiler = ToolProvider.getSystemJavaCompiler();

    public Compiler(JEditorPane source) {
        this.source = source;
    }

    public void setCompiled(boolean state) {
        isCompiled = true;
    }

    public void compile(String fileName) {
        File sourceFile = new File(fileName);

        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(
                    sourceFile));
            source.write(fileOut);
        } catch (IOException e) {
            // Do nothing
        }

        System.out.println("\nCompiling " + fileName + "...\n");
        int status = theCompiler.run(null, System.out, System.err,
                sourceFile.getName());
        if (status == 0) {
            System.out.println("Compilation successful. No errors detected.");
            isCompiled = true;
        } else {
            System.out.println("Compilation failed.");
        }
    }

    public void setSource(JEditorPane source) {
        this.source = source;
    }
}
