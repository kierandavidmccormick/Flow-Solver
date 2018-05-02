package sample;

import java.util.*;

/**
 * Created by kieranmccormick on 1/30/18.
 */
public class Arbor {
	FlowBoard root;
	LinkedList<Layer> layers;
	Stack<FlowBoard> workingBoards;
	int viewIndex;
	
	public Arbor(FlowBoard root){
		layers = new LinkedList<>();
		workingBoards = new Stack<>();
		//genNewLayer();
		this.root = root;
		addNode(0,root, null, true);
		workingBoards.push(root);
		//root.layer = layers.get(0);
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
			for (FlowBoard f : layers.get(i).boards.values()){
				if (f.children.size() == 0 && !f.isLeaf){
					return f;
				}
			}
		}
		return null;
	}
	
	public FlowBoard getBacktrackBoard(){
		FlowBoard current = workingBoards.peek();
		do {
			for (FlowBoard child : current.children) {
				if (child.isSolved()){
					workingBoards.push(child);
					System.out.println("SOLVED");
					return null;
				}
				if (!child.isLeaf) {
					workingBoards.push(child);
					return child;
				}
			}
			workingBoards.pop();
			current = workingBoards.peek();
		} while (current != root);
		return root;
	}
	
	
	public void genNextNodes(){
		//FlowBoard f = getHighestPriorityBoard();
		FlowBoard f = getBacktrackBoard();
		int count = 0;
		int repetitions = 100;
		//HashSet<Integer> ids = new HashSet<>(repetitions);
		while (f != null && count < repetitions){
			addNodes(layers.indexOf(f.layer) + 1, f.getApplicableChildren(), f);
			//f = getHighestPriorityBoard();
			f = getBacktrackBoard();
			if (f == null || f == root){
				return;
			}
			count++;
			//int id = System.identityHashCode(f);
			//boolean New = ids.add(id);
			System.out.println("Count: " + count/* + " New: " + New + " Board: " + id*/);
			
		}
		//System.out.println("Boards: " + getBoardsSize());
	}
	
	public int getBoardsSize(){
		int size = 0;
		for (Layer l : layers){
			size += l.boards.size();
		}
		return size;
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
	
	public boolean addNode(int layer, FlowBoard f, FlowBoard p, boolean addChildren){
		if (p != null) {
			p.setAsParentOf(f);
		}
		if (layer < layers.size()){
			f.layer = layers.get(layer);
			//layers.get(layer).boards.put(f.hashCode(), f);
			layers.get(layer).addM(f, p);
		} else if (layer == layers.size()){
			genNewLayer();
			f.layer = layers.get(layer);
			//layers.get(layer).boards.put(f.hashCode(), f);
			layers.get(layer).addM(f, p);
		} else {
			System.err.println("ATTEMPTED TO ADD NODE(S) TO INVALID LAYER");
			return false;
		}
		if (f.isSolved()){
			return true;
		}
		if (addChildren){
			//HashSet<FlowBoard> newBoards = f.setAsParentOf(f.getApplicableChildren());
			HashSet<FlowBoard> newBoards = f.getApplicableChildren();
			for (FlowBoard fl : newBoards){
				addNode(layer + 1, fl, f, newBoards.size() == 1);
			}
		}
		return false;
	}
	
	public boolean addNodes(int layer, Collection<FlowBoard> f, FlowBoard p){
		for (FlowBoard fl : f){
			if (addNode(layer, fl, p, f.size() == 1)){
				return true;
			}
		}
		return false;
	}
	
}
