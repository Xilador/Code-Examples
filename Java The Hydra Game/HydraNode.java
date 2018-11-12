//Cody Dunlevy
//CSC 364
//Hydra Project
//HydraNode file creates and maintains the actual library for the nodes
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HydraNode {
	
	//each hydranode will keep track of its parent, children, level and root. as well as if its choppable
	HydraNode parent;
	Hydra hydra;
	List<HydraNode> children = new ArrayList<HydraNode>();
	int level = 0;
	boolean isChoppable;
	int centerX, centerY;
	
	//the constructor for the HydraNode
	HydraNode(Hydra hydra,HydraNode parent){
		this.hydra = hydra;
		this.parent = parent;
		this.isChoppable = false;
		if (parent != null){
			level = parent.level+1;
			this.isChoppable = true;
		}
	}
	
	//adds a child to the given node and updates the given info
	public HydraNode addChild(){
		HydraNode child = new HydraNode(this.hydra,this);
		child.hydra = this.hydra;
		child.level = this.level+1;
		child.isChoppable = true;
		this.isChoppable = false;
		relevelChildren(child);
		children.add(child);
		return child;
	}
	
	//adds an already specified HydraNode to the current head
	public void addChild(HydraNode child){
		child.hydra = this.hydra;
		child.level = this.level+1;
		child.parent = this;
		child.parent.isChoppable = false;
		child.isChoppable = true;
		relevelChildren(child);
		children.add(child);
	}
	
	
	//changes the childrens level relative to their parent
	private void relevelChildren(HydraNode node ){
		
		node.isChoppable = (node.children.size() == 0);
		
		for (Iterator<HydraNode> i = node.children.iterator(); i.hasNext();){
			HydraNode child = i.next();
			child.level = child.parent.level + 1;
			relevelChildren(child);
		}
		
	}
	
	//creates a head at a given point at random
	public int addRandomHead(int heads){
		if (heads == 0) return heads;
		if (Math.random() < 0.5) heads = (this.addChild()).addRandomHead(heads-1);
		return heads;
			
	}
	
	// gets the encoded information from the hydra head
	public String getEncoding(){
		
		String encoded = new String();
		for (Iterator<HydraNode> i = children.iterator(); i.hasNext();){
			HydraNode child = i.next();
			encoded += child.getEncoding();
		}
		int size = children.size();
		encoded = size + encoded;
		return encoded;
	}
	
	//decodes a string given to it to be turned into a new hydra
	public void decode(String encodedInt){
		List<Integer> encodedIntTemp = new ArrayList<Integer>();
		for (char c : encodedInt.toCharArray()) encodedIntTemp.add(new Integer("" + c));
		decode(encodedIntTemp);
	}
	
	//decode to help deep copy the heads
	public void decode(List<Integer> encodedInt){
		if (encodedInt == null) return;
		if (encodedInt.size() == 0) return;
		Integer numChildren = encodedInt.remove(0);
		if (numChildren == 0) return;
		
		for(int i = 0; i < numChildren; i++){
			HydraNode child = this.addChild();
			child.decode(encodedInt);
		}
	}
	
	//gets the total height of the hydra at its highest point
	public int getHeight(int maxHeight){
		if (this.level > maxHeight){
			maxHeight = this.level;
		}
		if (this.children.size() == 0){
			return maxHeight;
		}
		for (Iterator<HydraNode> i = children.iterator(); i.hasNext();){
			HydraNode child = i.next();
			maxHeight = child.getHeight(maxHeight);
		}
		return maxHeight;
	}
	
	//returns an array of widths of the hydra
	public int[] getWidth(int[] array){
		array[this.level]++;
		if (this.children.size() == 0){
			return array;
		}
		for (Iterator<HydraNode> i = children.iterator(); i.hasNext();){
			HydraNode child = i.next();
			array = child.getWidth(array);
		}
		return array;
	}
	
	//fills a grid with where all the hydra heads should be positioned
	public HydraNode[][] fillGrid(HydraNode[][] grid,int maxHeight) {
		for (int i = 0; i < grid[level].length; i++ ) {
			if (grid[maxHeight-level][i] == null) {
				grid[maxHeight-level][i] = this;
				break;
			}
		}
		
		for (Iterator<HydraNode> i = children.iterator(); i.hasNext();){
			HydraNode child = i.next();
			grid = child.fillGrid(grid,maxHeight);
		}
		
		return grid;		
	}
	
	//removes a selected head and duplicates their parent however many times copySize is
	public void chop(int copySize){
		if (this.children.size() > 0) return;
		if (this.parent == null) return;
		if (this.parent.parent == null ){
			parent.children.remove(this);
			return;
		}
		
		HydraNode node = this;
		HydraNode parent = this.parent;
		HydraNode grandparent = parent.parent;
		
		parent.children.remove(node);
		if (parent.children.size() == 0) parent.isChoppable = true;
		
		for (int i = 0; i < copySize; i++){
			String parentString = parent.getEncoding();
			HydraNode x = new HydraNode(parent.hydra, null);
			x.decode(parentString);
			
			grandparent.addChild(x);
		}
		
	}
}

//hail hydra