package Sudoku;

import java.util.StringJoiner;
import java.util.Vector;

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
	
	public int[][] reconstructPuzzle() {
		int sqrLength = (int) Math.sqrt(QQWing.BOARD_SIZE);
		int[][] convertedChromosome = new int[sqrLength][sqrLength];
		int l = 0;
		for(int i = 0; i < sqrLength; i++) {
			for(int j = 0; j < sqrLength; j++) {
				convertedChromosome[i][j] = puzzle[l];
				l++;
			}
		}

		return convertedChromosome;
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
	@SuppressWarnings("unchecked")
	public int[][] reconstructPuzzle(IChromosome bestSolutionSoFar) {
		int sqrLength = (int) Math.sqrt(QQWing.BOARD_SIZE);
		int[][] convertedChromosome = new int[sqrLength][sqrLength];
		int l = 0;
		for(int i = 0; i < sqrLength; i++) {
			int k = 0;
			for(int j = 0; j < sqrLength; j++) {
				convertedChromosome[i][j] = (puzzle[l] == 0) ? 
						 ((Vector<Integer>) bestSolutionSoFar.getGene(i).getAllele()).get(k++).intValue()  
						: puzzle[l];
				l++;
			}
		}

		return convertedChromosome;
	}
	
	public int[] reconstructSudokuArr(int[][] sudoku){
		int res[] = new int[QQWing.BOARD_SIZE];
		int length = sudoku[0].length;
		int k=0;
		for (int i=0;i < length; i++){
			for(int j=0; j < length; j++){
				res[k] =  sudoku[i][j];
				k++;
			}
		}
		return res;
	}
}
