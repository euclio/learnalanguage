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

    private OutputStream writer = new OutputStream() {
        public void write(final int b) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        append(String.valueOf((char) b));
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    };

    public Console(String text, int rows, int cols) {
        super(rows, cols);
        this.setFont(TERMINAL_FONT);
        this.setCaretPosition(0);
        DefaultCaret caret = (DefaultCaret) this.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.setEditable(false);
        this.append(text);
    }

    public Console(int rows, int cols) {
        this(null, rows, cols);
    }
    
    public void appendLine (String text) {
        append(text + "\n");
    }

    public void append(String text) {
        try {
            getDocument().insertString(getDocument().getLength(), text, null);
            int lines = getDocument().getDefaultRootElement().getElementCount();
            if (lines > maxLines) {
                int firstGoodLine = lines - maxLines;
                
                replaceRange(null, 0, getDocument().getDefaultRootElement()
                        .getElement(firstGoodLine).getStartOffset());
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public OutputStream getStream() {
        return writer;
    }

    public void setMaxLines(int num) {
        this.maxLines = num;
    }
}
