package th.ac.kmutt.cpe.algorithm.suntalumiti.algo.classical;

import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Cell;

public class AStarNode implements Comparable<AStarNode> {
    public final Cell cell;
    public final AStarNode parent;
    public final int gCost;
    public final int hCost;
    public final int fCost;

    public AStarNode(Cell cell, AStarNode parent, int gCost, int hCost) {
        this.cell = cell;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
    }

    @Override
    public int compareTo(AStarNode other) {
        if (this.fCost != other.fCost) {
            return Integer.compare(this.fCost, other.fCost); 
        }
        return Integer.compare(this.hCost, other.hCost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AStarNode that = (AStarNode) o;
        return cell.equals(that.cell);
    }

    @Override
    public int hashCode() {
        return cell.hashCode();
    }
}
