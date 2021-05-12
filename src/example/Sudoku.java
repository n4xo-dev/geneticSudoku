package example;

import java.util.StringJoiner;

import com.qqwing.QQWing;

public class Sudoku {
	private int [] puzzle;
	private int [] solution;
	
	public Sudoku() {
		QQWing a =  new QQWing();
		a.generatePuzzle();
		puzzle = a.getPuzzle();
		//Get the solution as a string 
		a.solve();
		solution = a.getSolution();
	}
	
	public int[] getPuzzle() {
		return puzzle;
	}
	
	public int [] getSolution() {
		return solution;
	}
	
	 
	public String writePuzzle() {
		StringJoiner str = new StringJoiner(" | ", "[", "]");
		for(int sqr : puzzle)
				str.add(sqr + "");
		return str.toString();
	}
	
	public String writeSolution() {
		StringJoiner str = new StringJoiner(" | ", "[", "]");
		for(int sqr : solution)
				str.add(sqr + "");
		return str.toString();
	}
}
