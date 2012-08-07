package com.acrussell.learnalanguage;

import java.awt.Font;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class Console extends JTextArea {
    private static final Font TERMINAL_FONT = new Font("Monospaced",
            Font.PLAIN, 12);

    private int maxLines = 80;

    /*
     * Writes an output stream byte by byte to the component.
     */
    private OutputStream writer = new OutputStream() {
        public void write(final int b) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    append(String.valueOf((char) b));
                }
            });
        }
    };

    /**
     * Constructs a new Console with the given rows and columns, and initializes
     * with the given text.
     * 
     * @param text
     *            The text displayed initially on the Console
     * @param rows
     *            The number of rows in the Console
     * @param cols
     *            The number of columns in the Console
     */
    public Console(String text, int rows, int cols) {
        super(rows, cols);
        this.setFont(TERMINAL_FONT);
        this.setCaretPosition(0);
        DefaultCaret caret = (DefaultCaret) this.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.setEditable(false);
        this.append(text);
    }

    /**
     * Constructs a new Console with the given rows and columns
     * 
     * @param rows
     *            The number of rows in the Console
     * @param cols
     *            The number of columns in the Console
     */
    public Console(int rows, int cols) {
        this(null, rows, cols);
    }

    /**
     * Appends the given text to the component with a trailing newline.
     * 
     * @param text
     *            The text to be added to the component
     * @see #append(String)
     */
    public void appendLine(String text) {
        append(text + "\n");
    }

    /**
     * Appends the given parameter to the component. Works similarly to
     * JTextArea's append, but it limits the amount of lines that can be written
     * to the component.
     * 
     * @param text
     *            The text to be added to the component
     */
    @Override
    public void append(String text) {
        try {
            getDocument().insertString(getDocument().getLength(), text, null);
            int lines = getDocument().getDefaultRootElement().getElementCount();

            if (lines > maxLines) {
                int firstGoodLine = lines - maxLines;

                // Delete each line above the max number of lines, oldest first
                replaceRange(null, 0, getDocument().getDefaultRootElement()
                        .getElement(firstGoodLine).getStartOffset());
            }

        } catch (BadLocationException e) {
            System.err.println("Error: Could not write to console.");
        }

        // Ensure that the cursor remains at the end of the console
        this.setCaretPosition(this.getDocument().getLength());
    }

    /**
     * @return The output stream that writes to this component
     */
    public OutputStream getStream() {
        return writer;
    }

    /**
     * @param num
     *            The max number lines this component should show
     */
    public void setMaxLines(int num) {
        this.maxLines = num;
    }
}
