package th.ac.kmutt.cpe.algorithm.suntalumiti.algo.ga;

import java.util.Random;

/**
 * Represents a single gene (a move direction: Up, Down, Left, Right).
 */
public enum Gene {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    private final int dr; // Change in row
    private final int dc; // Change in column
    private static final Random RANDOM = new Random();

    Gene(int dr, int dc) {
        this.dr = dr;
        this.dc = dc;
    }

    public int getDr() { return dr; }
    public int getDc() { return dc; }

    /**
     * Returns a random movement gene.
     */
    public static Gene getRandomGene() {
        return Gene.values()[RANDOM.nextInt(Gene.values().length)];
    }
}