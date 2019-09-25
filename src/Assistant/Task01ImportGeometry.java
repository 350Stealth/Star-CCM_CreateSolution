package Assistant;

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

import javax.swing.*;
import java.io.File;
import java.util.Collection;

@StarAssistantTask(display = "Импорт геометрии",
    contentPath = "XHTML/01_ImportGeometry.xhtml",
    controller = Task01ImportGeometry.ImportGeometryTaskController.class)
public class Task01ImportGeometry extends Task {
    
    private Simulation UsedSim;
    
    public Task01ImportGeometry() {
//        UsedSim = usedSim;
//        UsedSim.println("Импорт геометрии");
    }
//    Контроллер интерфейса
    public class ImportGeometryTaskController extends FunctionTaskController{
        
        /*
        Метод для импорта геометрии
         */
        public void importSurfaceMeshDialog(){
            UsedSim = getActiveSimulation();
    
            //open a file chooser
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File cadFile = fileChooser.getSelectedFile();

//import the part
//            Simulation simulation_0 = getSimulation();
            PartImportManager pIM_ImportedPart =
                UsedSim.get(PartImportManager.class);

//use default import options
            pIM_ImportedPart.importCadPart(cadFile.getPath(), "SharpEdges", 30.0, 4, true, 1.0E-5, true, false);

//add the new part to the lookup
            Collection<GeometryPart> new_parts = UsedSim.get(SimulationPartManager.class).getParts();
            if (!new_parts.isEmpty()) {
                addToTaskLookup(new_parts.iterator().next());
            }

//create a Geometry Scene
            UsedSim.getSceneManager().createGeometryScene("Geometry Scene", "Outline", "Geometry", 1);
            Scene s_scene =
                UsedSim.getSceneManager().getScene("Geometry Scene 1");
            s_scene.setPresentationName("Геометрия");
    
            s_scene.initializeAndWait();
            PartDisplayer pd_Displayer0 =
                (/*(PartDisplayer)*/ s_scene.getCreatorDisplayer());
            pd_Displayer0.initialize();
            PartDisplayer pd_Displayer1 =
                ((PartDisplayer) s_scene.getDisplayerManager().getDisplayer("Outline 1"));
            pd_Displayer1.initialize();
            PartDisplayer pd_displayer2 =
                ((PartDisplayer) s_scene.getDisplayerManager().getDisplayer("Geometry 1"));
            pd_displayer2.initialize();
            s_scene.open(true);
            CurrentView cv_View1 =
                s_scene.getCurrentView();
            s_scene.setViewOrientation(new DoubleVector(new double[] {0.0, 0.0, 1.0}), new DoubleVector(new double[] {0.0, 1.0, 0.0}));
            cv_View1.setInput(new DoubleVector(new double[]{12.5, 0.0, -7.5}),
                new DoubleVector(new double[]{-12.5, 0.0, 116.95083721698647}),
                new DoubleVector(new double[]{0.0, 1.1, -0.0}),
                32.48818778306368, 1, 30.0);
            s_scene.resetCamera();
            
            UsedSim.println("Геометрия импортирована");
            UsedSim = null;
        }
    }
}
