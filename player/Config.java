public class Config {
    static boolean DEBUG = true;

    static double LOST_PENALTY = 10000;
	static final String outputFile = "test.txt";
	static int NUM_OF_FEATURES = 7;
    static String[] features = {
                                    "countHolesSimple",
                                    "rowTrans",
                                    "colTrans",
                                    "wellSums",
                                    "clearedRows",
                                    "landingHeight",
                                    "holeDepth",
                                };

	static int NUMBER_OF_PARTICLES = 200;
	static int GAMES = 10;
	static boolean USE_REPULSIVE_ALGO = false;
	static double MAX_POSITION = 10.0;
    static double MIN_POSITION = -10.0;
    
    static int THREADS = 5;
    static int STOP_THRESHOLD = 100000;

    

    //static double INERTIA = 0.6571;
    static double INERTIA = 1.0;
    
    static double PARTICLE_INCREMENT = 0.9;
    //static double PARTICLE_INCREMENT = 1.6319;

    //static double GLOBAL_INCREMENT = 0.6239;
    //static double GLOBAL_INCREMENT = 0.4239;
    static double GLOBAL_INCREMENT = 1.0;
    
}