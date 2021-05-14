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

import java.io.*;
import org.jgap.*;
import org.jgap.data.*;
import org.jgap.impl.*;
import org.jgap.xml.*;
import org.w3c.dom.*;

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
	private static final int MAX_ALLOWED_EVOLUTIONS = 140;



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
				new SudokuFitnessFunction(sudokuArr);
		conf.setFitnessFunction(myFunc);
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
		int chromosomeLength = 0;
		for (int i = 0; i < sudokuArr.length; i++)
			// If sudokuArr[i] == 0 (empty cell) increase length of chromosome
			chromosomeLength += (sudokuArr[i] == 0) ? 1 : 0 ; 

		Gene[] sampleGenes = new Gene[chromosomeLength];
		for (int i = 0; i < chromosomeLength; i++)
			sampleGenes[i] = new IntegerGene(conf, 1, 9);

		IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
		conf.setSampleChromosome(sampleChromosome);
		// Finally, we need to tell the Configuration object how many
		// Chromosomes we want in our population. The more Chromosomes,
		// the larger number of potential solutions (which is good for
		// finding the answer), but the longer it will take to evolve
		// the population (which could be seen as bad).
		// ------------------------------------------------------------
		conf.setPopulationSize(50);
		// Create random initial population of Chromosomes.
		Genotype population = Genotype.randomInitialGenotype(conf);
		// Evolve the population. Until the result obtained is valid or we
		// surpass the max number of iterations the population keeps evolving.
		// ---------------------------------------------------------------
		for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
			if (resultIsValid(population.getFittestChromosome()))
				break;
			population.evolve();
		}
		// Save progress to file.
		// Represent Genotype as tree with elements Chromosomes and Genes.
		// ------------------------------------------------------------
		DataTreeBuilder builder = DataTreeBuilder.getInstance();
		IDataCreators doc2 = builder.representGenotypeAsDocument(population);
		// create XML document from generated tree
		// ---------------------------------------
		XMLDocumentBuilder docbuilder = new XMLDocumentBuilder();
		Document xmlDoc = (Document) docbuilder.buildDocument(doc2);
		XMLManager.writeFile(xmlDoc, new File("knapsackJGAP.xml"));
		// Display the best solution we found.
		// -----------------------------------
		IChromosome bestSolutionSoFar = population.getFittestChromosome();
		QQWing bestSudokuSolution = new QQWing();
		bestSudokuSolution.setPuzzle(reconstructPuzzle(bestSolutionSoFar, sudokuArr));
		System.out.println("The best solution has a fitness value of " +
				bestSolutionSoFar.getFitnessValue());
		System.out.println("It contained the following: ");
		bestSudokuSolution.printPuzzle();
		System.out.println("It was expected: ");
		sudoku.writeSolution();
	}
	
	/**
	 * Turns chromosome into sudoku in the form of an int array.
	 * 
	 * @param bestSolutionSoFar Chromosome to be converted into array
	 * @param initPuzzle Initial sudoku from where Chromosome was created
	 *
	 * @author iLopezosa
	 * @version 1.0
	 * 
	 */
	private static int[] reconstructPuzzle(IChromosome bestSolutionSoFar, int[] initPuzzle) {
		int[] convertedChromosome = new int[initPuzzle.length];

		for(int i = 0; i < initPuzzle.length; i++) 
			convertedChromosome[i] = (initPuzzle[i] == 0) ? 
					((Integer) bestSolutionSoFar.getGene(i).getAllele()).intValue() 
					: initPuzzle[i];

		return convertedChromosome;
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
		return (best.getFitnessValue() == 1000000000) ? true : false; 
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

