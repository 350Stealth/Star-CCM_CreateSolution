package Assistant;

import star.assistant.SimulationAssistant;
import star.assistant.Task;
import star.assistant.annotation.StarAssistant;

import java.util.ArrayList;
import java.util.List;

// Определяем название нашего ассистента
@StarAssistant(display = "Помощник создания решения")
public class SolutionCreateAssistant extends SimulationAssistant {

    public SolutionCreateAssistant() {
    
//        Создаем новый массив списка с задачами
        List<Task> tasks = new ArrayList<>();
//        Напоняем список задачами
        tasks.add(new Task01ImportGeometry());
        tasks.add(new Task02MakeParameters());
        tasks.add(new Task03MakeMeshTemplate());
        tasks.add(new Task04MakePhysics());
        tasks.add(new Task05MakeBoundaries());
        tasks.add(new Task06MakeReports());
        setOutline(tasks);
    }
}
