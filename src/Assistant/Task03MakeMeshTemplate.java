package Assistant;

import java.util.*;

import star.assistant.Task;
import star.assistant.annotation.StarAssistantTask;
import star.assistant.ui.FunctionTaskController;
import star.common.Simulation;
import star.common.*;
import star.base.neo.*;
import star.dualmesher.DualAutoMesher;
import star.resurfacer.*;
import star.prismmesher.*;
import star.meshing.*;
import Sources.SC_VariableSet;

@StarAssistantTask(display = "Создание генератора сетки",
    contentPath = "XHTML/03_MakeMeshTemplate.xhtml",
    controller = Task03MakeMeshTemplate.MakeMeshTemplateController.class)
public class Task03MakeMeshTemplate extends Task {
    
    public Task03MakeMeshTemplate() {
        System.out.println("Создание шаблона генератора сетки");
    }
    
    public class MakeMeshTemplateController extends FunctionTaskController{
        
        public void makeMeshGenerator(){
            Simulation UsedSim = getActiveSimulation();
            
            /*
            Создаем шаблон генератора сетки
             */
            Collection<GeometryPart> new_parts = UsedSim.get(SimulationPartManager.class).getParts();
            if (!new_parts.isEmpty()) {
                CompositePart geomPart_ForMesh = (CompositePart) new_parts.iterator().next();
    
                AutoMeshOperation aMO_Meshing =
                    UsedSim.get(MeshOperationManager.class).createAutoMeshOperation(
                        new StringVector(
                            new String[]{"star.resurfacer.ResurfacerAutoMesher", "star.dualmesher.DualAutoMesher", "star.prismmesher.PrismAutoMesher"}
                        ), new NeoObjectVector(new Object[]{geomPart_ForMesh})
                    );
                
//                Устанавливаем параллельный режим генератора сетки Concurrent
                mesherParallelMode(aMO_Meshing);
                
//                Устанавливаем базовый размер сетки
                setBaseCellSize(aMO_Meshing, 2.5);
                
//                Устанавливаем целевой размер ячейки ан поверхности
                targetSurfSize(aMO_Meshing, 100.0);
                
                Units units_mm =
                    (UsedSim.getUnitsManager().getObject("mm"));
                
//                Устанавливаем аппроксимацию кривизны поверхностей
                setSurfaceCurvature(aMO_Meshing, 80.0, units_mm);
                
//                Задаем коэффициент роста на поверхности
                setSurfGrowthRate(aMO_Meshing, 1.2);
                
//                Задаем настройки призматического слоя
                setPrismLayerOptions(UsedSim, aMO_Meshing, units_mm, 100.0, 0.1);
    
//                Задаем оптимизацию сетки
                setMeshOptimization(aMO_Meshing);

                
//                Задаем рост в объеме
                setVolumeGrowthRate(aMO_Meshing, 1.15, 200.0);
    
                UsedSim.println("Создание генератора сетки");
                
                
//                Создаем Surface custom control
                
                makeSurfaceCustomControl(UsedSim, new_parts, aMO_Meshing, units_mm);
    
            } else {
                
                UsedSim.println("Нет загруженной геометрии!!!");
            }
        }
    
        private void makeSurfaceCustomControl(Simulation usedSim, Collection<GeometryPart> new_parts, AutoMeshOperation aMO_Meshing, Units units_mm) {
            Collection<GeometryPart> geomBodyParts = new_parts;
        
            Collection<PartSurface> ps_partSurf = new_parts.iterator().next().getPartSurfaces();
        
            int compPartsCount = ps_partSurf.size();
        
            usedSim.println("Количество поверхностей объекта: " + compPartsCount);
            
            int f = 0;
        
            while (compPartsCount !=0) {

                GeometryPart gP_nextPart = geomBodyParts.iterator().next();

                geomBodyParts = gP_nextPart.getPartManager().getParts();

                compPartsCount = geomBodyParts.iterator().next().getPartSurfaces().size();

                usedSim.println("количество поверхностей объекта: " + compPartsCount);
                usedSim.println(geomBodyParts.toString());
                
                f++;
                if (f > 10) break;
            }
        
            if (!geomBodyParts.isEmpty()){
                CadPart cadPart = (CadPart) geomBodyParts.iterator().next();
                
                try {
                    PartSurface partSurface = cadPart.getPartSurfaceManager().getPartSurface("Plane");

                    /*
                    Задаем настройки
                     */
                    customSurfaceControl(usedSim, aMO_Meshing, units_mm, partSurface);

                    usedSim.println("Есть поверхность с именем Plane!");
                }
                catch (Exception ex) {

                    usedSim.println("Нет поверхности с именем Plane!");
                    usedSim.println(ex.getMessage());
                }
            }
        }
    
