/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package Sudoku;

import org.jgap.*;

import com.qqwing.QQWing;

/**
 * Fitness function for the sudoku problem.
 *
 * @author pablogodiaz, Dani130301, iLopezosa
 * @version 1.0
 */
public class SudokuFitnessFunction
extends FitnessFunction {

	private final int[] sudoku_init;
	private final Sudoku sudoku;

	public static final int MAX_BOUND = 1000000;

	//private static final double ZERO_DIFFERENCE_FITNESS = Math.sqrt(MAX_BOUND); // What is this?

	public SudokuFitnessFunction(Sudoku sudoku) {
		sudoku_init = sudoku.getPuzzle();
		this.sudoku = sudoku;
	}

	/**
	 * Determine the fitness of the given Chromosome instance. The higher the
	 * return value, the more fit the instance. This method should always
	 * return the same fitness value for two equivalent Chromosome instances.
	 * [MODIFIED VERSION FOR SUDOKU]
	 *
	 * @param a_subject the Chromosome instance to evaluate
	 * @return a positive double reflecting the fitness rating of the given
	 * Chromosome
	 *
	 * @author Klaus Meffert, iLopezosa, pablogodiaz, Dani130301
	 * @version 1.0
	 */
	public double evaluate(IChromosome a_subject) {
		// The fitness value measures both how close the value is to the
		// target amount supplied by the user and the total number of items
		// represented by the solution. We do this in two steps: first,
		// we consider only the represented amount of change vs. the target
		// amount of change and return higher fitness values for amounts
		// closer to the target, and lower fitness values for amounts further
		// away from the target. Then we go to step 2, which returns a higher
		// fitness value for solutions representing fewer total items, and
		// lower fitness values for solutions representing more total items.
		// ------------------------------------------------------------------
		// Gets amount of incorrect cells multiplied by the number of conditions
		// violated by the cell (ponderated sum)
		double penalty = getPenalty(a_subject);
		//
		// -----------------------------------------------------------------
		double fitness = getFitness(MAX_BOUND, penalty);
		// Make sure fitness value is always positive.
		// -------------------------------------------
		return Math.max(1.0d, fitness);
	}

	/**
	 * Bonus calculation of fitness value.
	 * @param a_maxFitness maximum fitness value appliable
	 * @param a_volumeDifference volume difference in ccm for the items problem
	 * @return bonus for given volume difference
	 *
	 * @author Klaus Meffert
	 * @since 2.3
	 */
	protected double getFitness(double a_maxFitness, double penalty) {
		if (penalty == 0) {
			return a_maxFitness;
		}
		else {
			// we arbitrarily work with half of the maximum fitness as basis for non-
			// optimal solutions (concerning volume difference)
			// 373.248 max penalty value for 72 empty cells
			return a_maxFitness / 2 - (penalty * penalty * penalty);  
		}
	}
	
	private int [] substr (int initial, int end){
		return 0; //TODO
	}
	
	protected double getPenalty(IChromosome a_subject){
		// We iterate the array of cells and search for errors, if there is a double or tripe error, there is a penalty
		int errorCounter = 0;
		double penalty = 0.0d;
		int sudokuSize = QQWing.BOARD_SIZE;
		int sideSize = (int) Math.ceil(Math.sqrt(sudokuSize));
		
		int[] fullSudoku = sudoku.reconstructPuzzle(a_subject);
		// We divide the sudoku in its respective groups (colummns, rows, blocks), that way we can check if there are errors, and see if there are double errors
		int[][] rows    = new int[sideSize][sideSize];
		int[][] columns = new int[sideSize][sideSize];
		int[][] blocks  = new int[sideSize][sideSize];
		
		
		for(int i = 0; i < sideSize; i++) {
			row[i] = substr(i,i*size);
			
			//TODO substring function for int[]
		}
		
		
	return 0; //TODO
	}
}
