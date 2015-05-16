package mobius;

import figure.Circle;
import number.Complex;
import matrix.Matrix;

public class Mobius {
	private Mobius(){
	}
	
	public static Complex getPlusFixPoint(Matrix t){
		Complex num =  t.a.sub(t.d).add( Complex.sqrt(t.trace().mult(t.trace()).sub(4.0f)));
		return num.div(t.c.mult(2.0f));
	}
	
	public static Complex getMinusFixPoint(Matrix t){
		Complex num =  t.a.sub(t.d).sub( Complex.sqrt(t.trace().mult(t.trace()).sub(4.0f)));
		return num.div(t.c.mult(2.0f));
	}

	public static Complex mobiusOnPoint(Matrix t, Complex z){
		if(z.isInfinity()){
			if(!t.c.isZero()){
				return Complex.div(t.a, t.c);
			}else{
				return Complex.INFINITY;
			}
		}else{
			Complex numerix = Complex.add( Complex.mult(t.a, z), t.b);
			Complex denominator = Complex.add( Complex.mult(t.c, z), t.d);

			if(denominator.isZero()){
				return Complex.INFINITY;
			}else{
				return Complex.div( numerix, denominator);
			}
		}
	}


	public static Circle mobiusOnCircle(Matrix t, Circle c){
		Complex rad = new Complex(c.getR(), 0);
		Complex z = c.getCenter().sub( rad.mult(rad).div( Complex.conjugate(t.d.div(t.c).add(c.getCenter()))));
		Complex newCenter = mobiusOnPoint(t, z);
		double newR = Complex.abs(newCenter.sub( mobiusOnPoint(t, c.getCenter().add(c.getR()))));
		return new Circle(newCenter, newR);
	}
}
