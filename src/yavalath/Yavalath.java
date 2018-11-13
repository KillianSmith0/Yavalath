/**
 * USERNAME: KILLIAN SMITH
 * STUD NO.: 13501407
 * */

package yavalath;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;

enum Direction {
	HORIZONTAL_LEFT, HORIZONTAL_RIGHT, DIAGONAL_LEFT, DIAGONAL_RIGHT
}

enum Opponent {
	USER, AI
}

class Piece {
    private int x, y;

    Piece(int x, int y){
        this.x = x;
        this.y = y;
    }

    int getX(){
        return x;
    }

    int getY(){
        return y;
    }

    public String toString(){
        return "("+x+", "+y+")";
    }
}


public class Yavalath {

	private Random rnd = new Random();
	private Scanner input = new Scanner(System.in);
	
	private int staticEvalCounter, numberOfMoves = 1, playablePieces = 61;
	private int firstX, firstY;
	private boolean isP1 = true;
	
	private Player player1 = new Player(8);
	private Player player2 = new Player(9);
	
	private boolean endGame = false;
	private Hashtable<Piece, Integer> pieceTable = new Hashtable<>();

	private int[][] frame = new int[][] {
		{ 0, 0, 0, 0, 0, 1, 3, 3, 3, 3, 2 },
		{ 0, 0, 0, 0, 5, 7, 7, 7, 7, 7, 2 },
		{ 0, 0, 0, 5, 7, 7, 7, 7, 7, 7, 2 },
		{ 0, 0, 5, 7, 7, 7, 7, 7, 7, 7, 2 },
		{ 0, 5, 7, 7, 7, 7, 7, 7, 7, 7, 2 },
		{ 4, 7, 7, 7, 7, 7, 7, 7, 7, 7, 0 },
		{ 4, 7, 7, 7, 7, 7, 7, 7, 7, 0, 0 },
		{ 4, 7, 7, 7, 7, 7, 7, 7, 0, 0, 0 },
		{ 4, 7, 7, 7, 7, 7, 7, 0, 0, 0, 0 },
		{ 4, 7, 7, 7, 7, 7, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
	};

	private int[][] board = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0 },
		{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0 },
		{ 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0 },
		{ 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
		{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
		{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
		{ 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0 },
		{ 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
		{ 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
	};

    /** MISCALLAENOUS
	 * Used to display a help message when a player makes a mistake with input
	 */
    private void explainCoordinateRule() {
		println("Coordinate rules:");
		println("Must be a Caps letter followed by a single digit.");
		print("A1-A5\tB1-B6\tC1-C7\tD1-D8\tE1-E9");
		print("\tF1-F8\tG1-G7\tH1-H6\tI1-I5\n");
	}

    private void playWho(Opponent opponent){
		switch (opponent) {
			case USER:
				playUser();
			case AI: 
				playAI();
		}
	}

	/**
	 * Returns a binary array, where 1s represent playable squares
	 * Used when don't want messaged display for checking if playable*/
    private int[][] getPlayableCells(){
		int[][] empty = new int[board.length][board.length];
		for(int i = 0; i< board.length; i++){
			for(int j = 0; j < board.length; j++){
				if(board[i][j] == 1) {
					empty[i][j] = board[i][j];
				}
			}
		}
		return empty;
	}

	/**PART 1
	 * Print the board
	 * */
    private void printBoard() {
		String indentation = "";
		for (int i = 0; i < board.length; i++) {
			print(indentation);
			print(" ");

			for (int j = 0; j < board.length; j++) {

				if (board[i][j] == player1.getPlayerValue()) {
                    print("X");
                } else if (board[i][j] == player2.getPlayerValue()) {
                    print("O");
                } else {
                    print(" ");
                }

				print(" ");
				if (frame[i][j] == 4 || frame[i][j] == 5 || frame[i][j] == 7) {
					print("|");
				} else {
                    print(" ");
                }

				print(" ");
			}
			println(indentation);
			print(indentation);
			for (int j = 0; j < board.length; j++) {
				int part = frame[i][j];

				if (part == 2 || part == 3 || part == 7) {
					print("\\");
				} else {
					print(" ");
				}
				print(" ");

				if (part == 1 || part == 3 || part == 5 || part == 7) {
                    print("/");
                } else {
                    print(" ");
                }
				print(" ");
			}
			println("");
			indentation += "  ";
		}
		if (this.isP1) {
            println("Player 1's move");
        } else {
            println("Player 2's move");
        }
	}

	/** PART 2
	 * Method for two users to play against each other.
	 */
    private void playUser() {
		numberOfMoves = 1;
		printBoard();
		while (!endGame) {
			if (numberOfMoves == 2) {
				takeFirstMove();
				printBoard();
			}
			userInput();
			printBoard();
		}
		println("Game Over!!");
	}

	/** PART 2
	 * Used to decide whether to take player 1s move
	 * */
	private void takeFirstMove() {
		println("If you wish to take P1s move: Enter X");
		println("If not: Enter C");
		String takeMove = input.next();

		// takeMove.equals("C")
		switch(takeMove) {
			case "X": case "x":
					println("Took Move. \nNEXT PLAYERS TURN");
					board[firstX][firstY] = (isP1) ? 
							player1.getPlayerValue() :
								player2.getPlayerValue();
					isP1 = !isP1;
					numberOfMoves++;
					break;
			case "C": case "c":
					println("You didnt take the position.");
					userInput();
					break;
			default:
					println("Wrong input");
					takeFirstMove();
		}
	}

	/** PART 2
	 * Checks whether the users input is valid, until it is valid. If it's valid
	 * it checks if its a playable position If it's playable then it calls
	 * makeMove() and makes the move.
	 */
    private void userInput() {
		println("Enter Coordinate:");

		String move = input.next();
		while (move.length() != 2) {
			println("Re-enter");
			move = input.next();
		}

		char r = move.charAt(0), col = move.charAt(1);
		int row = r - 64; // via ASCII code. 'A'-64 = 1. 'B'-64 = 2
		int column = Character.getNumericValue(col);
		int shift = 0; // to get rid of 0's in board array

		switch(row) {
		case 1:
			shift = 4;
			break;
		case 2: 
			shift = 3;
			break;
		case 3:
			shift = 2;
			break;
		case 4:
			shift = 1;
			break;
		}

		column += shift;
		Piece position = new Piece(row, column);

		if (playable(position))
			makeMove(position);
		else {
			explainCoordinateRule();
			userInput();
		}
	}

	/** PART 2 & 5
	 * Take's in piece to be placed on board. Checks if playable If playable,
	 * places piece on board depending on which player Calls endGame() to see if
	 * the game has finished after that move
	 */
    private void makeMove(int x, int y) {
		if (numberOfMoves == 1) {
			firstX = x;
			firstY = y;
		}
		int playerPiece = (isP1) ? player1.getPlayerValue() : player2.getPlayerValue();
		board[x][y] = playerPiece; 
		
		numberOfMoves++;
		playablePieces--;
		isP1 = !isP1;
		checkMoveFinishesGame(x, y);
	}

    private void makeMove(Piece position) {
		if (numberOfMoves == 1) {
			firstX = position.getX();
			firstY = position.getY();
		}
		int playerPiece = (isP1) ? player1.getPlayerValue() : player2.getPlayerValue();
		board[position.getX()][position.getY()] = playerPiece;
		
		numberOfMoves++;
		playablePieces--;
		isP1 = !isP1;
		checkMoveFinishesGame(position.getX(), position.getY());
	}

	/** PART 2- Interpret User input
	 * Checks whether the given coordinates are playable Checks if the
	 * coordinate is already occupied or is a playable position i.e
	 * board[x][y] == 1
	 */
    private boolean playable(Piece position) {
		// checks bounds
		int x = position.getX(), y = position.getY();
		int[][] playableCells = getPlayableCells();

		if (x < 1 || x > board.length || y < 1 || y >= board.length-1 || board[x][y] < 1) {
			println("Syntax Error. Not playable");
			return false;
		}
		else if (playableCells[x][y] == 1) {
			return true;
		} else {
			println("Syntax Error. Not playable.");
			return false;
		}
	}

	/** PART 3- End Game Logic
	 * Checks the patterns to the right, and then the left of the
	 * piece placed at the (x, y) position of the board.
	 */
    private boolean checkMoveFinishesGame(int x, int y) {
		int player = board[x][y];
		int shift, rightPiece = board[x][y], leftPiece = board[x][y];
		int horizInRow = 0, diagInRow1 = 0, diagInRow2 = 0;

		// i = 0: Horizontal, i = 1: Diagonal, i = 2: Other Diagonal
		for (int i = 0; i < 3; i++) {
			int patternLeft = 0, patternRight = 0;

			// Check right half of board in relation to placed piece
			shift = 1;
			while (shift < 5){
				switch(i) {
				case 0:
					rightPiece = board[x][y + shift]; // Horizontal
					break;
				case 1:
					rightPiece = board[x + shift][y]; // Diagonal
					break;
				case 2:
					rightPiece = board[x - shift][y + shift]; // Diagonal
					break;
				}
				if(rightPiece == player) {
					patternRight++;
					shift++;
				} else {
					break;
				}
			}

			shift = 1;
			while(shift < 5){
				switch(i) {
					case 0: 
						leftPiece = board[x][y - shift]; // Horizontal
						break;
					case 1: 
						leftPiece = board[x - shift][y]; // Diagonal
						break;
					case 2:
						leftPiece = board[x + shift][y - shift]; // Diagonal
						break;
				}

				if(leftPiece == player){
					patternLeft++;
					shift++;
				} else break;
			}

			switch(i) {
				case 0: 
					horizInRow = patternRight + patternLeft + 1;
					break;
				case 1: 
					diagInRow1 = patternRight + patternLeft + 1;
					break;
				case 2: 
					diagInRow2 = patternRight + patternLeft + 1;
					break;
			}
		}

		if (horizInRow == 4 || diagInRow1 == 4 || diagInRow2 == 4) {
			endGame = true;
			if (player == player1.getPlayerValue())
				println("Player1 Wins!");
			else
				println("Player2 Wins!");
		} else {
			if (horizInRow == 3 || diagInRow1 == 3 || diagInRow2 == 3) {
				endGame = true;
				if (player == player1.getPlayerValue()) {
					println("Player1 Loses!");
				} else{
					println("Player2 Loses!");
				}
			}
		}
		if (playablePieces == 0) {
			endGame = true;
			println("Draw");
		}
		return endGame;
	}

	/** PART 4- Static Evaluation
	 * The scoring function for the board layout.
	 * @param pattern is a line of tiles.
	 * @param listPattern
	 *            contains a list of max 6 patterns, the different orientations
	 * @param boardPatterns
	 *            contains a list of all the patterns, for each piece on the board
	 */
    private int staticEvaluation() {
		ArrayList<ArrayList<ArrayList<Integer>>> boardPatterns = new ArrayList<>();
		ArrayList<ArrayList<Integer>> p1LinearPattern = player1.getPatterns(); // For X pieces
		ArrayList<ArrayList<Integer>> p2LinearPattern = player2.getPatterns(); // For O pieces

		for (int x = 1; x < board.length; x++) {
			for (int y = 1; y < board.length; y++) {
				if (board[x][y] < 1) {    //	unplayable
                    continue;
                }

				int piece = board[x][y];
				int patternDirection = 0;
				ArrayList<ArrayList<Integer>> listPattern = new ArrayList<>();
				while (patternDirection < 6) {
					int shift = 0;
					ArrayList<Integer> pattern = new ArrayList<>();
					while (shift < 5) {
						if (piece < 1) { // check off board
							break;
						}
							
						switch(patternDirection){
						case 0: // horizontal right
							piece = board[x][y+shift]; 
							break;
						case 1: // diag down right
							piece = board[x+shift][y]; 
							break;
						case 2: // diag down left
							piece = board[x+shift][y-shift];
							break;
						case 3:  // horizontal left
							piece = board[x][y-shift];
							break;
						case 4: // diagonal upleft
							piece = board[x-shift][y];
							break;
						case 5: // diagonal upright
							piece = board[x-shift][y+shift];
							break;
						}	

						pattern.add(piece);
						shift++;
					}
					patternDirection++;
					if (pattern.size() < 4) { // Don't add pattern length < 4, no point searching
                        continue;
                    }
					listPattern.add(pattern);
				}
				boardPatterns.add(listPattern);
			}
		}
		
		for (ArrayList<ArrayList<Integer>> tile : boardPatterns) {
			for (ArrayList<Integer> sequence : tile) {

				if(sequence.equals(p1LinearPattern.get(0))) {
					player1.addScore(100);
				}
				if (sequence.equals(p1LinearPattern.get(1))) {
					player1.addScore(20);
				}
				if(sequence.equals(p1LinearPattern.get(2))) {
					player1.addScore(10);
				}	
				if (sequence.equals(p1LinearPattern.get(3)) || sequence.equals(p1LinearPattern.get(4))) {
					player1.addScore(-10);
				}
				if(sequence.equals(p1LinearPattern.get(5))) {
					player1.addScore(-1000);
				}

				if(sequence.equals(p1LinearPattern.get(0))) {
					player2.addScore(100);
				}
				if (sequence.equals(p2LinearPattern.get(1))) {
					player2.addScore(20);
				}

				if(sequence.equals(p2LinearPattern.get(2))) {
					player2.addScore(10);
				}	
				if (sequence.equals(p2LinearPattern.get(3)) || sequence.equals(p2LinearPattern.get(4))) {
					player2.addScore(-10);
				}
				if(sequence.equals(p1LinearPattern.get(5)) ) {
					player2.addScore(-1000);
				}
			}
		}

		return player1.getScore() - player2.getScore();
	}


	/** PART 5
	 * Method to play against the computer isP1 is a boolean to check if first
	 * player, in this case if its the AI
	 */
    private void playAI() {
		int alphaBeta = 0, alpha = -1_000_000, beta = 1_000_000;
		int bestRow = 1, bestCol = 5;
        int[][] playableCells = getPlayableCells();
		boolean killer = false;
		fillTable();
		numberOfMoves = 1;

		while(!endGame){
			if(numberOfMoves == 2){
				takeFirstMove();
				printBoard();
				continue;
			}

			if(isP1) {
				int statEval = staticEvaluation();

				for(int i = 1; i < board.length-1; i++) {
					for(int j = 1; j < board.length-1; j++) {
						if(playableCells[i][j] == 1) {
							
							makeMove(i, j);
							alphaBeta = alphaBeta(1, alpha, beta, killer);

							if(alphaBeta < statEval) {
								bestRow = i;
								bestCol = j;
								statEval = alphaBeta;
							}
							unmakeMove(i, j);
						}
					}
				}
				if(playableCells[bestRow][bestCol] != 1) {
					while(playableCells[bestRow][bestCol] != 1) {
						bestRow = rnd.nextInt(9);	// IF can't find better evaluation
						bestCol = rnd.nextInt(9);
					}
				}
                println(bestRow + " bestCol " + bestCol);
				makeMove(bestRow, bestCol);
			} else {
				userInput(); // every 2nd move is user
			}
			printBoard();
		}
		println("Game Over!!");
	}

	/** PART 5 - AlphaBeta
	 * Search Algorithm
	 * @param height number of playable pieces to search ahead
	 * @param player decides whether X,O to place
	 * @param killer decides whether to use killer heuristic or not
	 */
    private int alphaBeta(int height, int alpha, int beta, boolean killer) {
		if(height == 0){
			staticEvalCounter++;
			return staticEvaluation();
		} else {
			int temp = 0;
			for(int i = 1; i < board.length-1; i++) {
				for(int j = 1; j < board.length-1; j++) {
					if(getPlayableCells()[i][j] == 1) {
						makeMove(i,j);
						temp = -alphaBeta(height-1, -beta, -alpha, killer);
						unmakeMove(i,j);
					}
				}
			}
			if (temp >= beta) {
				if(killer) {
					killer(); 
				}
				return temp;
			}
			alpha = Math.max(temp, alpha);
		}
		return alpha;
	}

	/** PART 5- AlphaBeta
	 * Remove the previously placed tile For search.
	 */
    private void unmakeMove(int row, int column) {
		if (board[row][column] < 1) { // if not on board ignore
			board[row][column] = 0;
		}
		
		board[row][column] = 1;
		isP1 = !isP1;
		endGame = false;
	}

    private void killer(){

	}

	/** PART 5 - Killer
	 * For start of heuristic
	 * */
    private void fillTable() {
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board.length; y++) {
				if (board[x][y] >= 1) {
					pieceTable.put(new Piece(x, y), 0);
				}
			}
		}
	}

	private void print(String string){
        System.out.print(string);
    }

	private void println(String string){
        System.out.println(string);
    }

	public static void main(String[] args) {
		Yavalath game = new Yavalath();

		System.out.println("\t  *************************************");
		System.out.println("\t  ********** Y A V A L A T H **********");
		System.out.println("\t  *************************************");
		System.out.println("Game mode:\nUser  vs User? Type 1\nUser vs AI? Type 2");
		
		int player;
		player = game.input.nextInt();
		boolean isValid = (player == 1) || (player == 2);

		while(!isValid) {
			System.out.println("Invalid selection, choose 1 or 2");
			player = game.input.nextInt();
			isValid = (player == 1) || (player == 2);
		}

		switch (player) {
            case 1:
                game.playWho(Opponent.USER);
                break;
            case 2:
                game.playWho(Opponent.AI);
                break;
            default:
                break;
        }
		game.input.close();
	}
}
