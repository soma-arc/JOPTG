package dfs;

import java.util.ArrayList;

import number.Complex;
import matrix.Matrix;
import mobius.Mobius;

public class DFSOperator {
	private Matrix[] gens;
	private Matrix[] words;
	private Complex[] fixPoints;
	private int[] tags;
	private int level;
	private ArrayList<Complex> pointsList;
	
	public DFSOperator(Matrix[] gens){
		this.gens = new Matrix[gens.length];
		fixPoints = new Complex[gens.length];
		for(int i = 0 ; i < gens.length ; i++){
			this.gens[i] = gens[i];
			fixPoints[i] = Mobius.mobiusOnPoint(gens[i], Complex.INFINITY);
		}
	}

	private void init(int maxLevel){
		tags = new int[maxLevel + 1];
		words = new Matrix[maxLevel + 1];
		words[0] = Matrix.UNIT;
		tags[0] = -1;
		level = 0;
		pointsList = new ArrayList<>();

		words[1] = gens[1];
		tags[1] = 1;
		level++;
	}
	
	public ArrayList<Complex> run(int maxLevel, double epsilon){
		init(maxLevel);
		do{
			while(branchTermination(maxLevel, epsilon) == false){
				goForward();
			}
			do{
				goBackward();
			}while(level != 0 && isAvailableTurn() == false);
			turnAndGoForward();
		}while(level != 1 || tags[1] != 0);
		return pointsList;
	}
	
	private void goForward(){
		level++;
		tags[level] = (tags[level-1] + 1) % 3; 

		words[level] = words[level -1].mult(gens[tags[level]]);
	}

	private boolean isAvailableTurn(){
		if((tags[level] + 2) % 3 != (tags[level + 1] + 1) % 3 ||
		   (level == 2 && tags[1] == 2 && tags[2] == 1 && tags[3] == 2)){
			return false;
		}
		return true;
	}
	
	private void goBackward(){
		level--;
	}
	
	private void turnAndGoForward(){
		tags[level + 1] = (tags[level + 1] + 1)%3;

		words[level + 1] = words[level].mult(gens[tags[level + 1]]);
		level++;
	}

	private boolean branchTermination(int maxLevel, double epsilon){
		Complex[] z = {Mobius.mobiusOnPoint(words[level], fixPoints[(tags[level] + 1) % 3]),
					   Mobius.mobiusOnPoint(words[level], fixPoints[(tags[level] + 2) % 3])};
		
		if((z[0].dist(z[1])) < epsilon || level == maxLevel){
			pointsList.add(z[0]);
			pointsList.add(z[1]);
			return true;
		}
		
		return false;
	}
}
