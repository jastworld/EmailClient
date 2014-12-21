import java.awt.EventQueue;

import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

/**
 * GUI for the main menu - runnable
 * @author Helen
 *
 */
public class GUI_Main_Menu {
	
	private static EmailClient client;

	private JFrame frmEmailClient;

	/**
	 * Launch the application.
	 * @throws MessagingException 
	 */
	public static void main(String[] args) throws MessagingException {
		client = new EmailClient();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_Main_Menu window = new GUI_Main_Menu();
					window.frmEmailClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

   /**
 	* constructor
 	*/
	public GUI_Main_Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEmailClient = new JFrame();
		frmEmailClient.setTitle("Email Client");
		frmEmailClient.setBounds(100, 100, 450, 300);
		frmEmailClient.setResizable(false);
		frmEmailClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmEmailClient.getContentPane().add(panel, BorderLayout.CENTER);
		
		// create send mail button to open GUI to send mail
		JButton btn_SendMail = new JButton("Send Mail");
		btn_SendMail.setBounds(167, 78, 124, 50);
		btn_SendMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					GUI_Send_Mail window = new GUI_Send_Mail(client);
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		panel.setLayout(null);
		panel.add(btn_SendMail);
		
		// create check mail button to open GUI to check mail
		JButton btn_CheckMail = new JButton("Check Mail");
		btn_CheckMail.setBounds(167, 141, 124, 50);
		btn_CheckMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					GUI_Check_Mail window = new GUI_Check_Mail(client);
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		panel.add(btn_CheckMail);
		
		// create set flags button to open GUI to set custom flags
		JButton btn_SetFlags = new JButton("Set Flags");
		btn_SetFlags.setBounds(167, 204, 124, 50);
		btn_SetFlags.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					GUI_Set_Flags frame = new GUI_Set_Flags(client);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		panel.add(btn_SetFlags);
		
		JLabel lbl_Title = new JLabel("Select an option...");
		lbl_Title.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lbl_Title.setBounds(12, 13, 420, 52);
		panel.add(lbl_Title);
	}
}
