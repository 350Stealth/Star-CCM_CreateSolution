package Assistant;

import Sources.SC_VariableSet;
import star.assistant.Task;
import star.assistant.annotation.StarAssistantTask;
import star.assistant.ui.FunctionTaskController;
import star.common.*;
import star.flow.ConstantDensityModel;
import star.flow.DynamicViscosityProperty;
import star.flow.VelocityProfile;
import star.keturb.KEpsilonTurbulence;
import star.keturb.KeTurbSpecOption;
import star.keturb.KeTwoLayerAllYplusWallTreatment;
import star.keturb.RkeTwoLayerTurbModel;
import star.material.ConstantMaterialPropertyMethod;
import star.material.Gas;
import star.material.SingleComponentGasModel;
import star.metrics.ThreeDimensionalModel;
import star.segregatedflow.SegregatedFlowModel;
import star.turbulence.*;

@StarAssistantTask(display = "Определение физической модели",
    contentPath = "XHTML/04_MakePhysics.xhtml",
    controller = Task04MakePhysics.MakeContinuaController.class)
public class Task04MakePhysics extends Task {
    
    public Task04MakePhysics() {
    }
    
    public class MakeContinuaController extends FunctionTaskController{
        
        Simulation UsedSim;
        
        public void createPhysics(){
            UsedSim = getActiveSimulation();
            
            makePhysicsContinuum(UsedSim);
        }
        
        public void fillInitialConditions(){
    
            UsedSim = getActiveSimulation();
            
            String
                nm_Viscousity = SC_VariableSet.nm_Viscousity,
                nm_ScalarV = SC_VariableSet.nm_ScalarV,
                nm_TurbIntencity = SC_VariableSet.nm_TurbIntencity,
                nm_TurbScale = SC_VariableSet.nm_TurbScale,
                nm_TurbVel = SC_VariableSet.nm_TurbVel,
                nm_VelCS = SC_VariableSet.nm_VelCS;
    
    
            PhysicsContinuum pC_physicsContinuum =
                ((PhysicsContinuum) UsedSim.getContinuumManager().getContinuum("Air"));
    
            fillPhysicsContinuum(nm_Viscousity, pC_physicsContinuum);
    
            /*
            Заполнение начальных данных среды
             */
    
            fillStartParameters(nm_ScalarV, nm_TurbIntencity, nm_TurbScale, nm_TurbVel, nm_VelCS, pC_physicsContinuum);
        }
    
        /*
        Заполняем начальные параметры
         */
        private void fillStartParameters(String nm_ScalarV, String nm_TurbIntencity, String nm_TurbScale, String nm_TurbVel, String nm_VelCS, PhysicsContinuum pC_physicsContinuum) {
            VelocityProfile vP_velocityProfile =
                pC_physicsContinuum.getInitialConditions().get(VelocityProfile.class);
        
            vP_velocityProfile.getMethod(ConstantVectorProfileMethod.class).getQuantity().setDefinition("${" + nm_ScalarV + "}*[1.0, 0.0, 0.0]");
        
            LabCoordinateSystem lCS_labCoordinateSystem =
                UsedSim.getCoordinateSystemManager().getLabCoordinateSystem();
        
            CartesianCoordinateSystem cCS_cartesianCoordinateSystem =
                ((CartesianCoordinateSystem) lCS_labCoordinateSystem.getLocalCoordinateSystemManager().getObject(nm_VelCS));
        
            vP_velocityProfile.setCoordinateSystem(cCS_cartesianCoordinateSystem);
        
            pC_physicsContinuum.getInitialConditions().get(KeTurbSpecOption.class).setSelected(KeTurbSpecOption.Type.INTENSITY_LENGTH_SCALE);
        
            TurbulenceIntensityProfile tIP_turbulenceIntensityProfile =
                pC_physicsContinuum.getInitialConditions().get(TurbulenceIntensityProfile.class);
        
            tIP_turbulenceIntensityProfile.getMethod(ConstantScalarProfileMethod.class).getQuantity().setDefinition("${" + nm_TurbIntencity + "}");

//            Units units_4 =
//                simulation_0.getUnitsManager().getInternalUnits(new IntVector(new int[] {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        
            TurbulentLengthScaleProfile tLSP_turbulentLengthScaleProfile =
                pC_physicsContinuum.getInitialConditions().get(TurbulentLengthScaleProfile.class);
        
            tLSP_turbulentLengthScaleProfile.getMethod(ConstantScalarProfileMethod.class).getQuantity().setDefinition("${" + nm_TurbScale + "}");
        
            TurbulentVelocityScaleProfile tVSP_turbulentVelocityScaleProfile =
                pC_physicsContinuum.getInitialConditions().get(TurbulentVelocityScaleProfile.class);
        
            tVSP_turbulentVelocityScaleProfile.getMethod(ConstantScalarProfileMethod.class).getQuantity().setDefinition("${" + nm_TurbVel + "}");
        }
    
        /*
        Заполняем данные среды
         */
        private void fillPhysicsContinuum(String nm_Viscousity, PhysicsContinuum pC_physicsContinuum) {
            SingleComponentGasModel sCGM_singleComponentGasModel =
                pC_physicsContinuum.getModelManager().getModel(SingleComponentGasModel.class);
        
            Gas g_air =
                ((Gas) sCGM_singleComponentGasModel.getMaterial());
        
            ConstantMaterialPropertyMethod cMPM_constantMaterialPropertyMethod =
                ((ConstantMaterialPropertyMethod) g_air.getMaterialProperties().getMaterialProperty(DynamicViscosityProperty.class).getMethod());
        
            cMPM_constantMaterialPropertyMethod.getQuantity().setDefinition("${" + nm_Viscousity + "}");
        }
        
        /*
        Создаем среду
         */
        private void makePhysicsContinuum(Simulation theSim) {
            
            String nm_Gas = SC_VariableSet.nm_Gas;
    
            PhysicsContinuum pc_Physics_1 =
                theSim.getContinuumManager().createContinuum(PhysicsContinuum.class);
    
            pc_Physics_1.setPresentationName(nm_Gas);           //nm_Gas = "Air"
    
            pc_Physics_1.enable(ThreeDimensionalModel.class);
    
            pc_Physics_1.enable(SteadyModel.class);
    
            pc_Physics_1.enable(SingleComponentGasModel.class);
    
            pc_Physics_1.enable(SegregatedFlowModel.class);
    
            pc_Physics_1.enable(ConstantDensityModel.class);
    
            pc_Physics_1.enable(TurbulentModel.class);
    
            pc_Physics_1.enable(RansTurbulenceModel.class);
    
            pc_Physics_1.enable(KEpsilonTurbulence.class);
    
            pc_Physics_1.enable(RkeTwoLayerTurbModel.class);
    
            pc_Physics_1.enable(KeTwoLayerAllYplusWallTreatment.class);
        }
    }
}
