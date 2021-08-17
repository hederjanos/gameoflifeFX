package hu.hj.gameoflifefx.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CellTest {
    private Cell cell;

    @Before
    public void setUp() {
        cell = new Cell(5, 5);
    }

    @Test
    public void testEquals() {
        Cell cell1 = new Cell(5, 5);
        Cell cell2 = new Cell(5, 6);
        assertEquals(cell, cell1);
        assertNotEquals(cell, cell2);
    }

    @Test
    public void testSetNeighbours() {

        cell.setNeighbours();
        Set<Cell> actualCells = cell.getNeighbours();

        Set<Cell> expectedCells = new HashSet<>();
        expectedCells.add(new Cell(5, 4));
        expectedCells.add(new Cell(6, 5));
        expectedCells.add(new Cell(5, 6));
        expectedCells.add(new Cell(4, 5));
        expectedCells.add(new Cell(4, 4));
        expectedCells.add(new Cell(6, 4));
        expectedCells.add(new Cell(4, 6));
        expectedCells.add(new Cell(6, 6));

        assertEquals(actualCells, expectedCells);
    }

}
