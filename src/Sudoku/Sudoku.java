package Sudoku;

import java.util.StringJoiner;

import org.jgap.IChromosome;

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
	
	public Sudoku(QQWing sudoku) {
		this.sudoku = sudoku;
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
	
	/**
	 * Turns chromosome into sudoku in the form of an int array.
	 * 
	 * @param bestSolutionSoFar Chromosome to be converted into array
	 * @param initPuzzle Initial sudoku from where Chromosome was created
	 *
	 * @author iLopezosa, pablogodiaz, Dani130301
	 * @version 1.0
	 * 
	 * @return int[] convertedChromosome
	 * 
	 */
	public int[] reconstructPuzzle(IChromosome bestSolutionSoFar) {
		int[] convertedChromosome = new int[puzzle.length];
		int j = 0;
		for(int i = 0; i < puzzle.length; i++) {
			convertedChromosome[i] = (puzzle[i] == 0) ? 
					((Integer) bestSolutionSoFar.getGene(j++).getAllele()).intValue() 
					: puzzle[i];
		}

		return convertedChromosome;
	}
}
