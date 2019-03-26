import java.util.Scanner;
import java.io.*;


public class SimDriver {

    private State[] states;
    private Environment environ;
    boolean verbose;
    int startingGrassCount;
    int nTurns;

    public SimDriver() {
    	verbose = false;
    	startingGrassCount = 0;
    	nTurns = 0;
    }

    public void uploadStartingFile(String testFileName) {
        final String DELIMITER = ",";
        
        Integer lawnHeight;
        Integer lawnWidth;
        Square[][] lawnInfo;

        try {
            Scanner takeCommand = new Scanner(new File(testFileName));
            String[] tokens;
            int i, j, k;

            // read in the lawn information
            tokens = takeCommand.nextLine().split(DELIMITER);
            lawnWidth = Integer.parseInt(tokens[0]);
            tokens = takeCommand.nextLine().split(DELIMITER);
            lawnHeight = Integer.parseInt(tokens[0]);

            // generate the lawn information
            lawnInfo = new Square[lawnWidth][lawnHeight];
            for (i = 0; i < lawnWidth; i++) {
                for (j = 0; j < lawnHeight; j++) {
                    lawnInfo[i][j] = Square.UNCUT;
                }
            }

            // read in the lawnmower starting information
            tokens = takeCommand.nextLine().split(DELIMITER);
            int numMowers = Integer.parseInt(tokens[0]);
            states = new State[numMowers];
            for (k = 0; k < numMowers; k++) {
                tokens = takeCommand.nextLine().split(DELIMITER);
                State state = new State();
                state.truthX = Integer.parseInt(tokens[0]);
                state.truthY = Integer.parseInt(tokens[1]);
                state.direction = Utils.StringToDirection(tokens[2]);
                states[k] = state;
//              IT DOESNT MAKE SENSE TO CUT THIS GRASS. THE SIM HASNT STARTED YET. AND ACCORDING TO THE RULES
//              IF ALL WE DID WAS SCAN AND NOT MOVE, THEN THIS SHOULDNT GET CUT
//                // mow the grass at the initial location
//                if (lawnInfo[state.truthX][state.truthY] == Square.UNCUT)
//                	lawnInfo[state.truthX][state.truthY] = Square.CUT;
            }

            // read in the crater information
            tokens = takeCommand.nextLine().split(DELIMITER);
            int numCraters = Integer.parseInt(tokens[0]);
            for (k = 0; k < numCraters; k++) {
                tokens = takeCommand.nextLine().split(DELIMITER);

                // place a crater at the given location
                lawnInfo[Integer.parseInt(tokens[0])][Integer.parseInt(tokens[1])] = Square.OBSTACLE;
            }
            environ = new Environment(lawnWidth, lawnHeight, lawnInfo);
            startingGrassCount = environ.CountOccurances(Square.UNCUT);

            takeCommand.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
        }
	}

	public boolean AreAllStopped() {

		for (int s = 0; s < states.length; s++) {
			if (states[s].stopped == false)
				return false;
		}
		return true;
	}

	public void Step() {

		for (int s = 0; s < states.length; s++) {
			State state = states[s];
			Mower mower = state.mower;
			
			if (state.stopped)
				continue;
			
			state.lastAction = mower.GetNextAction();
			switch (state.lastAction) {
			case TURN_OFF:
				state.stopped = true;
				break;
			case SCAN:
				Square[] scan = environ.GetScan(state.truthX, state.truthY);
				state.lastScan = scan;
				mower.ReceiveScan(scan);
				break;
			case MOVE:
				Move move = mower.DetermineMoveRequest();
				boolean moveIsGood = environ.IsMoveValid(state.truthX, state.truthY, move.nSteps, state.direction);
				
				if (moveIsGood) {
					environ.ApplyMove(state.truthX, state.truthY, move.nSteps, state.direction);
					mower.ExecuteRequestedMove();
				} else {
					state.stopped = true;
					state.crashed = true;
					mower.Shutdown();
				}
				state.truthX += Utils.GetDirectionDeltaX(state.direction) * move.nSteps;
				state.truthY += Utils.GetDirectionDeltaY(state.direction) * move.nSteps;
				state.direction = move.direction;
				state.lastMove = move;
				break;
			default:
				break;
			}

		}
		nTurns++;
	}

	public void displayActionAndResponses() {

		for (int s = 0; s < states.length; s++) {
			State state = states[s];
			Action lastAction = state.lastAction;
			String trackAction = Utils.ActionToString(lastAction);
			
			// display the mower's actions
			System.out.print(trackAction);
			if (lastAction == Action.MOVE) {
				System.out.println("," + state.lastMove.nSteps + "," + Utils.DirectionToString(state.lastMove.direction));
			} else {
				System.out.println();
			}

			// display the simulation checks and/or responses
			if (lastAction == Action.MOVE | lastAction == Action.TURN_OFF) {
				String trackMoveCheck = "ok";
				if (state.crashed)
					trackMoveCheck = "crashed";
				System.out.println(trackMoveCheck);
			} else if (lastAction == Action.SCAN) {
				System.out.println(Utils.ScanToString(state.lastScan));
			} else {
				System.out.println("action not recognized");
			}
		}
	}

    public void renderLawn() {

    	int[] mowerX = new int[states.length];
    	int[] mowerY = new int[states.length];    	

    	for (int s = 0; s < states.length; s++) {
    		mowerX[s] = states[s].truthX;
    		mowerY[s] = states[s].truthY;
    	}
    	
    	environ.Render(mowerX, mowerY);
    	
		if (verbose) {
			System.out.println("MOWERS:::::::::::::::::::::::::::::::");

			for (int s = 0; s < states.length; s++) {
				mowerX = new int[1];
				mowerY = new int[1];
				mowerX[0] = states[s].mower.nav.x;
				mowerY[0] = states[s].mower.nav.y;
				states[s].mower.nav.Render(mowerX, mowerY);
			}
		}
    	
    	for (int s = 0; s < states.length; s++) {
            // display the mower's direction
            System.out.println("dir: " + Utils.DirectionToString(states[s].direction));
    	}

        System.out.println("");
        
		if (verbose) {
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
		}
    }
    
    public void PrintReport(boolean reportIsVerbose) {
    	
		int lawnSize = environ.height * environ.width;
		int nGrassesCut = environ.CountOccurances(Square.CUT);

		if (reportIsVerbose) {
            System.out.println("REPORT:");
			System.out.println("Lawn Size: " + lawnSize);
			System.out.println("Starting grass count: " + startingGrassCount);
			System.out.println("Number of grasses cut: " + nGrassesCut);
			System.out.println("Total Turns: " + nTurns);
		} else {
			System.out.println(lawnSize + "," + startingGrassCount + "," + nGrassesCut + "," + nTurns);
		}
    }

}