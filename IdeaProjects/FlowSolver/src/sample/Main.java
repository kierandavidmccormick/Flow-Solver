package sample;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;

//TODO: implement equals and hashCode properly in all classes
//TODO: switch to depth-first/beam search
//TODO: add compressed versions of flowBoards, nodes, colors, etc.
//TODO: add isSolved method to flowBoards
//TODO: add immediate addition of certain moves
//TODO: add isSolved field to flows, provisions for
//TODO: add to gitHub

public class Main extends Application {

	public static int DIM = 7;
	Group squares = new Group();
	Rectangle incRectangle = new Rectangle(0, DIM * 50, (DIM * 50) / 3, 50);
	Rectangle decRectangle = new Rectangle((DIM * 50) / 1.5, DIM * 50, (DIM * 50) / 3, 50);
	Rectangle runRectangle = new Rectangle((DIM * 50) / 3, DIM * 50, (DIM * 50) / 3, 50);
	Group interactables = new Group(incRectangle, decRectangle, runRectangle);
	Rectangle[][] squareArray = new Rectangle[DIM][DIM];
	Group root = new Group(squares, interactables);
	ArrayList<FlowBoard> flowBoards = new ArrayList<>();
	int flowBoardIndex = 0;
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("FlowSolver");
        incRectangle.setFill(Color.RED);
        decRectangle.setFill(Color.BLUE);
        runRectangle.setFill(Color.BROWN);
        incRectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
	        flowBoardIndex = flowBoardIndex == flowBoards.size() - 1 ? 0 : flowBoardIndex + 1;
	        System.out.println("Clicked: " + flowBoardIndex);
	        updateGUI();
        });
        decRectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
        	flowBoardIndex = flowBoardIndex == 0 ? flowBoards.size() - 1 : flowBoardIndex - 1;
	        System.out.println("Clicked: " + flowBoardIndex);
        	updateGUI();
        });
        runRectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
        	System.out.println("Result: " + flowBoards.get(flowBoardIndex).fatalError());
        });
	    for (int i = 0; i < DIM; i++){
	    	for (int j = 0; j < DIM; j++){
	    		squareArray[i][j] = new Rectangle(i * 50, j * 50, 50, 50);
		    }
	    }
	    
	    ArrayList<Flow> flowList = new ArrayList<>();
	    flowList.add(new Flow(new Node(new Coordinate(0, 0), Color.RED), new Node(new Coordinate(6, 0), Color.RED), Color.RED));
	    flowList.add(new Flow(new Node(new Coordinate(0, 1), Color.BLUE), new Node(new Coordinate(6, 6), Color.BLUE), Color.BLUE));
	    flowList.add(new Flow(new Node(new Coordinate(1, 1), Color.GREEN), new Node(new Coordinate(6, 1), Color.GREEN), Color.GREEN));
	    flowList.add(new Flow(new Node(new Coordinate(1, 2), Color.ORANGE), new Node(new Coordinate(6, 2), Color.ORANGE), Color.ORANGE));
	    flowList.add(new Flow(new Node(new Coordinate(1, 3), Color.PURPLE), new Node(new Coordinate(6, 3), Color.PURPLE), Color.PURPLE));
	    flowList.add(new Flow(new Node(new Coordinate(1, 4), Color.DARKGRAY), new Node(new Coordinate(6, 4), Color.DARKGRAY), Color.DARKGRAY));
	    flowList.add(new Flow(new Node(new Coordinate(1, 5), Color.YELLOW), new Node(new Coordinate(6, 5), Color.YELLOW), Color.YELLOW));
	    FlowBoard fl = new FlowBoard(new LinkedList<>(), new LinkedList<>(), flowList, null);
	    long t1 = System.currentTimeMillis();
	    Arbor ar = new Arbor(fl);
	    while (ar.layers.getLast().boards.size() > 0){
		    ar.genLayerDepthFirst();
		    System.out.println("Time taken: " + (System.currentTimeMillis() - t1) + " millis, size: " + ar.layers.getLast().boards.size() + ", memory used (mb): " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
	    }
	    /*
	    ar.genLayer();
	    System.out.println("Time taken: " + (System.currentTimeMillis() - t1) + " millis, size: " + ar.layers.getLast().boards.size() + ", memory used (mb): " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
	    ar.genLayer();
	    System.out.println("Time taken: " + (System.currentTimeMillis() - t1) + " millis, size: " + ar.layers.getLast().boards.size() + ", memory used (mb): " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
	    ar.genLayer();
	    System.out.println("Time taken: " + (System.currentTimeMillis() - t1) + " millis, size: " + ar.layers.getLast().boards.size() + ", memory used (mb): " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
	    ar.genLayer();
	    System.out.println("Time taken: " + (System.currentTimeMillis() - t1) + " millis, size: " + ar.layers.getLast().boards.size() + ", memory used (mb): " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
	    try {
		    ar.genLayer();
		    System.out.println("Time taken: " + (System.currentTimeMillis() - t1) + " millis, size: " + ar.layers.getLast().boards.size() + ", memory used (mb): " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
	    } catch (OutOfMemoryError e){
	    	System.out.println(e.getMessage());
		    System.out.println("Time taken: " + (System.currentTimeMillis() - t1) + " millis, size: " + ar.layers.getLast().boards.size() + ", memory used (mb): " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
	    }
	    */
	    //ar.genLayersForever();
	    for (Layer l : ar.layers){
	    	for (FlowBoard board : l.boards){
	    		if (board.children.size() > 0){
	    			flowBoards.add(board);
			    }
		    }
	    	//flowBoards.addAll(l.boards);
	    }
	    updateGUI();
	    primaryStage.setScene(new Scene(root, DIM * 50, DIM * 50 + 50));
        primaryStage.show();
	    //Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your program has completed");
        //alert.showAndWait();
    }
	
    public void updateGUI(){
    	setSquareArray(flowBoards.get(flowBoardIndex));
    	if (squares.getChildren().size() == 0) {
		    createSquares(squareArray);
	    }
    }
    
    public void createSquares(Rectangle[][] squareArray){
	    for (int i = 0; i < DIM; i++) {
		    for (int j = 0; j < DIM; j++) {
		    	if (squareArray[i][j] != null) {
				    squares.getChildren().add(squareArray[i][j]);
			    }
		    }
	    }
    }
    
    public void setSquareArray(FlowBoard f){
	    for (int i = 0; i < 7; i++) {
		    for (int j = 0; j < 7; j++) {
		    	if (f.nodes[i][j].col != null) {
				    squareArray[i][j].setFill(f.nodes[i][j].col);
			    } else {
				    squareArray[i][j].setFill(Color.BLACK);
			    }
		    }
	    }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
