
import java.io.* ;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;







public class Client extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message ="";
	private String serverIP;
	private Socket connection;
	
	public Client(String host) {
		super("Client mofo!");
		serverIP=host;
		userText=new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				});
		
		add(userText,BorderLayout.NORTH);
		chatWindow=new JTextArea();
		add(new JScrollPane(chatWindow),BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}
	
	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eofException) {
			showMessage("\n Client terminated connection");
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}finally {
			closeCrap();
		}
	}
	
	private void connectToServer()throws IOException{
		showMessage("Attempting connection....\n");
		connection=new Socket(InetAddress.getByName(serverIP),6789);
		showMessage("connected to:"+connection.getInetAddress().getHostName());
	}
	
	
	
	private void setupStreams() throws IOException{
		output =new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input=new ObjectInputStream(connection.getInputStream());
		showMessage("\n Dude your streams ae now good to go! \n");
	}
	
	private void whileChatting() throws IOException {
		abletoType(true);
		do {
			try {
				message=(String) input.readObject();
				showMessage("\n" + message);
				
			}catch(ClassNotFoundException classNotFoundException) {
				showMessage("\n I dont know that object type");
		}
		}while(!message.equals("SERVER - END"));
	
	}
	
	
	private void closeCrap() {
		showMessage("\n closing crap down.....");
		abletoType(false);
	
	try {
		output.close();
		input.close();
		connection.close();
	}catch(IOException ioException) {
		ioException.printStackTrace();
	}}
	
	
	
	private void sendMessage(String message) {
		try {
			output.writeObject("CLIENT -"+message);
			output.flush();
			showMessage("\nCLIENT-"+message);
		}catch(IOException ioException) {
			chatWindow.append("\n something messed up sending message hoss!");
		}
	}
	
	private void showMessage(final String m) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						chatWindow.append(m);
					}
				});
	}
	
	private void abletoType(final boolean tof) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						userText.setEditable(tof);
					}
				});
	}
	
	
	
	
	
	
	}
	
	
