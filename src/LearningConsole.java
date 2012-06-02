import java.io.OutputStream;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class LearningConsole extends JTextArea {
    private OutputStream writer = new OutputStream() {
        public void write(int b) {
            LearningConsole.this.append(String.valueOf((char) b));
        }
    };

    public LearningConsole(String text, int rows, int cols) {
        super(text, rows, cols);
    }

    public OutputStream getStream() {
        return writer;
    }
}
