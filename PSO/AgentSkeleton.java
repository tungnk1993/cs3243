import java.util.Arrays;


public class AgentSkeleton implements Runnable{
	
	double featureVector[] = new double[Config.NUM_OF_FEATURES];
    double weightVector[] = new double[Config.NUM_OF_FEATURES];
	int totalCleared = 0;
    /*
	 * FakeSateClass is a class which is similar to the State class. The reason to use such a class is 
	 * to simulate the make move function and get the field from the State.
	 */
	
    public static class FakeState {
    	public static final int COLS = 10;
    	public static final int ROWS = 21;
    	public static final int N_PIECES = 7;

    	//all legal moves - first index is piece type - then a list of 2-length arrays
    	protected static int[][][] legalMoves = new int[N_PIECES][][];
    	
    	//indices for legalMoves
    	public static final int ORIENT = 0;
    	public static final int SLOT = 1;
    	
    	//possible orientations for a given piece type
    	protected static int[] pOrients = {1,2,4,4,4,2,2};
    	
    	//the next several arrays define the piece vocabulary in detail
    	//width of the pieces [piece ID][orientation]
    	protected static int[][] pWidth = {
    			{2},
    			{1,4},
    			{2,3,2,3},
    			{2,3,2,3},
    			{2,3,2,3},
    			{3,2},
    			{3,2}
    	};
    	//height of the pieces [piece ID][orientation]
    	private static int[][] pHeight = {
    			{2},
    			{4,1},
    			{3,2,3,2},
    			{3,2,3,2},
    			{3,2,3,2},
    			{2,3},
    			{2,3}
    	};
    	private static int[][][] pBottom = {
    		{{0,0}},
    		{{0},{0,0,0,0}},
    		{{0,0},{0,1,1},{2,0},{0,0,0}},
    		{{0,0},{0,0,0},{0,2},{1,1,0}},
    		{{0,1},{1,0,1},{1,0},{0,0,0}},
    		{{0,0,1},{1,0}},
    		{{1,0,0},{0,1}}
    	};
    	private static int[][][] pTop = {
    		{{2,2}},
    		{{4},{1,1,1,1}},
    		{{3,1},{2,2,2},{3,3},{1,1,2}},
    		{{1,3},{2,1,1},{3,3},{2,2,2}},
    		{{3,2},{2,2,2},{2,3},{1,2,1}},
    		{{1,2,2},{3,2}},
    		{{2,2,1},{2,3}}
    	};

        {
        //for each piece type
            for(int i = 0; i < N_PIECES; i++) {
                //figure number of legal moves
                int n = 0;
                for(int j = 0; j < pOrients[i]; j++) {
                    //number of locations in this orientation
                    n += COLS+1-pWidth[i][j];
                }
                //allocate space
                legalMoves[i] = new int[n][2];
                //for each orientation
                n = 0;
                for(int j = 0; j < pOrients[i]; j++) {
                    //for each slot
                    for(int k = 0; k < COLS+1-pWidth[i][j];k++) {
                        legalMoves[i][n][ORIENT] = j;
                        legalMoves[i][n][SLOT] = k;
                        n++;
                    }
                }
            }
        
        }

    	private int[][] field = new int[ROWS][COLS];
    	private int[] top = new int[COLS];
    	private int nextPiece;
    	private boolean lost;
    	private int turn;
    	private int clearedRowsLastTime; //The number of rows cleared by the last move.

        private int landingHeight; // height of column of piece
        private int pieceHeight; // height of last piece

