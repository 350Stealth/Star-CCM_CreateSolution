// STAR-CCM+ macro: MacroSavePictures.java
// Written by STAR-CCM+ 14.06.012
package OldMacro;

import star.base.neo.DoubleVector;
import star.common.HardcopyProperties;
import star.common.Simulation;
import star.common.StarMacro;
import star.vis.CurrentView;
import star.vis.Scene;
import star.vis.SceneUpdate;
import star.vis.VisView;

import java.io.File;

public class MacroSavePictures extends StarMacro {
    
    public void execute() {
        execute0();
    }
    
    private void execute0() {
        
        int speed = 72;
        int aileron = 0;
        int flap = 30;
        int angle = 12;
        int fin = 0;
        String height = "1.31";
        String folderPath = String.format("D:\\Projects\\Tantal\\BPLA\\334model\\CFD\\Results\\Test A7 high-lift wing plane\\Images\\Vel %d flap %d ailer %d angle %d heigh %s", speed, flap, aileron, angle, height);
        String sceneName;
        int counter = 2;
        String count = "";
        
        while (!(new File(folderPath).exists())) {
            count = String.format("%d", counter);
            folderPath = String.format("%s(%s)", folderPath, count);
            counter++;
        }
        
        Simulation simulation_0 =
            getActiveSimulation();
        
        Scene scene_5 =
            simulation_0.getSceneManager().getScene("Volume Pressure");
        
        SceneUpdate sceneUpdate_4 =
            scene_5.getSceneUpdate();
        
        HardcopyProperties hardcopyProperties_4 =
            sceneUpdate_4.getHardcopyProperties();
        
        hardcopyProperties_4.setCurrentResolutionWidth(2162);
        
        hardcopyProperties_4.setCurrentResolutionWidth(2527);
        
        hardcopyProperties_4.setCurrentResolutionHeight(1270);
        
        CurrentView currentView_4 =
            scene_5.getCurrentView();
        
        VisView visView_0 =
            ((VisView) simulation_0.getViewManager().getObject("View 7"));
        
        currentView_4.setView(visView_0);
        
        currentView_4.setInput(new DoubleVector(new double[]{0.28193823348888714, 0.08032544426986837, 0.024268146488536903}), new DoubleVector(new double[]{-78.0049722661562, 0.08032544426986837, 0.024268146488536903}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.6821238835781922, 1, 30.0);
        
        sceneName = "Volume Pressure";
        
        scene_5.printAndWait(resolvePath(String.format("%s\\%s front.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        VisView visView_1 =
            ((VisView) simulation_0.getViewManager().getObject("View 3"));
        
        currentView_4.setView(visView_1);
        
        currentView_4.setInput(new DoubleVector(new double[]{0.28155498292955117, 0.09702454724563514, 0.13134744296918655}), new DoubleVector(new double[]{0.28155498292955117, 0.09702454724563514, 78.41787469205494}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.6201128721533014, 1, 30.0);
        
        scene_5.printAndWait(resolvePath(String.format("%s\\%s side.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        VisView visView_2 =
            ((VisView) simulation_0.getViewManager().getObject("View 5"));
        
        currentView_4.setView(visView_2);
        
        scene_5.setAdvancedRenderingEnabled(true);
        
        currentView_4.setInput(new DoubleVector(new double[]{0.42695269307071815, 0.09401912077776231, -0.03739367409978822}), new DoubleVector(new double[]{45.634483218716575, 45.301549646423624, 45.17013685154607}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.8171083128080675, 1, 30.0);
        
        scene_5.printAndWait(resolvePath(String.format("%s\\%s iso1.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        scene_5.setAdvancedRenderingEnabled(false);
        
        currentView_4.setInput(new DoubleVector(new double[]{0.42695269307071815, 0.09401912077776231, -0.03739367409978822}), new DoubleVector(new double[]{45.634483218716575, 45.301549646423624, 45.17013685154607}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.8171083128080675, 1, 30.0);
        
        VisView visView_3 =
            ((VisView) simulation_0.getViewManager().getObject("View 6"));
        
        currentView_4.setView(visView_3);
        
        currentView_4.setInput(new DoubleVector(new double[]{0.39887729908506664, -0.07352378704946183, 0.05846638749943267}), new DoubleVector(new double[]{-44.823189043974914, 45.14854255601052, 45.280532730559415}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.800848762065106, 1, 30.0);
        
        scene_5.printAndWait(resolvePath(String.format("%s\\%s iso2.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        Scene scene_4 =
            simulation_0.getSceneManager().getScene("Volume Velocity");
        
        SceneUpdate sceneUpdate_6 =
            scene_4.getSceneUpdate();
        
        HardcopyProperties hardcopyProperties_9 =
            sceneUpdate_6.getHardcopyProperties();
        
        hardcopyProperties_9.setCurrentResolutionWidth(2527);
        
        hardcopyProperties_9.setCurrentResolutionHeight(1270);
        
        CurrentView currentView_5 =
            scene_4.getCurrentView();
        
        currentView_5.setView(visView_0);
        
        sceneName = "Volume Velocity";
        
        currentView_5.setInput(new DoubleVector(new double[]{0.28193823348888714, 0.08032544426986837, 0.024268146488536903}), new DoubleVector(new double[]{-78.0049722661562, 0.08032544426986837, 0.024268146488536903}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.6821238835781922, 1, 30.0);
        
        currentView_5.setInput(new DoubleVector(new double[]{0.28193823348888714, 0.08032544426986837, 0.024268146488536903}), new DoubleVector(new double[]{-78.0049722661562, 0.08032544426986837, 0.024268146488536903}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.6821238835781922, 1, 30.0);
        
        scene_4.printAndWait(resolvePath(String.format("%s\\%s front.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        currentView_5.setView(visView_1);
        
        currentView_5.setInput(new DoubleVector(new double[]{0.28155498292955117, 0.09702454724563514, 0.13134744296918655}), new DoubleVector(new double[]{0.28155498292955117, 0.09702454724563514, 78.41787469205494}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.6201128721533014, 1, 30.0);
        
        scene_4.printAndWait(resolvePath(String.format("%s\\%s side.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        currentView_5.setView(visView_2);
        
        currentView_5.setInput(new DoubleVector(new double[]{0.42695269307071815, 0.09401912077776231, -0.03739367409978822}), new DoubleVector(new double[]{45.634483218716575, 45.301549646423624, 45.17013685154607}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.8171083128080675, 1, 30.0);
        
        scene_4.printAndWait(resolvePath(String.format("%s\\%s iso1.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        currentView_5.setView(visView_3);
        
        currentView_5.setInput(new DoubleVector(new double[]{0.39887729908506664, -0.07352378704946183, 0.05846638749943267}), new DoubleVector(new double[]{-44.823189043974914, 45.14854255601052, 45.280532730559415}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.800848762065106, 1, 30.0);
        
        scene_4.printAndWait(resolvePath(String.format("%s\\%s iso2.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        Scene scene_7 =
            simulation_0.getSceneManager().getScene("Volume TKE");
        
        SceneUpdate sceneUpdate_7 =
            scene_7.getSceneUpdate();
        
        HardcopyProperties hardcopyProperties_10 =
            sceneUpdate_7.getHardcopyProperties();
        
        hardcopyProperties_10.setCurrentResolutionWidth(2527);
        
        hardcopyProperties_10.setCurrentResolutionHeight(1270);
        
        CurrentView currentView_6 =
            scene_7.getCurrentView();
        
        currentView_6.setView(visView_0);
        
        sceneName = "Volume TKE";
        
        currentView_6.setInput(new DoubleVector(new double[]{0.28193823348888714, 0.08032544426986837, 0.024268146488536903}), new DoubleVector(new double[]{-78.0049722661562, 0.08032544426986837, 0.024268146488536903}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.6821238835781922, 1, 30.0);
        
        scene_7.printAndWait(resolvePath(String.format("%s\\%s front.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        currentView_6.setView(visView_1);
        
        currentView_6.setInput(new DoubleVector(new double[]{0.28155498292955117, 0.09702454724563514, 0.13134744296918655}), new DoubleVector(new double[]{0.28155498292955117, 0.09702454724563514, 78.41787469205494}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.6201128721533014, 1, 30.0);
        
        scene_7.printAndWait(resolvePath(String.format("%s\\%s side.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        currentView_6.setView(visView_2);
        
        currentView_6.setInput(new DoubleVector(new double[]{0.42695269307071815, 0.09401912077776231, -0.03739367409978822}), new DoubleVector(new double[]{45.634483218716575, 45.301549646423624, 45.17013685154607}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.8171083128080675, 1, 30.0);
        
        scene_7.printAndWait(resolvePath(String.format("%s\\%s iso11.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        currentView_6.setView(visView_3);
        
        currentView_6.setInput(new DoubleVector(new double[]{0.39887729908506664, -0.07352378704946183, 0.05846638749943267}), new DoubleVector(new double[]{-44.823189043974914, 45.14854255601052, 45.280532730559415}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 0.800848762065106, 1, 30.0);
        
        scene_7.printAndWait(resolvePath(String.format("%s\\%s iso2.png", folderPath, sceneName)), 1, 3840, 2160, true, false);
        
        Scene scene_6 =
            simulation_0.getSceneManager().getScene("Vector on cut plane");
        
        SceneUpdate sceneUpdate_5 =
            scene_6.getSceneUpdate();
        
        HardcopyProperties hardcopyProperties_5 =
            sceneUpdate_5.getHardcopyProperties();
        
        hardcopyProperties_5.setCurrentResolutionWidth(2527);
        
        hardcopyProperties_5.setCurrentResolutionHeight(1270);
        
        scene_6.setAdvancedRenderingEnabled(true);
        
        CurrentView currentView_3 =
            scene_6.getCurrentView();
        
        currentView_3.setInput(new DoubleVector(new double[]{1.3171224849506573, -0.08884380993205904, -0.021012982587848228}), new DoubleVector(new double[]{1.3171224849506573, -0.08884380993205904, 70.54470218535003}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 1.0922504244025104, 1, 30.0);
        
        scene_6.printAndWait(resolvePath(String.format("%s\\Vector on cut plane.png", folderPath)), 1, 3840, 2160, true, false);
        
        scene_6.setAdvancedRenderingEnabled(false);
    }
}
