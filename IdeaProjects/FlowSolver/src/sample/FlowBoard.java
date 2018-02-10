package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collection;

/**
 * Created by kieranmccormick on 12/26/17.
 */
public class FlowBoard implements Comparable<FlowBoard>{
	public Node[][] nodes;
	ArrayList<Flow> flows;
	//LinkedList<FlowBoard> parents;
	LinkedList<FlowBoard> children;
	Layer layer;
	
	public FlowBoard(FlowBoard f){
		nodes = new Node[f.nodes.length][f.nodes[0].length];
		for (int i = 0; i < f.nodes.length; i++){
			for (int j = 0; j < f.nodes[0].length; j++){
				nodes[i][j] = new Node(f.nodes[i][j]);
			}
		}
		/*
		if (f.parents != null) {
			parents = new LinkedList<>(f.parents);
		} else {
			parents = null;
		}
		*/
		if (f.children != null) {
			children = new LinkedList<>(f.children);
		} else {
			children = null;
		}
		layer = f.layer;
		flows = new ArrayList<>(f.flows.size());
		for (Flow flow : f.flows){
			flows.add(new Flow(flow));
		}
	}
	
	public FlowBoard(Node[][] nodes, ArrayList<Flow> flows, Layer layer){
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
		//parents = new LinkedList<>();
		children = new LinkedList<>();
	}
	
	public FlowBoard(LinkedList<FlowBoard> parents, LinkedList<FlowBoard> children, ArrayList<Flow> flows, Layer layer){
		this.flows = flows;
		//this.parents = parents;
		this.children = children;
		this.layer = layer;
		nodes = new Node[Main.DIM][Main.DIM];
		for(int i = 0; i < Main.DIM; i++){
			for (int j = 0; j < Main.DIM; j++){
				nodes[i][j] = new Node(new Coordinate(i, j), null);
			}
		}
		for (Flow f : flows){
			for (Node n : f.nodes){
				nodes[n.loc.x][n.loc.y] = n;
			}
		}
	}
	
	public FlowBoard(LinkedList<FlowBoard> parents, ArrayList<Flow> flows, Layer layer){
		this(parents, new LinkedList<>(), flows, layer);
	}
	
	public boolean equals(Object o){
		if (o == this){
			return true;
		}
		if (o instanceof FlowBoard){
			FlowBoard f = (FlowBoard)o;
			return flows.equals(f.flows) && /*parents.equals(f.parents) &&*/ children.equals(f.children) && layer.equals(f.layer);
		}
		return false;
	}
	
	public Flow getOneSimple(){
		for (Flow f : flows){
			if (f.endNodes.get(0).loc.isOnEdge(this) && f.endNodes.get(1).loc.isOnEdge(this)){
				return f;
			}
		}
		return null;
	}
	
	public int compareTo(FlowBoard f){
		if (equals(this)){
			return 0;
		}
		return Integer.valueOf(this.toString()) > Integer.valueOf(this.toString()) ? 1 : 0;
	}
	
	public ArrayList<FlowBoard> genChildren(){
		HashSet<FlowBoard> children = new HashSet<>();
		HashSet<Coordinate> neighbors = new HashSet<>();
		for (Flow flow : flows){
			for (Node node : flow.nodes){
				if (!node.isSolved){
					ArrayList<Coordinate> n = node.loc.getNeighbors(true, false, false, null, null, this);
					neighbors.addAll(n);
					for (Coordinate c : n){
					}
				}
			}
		}
		for (Coordinate c : neighbors){
			for (Color color : c.getNeighborColors(false, false, false, null, null, this)){
				FlowBoard newBoard = new FlowBoard(this.nodes, this.flows, this.layer);
				newBoard.addNode(new Node(c, color, false, false), newBoard.getFlow(color));
				if (!newBoard.fatalError(c)){
					children.add(newBoard);
				}
			}
		}
		return new ArrayList<>(children);
	}
	
	public void addCertainMoves(){
		boolean addedNode = false;
		LinkedList<Node> nodes = new LinkedList<>();
		for (Flow f : flows){
			for (Node n : f.workingNodes){
				ArrayList<Coordinate> neighborCoordinates = n.loc.getNeighbors(true, false, false, false, null, this);
				if (neighborCoordinates.size() == 1){
					nodes.add(n);
				}
			}
		}
		for (Node n : nodes){
			ArrayList<Coordinate> neighbors = n.loc.getNeighbors(true, false, false, false, null, this);
			if (neighbors.size() == 1) {
				addNode(new Node(neighbors.get(0), n.col, false, false), getFlow(n.col));
			}
			addedNode = true;
		}
		if (addedNode){
			addCertainMoves();
		}
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
			if (!connected(flow.workingNodes.get(0), flow.workingNodes.get(1))){
				System.out.println(flow.workingNodes.get(0).loc.toString() + " " + flow.workingNodes.get(1).loc.toString());
				return true;
			}
		}
		return !allNodesReachable() || globalFilterCheck();
	}
	
	public Boolean fatalError(Coordinate c){
		for (Flow flow : flows) {
			if (!connected(flow.workingNodes.get(0), flow.workingNodes.get(1))){
				return true;
			}
		}
		return !allNodesReachable() || filterCheck(c);
	}
	
	public Boolean globalFilterCheck(){
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
		Color compColor = nodes[c.x][c.y].col;
		for (int i = 0; i < filter.length; i++){
			for (int j = 0; j < filter[0].length; j++){
				Coordinate n = new Coordinate(c.x - offset.x + i, c.y - offset.y + j);
				if (filter != Filter.BLOCKFILTER || (n.isInBounds() && nodes[n.x][n.y].col != null && nodes[n.x][n.y].col.equals(compColor))) {
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
		LinkedList<Coordinate> connectedCoordinates = getConnectedCoordinates(flows.get(0).workingNodes.get(0).loc);
		for (Coordinate coordinate : connectedCoordinates) {
			inValidNodes[coordinate.x][coordinate.y][0] = true;
		}
		connectedCoordinates = getConnectedCoordinates(flows.get(0).workingNodes.get(1).loc);
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
		while(unConnectedCoordinates.size() > 0){
			for (Flow flow : flows){
				if (connected(flow.workingNodes.get(0).loc, unConnectedCoordinates.get(0)) && connected(flow.workingNodes.get(1).loc, unConnectedCoordinates.get(0))){
					unConnectedCoordinates.removeAll(getConnectedCoordinates(unConnectedCoordinates.get(0)));
					unConnectedCoordinates.remove(0);
					break;
				} else {
					return false;
				}
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
				if (c2.isInBounds() && c2.isEmpty(this) && !inValidNodes[c2.x][c2.y]){
					edgeCoordinates.add(c2);
					inValidNodes[c2.x][c2.y] = true;
				} else if (c2.equals(end)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Boolean connected(Node start, Node end){
		return connected(start.loc, end.loc);
	}
	
	public void addNode(Node n, Flow f){
		f.addNode(n, this);
		nodes[n.loc.x][n.loc.y] = n;
	}
	
	public void addNext(Node n, Node prev, Flow f){
		addNode(n, f);
		prev.isSolved = true;
	}
	
	public void removeNode(Node n, Flow f){
		f.removeNode(n);
		nodes[n.loc.x][n.loc.y] = new Node(new Coordinate(n.loc.x, n.loc.y), null);
	}
	
	public Flow getFlow(Color c){
		for (Flow f : flows) {
			if (f.color.equals(c)){
				return f;
			}
		}
		return null;
	}
}
