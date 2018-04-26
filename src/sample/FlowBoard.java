package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by kieranmccormick on 12/26/17.
 */
public class FlowBoard implements Comparable<FlowBoard>{
	public Node[][] nodes;
	ArrayList<Flow> flows;
	HashSet<FlowBoard> parents;
	HashSet<FlowBoard> children;
	Layer layer;
	boolean isLeaf;
	
	public FlowBoard(int... locArgs){
		nodes = new Node[Main.DIM][Main.DIM];
		for(int i = 0; i < Main.DIM; i++){
			for (int j = 0; j < Main.DIM; j++){
				nodes[i][j] = new Node(new Coordinate(i, j), (byte)-1);
			}
		}
		LinkedList<Flow> newFlows = new LinkedList<>();
		children = new HashSet<>();
		for (int i = 0; i < locArgs.length; i+=4){
			Coordinate c1 = new Coordinate(locArgs[i], locArgs[i+1]);
			Coordinate c2 = new Coordinate(locArgs[i+2], locArgs[i+3]);
			Node n1 = new Node(c1, (byte)(i/4), true, false);
			Node n2 = new Node(c2, (byte)(i/4), true, false);
			nodes[c1.x][c1.y] = n1;
			nodes[c2.x][c2.y] = n2;
			newFlows.add(new Flow(n1, n2, (byte)(i/4)));
		}
		flows = new ArrayList<>(newFlows);
		for (Flow flow : flows){
			for (Node n : flow.nodes){
				nodes[n.loc.x][n.loc.y] = n;
			}
		}
		parents = new HashSet<>(0);
		isLeaf = false;
	}
	
	public FlowBoard(FlowBoard f){
		nodes = new Node[f.nodes.length][f.nodes[0].length];
		for (int i = 0; i < f.nodes.length; i++) {
			for (int j = 0; j < f.nodes[0].length; j++) {
				nodes[i][j] = new Node(f.nodes[i][j]);
			}
		}
		if (f.parents != null) {
			parents = new HashSet<>(f.parents);
		} else {
			parents = new HashSet<>();
		}
		if (f.children != null) {
			children = new HashSet<>(f.children);
		} else {
			children = new HashSet<>();
		}
		layer = f.layer;
		flows = new ArrayList<>(f.flows.size());
		for (Flow flow : f.flows){
			flows.add(new Flow(flow));
		}
		for (Flow flow : flows){
			for (Node n : flow.nodes){
				nodes[n.loc.x][n.loc.y] = n;
			}
		}
		isLeaf = false;
	}
	
	public FlowBoard(Node[][] nodes, ArrayList<Flow> flows, Layer layer){
		//NOTE: not guaranteed not to possess duplicated nodes, do not use outside of proscribed test settings
		this.nodes = new Node[nodes.length][nodes[0].length];
		for (int i = 0; i < nodes.length; i++){
			for (int j = 0; j < nodes[0].length; j++){
				this.nodes[i][j] = new Node(nodes[i][j]);
			}
		}
		this.flows = new ArrayList<>(flows.size());
		for (Flow flow : flows){
			this.flows.add(new Flow(flow));
		}
		this.layer = layer;
		parents = new HashSet<>();
		children = new HashSet<>();
		isLeaf = false;
	}
	
	public FlowBoard(HashSet<FlowBoard> parents, HashSet<FlowBoard> children, ArrayList<Flow> flows, Layer layer){
		//NOTE: not guaranteed not to possess duplicated nodes, do not use outside of proscribed test settings
		this.flows = flows;
		this.parents = parents;
		this.children = children;
		this.layer = layer;
		nodes = new Node[Main.DIM][Main.DIM];
		for(int i = 0; i < Main.DIM; i++){
			for (int j = 0; j < Main.DIM; j++){
				nodes[i][j] = new Node(new Coordinate(i, j), (byte)-1);
			}
		}
		for (Flow f : flows){
			for (Node n : f.nodes){
				nodes[n.loc.x][n.loc.y] = n;
			}
		}
		isLeaf = false;
	}
	
	public FlowBoard(HashSet<FlowBoard> parents, ArrayList<Flow> flows, Layer layer){
		this(parents, new HashSet<>(), flows, layer);
	}
	
	public boolean equals(Object o){
		if (o == this){
			return true;
		}
		if (o instanceof FlowBoard){
			FlowBoard f = (FlowBoard)o;
			return flows.equals(f.flows) /*&& parents.equals(f.parents) && children.equals(f.children) && layer.equals(f.layer)*/;
		}
		return false;
	}
	
	public int hashCode(){
		int result = 17;
		result = 31 * result + Arrays.deepHashCode(nodes);
		return result;
	}
	
