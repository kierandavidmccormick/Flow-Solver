package sample;

import javafx.scene.paint.Color;

/**
 * Created by kieranmccormick on 12/26/17.
 */
public class Node {
	public Coordinate loc;
	public Color col;
	public boolean isSolved;
	public boolean isEnd;
	
	public Node(Coordinate coordinate, Color color, Boolean isEnd, Boolean isSolved){
		loc = coordinate;
		col = color;
		this.isSolved = isSolved;
		this.isEnd = isEnd;
	}
	public Node(Coordinate coordinate, Color color, boolean isEnd){this(coordinate, color, isEnd, false);}
	public Node(Coordinate coordinate, Color color){
		this(coordinate, color, true);
	}
	public Node(Node n){
		loc = new Coordinate(n.loc);
		if (n.col != null) {
			col = new Color(n.col.getRed(), n.col.getGreen(), n.col.getBlue(), n.col.getOpacity());
		} else {
			col = null;
		}
		isSolved = n.isSolved;
		isEnd = n.isEnd;
	}
	
	public String toString(){
		return "Loc: " + loc.toString() + " Col: " + col.toString() + " isSolved:  " + isSolved + " isEnd: " + isEnd;
	}
	
	public boolean equals(Object o) {
		if (o == this){
			return true;
		} else if (o instanceof Node) {
			Node n = (Node)o;
			return loc.equals(n.loc) && col.equals(n.col) && isSolved == n.isSolved && isEnd == n.isEnd;
		}
		return false;
	}
}
