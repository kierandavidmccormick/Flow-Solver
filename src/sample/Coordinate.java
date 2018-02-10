package sample;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by kieranmccormick on 12/26/17.
 */
public class Coordinate extends Object {
	public byte x;
	public byte y;
	
	public Coordinate(byte x, byte y){
		this.x = x;
		this.y = y;
	}
	
	public Coordinate(int x, int y){
		this.x = (byte)x;
		this.y = (byte)y;
	}
	
	public Coordinate(Coordinate c){
		x = c.x;
		y = c.y;
	}
	
	public Boolean checkSquare(FlowBoard f, int compareVal){
		if (compareVal == Filter.ISSQUAREVAL){
			return isSquare(f);
		} else if (compareVal == Filter.ISDENTBLOCKVAL){
			return isDentBlock(f);
		} else if (compareVal == Filter.ISDENTEMPTYVAL){
			return isDentEmpty(f);
		} else if (compareVal == Filter.ISIRRELEVANT){
			return true;
		}
		return null;
	}
	
	public Boolean isSquare(FlowBoard f){
		return isInBounds() && !isEmpty(f);
		//also requires checking if the colors match
	}
	
	public Boolean isDentBlock(FlowBoard f){
		return !isInBounds() || (!isEmpty(f) && f.nodes[x][y].isSolved);
		//either out of bounds, or solved
	}
	
	public Boolean isDentEmpty(FlowBoard f){
		return isInBounds() && isEmpty(f);
	}
	
	public boolean equals(Object o) {
		if (o == this){
			return true;
		} else if (!(o instanceof Coordinate)){
			return false;
		}
		return x == ((Coordinate)o).x && y == ((Coordinate)o).y;
	}
	
	public int hashCode() {
		int result = 17;
		result = 31 * result + x;
		result = 31 * result + y;
		return result;
	}
	
	public void print(){
		System.out.println(x + " " + y);
	}
	
	public String toString(){
		return x + " " + y;
	}
	
	public void setCoordinate(byte x, byte y){
		this.x = x;
		this.y = y;
	}
	
	public void add(Coordinate c){
		x += c.x;
		y += c.y;
	}
	
	public void sub(Coordinate c){
		x -= c.x;
		y -= c.y;
	}
	
	public Coordinate addVal(Coordinate c){
		c.x += x;
		c.y += y;
		return c;
	}
	
	public Coordinate subVal(Coordinate c){
		Coordinate d = this;
		d.x -= c.x;
		d.y -= c.y;
		return d;
	}
	
	public ArrayList<Coordinate> getNeighbors(Boolean empty, Boolean edge, Boolean allowCorner, Boolean solved, byte color, FlowBoard f){
		ArrayList<Coordinate> validNeighbors = new ArrayList<>(allowCorner ? 8 : 4);
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				Coordinate next = addVal(new Coordinate(i, j));
				if (next.isInBounds() && !next.equals(this) && (!empty || next.isEmpty(f)) && (!edge || next.isOnEdge(f)) && (allowCorner || Math.abs(i) + Math.abs(j) < 2) && (solved == null || f.nodes[next.x][next.y].isSolved == solved) && (color == -1 || color == f.nodes[next.x][next.y].colorCode)) {
					validNeighbors.add(next);
				}
			}
		}
		return validNeighbors;
	}
	
	public LinkedList<Byte> getNeighborColors(Boolean empty, Boolean edge, Boolean allowCorner, Boolean solved, byte color, FlowBoard f){
		ArrayList<Coordinate> validNeighbors = getNeighbors(empty, edge, allowCorner, solved, color, f);
		LinkedList<Byte> colors = new LinkedList<>();
		for (Coordinate c : validNeighbors){
			if (f.nodes[c.x][c.y].colorCode != -1 && !colors.contains(f.nodes[c.x][c.y].colorCode)){
				colors.add(f.nodes[c.x][c.y].colorCode);
			}
		}
		return colors;
	}
	
	public Boolean isOnEdge(FlowBoard f){
		if (x == 0 || y == 0 || x == Main.DIM-1 || y == Main.DIM-1){
			return true;
		} else if (f.nodes[x-1][y].colorCode != -1 && f.nodes[x-1][y].isSolved){
			return true;
		} else if (f.nodes[x+1][y].colorCode != -1 && f.nodes[x+1][y].isSolved){
			return true;
		} else if (f.nodes[x][y-1].colorCode != -1 && f.nodes[x][y-1].isSolved){
			return true;
		} else if (f.nodes[x][y+1].colorCode != -1 && f.nodes[x][y+1].isSolved){
			return true;
		} else if (f.nodes[x-1][y-1].colorCode != -1 && f.nodes[x-1][y-1].isSolved){
			return true;
		} else if (f.nodes[x-1][y+1].colorCode != -1 && f.nodes[x-1][y+1].isSolved){
			return true;
		} else if (f.nodes[x+1][y-1].colorCode != -1 && f.nodes[x+1][y-1].isSolved){
			return true;
		} else if (f.nodes[x+1][y+1].colorCode != -1 && f.nodes[x+1][y+1].isSolved){
			return true;
		}
		return false;
	}
	
	public Boolean isInBounds(){
		return x >= 0 && x < Main.DIM && y >= 0 && y < Main.DIM;
	}
	
	public Boolean isEmpty(FlowBoard f){
		return f.nodes[x][y] == null || f.nodes[x][y].colorCode == -1;
	}
	
	public static Direction turnRight(Direction d){
		switch(d) {
			case LEFT: return Direction.UP;
			case UP: return Direction.RIGHT;
			case RIGHT: return Direction.DOWN;
			case DOWN: return Direction.LEFT;
			default: return null;
		}
	}
	
	public static Direction turnLeft(Direction d){
		switch (d){
			case LEFT: return Direction.DOWN;
			case UP: return Direction.LEFT;
			case RIGHT: return Direction.UP;
			case DOWN: return Direction.RIGHT;
			default: return null;
		}
	}
	
	public static Direction invert(Direction d){
		switch (d){
			case LEFT: return Direction.RIGHT;
			case UP: return Direction.DOWN;
			case RIGHT: return Direction.LEFT;
			case DOWN: return Direction.UP;
			default: return null;
		}
	}
	
	public static Direction toDirection(Coordinate c){
		if (c.x == -1){
			return Direction.LEFT;
		} else if (c.x == 1){
			return Direction.RIGHT;
		}
		if (c.y == -1){
			return Direction.UP;
		} else if (c.y == 1){
			return Direction.DOWN;
		}
		return null;
	}
}
