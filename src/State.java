
public class State {

    public int truthX, truthY;
    public Direction direction;
    

//    public String trackAction;
//    public Integer trackMoveDistance;
//    public String trackNewDirection;
//    public String trackMoveCheck;
//    public String trackScanResults;
    
    public Action lastAction;
    public Move lastMove;
    public Square[] lastScan;
    
    public Mower mower;
    
    boolean crashed, stopped;

    
    public State()
    {
        truthX = -1;
        truthY = -1;
        direction = Direction.NORTH;
        crashed = false;
        stopped = false;
        mower = new Mower(Direction.NORTH);
    }
}
