/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StarTestMacro;

import star.assistant.annotation.StarAssistantTask;
import star.assistant.Task;
import star.assistant.ui.FunctionTaskController;
import star.common.Boundary;
import star.common.ConstantScalarProfileMethod;
import star.common.ConstantVectorProfileMethod;
import star.common.InletBoundary;
import star.common.PhysicsContinuum;
import star.common.PressureBoundary;
import star.common.Region;
import star.common.Simulation;
import star.common.SteadyModel;
import star.flow.ConstantDensityModel;
import star.flow.ConstantDensityProperty;
import star.flow.DynamicViscosityProperty;
import star.flow.LaminarModel;
import star.flow.VelocityMagnitudeProfile;
import star.flow.VelocityProfile;
import star.material.ConstantMaterialPropertyMethod;
import star.material.Gas;
import star.material.SingleComponentGasModel;
import star.metrics.ThreeDimensionalModel;
import star.segregatedflow.SegregatedFlowModel;

@StarAssistantTask(display = "Create Physics",
        contentPath = "XHTML/03_Physics.xhtml",
        controller = Task03Physics.PhysicsTaskController.class)
public class Task03Physics extends Task {

    public class PhysicsTaskController extends FunctionTaskController {

        public void createPhysicsContinuum() {
            // code for Step 1: creating and defining the physics continuum.
            Simulation simulation_0
                    = getActiveSimulation();
            PhysicsContinuum physicsContinuum_0
                    = simulation_0.getContinuumManager().createContinuum(PhysicsContinuum.class);
            physicsContinuum_0.setPresentationName("Physics");
            physicsContinuum_0.enable(ThreeDimensionalModel.class);
            physicsContinuum_0.enable(SteadyModel.class);
            physicsContinuum_0.enable(SingleComponentGasModel.class);
            physicsContinuum_0.enable(SegregatedFlowModel.class);
            physicsContinuum_0.enable(ConstantDensityModel.class);
            physicsContinuum_0.enable(LaminarModel.class);
            // Object tree focus that expands the physics model manager node. All other open nodes are collapsed.
            selectAndExpandNodeExclusive(physicsContinuum_0.getModelManager());
        }

        public void materialProperties() {
            // code for Step 2: modifying the material properties of air.
            Simulation simulation_0
                    = getSimulation();
            PhysicsContinuum physicsContinuum_0
                    = ((PhysicsContinuum) simulation_0.getContinuumManager().getContinuum("Physics"));
            SingleComponentGasModel singleComponentGasModel_0
                    = physicsContinuum_0.getModelManager().getModel(SingleComponentGasModel.class);
            Gas gas_0
                    = ((Gas) singleComponentGasModel_0.getMaterial());
            ConstantMaterialPropertyMethod constantMaterialPropertyMethod_0
                    = ((ConstantMaterialPropertyMethod) gas_0.getMaterialProperties()
                            .getMaterialProperty(ConstantDensityProperty.class).getMethod());
            constantMaterialPropertyMethod_0.getQuantity().setValue(1.0);
            ConstantMaterialPropertyMethod constantMaterialPropertyMethod_1
                    = ((ConstantMaterialPropertyMethod) gas_0.getMaterialProperties()
                            .getMaterialProperty(DynamicViscosityProperty.class).getMethod());
            constantMaterialPropertyMethod_1.getQuantity().setValue(1.716E-5);
            //Object tree focus that expands the Dynamic Viscosity node without affecting other open nodes.
            selectAndExpandNode(gas_0.getMaterialProperties()
                    .getMaterialProperty(DynamicViscosityProperty.class).getMethod());
        }

        public void initialConditionsAndBoundarySettings() {
            // code for Step 3: defining the initial conditions, boundary type, and boundary conditions.
            Simulation simulation_0
                    = getActiveSimulation();
            PhysicsContinuum physicsContinuum_0
                    = ((PhysicsContinuum) simulation_0.getContinuumManager().getContinuum("Physics"));
            VelocityProfile velocityProfile_0
                    = physicsContinuum_0.getInitialConditions().get(VelocityProfile.class);
            velocityProfile_0.getMethod(ConstantVectorProfileMethod.class).getQuantity().setComponents(0.429, 0.0, 0.0);
            Region region_0
                    = simulation_0.getRegionManager().getRegion("Fluid");
            Boundary boundary_0
                    = region_0.getBoundaryManager().getBoundary("Inlet");
            boundary_0.setBoundaryType(InletBoundary.class);
            VelocityMagnitudeProfile velocityMagnitudeProfile_0
                    = boundary_0.getValues().get(VelocityMagnitudeProfile.class);
            velocityMagnitudeProfile_0.getMethod(ConstantScalarProfileMethod.class).getQuantity().setValue(0.429);
            Boundary boundary_1
                    = region_0.getBoundaryManager().getBoundary("Outlet");
            boundary_1.setBoundaryType(PressureBoundary.class);
        }
    }
}