	public Flow getOneSimple(){
		for (Flow f : flows){
			if (f.endNodes.get(0).loc.isOnEdge(this) && f.endNodes.get(1).loc.isOnEdge(this)){
				return f;
			}
		}
		return null;
	}
	
	public int compareTo(FlowBoard f) {
		if (equals(f)) {
			return 0;
		}
		try {
			return toInt() > f.toInt() ? 1 : -1;
		} catch (Exception e){
			return 0;
		}
	}
	
	public int toInt(){
		char[] chars = this.toString().toCharArray();
		int val = 0;
		for (Character c : chars){
			val = 31 * val + c;
		}
		return val;
	}
	
	/*
	public ArrayList<FlowBoard> genChildren(){
		HashSet<FlowBoard> children = new HashSet<>();
		HashSet<Coordinate> neighbors = new HashSet<>();
		for (Flow flow : flows){
			for (Node node : flow.workingNodes){
				ArrayList<Coordinate> n = node.loc.getNeighbors(true, false, false, null, (byte)-1, this);
				neighbors.addAll(n);
			}
		}
		for (Coordinate c : neighbors){
			for (byte color : c.getNeighborColors(false, false, false, null, (byte)-1, this)){
				FlowBoard newBoard = new FlowBoard(this.nodes, this.flows, this.layer);
				if (!newBoard.getFlow(color).isSolved) {
					newBoard.addNode(new Node(c, color, false, false), newBoard.getFlow(color));
				}
				if (!newBoard.fatalError(c)){
					children.add(newBoard);
				}
			}
		}
		return new ArrayList<>(children);
	}
	*/
	public HashSet<FlowBoard> getApplicableChildren(){
		HashSet<FlowBoard> newBoards = new HashSet<>();
		for (Flow f : flows){
			for (Node n : f.workingNodes){
				LinkedList<FlowBoard> newBoardsTemp = n.getBoardChildren(this);
				if (newBoardsTemp.size() == 1){
					return new HashSet<>(newBoardsTemp);
				} else if (newBoardsTemp.size() == 0) {
					markAsLeaf();
					return new HashSet<>(0);
				}
				newBoards.addAll(newBoardsTemp);
			}
		}
		return newBoards;
	}
	
	public HashSet<FlowBoard> setAsParentOf(HashSet<FlowBoard> fs){
		children.addAll(fs);
		for (FlowBoard f : fs){
			f.parents.add(this);
		}
		return fs;
	}
	
	public FlowBoard setAsParentOf(FlowBoard f){
		children.add(f);
		f.parents.add(this);
		return f;
	}
	
	public boolean addCertainMoves(boolean rec){
		boolean addedNode = false;
		LinkedList<Node> nodes = new LinkedList<>();
		for (Flow f : flows){
			for (Node n : f.workingNodes){
				ArrayList<Coordinate> neighborCoordinates = n.loc.getNeighbors(true, false, false, false, (byte)-1, this);
				if (neighborCoordinates.size() == 1){
					nodes.add(n);
					break;
				}
			}
		}
		for (Node n : nodes){
			ArrayList<Coordinate> neighbors = n.loc.getNeighbors(true, false, false, false, (byte)(-1), this);
			if (neighbors.size() == 1) {
				Flow f = getFlow(n.colorCode);
				//f.resolveSolved(this);
				addNode(new Node(neighbors.get(0), n.colorCode, false, false), f);
			}
			addedNode = true;
		}
		if (rec && addedNode){
			addCertainMoves(true);
		}
		return addedNode;
	}
	
	/*
	public boolean addCertainMoves(boolean rec){
		Node node = null;
		for (Flow f : flows){
			for (Node n : f.workingNodes){
				ArrayList<Coordinate> neighborCoordinates = n.loc.getNeighbors(true, false, false, false, null, this);
				if (neighborCoordinates.size() == 1){
					node = new Node(neighborCoordinates.get(0), f.colorCode, false, false);
				}
			}
		}
		if (node != null) {
			Flow f = getFlow(node.colorCode);
			addNode(node, f);
			f.resolveSolved(this);
			if (rec){
				addCertainMoves(true);
			}
			return true;
		}
		return false;
	}
	*/
	public boolean isSolved(){
		//TODO: ensure that all squares are filled (?)
		for (Flow f : flows){
			if (!f.isSolved){
				return false;
			}
		}
		return true;
	}
	/*
	public Boolean addNextChildren(Node n){
		ArrayList<Coordinate> nextChildrenRec = n.loc.getNeighbors(true, false, this);
		Boolean addedChild = false;
		for (Coordinate c : nextChildrenRec) {
			FlowBoard newF = this;
			if (c.isOnEdge(this)){
				newF.priorityRating *= 2;
				//TODO: adjust?
			} else {
				newF.priorityRating /= 2;
			}
			newF.addNext(new Node(c, n.colorCode, false, false), n, getFlow(n.colorCode));
			addChild(newF);
			addedChild = true;
		}
		return addedChild;
	}
	*/
	/*
	public Flow getNextFlow(){
		for (Flow flow : flows) {
			Node a = flow.endNodes.get(0);
			Node b = flow.endNodes.get(1);
			if (!(a.isSolved && b.isSolved) && a.loc.isOnEdge(this) && b.loc.isOnEdge(this)){
				return flow;
			}
		}
		for (Flow flow : flows){
			Node a = flow.endNodes.get(0);
			Node b = flow.endNodes.get(1);
			if ((!a.isSolved && b.isSolved) && (a.loc.isOnEdge(this) || b.loc.isOnEdge(this))){
				return flow;
			}
		}
		for (Flow flow : flows){
			Node a = flow.endNodes.get(0);
			Node b = flow.endNodes.get(1);
			if (!(a.isSolved && b.isSolved)){
				return flow;
			}
		}
		return null;
	}
	*/
	public Boolean fatalError(){
		for (Flow flow : flows) {
			if (!flow.isSolved && !connected(flow.workingNodes.get(0), flow.workingNodes.get(1))){
				//System.out.println("Flow " + ColorSet.colorNames[flow.colorCode] + " not connected");
				return true;
			}
		}
		return !allNodesReachable() || globalFilterCheck();
	}
	
