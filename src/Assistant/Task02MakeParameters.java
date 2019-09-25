package Assistant;

import Sources.SC_VariableSet;
import star.assistant.Task;
import star.assistant.annotation.StarAssistantTask;
import star.assistant.ui.FunctionTaskController;
import star.base.neo.IntVector;
import star.common.*;

@StarAssistantTask(display = "Создание параметров",
    contentPath = "XHTML/02_MakeParameters.xhtml",
    controller = Task02MakeParameters.ParametersTaskController.class)
public class Task02MakeParameters extends Task {
    
    public Task02MakeParameters() {
//        System.out.println("Создание параметров");
    }
    
    public class ParametersTaskController extends FunctionTaskController{
        private Simulation UsedSim;
        private double
            refArea = SC_VariableSet.refArea,
            refChord =SC_VariableSet.refChord;
        private String
            nm_VelCS = SC_VariableSet.nm_VelCS,
            nm_AOA = SC_VariableSet.nm_AOA,
            nm_ScalarV = SC_VariableSet.nm_ScalarV,
            nm_Velocity = SC_VariableSet.nm_Velocity,
            nm_TurbScale = SC_VariableSet.nm_TurbScale,
            nm_TurbVel = SC_VariableSet.nm_TurbVel,
            nm_TurbIntencity = SC_VariableSet.nm_TurbIntencity,
            nm_VelocityVec = SC_VariableSet.nm_VelocityVec,
            nm_Viscousity = SC_VariableSet.nm_Viscousity,
            nm_Chord = SC_VariableSet.nm_Chord,
            nm_RefArea = SC_VariableSet.nm_refArea;
        
        private String
            nm_BaseCellSize = SC_VariableSet.nm_BaseCellSize,
            nm_MinCellSizePerc = SC_VariableSet.nm_MinCellSizePerc,
            nm_TKE_Lim = SC_VariableSet.nm_TKE_Lim,
            nm_RefCoef = SC_VariableSet.nm_RefCoef;
        
        /*
        Создаем скоростную СК
         */
        public void createNewCS(){
            UsedSim = getActiveSimulation();
            
//            Создаем СК
            LabCoordinateSystem lcs_VelVecCS = UsedSim.getCoordinateSystemManager().getLabCoordinateSystem();
            CartesianCoordinateSystem ccs_VelVecCS = lcs_VelVecCS.getLocalCoordinateSystemManager()
                .createLocalCoordinateSystem(CartesianCoordinateSystem.class, "LocalCoordinateSystem");
            ccs_VelVecCS.setPresentationName(nm_VelCS);
            
            UsedSim.println("Создана новая СК");
            UsedSim = null;
        }
        
        /*
        Создаем стандартный набор переменных
         */
        public void createVarSet(){
            UsedSim = getActiveSimulation();
            
//            Создаем переменные
            makeAngleOfAttack(UsedSim);
            makeScalarV(UsedSim);
            makeVelocity(UsedSim);
            makeTurbScale(UsedSim);
            makeTurbVel(UsedSim);
            makeTurbIntensity(UsedSim);
            makeVelocityVec(UsedSim);
            makeViscousity(UsedSim);
            makeChord(UsedSim);
            
            UsedSim.println("Создан общий набор переменных");
            UsedSim = null;
        }
        
        /*
        Создаем переменные для адаптации сетки
         */
        public void createAdaptMeshVarSet(){
            UsedSim = getActiveSimulation();
            
//            Создаем переменные
            makeBaseCellSize(UsedSim);
            makeMinCellSizePerc(UsedSim);
            makeTKELim(UsedSim);
            makeRefCoef(UsedSim);
    
            UsedSim.println("Создан набор переменных для адаптации сетки");
            UsedSim = null;
        }
        
