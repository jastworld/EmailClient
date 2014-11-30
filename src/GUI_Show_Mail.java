import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;


@SuppressWarnings("serial")
public class GUI_Show_Mail extends JFrame {

	private JPanel contentPane;
	private JTextField txt_From;
	private JTextField txt_Subject;
	private static String from;
	private static String subject;
	private static String body;
	private EmailClient client;

	/**
	 * constructor - creates new instance of GUI
	 * @param client EmailClient to show email from
	 * @param from Who the email is from
	 * @param subject The subject of the email
	 * @param body The body of the email
	 */
	public GUI_Show_Mail(final EmailClient client, String from, String subject, String body) {
		this.client = client;
		setTitle("Email: " + subject);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onClose();
			}
		});
		
		JLabel lbl_From = new JLabel("From:");
		lbl_From.setBounds(12, 13, 56, 16);
		contentPane.add(lbl_From);
		
		txt_From = new JTextField();
		lbl_From.setLabelFor(txt_From);
		txt_From.setBounds(80, 10, 340, 22);
		contentPane.add(txt_From);
		txt_From.setColumns(10);
		txt_From.setText(from);
		
		JLabel lbl_Subject = new JLabel("Subject:");
		lbl_Subject.setBounds(12, 42, 56, 16);
		contentPane.add(lbl_Subject);
		
		txt_Subject = new JTextField();
		lbl_Subject.setLabelFor(txt_Subject);
		txt_Subject.setBounds(80, 39, 340, 22);
		contentPane.add(txt_Subject);
		txt_Subject.setColumns(10);
		txt_Subject.setText(subject);
		
		JLabel lbl_Body = new JLabel("Body:");
		lbl_Body.setBounds(12, 71, 56, 16);
		contentPane.add(lbl_Body);
		
		JTextArea txt_Body = new JTextArea();
		txt_Body.setLineWrap(true);
		lbl_Body.setLabelFor(txt_Body);
		txt_Body.setBounds(80, 74, 340, 168);
		contentPane.add(txt_Body);
		txt_Body.setText(body);
		
		JScrollPane scrollPane = new JScrollPane(txt_Body);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		DefaultCaret caret = (DefaultCaret)txt_Body.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		scrollPane.setBounds(80, 71, 340, 171);
		contentPane.add(scrollPane);
	}
	
	private void onClose() {
		try {
			GUI_Check_Mail check = new GUI_Check_Mail(client);
			check.getFrame().setVisible(true);
			dispose();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
}