        private void customSurfaceControl(Simulation usedSim, AutoMeshOperation aMO_Meshing, Units units_mm, PartSurface partSurface) {
            SurfaceCustomMeshControl sCMC_surfaceCustomMeshControl =
                aMO_Meshing.getCustomMeshControls().createSurfaceControl();
        
            sCMC_surfaceCustomMeshControl.getGeometryObjects().setQuery(null);
        
            sCMC_surfaceCustomMeshControl.getGeometryObjects().setObjects(partSurface);
        
            sCMC_surfaceCustomMeshControl.setPresentationName("Plane");
        
            sCMC_surfaceCustomMeshControl.getCustomConditions().get(PartsMinimumSurfaceSizeOption.class).setSelected(PartsMinimumSurfaceSizeOption.Type.CUSTOM);
        
            sCMC_surfaceCustomMeshControl.getCustomConditions().get(PartsSurfaceCurvatureOption.class).setSelected(PartsSurfaceCurvatureOption.Type.CUSTOM_VALUES);
        
            PartsCustomizePrismMesh pCPM_partsCustomizePrismMesh =
                sCMC_surfaceCustomMeshControl.getCustomConditions().get(PartsCustomizePrismMesh.class);
        
            pCPM_partsCustomizePrismMesh.getCustomPrismOptions().setSelected(PartsCustomPrismsOption.Type.CUSTOMIZE);
        
            PartsCustomizePrismMeshControls pCPMC_meshCustomPrismControls =
                pCPM_partsCustomizePrismMesh.getCustomPrismControls();
        
            pCPMC_meshCustomPrismControls.setCustomizeNumLayers(true);
        
            pCPMC_meshCustomPrismControls.setCustomizeTotalThickness(true);
        
            pCPMC_meshCustomPrismControls.setCustomizeStretching(true);
            
            /*
            Устанавливаем параметры кривизны поверхности
             */
    
            setSurfaceCurvature(sCMC_surfaceCustomMeshControl, 80.0, units_mm);
//            setSurfaceCurvature(aMO_Meshing, 80.0, units_mm);
        
            PartsMinimumSurfaceSize pMSS_partsMinimumSurfaceSize =
                sCMC_surfaceCustomMeshControl.getCustomValues().get(PartsMinimumSurfaceSize.class);
        
            pMSS_partsMinimumSurfaceSize.getRelativeOrAbsoluteOption().setSelected(RelativeOrAbsoluteOption.Type.ABSOLUTE);
        
            pMSS_partsMinimumSurfaceSize.getAbsoluteSizeValue().setUnits(units_mm);
        
            pMSS_partsMinimumSurfaceSize.getAbsoluteSizeValue().setValue(5.0);
            
            /*
            Устанавливаем параметры призматического слоя
             */
    
            setPrismLayerOptions(usedSim, sCMC_surfaceCustomMeshControl, units_mm, 15.0, 0.1);
//            setPrismLayerOptions(usedSim, aMO_Meshing, units_mm, 10.0, 0.1);
        }
    
