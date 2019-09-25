/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StarTestMacro;

import java.util.Collections;
import star.assistant.CSOCondition;
import star.assistant.CSOLookupConditionTrigger;
import star.common.GeometryPart;
import star.common.filters.Predicate;

/**
 *
 * @author Stealth This class contains all conditions used in the Internal Flow
 * Assistant.
 *
 */
public class InternalFlowConditions {

    public static synchronized CSOCondition<GeometryPart> createPartCondition() {

        // Creates a new condition
        CSOCondition<GeometryPart> partCondition = new CSOCondition<GeometryPart>();
        // Sets the text description of the condition
        partCondition.setDesc("A geometry part must be present.");
        // Creates a new condition trigger that goes off when a cadpart is added to the lookup
        CSOLookupConditionTrigger<GeometryPart> partConditionTrigger = new CSOLookupConditionTrigger<GeometryPart>(GeometryPart.class);
        // Sets the list of triggers to the one created above.
        partCondition.setTriggers(Collections.singleton(partConditionTrigger));
        // Creates a new predicate (true/false evaluation) with an evaluate method and evaluates whether an object satisfies the condition.
        partCondition.setPredicate(new Predicate<GeometryPart>() {
            @Override
            public boolean evaluate(GeometryPart part) {
            // You could check for specific attributes of the part here.
                return true;
            }
        });
        return partCondition;
    }

}
