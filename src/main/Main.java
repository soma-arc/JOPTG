package main;

import javax.swing.JFrame;

import org.w3c.dom.DocumentFragment;

import ui.Display;

public class Main {
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 1000;

	public static void main(String[] args) {
		new Main().start();
	}

	public void start(){
		JFrame frame = new JFrame();
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Display display = new Display();
		frame.add(display);
		frame.setVisible(true);
		display.requestFocus();
	}
}