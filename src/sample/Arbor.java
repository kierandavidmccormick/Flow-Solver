package sample;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by kieranmccormick on 1/30/18.
 */
public class Arbor {
	FlowBoard root;
	LinkedList<Layer> layers;
	
	public Arbor(FlowBoard root){
		layers = new LinkedList<>();
		genLayer(root);
	}
	
	public void genLayer(Collection<FlowBoard> fls){
		layers.add(new Layer(fls));
	}
	
	public void genLayer(FlowBoard f){
		layers.add(new Layer(f));
	}
	
	public void genLayer(){
		Layer prev = layers.getLast();
		layers.add(new Layer(prev.getNewNodes()));
	}
	
	public void genLayerDepthFirst(){
		Layer prev = layers.getLast();
		LinkedList<FlowBoard> boards = new LinkedList<>(prev.boards);
		layers.add(new Layer(prev.getNewNodes(boards.get(0), this)));
		layers.getLast().addAllCertainMoves();
	}
}
