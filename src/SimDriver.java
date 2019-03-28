import java.util.Scanner;
import java.io.*;


public class SimDriver {

    private TruthWithMower[] truths;
    private Environment environ;
    boolean verbose;
    int startingGrassCount;
    int nTurns;

    public SimDriver() {
    	verbose = false;
    	startingGrassCount = 0;
    	nTurns = 0;
    }

    // TODO this is left over from the non-puppy days. it needs to be updated
    // read in the file and create the lawn, mowers, and puppies
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
            truths = new TruthWithMower[numMowers];
            for (k = 0; k < numMowers; k++) {
                tokens = takeCommand.nextLine().split(DELIMITER);
                TruthWithMower truth = new TruthWithMower();
                truth.truthX = Integer.parseInt(tokens[0]);
                truth.truthY = Integer.parseInt(tokens[1]);
                truth.direction = Utils.StringToDirection(tokens[2]);
                truths[k] = truth;
//              IT DOESNT MAKE SENSE TO CUT THIS GRASS. THE SIM HASNT STARTED YET. AND ACCORDING TO THE RULES
//              IF ALL WE DID WAS SCAN AND NOT MOVE, THEN THIS SHOULDNT GET CUT
//                // mow the grass at the initial location
//                if (lawnInfo[truth.truthX][truth.truthY] == Square.UNCUT)
//                	lawnInfo[truth.truthX][truth.truthY] = Square.CUT;
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

    // check if each mower is stopped
	public boolean AreAllStopped() {

		for (int s = 0; s < truths.length; s++) {
			if (truths[s].stopped == false)
				return false;
		}
		return true;
	}

	// perform a single step of the simulation
	public void Step() {

		for (int s = 0; s < truths.length; s++) {
			TruthWithMower truth = truths[s];
			Mower mower = truth.mower;
			
			if (truth.stopped)
				continue;
			
			truth.lastAction = mower.GetNextAction();
			switch (truth.lastAction) {
			case TURN_OFF:
				truth.stopped = true;
				break;
			case SCAN:
				Square[] scan = environ.GetScan(truth.truthX, truth.truthY);
				truth.lastScan = scan;
				mower.ReceiveScan(scan);
				break;
			case MOVE:
				Move move = mower.DetermineMoveRequest();
				boolean moveIsGood = environ.IsMoveValid(truth.truthX, truth.truthY, move.nSteps, truth.direction);
				
				// TODO HANDLE STALL LOGIC HERE
				if (moveIsGood) {
					environ.ApplyMove(truth.truthX, truth.truthY, move.nSteps, truth.direction);
					mower.ExecuteRequestedMove();
				} else {
					// we seem to have crashed
					truth.stopped = true;
					truth.crashed = true;
					mower.Shutdown();
				}
				truth.truthX += Utils.GetDirectionDeltaX(truth.direction) * move.nSteps;
				truth.truthY += Utils.GetDirectionDeltaY(truth.direction) * move.nSteps;
				truth.direction = move.direction;
				truth.lastMove = move;
				break;
			default:
				break;
			}

		}
		nTurns++;
	}

	public void displayActionAndResponses() {

		for (int s = 0; s < truths.length; s++) {
			TruthWithMower truth = truths[s];
			Action lastAction = truth.lastAction;
			String trackAction = Utils.ActionToString(lastAction);
			
			// display the mower's actions
			System.out.print(trackAction);
			if (lastAction == Action.MOVE) {
				System.out.println("," + truth.lastMove.nSteps + "," + Utils.DirectionToString(truth.lastMove.direction));
			} else {
				System.out.println();
			}

			// display the simulation checks and/or responses
			if (lastAction == Action.MOVE | lastAction == Action.TURN_OFF) {
				String trackMoveCheck = "ok";
				if (truth.crashed)
					trackMoveCheck = "crashed";
				System.out.println(trackMoveCheck);
			} else if (lastAction == Action.SCAN) {
				System.out.println(Utils.ScanToString(truth.lastScan));
			} else {
				System.out.println("action not recognized");
			}
		}
	}

    public void renderLawn() {

    	int[] mowerX = new int[truths.length];
    	int[] mowerY = new int[truths.length];    	

    	for (int s = 0; s < truths.length; s++) {
    		mowerX[s] = truths[s].truthX;
    		mowerY[s] = truths[s].truthY;
    	}
    	
    	environ.Render(mowerX, mowerY);
    	
		if (verbose) {
			System.out.println("MOWERS:::::::::::::::::::::::::::::::");

			for (int s = 0; s < truths.length; s++) {
				mowerX = new int[1];
				mowerY = new int[1];
				mowerX[0] = truths[s].mower.nav.x;
				mowerY[0] = truths[s].mower.nav.y;
				truths[s].mower.nav.Render(mowerX, mowerY);
			}
		}
    	
    	for (int s = 0; s < truths.length; s++) {
            // display the mower's direction
            System.out.println("dir: " + Utils.DirectionToString(truths[s].direction));
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