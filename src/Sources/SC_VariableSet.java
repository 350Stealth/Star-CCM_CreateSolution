package Sources;

public class SC_VariableSet {

    /*
    Task02MakeParameters
     */
    public final static double
        refArea = 2,
        refChord = 0.375;
    public final static String
        nm_VelCS = "Вектор скорости",
        nm_AOA = "Угол атаки",
        nm_ScalarV = "Скаляр V",
        nm_Velocity = "Скорость",
        nm_TurbScale = "Масштаб турбулентности",
        nm_TurbVel = "Масштаб турбулентной скорости",
        nm_TurbIntencity = "Интенсивность турбулентности",
        nm_VelocityVec = "Вектор скорости",
        nm_Viscousity = "Вязкость",
        nm_Chord = "Хорда",
        nm_refArea = "Площадь крыла";
    
//    Имена переменных для адаптации сетки
    /*
    Task03MakeMeshTemplate
     */
    public final static String
        nm_BaseCellSize = "BaseCellSize",
        nm_MinCellSizePerc = "MinCellSizePerc",
        nm_TKE_Lim = "TKE_Lim",
        nm_RefCoef = "RefCoef";
    
    /*
    Task04MakePhysics
     */
    public final static String
        nm_Gas = "Air";
    
    /*
    Task05MakeBoundaries
     */
    public final static String
        nm_Inlet = "Inlet",
        nm_Outlet = "Outlet",
        nm_Top = "Top",
        nm_Bottom = "Bottom",
        nm_Symmetry1 = "Symmetry 1",
        nm_Symmetry2 = "Symmetry 2",
        nm_EngineInlet = "EngineInlet",
        nm_EngineOutlet = "EngineOutlet",
        nm_Plane = "Plane",
        nm_Region = "Region";
}
