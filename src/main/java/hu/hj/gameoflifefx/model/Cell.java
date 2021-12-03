package hu.hj.gameoflifefx.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Cell {

    private final int x;
    private final int y;
    private final Set<Cell> neighbours;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.neighbours = new HashSet<>();
    }

    public void setNeighbours() {
        for (Direction direction : Direction.values()) {
            this.neighbours.add(new Cell(getX() + direction.getX(), getY() + direction.getY()));
        }
    }

    public Set<Cell> getNeighbours() {
        return new HashSet<>(neighbours);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return getX() == cell.getX() && getY() == cell.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
