public class Main {

    public static void main(String[] args) {
    	boolean outputEnvironment = false;
    	boolean outputMowerEnvironment = false;
    	boolean reportIsVerbose = false;
    	
        SimDriver monitorSim = new SimDriver();
        monitorSim.verbose = outputMowerEnvironment;

        // check for the test scenario file name
        if (args.length == 0) {
            System.out.println("ERROR: Test scenario file name not found.");
        } else {
            monitorSim.uploadStartingFile(args[0]);

            // run the simulation for a fixed number of steps
            for(int turns = 0; turns < 100; turns++) {
            	if (monitorSim.AreAllStopped())
            		break;
                monitorSim.Step();
                monitorSim.displayActionAndResponses();

                // REMEMBER to delete or comment out the rendering before submission
                if (outputEnvironment)
                	monitorSim.renderLawn();

                // pause after each event for a given number of seconds
                // pausing is completely optional
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
            
            monitorSim.PrintReport(reportIsVerbose);
        }
    }

}
