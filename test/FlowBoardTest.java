/**
 * Created by kieranmccormick on 12/26/17.
 */

import org.junit.Before;
import org.junit.Test;
import sample.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class FlowBoardTest {
	Node a;
	Node b;
	Node c;
	Node d;
	Flow f1;
	Flow f2;
	Flow f3;
	Flow f4;
	ArrayList<Flow> flowArrayList = new ArrayList<>(2);
	ArrayList<Flow> flowArrayList2 = new ArrayList<>(2);
	FlowBoard fl;
	FlowBoard fl2;
	
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
		fl = new FlowBoard(new HashMap<>(), new HashMap<>(), flowArrayList, null);
		f3 = new Flow(a, b, 0);
		f4 = new Flow(new Node(new Coordinate(3, 0), 1, true), new Node(new Coordinate(3, 6), 1, true), 1);
		for (int i = 1; i < 6; i++){
			f4.nodes.add(new Node(new Coordinate(3, i), 1));
		}
		flowArrayList2.add(f3);
		flowArrayList2.add(f4);
		fl2 = new FlowBoard(new HashMap<>(), new HashMap<>(), flowArrayList2, null);
	}
	
	@Test
	public void checkSquareTest() {
		Node n1 = new Node(new Coordinate(2, 2), 0, true, true);
		Node n2 = new Node(new Coordinate(2, 3), 0, true, true);
		Node n3 = new Node(new Coordinate(3, 2), 0, false, true);
		Node n4 = new Node(new Coordinate(3, 3), 0, false, true);
		Node n5 = new Node(new Coordinate(4, 3), 2, true, true);
		Node n6 = new Node(new Coordinate(4, 4), 2, true, true);
		LinkedList<Node> nodeList = new LinkedList<>();
		nodeList.add(n1);
		nodeList.add(n2);
		nodeList.add(n3);
		nodeList.add(n4);
		Flow f = new Flow(nodeList, n1, n2, 0);
		Flow f2 = new Flow(n5, n6, 2);
		ArrayList<Flow> flAr = new ArrayList<>();
		flAr.add(f);
		flAr.add(f2);
		FlowBoard fb = new FlowBoard(new HashMap<>(), new HashMap<>(), flAr, null);
		assert fb.checkSquare(new Coordinate(2, 2), new Coordinate(0, 0), Filter.BLOCKFILTER);
		assert !fb.checkSquare(new Coordinate(2, 2), new Coordinate(1, 0), Filter.BLOCKFILTER);
		assert fb.filterCheck(new Coordinate(2, 2));
		assert fb.filterCheck(new Coordinate(3, 3));
		assert !fb.checkSquare(new Coordinate(2, 2), new Coordinate(0, 1), Filter.BLOCKFILTER);
		assert !fb.checkSquare(new Coordinate(0, 0), new Coordinate(-1, -1), Filter.BLOCKFILTER);
		assert !fb.checkSquare(new Coordinate(0, 0), new Coordinate(0, 0), Filter.BLOCKFILTER);
		assert !fb.checkSquare(new Coordinate(3, 2), new Coordinate(0, 0), Filter.BLOCKFILTER);
		assert !fb.checkSquare(new Coordinate(4, 2), new Coordinate(0, 0), Filter.BLOCKFILTER);
		fb.addNode(new Node(new Coordinate(5, 2), 2, false, true), f2);
		assert fb.checkSquare(new Coordinate(4, 2), new Coordinate(1, 1), Filter.DENT1FILTERUP);
		assert fb.checkSquare(new Coordinate(4, 3), new Coordinate(1, 2), Filter.DENT1FILTERUP);
		assert !fb.checkSquare(new Coordinate(4, 2), new Coordinate(0, 0), Filter.DENT1FILTERUP);
		assert !fb.checkSquare(new Coordinate(4, 2), new Coordinate(1, 1), Filter.DENT1FILTERDOWN);
		Node n7 = new Node(new Coordinate(2, 4), 2, false, true);
		fb.addNode(n7, f2);
		assert fb.checkSquare(new Coordinate(3, 4), new Coordinate(1, 1), Filter.DENT1FILTERDOWN);
		Node n8 = new Node(new Coordinate(5, 4), 2, false, true);
		fb.addNode(n8, f2);
		assert fb.checkSquare(new Coordinate(5, 3), new Coordinate(1, 1), Filter.DENT1FILTERRIGHT);
		fb.removeNode(n7, f2);
		Node n9 = new Node(new Coordinate(3, 5), 2, false, true);
		fb.addNode(n9, f2);
		assert fb.checkSquare(new Coordinate(3, 4), new Coordinate(1, 1), Filter.DENT1FILTERLEFT);
		fb.removeNode(n9, f2);
		fb.addNode(new Node(new Coordinate(1, 4), 2, false, true), f2);
		assert fb.checkSquare(new Coordinate(2, 4), new Coordinate(1, 1), Filter.DENT2FILTERDOWN);
		fb.addNode(new Node(new Coordinate(1, 1), 2, false, true), f2);
		assert fb.checkSquare(new Coordinate(1, 1), new Coordinate(1, 0), Filter.DENT2FILTERLEFT);
		fb.addNode(new Node(new Coordinate(4, 1), 2, false, true), f2);
		assert fb.checkSquare(new Coordinate(2, 1), new Coordinate(1, 1), Filter.DENT2FILTERUP);
		fb.removeNode(n8, f2);
		fb.addNode(new Node(new Coordinate(5, 5), 2, false, true), f2);
		assert fb.checkSquare(new Coordinate(5, 5), new Coordinate(1, 3), Filter.DENT2FILTERRIGHT);
		assert !fb.checkSquare(new Coordinate(5, 0), new Coordinate(1, 3), Filter.DENT2FILTERRIGHT);
		assert !fb.filterCheck(new Coordinate(0, 0));
		assert fb.filterCheck(new Coordinate(1, 2));
		assert fb.filterCheck(new Coordinate(4, 4));
		assert fb.filterCheck(new Coordinate(5, 5));
		assert fb.globalFilterCheck();
	}
	
	@Test
	public void connectedTest(){
		assert fl.connected(a, b);
		assert !fl2.connected(a, b);
	}
	
	@Test
	public void allNodesReachableTest(){
		assert fl.allNodesReachable();
		assert !fl2.allNodesReachable();
	}
}
