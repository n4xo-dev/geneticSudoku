package Sudoku;


public class TestMain {
	public static void main(String[] args) {
		Sudoku s = new Sudoku();
		SudokuFitnessFunction ff = new SudokuFitnessFunction(s);
		
		s.getSudoku().printPuzzle();
		s.getSudoku().printSolution();
		
		int rows[][] = new int[9][9];
		int columns[][] = new int[9][9];
		int blocks[][] = new int[9][9];
		for(int i = 0; i < 9; i++) {
			rows[i] = ff.extractRows(i*9, i*9+8 , s.getSolution());
			System.out.print("[");
			for(int j = 0; j < 9; j++) {
				System.out.print(rows[i][j] + ",");
			}
			System.out.print("]\n");
		}
		System.out.print("-----------------------------------------\n");
		for(int i = 0; i < 9; i++) {
			columns[i] = ff.extractColumns(i, 9, s.getSolution());
			System.out.print("[");
			for(int j = 0; j < 9; j++) {
				System.out.print(columns[i][j] + ",");
			}
			System.out.print("]\n");			
		}
		System.out.print("-----------------------------------------\n");
		for(int i = 0; i < 9; i++) {
			blocks[i] = ff.extractBlocks(i, 9, s.getSolution());
			System.out.print("[");
			for(int j = 0; j < 9; j++) {
				System.out.print(blocks[i][j] + ",");
			}
			System.out.print("]\n");			
		}
	}
}
