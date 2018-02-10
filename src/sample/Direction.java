package sample;

/**
 * Created by kieranmccormick on 12/26/17.
 */
public enum Direction {
	LEFT(-1, 0), UP(0, -1), RIGHT(1, 0), DOWN(0, 1);
	public final int x;
	public final int y;
	public static Direction[] directions = {LEFT, UP, RIGHT, DOWN};
	Direction(int x, int y){
		this.x = x;
		this.y = y;
	}
	public Coordinate toCoordinate(){
		return new Coordinate(x, y);
	}
}
