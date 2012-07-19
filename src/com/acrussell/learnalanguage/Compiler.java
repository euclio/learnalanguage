package com.acrussell.learnalanguage;

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

    /**
     * Creates a new Compiler object
     * 
     * @param source
     *            The TextComponent that holds the source code
     * @param status
     *            The text component that should display the compiler's status
     */
    public Compiler(JTextComponent source, Console status) {
        this.source = source;
        this.status = status;
    }

    /**
     * Compiles a .java filename into a executable class
     * 
     * @param fileName
     *            The name of the .java file to be compiled
     */
    public void compile(String fileName) {
        File sourceFile = new File(fileName);

        try {
            // Writes the contents of the editor to a file on the filesystem
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(
                    sourceFile));
            source.write(fileOut);
        } catch (IOException e) {
            status.appendLine("Error: Could not write to file.");
            return;
        }

        status.append("Compiling " + fileName + "...\n");

        // Compiles the source into a .class file
        int statusCode = theCompiler.run(null, status.getStream(),
                status.getStream(), sourceFile.getName());
        if (statusCode == 0) {
            status.appendLine("Compilation successful. No errors detected.");
        } else {
            status.appendLine("Compilation failed.");
        }
    }

    /**
     * @param source
     *            The component that holds the code to be compiled
     */
    public void setSource(JTextComponent source) {
        this.source = source;
    }
}
