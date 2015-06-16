package discriminator;

import java.util.ArrayList;

import number.Complex;
import fuchs.ComplexProbability;

public class DiscretenessDiscriminator {
	private ComplexProbability root;

	public DiscretenessDiscriminator(ComplexProbability root){
		this.root = root;
	}
	
	private int maxLevel = 5;
	private ArrayList<ComplexProbability> list = new ArrayList<>();
	public void explore(){
		list.clear();
		ArrayList<ComplexProbability> rootReplaced = root.replace();
		list.addAll(rootReplaced);
		for(ComplexProbability cpp : rootReplaced){
			replace(cpp, 1);
		}
	}
	
	private void replace(ComplexProbability cp, int level){
		if(level > maxLevel) return;
		ArrayList<ComplexProbability> replaced = cp.replace();
		list.addAll(replaced);
		for(ComplexProbability cpp : replaced){
			replace(cpp, level + 1);
		}
	}
	
	public ArrayList<ComplexProbability> getComplexProbabilities(){
		return list;
	}
}