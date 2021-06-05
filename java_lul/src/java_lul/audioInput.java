package java_lul;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.io.*;

public class audioInput {

	static final int RECORD_TIME = 10000;

	File wavFile = new File("E:/Audio/newaudio.wav");

	// define format of audio file
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

	// line from which audio data was captured
	TargetDataLine line;

	// define an audio format
	AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 8;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

		return format;
	}

	void start() {
		try {
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			// checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not supported");
				System.exit(0);

			}
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start(); // start capturing

			System.out.println("Start Capturing audio...   ");
			AudioInputStream input = new AudioInputStream(line);
			System.out.println("Start Recording...   ");

			// start recording
			AudioSystem.write(input, fileType, wavFile);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException lu) {
			lu.printStackTrace();
		}
	}

	void finish() {
		line.stop();
		line.close();
		System.out.println("Finisher Recording ....");
	}

	public static void main(String[] args) {
		audioInput recorder = new audioInput();

		Thread stopper = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(RECORD_TIME);
				} catch (InterruptedException ie) {

					ie.printStackTrace();
				}

				recorder.finish();
			}

		});
		stopper.start();

		// start recording
		recorder.start();
	}
}
