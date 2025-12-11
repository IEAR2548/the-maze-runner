package th.ac.kmutt.cpe.algorithm.suntalumiti.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Cell;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Maze;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.PathResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A custom JavaFX component (Canvas) to draw and visualize the Maze and Path.
 */
public class MazePanel extends Canvas {
    private Maze maze;
    private PathResult result = new PathResult(Collections.emptyList(), 0, "None", 0);
    private final double CELL_SIZE = 30.0;
    
    public MazePanel() {
        // Set initial size
        setWidth(500);
        setHeight(500);
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        // Adjust canvas size based on maze dimensions
        if (maze != null) {
            setWidth(maze.getCols() * CELL_SIZE);
            setHeight(maze.getRows() * CELL_SIZE);
        }
        draw();
    }
    
    public void setResult(PathResult result) {
        this.result = result;
        draw();
    }

    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        if (maze == null) {
            gc.fillText("Load Maze File to Start", 10, 20);
            return;
        }

        Set<Cell> pathCells = new HashSet<>(result.getPath());

        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                Cell cell = maze.getCell(r, c);
                double x = c * CELL_SIZE;
                double y = r * CELL_SIZE;

                // 1. Draw Cell Background (Default: White)
                gc.setFill(Color.WHITE);
                if (cell.isWall()) {
                    gc.setFill(Color.BLACK); // Wall
                } else if (pathCells.contains(cell)) {
                    gc.setFill(Color.LIGHTGREEN); // Path Highlight
                }
                gc.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // 2. Draw Cell Border
                gc.setStroke(Color.GRAY);
                gc.setLineWidth(1);
                gc.strokeRect(x, y, CELL_SIZE, CELL_SIZE);

                // 3. Draw Text/Symbols
                gc.setFill(Color.BLACK);
                gc.setFont(Font.font("Monospaced", CELL_SIZE * 0.4));
                String text = "";
                
                if (cell.isStart()) {
                    text = "S";
                    gc.setFill(Color.DARKGREEN); // Start Cell Color
                } else if (cell.isGoal()) {
                    text = "G";
                    gc.setFill(Color.RED); // Goal Cell Color
                } else if (!cell.isWall() && cell.getTimeCost() > 0) {
                    text = String.valueOf(cell.getTimeCost());
                    gc.setFill(Color.DARKBLUE); // Cost Text Color
                }

                // Center the text
                double textWidth = text.length() * CELL_SIZE * 0.2; 
                gc.fillText(text, x + (CELL_SIZE - textWidth) / 2, y + CELL_SIZE / 2 + 5);
            }
        }
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}