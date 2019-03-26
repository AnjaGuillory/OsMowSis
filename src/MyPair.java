
public class MyPair {

	int x, y;
	public MyPair(int x_, int y_) {
		x = x_;
		y =y_;
	}
	
	public int GetDistance(MyPair p) {
		return Math.abs(x - p.x) + Math.abs(y - p.y);
	}
	
	public boolean IsNeighbor(MyPair p) {
		int dX = Math.abs(x - p.x); 
		int dY = Math.abs(y - p.y);
//		if (dX==0 && dY==1)
//			return true;
//		else if (dX==1 && dX==0)
//			return true;
//		else if (dX==1 && dY==1)
//			return true;
//		else
//			return false;
		if (dX>1 || dY>1)
			return false;
		else
			return true;
	}
	
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
