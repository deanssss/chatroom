import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class Pal extends JPanel {

	/**
	 * Create the panel.
	 */
	public Pal() {
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblDeanssss = new JLabel("deanssss    2017-06-26 12:36:21");
		lblDeanssss.setForeground(new Color(0, 0, 255));
		lblDeanssss.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		add(lblDeanssss, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JTextArea textArea = new JTextArea();
		textArea.setBorder(UIManager.getBorder("Button.border"));
		textArea.setEnabled(false);
		textArea.setEditable(false);
		textArea.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		textArea.setText("\u6211\u542C\u8BF4\u8BFE\u7A0B\u8BBE\u8BA1\u4E0D\u9A8C\u6536\u4E86\uFF0C\u8001\u5E08\u76F4\u63A5\u7ED9\u6EE1\u5206\uFF01");
		panel.add(textArea);
	}

}
