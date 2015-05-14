package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JPanel;

import number.Complex;
import fuchs.ComplexProbability;

public class Display extends JPanel{
	private ComplexProbability cp, cp2;

	private double magnification = 500;
	private boolean isDraggingQ = false;
	private boolean isDraggingR = false;
	//private Complex draggingPoint = null;

	public Display(){
		Complex a1 = new Complex(0.3, 0.4);
		Complex a2 = new Complex(0.3, 0.1);
		cp = new ComplexProbability(a1, a2, Complex.ZERO);
		cp.setColor(Color.red);
		cp2 = cp.replace(1);
		cp2.setColor(Color.green);

		addMouseListener(new MousePressedAdapter());
		addMouseMotionListener(new MouseDraggedAdapter());
	}

	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawAxis(g2);
		cp.draw(g2, magnification, getWidth(), getHeight());
		cp.drawControlPoints(g2, magnification, getWidth(), getHeight());
	}

	private void drawAxis(Graphics2D g2){
		g2.setColor(Color.gray);
		g2.drawLine(0, getHeight()/2, getWidth(), getHeight()/2); //x axis
		int n = (int)(getWidth() / 2 / (1 * magnification)); 
		for(int i = -n ; i <= n ; i++){
			g2.drawLine((int) magnification * i + getWidth()/2, 0, (int) magnification * i + getWidth()/2, getHeight());
		}
	}
	
	private class MousePressedAdapter extends MouseAdapter{
		public void mousePressed(MouseEvent e){
			double mouseX = e.getX() - getWidth() / 2;
			double mouseY = e.getY() - getHeight() / 2;
			if(cp.isClickedQ(mouseX , mouseY, magnification)){
				isDraggingQ = true;
			}else if(cp.isClickedR(mouseX, mouseY, magnification)){
				isDraggingR = true;
			}
		}
		
		public void mouseReleased(MouseEvent e){
			isDraggingQ = false;
			isDraggingR = false;
		}
	}
	
	private class MouseDraggedAdapter extends MouseMotionAdapter{
		public void mouseDragged(MouseEvent e){
			double mouseX = e.getX() - getWidth() / 2;
			double mouseY = e.getY() - getHeight() / 2;
			Complex np = new Complex(mouseX/magnification, mouseY / magnification);
			if(isDraggingQ){
				cp.moveQ(np);
			}else if(isDraggingR){
				cp.moveR(np);
			}
			repaint();
		}
	}
}