public class Config {
	static final String outputFile = "test.txt";
	static int NUM_OF_FEATURES = 8;
	static int POPULATION = 100;
	static int GAMES = 3;
	
	static double crossoverRate = 0.7;
    static double mutationRate = 0.20;

    static int tournamentSize = 10;
    static double tournamentChance = 0.8;
    static boolean elitism = true;
    static double eliteFavor = 0.7;

    static int TEST_THRESHOLD = 100000;
    static double LEADER_CUTOFF = 0.85;
    static double SURVIVE_THRESHOLD = 0.95;
}