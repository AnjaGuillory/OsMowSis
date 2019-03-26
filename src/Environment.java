
public class Environment {
	
	public int width, height;
	protected Square[][] grid;
	

	public Environment(int width_, int height_) {
		width = width_;
		height = height_;
		grid = new Square[width][height];
	}	
	
	public Environment(int width_, int height_, Integer[] cratorsX, Integer[] cratorsY) {
		width = width_;
		height = height_;
		grid = new Square[width][height];
	}	
	
	public Environment(int width_, int height_, Square[][] grid_) {
		width = width_;
		height = height_;
		grid = grid_;
	}
	
	public void SetAll(Square val) {
		for (int h = height - 1; h >= 0; h--) {
			for (int w = width - 1; w >= 0; w--) {
				grid[w][h] = val;
			}
		}		
	}
	
	public int CountOccurances(Square val) {
		int count = 0;
		for (int h = height - 1; h >= 0; h--) {
			for (int w = width - 1; w >= 0; w--) {
				if (grid[w][h] == val)
					count++;
			}
		}
		return count;		
	}
	
	public int CountNearby(int x, int y, Square val) {
		int count = 0;
		for (int h = y - 1; h <= y+1; h++) {
			for (int w = x - 1; w <= x + 1; w++) {
				if (SafeQuery(w,h) == val)
					count++;
			}
		}
		return count;		
	}
	
	public Square GetIJ(int i, int j) {
		return grid[i][j];
	}
	
	public boolean IsMoveValid(int x, int y, int nSteps, Direction direction) {
		
		int dX = Utils.GetDirectionDeltaX(direction);
		int dY = Utils.GetDirectionDeltaY(direction);
		for (int i = 0; i <= nSteps; i++ ) {
			int newX = x+dX*i;
			int newY = y+dY*i;
			if (newX <0 || newX>=width || newY <0 || newY>=height)
				return false;
			if (grid[newX][newY]==Square.OBSTACLE || grid[newX][newY]==Square.FENCE)
				return false;
		}		
		return true;		
	}
	
	public void ApplyMove(int x, int y, int nSteps, Direction direction) {
		
		if (IsMoveValid(x, y, nSteps, direction) == false)
			System.out.println("TRYING TO APPLY AN INVALID MOVE");
		
		int dX = Utils.GetDirectionDeltaX(direction);
		int dY = Utils.GetDirectionDeltaY(direction);
		for (int i = 0; i <= nSteps; i++ ) {
			int newX = x+dX*i;
			int newY = y+dY*i;
			if (newX <0 || newX>=width || newY <0 || newY>=height)
				break;
			if (grid[newX][newY] == Square.UNCUT)
				grid[newX][newY] = Square.CUT;
		}				
	}
	
	
	public Square SafeQuery(int x, int y) {
		if (x <0 || x>=width || y <0 || y>=height)
			return Square.FENCE;
		else 
			return grid[x][y];
	}

	public void SafeSet(int x, int y, Square square) {
		if (x <0 || x>=width || y <0 || y>=height)
			System.out.println("SAFE SET GOT ERROR");
		else
			 grid[x][y] = square;
	}
	
	public Square[] GetScan(int x, int y) {
		Square[] scan = new Square[8];
		scan[0] = SafeQuery(x+0, y+1);
		scan[1] = SafeQuery(x+1, y+1);
		scan[2] = SafeQuery(x+1, y+0);
		scan[3] = SafeQuery(x+1, y-1);
		scan[4] = SafeQuery(x+0, y-1);
		scan[5] = SafeQuery(x-1, y-1);
		scan[6] = SafeQuery(x-1, y+0);
		scan[7] = SafeQuery(x-1, y+1);
		return scan;
	}
	
	

    public void Render(int[] mowerX, int[] mowerY) {
    	
        int i, j;
        int charWidth = 2 * width + 2;

        // display the rows of the lawn from top to bottom
        for (j = height - 1; j >= 0; j--) {
            renderHorizontalBar(charWidth);

            // display the Y-direction identifier
            System.out.print(j);

            // display the contents of each square on this row
            for (i = 0; i < width; i++) {
                System.out.print("|");
                
                boolean mowerOnSquare = false;
                for (int s = 0; s < mowerX.length; s++)
                	if (i == mowerX[s] & j == mowerY[s])
                	{
                		mowerOnSquare = true;
                		break;
                	}


                // the mower overrides all other contents
                if (mowerOnSquare) {
                    System.out.print("M");
                } else {
                    switch (GetIJ(i,j)) {
                        case NOTHING:
                        case CUT:
                            System.out.print(" ");
                            break;
                        case UNCUT:
                            System.out.print("g");
                            break;
                        case OBSTACLE:
                            System.out.print("c");
                            break;
                        case UNKNOWN:
                            System.out.print("?");
                            break;
                        case FENCE:
                            System.out.print("F");
                            break;
                        default:
                            break;
                    }
                }
            }
            System.out.println("|");
        }
        renderHorizontalBar(charWidth);

        // display the column X-direction identifiers
        System.out.print(" ");
        for (i = 0; i < width; i++) {
            System.out.print(" " + i);
        }
        System.out.println("");

//        // display the mower's direction
//        System.out.println("dir: " + Utils.DirectionToString(states[0].direction));
//        System.out.println("");
    }
    
  private void renderHorizontalBar(int size) {
  System.out.print(" ");
  for (int k = 0; k < size; k++) {
      System.out.print("-");
  }
  System.out.println("");
}


}
