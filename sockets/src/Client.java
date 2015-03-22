import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

//version 4

public class Client extends JFrame implements ActionListener, Runnable{
	
	private static final long serialVersionUID = 980389841528802556L;
	
	// define the user interface components
	JTextField chatInput = new JTextField(50);
	JTextArea chatHistory = new JTextArea(9,40);
	JButton chatMessage = new JButton("Send");
	JButton exit = new JButton("Exit");
	JPanel inputBar = new JPanel();
	JPanel topPanel = new JPanel();
			
	// define the socket and io streams
	Socket client;
	DataInputStream dis;
	DataOutputStream dos;
	
	public Client()
	{
		// create the user interface and setup an action listener linked to the send message button
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout(1,1));
				 
		//TODO develop prompt to allow a user (client) to enter the nickname / handle for their client suggest using JOptionPane
		String userName = JOptionPane.showInputDialog(null, "Welcome to the Chat Service! \nPlease input your user name.", "User Registration", JOptionPane.PLAIN_MESSAGE);
		setTitle(userName);
		
		//changed input to userName
		
		//TODO add in extra user interface components to allow a user to select the remote client that they want to send a message to suggest using a JList
		ArrayList users = new ArrayList();
		users.add(userName);
		JList list = new JList(users.toArray());
		
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setPreferredSize(new Dimension(250,80));
				
		topPanel.add(new JScrollPane(chatHistory),BorderLayout.CENTER);
		topPanel.add(new JScrollPane(list),BorderLayout.EAST);
		contentPane.add(topPanel,BorderLayout.NORTH);
		inputBar.add(new JLabel("Your Message: "), BorderLayout.WEST);
		inputBar.add(chatInput,BorderLayout.CENTER);
		inputBar.add(chatMessage,BorderLayout.EAST);
		inputBar.add(exit,BorderLayout.EAST);
		contentPane.add(inputBar,BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
		
		chatMessage.addActionListener(this);
		exit.addActionListener(this);
		
		// attempt to connect to the defined remote host
		try {
			client = new Socket("localhost",5000);
			dis = new DataInputStream(client.getInputStream());
			dos = new DataOutputStream(client.getOutputStream());
			
			// define a thread to take care of messages sent from the server
			Thread clientThread = new Thread(this);
			clientThread.start();
			
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			client.shutdownInput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		finally{
//	        try {
//	            if(dis != null) dis.close();
//	        }
//	        catch(Exception e) {} 
//	        try {
//	            if(dos != null) dos.close();
//	        }
//	        catch(Exception e) {}
//	        try{
//	            if(client != null) client.close();
//	        }
//	        catch(Exception e) {} 
//         }
		//disconnect(); //goes loopy - where to close socket ?

	}
	public static void actionPerformed2(ActionEvent exit)
	{
		//disconnect();
		   //doesnt work
		//Client.dispose();
		//close socket and exit gui
	}
	/**
	 * Method to respond to button press events, and send a chat message to the Server
	 */
	
	@Override
	public void actionPerformed(ActionEvent chatMessage)
	{
		try {
			dos.writeInt(ServerConstants.CHAT_MESSAGE); // determine the type of message to be sent
			//dos.writeUTF(userName.getText()); //get user name
			dos.writeUTF(chatInput.getText()); // message payload
			
			dos.flush(); // force the message to be sent (sometimes data can be buffered)
			chatInput.setText(""); // clear text
			
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}


	// process messages from the server
	@Override
	public void run()
	{
		while(true)
		{
			try {
				int messageType = dis.readInt(); // receive a message from the server, determine message type based on an integer
				
				// decode message and process
				switch(messageType)
				{
					case ServerConstants.CHAT_BROADCAST:
						chatHistory.append(dis.readUTF()+"\n");
						break;
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();			
			}
	        finally {
	        		try {
	        	
	        				// Close the input stream.
	        	                client.close();
	        	            }
	        	
	        	            catch(IOException ex) {
	        	
	        	                System.err.println("An IOException was caught: " + ex.getMessage());
	        	                ex.printStackTrace();
	        	            }

				
	        }
		}
		
	}
	public static void main(String[] args)
	{
		Client client = new Client();
		client.setVisible(true);
		//client.disconnect();  // this makes it go loopy

	}
	
}
