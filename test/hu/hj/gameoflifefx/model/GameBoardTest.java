package hu.hj.gameoflifefx.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameBoardTest {

    private GameBoard gameBoard;

    @Before
    public void setUp() {
        gameBoard = new GameBoard();
    }

    @Test
    public void testAddCell() {
        gameBoard.addCell(0, 0);

        assertTrue(gameBoard.isCellAlive(0, 0));
    }

    /**
     * Rule 1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
     */
    @Test
    public void testRule1_noNeighbours() {
        gameBoard.nextGeneration();

        assertFalse(gameBoard.isCellAlive(0, 0));
    }

    /**
     * Rule 1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
     */
    @Test
    public void testRule1_oneNeighbour() {
        gameBoard.addCell(0, 0);
        gameBoard.addCell(1, 0);
        gameBoard.nextGeneration();

        assertFalse(gameBoard.isCellAlive(0, 0));
    }

    /**
     * Rule 2. Any live cell with two or three live neighbours lives on to the next generation.
     */
    @Test
    public void testRule2_twoNeighbours() {
        gameBoard.addCell(0, 0);
        gameBoard.addCell(1, 0);
        gameBoard.addCell(1, 1);
        gameBoard.nextGeneration();

        assertTrue(gameBoard.isCellAlive(0, 0));
    }

    /**
     * Rule 2. Any live cell with two or three live neighbours lives on to the next generation.
     */
    @Test
    public void testRule2_threeNeighbours() {
        gameBoard.addCell(0, 0);
        gameBoard.addCell(1, 0);
        gameBoard.addCell(1, 1);
        gameBoard.addCell(0, 1);
        gameBoard.nextGeneration();

        assertTrue(gameBoard.isCellAlive(0, 0));
    }

    /**
     * Rule 3. Any live cell with more than three live neighbours dies, as if by overpopulation.
     */
    @Test
    public void testRule3_fourNeighbours() {
        gameBoard.addCell(0, 0);
        gameBoard.addCell(1, 0);
        gameBoard.addCell(1, 1);
        gameBoard.addCell(0, 1);
        gameBoard.addCell(-1, 1);
        gameBoard.nextGeneration();

        assertFalse(gameBoard.isCellAlive(0, 0));
    }

    /**
     * Rule 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
     */
    @Test
    public void testRule4_resurrection() {
        gameBoard.addCell(1, 0);
        gameBoard.addCell(1, 1);
        gameBoard.addCell(0, 1);
        gameBoard.nextGeneration();

        assertTrue(gameBoard.isCellAlive(0, 0));
    }
}