import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.tools.*;

public class Compiler {
    // private OutputStream errorStream = null;
    private JEditorPane source;
    private JTextArea output;
    private boolean isCompiled = false;
    private JavaCompiler theCompiler = ToolProvider.getSystemJavaCompiler();
    private OutputStream errorStream = new OutputStream() {
        public void write(int b) {
            output.append(String.valueOf((char) b));
        }
    };

    public Compiler() {

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

        output.append("\nCompiling " + fileName + "...\n");
        int status = theCompiler.run(null, null, errorStream,
                sourceFile.getName());
        if (status == 0) {
            output.append("Compilation successful. No errors detected.");
            isCompiled = true;
        } else {
            output.append("Compilation failed.");
        }
    }

    public void setOutput(JTextArea output) {
        this.output = output;
    }

    public void setSource(JEditorPane source) {
        this.source = source;
    }
}
