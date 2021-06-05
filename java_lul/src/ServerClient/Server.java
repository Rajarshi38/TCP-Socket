package ServerClient;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;

	public Server() {
		super("My Messenger");
		userText = new JTextField();
		userText.setColumns(2);
		userText.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendMessage(event.getActionCommand());
				userText.setText(" ");
			}
		}

		);
		getContentPane().add(userText, BorderLayout.SOUTH);
		chatWindow = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(chatWindow);
		getContentPane().add(scrollPane);
		setSize(500, 300);
		setVisible(true);

		chatWindow.setBackground(Color.DARK_GRAY);
		chatWindow.setForeground(Color.white);

	}

	// set up and run the server
	public void startRunning() {
		try {
			server = new ServerSocket(6888, 100);
			while (true) {
				try {
					waitForConnection();
					setupStreams();
					whileChatting();
				} catch (EOFException e) {
					showMessage("\n server ended the connection!!");
				} finally {
					closeCrap();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// wait for connection then display connection information

	private void waitForConnection() throws IOException {
		showMessage("Waiting for someone to connect... \n");
		connection = server.accept();
		showMessage("Connection established to " + connection.getInetAddress().getHostName() + "\n");

	}

	// get stream to send and receive data
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now setup!  \n");

	}

	// during the chat conversation
	private void whileChatting() throws IOException {
		String message = "You are now connected!";
		sendMessage(message);
		ableToType(true);

		do {
			// have a conversation
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);

			} catch (ClassNotFoundException e) {
				showMessage("\n I dont know what the object is");
			}

		} while (!message.equals("CLIENT - END"));

	}

	// close streams and sockets after done chatting
	private void closeCrap() {
		showMessage("\n Closing connection... \n");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// send a message to client
	private void sendMessage(String message) {
		try {
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\nSERVER - " + message);
		} catch (IOException e) {
			chatWindow.append("\n Error: I CAN'T SEND THAT MESSAGE ");
		}
	}

	// updates chat window
	private void showMessage(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.append(text);
			}
		}

		);
	}

	// let the user type stuff into their box

	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				userText.setEditable(tof);
			}
		}

		);
	}

}
