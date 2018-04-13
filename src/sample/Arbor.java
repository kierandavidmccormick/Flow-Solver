package sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by kieranmccormick on 1/30/18.
 */
public class Arbor {
	FlowBoard root;
	LinkedList<Layer> layers;
	int viewIndex;
	
	public Arbor(FlowBoard root){
		layers = new LinkedList<>();
		genLayer(root);
		viewIndex = 0;
	}
	
	public void changeViewIndex(int change){
		viewIndex += change;
		while (viewIndex < 0){
			viewIndex += layers.size();
		}
		viewIndex %= layers.size();
	}
	
	public void genLayer(Collection<FlowBoard> fls){
		layers.add(new Layer(fls, this));
	}
	
	public void genLayer(FlowBoard f){
		layers.add(new Layer(f, this));
	}
	
	public void genLayer(){
		Layer prev = layers.getLast();
		layers.add(new Layer(prev.getNewNodes(), this));
	}
	
	public boolean genLayerDepthFirst(){
		Layer prev = layers.getLast();
		LinkedList<FlowBoard> boards = new LinkedList<>(prev.boards);
		Layer n = new Layer(prev.getNewNodes(boards.get(0), this), this);
		if (n.boards.size() > 0) {
			layers.add(n);
			layers.getLast().addAllCertainMoves();
			return true;
		}
		return false;
	}
	
	public boolean genNewLayer(Collection<FlowBoard> collection){
		if (collection.size() > 0) {
			layers.add(new Layer(collection, this));
			return true;
		}
		return false;
	}
	
	public boolean genNewLayer(FlowBoard f){
		return layers.add(new Layer(f, this));
	}
	
	public boolean addNode(int layer, FlowBoard f, boolean addChildren){
		if (layer < layers.size()){
			layers.get(layer).boards.add(f);
		} else if (layer == layers.size()){
			genNewLayer(f);
		} else {
			System.err.println("ATTEMPTED TO ADD NODE(S) TO INVALID LAYER");
			return false;
		}
		if (addChildren){
			HashSet<FlowBoard> newBoards = f.setAsParentOf(f.getApplicableChildren());
			for (FlowBoard fl : newBoards){
				addNode(layer, fl, newBoards.size() == 1);
			}
		}
		return true;
	}
	
	public boolean addNodes(int layer, Collection<FlowBoard> f, boolean addChildren){
		for (FlowBoard fl : f){
			if (!addNode(layer, fl, addChildren)){
				return false;
			}
		}
		return true;
	}
	
}
