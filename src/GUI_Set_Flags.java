import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * GUI to allow user to set custom flags
 * @author Helen
 *
 */
@SuppressWarnings("serial")
public class GUI_Set_Flags extends JFrame {

	private static JFrame frame;
	private JPanel contentPane;
	private JTextField txt_Filter_Text;
	private JTextField txt_Flag_Name;
	private EmailClient client;

	/**
	 * constructor to create new instance of GUI
	 * @param client EmailClient to be used to set custom flags
	 */
	public GUI_Set_Flags(final EmailClient client) {
		this.client = client;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 140);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_Filter_Text = new JLabel("Enter keywords to filter by:");
		lbl_Filter_Text.setBounds(5, 5, 164, 16);
		contentPane.add(lbl_Filter_Text);
		
		txt_Filter_Text = new JTextField();
		txt_Filter_Text.setBounds(181, 5, 239, 22);
		contentPane.add(txt_Filter_Text);
		txt_Filter_Text.setColumns(10);
		
		JLabel lbl_Flag_Name = new JLabel("Enter flag name:");
		lbl_Flag_Name.setBounds(5, 34, 125, 16);
		contentPane.add(lbl_Flag_Name);
		
		txt_Flag_Name = new JTextField();
		txt_Flag_Name.setBounds(181, 34, 239, 22);
		contentPane.add(txt_Flag_Name);
		txt_Flag_Name.setColumns(10);
		
		// create button that sets custom flags for emails
		JButton btn_Set_Flags = new JButton("Set Flags");
		btn_Set_Flags.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// create list of keywords - parse String made of comma separated words into a list
					List<String> keywords = new ArrayList<String>(Arrays.asList(txt_Filter_Text.getText().split("\\s*,\\s*")));
					client.setCustomFlags(keywords, txt_Flag_Name.getText());
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btn_Set_Flags.setBounds(5, 63, 97, 25);
		contentPane.add(btn_Set_Flags);
	}
}