        public FakeState(int nextPiece, int turn, int field[][], int top[]) {
            copyField(field);
            this.top = top.clone();
            this.nextPiece = nextPiece;
            this.turn = turn;
        }
        private void copyField(int[][] field) {
        	for (int i = 0 ; i< ROWS; i++){
        		for (int j = 0; j< COLS; j++){
        			this.field[i][j] = field[i][j];
        		}
        	}
        }
        // returns false if you lose - true otherwise
        public boolean makeMove(int orient, int slot) {
        	int piece = nextPiece;
            // height if the first column makes contact
            int height = top[slot] - pBottom[piece][orient][0];
            // for each column beyond the first in the piece
            for (int c = 1; c < pWidth[piece][orient]; c++) {
                height = Math.max(height, top[slot + c] - pBottom[piece][orient][c]);
            }

            landingHeight = height;
            pieceHeight = pHeight[piece][orient];

            // check if game ended
            if (height + pHeight[piece][orient] >= ROWS) {
            	lost = true;
                return false;
            }

            // for each column in the piece - fill in the appropriate blocks
            for (int i = 0; i < pWidth[piece][orient]; i++) {

                // from bottom to top of brick
                for (int h = height + pBottom[piece][orient][i]; h < height + pTop[piece][orient][i]; h++) {
                    field[h][i + slot] = turn;
                }
            }

            // adjust top
            for (int c = 0; c < pWidth[piece][orient]; c++) {
                top[slot + c] = height + pTop[piece][orient][c];
            }

            int rowsCleared = 0;

            // check for full rows - starting at the top
            for (int r = height + pHeight[piece][orient] - 1; r >= height; r--) {
                // check all columns in the row
                boolean full = true;
                for (int c = 0; c < COLS; c++) {
                    if (field[r][c] == 0) {
                        full = false;
                        break;
                    }
                }
                // if the row was full - remove it and slide above stuff down
                if (full) {
                    rowsCleared++;
                    // for each column
                    for (int c = 0; c < COLS; c++) {

                        // slide down all bricks
                        for (int i = r; i < top[c]; i++) {
                            field[i][c] = field[i + 1][c];
                        }
                        // lower the top
                        top[c]--;
                        while (top[c] >= 1 && field[top[c] - 1][c] == 0)
                            top[c]--;
                    }
                }
            }
            clearedRowsLastTime = rowsCleared;
            lost = false;
            return true;
        }
        
        public int[][] getlegalMoves(int pieceNumber) {
            return legalMoves[pieceNumber];
        }

    	public int[][] getField() {
    		return field;
    	}

        public int[] getTop() {
            return top;
        }

        public boolean getLost() {
            return lost;
        }

        public int getTurnNumber() {
            return turn;
        }

    	public int getClearedRows(){
    		return clearedRowsLastTime;
    	}
    	
    	public int getColumnHeight(int columnID){
    		return top[columnID];
    	}

        public int getLandingHeight() {
            return landingHeight;
        }

        public int getPieceHeight() {
            return pieceHeight;
        }
    	
        // evaluators
        public int getHolesSimple() {
            int countHole = 0;

            for (int j = 0; j < State.COLS; j++) {
                for (int i = 0; i < top[j] - 1; i++)
                    if ((field[i][j] == 0) && (field[i+1][j] != 0))
                        countHole++;
            }
            return countHole;
        }
    	public int getHoles(){
            int countHole = 0;

            for (int i = 0; i < State.COLS; i++) {
                if (top[i] > 0) {
                    int lastHole = 0;
                    if (field[0][i] == 0) lastHole = 1;

                    for (int j = 1; j < top[i]; j++) {
                        if (field[j][i] == 0) lastHole++;
                        else {
                            countHole += lastHole;
                            lastHole = 0;
                        }
                    }
                }
            }
            return countHole;
        }

        public int getSumColumnHeight() {
            int s = 0;
            for (int i = 0; i < State.COLS; i++) s += top[i];
            return s;
        }

        public int getSumDiffColumnHeight() {
            int s = 0;
            for (int i = 0; i < State.COLS - 1; i++) s += Math.abs(top[i] - top[i+1]);
            return s;
        }

        public int getRowTransition() {
            // count number of filled cells adjacent to empty cells
            int s = 0;
            int maxTop = 0;
            for (int i = 0; i < State.COLS; i++) 
                if (top[i] > maxTop) maxTop = top[i];

            for (int i = 0; i < maxTop; i++)
            {
                int lastBlock = 1;
                for (int j = 0; j < State.COLS; j++) {
                    if ((field[i][j] != lastBlock) && (field[i][j] * lastBlock == 0)) s++;
                    if (field[i][j] != 0) lastBlock = 1;
                    else lastBlock = 0;
                }
                if (lastBlock == 0) s++;
            }
            return s;
        }

