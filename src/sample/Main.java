package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

//TODO: implement equals and hashCode properly in all classes
//TODO: switch to depth-first/beam search
//TODO: add compressed versions of flowBoards, nodes, etc.

public class Main extends Application {

	public static int DIM = 9;
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
        FlowBoard fl = new FlowBoard(8, 2, 0, 7, 8, 4, 0, 8, 4, 4, 4, 7, 6, 5, 3, 6, 2, 8, 6, 7, 3, 7, 5, 8);
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
	        updateGUI();
        });
	    for (int i = 0; i < DIM; i++){
	    	for (int j = 0; j < DIM; j++){
	    		squareArray[i][j] = new Rectangle(i * 50, j * 50, 50, 50);
		    }
	    }
	    fl.fatalError();
	    long t1 = System.currentTimeMillis();
	    Arbor ar = new Arbor(fl);
	    fl.addCertainMoves(true);
	    
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
	    		//if (board.children.size() > 0){
	    			flowBoards.add(board);
			    //}
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
	    for (int i = 0; i < DIM; i++) {
		    for (int j = 0; j < DIM; j++) {
		    	if (f.nodes[i][j].colorCode != -1) {
				    squareArray[i][j].setFill(ColorSet.colorArray[f.nodes[i][j].colorCode]);
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