        private void setMeshOptimization(AutoMeshOperation aMO_Meshing) {
            PartsCoreMeshOptimizer pCMO_MeshOptimazer =
                aMO_Meshing.getDefaultValues().get(PartsCoreMeshOptimizer.class);
        
            pCMO_MeshOptimazer.setOptimizeCycles(2);
        
            PartsPostMeshOptimizerBase partsPostMeshOptimizerBase =
                aMO_Meshing.getDefaultValues().get(PartsPostMeshOptimizerBase.class);
        
            partsPostMeshOptimizerBase.setOptimizeTopology(true);
        }
        
        private void setPrismLayerOptions(Simulation usedSim, SurfaceCustomMeshControl sCMC_surfaceCustomMeshControl, Units units_mm, double numPrismLayers, double firstLayer) {
            NumPrismLayers nPL_numPrismLayers =
                sCMC_surfaceCustomMeshControl.getCustomValues().get(CustomPrismValuesManager.class).get(NumPrismLayers.class);
        
            IntegerValue iV_integerValue =
                nPL_numPrismLayers.getNumLayersValue();
        
            iV_integerValue.getQuantity().setValue(numPrismLayers);
        
            sCMC_surfaceCustomMeshControl.getCustomValues().get(CustomPrismValuesManager.class).get(PrismWallThickness.class).setUnits(units_mm);
        
            sCMC_surfaceCustomMeshControl.getCustomValues().get(CustomPrismValuesManager.class).get(PrismWallThickness.class).setValue(firstLayer);
        
            PrismThickness pT_prismThickness =
                sCMC_surfaceCustomMeshControl.getCustomValues().get(CustomPrismValuesManager.class).get(PrismThickness.class);
        
            pT_prismThickness.getRelativeOrAbsoluteOption().setSelected(RelativeOrAbsoluteOption.Type.ABSOLUTE);
        
            pT_prismThickness.getAbsoluteSizeValue().setUnits(units_mm);
    
//                проверяем, есть ли переменная "Хорда". Если да, то толщина слоя Хорда/20, иначе просто 15 мм.
    
            if ((usedSim.get(GlobalParameterManager.class).getObject(SC_VariableSet.nm_Chord)) != null) {
        
                (pT_prismThickness.getAbsoluteSizeValue()).setDefinition("${" + SC_VariableSet.nm_Chord + "}/20");
        
                (pT_prismThickness.getAbsoluteSizeValue()).setUnits(units_mm);
            } else {
        
                (pT_prismThickness.getAbsoluteSizeValue()).setValue(15.0);
        
                (pT_prismThickness.getAbsoluteSizeValue()).setUnits(units_mm);
            }
        
//            pT_prismThickness.getAbsoluteSizeValue().setValue(15.0);
        }
    
        private void setPrismLayerOptions(Simulation usedSim, AutoMeshOperation aMO_Meshing, Units units_mm, double numPrismLayers, double firstLayer) {
            NumPrismLayers nPL_numPrismLayer =
                aMO_Meshing.getDefaultValues().get(NumPrismLayers.class);
        
            IntegerValue iV_Value_1 =
                nPL_numPrismLayer.getNumLayersValue();
        
            iV_Value_1.getQuantity().setValue(numPrismLayers);
        
            PrismAutoMesher prism_layer_mesher =
                ((PrismAutoMesher) aMO_Meshing.getMeshers().getObject("Prism Layer Mesher"));
        
            prism_layer_mesher.getPrismStretchingOption().setSelected(PrismStretchingOption.Type.WALL_THICKNESS);
        
            aMO_Meshing.getDefaultValues().get(PrismWallThickness.class).setUnits(units_mm);
        
            aMO_Meshing.getDefaultValues().get(PrismWallThickness.class).setValue(firstLayer);
        
            PrismThickness pT_Thickness =
                aMO_Meshing.getDefaultValues().get(PrismThickness.class);
        
            pT_Thickness.getRelativeOrAbsoluteOption().setSelected(RelativeOrAbsoluteOption.Type.ABSOLUTE);

//                проверяем, есть ли переменная "Хорда". Если да, то толщина слоя Хорда/20, иначе просто 15 мм.
        
            if ((usedSim.get(GlobalParameterManager.class).getObject(SC_VariableSet.nm_Chord)) != null) {
                
                (pT_Thickness.getAbsoluteSizeValue()).setDefinition("${" + SC_VariableSet.nm_Chord + "}/20");
    
                (pT_Thickness.getAbsoluteSizeValue()).setUnits(units_mm);
            } else {
    
                (pT_Thickness.getAbsoluteSizeValue()).setValue(15.0);
    
                (pT_Thickness.getAbsoluteSizeValue()).setUnits(units_mm);
            }
        }
    
