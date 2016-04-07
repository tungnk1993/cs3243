public class Config {
    static boolean DEBUG = true;
    
	static final String outputFile = "test.txt";
	static int NUM_OF_FEATURES = 6;
    static String[] features = {
                                    "countHoles",
                                    "rowTrans",
                                    "colTrans",
                                    "wellSums",
                                    "clearedRows",
                                    "landingHeight+pieceHeight/2"
                                };

	static int POPULATION = 250;
	static int GAMES = 5;
	
    static int VALUE_RANGE = 10;
    static int THREADS = 3;
    
	static double crossoverRate = 0.6;
    static double mutationRate = 0.05;

    static int tournamentSize = 25;
    static double tournamentChance = 0.8;
    static boolean elitism = true;
    static double eliteFavor = 0.7;

    static int TEST_THRESHOLD = 100000;
    static double LEADER_CUTOFF = 0.85;
    static double SURVIVE_THRESHOLD = 0.95;
}