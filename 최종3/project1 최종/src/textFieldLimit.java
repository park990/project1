import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class textFieldLimit extends PlainDocument {
    private int limit;

    public textFieldLimit(int n) {

        this.limit = n;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) return;

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
        // 4자리 넘으면 무시됨
    }
}
