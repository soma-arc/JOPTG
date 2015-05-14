package figure;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import number.Complex;

public class Circle {
	private Complex center;
	private double r;
	public static final int CENTER_POINT_R = 5;
	
	public Circle(Complex center, double r){
		this.center = center;
		this.r = r;

	}
	
	public void draw(Graphics2D g2, double magnification){
		g2.drawOval((int) ((center.re() - r) * magnification), (int) ((center.im() - r) * magnification), 
				   (int) (2*r * magnification), (int) (2*r * magnification));
		drawCenter(g2, magnification);
	}
	
	public void drawCenter(Graphics g, double magnification){
		g.fillOval((int)(center.re() * magnification - CENTER_POINT_R),
				   (int)(center.im() * magnification - CENTER_POINT_R), 
				   2 * CENTER_POINT_R, 2 * CENTER_POINT_R);
	}
	
	public Complex getCenter(){
		return center;
	}
	
	public double getR(){
		return r;
	}
	
	public String toString(){
		return "{"+ center +" r = "+ r +"}";
	}
}
