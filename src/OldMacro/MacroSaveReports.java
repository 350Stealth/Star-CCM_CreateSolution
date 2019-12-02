// STAR-CCM+ macro: TestMacro8.java
// Written by STAR-CCM+ 14.06.012
package OldMacro;

import star.common.Simulation;
import star.common.StarMacro;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MacroSaveReports extends StarMacro {
    
    public static final String folder =
        "D:\\Projects\\Tantal\\BPLA\\334model\\CFD\\Results\\Test A7 high-lift wing plane\\Reports\\";
    private String[] reports = new String[]{
        "Cx",
        "Cy",
        "K",
        "Y",
        "X",
        "Cmz",
        "Mz",
        "Ya",
        "Xa",
        "Za",
        "Mxa",
        "Mya",
        "Mza",
        "Yf",
        "Xf",
        "Zf",
        "Mxf",
        "Myf",
        "Mzf",
        "Yfin",
        "Xfin",
        "Zfin",
        "Mxfin",
        "Myfin",
        "Mzfin",
        "Yw",
        "Xw",
        "Zw",
        "Mxw",
        "Myw",
        "Mzw"};
    
    public void execute() {
        execute0();
    }
    
    private void execute0() {
        
        boolean flag = false;
        int speed = 324;
        int aileron = 0;
        int flap = 0;
        int angle = 12;
        int fin = 10;
        String height = "15";
        
        String filePathFull = String.format("%sVel %d flap %d ailer %d angle %d fin %d heigh %s\\", folder, speed, flap, aileron, angle, fin, height);
        
        String fileName = String.format("%s%s.csv", filePathFull, "Reports");
        
        Simulation simulation_0 =
            getActiveSimulation();
        
        if (!(new File(filePathFull).exists())) {
            new File(filePathFull).mkdir();
        }
        
        try {
            File file = new File(filePathFull);
            
            if (!file.exists()) {
                file.mkdirs();
            }
            int counter = 1;
            String buffFileName = fileName;
            
            while (true) {
                file = new File(buffFileName);
                if (!file.exists()) {
                    flag = true;
                    fileName = buffFileName;
                    break;
                }
                buffFileName = String.format("%s%s(%d).csv", filePathFull, "Reports", counter);
                counter++;
            }
            
            file = new File(fileName);
            
            FileWriter writer = new FileWriter(file, true);
            
            if (flag) {
                for (int i = 0; i < reports.length; i++) {
                    writer.write(reports[i] + ", ");
                }
                writer.write("\n");
            }
            
            for (int i = 0; i < reports.length; i++) {
                Double line = simulation_0.getReportManager().getReport(reports[i]).getReportMonitorValue();
                writer.write(line.toString() + ",");
                simulation_0.println(String.format("%s: %s", reports[i], line.toString()));
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            simulation_0.println(e.getMessage());
        }
        
        simulation_0.println(simulation_0.getReportManager().childOrder());
        
    }
}
