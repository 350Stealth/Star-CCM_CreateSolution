/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StarTestMacro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import star.assistant.Task;
import star.assistant.annotation.StarAssistantTask;
import star.assistant.ui.FunctionTaskController;
import star.common.GeometryPart;
import star.meshing.CadPart;

@StarAssistantTask(display = "Create Region from Part",
        contentPath = "XHTML/02_CreateRegionFromPart.xhtml",
        controller = Task02CreateRegionFromPart.RegionFromPartTaskController.class)
public class Task02CreateRegionFromPart extends Task {

    public Task02CreateRegionFromPart() {

        // Makes sure that a geometry exists before enabling this task.
        setPreconditions(Collections.singleton(InternalFlowConditions.createPartCondition()));
    }

    public class RegionFromPartTaskController extends FunctionTaskController {

        public void createRegion() {
            CadPart cadPart_1 = lookupObject(CadPart.class);
            if (cadPart_1 != null) {
                Collection<GeometryPart> list = new ArrayList<GeometryPart>();
                list.add(cadPart_1);
                getSimulation().getRegionManager().newRegionsFromParts(list,
                        "OneRegionPerPart", null, "OneBoundaryPerPartSurface", null,
                        "OneFeatureCurve", null, true);
            }
        }
    }
}
