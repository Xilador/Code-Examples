//Cody Dunlevy
//CSC 364
//Hydra Project
//Hydra file helps maintain, export and import

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Hydra {
	
	//the library of nodes to keep track of
	HydraNode root = new HydraNode(this,null);
	int copySize = 2;
	
	//imports the files encoded hydra and decodes it into the current hydra overwriting what was there
	public void importHydra(){
		JFileChooser jfc = new JFileChooser( );
        jfc.showOpenDialog( new JFrame() );
        File f = jfc.getSelectedFile( );
        String path = f.getPath();
        try{
        	BufferedReader br = new BufferedReader(new FileReader(path));
        	root = new HydraNode(this,null);
        	String line = br.readLine();
        	System.out.println(line);
        	decode(line);
        	br.close();
        }
        catch( IOException e ){
            System.out.println( "Error opening file" ) ;
            return ;
        }
	}
	
	//changes the copySize
	public void changeCopySize(int i){
		copySize = i;
	}
	
	//exports the current hydra into a txt file of the users choosing
	public void exportHydra(){
		JFileChooser jfc = new JFileChooser( );
        jfc.showOpenDialog( new JFrame() );
        File file = jfc.getSelectedFile( );
        try{
        	OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(file));
        	Writer writer = new BufferedWriter(streamWriter);
        	writer.write(this.getEncoding());
        	writer.close();
        }
        catch( IOException e ){
            System.out.println( "Error opening file" ) ;
            return ;
        }
	}
	
	//decodes the encoded string taken from an imported file
	public Hydra decode(String encoded){
		
		Hydra hydra = new Hydra();
		List<Integer> encodedInt = new ArrayList<Integer>();
		for (char c : encoded.toCharArray()) encodedInt.add(new Integer("" + c));
		
		root.decode(encodedInt);
		
		return hydra;
	}
	
	//gets the encoding from an already decoded hydra
	public String getEncoding(){
		return root.getEncoding();
	}
	
	//adds a child to the hydra
	public HydraNode addChild(){
		return root.addChild();
	}
	
	//gets the grid equivalent of the hydra
	public HydraNode[][] getGrid() {
		int height = getHeight();
		int width = getWidth();
		HydraNode[][] grid = new HydraNode[height][width];
		
		grid = root.fillGrid(grid,height-1);
		return grid;
	}
	
	//draws the pane and the hydra heads relative to where they should be
	public void draw(Pane pane){
		int[] blockSize = getBlockSize(pane);
		HydraNode[][] hydraGrid = this.getGrid();
		
		for (int y = 0; y < this.getHeight() ; y++){
			for (int x = 0; x < this.getWidth(); x++){
				HydraNode node = hydraGrid[y][x];
				if (node == null) continue;
				node.centerX = (blockSize[0]) * x;
				node.centerY = (blockSize[1]) * y;
				
			}
		}
		
		for (int y = 0; y < this.getHeight() ; y++){
			for (int x = 0; x < this.getWidth(); x++){
				
				HydraNode node = hydraGrid[y][x];
				if (node == null) continue;
				
				Circle circle = new Circle(node.centerX,node.centerY,5,Color.BLACK);
				
				if (hydraGrid[y][x].isChoppable){
					circle.setFill(Color.RED);			
					circle.setOnMousePressed(new EventHandler<MouseEvent>(){
						HydraNode head = node;
						public void handle(MouseEvent arg0) {
								node.chop(copySize);
								pane.getChildren().clear();
								draw(pane);
								System.out.println("CHOPPED!");
							}
			             });
				}
				
				if (node.parent != null){
					Line line = new Line();
					line.setStartX(node.parent.centerX);
					line.setStartY(node.parent.centerY);
					line.setEndX(node.centerX);
					line.setEndY(node.centerY);
					pane.getChildren().add(line);
				}
				
				pane.getChildren().add(circle);
				
			}
		}
	}
	
	//gets the area in which the heads should be located in
	public int[] getBlockSize(Pane pane){
		int[] xY = new int[2];
		xY[0] = (int)(pane.getWidth() / getWidth());
		xY[1] = (int)(pane.getHeight() / getHeight());
		return xY;
	}
	
	
	
	//generates a random hydra with a random amout of heads up to 5
	public void randomHydra(){
		int heads = 5;
		while (heads > 0){
			heads = root.addRandomHead(heads);
		}
		System.out.println(root.getEncoding());
	}
	
	//gets the highest width the hydra is at a single point
	public int getWidth(){
		int[] height = new int[getHeight()];
		height = root.getWidth(height);
		int width = 0;
		for (int i = 0; i < height.length; i++){
			if (width < height[i]){
				width = height[i];
			}
		}
		return width;
	}
	
	//returns the array with the widths of every level
	public int[] getWidthArray(){
		int[] height = new int[getHeight()];
		return root.getWidth(height);
	}
	
	//returns the height of the Hydra
	public int getHeight(){
		return root.getHeight(0)+1;
	}
	
	//chops off a random head
	public void randomChop(){
		
		HydraNode[][] hydraGrid = this.getGrid();
		ArrayList<HydraNode> heads = new ArrayList<HydraNode>();
		for (int y = 0; y < this.getHeight() ; y++){
			for (int x = 0; x < this.getWidth(); x++){
				HydraNode node = hydraGrid[y][x];
				if (node == null) continue;
				if (node.isChoppable == true)heads.add(node);
				
			}
		}
		
		int random = (int) Math.random()*heads.size();
		heads.get(random).chop(copySize);
	}
}

//hail hydra