        public int getColumnTransition() {
            int s = 0;
            for (int c = 0; c < State.COLS; c++) {
                int lastBlock = 1;
                for (int r = 0; r < top[c]; r++) {
                    if ((field[r][c] != lastBlock) && (field[r][c] * lastBlock == 0)) s++;
                    if (field[r][c] != 0) lastBlock = 1;
                    else lastBlock = 0;
                }
            }
            return s;
        }

        public int getHoleDepth() {
            int s = 0;
            for (int c = 0; c < State.COLS; c++) {
                int currentFilled = 0;
                for (int r = top[c] - 1; r >= 0; r--) {
                    if (field[r][c] != 0) currentFilled++;
                    else {
                        s += currentFilled;
                    }
                }
            }
            return s;
        }

        public int getRowWithHole() {
            int s = 0;
            for (int r = 0; r < State.ROWS; r++) {
                boolean hasBlank = false;
                boolean hasFilled = false;
                for (int c = 0; c < State.COLS; c++) {
                    if (field[r][c] == 0) hasBlank = true;
                    else hasFilled = true;
                    if (hasFilled && hasBlank) break;
                }
                    
                if (hasFilled && hasBlank) s++;
            }
            return s;
        }

        public int getWellSums() {
            int s = 0;

            // left
            for (int i = top[0] - 1; i >= 0; i--) {
                if ((field[i][0] == 0) && (field[i][1] != 0)) {
                    s++;
                    for (int k = i - 1; k >= 0; k--) {
                        if (field[k][0] == 0) s++;
                        else break;
                    }
                }
            }
            
            // middle
            for (int j = 1; j < State.COLS - 1; j++) {
                for (int i = top[j] - 1; i >= 0; i--) 
                {
                    if ((field[i][j] == 0) && (field[i][j-1] != 0) 
                        && (field[i][j+1] != 0)) 
                    {
                        s++;
                        for (int k = i - 1; k >= 0; k--) {
                            if (field[k][j] == 0) s++;
                            else break;
                        }
                    }
                }
            }

            // right
            for (int i = top[State.COLS - 1] - 1; i >= 0; i--) {
                if ((field[i][State.COLS - 1] == 0) && (field[i][State.COLS - 2] != 0)) {
                    s++;
                    for (int k = i - 1; k >= 0; k--) {
                        if (field[k][State.COLS - 1] == 0) s++;
                        else break;
                    }
                }
            }
            return s;
        }

    }
	
    /*
     * Constructor: Use a set of weightVector to initialize the weightVector
     */
    public AgentSkeleton(double[] _weightVector){
    	for (int i = 0 ; i < Config.NUM_OF_FEATURES; i++ ){
    		this.weightVector[i] = _weightVector[i];
    	}
    }

    public AgentSkeleton(){
    	//Do nothing is no parameters provided.
    }
    
