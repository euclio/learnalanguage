import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.tools.*;

public class Compiler {
    private JEditorPane source;
    private LearningConsole output;
    private boolean isCompiled = false;
    private JavaCompiler theCompiler = ToolProvider.getSystemJavaCompiler();
    
    public Compiler(JEditorPane source, LearningConsole output) {
        this.source = source;
        this.output = output;
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
        int status = theCompiler.run(null, null, output.getStream(),
                sourceFile.getName());
        if (status == 0) {
            output.append("Compilation successful. No errors detected.");
            isCompiled = true;
        } else {
            output.append("Compilation failed.");
        }
    }

    public void setSource(JEditorPane source) {
        this.source = source;
    }
}
