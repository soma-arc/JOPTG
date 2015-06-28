package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JPanel;

import dfs.DFSOperator;
import discriminator.DiscretenessDiscriminator;
import number.Complex;
import fuchs.ComplexProbability;

public class Display extends JPanel{
	private ComplexProbability cp, cp2;

	private double magnification = 500;
	private boolean isDraggingQ = false;
	private boolean isDraggingR = false;

	private int maxLevel = 30;
	private double epsilon = 0.0019;
	private static final double EPSILON_STEP = 0.0001;
	private ArrayList<Complex> points = new ArrayList<>();
	private DFSOperator dfs;
	
	private static final int FONT_SIZE = 30;
	private static final int STATUS_POS_X = 10;
	private static final int MAX_LEVEL_POS_Y = 30;
	private static final int EPSILON_POS_Y = 60;
	private static final int DISCRETE_POS_Y = 90;
	private DecimalFormat epsilonFormatter = new DecimalFormat("0.00000");
	
	private DiscretenessDiscriminator discriminator;
	
	public Display(){
		Complex a1 = new Complex(0.25, 0);
		Complex a2 = new Complex(0.25, 0);
		cp = new ComplexProbability(a1, a2, Complex.ZERO);
		cp.setColor(Color.red);
		cp2 = cp.replace(1);
		cp2.setColor(Color.green);
		
		dfs = new DFSOperator(cp.getGens());
		points = dfs.run(maxLevel, epsilon);
		
		addMouseListener(new MousePressedAdapter());
		addMouseMotionListener(new MouseDraggedAdapter());
		addKeyListener(new KeyPressedAdapter());
		
		discriminator = new DiscretenessDiscriminator(cp);
	}

	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawAxis(g2);
		drawCurrentStatus(g2);
		
		cp.drawCircles(g2, magnification, getWidth(), getHeight());
		
		AffineTransform originAf = AffineTransform.getTranslateInstance(getWidth() / 2, getHeight() / 2);
		g2.setTransform(originAf);
		drawLimitSet(g2);
		cp.drawTriangles(g2, magnification, getWidth(), getHeight());

		for(ComplexProbability cpp : discriminator.getComplexProbabilities()){
			cpp.setColor(Color.red);
			cpp.drawCircles(g2, magnification, getWidth(), getHeight());
		}

		cp.drawControlPoints(g2, magnification, getWidth(), getHeight());
	}
	
	private void drawLimitSet(Graphics2D g2){
		g2.setColor(Color.ORANGE);
		for(int i = 0 ; i < points.size()-1; i++){
			Complex point = points.get(i);
			Complex point2 = points.get(i+1);
			g2.drawLine((int) (point.re() * magnification), (int) (point.im() * magnification), (int) (point2.re() * magnification), (int) (point2.im() * magnification));
		}
	}
	
	private void drawCurrentStatus(Graphics2D g2){
		g2.setFont(new Font("Times New Roman", Font.BOLD, FONT_SIZE));
		g2.setColor(Color.white);
		g2.drawString("max level "+ maxLevel, STATUS_POS_X, MAX_LEVEL_POS_Y);
		g2.drawString("epsilon "+ epsilonFormatter.format(epsilon), STATUS_POS_X, EPSILON_POS_Y);
		g2.drawString("isDiscrete : "+ discriminator.isDiscrete(), STATUS_POS_X, DISCRETE_POS_Y);
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
		@Override
		public void mouseDragged(MouseEvent e){
			double mouseX = e.getX() - getWidth() / 2;
			double mouseY = e.getY() - getHeight() / 2;
			Complex np = new Complex(mouseX/magnification, mouseY / magnification);
			if(isDraggingQ){
				cp.moveQ(np);
			}else if(isDraggingR){
				cp.moveR(np);
			}
			dfs = new DFSOperator(cp.getGens());
			points = dfs.run(maxLevel, epsilon);
			discriminator.discriminate();
			repaint();
		}
	}
	
	private class KeyPressedAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
			char keyChar = e.getKeyChar();
			if(e.getKeyChar() == '+'){
				maxLevel++;
				points = dfs.run(maxLevel, epsilon);
			}else if(keyChar == '-'){
				if(maxLevel != 1){
					maxLevel--;
					points = dfs.run(maxLevel, epsilon);
				}
			}else if(keyChar == 'p'){
				epsilon += EPSILON_STEP;
				points = dfs.run(maxLevel, epsilon);
			}else if(keyChar == 'n'){
				epsilon -= EPSILON_STEP;
				points = dfs.run(maxLevel, epsilon);
			}
			repaint();
		}
	}
}