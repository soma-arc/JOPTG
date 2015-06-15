package fuchs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import figure.Circle;
import matrix.Matrix;
import number.Complex;

public class ComplexProbability {
	private Complex origin;
	private Complex a0, a1, a2, x, y, z;
	private Complex p0, q0, r0;
	private Complex mirrorVecX, mirrorVecY, mirrorVecZ;
	private Circle cP0, cQ0, cR0;
	private Color color = Color.white;
	//‰~PQR‚ÌŒð“_
	private Complex aboveIntersectPQ, aboveIntersectQR, aboveIntersectRP1, aboveIntersectR1P;
	private Complex bottomIntersectPQ, bottomIntersectQR, bottomIntersectRP1, bottomIntersectR1P;
	
	public ComplexProbability(Complex a1, Complex a2, Complex origin){
		this.origin = origin;
		this.a1 = a1;
		this.a2 = a2;
		this.a0 = Complex.ONE.sub(a1).sub(a2);
		setData();
	}
	
	public void moveQ(Complex Q){
		this.a1 = Q.sub(origin);
		this.a2 = r0.sub(Q);
		this.a0 = Complex.ONE.sub(a1).sub(a2);
		setData();
	}
	
	public void moveR(Complex R){
		this.a2 = R.sub(origin).sub(a1);
		this.a0 = Complex.ONE.sub(a1).sub(a2);
		setData();
	}
	
	private void setData(){
		x = Complex.sqrt(Complex.ONE.div(a0.mult(a1)));
		y = Complex.sqrt(Complex.ONE.div(a1.mult(a2)));
		z = Complex.sqrt(Complex.ONE.div(a2.mult(a0)));
		
		mirrorVecX = Complex.I.div(x);
		mirrorVecY = Complex.I.div(y);
		mirrorVecZ = Complex.I.div(z);
		
		p0 = origin;
		q0 = p0.add(a1);
		r0 = q0.add(a2);
		
		cP0 = new Circle(p0, mirrorVecX.abs());
		cQ0 = new Circle(q0, mirrorVecY.abs());
		cR0 = new Circle(r0, mirrorVecZ.abs());
		
		Complex[] intersectPQ = Circle.getIntersections(cP0, cQ0);
		Complex[] intersectQR = Circle.getIntersections(cQ0, cR0);
		Complex[] intersectRP1 = Circle.getIntersections(cR0, new Circle(cP0.getCenter().add(Complex.ONE), cP0.getR()));
		if(intersectPQ[0].im() > intersectPQ[1].im()){
			aboveIntersectPQ = intersectPQ[0];
			bottomIntersectPQ = intersectPQ[1];
		}else{
			aboveIntersectPQ = intersectPQ[1];
			bottomIntersectPQ = intersectPQ[0];
		}
		if(intersectQR[0].im() > intersectQR[1].im()){
			aboveIntersectQR = intersectQR[0];
			bottomIntersectQR = intersectQR[1];
		}else{
			aboveIntersectQR = intersectQR[1];
			bottomIntersectQR = intersectQR[0];
		}
		if(intersectRP1[0].im() > intersectRP1[1].im()){
			aboveIntersectRP1 = intersectRP1[0];
			bottomIntersectRP1 = intersectRP1[1];
		}else{
			aboveIntersectRP1 = intersectRP1[1];
			bottomIntersectRP1 = intersectRP1[0];
		}
		
		aboveIntersectR1P = aboveIntersectRP1.sub(Complex.ONE);
		bottomIntersectR1P = bottomIntersectRP1.sub(Complex.ONE);
	}
	
	public Matrix[] getGens(){
		Matrix[] gens = new Matrix[3];
		gens[0] = calcGen(p0, x);
		gens[1] = calcGen(q0, y);
		gens[2] = calcGen(r0, z);
		return gens;
	}
	
	private Matrix calcGen(Complex p, Complex x){
		return new Matrix(x.mult(p), x.mult(p.mult(p)).mult(-1).sub(Complex.ONE.div(x)),
						  x, x.mult(p).mult(-1));
	}
	
	public void draw(Graphics2D g2, double magnification, int width, int height){
		drawCircles(g2, magnification, width, height);
		drawPath(g2, magnification, width, height);
		drawVectors(g2, magnification, width, height);
	}
	
	public void drawTriangles(Graphics2D g2, double magnification, int width, int height){
		AffineTransform af = AffineTransform.getTranslateInstance(width/2 , height/2);
		g2.setTransform(af);
		g2.setColor(Color.white);
		drawTriangle(g2, magnification, p0, aboveIntersectPQ, q0);
		drawTriangle(g2, magnification, p0, bottomIntersectPQ, q0);

		drawTriangle(g2, magnification, q0, aboveIntersectQR, r0);
		drawTriangle(g2, magnification, q0, bottomIntersectQR, r0);
		
		drawTriangle(g2, magnification, r0, aboveIntersectRP1, p0.add(Complex.ONE));
		drawTriangle(g2, magnification, r0, bottomIntersectRP1, p0.add(Complex.ONE));
		
		drawTriangle(g2, magnification, r0.sub(Complex.ONE), aboveIntersectR1P, p0);
		drawTriangle(g2, magnification, r0.sub(Complex.ONE), bottomIntersectR1P, p0);
		
		g2.setTransform(new AffineTransform());
	}
	
