package th.ac.kmutt.cpe.algorithm.suntalumiti.model;

import java.util.List;

public class PathResult {
    private final List<Cell> path;
    private final int totalCost;
    private final String algorithmUsed;
    private final long executionTime;

    public PathResult(List<Cell> path, int totalCost, String algorithmUsed, long executionTime) {
        this.path = path;
        this.totalCost = totalCost;
        this.algorithmUsed = algorithmUsed;
        this.executionTime = executionTime;
    }

    public List<Cell> getPath() {
        return path;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public String getAlgorithmUsed() {
        return algorithmUsed;
    }

    public long getExecutionTime() {
        return executionTime;
    }
    
    public boolean isFound() {
        return path != null && !path.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s Result: Cost=%d, Steps=%d, Time=%.2f ms", 
            algorithmUsed, totalCost, path.size(), (double) executionTime);
    }
}