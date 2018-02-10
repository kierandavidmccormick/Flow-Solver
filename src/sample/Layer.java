package sample;

import java.util.*;

/**
 * Created by kieranmccormick on 1/30/18.
 */
public class Layer {
	HashSet<FlowBoard> boards;
	
	public Layer(){
		boards = new HashSet<>();
	}
	
	public Layer(FlowBoard f){
		this();
		boards.add(f);
		f.layer = this;
	}
	
	public Layer(Collection<FlowBoard> fls){
		boards = new HashSet<>(fls);
		for (FlowBoard f : boards){
			f.layer = this;
		}
	}
	
	public void delete(){
		boards.clear();
	}
	
	public HashSet<FlowBoard> getNewNodes(){
		HashSet newNodes = new HashSet();
		for (FlowBoard f: boards){
			ArrayList<FlowBoard> newBoards = f.genChildren();
			newNodes.addAll(newBoards);
			for (FlowBoard nb : newBoards){
				//nb.parents.add(f);
				f.children.add(nb);
			}
		}
		return newNodes;
	}
	
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
		return newNodes;
	}
	
	public void addAllCertainMoves(){
		for (FlowBoard f : boards){
			f.addCertainMoves(true);
		}
	}
}
