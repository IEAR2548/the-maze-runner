package th.ac.kmutt.cpe.algorithm.suntalumiti.algo.ga;

import java.util.Random;

public enum Gene {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);
    private final int dr;
    private final int dc;
    private static final Random RANDOM = new Random();

    Gene(int dr, int dc) {
        this.dr = dr;
        this.dc = dc;
    }

    public int getDr() { return dr; }
    public int getDc() { return dc; }

    public static Gene getRandomGene() {
        return Gene.values()[RANDOM.nextInt(Gene.values().length)];
    }
}