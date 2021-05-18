package Sudoku;

import java.util.StringJoiner;

import org.jgap.*;
import org.jgap.impl.*;
import com.qqwing.*;

/*
 * You must develop a Java console application that does the following:

-1. 	Generate a 9 x 9 matrix that is a valid Sudoku puzzle. This must be done
	with the help of the QQWing library, version 1.3.4, which you should
	download from the virtual campus. Then the puzzle and its solution must be
	printed out.

-2. 	Run a genetic algorithm that tries to solve the generated Sudoku puzzle.
	Each time that a new generation is obtained, the application must print the 
	mean fitness of the individuals, and the fitness of the best individual. The
	genetic algorithm must be based on the JGAP library, version 3.6.3.

-3. 	Once the genetic algorithm has finished, a message must be printed
	indicating whether the solution to the puzzle was found. After that, the best
	individual of the last generation must be printed out. 	
	
	
-Optional task:
 		You can show the progression of the genetic algorithm graphically, by showing
	how the mean fitness and the fitness of the best individual progress as new generations
	are obtained. You must also analyze how the performance of the genetic algorithm
	changes as the genetic algorithm parameters are changed. 
 */


public class main {

	public static void main(String[] args) {
		
		Sudoku s = new Sudoku();
		System.out.println(s.writePuzzle());
		System.out.println(s.writeSolution());
		
		
		
		System.out.println(); System.out.println("Sudoku Solved: ");
		QQWing solved = new QQWing();
		solved.setPuzzle(s.getSolution());
		System.out.println(solved.getPuzzleString());
	}


}
