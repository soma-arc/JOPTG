package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import number.Complex;
import fuchs.ComplexProbability;

public class Display extends JPanel{
	private ComplexProbability cp, cp2;
	public Display(){
		Complex a1 = new Complex(0.3, 0.4);
		Complex a2 = new Complex(0.3, 0.1);
		cp = new ComplexProbability(a1, a2, Complex.ZERO);
		cp.setColor(Color.red);
		cp2 = cp.replace(1);
		cp2.setColor(Color.green);
	}
	
	private double magnification = 500;
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawAxis(g2);
		//cp.draw(g2, magnification, getWidth(), getHeight());
		cp2.draw(g2, magnification, getWidth(), getHeight());
	}
	
	private void drawAxis(Graphics2D g2){
		g2.setColor(Color.gray);
		g2.drawLine(0, getHeight()/2, getWidth(), getHeight()/2); //x axis
		int n = (int)(getWidth() / 2 / (1 * magnification)); 
		for(int i = -n ; i <= n ; i++){
			g2.drawLine((int) magnification * i + getWidth()/2, 0, (int) magnification * i + getWidth()/2, getHeight());
		}
	}
}