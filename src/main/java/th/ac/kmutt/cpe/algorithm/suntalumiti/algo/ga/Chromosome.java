package th.ac.kmutt.cpe.algorithm.suntalumiti.algo.ga;

import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Cell;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {
    private final List<Gene> genes;
    private double fitness = 0.0;
    private int totalCost = Integer.MAX_VALUE;
    private boolean isGoalReached = false;
    private List<Cell> path;

    private static final Random RANDOM = new Random();
    
    private static final int MAX_PATH_LENGTH = 1000; 
    private static final int COLLISION_PENALTY = 100;
    private static final int WALL_HIT_FIXED_PENALTY = 5000;

    public Chromosome(int geneLength) {
        this.genes = new ArrayList<>(geneLength);
        for (int i = 0; i < geneLength; i++) {
            this.genes.add(Gene.getRandomGene());
        }
    }

    public Chromosome(List<Gene> genes) {
        this.genes = new ArrayList<>(genes);
    }

    public void evaluate(Maze maze) {
        Cell currentCell = maze.getStartCell();
        this.totalCost = 0;
        this.isGoalReached = false;
        this.path = new ArrayList<>();
        path.add(currentCell);

        for (int i = 0; i < genes.size(); i++) {
            Gene move = genes.get(i);
            int nextR = currentCell.getRow() + move.getDr();
            int nextC = currentCell.getCol() + move.getDc();

            if (!maze.isValid(nextR, nextC)) {
                this.totalCost += WALL_HIT_FIXED_PENALTY;
                break; 
            }

            Cell nextCell = maze.getCell(nextR, nextC);
            this.totalCost += nextCell.getTimeCost();
            this.path.add(nextCell);
            currentCell = nextCell;

            if (currentCell.isGoal()) {
                this.isGoalReached = true;
                break; 
            }
            
            if (path.size() >= MAX_PATH_LENGTH) {
                this.totalCost += 10000; 
                break;
            }
        }
        
        if (!isGoalReached) {
            int distance = Math.abs(currentCell.getRow() - maze.getGoalCell().getRow()) + 
                           Math.abs(currentCell.getCol() - maze.getGoalCell().getCol());
            this.totalCost += distance * 50; 
        }

        this.fitness = 1.0 / (this.totalCost + 1e-6); 
    }

    public Chromosome crossover(Chromosome other) {
        int crossoverPoint = RANDOM.nextInt(genes.size());
        List<Gene> newGenes = new ArrayList<>();
        
        for (int i = 0; i < crossoverPoint; i++) {
            newGenes.add(this.genes.get(i));
        }
        
        for (int i = crossoverPoint; i < other.genes.size(); i++) {
            if(newGenes.size() < genes.size()) {
                 newGenes.add(other.genes.get(i));
            }
        }
        
        return new Chromosome(newGenes);
    }

    public void mutate(double mutationRate) {
        for (int i = 0; i < genes.size(); i++) {
            if (RANDOM.nextDouble() < mutationRate) {
                genes.set(i, Gene.getRandomGene());
            }
        }
    }

    public double getFitness() { return fitness; }
    public int getTotalCost() { return totalCost; }
    public List<Cell> getPath() { return path; }
    public boolean isGoalReached() { return isGoalReached; }
    public List<Gene> getGenes() { return genes; }

    @Override
    public int compareTo(Chromosome other) {
        return Double.compare(other.fitness, this.fitness);
    }
}