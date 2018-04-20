package sample;

import java.util.*;

/**
 * Created by kieranmccormick on 1/30/18.
 */
public class Layer {
	HashSet<FlowBoard> boards;
	int viewIndex;
	Arbor ar;
	
	public Layer(Arbor ar){
		boards = new HashSet<>();
		viewIndex = 0;
		this.ar = ar;
		ar.layers.add(this);
	}
	/*
	public Layer(FlowBoard f, Arbor ar){
		this(ar);
		f.layer = this;
		ar.addNode(ar.layers.indexOf(this), f, true);   //TODO: ?
	}
	*/
	/*
	public Layer(Collection<FlowBoard> fls, Arbor ar){
		this(ar);
		for (FlowBoard f : boards){
			f.layer = this;
		}
		ar.addNodes(ar.layers.indexOf(this), fls);    //TODO: ?
		viewIndex = 0;
	}
	*/
	public void changeViewIndex(int change){
		viewIndex += change;
		while (viewIndex < 0){
			viewIndex += boards.size();
		}
		viewIndex %= boards.size();
	}
	
	public ArrayList<FlowBoard> getBoardsIterable(){
		return new ArrayList<>(boards);
	}
	/*
	public void delete(){
		boards.clear();
	}
	*/
	/*
	public HashSet<FlowBoard> getNewNodes() {
		HashSet newNodes = new HashSet();
		for (FlowBoard f : boards) {
			ArrayList<FlowBoard> newBoards = f.genChildren();
			newNodes.addAll(newBoards);
			for (FlowBoard nb : newBoards) {
				//nb.parents.add(f);
				f.children.add(nb);
			}
		}
		return newNodes;
	}
	/*
	public HashSet<FlowBoard> getNewNodes(FlowBoard f, Arbor ar){
		HashSet<FlowBoard> newNodes = new HashSet<>(f.genChildren());
		for (FlowBoard nb : newNodes) {
			Boolean original = true;
			for (Layer l : ar.layers) {
				if (l.boards.contains(f)) {
					for (FlowBoard flowBoard : l.boards){
						if (flowBoard.equals(nb)){
							flowBoard.parents.add(f);
							f.children.add(flowBoard);
							original = false;
						}
					}
				}
			}
			if (original){
				nb.parents.add(f);
				f.children.add(nb);
			}
		}
		/*
		for (FlowBoard nb : newNodes){
			nb.parents.add(f);
			f.children.add(nb);
		}
		*/
	/*
		return newNodes;
	}
	
	//TODO: add handling for adding parent references to nodes already in the arbor
	public HashSet<FlowBoard> getNewNodes2(FlowBoard f, Arbor ar){
		HashSet<FlowBoard> newBoards = f.getApplicableChildren();
		for (FlowBoard fl : newBoards){
			f.children.add(fl);
			//fl.parents.add(f);
		}
		return newBoards;
	}
	/*
	public void addNode(FlowBoard f, boolean addChildren){
		boards.add(f);
		if (addChildren){
			HashSet<FlowBoard> newBoards = f.setAsParentOf(f.getApplicableChildren());
			
		}
	}
	
	//TODO: base addChildren off of individual assessment of flowBoard siblings
	public void addNodes(Collection<FlowBoard> f, boolean addChildren){
		boards.addAll(f);
		if (addChildren){
			HashSet<FlowBoard> newBoards = new HashSet<>();
			for (FlowBoard fl : f){
				newBoards.addAll(fl.setAsParentOf(fl.getApplicableChildren()));
			}
			ar.addNodes(ar.layers.indexOf(this), newBoards, false);
		}
	}
	*/
	/*
	public void addAllCertainMoves(){
		for (FlowBoard f : boards){
			f.addCertainMoves(true);
		}
	}
	*/
}
