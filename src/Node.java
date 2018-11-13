//Killian Smith
//13501407

import java.util.*;

public class Node{
	
	Random rnd = new Random();
	private ArrayList<Node> children;
	private int value;
	int staticEvaluationCounter = 0, quiescenceEvalCounter = 0;
	
	/**
	 * Constructors
	 * **/
	public Node(){
		children = new ArrayList<Node>();
	}
	public Node(int value){
		this.value = value;
		children = new ArrayList<Node>();
	}

	/**
	 * This is the recursive call function for part 1, to generate the tree.
	 * Generate tree calls generateChildren which generates the children of that node.
	 * It then calls itself on each of the children nodes just created.
	 * */
	Node createTree(Node node, int depth, int branch, int value, int inaccuracy, int spread){
		int s = rnd.nextInt(spread);
		int spreadVal = -value + s;
		int j = rnd.nextInt(branch);	//Chose random child
		
		if(depth>0){ 
			node.setValue(evaluation(value, inaccuracy));	// Assign node a inaccurate score
		
			for(int i =0; i < branch; i++){		// create its children
				node.addChild(new Node());		
			}
			
			for(Node n: node.getChildren()){				
				if(n.equals(node.getChildren().get(j))) 
					createTree(n, depth-1, branch, -value, inaccuracy, spread);
				else								
					createTree(n, depth-1, branch, spreadVal, inaccuracy, spread);
			}
		}
		else		// LEAF node, assign value between 0 and spread
			node.setValue(value);
		return node;
	}
	
	/**
	 * The Static Evaluation heuristic
	 */
	int evaluation(int value, int inaccuracy){
		int max = inaccuracy, min = -inaccuracy;
		inaccuracy = rnd.nextInt((max - min) + 1) + min;
		return value + inaccuracy;
	}
	
	/**
	 * The NegaMax Algorithm
	 * */
	int alphaBetaNegaMax(Node node, int height, int alpha, int beta){
		if(height == 0){
			staticEvaluationCounter++;
			printer(height, alpha, beta, node.getValue());
			return node.getValue();
		}else{
			int temp;
			for(Node n: node.getChildren()){
				temp = -(alphaBetaNegaMax(n, height-1, -beta, -alpha));
		
				if(temp >= beta) return temp;
				alpha = Math.max(temp, alpha);
				printer(height, alpha, beta, temp);
				
			}
		}
		return alpha;
	}
	
	/**
	 * The Null-Move Algorithm
	 * **/
	int alphaBetaNMQuiescence(Node node, int height, int alpha, int beta){
		if(height == 0){
			printer(height, alpha, beta, node.getValue());
			return nMQEvaluation(node, alpha, beta);
		}else{
			int temp;
			for(Node n: node.getChildren()){
				temp = -(alphaBetaNMQuiescence(n, height-1, -beta, -alpha));
				if(temp >= beta) return temp;
				
				alpha = Math.max(temp, alpha);
				printer(height, alpha, beta, temp);
			}
		}
		return alpha;
	}
	
	/**
	 * The Null-Move Heuristic function 
	 * */
	int nMQEvaluation(Node node, int lower, int upper){
		int temp, best;
		best = node.getValue();

		for(Node n: node.getChildren()){
			quiescenceEvalCounter++;		
			if(best>= upper)
				return best;
			temp = - nMQEvaluation(n, -upper, -best);
			best = Math.max(best, temp);	
		}
		return best;
	}

	void printer(int height, int alpha, int beta, int value){
		System.out.println();
		if(height == 0)
			System.out.println();
		
		for(int i = 0; i < height; i++){
			System.out.print("\t");
		}
		
		System.out.print("Level:" + height+"\t");
		if(height != 0)
			System.out.print("INTERIOR node:");
		else if(height == 0)
				System.out.print("LEAF node:");
		System.out.print("\tAlpha: "+ alpha+"\tBeta: "+ beta + "\tValue: "+ value);	
	}
	
	/**
	 * Node Attribute Methods
	 * */
	int getValue(){
		return value;
	}
	
	void setValue(int value){
		this.value = value;
	}
	
	ArrayList<Node> getChildren(){
		return children;
	}
	
	void addChild(Node n){
		children.add(n);
	}

	/**
	 * Main
	 * */
	public static void main(String args[]){
		Scanner input = new Scanner(System.in);  // Reading from System.in
		
		System.out.println("Enter a value for node: ");
		int value = input.nextInt();
		System.out.println("Tree depth: ");
		int depth = input.nextInt();
		System.out.println("Branch factor: ");
		int branch = input.nextInt();
		System.out.println("Inaccuracy range: ");
		int inaccuracy = input.nextInt();
		System.out.println("Spread: ");
		int spread = input.nextInt();

		int alpha = -1000000, beta = 1000000;
		
		Node root = new Node(value);
		root.createTree(root, depth, branch, value, inaccuracy, spread);
		
		System.out.println("\n\n\n********NegaMax Implementation********");
		root.alphaBetaNegaMax(root, depth, alpha, beta);
		System.out.println("\n\n********AlphaBeta NullMove Quiescence********");
		root.alphaBetaNMQuiescence(root, depth, alpha, beta);
//		ArrayList<Integer> nega = new ArrayList<Integer>();
//		ArrayList<Integer> nmq = new ArrayList<Integer>();
//		ArrayList<Integer> negaVal = new ArrayList<Integer>();
//		ArrayList<Integer> nmqVal = new ArrayList<Integer>();
//		
//		for(int srchLvl = 0; srchLvl < depth; srchLvl++){
//			System.out.println("\n\n\n********NegaMax Implementation********");
//			root.alphaBetaNegaMax(root, depth-srchLvl, alpha, beta);
//			System.out.println("\n\n********AlphaBeta NullMove Quiescence********");
//			root.alphaBetaNMQuiescence(root, depth-srchLvl, alpha, beta);
//			
//			nega.add(root.staticEvaluationCounter);
//			nmq.add(root.quiescenceEvalCounter);
//			negaVal.add(root.alphaBetaNegaMax(root, depth-srchLvl, alpha, beta));
//			nmqVal.add(root.alphaBetaNMQuiescence(root, depth-srchLvl, alpha, beta));
//		}
//
//		System.out.println("\nNegaMax: "+ nega);
//		System.out.println("NEGAMAX VAL: "+ negaVal);
//		System.out.println("NMQ: "+nmq);
//		System.out.println("NMQ VAL: "+ nmqVal);
		
		input.close();
	}
}