    public void setWeight(double[] _weightVector) {
        for (int i = 0 ; i < Config.NUM_OF_FEATURES; i++ ){
            this.weightVector[i] = _weightVector[i];
        }   
    }

	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
		int numOfChoice = legalMoves.length;
		double maxValue = 0;
		int currentChoice = -1;//-1 means have not choosed
		for (int i = 0; i < numOfChoice; i++){
			FakeState tmpState = new FakeState(s.getNextPiece(),s.getTurnNumber(),s.getField(),s.getTop());
			tmpState.makeMove(legalMoves[i][FakeState.ORIENT], legalMoves[i][FakeState.SLOT]);
			double tmpValue = evaluateState(tmpState);
			if (tmpValue > maxValue || currentChoice == -1){
				currentChoice = i;
				maxValue = tmpValue;
			}
		}
		return currentChoice;
	}

    public int pickMoveLookAhead(State s, int[][] legalMoves) {
        int numOfChoice = legalMoves.length;
        double maxValue = 0;
        int currentChoice = -1;//-1 means have not choosed

        for (int i = 0; i < numOfChoice; i++){
            FakeState tmpState = new FakeState(s.getNextPiece(),s.getTurnNumber(),s.getField(),s.getTop());
            
            boolean alreadyLost = tmpState.makeMove(legalMoves[i][FakeState.ORIENT], legalMoves[i][FakeState.SLOT]);

            double sumFitness = 0.0;
            // for each piece to try for this tmpState, construct new second state with this piece
            // get legal moves, pick best out of all Legal moves
            // then sumFitness up, divide by N_PIECES.length
            for (int j = 0; j < State.N_PIECES; j++) {
                if (!alreadyLost) {
                    int[][] secondLegalMoves = tmpState.getlegalMoves(j);
                    double maxSecondValue = 0.0;
                    int secondChoice = -1;
                    for (int k = 0; k < secondLegalMoves.length; k++) {
                        FakeState secondState = new FakeState(j, tmpState.getTurnNumber(), tmpState.getField(), tmpState.getTop());
                        secondState.makeMove(secondLegalMoves[k][FakeState.ORIENT], secondLegalMoves[k][FakeState.SLOT]);
                        double secondTmpValue = evaluateState(secondState);
                        if (secondTmpValue > maxSecondValue || secondChoice == -1) {
                            secondChoice = k;
                            maxSecondValue = secondTmpValue;
                        }
                    }
                    /*
                    if (j == 0) sumFitness = maxSecondValue;
                    else if (maxSecondValue < sumFitness) sumFitness = maxSecondValue;
                    */
                    sumFitness += maxSecondValue;
                }
            }

            sumFitness = sumFitness / State.N_PIECES;
            double tmpValue = evaluateState(tmpState);
            //if (tmpValue < sumFitness) sumFitness = tmpValue;
            sumFitness += tmpValue;
            //sumFitness = tmpValue;
            //sumFitness /= 2;
            if (sumFitness > maxValue || currentChoice == -1){
                currentChoice = i;
                maxValue = sumFitness;
            }
            /*
            double tmpValue = evaluateState(tmpState);
            if (tmpValue > maxValue || currentChoice == -1){
                currentChoice = i;
                maxValue = tmpValue;
            }
            */
        }
        return currentChoice;
    }
	
	/*
	 * The Heuristics function
	 * Get all the corresponding feature values based on the state.
	 */
	private double evaluateState(FakeState tmpState) {
        featureVector[0] = tmpState.getHolesSimple();
		//featureVector[0] = tmpState.getHoles();
        featureVector[1] = tmpState.getRowTransition();
        featureVector[2] = tmpState.getColumnTransition();
        featureVector[3] = tmpState.getWellSums();
        featureVector[4] = tmpState.getClearedRows();
        featureVector[5] = tmpState.getLandingHeight();
        featureVector[6] = tmpState.getHoleDepth();
        featureVector[7] = tmpState.getLost() ? -1000 : 1000;
        //featureVector[1] = tmpState.getSumColumnHeight();
        //featureVector[2] = tmpState.getSumDiffColumnHeight();
        //featureVector[6] = tmpState.getRowWithHole();

		double finalScore = 0;
		for (int i = 0; i < Config.NUM_OF_FEATURES; i++){
			finalScore += weightVector[i] * featureVector[i];
		}
		return finalScore;
	}

    public int getSimulationFitness() {
        return totalCleared;
    }

    @Override
    public void run() {
        State s = new State();
        while(!s.hasLost()) {
            s.makeMove(this.pickMove(s,s.legalMoves()));
        }
        totalCleared = s.getRowsCleared();
    }
    
	public int runSimulation(){
		State s = new State();
		while(!s.hasLost()) {
			s.makeMove(this.pickMove(s,s.legalMoves()));
		}
        totalCleared = s.getRowsCleared();
		return s.getRowsCleared();
	}
	
	
}