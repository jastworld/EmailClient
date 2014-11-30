import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;


import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

/**
 * GUI to send an email
 * @author Helen
 *
 */
public class GUI_Send_Mail {

	private JFrame frame;
	private JTextField txt_To;
	private JTextField txt_Subject;
	private JTextArea txt_Attachments;
	private JTextArea txt_Body;
	private static ArrayList<File> lst_Attachments;
	private static EmailClient client;
	private JTextField txt_Cc;
	private JTextField txt_Bcc;

	/**
	 * constructor - creates instance of send mail window
	 * @param client EmailClient to send mail through
	 */
	public GUI_Send_Mail(EmailClient client) {
		GUI_Send_Mail.client = client;
		lst_Attachments = new ArrayList<File>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrame(new JFrame());
		getFrame().setBounds(100, 100, 450, 451);
		getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getFrame().getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		getFrame().getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lbl_To = new JLabel("To:");
		lbl_To.setBounds(12, 13, 56, 16);
		panel.add(lbl_To);
		
		txt_To = new JTextField();
		txt_To.setBounds(80, 10, 340, 22);
		panel.add(txt_To);
		txt_To.setColumns(10);
		
		JLabel lbl_Cc = new JLabel("CC:");
		lbl_Cc.setBounds(12, 42, 56, 16);
		panel.add(lbl_Cc);
		
		JLabel lbl_Subject = new JLabel("Subject:");
		lbl_Subject.setBounds(12, 129, 56, 16);
		panel.add(lbl_Subject);
		
		txt_Subject = new JTextField();
		lbl_Subject.setLabelFor(txt_Subject);
		txt_Subject.setBounds(80, 126, 340, 22);
		panel.add(txt_Subject);
		txt_Subject.setColumns(10);
		
		JLabel lbl_Body = new JLabel("Body:");
		lbl_Body.setBounds(12, 158, 56, 16);
		panel.add(lbl_Body);
		
		JLabel lbl_Attachments = new JLabel("Attachments:");
		lbl_Attachments.setBounds(12, 281, 80, 16);
		panel.add(lbl_Attachments);
		
		// use JFileChooser to select attachments for email - add them to ArrayList of files to be passed into sendMail method
		JButton btn_Attachments = new JButton("+");
		btn_Attachments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		          File selectedFile = fileChooser.getSelectedFile();
		          lst_Attachments.add(selectedFile);
		          txt_Attachments.append(selectedFile.getName() + "; ");
		        }
			}
		});

		btn_Attachments.setBounds(373, 281, 47, 53);
		panel.add(btn_Attachments);

		// send button - runs sendMail method from EmailClient
		JButton btn_Send = new JButton("Send");
		btn_Send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					for(int i = 0; i < lst_Attachments.size(); i++) {
						System.out.println(lst_Attachments.get(i));
					}
					client.sendMail(txt_To.getText(), txt_Subject.getText(), txt_Body.getText(), txt_Cc.getText(), txt_Bcc.getText(), lst_Attachments);
				} catch (IOException e) {
					e.printStackTrace();
				}
				getFrame().dispose();
			}
		});
		btn_Send.setBounds(12, 346, 200, 50);
		panel.add(btn_Send);
		
		txt_Attachments = new JTextArea();
		txt_Attachments.setEditable(false);
		txt_Attachments.setBounds(103, 229, 258, 50);
		panel.add(txt_Attachments);
		
		JScrollPane scr_Attachments = new JScrollPane(txt_Attachments);
		scr_Attachments.setBounds(104, 283, 257, 50);
		panel.add(scr_Attachments);
		
		txt_Cc = new JTextField();
		lbl_Cc.setLabelFor(txt_Cc);
		txt_Cc.setBounds(80, 39, 340, 22);
		panel.add(txt_Cc);
		txt_Cc.setColumns(10);
		
		JLabel lbl_Bcc = new JLabel("BCC:");
		lbl_Bcc.setBounds(12, 71, 56, 16);
		panel.add(lbl_Bcc);
		
		txt_Bcc = new JTextField();
		txt_Bcc.setBounds(80, 68, 340, 22);
		panel.add(txt_Bcc);
		txt_Bcc.setColumns(10);
		
		txt_Body = new JTextArea();
		txt_Body.setLineWrap(true);
		txt_Body.setBounds(80, 161, 340, 115);
		panel.add(txt_Body);
				
	}

	/**
	 * @return frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
