package com.sudoku.computationlogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sudoku.problemdomain.Coordinate;
import com.sudoku.problemdomain.SudokuGame;

public class GameGenerator {
    public static int[][] getNewGameGrid() {
        return unnsolveGame(getSolvedGame());
    }

    private static int[][] unnsolveGame(int[][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());

        boolean solvable = false;
        int[][] solvableArray = new int[SudokuGame.GRID_BOUNDARY][SudokuGame.GRID_BOUNDARY];

        while (solvable == false) {
            SudokuUtilities.copySudokuArrayValues(solvedGame, solvableArray);

            int index = 0;

            while (index < 40) {
                int xCoordinate = random.nextInt(SudokuGame.GRID_BOUNDARY);
                int yCoordinate = random.nextInt(SudokuGame.GRID_BOUNDARY);

                if (solvableArray[xCoordinate][yCoordinate] != 0) {
                    solvableArray[xCoordinate][yCoordinate] = 0;
                    index++;
                }
            }

            int[][] toBeSolved = new int[SudokuGame.GRID_BOUNDARY][SudokuGame.GRID_BOUNDARY];
            SudokuUtilities.copySudokuArrayValues(solvableArray, toBeSolved);

            solvable = SudokuSolver.puzzleIsSolvable(toBeSolved);
        }
        return solvableArray;
    }

    private static int[][] getSolvedGame() {
        Random random = new Random(System.currentTimeMillis());
        int[][] newGrid = new int[SudokuGame.GRID_BOUNDARY][SudokuGame.GRID_BOUNDARY];

        for (int value = 1; value < SudokuGame.GRID_BOUNDARY; value++) {
            int allocation = 0;
            int interrupt = 0;

            List<Coordinate> alloctracker = new ArrayList<>();

            int attempts = 0;

            while (allocation < SudokuGame.GRID_BOUNDARY) {
                if (interrupt > 200) {
                    alloctracker.forEach(coord -> {
                        newGrid[coord.getX()][coord.getY()] = 0;
                    });

                    interrupt = 0;
                    allocation = 0;
                    alloctracker.clear();
                    attempts++;

                    if (attempts > 500) {
                        clearArray(newGrid);
                        attempts = 0;
                        value = 1;
                    }
                }

                int xCoordinate = random.nextInt(SudokuGame.GRID_BOUNDARY);
                int yCoordinate = random.nextInt(SudokuGame.GRID_BOUNDARY);

                if (newGrid[xCoordinate][yCoordinate] == 0) {
                    newGrid[xCoordinate][yCoordinate] = value;

                    if (GameLogic.sudokuIsInValid(newGrid)) {
                        newGrid[xCoordinate][yCoordinate] = 0;
                        interrupt++;
                    }
                    else {
                        alloctracker.add(new Coordinate(xCoordinate, yCoordinate));
                        allocation++;
                    }
                }
            }
        }

        return newGrid;
    }

    private static void clearArray(int[][] newGrid) {
        for (int xIndex = 0; xIndex < SudokuGame.GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < SudokuGame.GRID_BOUNDARY; yIndex++) {
                newGrid[xIndex][yIndex] = 0;
            }
        }
    }
}
