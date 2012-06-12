import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.text.JTextComponent;
import javax.tools.*;

public class Compiler {
    private JTextComponent source;
    private Console status;
    private JavaCompiler theCompiler = ToolProvider.getSystemJavaCompiler();

    public Compiler(JTextComponent source, Console status) {
        this.source = source;
        this.status = status;
    }

    public void compile(String fileName) {
        File sourceFile = new File(fileName);

        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(
                    sourceFile));
            source.write(fileOut);
        } catch (IOException e) {
            status.appendLine("Error: Could not write to file.");
            return;
        }

        status.append("Compiling " + fileName + "...\n");
        int statusCode = theCompiler.run(null, System.out, System.err,
                sourceFile.getName());
        if (statusCode == 0) {
            status.appendLine("Compilation successful. No errors detected.");
        } else {
            status.appendLine("Compilation failed.");
        }
    }

    public void setSource(JTextComponent source) {
        this.source = source;
    }
}
