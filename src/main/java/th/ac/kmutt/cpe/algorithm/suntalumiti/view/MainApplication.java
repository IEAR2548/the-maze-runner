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

public class MainApplication extends Application {

    private Maze maze;
    private final MazePanel mazePanel = new MazePanel();
    private final Map<String, ISolver> solvers = new HashMap<>();
    private final Label resultLabel = new Label("Result: No maze loaded.");
    private final ComboBox<String> solverComboBox = new ComboBox<>();

    public MainApplication() {
        solvers.put(new AStarSolver().getName(), new AStarSolver());
        solvers.put(new DijkstraSolver().getName(), new DijkstraSolver());
        solvers.put(new GeneticSolver().getName(), new GeneticSolver());
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Maze Runner: Minimum Time Cost Pathfinding");

        Button loadButton = new Button("Load Maze File");
        loadButton.setMaxWidth(Double.MAX_VALUE);
        loadButton.setOnAction(e -> loadMazeFromFile(stage));

        solverComboBox.getItems().addAll(solvers.keySet());
        solverComboBox.setValue(new AStarSolver().getName()); // Default selection
        solverComboBox.setMaxWidth(Double.MAX_VALUE);
        
        Label comboLabel = new Label("Select Algorithm:");
        
        Button runButton = new Button("Find Path");
        runButton.setMaxWidth(Double.MAX_VALUE);
        runButton.setOnAction(e -> runSolver());

        resultLabel.setId("result-label");
        resultLabel.setWrapText(true);
        resultLabel.setStyle("-fx-border-color: #ccc;");
        
        VBox controlBox = new VBox(10, loadButton, new Separator(), comboLabel, solverComboBox, runButton, new Separator(), resultLabel);
        controlBox.setPadding(new Insets(15));
        controlBox.setPrefWidth(250);
        controlBox.setAlignment(Pos.TOP_CENTER);
        
        ScrollPane scrollPane = new ScrollPane(mazePanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: #f4f4f4;");

        BorderPane root = new BorderPane();
        root.setLeft(controlBox);
        root.setCenter(scrollPane);

        mazePanel.draw(); 
        
        Scene scene = new Scene(root, 1000, 700);

        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private void loadMazeFromFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Maze Data File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
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

        PathResult result = solver.solve(maze);
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

    public static void main(String[] args) {
        launch(args);
    }
}