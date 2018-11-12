//Cody Dunlevy
//CSC 364
//Hydra Project
//Main file- controls the gui and startup

import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

public class Main extends Application{
	
	//panes and the size variable to track how many heads to duplicate
	public static Integer copySize = 2;
	static Hydra hydra, hydra2;
	GridPane mainPane = new GridPane();
	HBox buttonArea = new HBox();
	Pane hydraPane = new Pane();
	
	//creates a hydra and tests it as well as starts the application
	public static void main(String[] args){

		hydra = new Hydra();
		hydra.changeCopySize(copySize);
		hydra.randomHydra();
		String test = hydra.getEncoding();
		System.out.println(test);
		System.out.println(hydra.getHeight());
		System.out.println(hydra.getWidth());
		
		hydra.getGrid();
		
		launch(args);
	}
	
	//starts the application and the gui
	public void start(Stage primaryStage){
		
		mainPane.setAlignment(Pos.BOTTOM_CENTER);
		mainPane.setPadding(new Insets(10, 10, 10, 10));
		
		buttonArea.setAlignment(Pos.CENTER);
		
		Button chop = new Button(" CHOP! ");
		Button add = new Button(" + ");
		Button subtract = new Button(" - ");
		Button importHydra = new Button(" Import ");
		Button exportHydra = new Button(" Export ");
		TextArea copyAmt = new TextArea(copySize.toString());
		copyAmt.setPrefSize(10, 10);
		
		buttonArea.getChildren().addAll(chop,subtract,copyAmt,add,importHydra,exportHydra);
		buttonArea.setPadding(new Insets(15, 12, 15, 12));
		buttonArea.setSpacing(10);
		
		Pane hydraPane = new Pane();
		hydraPane.setPrefHeight(700);
		hydraPane.setPrefWidth(800);
		hydraPane.autosize();
		
		mainPane.add(buttonArea, 0, 10);
		mainPane.add(hydraPane, 0, 0);
		
		hydra.draw(hydraPane);
		
		Scene scene = new Scene(mainPane, 800, 800);
		primaryStage.setTitle("HAIL HYDRA");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		// add button to change the copy size
		add.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent args0){
				copySize++;
				hydra.changeCopySize(copySize);
				copyAmt.setText(copySize.toString());
			}
		});
		
		//subtract button to change the copy size
		subtract.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent args0){
				copySize--;
				hydra.changeCopySize(copySize);
				copyAmt.setText(copySize.toString());
			}
		});
		
		//imports the file given into the porgram
		importHydra.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent args0){
				hydra.importHydra();
				hydra.draw(hydraPane);
				System.out.println("Imported File");
			}
		});
		
		//exports the current hydra for later use
		exportHydra.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent args0){
				info();
				hydra.exportHydra();
				System.out.println("Exported File");
			}
		});
		
		//chops a random head
		chop.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent args0){
				hydra.randomChop();
				hydraPane.getChildren().clear();
				hydra.draw(hydraPane);
			}
		});
	}
	
	//shows an info box to explain some dangers of saving the file
	public void info(){
		JOptionPane.showMessageDialog(null, "Please pick a file for the decoded hydra to be writen in to. \n WARNING it will overwrite the file");
	}
}

//hail hydra