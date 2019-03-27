import java.util.ArrayList;

public class Navigator extends Environment {
	
	public int x,y;
	
	public Navigator() {
		super(3,3);

		x = 1;
		y = 1;
		SetAll(Square.UNKNOWN);
		SafeSet(x,y, Square.CUT);
		
	}
	
	// grow this lawn by one unit to the west. shoulda have used arraylists
	private void ExpandWest() {
		Square[][] gridNew = new Square[width+1][height];
		for (int i = 0; i < height; i++)
			gridNew[0][i] = Square.UNKNOWN;
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				gridNew[w+1][h] = grid[w][h];
			}
		}	
		grid = gridNew;
		width++;	
		x++;		
	}
	
	// grow this lawn by one unit to the east. shoulda have used arraylists
	private void ExpandEast() {
		Square[][] gridNew = new Square[width+1][height];
		for (int i = 0; i < height; i++)
			gridNew[width][i] = Square.UNKNOWN;
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				gridNew[w][h] = grid[w][h];
			}
		}	
		grid = gridNew;
		width++;	
	}
	
	// grow this lawn by one unit to the south. shoulda have used arraylists
	private void ExpandSouth() {
		Square[][] gridNew = new Square[width][height+1];
		for (int i = 0; i < width; i++)
			gridNew[i][0] = Square.UNKNOWN;
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				gridNew[w][h+1] = grid[w][h];
			}
		}	
		grid = gridNew;
		height++;
		y++;
	}
	
	// grow this lawn by one unit to the north. shoulda have used arraylists
	private void ExpandNorth() {
		Square[][] gridNew = new Square[width][height+1];
		for (int i = 0; i < width; i++)
			gridNew[i][height] = Square.UNKNOWN;
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				gridNew[w][h] = grid[w][h];
			}
		}	
		grid = gridNew;
		height++;
	}
	
	// check if one square on the edge of the lawn is known or not a fence, crator, etc
	// if so, expand the lawn on that edge
	private void PadBorder() {
		for (int i = 0; i < height; i++) {
			Square square = SafeQuery(0, i);
			if (square != Square.OBSTACLE && square != Square.UNKNOWN && square != Square.FENCE) {
				ExpandWest();
				break;
			}
		}
		
		for (int i = 0; i < width; i++) {
			Square square = SafeQuery(i, 0);
			if (square != Square.OBSTACLE && square != Square.UNKNOWN && square != Square.FENCE) {
				ExpandSouth();
				break;
			}
		}
		
		for (int i = 0; i < height; i++) {
			Square square = SafeQuery(width-1, i);
			if (square != Square.OBSTACLE && square != Square.UNKNOWN && square != Square.FENCE) {
				ExpandEast();
				break;
			}
		}
		
		for (int i = 0; i < width; i++) {
			Square square = SafeQuery(i, height-1);
			if (square != Square.OBSTACLE && square != Square.UNKNOWN && square != Square.FENCE) {
				ExpandNorth();
				break;
			}
		}
	}
	
	// ingest a scan. if we are at the edge of the lawn, 
	// expand the lawn to hold the new data
	// then ingest the data
	public void IngestScan(Square[] scan) {
		
		
		if (x==0)
			ExpandWest();
		
		if (y==0)
			ExpandSouth();			

		if (x==width-1)
			ExpandEast();
		
		if (y==height-1)
			ExpandNorth();
		
		PadBorder();
				
		// at this point, we should be good to freely ingest the scan

		SafeSet(x+0,y+1, scan[0]);
		SafeSet(x+1,y+1, scan[1]);
		SafeSet(x+1,y+0, scan[2]);
		SafeSet(x+1,y-1, scan[3]);
		SafeSet(x+0,y-1, scan[4]);
		SafeSet(x-1,y-1, scan[5]);
		SafeSet(x-1,y+0, scan[6]);
		SafeSet(x-1,y+1, scan[7]);
		

		PadBorder();
	}
	
	private boolean IsInList(Square[] squares, Square query) {
		for (int i = 0; i < squares.length; i++)
			if (squares[i] == query)
				return true;
		return false;
	}

	// this is keith's logic here.
	// its pretty much a breadth first search to the nearst square, 
	// with a priority in a given direction
	// check the doc uploaded to slack for this logic. im sure itll change
	public ArrayList<MyPair> GetNextMoveToFind(Square[] dest, Direction preferedDir) {
		MyPair current = new MyPair(x, y);
		ArrayList<MyPair> ret = new ArrayList<MyPair>();
		ArrayList<MyPair> positions = new ArrayList<MyPair>();
		ArrayList<ArrayList<MyPair>> paths = new ArrayList<ArrayList<MyPair>>();
		boolean foundPath = false;

		for (int x_ = 0; x_ < width; x_++) {
			for (int y_ = 0; y_ < height; y_++) {
				if (x==x_ && y==y_)
					continue;
				Square square = SafeQuery(x_, y_);
				if (square != Square.OBSTACLE && square != Square.FENCE)
					positions.add(new MyPair(x_, y_));
			}
		}
		
		// get all points neighboring current x,y
		for (int p = positions.size()-1; p >=0 && !foundPath ; p--) {
			if (current.IsNeighbor(positions.get(p)) ) {
				ArrayList<MyPair> path = new ArrayList<MyPair>();
				path.add(current);
				path.add(positions.get(p));
				//Square square = SafeQuery(positions.get(p).x, positions.get(p).y);
				positions.remove(p);
				paths.add(path);
			}
		}
		
		// check that one of those points is in our direction of travel. 
		// if so, make it appear first. we will favor it.
		for (int p = 0; p < paths.size(); p++) {
			if (Utils.IsNeighborInDirection(current, paths.get(p).get(1),  preferedDir)) {
				ArrayList<MyPair> temp = paths.get(0);
				paths.set(0,  paths.get(p));
				paths.set(p, temp);
				break;
			}
		}
		
		// check if we have already found a suitable path that is in our direction of travel
		for (int p = 0; p < paths.size(); p++) {
			ArrayList<MyPair> currentPath = paths.get(p);
			MyPair latestPoint = currentPath.get(currentPath.size() - 1);
			Square square = SafeQuery(latestPoint.x, latestPoint.y);
			if (IsInList(dest, square)) {
				foundPath = true;
				ret = currentPath;
				break;
			}
		
		}
		

		while (positions.size()>0 && !foundPath) {
			boolean removedPoint = false;

			ArrayList<ArrayList<MyPair>> fresh = new ArrayList<ArrayList<MyPair>>();

			for (int i = 0; i < paths.size()&& !foundPath; i++) {
				ArrayList<MyPair> currentPath = paths.get(i);
				MyPair latestPoint = currentPath.get(currentPath.size() - 1);
				
				// find a point that is in the direction of the current path's movement.
				for (int p = 0; p < positions.size(); p++) {
					Direction instantDir = currentPath.get(currentPath.size() - 2).GetDirectionTorwards(latestPoint);
					if (Utils.IsNeighborInDirection(latestPoint, positions.get(p),  instantDir)) {
						MyPair temp = positions.get(positions.size()-1);
						positions.set(positions.size()-1,  positions.get(p));
						positions.set(p, temp);
						break;
					}
				}
				

				// get all neighbor points of last point in the path
				for (int p = positions.size() - 1; p >= 0 && !foundPath; p--) {
					if (latestPoint.IsNeighbor(positions.get(p)) ) {
						ArrayList<MyPair> pathCopy = new ArrayList<MyPair>(currentPath);
						pathCopy.add(positions.get(p));
						Square square = SafeQuery(positions.get(p).x, positions.get(p).y);
						positions.remove(p);
						fresh.add(pathCopy);
						removedPoint = true;
						if (IsInList(dest, square)) {
							ret = pathCopy;
							foundPath = true;
							break;
						}
							
							
					}
				}
			}

			paths = fresh;
			if (!removedPoint)
				break;
		
		} // end while
		return ret;
		
	} // end function
	
	
	
	
	
}
