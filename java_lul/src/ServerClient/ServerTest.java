package ServerClient;

import javax.swing.*;

public class ServerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server myserver = new Server();
		myserver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myserver.startRunning();

	}

}
