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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.jgap.*;
import org.jgap.event.EventManager;
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

	public static final int MAX_BOUND = Integer.MAX_VALUE;

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
		Configuration conf = new Configuration();
		try {
			conf.setBreeder(new GABreeder());
			conf.setRandomGenerator(new StockRandomGenerator());
			conf.setEventManager(new EventManager());
			BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector(conf, 0.90d);
			bestChromsSelector.setDoubletteChromosomesAllowed(true);
			conf.addNaturalSelector(bestChromsSelector, false);
			conf.setMinimumPopSizePercent(0);
			conf.setSelectFromPrevGen(1.0d);
			conf.setKeepPopulationSizeConstant(true);
			conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
			conf.setChromosomePool(new ChromosomePool());
			
			conf.addGeneticOperator(new SudokuCrossoverOperator(conf));	
			conf.addGeneticOperator(new SudokuMutationOperator(conf, 12));
		} catch (InvalidConfigurationException e) {
			throw new RuntimeException(
					"Fatal error: DefaultConfiguration class could not use its "
							+ "own stock configuration values. This should never happen. "
							+ "Please report this as a bug to the JGAP team.");
		}
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
		
		// Initial correct chromosome
		List<?>[] initialRowValue = new List[chromosomeLength];
		IChromosome intermediaryChromosome = new Chromosome(conf,sampleGenes);
		Gene[] rows = new Gene[chromosomeLength];
		for(int j=0;j < chromosomeLength;j++){
			ArrayList<Integer> nums = new ArrayList<>(initNums);
			List<Integer> rowGenes = new Vector<>();
			
			for(int k = 0; k < chromosomeLength; k++) 
				if(nums.contains(matrixSudoku[j][k]))
					nums.remove(nums.indexOf(matrixSudoku[j][k]));
			
			int l = 0;
			for(int k = 0; k < chromosomeLength; k++)
				if(matrixSudoku[j][k] == 0)
					rowGenes.add(nums.get(l++));
			
			rows[j] = new CompositeGene(conf, new IntegerGene(conf, 1, 9));
			for(int k = 0; k < nums.size(); k++) 
				((CompositeGene) rows[j]).addGene(new IntegerGene(conf, 1, 9));
			
			rows[j].setAllele(rowGenes);
			// Save rowGenes for later
			initialRowValue[j] = new Vector<Integer>(rowGenes);
		}
		
		intermediaryChromosome.setGenes(rows);
		initialArray[0] = intermediaryChromosome;
		
		// Permutations
		for(int i = 1; i < 50; i++) {
			for(int j = 0; j < chromosomeLength; j++) {
				Collections.shuffle(initialRowValue[j]);
				rows[j].setAllele(initialRowValue[j]);
			}
			intermediaryChromosome.setGenes(rows);
			initialArray[i] = (IChromosome) intermediaryChromosome.clone();	
			
		}
		
//		System.out.println(Arrays.toString(conf.getGeneticOperators().toArray()));
		Genotype population = new Genotype(conf, new Population(conf, initialArray));
		
		// Create random initial population of Chromosomes.
		// Evolve the population. Until the result obtained is valid or we
		// surpass the max number of iterations the population keeps evolving.
		// ---------------------------------------------------------------
		for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
			System.out.println(i+" iteration");
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
		System.out.print("NO");
		try {
			solveSudoku(new Sudoku());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

