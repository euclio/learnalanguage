import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class LearningConsole extends JTextArea {
    private OutputStream writer = new OutputStream() {
        public void write(final int b) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        LearningConsole.this.append(String.valueOf((char) b));
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            int lines = getDocument().getDefaultRootElement().getElementCount();
            if (lines > 80) {
                replaceRange(null, 0, getDocument().getDefaultRootElement()
                        .getElement(1).getStartOffset());
            }

        }
    };

    public LearningConsole(String text, int rows, int cols) {
        super(rows, cols);
        DefaultCaret caret = (DefaultCaret) this.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.append(text);
    }

    public void append(String text) {
        append(text, null);
    }

    public void append(String text, AttributeSet a) {
        try {
            getDocument().insertString(getDocument().getLength(), text, a);
            int lines = getDocument().getDefaultRootElement().getElementCount();
            if (lines > 80) {
                replaceRange(null, 0, getDocument().getDefaultRootElement()
                        .getElement(1).getStartOffset());
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public OutputStream getStream() {
        return writer;
    }
}
