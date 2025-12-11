package th.ac.kmutt.cpe.algorithm.suntalumiti.algo.classical;

import th.ac.kmutt.cpe.algorithm.suntalumiti.ISolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Cell;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Maze;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.PathResult;

import java.util.*;

public class DijkstraSolver implements ISolver {
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
        PriorityQueue<DijkstraNode> pq = new PriorityQueue<>();
        Map<Cell, Integer> distanceMap = new HashMap<>();
        DijkstraNode startNode = new DijkstraNode(start, null, 0);   
        pq.add(startNode);
        distanceMap.put(start, 0);
        DijkstraNode finalNode = null;
        while (!pq.isEmpty()) {
            DijkstraNode current = pq.poll();
            Cell currentCell = current.cell;
            if (current.distance > distanceMap.getOrDefault(currentCell, Integer.MAX_VALUE)) {
                continue;
            }
            if (currentCell.equals(goal)) {
                finalNode = current;
                break; 
            }
            for (int[] direction : DIRECTIONS) {
                int nextR = currentCell.getRow() + direction[0];
                int nextC = currentCell.getCol() + direction[1];
                if (!maze.isValid(nextR, nextC)) {
                    continue;
                }
                Cell neighborCell = maze.getCell(nextR, nextC);
                int newDistance = current.distance + neighborCell.getTimeCost();
                if (newDistance < distanceMap.getOrDefault(neighborCell, Integer.MAX_VALUE)) {
                    distanceMap.put(neighborCell, newDistance);
                    DijkstraNode neighborNode = new DijkstraNode(
                        neighborCell, 
                        current, 
                        newDistance
                    );
                    pq.add(neighborNode);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        if (finalNode != null) {
            return reconstructPath(finalNode, startTime, endTime);
        } else {
            return createFailureResult(getName(), startTime);
        }
    }

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