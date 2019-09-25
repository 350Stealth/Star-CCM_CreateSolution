/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StarTestMacro;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JFileChooser;

import star.assistant.Task;
import star.assistant.annotation.StarAssistantTask;
import star.assistant.ui.FunctionTaskController;
import star.base.neo.DoubleVector;
import star.common.GeometryPart;
import star.common.Simulation;
import star.common.SimulationPartManager;
import star.meshing.PartImportManager;
import star.vis.CurrentView;
import star.vis.PartDisplayer;
import star.vis.Scene;

@StarAssistantTask(display = "Import Geometry",
        contentPath = "XHTML/01_ImportGeometry.xhtml",
        controller = Task01ImportGeometry.ImportGeometryTaskController.class)
public class Task01ImportGeometry extends Task {

    public Task01ImportGeometry() {
        // Makes sure that a geometry part is created.
        setPostconditions(Collections.singleton(InternalFlowConditions.createPartCondition()));
    }

    public class ImportGeometryTaskController extends FunctionTaskController {

        public void importSurfaceMeshDialog() {

//open a file chooser
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File cadFile = fileChooser.getSelectedFile();

//import the part
            Simulation simulation_0 = getSimulation();
            PartImportManager partImportManager_0
                    = simulation_0.get(PartImportManager.class);

//use default import options
            partImportManager_0.importCadPart(cadFile.getPath(), "SharpEdges", 30.0, 2, true, 1.0E-5, true, false);
//add the new part to the lookup
            Collection<GeometryPart> new_parts = simulation_0.get(SimulationPartManager.class).getParts();
            if (!new_parts.isEmpty()) {
                addToTaskLookup(new_parts.iterator().next());
            }

//create a Geometry Scene
            simulation_0.getSceneManager().createGeometryScene("Geometry Scene", "Outline", "Geometry", 1);
            Scene scene_0
                    = simulation_0.getSceneManager().getScene("Geometry Scene 1");
            scene_0.initializeAndWait();
            PartDisplayer partDisplayer_1
                    = ((PartDisplayer) scene_0.getCreatorDisplayer());
            partDisplayer_1.initialize();
            PartDisplayer partDisplayer_0
                    = ((PartDisplayer) scene_0.getDisplayerManager().getDisplayer("Outline 1"));
            partDisplayer_0.initialize();
            PartDisplayer partDisplayer_2
                    = ((PartDisplayer) scene_0.getDisplayerManager().getDisplayer("Geometry 1"));
            partDisplayer_2.initialize();
            scene_0.open(true);
            CurrentView currentView_0
                    = scene_0.getCurrentView();
            currentView_0.setInput(new DoubleVector(new double[]{0.07000000029802322, 0.02000000048428774, 0.0}),
                    new DoubleVector(new double[]{-0.14607243684130425, 0.11741414039370678, 0.17860456694301519}),
                    new DoubleVector(new double[]{0.20083797287207372, 0.9415226723669483, -0.2705534440210218}),
                    0.07747426272518879, 0);
        }
    }
}
