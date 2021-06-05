package java_lul;

import java.io.*;
import javax.sound.sampled.*;
import java.util.Scanner;

public class audioPlay {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		Scanner sc = new Scanner(System.in);
		File file = new File("E:/Audio/newaudio.wav");
		AudioInputStream audioinput = AudioSystem.getAudioInputStream(file);
		Clip clip = AudioSystem.getClip();

		/*
		 * calculating audio length AudioFormat format = audioinput.getFormat(); long
		 * audioFileLength = file.length();
		 * 
		 * int framesize = format.getFrameSize(); float framerate =
		 * format.getFrameRate(); float duration = (audioFileLength / (framesize *
		 * framerate)); System.out.println(duration);
		 */

		clip.open(audioinput);
		// clip.start();

		String response = "";

		while (!response.equals("Q")) {
			System.out.println("P = PLAY\nS = STOP\nR = RESET\nQ = QUIT");
			System.out.println("Enter your choice: ");
			response = sc.next();
			response = response.toUpperCase();

			switch (response) {
			case "P":
				clip.start();
				break;
			case "S":
				clip.stop();
				break;
			case "R":
				clip.setMicrosecondPosition(0);
				break;
			case "Q":
				clip.close();

			default:
				System.out.println("ERROR! enter valid command");
			}
		}
	}

}
