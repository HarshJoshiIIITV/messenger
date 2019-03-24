package messanger;

import java.io.*;
import java.net.*; 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Server extends JFrame {  /*for all the GUI craps we have to extend JFrame */
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;   /*this Stream has message that we sent to Client */
	private ObjectInputStream input;  /* this Stream has message that we receive ie message sent by Client */
	private ServerSocket server;  /* A server that wants that everyone(Client) can connect to my PC */
	private Socket connection;   /*setup the connection b/w my computer and another person's computer */

	
	
	//constructor and GUI
	
	public Server() {
		super("HARSH'S MESSENGER");
		userText=new JTextField();
		userText.setEditable(false);  /*This ensures that we can't type anything(NOT EDITABLE) before we connect  */ 
		userText.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					sendMessage(event.getActionCommand()); /* Sends the message  that we write and getActionCommand returns string of what we write */
					userText.setText("");   /* So that text area is cleared as we send our message */
		}});
		
		add(userText,BorderLayout.NORTH);
		chatWindow=new JTextArea();   /* In chatwindow all messages is seen */
		add(new JScrollPane(chatWindow));
		setSize(300,150);
		setVisible(true);
	}
	
	
	
	
	//setting up and run the server
	
	public void startRunning() {
		try {
			server =new ServerSocket(6789,100); /*setup the server with portno. and number of backlog */
			while(true) {
				try {
					waitForConnection();  /*waiting for connection */
					setupStreams();   /*set the output and input streams as we connect */
					whileChatting(); /*now send and receive messages as we completely connect */
					
				}catch(EOFException eofException) {
					showMessage("\n Server ended the connection ! ");
				}finally {
					CloseCrap();
				}
			}
			
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}	
	}
	
	
	
	
	//wait for connection
	private void waitForConnection() throws IOException{
		showMessage("waiting for someone to connect .....\n");
		connection=server.accept();  // when we get a server that accepts the connection
		showMessage("Now connected to "+connection.getInetAddress().getHostName()); //returns the IPaddress as string		
	}
	
	
	
	//setting up the streams for sending and receiving data
	private void setupStreams() throws IOException{
		output=new ObjectOutputStream(connection.getOutputStream()); //creating a pathway that allows us to connect to a computer
		output.flush(); //for clearing the buffer 
		input=new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now setup! \n");
	}
	
	
	//during the chat conversation
	
	private void whileChatting() throws IOException {
		String message ="you are now connected !";
		sendMessage(message);
		abletoType(true);   //allows user to type stuff into the text box
		do {
			try {
				message=(String) input.readObject(); //read message which  is comingup and convert it to a  String
				showMessage("\n" + message);
				
			}catch(ClassNotFoundException classNotFoundException) {
				showMessage("\n idk wtf that user send !");
		}
	}while(!message.equals("CLIENT - END")); //if user types END than program gonna be stop
	}	
	
	
	
	
	
	// Close streams and sockets after you are done with chatting
	private void CloseCrap() {
			showMessage("\n Closing connections... \n");
			abletoType(false); //now you can't type anymore
			try {
				output.close();      //closing all inputs and outputs ad connections
				input.close();
				connection.close();
			}catch(IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
		
		
		
		//send a message to client
		
		private void sendMessage(String message) {
			try {
				output.writeObject("SERVER -  "+message); //create a object and send message to output stream 
				output.flush(); 
				showMessage("\n SERVER -"+message); 
			}catch(IOException ioException) {  
				chatWindow.append("\n ERROR: DUDE I CAN'T SEND THAT MESSAGE");
			}
		}
		
		
		
		
		//updates chatwindow
		private void showMessage(final String text) {
			SwingUtilities.invokeLater(    //allows to create a thread that can update the GUI
					new Runnable() {    //so synthesis the thread here
						public void run() {
							chatWindow.append(text);
						}
					}
					);
		}
		
		
		
		//let the user type stuff into their box
		private void abletoType(final boolean tof) {  //tof is variable which is wither true or false
			SwingUtilities.invokeLater(    
					new Runnable( ) {
						 public void run() {
							 userText.setEditable(tof);   
						 }
						
					});
		}
		
		
		
		
		
		
}
