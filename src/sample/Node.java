package sample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by kieranmccormick on 12/26/17.
 */
public class Node {
	public Coordinate loc;
	public byte colorCode;
	public boolean isSolved;
	public boolean isEnd;
	
	public Node(Coordinate coordinate, int color, Boolean isEnd, Boolean isSolved){
		loc = coordinate;
		colorCode = (byte)color;
		this.isSolved = isSolved;
		this.isEnd = isEnd;
	}
	public Node(Coordinate coordinate, int color, boolean isEnd){this(coordinate, color, isEnd, false);}
	public Node(Coordinate coordinate, int color){
		this(coordinate, color, true);
	}
	public Node(Node n){
		loc = new Coordinate(n.loc);
		if (n.colorCode != -1) {
			colorCode = n.colorCode;
		} else {
			colorCode = -1;
		}
		isSolved = n.isSolved;
		isEnd = n.isEnd;
	}
	
	public String toString(){
		return "Loc: " + loc.toString() + " Col: " + ColorSet.colorNames[colorCode] + " isSolved:  " + isSolved + " isEnd: " + isEnd;
	}
	
	public boolean equals(Object o) {
		if (o == this){
			return true;
		} else if (o instanceof Node) {
			Node n = (Node)o;
			return loc.equals(n.loc) && colorCode == n.colorCode && isSolved == n.isSolved && isEnd == n.isEnd;
		}
		return false;
	}
	
	public int hashCode(){
		int result = 17;
		result = 31 * result + loc.hashCode();
		result = 31 * result + colorCode;
		return result;
	}
	
	public LinkedList<FlowBoard> getBoardChildren(FlowBoard f){
		//HashSet<FlowBoard> newBoards = new HashSet<>(endNodes.get(0).loc.getNeighbors(true, false, true, false, (byte)-1, f).size() + endNodes.get(0).loc.getNeighbors(true, false, true, false, (byte)-1, f).size());
		LinkedList<FlowBoard> newBoards = new LinkedList<>();
		ArrayList<Coordinate> newNodes = loc.getNeighbors(true, false, false, null, (byte)-1, f);
		for (Coordinate c : newNodes){
			FlowBoard newBoard = new FlowBoard(f);
			newBoard.layer = null;
			newBoard.parents.clear();
			newBoard.children.clear();
			newBoard.addNode(new Node(c, colorCode, true, false), newBoard.getFlow(colorCode));
			if (!c.isOnEdge(f)){
				newBoard.priorityrating--;
			}
			if (!newBoard.fatalError()) {
				newBoards.add(newBoard);
			}
		}
		for (FlowBoard fl : newBoards){
			if (newBoards.size() > 1){
				fl.priorityrating -= 1;
			} else {
				fl.priorityrating = f.priorityrating;
			}
		}
		return newBoards;
	}
}
