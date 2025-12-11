package th.ac.kmutt.cpe.algorithm.suntalumiti.algo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.util.Objects;

import th.ac.kmutt.cpe.algorithm.suntalumiti.ISolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.algo.classical.AStarSolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.algo.classical.DijkstraSolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.algo.ga.GeneticSolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Maze;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.PathResult;

import static org.junit.jupiter.api.Assertions.*;

public class SolverTest {

    private static Maze simpleMaze;
    // ********** แก้ไข: อัปเดต Cost ต่ำสุดสำหรับ Maze 13x13 ใหม่ **********
    private static final int EXPECTED_OPTIMAL_COST = 124; 
    // ***************************************************************
    
    @BeforeAll
    static void setup() {
        // ใช้วิธีโหลด Resource แบบ Static context และสร้าง Maze ด้วย InputStream
        InputStream is = SolverTest.class.getResourceAsStream("/data/test_maze_simple.txt");
        // ตรวจสอบให้แน่ใจว่าไฟล์ถูกพบ
        Objects.requireNonNull(is, "Test resource file not found at: /data/test_maze_simple.txt. Ensure it is in src/test/resources/data/");
        
        simpleMaze = new Maze(is);
        // การ Assert ตรงนี้ต้องผ่านได้ทันที หาก Maze.java ถูกแก้ไขเรื่อง Goal Cell แล้ว
        assertNotNull(simpleMaze.getStartCell(), "Maze must have a start cell.");
        assertNotNull(simpleMaze.getGoalCell(), "Maze must have a goal cell.");
    }
    
    // --- Classical Algorithms Test ---

    @Test
    void testDijkstraSolverForOptimalCost() {
        ISolver solver = new DijkstraSolver();
        PathResult result = solver.solve(simpleMaze);

        assertTrue(result.isFound(), "Dijkstra's should find a path.");
        // Dijkstra's guarantees the optimal solution
        assertEquals(EXPECTED_OPTIMAL_COST, result.getTotalCost(), "Dijkstra's must find the expected minimum cost (" + EXPECTED_OPTIMAL_COST + "). Found: " + result.getTotalCost());
        assertTrue(result.getPath().size() > 2, "Path length should be greater than 2 steps.");
    }

    @Test
    void testAStarSolverForOptimalCost() {
        ISolver solver = new AStarSolver();
        PathResult result = solver.solve(simpleMaze);

        assertTrue(result.isFound(), "A* should find a path.");
        // A* with valid heuristic guarantees the optimal solution
        assertEquals(EXPECTED_OPTIMAL_COST, result.getTotalCost(), "A* must find the expected minimum cost (" + EXPECTED_OPTIMAL_COST + "). Found: " + result.getTotalCost());
        assertTrue(result.getPath().size() > 2, "Path length should be greater than 2 steps.");
    }
    
    // --- Genetic Algorithm Test ---

    @Test
    void testGeneticSolverGoalReachedAndNearOptimal() {
        ISolver solver = new GeneticSolver();
        PathResult result = solver.solve(simpleMaze);
        
        // GA is non-deterministic, so we check if it finds *a* path and if the cost is reasonable.
        assertTrue(result.isFound(), "GA should be able to find a path to the goal.");
        
        // ********** แก้ไข: อัปเดต Threshold ให้เหมาะสมกับ Cost 95 **********
        // 95 * 3 = 285.
        int maxAcceptableCost = EXPECTED_OPTIMAL_COST * 3;
        assertTrue(result.getTotalCost() <= maxAcceptableCost, 
                   "GA cost should be reasonably close to optimal (max " + maxAcceptableCost + "). Found: " + result.getTotalCost());
        // ***************************************************************
    }
}