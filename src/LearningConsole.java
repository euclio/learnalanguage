import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class LearningConsole extends JTextArea {
    private OutputStream writer = new OutputStream() {
        public void write(int b) {
            LearningConsole.this.append(String.valueOf((char) b));
        }
    };

    public LearningConsole(String text, int rows, int cols) {
        super(rows, cols);
        DefaultCaret caret = (DefaultCaret)this.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.append(text);
    }

    public OutputStream getStream() {
        return writer;
    }
}
