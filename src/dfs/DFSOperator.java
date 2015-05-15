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
		fixPoints = new Complex[gens.length * 2];
		for(int i = 0 ; i < gens.length ; i++){
			this.gens[i] = gens[i];
			fixPoints[i] = Mobius.getPlusFixPoint(gens[(i + 1)%3].mult(gens[(i + 2)%3]).mult(gens[(i + 3)%3]));
		}
	}

	private void init(int maxLevel){
		tags = new int[maxLevel + 1];
		words = new Matrix[maxLevel + 1];
		words[0] = Matrix.UNIT;
		tags[0] = -1;
		level = 0;
		pointsList = new ArrayList<>();
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
		if(tags[level -1] == 0){
			tags[level] = 1;
		}else{
			tags[level] = 0;
		}

		words[level] = words[level -1].mult(gens[tags[level]]);
	}

	private boolean isAvailableTurn(){
		if(tags[level + 1] == 2 || 
		   (tags[level] == 2 && tags[level + 1] == 1)){
			return false;
		}
		return true;
	}
	
	private void goBackward(){
		level--;
	}
	
	private void turnAndGoForward(){
		tags[level + 1] = (tags[level + 1] + 1)%3;
		if(tags[level] == tags[level + 1]){
			tags[level + 1] = (tags[level + 1] + 1)%3;
		}

		words[level + 1] = words[level].mult(gens[tags[level + 1]]);
		level++;
	}

	private boolean branchTermination(int maxLevel, double epsilon){
		if(level == maxLevel ){
			pointsList.add(Mobius.mobiusOnPoint(words[level], fixPoints[(tags[level])]));
			return true;
		}
		return false;
	}
}
