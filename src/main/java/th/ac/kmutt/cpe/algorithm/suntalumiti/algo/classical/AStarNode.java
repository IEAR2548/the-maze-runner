package th.ac.kmutt.cpe.algorithm.suntalumiti.algo.classical;

import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Cell;

/**
 * Node structure used specifically for A* Search.
 * Implements Comparable for PriorityQueue ordering based on f_cost.
 */
public class AStarNode implements Comparable<AStarNode> {
    public final Cell cell;
    public final AStarNode parent;
    public final int gCost; // Cost from start (ผลรวมเวลาที่ลดลง)
    public final int hCost; // Heuristic cost to goal (ค่าประมาณการถึงจุดสิ้นสุด)
    public final int fCost; // Total estimated cost (g + h)

    public AStarNode(Cell cell, AStarNode parent, int gCost, int hCost) {
        this.cell = cell;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
    }

    @Override
    public int compareTo(AStarNode other) {
        // จัดเรียงใน PriorityQueue: โหนดที่มี fCost น้อยที่สุดจะถูกเลือกก่อน
        if (this.fCost != other.fCost) {
            return Integer.compare(this.fCost, other.fCost); 
        }
        // ใช้ hCost เป็นตัวตัดสินเมื่อ fCost เท่ากัน
        return Integer.compare(this.hCost, other.hCost);
    }

    // Nodes are equal if they represent the same Cell
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
