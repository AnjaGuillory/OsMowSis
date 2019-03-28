
public class TruthWithMower {

    public int truthX, truthY;
    public Direction direction;
        
    public Action lastAction;
    public Move lastMove;
    public Square[] lastScan;
    
    public Mower mower;
    
    boolean crashed, stopped;

    
    public TruthWithMower()
    {
        truthX = -1;
        truthY = -1;
        direction = Direction.NORTH;
        crashed = false;
        stopped = false;
        mower = new Mower(Direction.NORTH);
    }
}
