package Assistant;


import Sources.SC_VariableSet;
import star.assistant.Task;
import star.assistant.annotation.StarAssistantTask;
import star.assistant.ui.FunctionTaskController;
import star.common.Boundary;
import star.common.Region;
import star.common.Simulation;
import star.common.*;
import star.base.neo.*;
import star.keturb.*;
import star.turbulence.*;
import star.flow.*;
import star.meshing.*;

@StarAssistantTask(display = "Определение границ",
    contentPath = "XHTML/05_MakeBoundaries.xhtml",
    controller = Task05MakeBoundaries.MakeBoundariesController.class)
public class Task05MakeBoundaries extends Task {
    
    public Task05MakeBoundaries() {
    }
    
    public class MakeBoundariesController extends FunctionTaskController{
    
        Simulation UsedSim;
        
        public void createBoundaries() {
            
            UsedSim = getActiveSimulation();
            
            String
                nm_Inlet = SC_VariableSet.nm_Inlet,
                nm_Outlet = SC_VariableSet.nm_Outlet,
                nm_Top = SC_VariableSet.nm_Top,
                nm_Bottom = SC_VariableSet.nm_Bottom,
                nm_Symmetry1 = SC_VariableSet.nm_Symmetry1,
                nm_Symmetry2 = SC_VariableSet.nm_Symmetry2,
                nm_EngineInlet = SC_VariableSet.nm_EngineInlet,
                nm_EngineOutlet = SC_VariableSet.nm_EngineOutlet,
                nm_Plane = SC_VariableSet.nm_Plane,
                nm_Region = SC_VariableSet.nm_Region;
    
            Region r_region =
                UsedSim.getRegionManager().createEmptyRegion();
    
            r_region.setPresentationName(nm_Region);
    
            Boundary b_boundary =
                r_region.getBoundaryManager().createEmptyBoundary();
    
            r_region.getPartGroup().setQuery(null);
    
            CompositePart cP_compositePart1 =
                ((CompositePart) UsedSim.get(SimulationPartManager.class).getPart("Test A plane 334m v2"));
    
            CompositePart cP_compositePart2 =
                ((CompositePart) cP_compositePart1.getChildParts().getPart("Domain for plane model v2; refset Entire Part"));
    
            CadPart cadP_cadPart1 =
                ((CadPart) cP_compositePart2.getChildParts().getPart("Body"));
    
            r_region.getPartGroup().setObjects(cadP_cadPart1);
    
            b_boundary.setPresentationName("Inlet");
    
            b_boundary.getPartSurfaceGroup().setQuery(null);
    
            PartSurface partSurface_1 =
                ((PartSurface) cadP_cadPart1.getPartSurfaceManager().getPartSurface("Inlet"));
    
            b_boundary.getPartSurfaceGroup().setObjects(partSurface_1);
    
            InletBoundary iB_inletBoundary1 =
                ((InletBoundary) UsedSim.get(ConditionTypeManager.class).get(InletBoundary.class));
    
            b_boundary.setBoundaryType(iB_inletBoundary1);
    
            Units units_1 =
                UsedSim.getUnitsManager().getPreferredUnits(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            TurbulenceIntensityProfile tIP_turbulenceIntensityProfile1 =
                b_boundary.getValues().get(TurbulenceIntensityProfile.class);
    
            tIP_turbulenceIntensityProfile1.getMethod(ConstantScalarProfileMethod.class).getQuantity().setDefinition("${\u0418\u043D\u0442\u0435\u043D\u0441\u0438\u0432\u043D\u043E\u0441\u0442\u044C \u0442\u0443\u0440\u0431\u0443\u043B\u0435\u043D\u0442\u043D\u043E\u0441\u0442\u0438}");
    
            b_boundary.getConditions().get(KeTurbSpecOption.class).setSelected(KeTurbSpecOption.Type.INTENSITY_LENGTH_SCALE);
    
            Units units_2 =
                UsedSim.getUnitsManager().getInternalUnits(new IntVector(new int[] {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            TurbulentLengthScaleProfile tLSP_turbulentLengthScaleProfile1 =
                b_boundary.getValues().get(TurbulentLengthScaleProfile.class);
    
            tLSP_turbulentLengthScaleProfile1.getMethod(ConstantScalarProfileMethod.class).getQuantity().setDefinition("${\u041C\u0430\u0441\u0448\u0442\u0430\u0431 \u0442\u0443\u0440\u0431\u0443\u043B\u0435\u043D\u0442\u043D\u043E\u0441\u0442\u0438}");
    
            Units units_3 =
                UsedSim.getUnitsManager().getPreferredUnits(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            VelocityMagnitudeProfile vMP_velocityMagnitudeProfile1 =
                b_boundary.getValues().get(VelocityMagnitudeProfile.class);
    
            vMP_velocityMagnitudeProfile1.getMethod(ConstantScalarProfileMethod.class).getQuantity().setDefinition("${\u0421\u043A\u043E\u0440\u043E\u0441\u0442\u044C}");
    
            b_boundary.getConditions().get(FlowDirectionOption.class).setSelected(FlowDirectionOption.Type.COMPONENTS);
    
            FlowDirectionProfile fDP_flowDirectionProfile1 =
                b_boundary.getValues().get(FlowDirectionProfile.class);
    
            LabCoordinateSystem lCS_labCoordinateSystem1 =
                UsedSim.getCoordinateSystemManager().getLabCoordinateSystem();
    
            CartesianCoordinateSystem cCS_cartesianCoordinateSystem =
                ((CartesianCoordinateSystem) lCS_labCoordinateSystem1.getLocalCoordinateSystemManager().getObject("\u0412\u0435\u043A\u0442\u043E\u0440 \u0441\u043A\u043E\u0440\u043E\u0441\u0442\u0438"));
    
            fDP_flowDirectionProfile1.setCoordinateSystem(cCS_cartesianCoordinateSystem);
        }
    }
}
