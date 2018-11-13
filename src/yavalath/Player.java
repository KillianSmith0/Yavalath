package yavalath;

import java.util.ArrayList;

public class Player {
	private ArrayList<ArrayList<Integer>> pattern;
	private int pieceValue, score;

	Player(int pieceValue) {
		this.pieceValue = pieceValue;
		pattern = generateLinearPatterns(pieceValue);
		score = 0;
	}
	
	ArrayList<ArrayList<Integer>> getPatterns() {
		return pattern;
	}
	
	int getPlayerValue() {
		return pieceValue;
	}
	
	int getScore() {
		return score;
	}
	
	void addScore(int score) {
		this.score += score;
	}
	
	/** PART 4 - Static Evaluation patterns
	 * Generates the list of linear patterns to check if a good pattern or not
	 *
	 * The list of patterns that pieces can have on a board.. 
	 * The value of 1 means a blank space, in a row of 5
	 * */
	private ArrayList<ArrayList<Integer>> generateLinearPatterns(int player) {
		ArrayList<ArrayList<Integer>> patternList = new ArrayList<>();
		ArrayList<Integer> pattern;

		//Great - Winning move
		pattern = new ArrayList<>();
		pattern.add(1);
		pattern.add(player);
		pattern.add(player);
		pattern.add(player);
		pattern.add(player);
		patternList.add(pattern);

		// Good - one off a win
		pattern = new ArrayList<>();
		pattern.add(1);
		pattern.add(player);
		pattern.add(player);
		pattern.add(1);
		pattern.add(player);
		patternList.add(pattern);

		// Good - two off a win
		pattern = new ArrayList<>();
		pattern.add(1);
		pattern.add(player);
		pattern.add(1);
		pattern.add(1);
		pattern.add(player);
		patternList.add(pattern);

		// Bad - one off a loss / can't win with this pattern
		pattern = new ArrayList<>();
		pattern.add(1);
		pattern.add(player);
		pattern.add(player);
		pattern.add(1);
		patternList.add(pattern);

		// Bad - two off a win, likely to be intercepted
		pattern = new ArrayList<>();
		pattern.add(1);
		pattern.add(player);
		pattern.add(1);
		pattern.add(player);
		pattern.add(1);
		patternList.add(pattern);

		// Terrible - make a move in this pattern and you'll lose
		pattern = new ArrayList<>();
		pattern.add(1);
		pattern.add(player);
		pattern.add(player);
		pattern.add(player);
		pattern.add(1);
		patternList.add(pattern);

		return patternList;
	}

}
