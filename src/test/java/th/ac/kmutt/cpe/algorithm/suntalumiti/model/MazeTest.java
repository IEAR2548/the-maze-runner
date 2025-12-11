package th.ac.kmutt.cpe.algorithm.suntalumiti.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.InputStream;
import java.util.Objects;

public class MazeTest {
    private Maze createMaze() {
        InputStream is = getClass().getResourceAsStream("/data/test_maze_simple.txt");
        Objects.requireNonNull(is, "Test resource file not found at: /data/test_maze_simple.txt. Ensure it is in src/test/resources/data/");
        return new Maze(is);
    }

    @Test
    void testMazeLoadingAndDimensions() {
        Maze maze = createMaze();
        assertEquals(13, maze.getRows(), "Maze should have 13 effective rows.");
        assertEquals(13, maze.getCols(), "Maze should have 13 effective columns.");
    }
    
    @Test
    void testStartAndGoalCellLocation() {
        Maze maze = createMaze();
        Cell start = maze.getStartCell();
        assertNotNull(start, "Start cell should be found.");
        assertEquals(0, start.getRow(), "Start cell row incorrect.");
        assertEquals(0, start.getCol(), "Start cell column incorrect.");
        assertTrue(start.isStart());
        Cell goal = maze.getGoalCell();
        assertNotNull(goal, "Goal cell should be found.");
        assertEquals(12, goal.getRow(), "Goal cell row incorrect.");
        assertEquals(12, goal.getCol(), "Goal cell column incorrect.");
        assertTrue(goal.isGoal());
    }

    @Test
    void testWallAndTimeCost() {
        Maze maze = createMaze();
        Cell wallCell = maze.getCell(1, 1);
        assertNotNull(wallCell);
        assertTrue(wallCell.isWall(), "Cell (1, 1) should be a wall.");

        Cell costCell1 = maze.getCell(1, 0); 
        assertNotNull(costCell1);
        assertFalse(costCell1.isWall());
        assertEquals(10, costCell1.getTimeCost(), "Cell (1, 0) cost should be 10.");

        Cell costCell2 = maze.getCell(0, 2);
        assertNotNull(costCell2);
        assertEquals(2, costCell2.getTimeCost(), "Cell (0, 2) cost should be 1.");

        Cell costCell3 = maze.getCell(11, 1);
        assertNotNull(costCell3);
        assertEquals(7, costCell3.getTimeCost(), "Cell (11, 1) cost should be 7 (from row 12: #\"7\"\"4\"...).");
    }

    @Test
    void testInvalidCell() {
        Maze maze = createMaze();
        assertFalse(maze.isValid(13, 13), "Should be invalid out of bounds.");

        Cell wallCell = maze.getCell(1, 1);
        assertNotNull(wallCell);
        assertFalse(maze.isValid(wallCell.getRow(), wallCell.getCol()), "Wall cell should be invalid for movement.");
    }
}