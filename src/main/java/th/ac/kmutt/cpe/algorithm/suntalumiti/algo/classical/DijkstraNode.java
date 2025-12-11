package th.ac.kmutt.cpe.algorithm.suntalumiti.algo.classical;

import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Cell;

/**
 * Node structure used for Dijkstra's Algorithm.
 * Implements Comparable for PriorityQueue ordering based on distance (gCost).
 */
public class DijkstraNode implements Comparable<DijkstraNode> {
    public final Cell cell;
    public final DijkstraNode parent;
    public final int distance; // Accumulated time cost from start

    public DijkstraNode(Cell cell, DijkstraNode parent, int distance) {
        this.cell = cell;
        this.parent = parent;
        this.distance = distance;
    }

    @Override
    public int compareTo(DijkstraNode other) {
        // จัดเรียงใน PriorityQueue: โหนดที่มี distance (cost) น้อยที่สุดจะถูกเลือกก่อน
        return Integer.compare(this.distance, other.distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DijkstraNode that = (DijkstraNode) o;
        return cell.equals(that.cell);
    }

    @Override
    public int hashCode() {
        return cell.hashCode();
    }
}