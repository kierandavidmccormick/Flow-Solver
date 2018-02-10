package sample;

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
}
