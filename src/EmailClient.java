import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.sun.mail.imap.IMAPFolder;


public class EmailClient {

	private IMAPFolder folder;
	private Store store;
	private String username;
	private String password;
	private String smtphost;
	private Session session;
	private HashMap<Flags, String> customFlags;
	
	/**
	 * Constructor to set up connection to email server
	 * Adapted from code written by Dr Shan He
	 * @throws MessagingException
	 */
	public EmailClient() throws MessagingException {
		// setup logins and IMAP/SMTP properties
		
		customFlags = new HashMap<Flags, String>();
		
		folder = null;
		store = null;
		
		username = "sscjavamail@gmail.com";
		password = "";
		smtphost = "smtp.gmail.com";

		// Step 1.1: Set all Properties
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtphost);
		props.put("mail.smtp.port", "587");
		
		JPasswordField pwd = new JPasswordField(10);  
		int action = JOptionPane.showConfirmDialog(null, pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION);  
		if(action < 0) {
			JOptionPane.showMessageDialog(null,"Cancel, X or escape key selected"); 
			System.exit(0); 
		}
		else 
			password = new String(pwd.getPassword());  
		
		// Set Property with username and password for authentication  
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);

		//Step 1.2: Establish a mail session (java.mail.Session)
		session = Session.getDefaultInstance(props);
		// Step 2: Get the Store object from the mail session
		// A store needs to connect to the IMAP server  
		store = session.getStore("imaps");
		store.connect("imap.googlemail.com",username, password);
		
		// Step 3: Choose a folder, in this case, we chose your inbox
		folder = (IMAPFolder) store.getFolder("inbox"); 
	}
	
	/**
	 * Sends an email
	 * @param to The address of the recipient
	 * @param subject The subject of the email
	 * @param body The body of the email
	 * @param cc The address of the cc'd recipients
	 * @param bcc The address of the bcc'd recipients
	 * @param attachments The ArrayList of Files to be attached
	 * @throws IOException
	 */
	public void sendMail(String to, String subject, String body, String cc, String bcc, ArrayList<File> attachments) throws IOException {
		try {

			// Step 3: Create a message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
			message.setSubject(subject);
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			MimeMultipart multipart = new MimeMultipart();
			messageBodyPart.setText(body);
			multipart.addBodyPart(messageBodyPart);
			//add attachments
			if (attachments.size() > 0) {
				for(File file:attachments) {
					MimeBodyPart messageBodyPart2 = new MimeBodyPart();
					String filename = file.getPath();
					DataSource source = new FileDataSource(filename);
					messageBodyPart2.setDataHandler(new DataHandler(source));
					messageBodyPart2.setFileName(filename);
					multipart.addBodyPart(messageBodyPart2);
				}
			}
			message.setContent(multipart);
			message.saveChanges();

			// Step 4: Send the message by javax.mail.Transport
			Transport tr = session.getTransport("smtp");	// Get Transport object from session		
			tr.connect(smtphost, username, password); // We need to connect
			tr.sendMessage(message, message.getAllRecipients()); // Send message


			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * retrieves all email from inbox
	 * @return messages An array of Messages in the inbox
	 * @throws MessagingException
	 */
	public Message[] getMail() throws MessagingException {
		// Step 4: Open the folder
		if(!folder.isOpen()) {
			folder.open(Folder.READ_WRITE);
		}
		
		Message[] messages = new Message[folder.getMessageCount()];
		
		// add all messages to array
		try
		{		
			for(int i = 0; i < folder.getMessageCount(); i++) {
				messages[i] = folder.getMessage(i + 1);
			}
			
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} 
		
		return messages;
	}
	
	/**
	 * Sets up custom flags
	 * @param keywords List of keywords to be checked for in the body of the email
	 * @param flagName Name of the new flag to be created
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void setCustomFlags(List<String> keywords, String flagName) throws MessagingException, IOException {
		// add flag to HashMap of custom flags
		customFlags.put(new Flags(flagName), flagName);
		Message[] messages = getMail();
		// check each message for any of the keywords - if it contains the keyword then change its flag
		for(int i = 0; i < messages.length; i++) {
			for(int j = 0; j < keywords.size(); j++) {
				if (readBody(messages[i]).contains(keywords.get(j))) {
					messages[i].setFlags(new Flags(flagName), true);
				}
			}
		}
	}
	
	/**
	 * reads body of email
	 * @param p Part (Message) to read
	 * @return body of email as String
	 * @throws IOException
	 * @throws MessagingException
	 */
	public String readBody(Part p) throws IOException, MessagingException {
		// read TEXT/PLAIN body content
		if (p.getContentType().contains("TEXT/PLAIN")) {
			return p.getContent().toString();
		}
		// read MIME body content
		else if (p.getContent() instanceof Multipart)
		{
			String body = "";
			// How to get parts from multiple body parts of MIME message
			Multipart multipart = (Multipart) p.getContent();
			for (int x = 0; x < multipart.getCount(); x++) {
				BodyPart bodyPart = multipart.getBodyPart(x);
				// If the part is a plan text message, then print it out.
				if(bodyPart.getContentType().contains("TEXT/PLAIN")) 
				{
					body += bodyPart.getContent().toString();
				}

			}
			return body;
		}
		return "";
	}
	
	/**
	 * Closes connection to email server
	 * @throws MessagingException
	 */
	public void closeConnection() throws MessagingException {
		if (folder != null && folder.isOpen()) { folder.close(true); }
		if (store != null) { store.close(); }
	}
	
	/**
	 * reads email flag into String
	 * @param m Message to read flag from
	 * @return String equivalent of email's flag
	 * @throws MessagingException
	 */
	public String readFlag(Message m) throws MessagingException {
		
		// try custom flags first
		for (HashMap.Entry<Flags, String> entry : customFlags.entrySet())
		{
			if(m.getFlags().contains(entry.getKey())) {
				return entry.getValue();
			}
		}
		
		if(m.getFlags().contains(Flags.Flag.ANSWERED)) {
			return "Answered";
		}
		
		else if(m.getFlags().contains(Flags.Flag.DELETED)) {
			return "Deleted";
		}
		
		else if(m.getFlags().contains(Flags.Flag.DRAFT)) {
			return "Draft";
		}
		
		else if(m.getFlags().contains(Flags.Flag.FLAGGED)) {
			return "Flagged";
		}
		
		else if(m.getFlags().contains(Flags.Flag.RECENT)) {
			return "Recent";
		}
		
		else if(m.getFlags().contains(Flags.Flag.SEEN)) {
			return "Seen";
		}
		
		else if(m.getFlags().contains(Flags.Flag.USER)) {
			return "User";
		}
		
		else {
			return "New";
		}
	}
	
	
	
}
