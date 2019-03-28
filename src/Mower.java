
import java.util.ArrayList;

public class Mower {

    public Direction direction;
    private Move nextMove;
	public Navigator nav;
	
	boolean stopped;
	
	// TODO update this constructor with 
	// initial position and a pointer to a shared lawn
	public Mower(Direction initDirection)
	{
		stopped = false;
		nextMove = new Move();
		nav = new Navigator();
		direction = initDirection;
	}
	
	// decide what our next action should be
	public Action GetNextAction()
	{
		// if a single square nearby us is unknown, scan. 
		if (nav.CountNearby(nav.x, nav.y, Square.UNKNOWN) > 0)
			return Action.SCAN;
		
		// if we are at the edge of a lawn, scan.
		// this should always keep our knowledge in a good state
		if (nav.x==0 || nav.x == nav.width-1 || nav.y ==0 || nav.y == nav.height-1)
			return Action.SCAN;
		
		// turn off IFF there is no remaining grass
		// on second look, this logic is probably flawed. 
		// what if our map hasnt been expanded yet and we have somehow not scanned so as to know our bounds
		if (nav.CountOccurances(Square.UNCUT) == 0)
			return Action.TURN_OFF;
		
		// we decided to stop (last turn?)
		// if that didnt take effect, force it to now and just turn off.
		if (stopped)
			return Action.TURN_OFF;
		
		// guess the only thing left to do is move
		return Action.MOVE;
	}
	
	public Move DetermineMoveRequest()
	{		
		// FIRST figure out if there are two grasses just sitting in front of us. 
		boolean nextTwoAreGrass = true;

		for (int i = 1; i <= 2; i++) {
			int nextX = nav.x + Utils.GetDirectionDeltaX(direction) * i;
			int nextY = nav.y + Utils.GetDirectionDeltaY(direction) * i;
			if (nav.SafeQuery(nextX, nextY) != Square.UNCUT) {
				nextTwoAreGrass = false;
				break;
			}

		}
		if (nextTwoAreGrass) {
			nextMove.nSteps = 2;
			int nextX = nav.x + Utils.GetDirectionDeltaX(direction) * nextMove.nSteps;
			int nextY = nav.y + Utils.GetDirectionDeltaY(direction) * nextMove.nSteps;
			nextMove.direction = GetDirectionToNearestGrassOrDefault(nextX, nextY, direction);
			;
			return nextMove;
		}
		
		
		// crate list of all possible positions THAT ARE VALID (not craters) (remove self)
		// get the 8 in your immediate vicinity. (max manhattan distance of 1)
		// take a path. get all possible next steps. 
		// split that path for all possible next steps. remove each next step from the list of possible positions
		// if you found a tile of type XXXX. then STOP. you have found the shortest path to get to a tile that you wanted
		// 
		
		Square[] desiredSquares = new Square[2];
		desiredSquares[0] = Square.UNKNOWN;
		desiredSquares[1] = Square.UNCUT;
		
		// there are literally no more squares of interest. quit
		ArrayList<MyPair> path = nav.GetNextMoveToFind(desiredSquares, direction);
		if (path.size() == 1) {
			stopped = true;
			nextMove.nSteps = 0;
			return nextMove;
		}
		
		// the produced path is actually invalid 
		MyPair nextPoint = path.get(1);		
		MyPair currentPoint = path.get(0);
		if (!currentPoint.IsNeighbor(nextPoint)) {
			stopped = true;
			nextMove.nSteps = 0;
			System.out.println("NEXT POINT IS MORE THAN 1 UNIT AWAY");
			return nextMove;
		} 
		
		// we have a valid move. lets output it. first, check if we have to change direction
		if (!Utils.IsNeighborInDirection(currentPoint, nextPoint, direction)) {
			nextMove.nSteps = 0;
			nextMove.direction = currentPoint.GetDirectionTorwards(nextPoint);
			return nextMove;
		}
		
		// Where is our next point? we need to face in its direction
		nextMove.nSteps = 1;
		nextMove.direction = GetDirectionToNearestGrassOrDefault(nextPoint.x, nextPoint.y, direction);
		
		
		
		if (path.size() > 2) {
			MyPair nextNextPoint = path.get(2);
			if (Utils.IsNeighborInDirection(nextPoint, nextNextPoint, direction)) {
				nextMove.nSteps = 2;
				if (path.size() > 3) {
					MyPair nextNextNextPoint = path.get(3);
					nextMove.direction = nextNextPoint.GetDirectionTorwards(nextNextNextPoint);
				}
			} else {
				nextMove.direction = nextPoint.GetDirectionTorwards(nextNextPoint);
			}
		}
		
		
		
		return nextMove;
	}
	
	public Direction GetDirectionToNearestGrassOrDefault(int x, int y, Direction dir) {
		if (nav.SafeQuery(x + 0, y + 1) == Square.UNCUT)
			return Direction.NORTH;
		else if (nav.SafeQuery(x + 1, y + 1) == Square.UNCUT)
			return Direction.NORTH_EAST;
		else if (nav.SafeQuery(x + 1, y + 0) == Square.UNCUT)
			return Direction.EAST;
		else if (nav.SafeQuery(x + 1, y - 1) == Square.UNCUT)
			return Direction.SOUTH_EAST;
		else if (nav.SafeQuery(x + 0, y - 1) == Square.UNCUT)
			return Direction.SOUTH;
		else if (nav.SafeQuery(x - 1, y - 1) == Square.UNCUT)
			return Direction.SOUTH_WEST;
		else if (nav.SafeQuery(x - 1, y + 0) == Square.UNCUT)
			return Direction.WEST;
		else if (nav.SafeQuery(x - 1, y + 1) == Square.UNCUT)
			return Direction.NORTH_WEST;
		else
			return dir;
	}

	// take the last move that we requested, and execute it
	public void ExecuteRequestedMove() {
		// update lawn and grass cut
		nav.ApplyMove(nav.x, nav.y, nextMove.nSteps, direction); 
		
		// update perceived mower position and direction
		nav.x += Utils.GetDirectionDeltaX(direction) * nextMove.nSteps;
		nav.y += Utils.GetDirectionDeltaY(direction) * nextMove.nSteps;
		direction = nextMove.direction;
	}
	
	// receive scan. update our own shared lawn
	public void ReceiveScan(Square[] scan) {
		
		nav.IngestScan(scan);
		
	}
	
	// change our own state to the shutdown state
	public void Shutdown() {
		stopped = true;
	}
}
