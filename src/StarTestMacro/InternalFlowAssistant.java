/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StarTestMacro;

import java.util.ArrayList;
import java.util.List;
import star.assistant.SimulationAssistant;
import star.assistant.Task;
import star.assistant.annotation.StarAssistant;

// Specifies the name of the Simulation Assistant in the GUI.
@StarAssistant(display = "Internal Flow Assistant")
public class InternalFlowAssistant extends SimulationAssistant {

    public InternalFlowAssistant() {
// Creates a new array list for the list of tasks.
        List<Task> tasks = new ArrayList<Task>();
// Contains the list of tasks.
        tasks.add(new Task01ImportGeometry());
        tasks.add(new Task02CreateRegionFromPart());
        tasks.add(new Task03Physics());
        setOutline(tasks);
    }
}
