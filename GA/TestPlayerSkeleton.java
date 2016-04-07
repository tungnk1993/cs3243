import java.util.Arrays;


public class TestPlayerSkeleton {

    
	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
	
		return (int)(Math.random() * (legalMoves.length));
	}
	
	
	
	public static int getHoles(int[] top, int[][] field){
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

	public static int getSumColumnHeight(int[] top) {
		int s = 0;
		for (int i = 0; i < State.COLS; i++) s += top[i];
		return s;
	}

	public static int getSumDiffColumnHeight(int[] top) {
		int s = 0;
		for (int i = 0; i < State.COLS - 1; i++) s += Math.abs(top[i] - top[i+1]);
		return s;
	}

	public static int getRowTransition(int[] top, int[][] field) {
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

	public static int getColumnTransition(int[] top, int[][] field) {
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

	public static int getHoleDepth(int[] top, int[][] field) {
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

	public static int getRowWithHole(int[] top, int[][] field) {
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

	public static int getWellSums(int[] top, int[][] field) {
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
		System.out.println("Well sums after 0 = " + s);
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
			System.out.println("Well sums after " + j + " = " + s);
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
		System.out.println("Well sums final = " + s);
		return s;
	}

	public static void main(String[] args) {
		State s = new State();
		new TFrame(s);
		TestPlayerSkeleton p = new TestPlayerSkeleton();
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);

            int[] t = s.getTop();
            int[][] f = s.getField();
            
            for (int c = 0; c < State.COLS; c++)
            {
            	for (int r = 0; r < State.ROWS; r++)
            		System.out.print(f[r][c] + " ");
            	System.out.println();
            }    	

            System.out.println("----");
            System.out.println("Holes = " + getHoles(t, f));
            System.out.println("Sum col = " + getSumColumnHeight(t));
            System.out.println("Sum diff = " + getSumDiffColumnHeight(t));
            System.out.println("Row trans = " + getRowTransition(t,f));
            System.out.println("Col trans = " + getColumnTransition(t,f));
            System.out.println("Hole depth = " + getHoleDepth(t,f));
            System.out.println("Row w/ hole = " + getRowWithHole(t,f));
            System.out.println("Well sums = " + getWellSums(t, f));
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
	
}