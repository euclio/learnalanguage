import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class LearningConsole extends JTextArea {
    private OutputStream writer = new OutputStream() {
        StringBuffer buff = new StringBuffer();

        public void write(int b) {
            if ((char) b == '\r')
                return;
            if ((char) b == '\n') {
                final String text = buff.toString() + '\n';
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        LearningConsole.this.append(text);
                    }
                });
                buff.setLength(0);
                return;
            }

            buff.append((char) b);
        }
    };

    public LearningConsole(String text, int rows, int cols) {
        super(rows, cols);
        DefaultCaret caret = (DefaultCaret) this.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.append(text);
    }

    public OutputStream getStream() {
        return writer;
    }
}
