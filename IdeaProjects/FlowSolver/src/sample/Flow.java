package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by kieranmccormick on 12/26/17.
 */
public class Flow {
	public LinkedList<Node> nodes;
	public LinkedList<Node> endNodes;
	public LinkedList<Node> workingNodes;
	public Color color;
	
	public Flow(LinkedList nodes, Node sNode, Node eNode, Color color){
		this.nodes = nodes;
		endNodes = new LinkedList<>();
		workingNodes = new LinkedList<>();
		this.endNodes.add(sNode);
		this.endNodes.add(eNode);
		this.workingNodes.add(sNode);
		this.workingNodes.add(eNode);
		this.color = color;
	}
	
	public Flow(Node sNode, Node eNode, Color color){
		this(new LinkedList(), sNode, eNode, color);
		nodes.add(sNode);
		nodes.add(eNode);
	}
	
	public boolean equals(Object o){
		if (o == this){
			return true;
		}
		if (o instanceof Flow){
			Flow f = (Flow)o;
			return nodes.equals(f.nodes) && endNodes.equals(f.endNodes) && workingNodes.equals(f.workingNodes) && color.equals(f.color);
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
		color = new Color(f.color.getRed(), f.color.getGreen(), f.color.getBlue(), f.color.getOpacity());
	}
	
	public void addNode(Node n, FlowBoard f){
		nodes.add(n);
		ArrayList<Coordinate> neighbors = n.loc.getNeighbors(false, false, false, false, n.col, f);
		for (Coordinate c : neighbors){
			workingNodes.remove(f.nodes[c.x][c.y]);
			workingNodes.add(n);
			f.nodes[c.x][c.y].isSolved = true;
			//NOTE: must set isSolved status of new node at node constructor
		}
	}
	
	public void removeNode(Node n){
		nodes.remove(n);
		if (endNodes.contains(n)){
			System.err.print("****** ATTEMPTED TO ILLEGALLY REMOVE NODE ");
			n.loc.print();
		}
	}
}
