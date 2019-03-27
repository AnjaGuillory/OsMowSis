
// helper class to hold an (x,y) pair
// has a couple convenience functions in it
public class MyPair {

	int x, y;
	public MyPair(int x_, int y_) {
		x = x_;
		y =y_;
	}
	
	// return manhatten distance to another point. 
	// NOTE, i think this function is antiquated. 
	// we never care about manhatten distance since 
	// we can travel diagonally
	public int GetDistance(MyPair p) {
		return Math.abs(x - p.x) + Math.abs(y - p.y);
	}
	
	// return true IFF a square is in the neighboring 8 places
	public boolean IsNeighbor(MyPair p) {
		int dX = Math.abs(x - p.x); 
		int dY = Math.abs(y - p.y);
		if (dX>1 || dY>1)
			return false;
		else
			return true;
	}
	
	// returns the direction from this quare to the given square
	// assumes that the two points are neighbors
	public Direction GetDirectionTorwards(MyPair dest) {
		Direction dir = Direction.NORTH; // default
		
		if (x == dest.x) {
			if (y == dest.y) {
				// NOT GOOD
			}
			else if (y < dest.y) {
				dir = Direction.NORTH;
			}
			else if (y > dest.y) {
				dir = Direction.SOUTH;				
			}			
		}
		else if (x < dest.x) {

			if (y == dest.y) {
				dir = Direction.EAST;				
			}
			else if (y < dest.y) {
				dir = Direction.NORTH_EAST;				
			}
			else if (y > dest.y) {
				dir = Direction.SOUTH_EAST;				
			}			
		}
		else if (x > dest.x) {

			if (y == dest.y) {
				dir = Direction.WEST;
				
			}
			else if (y < dest.y) {
				dir = Direction.NORTH_WEST;				
			}
			else if (y > dest.y) {
				dir = Direction.SOUTH_WEST;				
			}			
		}
		
		return dir;
	}
}
