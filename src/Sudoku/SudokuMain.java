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


import java.util.ArrayList;
import java.util.Vector;

import org.jgap.*;
import org.jgap.impl.*;

import com.qqwing.QQWing;

/**
 * This class provides an implementation of the classic sudoku problem
 * using a genetic algorithm.
 * <p>
 *
 * @author pablogodiaz, Dani130301, iLopezosa		
 * @version 1.0
 */
public class SudokuMain {
	/**
	 * The total number of times we'll let the population evolve.
	 */
	private static final int MAX_ALLOWED_EVOLUTIONS = 1000;

	public static final int MAX_BOUND = 20000000;

	/**
	 * Executes the genetic algorithm to solve the sudoku. The solution will then
	 * be written to the console.
	 * Based on {@link https://github.com/martin-steghoefer/jgap|JGAP library}.
	 * 
	 * @param sudoku -> The sudoku to be solved, type Sudoku.
	 *
	 * @throws Exception
	 *
	 * @author pablogodiaz, Dani130301, iLopezosa
	 * @version 1.0
	 * 
	 */
	public static void solveSudoku(Sudoku sudoku)
			throws Exception {
		// Start with a DefaultConfiguration, which comes setup with the
		// most common settings.
		// -------------------------------------------------------------
		Configuration conf = new DefaultConfiguration();
		conf.setPreservFittestIndividual(true);
		// Set the fitness function we want to use. We construct it with
		// the sudoku passed as an array in to this method.
		// ---------------------------------------------------------
		int[] sudokuArr = sudoku.getPuzzle();
		FitnessFunction myFunc =
				new SudokuFitnessFunction(sudoku);
		conf.setFitnessFunction(myFunc);
		sudoku.getSudoku();
		// Now we need to tell the Configuration object how we want our
		// Chromosomes to be setup. We do that by actually creating a
		// sample Chromosome and then setting it on the Configuration
		// object. As mentioned earlier, we want our Chromosomes to each
		// have as many genes as there are empty cells in the sudoku. We want the
		// values (alleles) of those genes to be integers, which are a number
		// between 1-9. We therefore use the IntegerGene class to represent each
		// of the genes. That class also lets us specify a lower and upper bound,
		// which we set to 1 and 9 for each empty cell.
		// --------------------------------------------------------------
		int chromosomeLength = (int) Math.sqrt(QQWing.BOARD_SIZE);
		
		Gene[] sampleGenes = new Gene[chromosomeLength];
		for (int i = 0; i < chromosomeLength; i++) {
			sampleGenes[i] = new CompositeGene(conf, new IntegerGene(conf, 1, 9));
			for(int j = 0; j < chromosomeLength; j++) {
				if(sudokuArr[j] == 0)
					((CompositeGene) sampleGenes[i]).addGene(new IntegerGene(conf, 1, 9), false);
			}
		}
		IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
		conf.setSampleChromosome(sampleChromosome);

		// Finally, we need to tell the Configuration object how many
		// Chromosomes we want in our population. The more Chromosomes,
		// the larger number of potential solutions (which is good for
		// finding the answer), but the longer it will take to evolve
		// the population (which could be seen as bad).
		// ------------------------------------------------------------
		conf.setPopulationSize(50);
		
		//Initialize rows 
		IChromosome[] initialArray = new Chromosome[50];
		int[][] matrixSudoku = sudoku.reconstructPuzzle();
		ArrayList<Integer> initNums = new ArrayList<>();
		for(int i = 1; i < 10; i++)
			initNums.add(Integer.valueOf(i));
		for(int i = 0; i < 50; i++) {			
			IChromosome initial = new Chromosome(conf,sampleGenes);
		
			Gene[] rowsSolution = new Gene[chromosomeLength];
			for(int j=0;j < chromosomeLength;j++){
				ArrayList<Integer> nums = initNums;
				Vector<Integer> rowGenes = new Vector<>();
				for(int k = 0; k < chromosomeLength; k++) {
					if(nums.contains(matrixSudoku[j][k]))
						nums.remove(nums.indexOf(matrixSudoku[j][k]));
				}
				System.out.println("nums.size:"+nums.size());
				int l = 0;
				for(int k = 0; k < chromosomeLength; k++) {
					if(matrixSudoku[j][k] == 0) {
						rowGenes.add(nums.get(l++));
						System.out.println("matrixSudoku["+j+"]["+k+"]:"+matrixSudoku[j][k]+" == 0?");
					}
				}
				rowsSolution[j] = new CompositeGene(conf, new IntegerGene(conf, 1, 9));
				System.out.println("XXXXXXX");
				rowsSolution[j].setAllele(rowGenes);
			}
			
			initial.setGenes(rowsSolution);
			initialArray[i] = initial;
		}
		
		Genotype population = new Genotype(conf, new Population(conf, initialArray));

		// Create random initial population of Chromosomes.
		// Evolve the population. Until the result obtained is valid or we
		// surpass the max number of iterations the population keeps evolving.
		// ---------------------------------------------------------------
		for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
			if (resultIsValid(population.getFittestChromosome()))
				break;
			population.evolve();
		}
		// Display the best solution we found.
		// -----------------------------------
		IChromosome bestSolutionSoFar = population.getFittestChromosome();
		QQWing bestSudokuSolution = new QQWing();
		bestSudokuSolution.setPuzzle(sudoku.reconstructSudokuArr(sudoku.reconstructPuzzle(bestSolutionSoFar)));
		System.out.println("The best solution has a fitness value of " +
				bestSolutionSoFar.getFitnessValue());
		System.out.println("It contained the following: ");
		bestSudokuSolution.printPuzzle();
		System.out.println("It was expected: ");
		sudoku.getSudoku().printSolution();
	}
	
	/**
	 * Checks if the best chromosome of a generation is a valid solution to the sudoku.
	 * 
	 * @param best Best chromosome from given population
	 *
	 * @author iLopezosa
	 * @version 1.0
	 * 
	 */ 
	private static boolean resultIsValid(IChromosome best) {
		// If best has MAX_BOUND for fitnessValue return true
		return (best.getFitnessValue() >= MAX_BOUND) ? true : false; 
	}

	/**
	 * Main method. Solves random sudoku.
	 *
	 * @author iLopezosa, Dani130301, pablogodiaz
	 * @version 1.0
	 */
	public static void main(String[] args) {

		try {
			solveSudoku(new Sudoku());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

