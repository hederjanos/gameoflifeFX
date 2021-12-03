package hu.hj.gameoflifefx.model;

import java.util.HashSet;
import java.util.Set;

public class GameBoard {

    private Set<Cell> cells;

    public GameBoard() {
        this.cells = new HashSet<>();
    }

    public Set<Cell> getCells() {
        return new HashSet<>(cells);
    }

    public void setCells(Set<Cell> cells) {
        this.cells = cells;
    }

    public void addCell(int x, int y) {
        Cell cell = new Cell(x, y);
        cell.setNeighbours();
        this.cells.add(cell);
    }

    public void removeCell(int x, int y) {
        Cell cell = new Cell(x, y);
        this.cells.remove(cell);
    }

    public void clear() {
        this.cells.clear();
    }

    public boolean isCellAlive(int x, int y) {
        return this.cells.contains(new Cell(x, y));
    }

    public void nextGeneration() {
        Set<Cell> nextGenerationCells = new HashSet<>();
        Set<Cell> possibleCells = getNextGenerationCells(nextGenerationCells);

        generateNewCells(nextGenerationCells, possibleCells);

        this.cells = nextGenerationCells;
    }

    private Set<Cell> getNextGenerationCells(Set<Cell> nextGenerationCells) {
        Set<Cell> possibleCells = new HashSet<>();
        for (Cell cell : this.cells) {
            int n = countNeighboursAlive(cell);
            if (n == 2 || n == 3) {
                nextGenerationCells.add(cell);
            }
            possibleCells.addAll(cell.getNeighbours());
        }
        return possibleCells;
    }

    private void generateNewCells(Set<Cell> nextGenerationCells, Set<Cell> possibleCells) {
        for (Cell cell : possibleCells) {
            cell.setNeighbours();
            int n = countNeighboursAlive(cell);
            if (n == 3) {
                nextGenerationCells.add(cell);
            }
        }
    }

    private int countNeighboursAlive(Cell cell) {
        int count = 0;
        for (Cell c : cell.getNeighbours()) {
            if (this.cells.contains(c)) {
                count++;
            }
        }
        return count;
    }

    public boolean hasMoreCell() {
        return !this.cells.isEmpty();
    }

    public void print() {
        int minX;
        int maxX;
        int minY;
        int maxY;
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;
        for (Cell cell : this.cells) {
            minX = Math.min(minX, cell.getX());
            minY = Math.min(minY, cell.getY());
            maxX = Math.max(maxX, cell.getX());
            maxY = Math.max(maxY, cell.getX());
        }

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (this.cells.contains(new Cell(x, y))) {
                    System.out.print('O');
                } else {
                    System.out.print(' ');
                }
            }
            System.out.println();
        }
        System.out.println("============================================================");
    }
}