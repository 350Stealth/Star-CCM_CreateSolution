// STAR-CCM+ macro: TestMacroTableExport1.java
// Written by STAR-CCM+ 14.02.012
package OldMacro;

import star.common.Simulation;
import star.common.StarMacro;
import star.common.XyzInternalTable;

import java.io.File;

public class MacroTableExport extends StarMacro {
    
    public static final String folder =
        "D:\\Projects\\Tantal\\BPLA\\334model\\CFD\\Results\\Test A7 high-lift wing plane\\Tables\\";
    
    public static final int
//        counter = 5,
        sDgetAngle = 10;
    
    public static void exportPressureTable(Simulation theSim, String tableName, String filePath) {
        
        XyzInternalTable xyzIT_table =
            ((XyzInternalTable) theSim.getTableManager().getTable(tableName));
        
        xyzIT_table.extract();
        
        int speed = 72;
        int aileron = 0;
        int flap = 30;
        int angle = 12;
        int fin = 0;
        String height = "1.31";
//        boolean flag = false;
        
        String filePathFull = String.format("%sVel %d flap %d ailer %d angle %d heigh %s\\", folder, speed, flap, aileron, angle, height);
        
        String fileName = String.format("%s%s CS.csv", filePathFull, tableName);
        
        File dirForTables = new File(filePathFull);
        //File fileForTable = new File(fileName);
        
        File file = new File(filePathFull);
        
        if (!file.exists()) {
            file.mkdirs();
        }
        
        int counter = 1;
        String buffFileName = fileName;
        
        while (true) {
            file = new File(buffFileName);
            if (!file.exists()) {
//			  flag = true;
                fileName = buffFileName;
                break;
            }
            buffFileName = fileName + "(" + counter + ")";
            counter++;
        }
        
        if (!dirForTables.exists()) {
            if (dirForTables.mkdirs()) {
                theSim.println("Создан каталог " + filePath);
                xyzIT_table.export(fileName, ",");
                theSim.println("Сохранена таблица " + tableName);
            } else {
                theSim.println("Каталог " + filePath + " создать не удалось");
            }
        } else {
            theSim.println("Каталог " + filePath + " уже существует");
            xyzIT_table.export(fileName, ",");
            theSim.println("Сохранена таблица " + tableName);
        }
        
    }
    
    public void execute() {
        execute0();
    }
    
    private void execute0() {
        
        Simulation theSim =
            getActiveSimulation();
/*
    XyzInternalTable xyzInternalTable_0 =
      ((XyzInternalTable) theSim.getTableManager().getTable("Fin pressure"));

    xyzInternalTable_0.extract();

    xyzInternalTable_0.export("D:\\Projects\\Tantal\\BPLA\\334model\\CFD\\Results\\Test A3 plane model\\Tables\\Fin pressure test.csv", ",");
*/
        
        
        //String folderForResults = String.format("%s\\Results", folder);
        
        String n_folderToSaveTables = folder;
        
        theSim.println(n_folderToSaveTables);
        
        theSim.println("Сохраняем таблицы давлений");
        
        exportPressureTable(theSim, "Aileron Force", n_folderToSaveTables);
        
        exportPressureTable(theSim, "Fin Force", n_folderToSaveTables);
        
        exportPressureTable(theSim, "Flap Force", n_folderToSaveTables);
        
        exportPressureTable(theSim, "Full Plane Force", n_folderToSaveTables);
        
        exportPressureTable(theSim, "Wing Force", n_folderToSaveTables);
        
    }
}