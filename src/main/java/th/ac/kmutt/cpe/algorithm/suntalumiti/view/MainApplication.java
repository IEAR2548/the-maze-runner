package th.ac.kmutt.cpe.algorithm.suntalumiti.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import th.ac.kmutt.cpe.algorithm.suntalumiti.ISolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.algo.classical.AStarSolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.algo.classical.DijkstraSolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.algo.ga.GeneticSolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Maze;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.PathResult;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Main JavaFX Application for the Maze Runner Project.
 * Integrates the Model, Algorithms, and View.
 */
public class MainApplication extends Application {

    private Maze maze;
    private final MazePanel mazePanel = new MazePanel();
    private final Map<String, ISolver> solvers = new HashMap<>();
    private final Label resultLabel = new Label("Result: No maze loaded.");
    private final ComboBox<String> solverComboBox = new ComboBox<>();

    public MainApplication() {
        // Initialize all Solvers
        solvers.put(new AStarSolver().getName(), new AStarSolver());
        solvers.put(new DijkstraSolver().getName(), new DijkstraSolver());
        solvers.put(new GeneticSolver().getName(), new GeneticSolver());
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Maze Runner: Minimum Time Cost Pathfinding");

        // --- 1. Control Panel (Left Side) ---
        
        // 1.1 Load Button
        Button loadButton = new Button("Load Maze File");
        loadButton.setMaxWidth(Double.MAX_VALUE);
        loadButton.setOnAction(e -> loadMazeFromFile(stage));

        // 1.2 Solver Selection
        solverComboBox.getItems().addAll(solvers.keySet());
        solverComboBox.setValue(new AStarSolver().getName()); // Default selection
        solverComboBox.setMaxWidth(Double.MAX_VALUE);
        
        Label comboLabel = new Label("Select Algorithm:");
        
        // 1.3 Run Button
        Button runButton = new Button("Find Path");
        runButton.setMaxWidth(Double.MAX_VALUE);
        runButton.setOnAction(e -> runSolver());

        // 1.4 Result Display
        resultLabel.setId("result-label");
        resultLabel.setWrapText(true);
        resultLabel.setStyle("-fx-border-color: #ccc;");
        
        VBox controlBox = new VBox(10, loadButton, new Separator(), comboLabel, solverComboBox, runButton, new Separator(), resultLabel);
        controlBox.setPadding(new Insets(15));
        controlBox.setPrefWidth(250);
        controlBox.setAlignment(Pos.TOP_CENTER);
        
        // --- 2. Visualization Panel (Center/Scrollable) ---
        ScrollPane scrollPane = new ScrollPane(mazePanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: #f4f4f4;");

        // --- 3. Main Layout ---
        BorderPane root = new BorderPane();
        root.setLeft(controlBox);
        root.setCenter(scrollPane);

        // Initial setup
        mazePanel.draw(); 
        
        Scene scene = new Scene(root, 1000, 700);

        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Handles the file loading process using FileChooser.
     */
    private void loadMazeFromFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Maze Data File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            // Note: Maze.java currently expects the file to be in resources.
            // For simplicity in JavaFX, we assume the file name is used to load from resources.
            // In a production environment, you would use file.getAbsolutePath() and adjust Maze.java logic.
            
            // For now, we pass the file name assuming user copies it to src/main/resources/data/
            String fileName = file.getName();
            maze = new Maze(fileName);
            
            if (maze.getRows() > 0) {
                mazePanel.setMaze(maze);
                resultLabel.setText("Result: Maze '" + fileName + "' loaded successfully.");
            } else {
                resultLabel.setText("Result: Failed to load maze data.");
            }
        }
    }
    
    /**
     * Executes the selected solver on the loaded maze.
     */
    private void runSolver() {
        if (maze == null || maze.getRows() == 0) {
            resultLabel.setText("ERROR: Please load a valid maze file first.");
            return;
        }
        
        String selectedSolverName = solverComboBox.getValue();
        ISolver solver = solvers.get(selectedSolverName);
        
        if (solver == null) {
            resultLabel.setText("ERROR: Selected solver not found.");
            return;
        }

        // Run the solver (off-thread in a real app, but synchronous here for simplicity)
        PathResult result = solver.solve(maze);
        
        // Update UI
        mazePanel.setResult(result);
        
        if (result.isFound()) {
            resultLabel.setText(String.format(
                "Algorithm: %s\nPath Found!\nTotal Time Cost: %d\nSteps: %d\nTime: %.2f ms",
                result.getAlgorithmUsed(), 
                result.getTotalCost(), 
                result.getPath().size(), 
                (double) result.getExecutionTime()
            ));

            
        } else {
             resultLabel.setText(String.format(
                "Algorithm: %s\nPath NOT Found\nTime: %.2f ms",
                result.getAlgorithmUsed(), 
                (double) result.getExecutionTime()
            ));
        }
    }

    /**
     * The main entry point to launch the JavaFX application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}