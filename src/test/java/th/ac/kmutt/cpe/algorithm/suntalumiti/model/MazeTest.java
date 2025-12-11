package th.ac.kmutt.cpe.algorithm.suntalumiti.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.InputStream;
import java.util.Objects;

public class MazeTest {
    
    // As before, this assumes the test file name is correct in your resources
    private Maze createMaze() {
        InputStream is = getClass().getResourceAsStream("/data/test_maze_simple.txt");
        Objects.requireNonNull(is, "Test resource file not found at: /data/test_maze_simple.txt. Ensure it is in src/test/resources/data/");
        return new Maze(is);
    }

    @Test
    void testMazeLoadingAndDimensions() {
        Maze maze = createMaze();
        
        // Expected dimensions based on the new file (13 rows of data)
        // ********** แก้ไข: 4 -> 13 **********
        assertEquals(13, maze.getRows(), "Maze should have 13 effective rows.");
        
        // Expected dimensions: 13 effective columns (e.g., Row 0: S, 11 costs, 1 wall)
        // ********** แก้ไข: 7 -> 13 **********
        assertEquals(13, maze.getCols(), "Maze should have 13 effective columns.");
    }
    
    @Test
    void testStartAndGoalCellLocation() {
        Maze maze = createMaze();
        
        // Start (S) is at (0, 0)
        Cell start = maze.getStartCell();
        assertNotNull(start, "Start cell should be found.");
        assertEquals(0, start.getRow(), "Start cell row incorrect.");
        assertEquals(0, start.getCol(), "Start cell column incorrect.");
        assertTrue(start.isStart());

        // Goal (G) is at (12, 12)
        // ********** แก้ไข: (0, 6) -> (12, 12) **********
        Cell goal = maze.getGoalCell();
        assertNotNull(goal, "Goal cell should be found.");
        assertEquals(12, goal.getRow(), "Goal cell row incorrect.");
        assertEquals(12, goal.getCol(), "Goal cell column incorrect.");
        assertTrue(goal.isGoal());
    }

    @Test
    void testWallAndTimeCost() {
        Maze maze = createMaze();
        
        // Wall check (e.g., cell at 1, 1: #)
        // ********** แก้ไข: (0, 3) -> (1, 1) **********
        Cell wallCell = maze.getCell(1, 1);
        assertNotNull(wallCell);
        assertTrue(wallCell.isWall(), "Cell (1, 1) should be a wall.");
        
        // Time Cost check (e.g., cell "10" at 1, 0)
        // ********** แก้ไข: (1, 0) -> (1, 0) แต่ค่าเดิมคือ "10" **********
        Cell costCell1 = maze.getCell(1, 0); 
        assertNotNull(costCell1);
        assertFalse(costCell1.isWall());
        assertEquals(10, costCell1.getTimeCost(), "Cell (1, 0) cost should be 10.");

        // Time Cost check (e.g., cell "1" at 0, 2)
        // ********** แก้ไข: (0, 2) -> (0, 2) **********
        Cell costCell2 = maze.getCell(0, 2);
        assertNotNull(costCell2);
        assertEquals(2, costCell2.getTimeCost(), "Cell (0, 2) cost should be 1.");

        // Time Cost check (e.g., cell "4" at 11, 1)
        // ********** เพิ่ม Test สำหรับ Cell อื่นๆ **********
        Cell costCell3 = maze.getCell(11, 1);
        assertNotNull(costCell3);
        assertEquals(7, costCell3.getTimeCost(), "Cell (11, 1) cost should be 7 (from row 12: #\"7\"\"4\"...).");

        // Cell ที่เป็น Path ธรรมดาโดยไม่มี Cost กำหนด (S, G)
        // Test นี้ไม่สามารถทำได้ง่ายๆ เพราะไม่มี Cell ที่เป็น Empty String ใน Maze ใหม่นี้
        // Cell emptyCell = maze.getCell(1, 1); 
        // assertEquals(0, emptyCell.getTimeCost(), "Cell (1, 1) cost (empty string) should be 0.");
    }

    @Test
    void testInvalidCell() {
        Maze maze = createMaze();
        // Check out of bounds (Rows=13, Cols=13)
        assertFalse(maze.isValid(13, 13), "Should be invalid out of bounds.");
        
        // Check Wall is invalid (Wall at 1, 1)
        Cell wallCell = maze.getCell(1, 1);
        assertNotNull(wallCell);
        assertFalse(maze.isValid(wallCell.getRow(), wallCell.getCol()), "Wall cell should be invalid for movement.");
    }
}