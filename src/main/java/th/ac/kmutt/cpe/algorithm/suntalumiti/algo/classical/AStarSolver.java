package th.ac.kmutt.cpe.algorithm.suntalumiti.algo.classical;

import th.ac.kmutt.cpe.algorithm.suntalumiti.ISolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Cell;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Maze;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.PathResult;

import java.util.*;

/**
 * Implementation of the A* Search Algorithm for finding the minimum time cost path.
 */
public class AStarSolver implements ISolver {
    
    // การเคลื่อนที่ 4 ทิศทาง (ขวา, ซ้าย, ล่าง, บน)
    private static final int[][] DIRECTIONS = {
        {0, 1}, {0, -1}, {1, 0}, {-1, 0}
    };
    
    @Override
    public String getName() {
        return "A* Search Algorithm";
    }

    @Override
    public PathResult solve(Maze maze) {
        long startTime = System.currentTimeMillis();
        
        Cell start = maze.getStartCell();
        Cell goal = maze.getGoalCell();
        
        if (start == null || goal == null) {
            return createFailureResult(getName(), startTime);
        }
        
        // PriorityQueue: ใช้จัดการโหนดที่จะสำรวจ โดยเลือกโหนด fCost ต่ำสุดก่อน
        PriorityQueue<AStarNode> openList = new PriorityQueue<>();
        
        // Map: เก็บค่า gCost ที่ต่ำที่สุดที่เคยเจอสำหรับแต่ละ Cell เพื่อปรับปรุงเส้นทาง
        Map<Cell, Integer> gCostMap = new HashMap<>();
        
        // โหนดเริ่มต้น
        int hStart = calculateManhattanDistance(start, goal);
        AStarNode startNode = new AStarNode(start, null, 0, hStart);
        
        openList.add(startNode);
        gCostMap.put(start, 0);

        AStarNode finalNode = null;

        // --- A* Search Loop ---
        while (!openList.isEmpty()) {
            AStarNode current = openList.poll();
            Cell currentCell = current.cell;

            // ตรวจสอบ: ถึงจุดสิ้นสุด (Goal) หรือยัง
            if (currentCell.equals(goal)) {
                finalNode = current;
                break;
            }

            // สำรวจเพื่อนบ้าน 4 ทิศทาง
            for (int[] direction : DIRECTIONS) {
                int nextR = currentCell.getRow() + direction[0];
                int nextC = currentCell.getCol() + direction[1];

                // ตรวจสอบ: อยู่นอกขอบเขต หรือเป็นกำแพงหรือไม่
                if (!maze.isValid(nextR, nextC)) {
                    continue;
                }

                Cell neighborCell = maze.getCell(nextR, nextC);
                
                // คำนวณ gCost ใหม่: gCost ปัจจุบัน + ค่าเวลาในการเดินไปยังเพื่อนบ้าน
                int tentativeGCost = current.gCost + neighborCell.getTimeCost();
                
                // ตรวจสอบ: ถ้าเส้นทางใหม่นี้ดีกว่าเส้นทางที่เคยพบก่อนหน้า
                if (tentativeGCost < gCostMap.getOrDefault(neighborCell, Integer.MAX_VALUE)) {
                    
                    // บันทึก gCost ที่ดีกว่า
                    gCostMap.put(neighborCell, tentativeGCost);
                    
                    // สร้างโหนดเพื่อนบ้านใหม่
                    int hNeighbor = calculateManhattanDistance(neighborCell, goal);
                    AStarNode neighborNode = new AStarNode(
                        neighborCell, 
                        current, 
                        tentativeGCost, 
                        hNeighbor
                    );
                    
                    // เพิ่มโหนดใหม่ที่ดีกว่าเข้าสู่ Open List
                    openList.add(neighborNode);
                }
            }
        }

        // --- Path Reconstruction and Result ---
        long endTime = System.currentTimeMillis();
        
        if (finalNode != null) {
            return reconstructPath(finalNode, startTime, endTime);
        } else {
            return createFailureResult(getName(), startTime);
        }
    }
    
    /**
     * Heuristic Function: Calculates Manhattan Distance (sum of absolute differences of coordinates).
     */
    private int calculateManhattanDistance(Cell current, Cell goal) {
        return Math.abs(current.getRow() - goal.getRow()) + Math.abs(current.getCol() - goal.getCol());
    }

    /**
     * Reconstructs the path from the goal node back to the start node.
     */
    private PathResult reconstructPath(AStarNode finalNode, long startTime, long endTime) {
        List<Cell> path = new LinkedList<>();
        AStarNode current = finalNode;
        while (current != null) {
            path.add(0, current.cell); 
            current = current.parent;
        }
        
        return new PathResult(
            path, 
            finalNode.gCost, 
            getName(), 
            endTime - startTime
        );
    }
    
    /**
     * Creates a PathResult object for a failed search.
     */
    private PathResult createFailureResult(String algorithmName, long startTime) {
        long endTime = System.currentTimeMillis();
        return new PathResult(
            Collections.emptyList(), 
            Integer.MAX_VALUE, 
            algorithmName, 
            endTime - startTime
        );
    }
}