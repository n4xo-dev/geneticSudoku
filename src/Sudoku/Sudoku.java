package Sudoku;

import java.util.StringJoiner;

import com.qqwing.QQWing;

public class Sudoku {
	private int [] puzzle;
	private int [] solution;
	private QQWing sudoku;
	
	public Sudoku() {
		sudoku =  new QQWing();
		sudoku.generatePuzzle();
		puzzle = sudoku.getPuzzle();
		//Get the solution as a string 
		sudoku.solve();
		solution = sudoku.getSolution();
	}
	
	public int[] getPuzzle() {
		return puzzle;
	}
	
	public int [] getSolution() {
		return solution;
	}
	
	public QQWing getSudoku() {
		return sudoku;
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
