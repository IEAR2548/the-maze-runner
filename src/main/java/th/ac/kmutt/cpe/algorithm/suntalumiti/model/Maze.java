package th.ac.kmutt.cpe.algorithm.suntalumiti.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Maze {
    private Cell[][] grid;
    private int rows;
    private int cols;
    private Cell startCell;
    private Cell goalCell;

    public Maze(String mazeFileName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("data/" + mazeFileName)) {
            if (is == null) {
                System.err.println("Maze file not found in resources: data/" + mazeFileName);
                initializeEmptyMaze();
            } else {
                loadMaze(is);
            }
        } catch (Exception e) {
            System.err.println("Error loading maze file: " + mazeFileName);
            e.printStackTrace();
            initializeEmptyMaze();
        }
    }

    public Maze(InputStream is) {
        loadMaze(is);
    }

    private void loadMaze(InputStream is) {
        if (is == null) {
            initializeEmptyMaze();
            return;
        }
        List<List<Cell>> cellRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int currentRow = 0;

            while ((line = br.readLine()) != null) {
                String trimmedLine = line.trim();
                
                if (trimmedLine.isEmpty() || trimmedLine.matches("^#+$")) {
                    continue;
                }

                if (trimmedLine.startsWith("#") && trimmedLine.endsWith("#")) {
                    if (trimmedLine.length() > 2) {
                        trimmedLine = trimmedLine.substring(1, trimmedLine.length() - 1);
                    } else {
                        continue;
                    }
                }
                String normalizedLine = trimmedLine
                    .replaceAll("#", " # ")
                    .replaceAll("S", " S ")
                    .replaceAll("G", " G ")
                    .replaceAll("\"\"", " ")
                    .replaceAll("\"", "")
                    .trim()
                    .replaceAll("\\s+", " "); 

                String[] tokens = normalizedLine.split(" ");

                List<Cell> rowList = new ArrayList<>();
                int currentCol = 0;

                for (String token : tokens) {
                    if (token.isEmpty()) continue;
                    
                    boolean isWall = false;
                    boolean isStart = false;
                    boolean isGoal = false;
                    int timeCost = 0;

                    if (token.equals("#")) {
                        isWall = true;
                        timeCost = 0; 
                    } else if (token.equals("S")) {
                        isStart = true;
                        timeCost = 0; 
                    } else if (token.equals("G")) {
                        isGoal = true;
                        timeCost = 0;
                    } else {
                        try {
                            timeCost = Integer.parseInt(token);
                        } catch (NumberFormatException e) {
                            timeCost = 1; 
                        }
                    }

                    Cell cell = new Cell(currentRow, currentCol, timeCost, isWall, isStart, isGoal);
                    rowList.add(cell);

                    if (isStart) startCell = cell;
                    if (isGoal) goalCell = cell;

                    currentCol++;
                }

                if (!rowList.isEmpty()) {
                    cellRows.add(rowList);
                    currentRow++;
                }
            }

            this.rows = cellRows.size();
            
            if (this.rows > 0) {
                this.cols = cellRows.get(0).size();
                this.grid = new Cell[rows][cols];
                for (int i = 0; i < rows; i++) {
                    if (cellRows.get(i).size() != this.cols) {
                         System.err.println("Warning: Maze rows are not uniform in length!");
                    }
                    this.grid[i] = cellRows.get(i).toArray(new Cell[this.cols]); 
                }
            } else {
                initializeEmptyMaze();
            }
            
        } catch (IOException e) {
            System.err.println("Error reading maze data: " + e.getMessage());
            e.printStackTrace();
            initializeEmptyMaze();
        }
    }

    private void initializeEmptyMaze() {
        this.rows = 0;
        this.cols = 0;
        this.grid = new Cell[0][0];
        this.startCell = null;
        this.goalCell = null;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public Cell getCell(int r, int c) {
        if (r >= 0 && r < rows && c >= 0 && c < cols) {
            return grid[r][c];
        }
        return null;
    }
    public Cell getStartCell() { return startCell; }
    public Cell getGoalCell() { return goalCell; }
    public boolean isValid(int r, int c) {
        if (r >= 0 && r < rows && c >= 0 && c < cols) {
            return !grid[r][c].isWall();
        }
        return false;
    }
}