public class Config {
    static boolean DEBUG = true;

	static final String outputFile = "test.txt";
	static int NUM_OF_FEATURES = 8;
    static String[] features = {
                                    "countHolesSimple",
                                    "rowTrans",
                                    "colTrans",
                                    "wellSums",
                                    "clearedRows",
                                    "landingHeight",
                                    "holeDepth",
                                    "hasLost",
                                };

	static int NUMBER_OF_PARTICLES = 100;
	static int GAMES = 6;
	static boolean USE_REPULSIVE_ALGO = false;
	static double MAX_POSITION = 100.0;
    static double MIN_POSITION = -100.0;
    
    static int THREADS = 5;
    static int STOP_THRESHOLD = 100000;

    

    static double INERTIA = 0.6571;
    //static double INERTIA = 1.0;
    
    //static double PARTICLE_INCREMENT = 0.9;
    static double PARTICLE_INCREMENT = 1.6319;

    static double GLOBAL_INCREMENT = 0.6239;
    //static double GLOBAL_INCREMENT = 0.4239;
    //static double GLOBAL_INCREMENT = 1.0;
    
}