	public void drawTriangle(Graphics2D g2, double magnification, Complex p1, Complex p2, Complex p3){
		g2.drawLine((int)(p1.re() * magnification), (int)(p1.im() * magnification),
					(int)(p2.re() * magnification), (int) (p2.im() * magnification));
		g2.drawLine((int)(p2.re() * magnification), (int)(p2.im() * magnification),
					(int)(p3.re() * magnification), (int) (p3.im() * magnification));
	}
	
	public void drawControlPoints(Graphics2D g2, double magnification, int width, int height){
		AffineTransform af = AffineTransform.getTranslateInstance(width/2 , height/2);
		g2.setTransform(af);
		g2.setColor(Color.ORANGE);
		cQ0.drawCenter(g2, magnification);
		cR0.drawCenter(g2, magnification);
		g2.setTransform(new AffineTransform());
	}

	public void drawCircles(Graphics2D g2, double magnification, int width, int height){
		g2.setColor(color);
		int n = (int)(width / 2 / (1 * magnification)) + 2; 
		for(int i = -n ; i <= n ; i++){
			AffineTransform af = AffineTransform.getTranslateInstance(width/2 + i * magnification , height/2);
			g2.setTransform(af);
			cP0.draw(g2, magnification);
			cQ0.draw(g2, magnification);
			cR0.draw(g2, magnification);
			g2.setTransform(new AffineTransform());
		}
	}
	
	public void drawPath(Graphics2D g2, double magnification, int width, int height){
		g2.setColor(Color.white);

		int n = (int)(width / 2 / (1 * magnification)) + 2; 
		for(int i = -n ; i <= n ; i++){
			AffineTransform af = AffineTransform.getTranslateInstance(width/2 + (i + origin.re()) * magnification , origin.im() * magnification  + height/2);
			g2.setTransform(af);

			drawVectorFromOrigin(a1, g2, magnification);

			af.translate(a1.re() * magnification, a1.im() * magnification);
			g2.setTransform(af);

			drawVectorFromOrigin(a2, g2, magnification);

			af.translate(a2.re() * magnification, a2.im() * magnification);
			g2.setTransform(af);

			drawVectorFromOrigin(a0, g2, magnification);

			g2.setTransform(new AffineTransform());
		}
	}
	
	public void drawVectors(Graphics2D g2, double magnification, int width, int height){
		g2.setColor(Color.white);

		int n = (int)(width / 2 / (1 * magnification)) + 2; 
		for(int i = -n ; i <= n ; i++){
			AffineTransform af = AffineTransform.getTranslateInstance(width/2 + (i + origin.re()) * magnification , origin.im() * magnification  + height/2);
			g2.setTransform(af);

			drawVectorFromOrigin(mirrorVecX, g2, magnification);
			drawVectorFromOrigin(mirrorVecX.mult(-1), g2, magnification);

			af.translate(a1.re() * magnification, a1.im() * magnification);
			g2.setTransform(af);

			drawVectorFromOrigin(mirrorVecY, g2, magnification);
			drawVectorFromOrigin(mirrorVecY.mult(-1), g2, magnification);

			af.translate(a2.re() * magnification, a2.im() * magnification);
			g2.setTransform(af);

			drawVectorFromOrigin(mirrorVecZ, g2, magnification);
			drawVectorFromOrigin(mirrorVecZ.mult(-1), g2, magnification);

			g2.setTransform(new AffineTransform());
		}
	}
	
	
	public void drawVectorFromOrigin(Complex to, Graphics2D g2, double magnification){
		g2.drawLine(0, 0, (int) (to.re() * magnification), (int) (to.im() * magnification));
	}
	
	public ComplexProbability replace(int index){
		if(index == 1){
			return new ComplexProbability(a1.add(a2), a2.mult(a0).div(a1.add(a2)), p0);
		}else if(index == 2){
			return new ComplexProbability(a2.add(a0), a0.mult(a1).div(a2.add(a0)), q0);
		}else{// 0
			return new ComplexProbability(a0.add(a1), a1.mult(a2).div(a0.add(a1)), r0);
		}
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public boolean isClickedQ(double mouseX, double mouseY, double magnification){
		if(q0.mult(magnification).dist(new Complex(mouseX, mouseY)) < Circle.CENTER_POINT_R){
			return true;
		}
		return false;
	}
	
	public boolean isClickedR(double mouseX, double mouseY, double magnification){
		if(r0.mult(magnification).dist(new Complex(mouseX, mouseY)) < Circle.CENTER_POINT_R){
			return true;
		}
		return false;
	}
	
	
	public boolean isIntersectAboveTriangle(){
		return aboveIntersectPQ.sub(q0).div(aboveIntersectQR.sub(q0)).im() < 0;
	}
	
	public boolean isIntersectBottomTriangle(){
		return bottomIntersectPQ.sub(q0).div(bottomIntersectQR.sub(q0)).im() > 0;
	}
}
