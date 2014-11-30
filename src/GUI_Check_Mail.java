import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * GUI to display all emails from inbox
 * @author Helen
 *
 */
public class GUI_Check_Mail {

	private static EmailClient client;
	private JFrame frame;
	private JTable tbl_Emails;


	/**
	 * Create the application.
	 * @param client EmailClient to check mail through
	 * @throws MessagingException 
	 */
	public GUI_Check_Mail(EmailClient client) throws MessagingException {
		GUI_Check_Mail.client = client;
		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 * @throws MessagingException 
	 */
	private void initialize() throws MessagingException {
		setFrame(new JFrame());
		getFrame().setBounds(100, 100, 450, 300);
		getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);
		
		String[] colNames = {"Message", "Subject", "Flag"};
		DefaultTableModel model = new DefaultTableModel(colNames, 0); 
		tbl_Emails = new JTable(model);
		tbl_Emails.setBounds(12, 13, 408, 229);
		// populate JTable with message information
		try {
			for(int i = 0; i < client.getMail().length; i++) {
				Message message = client.getMail()[i];
				Object[] row = {message, message.getSubject(), client.readFlag(message)};
				model.insertRow(tbl_Emails.getRowCount(), row);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		// remove dummy column containing message object - want it there but don't need to see it
		tbl_Emails.removeColumn(tbl_Emails.getColumnModel().getColumn(0));
		
		// when double click on row, open email
		tbl_Emails.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        if (me.getClickCount() == 2) {
		        	try {
						GUI_Show_Mail mailFrame = new GUI_Show_Mail(client, "" + InternetAddress.toString(((Message) tbl_Emails.getModel().getValueAt(row, 0)).getFrom()), "" + tbl_Emails.getModel().getValueAt(row, 1), "" + client.readBody((Message) tbl_Emails.getModel().getValueAt(row, 0)));
						mailFrame.setVisible(true);
						// set flag to read
						((Message) tbl_Emails.getModel().getValueAt(row, 0)).setFlag(Flags.Flag.SEEN, true);
						getFrame().dispose();
					} catch (Exception e) {
						e.printStackTrace();
					}
		        }
		    }
		});
		
		getFrame().getContentPane().add(tbl_Emails);
		
		// attach table to scroll pane to make it scrollable
		JScrollPane scrollPane = new JScrollPane(tbl_Emails);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(12, 13, 408, 229);
		getFrame().getContentPane().add(scrollPane);
	}

	/**
	 * 
	 * @return frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * 
	 * @param frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
