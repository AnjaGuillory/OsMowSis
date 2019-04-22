# OsMowSis Simulation
## Team 29 - CS6310


## Getting Started
```
To get started, please install the Windows VM titled team_29.ova.
The VM is currently set to a base memory of 4096 MB and 2 processors, but please adjust to your preference.
The VM window size may need to be adjusted to fully display the Simulation's GUI.
```

### Setting up Test Scenarios
```
Place any test scenario files in "/home/student/Desktop/OsMowSis/out/artifacts/OsMowSis_jar"
```

### Running a Scenario (jar)

```
To execute the jar, open a terminal window, and run:
"cd Desktop/OsMowSis/out/artifacts/OsMowSis_jar"
Then, "java -jar OsMowSis.jar <your scenario file>"
```

### Stepping through a Scenario

```
When the program is running and the GUI is visible, you have a few choices in the UI:

“Next” button will poll the next object, and step through the simulation turn by turn.

“Fast-Forward” button will execute the current simulation run to completion and write to the output log.

“Stop” button will terminate the simulation run at the current state of the simulation and write to 
the output log.

The simulation info at the bottom of the GUI will be updated as you step through the simulation with the 
"Next" button, or at the end of the "Fast-Forward".
```

### Viewing Output Log
```
The output log file "Log.txt" will be located in:
"/home/student/OsMowSis/out/artifacts/OsMowSis_jar/out"
This file will be overwritten with each subsequent scenario run.
```

### Building jar (IntelliJ)
```
Optionally, you can first delete the existing jar file "OsMowSis.jar" located in: "/home/student/Desktop/OsMowSis/out/artifacts/OsMowSis_jar"

To build the jar in IntelliJ, start by opening IntelliJ -- in bottom right of Toolbar.
The "OsMowSis" project should open automatically.
At the top of the IntelliJ window, click "Build --> Build Artifacts --> OsMowSis:jar --> Build"
Once finished, the jar file will be located in: "/home/student/Desktop/OsMowSis/out/artifacts/OsMowSis_jar"
```

## Authors

* **Sam Bayaa**
* **Jordan Burdette**
* **Anja Guillory**
* **Keith Rausch**
* **Hector Villafuerte**
