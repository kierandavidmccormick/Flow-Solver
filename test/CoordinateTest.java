import org.junit.Before;
import org.junit.Test;
import sample.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kieranmccormick on 12/26/17.
 */
public class CoordinateTest {
	Node a;
	Node b;
	Node c;
	Node d;
	Flow f1;
	Flow f2;
	ArrayList<Flow> flowArrayList = new ArrayList<>(2);
	FlowBoard fl;
	
	@Before
	public void reset(){
		Main.DIM = 7;
		a = new Node(new Coordinate(0, 0), 0, true);
		b = new Node(new Coordinate(4, 0), 0, true);
		c = new Node(new Coordinate(0, 1), 1, true);
		d = new Node(new Coordinate(4, 1), 1, true);
		f1 = new Flow(a, b, 0);
		f2 = new Flow(c, d, 1);
		flowArrayList.add(f1);
		flowArrayList.add(f2);
		fl = new FlowBoard(new HashMap<>(), new HashMap<>(), flowArrayList, new Layer(new Arbor(fl)));
	}
	
	@Test
	public void isOnEdgeTest(){
		assert a.loc.isOnEdge(fl);
		assert b.loc.isOnEdge(fl);
		assert c.loc.isOnEdge(fl);
		assert !d.loc.isOnEdge(fl);
		assert !new Coordinate(5, 5).isOnEdge(fl);
		assert new Coordinate(6, 6).isOnEdge(fl);
	}
	
	@Test
	public void isEmptyTest(){
		assert new Coordinate(1,1).isEmpty(fl);
		assert !new Coordinate(0, 1).isEmpty(fl);
	}
	
	@Test
	public void isInBoundsTest(){
		assert new Coordinate(0, 0).isInBounds();
		assert !new Coordinate(7, 7).isInBounds();
	}
	
	@Test
	public void getNeighborsTest(){
		ArrayList<Coordinate> neighbors = new Coordinate(1, 0).getNeighbors(true, false, true, null, (byte)-1, fl);
		assert neighbors.size() == 3;
	}
}
