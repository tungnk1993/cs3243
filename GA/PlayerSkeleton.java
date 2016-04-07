import java.util.Arrays;


public class PlayerSkeleton {
	
	double featureVector[] = new double[Config.NUM_OF_FEATURES];
    double weightVector[] = {
    -9.832139418941225, 
    -5.2999968294859798, 
    -9.897074430597115, 
    1.300957404717439,
     2.6002092703694064,
    -2.4243978262291028,
    };
	
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
            return true;
        }
        
    	public int[][] getField() {
    		return field;
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
                boolean hasHole = false;
                for (int c = 0; c < State.COLS; c++)
                    if (field[r][c] == 0) {
                        hasHole = true;
                        break;
                    }
                if (hasHole) s++;
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

    public PlayerSkeleton(){
    	//Do nothing is no parameters provided.
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
	
	/*
	 * The Heuristics function
	 * Get all the corresponding feature values based on the state.
	 */
	private double evaluateState(FakeState tmpState) {
		featureVector[0] = tmpState.getHoles();
        featureVector[1] = tmpState.getRowTransition();
        featureVector[2] = tmpState.getColumnTransition();
        featureVector[3] = tmpState.getWellSums();
        featureVector[4] = tmpState.getClearedRows();
        featureVector[5] = tmpState.getLandingHeight() + (tmpState.getPieceHeight() - 1)/2;

        //featureVector[3] = tmpState.getHoleDepth();
        //featureVector[1] = tmpState.getSumColumnHeight();
        //featureVector[2] = tmpState.getSumDiffColumnHeight();
        //featureVector[6] = tmpState.getRowWithHole();

		double finalScore = 0;
		for (int i = 0; i < Config.NUM_OF_FEATURES; i++){
			finalScore += weightVector[i] * featureVector[i];
		}
		return finalScore;
	}

	public static void main(String[] args) {
        int averageTotal = 0;
        int maxTotal = 0;
        for (int i = 0; i < 100; i++) {
            State s = new State();
            //new TFrame(s);
            PlayerSkeleton p = new PlayerSkeleton();
            while(!s.hasLost()) {
                s.makeMove(p.pickMove(s,s.legalMoves()));
                //s.draw();
                //s.drawNext(0,0);
                /*
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
            }
            System.out.println("You have completed "+s.getRowsCleared()+" rows.");
            averageTotal += s.getRowsCleared();
        }
        System.out.println(averageTotal / 100.0);
		
	}
}