	public Boolean fatalError(Coordinate c){
		for (Flow flow : flows) {
			if (flow.isSolved || !connected(flow.workingNodes.get(0), flow.workingNodes.get(1))){
				return true;
			}
		}
		return !allNodesReachable() || filterCheck(c);
	}
	
	public Boolean globalFilterCheck(){
		int r = 0;
		for (int[][] filter : Filter.filters) {
			int i;
			int j;
			for (i = 0; i < nodes.length; i++) {
				for (j = 0; j < nodes[0].length; j++) {
					if (i != nodes.length - 1 && j != nodes[0].length) {
						if (checkSquare(new Coordinate(i, j), new Coordinate(filter.length, filter[0].length), filter)) {
							return true;
						}
					} else {
						if (filterCheck(new Coordinate(i, j))){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public Boolean filterCheck(Coordinate c){
		for (int[][] filter : Filter.filters) {
			for (int i = 0; i < filter.length; i++){
				for (int j = 0; j < filter[0].length; j++){
					if (checkSquare(c, new Coordinate(i, j), filter)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Boolean checkSquare(Coordinate c, Coordinate offset, int[][] filter){
		Coordinate tc = new Coordinate(c.x - offset.x, c.y - offset.y);
		byte compColor = tc.isInBounds() ? nodes[tc.x][tc.y].colorCode : -1;
		for (int i = 0; i < filter.length; i++){
			for (int j = 0; j < filter[0].length; j++){
				Coordinate n = new Coordinate(c.x - offset.x + i, c.y - offset.y + j);
				if (filter != Filter.BLOCKFILTER || (n.isInBounds() && nodes[n.x][n.y].colorCode != -1 && nodes[n.x][n.y].colorCode == compColor)) {    //Questionable
					if (!n.checkSquare(this, filter[i][j])) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
	public Boolean allNodesReachable(){
		//TODO: optimize this whole process
		boolean[][][] inValidNodes = new boolean[nodes.length][nodes[0].length][2];
		//LinkedList<Coordinate> connectedCoordinates = getConnectedCoordinates(flows.get(0).workingNodes.get(0).loc);
		LinkedList<Coordinate> connectedCoordinates = new LinkedList<>();
		/*
		try {
			connectedCoordinates = getConnectedCoordinates(flows.get(0).workingNodes.get(0).loc);
		} catch (IndexOutOfBoundsException e) {
			int i = 0;
			connectedCoordinates = new LinkedList<>();
		}
		*/
		for (Flow f : flows){
			if (f.workingNodes.size() > 0){
				connectedCoordinates.addAll(getConnectedCoordinates(f.workingNodes.get(0).loc));
				break;
			}
		}
		for (Coordinate coordinate : connectedCoordinates) {
			inValidNodes[coordinate.x][coordinate.y][0] = true;
		}
		connectedCoordinates.clear();
		for (Flow f : flows){
			if (f.workingNodes.size() > 1){
				connectedCoordinates.addAll(getConnectedCoordinates(f.workingNodes.get(1).loc));
				break;
			}
		}
		//connectedCoordinates = getConnectedCoordinates(flows.get(0).workingNodes.get(1).loc);
		for (Coordinate coordinate : connectedCoordinates) {
			inValidNodes[coordinate.x][coordinate.y][1] = true;
		}
		LinkedList<Coordinate> unConnectedCoordinates = new LinkedList<>();
		for (int i = 0; i < inValidNodes.length; i++) {
			for (int j = 0; j < inValidNodes[0].length; j++) {
				if (!(inValidNodes[i][j][0] && inValidNodes[i][j][1]) && !nodes[i][j].isSolved) {        //new
					unConnectedCoordinates.add(new Coordinate(i, j));
				}
			}
		}
		/* old method, does not work
		while(unConnectedCoordinates.size() > 0){
			for (Flow flow : flows){
				if (!flow.isSolved) {
					if (connected(flow.workingNodes.get(0).loc, unConnectedCoordinates.get(0)) && connected(flow.workingNodes.get(1).loc, unConnectedCoordinates.get(0))) {
						unConnectedCoordinates.removeAll(getConnectedCoordinates(unConnectedCoordinates.get(0)));
						unConnectedCoordinates.remove(0);
						break;
					} else {
						return false;
					}
				}
			}
		}
		*/
		for (int i = 0; i < unConnectedCoordinates.size(); i++){
			boolean isConnected = false;
			for (Flow flow : flows){
				if (!flow.isSolved){
					if (connected(flow.workingNodes.get(0).loc, unConnectedCoordinates.get(0)) && connected(flow.workingNodes.get(1).loc, unConnectedCoordinates.get(0))) {
						unConnectedCoordinates.removeAll(getConnectedCoordinates(unConnectedCoordinates.get(0)));
						if (unConnectedCoordinates.size() == 0){
							return true;
						}
						unConnectedCoordinates.remove(0);
						i = 0;
						isConnected = true;
						break;
					}
				}
			}
			if (!isConnected){
				return false;
			}
		}
		return true;
	}
	
	public LinkedList<Coordinate> getConnectedCoordinates(Coordinate start){
		boolean[][] inValidNodes = new boolean[nodes.length][nodes[0].length];
		LinkedList<Coordinate> edgeCoordinates = new LinkedList<>();
		edgeCoordinates.add(start);
		while(edgeCoordinates.size() > 0){
			Coordinate c = edgeCoordinates.get(0);
			edgeCoordinates.remove(0);
			for (Direction d : Direction.directions) {
				Coordinate c2 = c.addVal(d.toCoordinate());
				if (c2.isInBounds() && !inValidNodes[c2.x][c2.y]){      //new
					inValidNodes[c2.x][c2.y] = true;
					if (c2.isEmpty(this)){
						edgeCoordinates.add(c2);
					}
				}
			}
		}
		LinkedList<Coordinate> connectedCoordinates = new LinkedList<>();
		for (int i = 0; i < inValidNodes.length; i++){
			for (int j = 0; j < inValidNodes[0].length; j++){
				if (inValidNodes[i][j]){
					connectedCoordinates.add(new Coordinate(i, j));
				}
			}
		}
		return connectedCoordinates;
	}
	
	public Boolean connected(Coordinate start, Coordinate end){
		boolean[][] inValidNodes = new boolean[nodes.length][nodes[0].length];
		LinkedList<Coordinate> edgeCoordinates = new LinkedList<>();
		edgeCoordinates.add(start);
		while(edgeCoordinates.size() > 0){
			Coordinate c = edgeCoordinates.get(0);
			edgeCoordinates.remove(0);
			for (Direction d : Direction.directions) {
				Coordinate c2 = c.addVal(d.toCoordinate());
				if (c2.equals(end)){
					return true;
				} else if (c2.isInBounds() && c2.isEmpty(this) && !inValidNodes[c2.x][c2.y]) {
					edgeCoordinates.add(c2);
					inValidNodes[c2.x][c2.y] = true;
				}
			}
		}
		return false;
	}
	
	public Boolean connected(Node start, Node end){
		return connected(start.loc, end.loc);
	}
	
	public void addNode(Node n, Flow f){
		nodes[n.loc.x][n.loc.y] = n;
		f.addNode(n, this);
		
	}
	/*
	//functionality replaced
	public void addNext(Node n, Node prev, Flow f){
		addNode(n, f);
		prev.isSolved = true;
	}
	*/
	public void removeNode(Node n, Flow f){
		f.removeNode(n);
		nodes[n.loc.x][n.loc.y] = new Node(new Coordinate(n.loc.x, n.loc.y), (byte)-1);
	}
	
	public Flow getFlow(byte c){
		for (Flow f : flows) {
			if (f.colorCode == c){
				return f;
			}
		}
		return null;
	}
	
	public void markAsLeaf(){
		isLeaf = true;
		if (parents.size() > 0){
			for (FlowBoard p : parents){
				boolean setIsLeaf = true;
				for (FlowBoard c : p.children){
					if (c.children.size() > 0 && !c.isLeaf){
						setIsLeaf = false;
						break;
					}
				}
				if (setIsLeaf){
					p.markAsLeaf();
				}
			}
		}
	}
}