        private void setSurfGrowthRate(AutoMeshOperation aMO_Meshing, double growthRate) {
            SurfaceGrowthRate sGR_SurfGrowthRate =
                aMO_Meshing.getDefaultValues().get(SurfaceGrowthRate.class);
        
            sGR_SurfGrowthRate.getGrowthRateScalar().setValue(growthRate);
        }
    
        private void setSurfaceCurvature(SurfaceCustomMeshControl sCMC_surfaceCustomMeshControl, double pointsAroundCircle, Units units_mm) {
            SurfaceCurvature sC_surfaceCurvature =
                sCMC_surfaceCustomMeshControl.getCustomValues().get(SurfaceCurvature.class);
        
            sC_surfaceCurvature.setEnableCurvatureDeviationDist(true);
        
            sC_surfaceCurvature.setNumPointsAroundCircle(pointsAroundCircle);
        
            sC_surfaceCurvature.getCurvatureDeviationDistance().setUnits(units_mm);
        }
        
        private void setSurfaceCurvature(AutoMeshOperation aMO_Meshing, double pointsAroundCircle, Units units_mm) {
            SurfaceCurvature surfCur_SurfaceCurvature =
                aMO_Meshing.getDefaultValues().get(SurfaceCurvature.class);
        
            surfCur_SurfaceCurvature.setEnableCurvatureDeviationDist(true);
        
            surfCur_SurfaceCurvature.setNumPointsAroundCircle(pointsAroundCircle);
        
            surfCur_SurfaceCurvature.getCurvatureDeviationDistance().setUnits(units_mm);
        }
    
        private void targetSurfSize(AutoMeshOperation aMO_Meshing, double surfCellSize) {
            PartsTargetSurfaceSize pTSS_TargetSurfSize =
                aMO_Meshing.getDefaultValues().get(PartsTargetSurfaceSize.class);
        
            pTSS_TargetSurfSize.getRelativeSizeScalar().setValue(surfCellSize);
        }
    
        private void setBaseCellSize(AutoMeshOperation aMO_Meshing, double cellBaseSize) {
            aMO_Meshing.getDefaultValues().get(BaseSize.class).setValue(cellBaseSize);
        }
    
        private void mesherParallelMode(AutoMeshOperation aMO_Meshing) {
            aMO_Meshing.getMesherParallelModeOption().setSelected(MesherParallelModeOption.Type.PARALLEL);
        }
        
        private void setVolumeGrowthRate(AutoMeshOperation aMO_Meshing, double growthRate, double maxCellSizePerc){
            DualAutoMesher dAM_AutoMesher =
                ((DualAutoMesher) aMO_Meshing.getMeshers().getObject("Polyhedral Mesher"));
    
            dAM_AutoMesher.setEnableGrowthRate(true);
    
            PartsTetPolyGrowthRate pTPGR_TetPolyGrowthRate =
                aMO_Meshing.getDefaultValues().get(PartsTetPolyGrowthRate.class);
    
            pTPGR_TetPolyGrowthRate.setGrowthRate(growthRate);
    
            MaximumCellSize maximumCellSize =
                aMO_Meshing.getDefaultValues().get(MaximumCellSize.class);
    
            maximumCellSize.getRelativeSizeScalar().setValue(maxCellSizePerc);
        }
    
        /*
        Запускаем генератор сетки
        */
        public void runMeshGenerator(){
            Simulation UsedSim = getActiveSimulation();
            
            UsedSim.println("Запустить генератор сетки");
        }
    }
}
