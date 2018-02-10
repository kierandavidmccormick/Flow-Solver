package sample;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by kieranmccormick on 12/26/17.
 */
public class Flow {
	public LinkedList<Node> nodes;
	public LinkedList<Node> endNodes;
	public LinkedList<Node> workingNodes;
	public byte colorCode;
	public boolean isSolved;
	
	public Flow(LinkedList nodes, Node sNode, Node eNode, int colorCode){
		this.nodes = nodes;
		endNodes = new LinkedList<>();
		workingNodes = new LinkedList<>();
		this.endNodes.add(sNode);
		this.endNodes.add(eNode);
		this.workingNodes.add(sNode);
		this.workingNodes.add(eNode);
		if (!nodes.contains(sNode)) {
			nodes.add(sNode);
		}
		if (!nodes.contains(eNode)) {
			nodes.add(eNode);
		}
		this.colorCode = (byte)colorCode;
	}
	
	public Flow(Node sNode, Node eNode, int colorCode){
		this(new LinkedList(), sNode, eNode, colorCode);
		if (!nodes.contains(sNode)) {
			nodes.add(sNode);
		}
		if (!nodes.contains(eNode)) {
			nodes.add(eNode);
		}
	}
	
	public boolean equals(Object o){
		if (o == this){
			return true;
		}
		if (o instanceof Flow){
			Flow f = (Flow)o;
			return nodes.equals(f.nodes) && endNodes.equals(f.endNodes) && workingNodes.equals(f.workingNodes) && colorCode == f.colorCode;
		}
		return false;
	}
	
	public Flow (Flow f){
		nodes = new LinkedList<>();
		for (Node n : f.nodes){
			nodes.add(new Node(n));
		}
		endNodes = new LinkedList<>();
		for (Node n : f.endNodes){
			endNodes.add(new Node(n));
		}
		workingNodes = new LinkedList<>();
		for (Node n : f.workingNodes){
			workingNodes.add(new Node(n));
		}
		colorCode = f.colorCode;
		isSolved = f.isSolved;
	}
	
	public void addNode(Node n, FlowBoard f){
		if (isSolved){
			System.err.println("****** ATTEMPTED TO ILLEGALLY ADD NODE");
		}
		nodes.add(n);
		ArrayList<Coordinate> neighbors = n.loc.getNeighbors(false, false, false, false, n.colorCode, f);
		if (workingNodes.size() == 0){
			int i = 0;
		}
		for (Coordinate c : neighbors){
			workingNodes.remove(f.nodes[c.x][c.y]);
			workingNodes.add(n);
			if (workingNodes.size() == 0){
				int i = 0;
			}
			f.nodes[c.x][c.y].isSolved = true;
			//NOTE: must set isSolved status of new node at node constructor
		}
		if (workingNodes.size() == 0){
			int i = 0;
		}
		resolveSolved(f);
	}
	
	public void removeNode(Node n){
		nodes.remove(n);
		if (endNodes.contains(n)){
			System.err.print("****** ATTEMPTED TO ILLEGALLY REMOVE NODE ");
			n.loc.print();
		}
	}
	
	public void resolveSolved(FlowBoard f){
		if (checkSolved(f)){
			finish();
		}
	}
	
	public boolean checkSolved(FlowBoard f) {
		//try {
			return isSolved || workingNodes.get(0).loc.equals(workingNodes.get(1).loc) || workingNodes.get(0).loc.getNeighbors(false, false, false, false, this.colorCode, f).contains(workingNodes.get(1).loc);
		//} catch (Exception e){
		//	int i = 0;
		//	return false;
		//}
	}
	
	public void finish(){
		for (Node n : workingNodes){
			n.isSolved = true;
		}
		workingNodes.clear();
		isSolved = true;
	}
}
