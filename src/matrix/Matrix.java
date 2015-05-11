package matrix;
import number.Complex;

public class Matrix {
	public Complex a, b, c, d;
	public static Matrix UNIT = new Matrix(1, 0, 0 , 1);

	public Matrix(Complex a, Complex b, Complex c, Complex d){
		this.a = a;
	    this.b = b;
	    this.c = c;
	    this.d = d;
	}

	public Matrix(double a, double b, double c, double d){
		this.a = new Complex(a);
	    this.b = new Complex(b);
	    this.c = new Complex(c);
	    this.d = new Complex(d);
	}

	public Matrix mult(Matrix n){
		return new Matrix(Complex.add(Complex.mult(a, n.a), Complex.mult(b, n.c)),
	                      Complex.add(Complex.mult(a, n.b), Complex.mult(b, n.d)),
	                      Complex.add(Complex.mult(c, n.a), Complex.mult(d, n.c)),
	                      Complex.add(Complex.mult(c, n.b), Complex.mult(d, n.d)));
	}

	public Matrix mult(double coefficient){
		return new Matrix(a.mult(coefficient),
	                      b.mult(coefficient),
	                      c.mult(coefficient),
	                      d.mult(coefficient));
	}

	public Matrix mult(Complex coefficient){
		return new Matrix(a.mult(coefficient),
				b.mult(coefficient),
				c.mult(coefficient),
				d.mult(coefficient));
	}

	public Matrix inverse(){
		Complex one = new Complex(1.0, 0.0);
		return new Matrix(d, b.mult(-1.0), c.mult(-1.0), a).mult(one.div(a.mult(d).sub(b.mult(c))));
	}

	public Complex trace(){
		return a.add(d);
	}

	public Matrix conjugate(Matrix T){
		return T.mult(this).mult(T.inverse());
	}

	public String toString(){
		return "{"+ a.toString() +","+ b.toString() +"\n"+ c.toString() +","+ d.toString() +"}";
	}
}
