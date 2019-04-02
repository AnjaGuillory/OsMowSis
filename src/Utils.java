import java.util.HashMap;

//Added New Puppy States and Mower
enum Square {
	UNKNOWN, NOTHING, UNCUT, CUT, OBSTACLE, FENCE, PUPPY_GRASS, PUPPY_EMPTY, PUPPY_MOWER, MOWER;
}

//Added Stall and Crash
enum Action {
	TURN_OFF, MOVE, SCAN, STAY, STALL, CRASH;
}

enum Direction {
	NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;
}

public class Utils {

	static public String ActionToString(Action action) {
		String str;
		switch (action) {
		case TURN_OFF:
			str = "turn_off";
			break;
		case MOVE:
			str = "move";
			break;
		case SCAN:
			str = "scan";
			break;
		case STAY:
			str = "stay";
		default:
			str = "UNKNOWN ACTION";
			break;
		}
		return str;
	}

	static public String DirectionToString(Direction direction) {
		String str;
		switch (direction) {
		case NORTH:
			str = "North";
			break;
		case NORTH_EAST:
			str = "Northeast";
			break;
		case EAST:
			str = "East";
			break;
		case SOUTH_EAST:
			str = "Southeast";
			break;
		case SOUTH:
			str = "South";
			break;
		case SOUTH_WEST:
			str = "Southwest";
			break;
		case WEST:
			str = "West";
			break;
		case NORTH_WEST:
			str = "Northwest";
			break;
		default:
			str = "UNKNOWN DIRECTION";
			break;
		}
		return str;
	}

	static public Direction StringToDirection(String str) {
		Direction dir;
		switch (str) {
		case "North":
			dir = Direction.NORTH;
			break;
		case "Northeast":
			dir = Direction.NORTH_EAST;
			break;
		case "East":
			dir = Direction.EAST;
			break;
		case "Southeast":
			dir = Direction.SOUTH_EAST;
			break;
		case "South":
			dir = Direction.SOUTH;
			break;
		case "Southwest":
			dir = Direction.SOUTH_WEST;
			break;
		case "West":
			dir = Direction.WEST;
			break;
		case "Northwest":
			dir = Direction.NORTH_WEST;
			break;
		default:
			dir = Direction.NORTH;
			break;
		}
		return dir;
	}
	
    //Added Puppy & Mower States
	static public String SquareToString(Square square) {
		String str;
		switch (square) {
		case UNKNOWN:
			str = "UNKNOWN";
			break;
		case NOTHING:
			str = "empty";
			break;
		case UNCUT:
			str = "grass";
			break;
		case CUT:
			str = "empty";
			break;
		case OBSTACLE:
			str = "crater";
			break;
		case FENCE:
			str = "fence";
			break;
		case PUPPY_GRASS:
			str = "puppy_grass";
		case PUPPY_EMPTY:
			str = "puppy_empty";
		case PUPPY_MOWER:
			str = "puppy_mower";
		case MOWER:
			str = "mower";
		default:
			str = "UNKNOWN SQUARE";
			break;
		}
		return str;
	}
	
	
	static public String ScanToString(Square[] squares) {
		String str = "";
		for (int i = 0; i < squares.length; i++)
			str += SquareToString(squares[i]) + ",";
		return str;
	}
	

	static private HashMap<Direction, Integer> xDIR_MAP;
	static private HashMap<Direction, Integer> yDIR_MAP;

	static {
		xDIR_MAP = new HashMap<>();
		xDIR_MAP.put(Direction.NORTH, 0);
		xDIR_MAP.put(Direction.NORTH_EAST, 1);
		xDIR_MAP.put(Direction.EAST, 1);
		xDIR_MAP.put(Direction.SOUTH_EAST, 1);
		xDIR_MAP.put(Direction.SOUTH, 0);
		xDIR_MAP.put(Direction.SOUTH_WEST, -1);
		xDIR_MAP.put(Direction.WEST, -1);
		xDIR_MAP.put(Direction.NORTH_WEST, -1);

		yDIR_MAP = new HashMap<>();
		yDIR_MAP.put(Direction.NORTH, 1);
		yDIR_MAP.put(Direction.NORTH_EAST, 1);
		yDIR_MAP.put(Direction.EAST, 0);
		yDIR_MAP.put(Direction.SOUTH_EAST, -1);
		yDIR_MAP.put(Direction.SOUTH, -1);
		yDIR_MAP.put(Direction.SOUTH_WEST, -1);
		yDIR_MAP.put(Direction.WEST, 0);
		yDIR_MAP.put(Direction.NORTH_WEST, 1);
	}	

	static public int GetDirectionDeltaX(Direction dir) {
		return xDIR_MAP.get(dir);
	}
	
	static public int GetDirectionDeltaY(Direction dir) {
		return yDIR_MAP.get(dir);
	}
	
	static public boolean IsNeighborInDirection(MyPair start, MyPair end, Direction dir) {
		return (start.x + GetDirectionDeltaX(dir) == end.x) && (start.y + GetDirectionDeltaY(dir) == end.y);
	}
}