        /*
        Переменная угла атаки
         */
        private void makeAngleOfAttack(Simulation theSim) {
        
//            Создаем переменную угла атаки типа скаляр
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
            
//            получаем ссылку на эту переменную
            ScalarGlobalParameter sGP_AngleOfAttack =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
            
//            Переименовываем
            sGP_AngleOfAttack.setPresentationName(nm_AOA);
            
//            задаем тип переменной - угол
            sGP_AngleOfAttack.setDimensionsVector(new IntVector(
                new int[] {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                ));
            
//            создаем внутреннюю переменную с еденицами измерения градусы (deg)
            Units Unit_degs = (/*(Units)*/ theSim.getUnitsManager().getObject("deg"));
            
//            назначаем переменной еденицы измерения градусы
            sGP_AngleOfAttack.getQuantity().setUnits(Unit_degs);
            
//            устанавливаем значение переменной угла атаки
            sGP_AngleOfAttack.getQuantity().setValue(0.0);
        }
        
        /*
        Безразмерное значение скорости
         */
        private void makeScalarV(Simulation theSim){
    
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
            
            ScalarGlobalParameter sGB_ScalarV =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
            
            sGB_ScalarV.setPresentationName(nm_ScalarV);
            
            sGB_ScalarV.setDimensionsVector(new IntVector(
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                ));
            
//            дефолтное значение
            sGB_ScalarV.getQuantity().setValue(90);
        }
        
        private void makeRefArea(Simulation theSim){
    
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
    
            ScalarGlobalParameter sGB_ScalarV =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
    
            sGB_ScalarV.setPresentationName(nm_RefArea);
    
            sGB_ScalarV.setDimensionsVector(new IntVector(
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            ));

//            дефолтное значение
            sGB_ScalarV.getQuantity().setValue(refArea);
        }
        
        /*
        Создаем переменную скорости
         */
        private void makeVelocity(Simulation theSim){
    
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
            
            ScalarGlobalParameter sGB_Velosity =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
            
            sGB_Velosity.setPresentationName(nm_Velocity);
            
//            задаем тип переменной - скорость
            sGB_Velosity.setDimensionsVector(new IntVector(
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                ));
            
            String str_ScalarV = "${" + nm_ScalarV + "}";               // "${Скаляр V}"
            sGB_Velosity.getQuantity().setDefinition(str_ScalarV);
        }
        
        /*
        Создаем переменную Масштаба турбулентности
         */
        private void makeTurbScale(Simulation theSim){
    
            //Создаем переменную угла атаки типа "скаляр"
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
    
            //получаем ссылку на эту переменную
            ScalarGlobalParameter sGB_TurbScale =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
    
            //переименовываем
            sGB_TurbScale.setPresentationName(nm_TurbScale);
    
            //задаем тип переменной - безразмерная
            sGB_TurbScale.setDimensionsVector(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            //устанавливаем значение переменной
            sGB_TurbScale.getQuantity().setValue(0.01);
        }
        
        /*
        Создаем переменную Масштаб турбулентной скорости
         */
        private void makeTurbVel(Simulation theSim){
    
            //Создаем переменную угла атаки типа "скаляр"
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
    
            //получаем ссылку на эту переменную
            ScalarGlobalParameter sGB_TurbVel =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
    
            //переименовываем
            sGB_TurbVel.setPresentationName(nm_TurbVel);
    
            //задаем тип переменной - безразмерная
            sGB_TurbVel.setDimensionsVector(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            //устанавливаем значение переменной
            sGB_TurbVel.getQuantity().setValue(1);
        }
        
        /*
        Создаем переменную Интенсивность турбулентности
         */
        private void makeTurbIntensity(Simulation theSim){
    
            //Создаем переменную угла атаки типа "скаляр"
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
    
            //получаем ссылку на эту переменную
            ScalarGlobalParameter sGB_TurbIntensity =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
    
            //переименовываем
            sGB_TurbIntensity.setPresentationName(nm_TurbIntencity);
    
            //задаем тип переменной - безразмерная
            sGB_TurbIntensity.setDimensionsVector(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            //устанавливаем значение переменной
            sGB_TurbIntensity.getQuantity().setValue(0.01);
        }
        
        /*
        Создаем переменную вектора скорости
         */
        private void makeVelocityVec(Simulation theSim){
    
            //Создаем переменную угла атаки типа "вектор"
            theSim.get(GlobalParameterManager.class).createGlobalParameter(VectorGlobalParameter.class, "Вектор");
    
            //получаем ссылку на эту переменную
            VectorGlobalParameter vGB_VelosityVec =
                ((VectorGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Вектор"));
    
            //переименовываем
            vGB_VelosityVec.setPresentationName(nm_VelocityVec);
    
            //задаем тип переменной - безразмерная
            vGB_VelosityVec.setDimensionsVector(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            String str_VelVecDefenition = "[cos(${" + nm_AOA + "}), sin(${" + nm_AOA + "}), 0.0]";
            //устанавливаем значение переменной
            vGB_VelosityVec.getQuantity().setDefinition(str_VelVecDefenition);
        }
        
        /*
        Создаем безразмерное значение вязкости
         */
        private void makeViscousity(Simulation theSim){
    
            //Создаем переменную угла атаки типа "скаляр"
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
    
            //получаем ссылку на эту переменную
            ScalarGlobalParameter sGB_Viscousity =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
    
            //переименовываем
            sGB_Viscousity.setPresentationName(nm_Viscousity);
    
            //задаем тип переменной - безразмерная
            sGB_Viscousity.setDimensionsVector(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            //устанавливаем значение переменной
            sGB_Viscousity.getQuantity().setValue(1.7894e-5);
        }
        
        /*
        Создаем безразмерное значение длины хорды (в метрах)
         */
        private void makeChord(Simulation theSim){
    
            //Создаем переменную угла атаки типа "скаляр"
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
    
            //получаем ссылку на эту переменную
            ScalarGlobalParameter sGB_Chord =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
    
            //переименовываем
            sGB_Chord.setPresentationName(nm_Chord);
    
            //задаем тип переменной - безразмерная
            sGB_Chord.setDimensionsVector(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            //устанавливаем значение переменной
            sGB_Chord.getQuantity().setValue(refChord);
        }
        
        /*
        Создаем переменную BaseCellSize
         */
        private void makeBaseCellSize(Simulation theSim){
    
            //Создаем переменную угла атаки типа "скаляр"
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
    
            //получаем ссылку на эту переменную
            ScalarGlobalParameter sGB_Chord =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
    
            //переименовываем
            sGB_Chord.setPresentationName(nm_BaseCellSize);
    
            //задаем тип переменной - безразмерная
            sGB_Chord.setDimensionsVector(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            //устанавливаем значение переменной
            sGB_Chord.getQuantity().setValue(2.5);
        }
        
        /*
        Создаем переменную MinCellSizePerc
         */
        private void makeMinCellSizePerc(Simulation theSim){
    
            //Создаем переменную угла атаки типа "скаляр"
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
    
            //получаем ссылку на эту переменную
            ScalarGlobalParameter sGB_Chord =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
    
            //переименовываем
            sGB_Chord.setPresentationName(nm_MinCellSizePerc);
    
            //задаем тип переменной - безразмерная
            sGB_Chord.setDimensionsVector(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            //устанавливаем значение переменной
            sGB_Chord.getQuantity().setValue(0.0025);
        }
        
        /*
        Создаем переменую TKE_Lim
         */
        private void makeTKELim(Simulation theSim){
    
            //Создаем переменную угла атаки типа "скаляр"
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
    
            //получаем ссылку на эту переменную
            ScalarGlobalParameter sGB_Chord =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
    
            //переименовываем
            sGB_Chord.setPresentationName(nm_TKE_Lim);
    
            //задаем тип переменной - безразмерная
            sGB_Chord.setDimensionsVector(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            //устанавливаем значение переменной
            sGB_Chord.getQuantity().setValue(50);
        }
        
        /*
        Создаем переменную RefCoef
         */
        private void makeRefCoef(Simulation theSim){
    
            //Создаем переменную угла атаки типа "скаляр"
            theSim.get(GlobalParameterManager.class).createGlobalParameter(ScalarGlobalParameter.class, "Скаляр");
    
            //получаем ссылку на эту переменную
            ScalarGlobalParameter sGB_Chord =
                ((ScalarGlobalParameter) theSim.get(GlobalParameterManager.class).getObject("Скаляр"));
    
            //переименовываем
            sGB_Chord.setPresentationName(nm_TKE_Lim);
    
            //задаем тип переменной - безразмерная
            sGB_Chord.setDimensionsVector(new IntVector(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    
            //создаем строку с формулой
            String strFormula = "${" + nm_BaseCellSize + "}*${" + nm_MinCellSizePerc + "}*sqrt(${" + nm_TKE_Lim + "})";
            //устанавливаем значение переменной
            sGB_Chord.getQuantity().setDefinition(strFormula);
        }
    }
}
