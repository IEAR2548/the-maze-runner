package th.ac.kmutt.cpe.algorithm.suntalumiti.algo.ga;

import th.ac.kmutt.cpe.algorithm.suntalumiti.ISolver;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.Maze;
import th.ac.kmutt.cpe.algorithm.suntalumiti.model.PathResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticSolver implements ISolver {
    private static final String NAME = "Genetic Algorithm (GA)";
    private static final int POPULATION_SIZE = 200;
    private static final int MAX_GENERATIONS = 3000;
    private static final double MUTATION_RATE = 0.05;
    private static final int GENE_LENGTH = 600;
    private static final int TOURNAMENT_SIZE = 5;
    private static final int ELITISM_COUNT = 5;
    private final Random random = new Random();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public PathResult solve(Maze maze) {
        long startTime = System.currentTimeMillis();
        List<Chromosome> currentPopulation = initializePopulation();
        evaluatePopulation(currentPopulation, maze);        
        Chromosome bestChromosome = currentPopulation.get(0);

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            List<Chromosome> nextPopulation = new ArrayList<>();           
            for (int i = 0; i < ELITISM_COUNT; i++) {
                nextPopulation.add(currentPopulation.get(i));
            }           
            while (nextPopulation.size() < POPULATION_SIZE) {
                Chromosome parent1 = tournamentSelection(currentPopulation);
                Chromosome parent2 = tournamentSelection(currentPopulation);
                Chromosome offspring = parent1.crossover(parent2);
                offspring.mutate(MUTATION_RATE);               
                nextPopulation.add(offspring);
            }
            
            currentPopulation = nextPopulation;
            evaluatePopulation(currentPopulation, maze);
            
            Chromosome generationBest = currentPopulation.get(0);
            if (generationBest.getFitness() > bestChromosome.getFitness()) {
                bestChromosome = generationBest;
            }           
            if (bestChromosome.isGoalReached() && generationBest.getTotalCost() < bestChromosome.getTotalCost()) {
                break; 
            }
        }
        
        long endTime = System.currentTimeMillis();
        
        if (bestChromosome.isGoalReached()) {
            return new PathResult(
                bestChromosome.getPath(), 
                bestChromosome.getTotalCost(), 
                NAME, 
                endTime - startTime
            );
        } else {
            // If the best chromosome did not reach the goal, we return the closest path found
            if (bestChromosome.getPath() != null && !bestChromosome.getPath().isEmpty()) {
                 // Return the path that got closest with the lowest cost, even if it didn't hit G
                 // Note: The totalCost here includes the penalty for not reaching G, which is higher than the actual cost.
                 // We will return the actual cost of the path found, but mark it as failure (high Integer.MAX_VALUE)
                 // to differentiate from optimal classical solutions.
            }
            return createFailureResult(NAME, startTime, endTime);
        }
    }

    private List<Chromosome> initializePopulation() {
        List<Chromosome> population = new ArrayList<>(POPULATION_SIZE);
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new Chromosome(GENE_LENGTH));
        }
        return population;
    }
    
    private void evaluatePopulation(List<Chromosome> population, Maze maze) {
        for (Chromosome chromosome : population) {
            chromosome.evaluate(maze);
        }
        Collections.sort(population); 
    }

    private Chromosome tournamentSelection(List<Chromosome> population) {
        List<Chromosome> tournament = new ArrayList<>(TOURNAMENT_SIZE);
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }
        Collections.sort(tournament);
        return tournament.get(0); 
    }
    
    private PathResult createFailureResult(String algorithmName, long startTime, long endTime) {
        return new PathResult(
            Collections.emptyList(), 
            Integer.MAX_VALUE, 
            algorithmName, 
            endTime - startTime
        );
    }
}