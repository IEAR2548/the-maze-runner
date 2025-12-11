package th.ac.kmutt.cpe.algorithm.suntalumiti.algo.classical;

import th.ac.kmutt.cpe.algorithm.suntalumiti.ISolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Cell;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Maze;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.PathResult;

import java.util.*;

/**
 * Implementation of Dijkstra's Algorithm for finding the minimum time cost path.
 */
public class DijkstraSolver implements ISolver {
    
    // การเคลื่อนที่ 4 ทิศทาง (ขวา, ซ้าย, ล่าง, บน)
    private static final int[][] DIRECTIONS = {
        {0, 1}, {0, -1}, {1, 0}, {-1, 0}
    };
    
    @Override
    public String getName() {
        return "Dijkstra's Algorithm";
    }

    @Override
    public PathResult solve(Maze maze) {
        long startTime = System.currentTimeMillis();
        
        Cell start = maze.getStartCell();
        Cell goal = maze.getGoalCell();
        
        if (start == null || goal == null) {
            return createFailureResult(getName(), startTime);
        }
        
        // PriorityQueue: จัดการโหนดที่จะสำรวจ โดยเลือกโหนดที่มี Accumulated Cost น้อยที่สุดก่อน
        PriorityQueue<DijkstraNode> pq = new PriorityQueue<>();
        
        // Map: เก็บค่าระยะทาง (Cost) ที่ต่ำที่สุดที่เคยเจอสำหรับแต่ละ Cell 
        Map<Cell, Integer> distanceMap = new HashMap<>();
        
        // โหนดเริ่มต้น
        DijkstraNode startNode = new DijkstraNode(start, null, 0);
        
        pq.add(startNode);
        distanceMap.put(start, 0);

        DijkstraNode finalNode = null;

        // --- Dijkstra's Loop ---
        while (!pq.isEmpty()) {
            DijkstraNode current = pq.poll();
            Cell currentCell = current.cell;

            // ป้องกันโหนดซ้ำซ้อน: ถ้าโหนดที่เพิ่งดึงออกมามี Cost มากกว่า Cost ที่ดีที่สุดที่เคยบันทึกไว้ ให้ข้าม
            if (current.distance > distanceMap.getOrDefault(currentCell, Integer.MAX_VALUE)) {
                continue;
            }

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
                
                // คำนวณ Cost ใหม่: Cost สะสมปัจจุบัน + ค่าเวลาในการเดินไปยังเพื่อนบ้าน
                int newDistance = current.distance + neighborCell.getTimeCost();
                
                // Relaxation Step: ถ้าเส้นทางใหม่นี้ดีกว่าเส้นทางที่เคยพบก่อนหน้า
                if (newDistance < distanceMap.getOrDefault(neighborCell, Integer.MAX_VALUE)) {
                    
                    // อัปเดต distance
                    distanceMap.put(neighborCell, newDistance);
                    
                    // สร้างและเพิ่มโหนดใหม่เข้าสู่ PriorityQueue
                    DijkstraNode neighborNode = new DijkstraNode(
                        neighborCell, 
                        current, 
                        newDistance
                    );
                    
                    pq.add(neighborNode);
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
     * Reconstructs the path from the goal node back to the start node.
     */
    private PathResult reconstructPath(DijkstraNode finalNode, long startTime, long endTime) {
        List<Cell> path = new LinkedList<>();
        DijkstraNode current = finalNode;
        while (current != null) {
            path.add(0, current.cell); 
            current = current.parent;
        }
        
        return new PathResult(
            path, 
            finalNode.distance, 
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