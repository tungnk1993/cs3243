public class Config {
    static boolean DEBUG = true;

	static final String outputFile = "test.txt";
	static int NUM_OF_FEATURES = 8;
	static int NUMBER_OF_PARTICLES = 63;
	static int GAMES = 5;
	static boolean USE_REPULSIVE_ALGO = false;
	static double MAX_POSITION = 10.0;
    static double MIN_POSITION = -10.0;
    
    static int THREADS = 5;
    static int STOP_THRESHOLD = 100000;

    static String[] features = {
                                    "countHoles",
                                    "sumCol",
                                    "sumColDiff",
                                    "rowTrans",
                                    "colTrans",
                                    "holeDepth",
                                    "rowWithHole",
                                    "clearedRows"
                                };

    static double INERTIA = 0.6571;
    static double PARTICLE_INCREMENT = 1.6319;
    static double GLOBAL_INCREMENT = 0.6239;
    
}