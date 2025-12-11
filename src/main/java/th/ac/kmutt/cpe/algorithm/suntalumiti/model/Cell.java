package th.ac.kmutt.cpe.algorithm.suntalumiti.model;

public class Cell {
    private final int row;
    private final int col;
    private final int timeCost;
    private final boolean isWall;
    private final boolean isStart;
    private final boolean isGoal;

    public Cell(int row, int col, int timeCost, boolean isWall, boolean isStart, boolean isGoal) {
        this.row = row;
        this.col = col;
        this.timeCost = timeCost;
        this.isWall = isWall;
        this.isStart = isStart;
        this.isGoal = isGoal;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getTimeCost() {
        return isWall ? Integer.MAX_VALUE : timeCost;
    }

    public boolean isWall() {
        return isWall;
    }

    public boolean isStart() {
        return isStart;
    }

    public boolean isGoal() {
        return isGoal;
    }

    @Override
    public String toString() {
        if (isStart) return "S";
        if (isGoal) return "G";
        if (isWall) return "#";
        return String.valueOf(timeCost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}
