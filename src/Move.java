

public class Move {
	public int nSteps;
	public Direction direction;


	public Move() {
		nSteps = 0;
		direction = Direction.NORTH;
	}
	public Move(int n, Direction dir) {
		nSteps = n;
		direction = dir;
	}
	
}
