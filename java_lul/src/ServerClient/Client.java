package ServerClient;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket connection;
	private String ServerIP;
	private String message = "";

	// constructor
	public Client(String host) {
		super("Client");
		ServerIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendMessage(event.getActionCommand());
				userText.setText("");

			}
		});
		add(userText, BorderLayout.SOUTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(500, 300);
		setVisible(true);

		chatWindow.setBackground(Color.DARK_GRAY);
		chatWindow.setForeground(Color.white);
	}

	public void startRunning() {
		try {
			connectToServer();
			setupStream();
			whileChatting();

		} catch (EOFException e) {
			showMessage("\n Client terminated the connection.");
		} catch (IOException ie) {
			ie.printStackTrace();
		} finally {
			closeCrap();
		}
	}

	// connect to server
	private void connectToServer() throws IOException {
		showMessage("Attempting Connection... \n");
		connection = new Socket(InetAddress.getByName(ServerIP), 6888);
		showMessage("You are connected to " + connection.getInetAddress().getHostName());
	}

	private void setupStream() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Your streams are connected \n");

	}

	private void whileChatting() throws IOException {
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);

			} catch (ClassNotFoundException e) {
				showMessage("\n Don't Know !!!");
			}
		} while (!message.equals("SERVER - END"));
	}

	private void closeCrap() {
		showMessage("\n Closing connection.. ");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(String text) {
		try {
			output.writeObject("CLIENT - " + text);
			output.flush();
			showMessage("\nCLIENT - " + text);

		} catch (IOException e) {
			chatWindow.append("\n ERROR CAN'T SEND MESSAGE");
		}
	}

	private void showMessage(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.append(text);
			}
		});
	}

	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				userText.setEditable(tof);
			}
		});
	}

}
