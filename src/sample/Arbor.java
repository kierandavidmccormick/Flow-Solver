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
		//genNewLayer();
		this.root = root;
		addNode(0,root,true);
		root.layer = layers.get(0);
		viewIndex = 0;
	}
	
	public void changeViewIndex(int change){
		viewIndex += change;
		while (viewIndex < 0){
			viewIndex += layers.size();
		}
		viewIndex %= layers.size();
	}
	/*
	public void genLayer(Collection<FlowBoard> fls){
		layers.add(new Layer(fls, this));
	}
	
	public void genLayer(FlowBoard f){
		layers.add(new Layer(f, this));
	}
	/*
	public void genLayer(){
		Layer prev = layers.getLast();
		layers.add(new Layer(prev.getNewNodes(), this));
	}
	/*
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
	*/
	
	public FlowBoard getHighestPriorityBoard(){
		//TODO: add handling for priority rating
		for (int i = layers.size()-1; i != 0; i--){
			for (FlowBoard f : layers.get(i).boards){
				if (f.children.size() == 0 && !f.isLeaf){
					return f;
				}
			}
		}
		return null;
	}
	
	public void genNextNodes(){
		FlowBoard f = getHighestPriorityBoard();
		int count = 0;
		while (f != null && count < 10){
			addNodes(layers.indexOf(f.layer) + 1, f.getApplicableChildren());
			if (f.children.size() == 0){
				int j = 0;
			}
			f = getHighestPriorityBoard();
			count++;
			if (count == 10){
				int i = 0;
				break;
			}
		}
	}
	/*
	public boolean genNewLayer(Collection<FlowBoard> collection){
		if (collection.size() > 0) {
			layers.add(new Layer(collection, this));
			return true;
		}
		return false;
	}
	/*
	public boolean genNewLayer(FlowBoard f){
		return layers.add(new Layer(f, this));
	}
	*/
	public void genNewLayer(){
		new Layer(this);
	}
	
	public boolean addNode(int layer, FlowBoard f, boolean addChildren){
		if (layer < layers.size()){
			f.layer = layers.get(layer);
			layers.get(layer).boards.add(f);
		} else if (layer == layers.size()){
			genNewLayer();
			f.layer = layers.get(layer);
			layers.get(layer).boards.add(f);
		} else {
			System.err.println("ATTEMPTED TO ADD NODE(S) TO INVALID LAYER");
			return false;
		}
		if (addChildren){
			HashSet<FlowBoard> newBoards = f.setAsParentOf(f.getApplicableChildren());
			for (FlowBoard fl : newBoards){
				addNode(layer + 1, fl, newBoards.size() == 1);
			}
		}
		return true;
	}
	
	public boolean addNodes(int layer, Collection<FlowBoard> f){
		for (FlowBoard fl : f){
			if (!addNode(layer, fl, f.size() == 1)){
				return false;
			}
		}
		return true;
	}
	
}
