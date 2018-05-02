package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

//TODO: add compressed versions of flowBoards, nodes, etc. (only if actually necessary, new techniques may obviate this)
//TODO: checking for solved boards is still not working (or possibly no solved boards have been encountered)
//TODO: actual handling for parents (may not be practicable, although may or may not be needed for deletion, not needed for backtracking)
//TODO: ensure that hashTables contain no duplicate keys or values

public class Main extends Application {

	public static int DIM = 8;
	Group squares = new Group();
	Rectangle incRectangle = new Rectangle(0, DIM * 50, (DIM * 50) / 3, 50);
	Rectangle decRectangle = new Rectangle((DIM * 50) / 1.5, DIM * 50, (DIM * 50) / 3, 50);
	Rectangle runRectangle = new Rectangle((DIM * 50) / 3, DIM * 50, (DIM * 50) / 3, 50);
	Rectangle incRectangle2 = new Rectangle(0, DIM * 50 + 50, (DIM * 50) / 3, 50);
	Rectangle decRectangle2 = new Rectangle((DIM * 50) / 1.5, DIM * 50 + 50, (DIM * 50) / 3, 50);
	Group interactables = new Group(incRectangle, decRectangle, runRectangle, incRectangle2, decRectangle2);
	Rectangle[][] squareArray = new Rectangle[DIM][DIM];
	Group root = new Group(squares, interactables);
	Arbor ar;
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("FlowSolver");
        //FlowBoard fl = new FlowBoard(8, 2, 0, 7, 8, 4, 0, 8, 4, 4, 4, 7, 6, 5, 3, 6, 2, 8, 6, 7, 3, 7, 5, 8);
	    //FlowBoard fl = new FlowBoard(2, 0, 6, 8, 3, 0, 8, 5, 4, 0, 8, 1, 2, 1, 6, 0, 3, 2, 2, 3, 5, 5, 0, 6, 0, 7, 5, 8, 7, 7, 9, 9, 8, 7, 9, 8, 0, 8, 3, 9, 1, 8, 7, 9);
	    //FlowBoard fl = new FlowBoard(0,0,4,4, 1,1,2,4, 1,2,2,7, 2,1,5,6, 3,1,2,6, 4,7,7,2);   //10x10
	    //FlowBoard fl = new FlowBoard(0,2,3,1, 1,1,3,3, 3,0,7,0, 4,1,4,3, 1,3,4,4, 0,3,0,7, 0,5,6,2);  //8x8
	    FlowBoard fl = new FlowBoard(0,0,5,2, 0,1,7,7, 1,1,4,2, 6,0,2,6, 7,0,3,4, 2,3,7,6, 3,6,6,6);  //8x8
	    ar = new Arbor(fl);
	    incRectangle.setFill(Color.RED);
        decRectangle.setFill(Color.BLUE);
        runRectangle.setFill(Color.BROWN);
        incRectangle2.setFill(Color.DARKRED);
        decRectangle2.setFill(Color.DARKBLUE);
        incRectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
	        ar.changeViewIndex(1);
	        System.out.println("Layer: " + ar.viewIndex + ", Board: " + ar.layers.get(ar.viewIndex).viewIndex + "/" + (ar.layers.get(ar.viewIndex).boards.size()-1));
	        updateGUI();
        });
        decRectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
        	ar.changeViewIndex(-1);
        	System.out.println("Layer: " + ar.viewIndex + ", Board: " + ar.layers.get(ar.viewIndex).viewIndex + "/" + (ar.layers.get(ar.viewIndex).boards.size()-1));
        	updateGUI();
        });
	    incRectangle2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
		    ar.layers.get(ar.viewIndex).changeViewIndex(1);
		    System.out.println("Layer: " + ar.viewIndex + ", Board: " + ar.layers.get(ar.viewIndex).viewIndex + "/" + (ar.layers.get(ar.viewIndex).boards.size()-1));
		    updateGUI();
	    });
	    decRectangle2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
		    ar.layers.get(ar.viewIndex).changeViewIndex(-1);
		    System.out.println("Layer: " + ar.viewIndex + ", Board: " + ar.layers.get(ar.viewIndex).viewIndex + "/" + (ar.layers.get(ar.viewIndex).boards.size()-1));
		    updateGUI();
	    });
        runRectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
        	System.out.println("isLeaf: " + ar.layers.get(ar.viewIndex).getBoardsIterable().get(ar.layers.get(ar.viewIndex).viewIndex).isLeaf + ", allBoardsUnique: " + ar.layers.get(ar.viewIndex).allBoardsUnique());
	        ar.layers.get(ar.viewIndex).getBoardsIterable().get(ar.layers.get(ar.viewIndex).viewIndex).globalFilterCheck();
        	updateGUI();
        });
	    for (int i = 0; i < DIM; i++){
	    	for (int j = 0; j < DIM; j++){
	    		squareArray[i][j] = new Rectangle(i * 50, j * 50, 50, 50);
		    }
	    }
	    fl.fatalError();
	    long t1 = System.currentTimeMillis();
	    //fl.addCertainMoves(true);
	    /*
	    while (ar.genLayerDepthFirst()){
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
	    ar.genNextNodes();
	    updateGUI();
	    primaryStage.setScene(new Scene(root, DIM * 50, DIM * 50 + 100));
        primaryStage.show();
	    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your program has completed");
	    System.out.println("Time taken: " + (System.currentTimeMillis() - t1) + " millis, size: " + ar.layers.getLast().boards.size() + ", memory used (mb): " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
        alert.showAndWait();
    }
	
    public void updateGUI(){
    	setSquareArray(ar.layers.get(ar.viewIndex).getBoardsIterable().get(ar.layers.get(ar.viewIndex).viewIndex));
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
