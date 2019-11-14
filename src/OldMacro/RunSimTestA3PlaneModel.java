// STAR-CCM+ macro: RunSimTestA3PlaneModel.java
package OldMacro/*macro*/;

import star.base.neo.DoubleVector;
import star.base.neo.IntVector;
import star.base.neo.NeoObjectVector;
import star.base.report.ExpressionReport;
import star.common.*;
import star.dualmesher.DualAutoMesher;
import star.flow.ForceCoefficientReport;
import star.flow.ForceReport;
import star.flow.MomentCoefficientReport;
import star.flow.MomentReport;
import star.meshing.AutoMeshOperation;
import star.meshing.MeshOperationManager;
import star.resurfacer.ResurfacerAutoMesher;
import star.vis.Scene;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunSimTestA3PlaneModel extends StarMacro {

    // Folder path: must contain input text file; all outputs will be located here
    public static final String folder = 
            "D:\\Projects\\Tantal\\BPLA\\334model\\CFD";
    public static final String[] sceneNames = {
            "Вектора скорости",
            "Распределение скоростей",
            "Распределение давления",
            "Линии потока"};
    public static final int iterations = 2000;
    public int counter = 1;

//    public double[] arr_reports;

    public String
            folderForResults = folder + "\\Results",
            n_testName = "A3 plane model",
            n_folderToSave = "",
            n_folderToSave_2 = "",
			n_folderToSavePress = "",
            n_folderToSavePress_2 = "",
    		n_folderToSaveTables = "";
			
    /**
     * This is the main method in the macro. It is executed when the macro is
     * run in STAR-CCM+.
     */
    public void execute() {

        try {
            // Find the current simulation
            Simulation theSim =
                    getActiveSimulation();

            theSim.println("Начало всего");

            // Set up DataReader
            DataReader reader = 
                    new DataReader();

            theSim.println("Создан объект DataReader");

            // Read from input file; populate list with SimData objects
            reader.readInput(folder + "\\Test " + n_testName + " Input.csv");

            theSim.println("Входные данные прочитаны");

            // Reference to list of SimData objects (to simplify later steps)
            List<SimData> listCases = 
                    reader.getFlowDetails();

            theSim.println("Входные данные записаны в массив");

            OutputData outData =
                    new OutputData();

            theSim.println("Создан массив выходных данных");
            
            OutputData outDataPress =
                    new OutputData();  
            
            theSim.println("Создан массив выходных данных по давлению");
		
			OutputData outDataWing =
				new OutputData();
			
			theSim.println("Создан массив выходных данных для крыла");
			
			OutputData outDataFin =
				new OutputData();
	
			theSim.println("Создан массив выходных данных для киля");
	
			// Set up PostProcessor (retrieves scenes and plots which will be saved)
			PostProcessor postP =
				new PostProcessor(theSim, folderForResults
					+ "\\Test "
					+ n_testName
					+ "\\Сетка.png");
	
			theSim.println("Создан объект PostProcessor");

            // Set up DataWriter (this creates the output file and writes headings)
          /*  DataWriter writer =
                    new DataWriter(
                            folderForResults
                                    + "\\Test "
                                    + n_testName
                                    + "\\Test "
                                    + n_testName
                                    + " "
                                    + " Results.txt");

            theSim.println("Создан объект DataWriter");
            
            DataWriter writerPress = 
                    new DataWriter(
                            folderForResults
                                    + "\\Test "
                                    + n_testName
                                    + "\\Test "
                                    + n_testName
                                    + " "
                                    + " Results pressure.txt");

            theSim.println("Создан объект DataWriterPress");*/
			
			DataWriterFull writerFull = 
                    new DataWriterFull(
                            folderForResults
                                    + "\\Test "
                                    + n_testName
                                    + "\\Test "
                                    + n_testName
                                    + " "
                                    + " Results Full.txt");

            theSim.println("Создан объект DataWriterFull");

            // Set up Simrunner (retrieves various objects from the sim which will be set)
            SimRunner runner = 
                    new SimRunner(theSim);

            theSim.println("Создан объект SimRunner");

            // The following construct is a "for-each" loop...
            for (SimData sD : listCases) {

                // Print line to output window to show how far the process has reached
                theSim.println("Inside the loop. Running case with angle " + sD.getAngle());

                // Подготовка переменных для следующего цикла
                runner.varPreparation(sD);

                // Поворот рабочей СК
                runner.RotateCS(sD);

                runner.SwitchBounderies(sD);
                
                execMeshAdoptation(theSim);

                // Set various conditions, clear previous solution, run simulation for x iterations
                runner.runCase(sD, outData, outDataPress, outDataWing, outDataFin, iterations);
				
				theSim.saveState(folder + "\\" + n_testName + ".sim" ); /**"D:\\Projects\\Tantal\\BPLA\\Theory\\CFD\\Test A1 plane mod7.sim"*/
/*
                // Retrieve the drag coefficient from the SimData object and write it to file
                writer.writeDataLine(sD, outData);
                
                // Пишем в файл данные по давлению
                writerPress.writeDataLine(sD, outDataPress);
*/				
				// Пишем в файл данные общие
                writerFull.writeDataPlane(sD, outData, outDataPress);
                
                writerFull.writeDataPart(sD, outDataWing);
                
                writerFull.writeDataPart(sD, outDataFin);
                
                writerFull.writeDataSetNewLine(sD);

                n_folderToSave = 
					folderForResults 
					+ "\\Test " 
					+ n_testName 
					+ "\\Normal\\Pictures\\" 
					+ counter 
					+ " " 
					+ n_testName
					+ " "
					+ sD.getAngle();

                n_folderToSave_2 = 
					folderForResults 
					+ "\\Test " 
					+ n_testName
					+ "\\Normal\\Pictures\\";
				
                // Save hardcopies of vel mag and streamlines scenes, and residual plot
/*                postP.saveVelMagScene(
                	n_folderToSave
						+ "\\Test "
						+ n_testName
						+ " "
						+ sceneNames[1]
						+ ".png", 1
                );
*/                
                // Копия в отдельную папку с картинками
                postP.saveVelMagScene(
					n_folderToSave_2
						+ "\\"
						+ sceneNames[1]
						+ "\\"
						+ counter
						+ " Test "
						+ n_testName
						+ " "
						+ sD.getAngle()
						+ ".png", 1
                );
/*
                postP.saveVelVecScene(
					n_folderToSave
						+ "\\Test "
						+ n_testName
						+ " "
						+ sceneNames[0]
						+ ".png", 1
                );
*/                
             // Копия в отдельную папку с картинками
                postP.saveVelVecScene(
                	n_folderToSave_2
						+ "\\"
						+ sceneNames[0]
						+ "\\"
						+ counter
						+ " Test "
						+ n_testName
						+ " "
						+ sD.getAngle()
						+ ".png", 1
                );
/*
                postP.savePressScene(
					n_folderToSave
						+ "\\Test "
						+ n_testName
						+ " "
						+ sceneNames[2]
						+ ".png", 1
                );
*/                
             // Копия в отдельную папку с картинками
                postP.savePressScene(
                	n_folderToSave_2
						+ "\\"
						+ sceneNames[2]
						+ "\\"
						+ counter
						+ " Test "
						+ n_testName
						+ " "
						+ sD.getAngle()
						+ ".png", 1
                );
/*
                postP.saveStreamLinesScene(
					n_folderToSave
						+ "\\Test "
						+ n_testName
						+ " "
						+ sceneNames[3]
						+ ".png", 1
                );
*/                
             // Копия в отдельную папку с картинками
/*                postP.saveStreamLinesScene(
                	n_folderToSave_2
						+ "\\"
						+ sceneNames[3]
						+ "\\"
						+ counter
						+ " Test "
						+ n_testName
						+ " "
						+ sD.getAngle()
						+ ".png", 1
                );*/
/*
                postP.saveResidualPlot(
					n_folderToSave
						+ "\\Test "
						+ n_testName
						+ " Residuals.png"
                );
*/                
             // Копия в отдельную папку с картинками
                postP.saveResidualPlot(
                	n_folderToSave_2
						+ "\\"
						+ "Residuals"
						+ "\\"
						+ counter
						+ " Test "
						+ n_testName
						+ " "
						+ sD.getAngle()
						+ ".png"
                );

		// Сохранение картинок с данными по давлению
                n_folderToSavePress = 
					folderForResults 
					+ "\\Test " 
					+ n_testName 
					+ "\\Pressure\\Pictures\\" 
					+ counter 
					+ " " 
					+ n_testName
					+ " "
					+ sD.getAngle();

                n_folderToSavePress_2 = 
					folderForResults 
					+ "\\Test " 
					+ n_testName 
					+ "\\Pressure\\Pictures\\";
					
/*                // Save hardcopies of vel mag and streamlines scenes, and residual plot
                postP.saveVelMagScene(
                        n_folderToSavePress
                                + "\\Test "
                                + n_testName
                                + " "
                                + sceneNames[1]
                                + ".png", 2
                );
*/                
                // Копия в отдельную папку с картинками
                postP.saveVelMagScene(
                	n_folderToSavePress_2
						+ "\\"
						+ sceneNames[1]
						+ "\\"
						+ counter
						+ " Test "
						+ n_testName
						+ " "
						+ sD.getAngle()
						+ ".png", 2
                );
/*
                postP.saveVelVecScene(
                	n_folderToSavePress
						+ "\\Test "
						+ n_testName
						+ " "
						+ sceneNames[0]
						+ ".png", 2
                );
*/                
             // Копия в отдельную папку с картинками
                postP.saveVelVecScene(
                	n_folderToSavePress_2
						+ "\\"
						+ sceneNames[0]
						+ "\\"
						+ counter
						+ " Test "
						+ n_testName
						+ " "
						+ sD.getAngle()
						+ ".png", 2
                );
/*
                postP.savePressScene(
                	n_folderToSavePress
						+ "\\Test "
						+ n_testName
						+ " "
						+ sceneNames[2]
						+ ".png", 2
                );
*/                
             // Копия в отдельную папку с картинками
                postP.savePressScene(
                	n_folderToSavePress_2
						+ "\\"
						+ sceneNames[2]
						+ "\\" + counter
						+ " Test "
						+ n_testName
						+ " "
						+ sD.getAngle()
						+ ".png", 2
                );
/*
                postP.saveStreamLinesScene(
                	n_folderToSavePress
						+ "\\Test "
						+ n_testName
						+ " "
						+ sceneNames[3]
						+ ".png", 2
                );
*/                
             // Копия в отдельную папку с картинками
/*                postP.saveStreamLinesScene(
                	n_folderToSavePress_2
						+ "\\"
						+ sceneNames[3]
						+ "\\"
						+ counter
						+ " Test "
						+ n_testName
						+ " "
						+ sD.getAngle()
						+ ".png", 2
                );*/
/*
                postP.saveResidualPlot(
                	n_folderToSavePress
						+ "\\Test "
						+ n_testName
						+ " Residuals.png"
                );
*/                
             // Копия в отдельную папку с картинками
                postP.saveResidualPlot(
                	n_folderToSavePress_2
						+ "\\"
						+ "Residuals"
						+ "\\" + counter
						+ " Test "
						+ n_testName
						+ " "
						+ sD.getAngle()
						+ ".png"
                );
	
				n_folderToSaveTables = folderForResults + "\\Test " + n_testName+ "\\Tables\\";
				
				theSim.println(n_folderToSaveTables);
				
				theSim.println("Сохраняем таблицы давлений");
	
				exportPressureTable(theSim, "Fin pressure", n_folderToSaveTables, counter, sD.getAngle());
				
				exportPressureTable(theSim, "Wing pressure", n_folderToSaveTables, counter, sD.getAngle());
				
				exportPressureTable(theSim, "Plane pressure", n_folderToSaveTables, counter, sD.getAngle());

 /*              postP.saveStreamlinesScene(
                        folderForResults + "/Streamlines" + sD.getAngle() + ".sce"
                );

                // Save simulation
                theSim.saveState(
                        folderForResults + "/train" + sD.getAngle() + ".sim"
                );
  */              
                ++counter;
            }
        } catch (Exception e) {
            // Included for debugging, create a window displaying the error message
            JOptionPane.showMessageDialog(
                    null, e.toString()
            );
        }

    }

    /**
     * This class receives data from the input data file and contains getter 
     * methods which can be used to set velocity components, etc. in the 
     * simulation. The input data includes flow angle, train velocity, wind
     * velocity etc., and from these velocity components are calculated.
     */
    public class SimData {

        // Member variables
        private double m_alphaDeg = 0.0;
        private double m_velocity = 0.0;
        private double m_turbScale = 0.0;
        private double m_turbVelScale = 0.0;
        private double m_turbIntence = 0.0;

        // Constructor specifies that any SimData object requires a set of input data
        public SimData(
                double alphaDeg, 
                double velocity, 
                double turbScale,
                double turbVelScale, 
                double turbIntence) {
    /**        // Assign input parameter to member variable
            m_angDeg = angDeg;
            // Local variable to convert angles from degrees to radians
            double angRad = 
                    Math.toRadians(angDeg);
            // Calculate velocity components using input parameters
            m_velX = 
                    -1 * velWnd * Math.sin(angRad);
            m_velY = 
                    velTrn + (
                    velWnd * (
                    Math.cos(angRad)));
            m_initVelX = 
                    -1 * initVelWnd * Math.sin(angRad);
            m_initVelY = 
                    initVelTrn + (
                    initVelWnd * (
                    Math.cos(angRad))); 
	*/		
			m_alphaDeg = alphaDeg;
			m_velocity = velocity;
			m_turbScale = turbScale;
			m_turbVelScale = turbVelScale;
			m_turbIntence = turbIntence;
     	 	    }

        // Getter methods to provide access to the member variables
        public double getAngle() {
            return m_alphaDeg;            // This will be used when naming files
        }
		
		public double getAngleRad() {
           
			double angRad = 
						Math.toRadians(m_alphaDeg);
			return angRad;       	     // This will be used when naming files
        }

        public double getVelocity() {
            return m_velocity;
        }

        public double getTurbScale() {
            return m_turbScale;
        }

        public double getTurbVelScale() {
            return m_turbVelScale;
        }

        public double getTurbIntence() {
            return m_turbIntence;
        }
    }
	
    /**
     * Класс данных результатов расчета
     */
	
    public class OutputData {
		
	private double
		m_X = 0.0,
		m_Y = 0.0,
		m_Z = 0.0,
		m_K = 0.0,
		m_Cx = 0.0,
		m_Cy = 0.0,
		m_Mx = 0.0,
		m_My = 0.0,
		m_Mz = 0.0,
		m_Cmz = 0.0,
		m_Re = 0.0;
		
	// Getters
	public double getX() {
            return m_X;
        }
		
	public double getY() {
            return m_Y;
        }
	
	public double getZ() {
			return m_Z;
		}
		
	public double getCx() {
            return m_Cx;
        }
		
	public double getCy() {
            return m_Cy;
        }
		
	public double getK() {
            return m_K;
        }
	
	public double getMx() {
			return m_Mx;
		}
		
	public double getMy() {
			return m_My;
		}
		
	public double getMz() {
            return m_Mz;
        }
		
	public double getCmz() {
            return m_Cmz;
        }
		
	public double getRe() {
            return m_Re;
        }
		
	//Setters
	public void setX(double param) {
            m_X = param;
        }
		
	public void setY(double param) {
            m_Y = param;
        }
	
	public void setZ(double param) {
			m_Z = param;
		}
		
	public void setK(double param) {
            m_K = param;
        }
		
	public void setCx(double param) {
            m_Cx = param;
        }
		
	public void setCy(double param) {
            m_Cy = param;
        }
		
	public void setMx(double param) {
            m_Mx = param;
        }
	
	public void setMy(double param) {
			m_My = param;
		}
	
	public void setMz(double param) {
			m_Mz = param;
		}
		
	public void setCmz(double param) {
            m_Cmz = param;
        }
		
	public void setRe(double param) {
            m_Re = param;
        }
    }

    /**
     * This class reads data from the input file. Each line of data in the input
     * file is stored in a SimData object, and this is added to an array list of
     * SimData objects.
     */
    public class DataReader {

        // Create array list for SimData objects
        private List<SimData> m_flows = 
                new ArrayList<SimData>();
    //    private String testName = "";

        // No constructor is specified so class has default constructor

        /*
         * This is the main function in this class. The method takes the path to
         * the input file.
         */
        public void readInput(String fileToRead) {
            try {

                // Read input file: fileToRead
                FileReader fr = 
                        new FileReader(fileToRead);
                BufferedReader br = 
                        new BufferedReader(fr);
                Scanner sc = 
                        new Scanner(br);

                /*
                 * Loop through each line in input file and create a new SimData
                 * object for each set of data.
                 */
            //    testName = sc.nextString();

                while (sc.hasNextLine()) {
                    sc.nextLine();
                    if (sc.hasNextDouble()) {
                        double alpha = 
                                sc.nextDouble();
                        double vel = 
                                sc.nextDouble();
                        double turbScale = 
                                sc.nextDouble();
                        double turbVelScale = 
                                sc.nextDouble();
                        double turbIntence = 
                                sc.nextDouble();

                        SimData sd = 
                                new SimData(
                                alpha, vel, turbScale, turbVelScale, turbIntence
                                );

                        // Add SimData object to list.
                        m_flows.add(sd);
                    }
                }
            } catch (Exception e) {
                // Create a window displaying the error message.
                JOptionPane.showMessageDialog(
                        null, e.toString()
                );
            }
        }

        // Returns the reference to the list of SimData objects.
        public List<SimData> getFlowDetails() {
            return m_flows;
        }
/**
        public String getTestName() {
            return testName;
        } */
    }

    /**
     * This class is used to write an output file containing the drag coefficient
     * for each inlet flow angle.
     */
   /* public class DataWriter {

        private String m_outputFile = "";

        */
   /*
         * Constructor takes the path to the output file. It will create an
         * output file and write the table headings, then close the file.
         * The exception is not caught in the constructor, if it fails, the
         * macro will stop.
         */
   /*
        public DataWriter(String fileToWrite) {
            // Assign the input parameter to the member variable
            m_outputFile = fileToWrite;

            try {
                FileWriter fw = 
                        new FileWriter(m_outputFile);
                BufferedWriter bw = 
                        new BufferedWriter(fw);
                bw.write(
					"Alpha	" + 
					"V	" +
					"Cx	" + 
					"Cy	" + 
					"K	" + 
					"Y	" + 
					"X	" + 
					"Cmz	" + 
					"Mz"
				);
                bw.newLine();
                bw.close();
            } catch (Exception e) {
            }
        }

        */
	/*
         * Method to write the drag coefficient and cross-wind flow angle to the
         * output file.
         */
	/*
        public void writeDataLine(SimData sD, OutputData outD) {
            try {
                // Set up file writer
                FileWriter fw = 
                        new FileWriter(m_outputFile, true);
                BufferedWriter bw = 
                        new BufferedWriter(fw);
                // Write cross-wind flow angle and drag coefficient to output file
                bw.write(
					sD.getAngle() + "	" +
					sD.getVelocity() + "	" +
					outD.getCx() + "	" +
					outD.getCy() + "	" +
					outD.getK() + "	" +
					outD.getY() + "	" +
					outD.getX() + "	" +
					outD.getCmz() + "	" +
					outD.getMz()
				);
                // Move cursor to next line in file for next set of data to be added
                bw.newLine();
                // Close file
                bw.close();
            } catch (Exception e) { // No exception is displayed to the user
            }
        }
    }
	*/
    public class DataWriterFull {

        private String m_outputFile = "";

        /*
         * Constructor takes the path to the output file. It will create an
         * output file and write the table headings, then close the file.
         * The exception is not caught in the constructor, if it fails, the
         * macro will stop.
         */
        public DataWriterFull(String fileToWrite) {
            // Assign the input parameter to the member variable
            m_outputFile = fileToWrite;

            try {
                FileWriter fw = 
                        new FileWriter(m_outputFile);
                BufferedWriter bw = 
                        new BufferedWriter(fw);
                bw.write(
					"Alpha	" + 
					"V	" +
					"Cx	" + 
					"Cy	" + 
					"K	" + 
					"Y	" + 
					"X	" + 
					"Cmz	" + 
					"Mz	" +
					"Cxp	" + 
					"Cyp	" + 
					"Kp	" +
					"Cmzp	" +
					"Y_wing	" +
					"X_wing	" +
					"Z_wing	" +
					"Mx_wing	" +
					"My_wing	" +
					"Mz_wing	" +
					"Y_fin	" +
					"X_fin	" +
					"Z_fin	" +
					"Mx_fin	" +
					"My_fin	" +
					"Mz_fin	"
				);
                bw.newLine();
                bw.close();
            } catch (Exception e) {
            }
        }

        /*
         * Method to write the drag coefficient and cross-wind flow angle to the
         * output file.
         */
        public void writeDataLine(SimData sD, OutputData outD, OutputData outDp) {
            try {
                // Set up file writer
                FileWriter fw = 
                        new FileWriter(m_outputFile, true);
                BufferedWriter bw = 
                        new BufferedWriter(fw);
                // Write cross-wind flow angle and drag coefficient to output file
                bw.write(
					sD.getAngle() + "	" +
					sD.getVelocity() + "	" +
					outD.getCx() + "	" +
					outD.getCy() + "	" +
					outD.getK() + "	" +
					outD.getY() + "	" +
					outD.getX() + "	" +
					outD.getCmz() + "	" +
					outD.getMz() + "	" +
					outDp.getCx() + "	" +
					outDp.getCy() + "	" +
					outDp.getK() + "	" +
					outDp.getCmz() + "	"
				);
                // Move cursor to next line in file for next set of data to be added
                bw.newLine();
                // Close file
                bw.close();
            } catch (Exception e) { // No exception is displayed to the user
            }
        }
	
		public void writeDataPlane(SimData sD, OutputData outD, OutputData outDp) {
			try {
				// Set up file writer
				FileWriter fw =
					new FileWriter(m_outputFile, true);
				BufferedWriter bw =
					new BufferedWriter(fw);
				// Write cross-wind flow angle and drag coefficient to output file
				bw.write(
					sD.getAngle() + "	" +
						sD.getVelocity() + "	" +
						outD.getCx() + "	" +
						outD.getCy() + "	" +
						outD.getK() + "	" +
						outD.getY() + "	" +
						outD.getX() + "	" +
						outD.getCmz() + "	" +
						outD.getMz() + "	" +
						outDp.getCx() + "	" +
						outDp.getCy() + "	" +
						outDp.getK() + "	" +
						outDp.getCmz() + "	"
				);
				// Move cursor to next line in file for next set of data to be added
				//bw.newLine();
				// Close file
				bw.close();
			} catch (Exception e) { // No exception is displayed to the user
			}
		}
	
		public void writeDataPart(SimData sD, OutputData outD) {
			try {
				// Set up file writer
				FileWriter fw =
					new FileWriter(m_outputFile, true);
				BufferedWriter bw =
					new BufferedWriter(fw);
				// Write cross-wind flow angle and drag coefficient to output file
				bw.write(
					outD.getY() + "	" +
						outD.getX() + "	" +
						outD.getZ() + "	" +
						outD.getMx() + "	" +
						outD.getMy() + "	" +
						outD.getMz() + "	"
				);
				// Move cursor to next line in file for next set of data to be added
				//bw.newLine();
				// Close file
				bw.close();
			} catch (Exception e) { // No exception is displayed to the user
			}
		}
		
		public void writeDataSetNewLine(SimData sD) {
			try {
				// Set up file writer
				FileWriter fw =
					new FileWriter(m_outputFile, true);
				BufferedWriter bw =
					new BufferedWriter(fw);
				// Write cross-wind flow angle and drag coefficient to output file
				/*bw.write(
					outD.getY() + "	" +
						outD.getX() + "	" +
						outD.getZ() + "	" +
						outD.getMx() + "	" +
						outD.getMy() + "	" +
						outD.getMz() + "	" +
				);*/
				// Move cursor to next line in file for next set of data to be added
				bw.newLine();
				// Close file
				bw.close();
			} catch (Exception e) { // No exception is displayed to the user
			}
		}
		
    }	

    /**
     * This class sets various conditions in the simulation using data read in 
     * from each SimData object, clear previous solutions, run the simulation
     * and obtain a value for the drag coefficient.
     */
    public class SimRunner {

        private Simulation m_simUsed = null;
        // Instance variables - properties in simulation that will be set using SimData
//        private VelocityProfile m_initVel = null;
//        private VelocityProfile m_inflowVel = null;
        private ForceReport 
		m_forceX = null,
		m_forceY = null,
		m_forceXpress = null,
		m_forceYpress = null,
        m_forceXwing = null,
		m_forceYwing = null,
		m_forceZwing = null,
		m_forceXfin = null,
		m_forceYfin = null,
		m_forceZfin = null;
	private ForceCoefficientReport
		m_coefCx = null,
		m_coefCy = null,
		m_coefCxpress = null,
		m_coefCypress = null;
	private ExpressionReport
		m_expK = null,
		m_expKpress = null;
	private MomentCoefficientReport
		m_momentCoefCmz = null,
		m_momentCoefCmzpress = null;
	private	MomentReport
		m_momentMz = null,
		m_momentMzpress = null,
		m_momentMxwing = null,
		m_momentMywing = null,
		m_momentMzwing = null,
		m_momentMxfin = null,
		m_momentMyfin = null,
		m_momentMzfin = null;
	private String	
		n_AngleOfAttack = "Угол атаки",
		n_Velocity = "Скаляр V",
		n_CoordSys = "Вектор скорости",
		n_var1 = "Масштаб турбулентности",
		n_var2 = "Масштаб турбулентной скорости",
		n_var3 = "Интенсивность турбулентности";
	private	String
		n_Cx = "Cx", 
		n_Cy = "Cy", 
		n_X = "X", 
		n_Y = "Y", 
		n_Cmz = "Cmz", 
		n_Mz = "Mz", 
		n_K = "K";
	private	String
		np_Cx = "Cxp", 
		np_Cy = "Cyp", 
		np_X = "Xp", 
		np_Y = "Yp", 
		np_Cmz = "Cmzp", 
		np_Mz = "Mzp", 
		np_K = "Kp";
	private String
		nwing_X = "X_wing",
		nwing_Y = "Y_wing",
		nwing_Z = "Z_wing",
		nwing_Mx = "Mx_wing",
		nwing_My = "My_wing",
		nwing_Mz = "Mz_wing";
	private String
		nfin_X = "X_fin",
		nfin_Y = "Y_fin",
		nfin_Z = "Z_fin",
		nfin_Mx = "Mx_fin",
		nfin_My = "My_fin",
		nfin_Mz = "Mz_fin";
	private Region m_region = null;
	private Boundary
        m_boundaryBottom = null,
		m_boundaryTop = null;
	private PressureBoundary m_pressureBoundary = null;
	private InletBoundary m_inletBoundary = null;

        // Constructor receives the current simulation as a Simulation object
        public SimRunner(Simulation theSim) {
            m_simUsed = theSim;

            // Find velocity initial condition and assign to member variable
    /*        PhysicsContinuum physics =
                    ((PhysicsContinuum) m_simUsed
                    .getContinuumManager()
                    .getContinuum("Physics 1"));
            m_initVel = 
                    ((VelocityProfile) physics
                    .getInitialConditions()
                    .get(VelocityProfile.class));

            // Find inlet velocity and assign to member variable
            Region region = 
                    ((Region) m_simUsed
                    .getRegionManager()
                    .getRegion("trainAndTrack"));
            Boundary inlet = 
                    ((Boundary) region
                    .getBoundaryManager()
                    .getBoundary("Inflow"));
            m_inflowVel = 
                    inlet.getValues()
                    .get(VelocityProfile.class);
	*/
            // Find drag coefficient report and assign to member variable
            m_forceX = 
        	(
        	    (ForceReport) m_simUsed
        	    .getReportManager()
        	    .getReport(n_X)
        	);
					
            m_forceY = 
            (
               	(ForceReport) m_simUsed
				.getReportManager()
				.getReport(n_Y)
			);
			
            m_coefCx = 
            (
               	(ForceCoefficientReport) m_simUsed
               	.getReportManager()
               	.getReport(n_Cx)
			);
			
            m_coefCy = 
            (
				(ForceCoefficientReport) m_simUsed
				.getReportManager()
				.getReport(n_Cy)
			);
			
            m_expK = 
            (
               	(ExpressionReport) m_simUsed
               	.getReportManager()
               	.getReport(n_K)
			);
			
            m_momentCoefCmz =
			(
              	(MomentCoefficientReport) m_simUsed
               	.getReportManager()
               	.getReport(n_Cmz)
			);
			
            m_momentMz = 
            (
               	(MomentReport) m_simUsed
               	.getReportManager()
               	.getReport(n_Mz)
			);
            
         // Получаем ссылки на отчеты по давлению
            
            m_forceXpress = 
        	(
        	    (ForceReport) m_simUsed
        	    .getReportManager()
        	    .getReport(np_X)
        	);
					
            m_forceYpress = 
            (
               	(ForceReport) m_simUsed
				.getReportManager()
				.getReport(np_Y)
			);
			
            m_coefCxpress = 
            (
               	(ForceCoefficientReport) m_simUsed
              	.getReportManager()
               	.getReport(np_Cx)
			);
			
            m_coefCypress = 
            (
				(ForceCoefficientReport) m_simUsed
				.getReportManager()
				.getReport(np_Cy)
			);
			
            m_expKpress = 
            (
               	(ExpressionReport) m_simUsed
               	.getReportManager()
               	.getReport(np_K)
			);
			
            m_momentCoefCmzpress = 
            (
               	(MomentCoefficientReport) m_simUsed
               	.getReportManager()
               	.getReport(np_Cmz)
			);
			
            m_momentMzpress = 
            (
               	(MomentReport) m_simUsed
               	.getReportManager()
               	.getReport(np_Mz)
			);
        
        // Получаем ссылки на отчеты по крылу
			
			m_forceXwing =
				(
					(ForceReport) m_simUsed
						.getReportManager()
						.getReport(nwing_X)
				);
	
			m_forceYwing =
				(
					(ForceReport) m_simUsed
						.getReportManager()
						.getReport(nwing_Y)
				);
	
			m_forceZwing =
				(
					(ForceReport) m_simUsed
						.getReportManager()
						.getReport(nwing_Z)
				);
	
			m_momentMxwing =
				(
					(MomentReport) m_simUsed
						.getReportManager()
						.getReport(nwing_Mx)
				);
	
			m_momentMywing =
				(
					(MomentReport) m_simUsed
						.getReportManager()
						.getReport(nwing_My)
				);
	
			m_momentMzwing =
				(
					(MomentReport) m_simUsed
						.getReportManager()
						.getReport(nwing_Mz)
				);
	
		// Получаем ссылки на отчеты по килю
	
			m_forceXfin =
				(
					(ForceReport) m_simUsed
						.getReportManager()
						.getReport(nfin_X)
				);
	
			m_forceYfin =
				(
					(ForceReport) m_simUsed
						.getReportManager()
						.getReport(nfin_Y)
				);
	
			m_forceZfin =
				(
					(ForceReport) m_simUsed
						.getReportManager()
						.getReport(nfin_Z)
				);
	
			m_momentMxfin =
				(
					(MomentReport) m_simUsed
						.getReportManager()
						.getReport(nfin_Mx)
				);
	
			m_momentMyfin =
				(
					(MomentReport) m_simUsed
						.getReportManager()
						.getReport(nfin_My)
				);
	
			m_momentMzfin =
				(
					(MomentReport) m_simUsed
						.getReportManager()
						.getReport(nfin_Mz)
				);
        
        // Получаем ссылки на регионы и границы
            m_region =
                    m_simUsed.getRegionManager()
                    .getRegion("Region");

            m_boundaryBottom =
                    m_region.getBoundaryManager()
                    .getBoundary("Bottom");

            m_boundaryTop =
                    m_region.getBoundaryManager()
                    .getBoundary("Top");

            m_pressureBoundary =
        	 (
             	(PressureBoundary) m_simUsed.get(ConditionTypeManager.class)
             	.get(PressureBoundary.class)
             );

            m_inletBoundary =
            (
            	(InletBoundary) m_simUsed.get(ConditionTypeManager.class)
            	.get(InletBoundary.class)
			);
        }

		// Подготовка переменных
		public void varPreparation(SimData sD) {
		// Устанавливаем значение угла атаки
			ScalarGlobalParameter sGP_Angle = 
				(
					(ScalarGlobalParameter) m_simUsed.get(GlobalParameterManager.class)
					.getObject(n_AngleOfAttack)
				);

			sGP_Angle.getQuantity().setValue(sD.getAngle());

		// Устанавливаем значение скорости
			ScalarGlobalParameter sGP_Velocity = 
				(
					(ScalarGlobalParameter) m_simUsed.get(GlobalParameterManager.class)
					.getObject(n_Velocity)
				);

			sGP_Velocity.getQuantity().setValue(sD.getVelocity());

		// Устанавливаем параметр Var1
			ScalarGlobalParameter sGP_Var1 =
				(
					(ScalarGlobalParameter) m_simUsed.get(GlobalParameterManager.class)
						.getObject(n_var1)
				);
			
			sGP_Var1.getQuantity().setValue(sD.getTurbScale());

		// Устанавливаем параметр Var2
			ScalarGlobalParameter sGP_Var2 =
				(
					(ScalarGlobalParameter) m_simUsed.get(GlobalParameterManager.class)
						.getObject(n_var2)
				);
			
			sGP_Var2.getQuantity().setValue(sD.getTurbVelScale());
			
		// Устанавливаем параметр Var2
			ScalarGlobalParameter sGP_Var3 =
				(
					(ScalarGlobalParameter) m_simUsed.get(GlobalParameterManager.class)
						.getObject(n_var3)
				);
			
			sGP_Var3.getQuantity().setValue(sD.getTurbIntence());
		}
		
		// Поворот СК
		public void RotateCS(SimData sD) {
			
			LabCoordinateSystem lCS_Used = 
				m_simUsed.getCoordinateSystemManager()
				.getLabCoordinateSystem();

			CartesianCoordinateSystem cCS_Used = 
				((CartesianCoordinateSystem) lCS_Used.getLocalCoordinateSystemManager().
				getObject(n_CoordSys));
		// Обнуляем СК
			cCS_Used.setBasis0(
				new DoubleVector(new double[] {1.0, 0.0, 0.0})
			);

			cCS_Used.setBasis1(
				new DoubleVector(new double[] {0.0, 1.0, 0.0})
			);

			if (sD.getAngle() != 0) {

                // Не понятно, зачем объявляется, не используется
                    Units u_Angle =
                            m_simUsed.getUnitsManager()
                        .getPreferredUnits(
                            new IntVector(
                                new int[] {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                            )
                        );

                    Units u_Lines =
                            m_simUsed.getUnitsManager()
                        .getPreferredUnits(
                            new IntVector(
                                new int[] {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                            )
                        );
                    // Поворот на угол атаки в радианах
                    cCS_Used.getLocalCoordinateSystemManager()
                    .rotateLocalCoordinateSystems(
                        new NeoObjectVector(
                            new Object[] {cCS_Used}
                        ), new DoubleVector(
                            new double[] {0.0, 0.0, 1.0}
                        ), new NeoObjectVector(
                            new Object[] {u_Lines, u_Lines, u_Lines}
                        ), sD.getAngleRad(), lCS_Used
                    );
			}
		}

		public void SwitchBounderies(SimData sD) {

            if (sD.getAngle() < -1) {

                m_boundaryBottom.setBoundaryType(m_pressureBoundary);

                m_boundaryTop.setBoundaryType(m_inletBoundary);

            } else if (sD.getAngle() > 1) {

                m_boundaryBottom.setBoundaryType(m_inletBoundary);

                m_boundaryTop.setBoundaryType(m_pressureBoundary);

            } else /** if (sD.getAngle() == 0) */{

                m_boundaryBottom.setBoundaryType(m_inletBoundary);

                m_boundaryTop.setBoundaryType(m_inletBoundary);
            }
        }
		
        // Method to set variables, clear solution, run simulation, save drag coefficient
        public void runCase(SimData sD, OutputData outD, OutputData outDpress, OutputData outDWing, OutputData outDFin, int iterations) {
            // Print line to output window to show how far the process has reached
            m_simUsed.println("Цикл расчета для угла атаки: " + sD.getAngle()+"°");
/*
            // Local variables; values obtained from SimData object
            double initX = 
                    sD.getInitVelX();
            double initY = 
                    sD.getInitVelY();
            double velX = 
                    sD.getVelX();
            double velY = 
                    sD.getVelY();

            // Set initial velocity condition
            ((ConstantVectorProfileMethod) m_initVel
                    .getMethod())
                    .getQuantity()
                    .setComponents(initX, initY, 0.0);

            // Set inlet velocity
            ((ConstantVectorProfileMethod) m_inflowVel
                    .getMethod())
                    .getQuantity()
                    .setComponents(velX, velY, 0.0);
*/
            // Clear any previous solution
            m_simUsed.clearSolution();
	
			StepStoppingCriterion stepStoppingCriterion_0 =
				((StepStoppingCriterion) m_simUsed.getSolverStoppingCriterionManager().getSolverStoppingCriterion("Maximum Steps"));
	
			stepStoppingCriterion_0.setMaximumNumberSteps(iterations);

            // Run for x iterations
            m_simUsed.getSimulationIterator()
                    .run(iterations);

// Записываем полученные значения после расчета
	// Получаем X
            double XValue =
                    m_forceX.getReportMonitorValue();

        // Сохраняем X в OutputData
            outD.setX(XValue);
			
            double YValue =
                    m_forceY.getReportMonitorValue();

            outD.setY(YValue);
			
            double CxValue =
                    m_coefCx.getReportMonitorValue();

            outD.setCx(CxValue);
			
            double CyValue =
                    m_coefCy.getReportMonitorValue();

            outD.setCy(CyValue);
			
            double KValue =
                    m_expK.getReportMonitorValue();

            outD.setK(KValue);
			
            double CmzValue =
                    m_momentCoefCmz.getReportMonitorValue();

            outD.setCmz(CmzValue);
			
            double MzValue =
                    m_momentMz.getReportMonitorValue();

            outD.setMz(MzValue);

    // Сохраняем данные по давлению в OutputDataPress            
            
            double XValuepress =
                    m_forceXpress.getReportMonitorValue();

            outDpress.setX(XValuepress);
			
            double YValuepress =
                    m_forceYpress.getReportMonitorValue();

            outDpress.setY(YValuepress);
			
            double CxValuepress =
                    m_coefCxpress.getReportMonitorValue();

            outDpress.setCx(CxValuepress);
			
            double CyValuepress =
                    m_coefCypress.getReportMonitorValue();

            outDpress.setCy(CyValuepress);
			
            double KValuepress =
                    m_expKpress.getReportMonitorValue();

            outDpress.setK(KValuepress);
			
            double CmzValuepress =
                    m_momentCoefCmzpress.getReportMonitorValue();

            outDpress.setCmz(CmzValuepress);
			
            double MzValuepress =
                    m_momentMzpress.getReportMonitorValue();

            outDpress.setMz(MzValuepress);
            
      // Сохраняем данные по крылу
	
			double XValueWing =
				m_forceXwing.getReportMonitorValue();
	
			outDWing.setX(XValueWing);
	
			double YValueWing =
				m_forceYwing.getReportMonitorValue();
	
			outDWing.setY(YValueWing);
	
			double ZValueWing =
				m_forceZwing.getReportMonitorValue();
	
			outDWing.setZ(ZValueWing);
	
			double MxValueWing =
				m_momentMxwing.getReportMonitorValue();
	
			outDWing.setMx(MxValueWing);
	
			double MyValueWing =
				m_momentMywing.getReportMonitorValue();
	
			outDWing.setMy(MyValueWing);
	
			double MzValueWing =
				m_momentMzwing.getReportMonitorValue();
	
			outDWing.setMz(MzValueWing);
	
		// Сохраняем данные по килю
	
			double XValueFin =
				m_forceXfin.getReportMonitorValue();
	
			outDFin.setX(XValueWing);
	
			double YValueFin =
				m_forceYfin.getReportMonitorValue();
	
			outDFin.setY(YValueFin);
	
			double ZValueFin =
				m_forceZfin.getReportMonitorValue();
	
			outDFin.setZ(ZValueFin);
	
			double MxValueFin =
				m_momentMxfin.getReportMonitorValue();
	
			outDFin.setMx(MxValueFin);
	
			double MyValueFin =
				m_momentMyfin.getReportMonitorValue();
	
			outDFin.setMy(MyValueFin);
	
			double MzValueFin =
				m_momentMzfin.getReportMonitorValue();
	
			outDFin.setMz(MzValueFin);
        }
    }

    /**
     * This class exports various scenes and plots for post-processing purposes.
     * This includes pre-prepared scenes of velocity magnitude and streamlines
     * and a residual plot.
     */
    public class PostProcessor {

        private Simulation m_sim = null;
        private Scene
			m_velMag = null,
			m_velMagPress = null,
			m_pressure = null,
			m_pressurePress = null,
			m_velVectors = null,
			m_velVectorsPress = null,
			m_streamlines = null,
			m_streamlinesPress = null,
			m_mesh = null;
        private ResidualPlot m_res = null;
        private String
            ns_velMag = "Распределение скоростей",
            ns_velVectors = "Вектора скорости",
            ns_pressure = "Распределение давления",
            ns_streamlines = "Линии потока",
			ns_velMagPress = "Распределение скоростей 2",
			ns_velVectorsPress = "Вектора скорости 2",
			ns_pressurePress = "Распределение давления 2",
			ns_streamlinesPress = "Линии потока 2",
			ns_mesh = "Сетка";
		private int
			res_width = 3840,
			res_hieght = 2160;

        // Constructor receives current simulation, and finds the necessary scenes and plot
        public PostProcessor(Simulation theSim, String pathName) {
            m_sim = theSim;

            // Read velocity magnitude scene
            m_velMag =
                ((Scene) m_sim
                    .getSceneManager()
                    .getScene(ns_velMag));
	
			m_velMagPress =
				((Scene) m_sim
					.getSceneManager()
					.getScene(ns_velMagPress));

            // Read velocity vectors scene
            m_velVectors =
                    ((Scene) m_sim
                    .getSceneManager()
                    .getScene(ns_velVectors));
	
			m_velVectorsPress =
				((Scene) m_sim
					.getSceneManager()
					.getScene(ns_velVectorsPress));

            // Read pressure scene
            m_pressure =
                    ((Scene) m_sim
                    .getSceneManager()
                    .getScene(ns_pressure));
	
			m_pressurePress =
				((Scene) m_sim
					.getSceneManager()
					.getScene(ns_pressurePress));

            // Read streamlines scene
            m_streamlines =
                    ((Scene) m_sim
                    .getSceneManager()
                    .getScene(ns_streamlines));
	
			m_streamlinesPress =
				((Scene) m_sim
					.getSceneManager()
					.getScene(ns_streamlinesPress));

            // Read residuals plot
            m_res =
                    ((ResidualPlot) m_sim
                    .getPlotManager()
                    .getObject("Residuals"));
	
			m_mesh =
				((Scene) m_sim
					.getSceneManager()
					.getScene(ns_mesh));
	
			m_mesh.printAndWait(
				pathName, 1, res_width, res_hieght, true, false
			);
        }

        // Method to save scene; receives path to where the scene will be saved
        public void saveVelMagScene(String sceneToSave, int flag) {
            // Save vel mag scene to file
            switch (flag) {
            
				case 1:
					m_velMag.printAndWait(
						sceneToSave, 1, res_width, res_hieght
					);
					break;
				
				case 2:
					m_velMagPress.printAndWait(
						sceneToSave, 1, res_width, res_hieght
					);
					break;
			}
			     
        //    m_velMag.close(true);
        }

        public void saveVelVecScene(String sceneToSave, int flag) {
            // Save vel mag scene to file
			switch (flag) {
				
				case 1:
					m_velVectors.printAndWait(
						sceneToSave, 1, res_width, res_hieght
					);
					break;
					
				case 2:
					m_velVectorsPress.printAndWait(
						sceneToSave, 1, res_width, res_hieght
					);
					break;
			}
        
        //    m_velVectors.close(true);
        }

        public void savePressScene(String sceneToSave, int flag) {
            // Save vel mag scene to file
			switch (flag) {
				
				case 1:
					m_pressure.printAndWait(
						sceneToSave, 1, res_width, res_hieght
					);
					break;
					
				case 2:
					m_pressurePress.printAndWait(
						sceneToSave, 1, res_width, res_hieght
					);
					break;
			}
        
        //    m_pressure.close(true);
        }

        public void saveStreamLinesScene(String sceneToSave, int flag) {
            // Save vel mag scene to file
			switch (flag) {
				
				case 1:
					m_streamlines.printAndWait(
						sceneToSave, 1, res_width, res_hieght
					);
					break;
					
				case 2:
					m_streamlinesPress.printAndWait(
						sceneToSave, 1, res_width, res_hieght
					);
			}

            // Save streamlines scene as a STAR-View+ file
        /**    m_streamlines.export3DSceneFileAndWait(
                    sceneToSave, true
            );*/
            // Close streamlines scene
        //    m_streamlines.close(true);
        }

        // Method to save scene; receives path to where the scene will be saved
    /**    public void saveStreamlinesScene(String sceneToSave) {
            // Save streamlines scene as a STAR-View+ file
            m_streamlines.export3DSceneFileAndWait(
                    sceneToSave, true
            );
            // Close streamlines scene
            m_streamlines.close(true);
        }
    */
        // Method to save plot; receives path to where the plot will be saved
        public void saveResidualPlot(String plotToSave) {
            // Save residual plot as png file
            m_res.encode(
                    plotToSave, "png", res_width, res_hieght
            );
        }
    }
    
    public static void execMeshAdoptation (Simulation UsedSim){
			
		AutoMeshOperation autoMeshOperation_0 =
			((AutoMeshOperation) UsedSim.get(MeshOperationManager.class).getObject("Automated Mesh"));
	
		DualAutoMesher dualAutoMesher_0 =
			((DualAutoMesher) autoMeshOperation_0.getMeshers().getObject("Polyhedral Mesher"));
	
		dualAutoMesher_0.setMeshSizeTable(null);
	
		ResurfacerAutoMesher resurfacerAutoMesher_0 =
			((ResurfacerAutoMesher) autoMeshOperation_0.getMeshers().getObject("Surface Remesher"));
	
		resurfacerAutoMesher_0.setMeshSizeTable(null);
	
		autoMeshOperation_0.execute();
	
		Solution solution_0 =
			UsedSim.getSolution();
	
		solution_0.clearSolution(Solution.Clear.History, Solution.Clear.Fields, Solution.Clear.LagrangianDem);
	
		StepStoppingCriterion stepStoppingCriterion_0 =
			((StepStoppingCriterion) UsedSim.getSolverStoppingCriterionManager().getSolverStoppingCriterion("Maximum Steps"));
	
		stepStoppingCriterion_0.setMaximumNumberSteps(200);
	
		ResidualPlot residualPlot_0 =
			((ResidualPlot) UsedSim.getPlotManager().getPlot("Residuals"));
	
		residualPlot_0.open();
	
		UsedSim.getSimulationIterator().runAutomation();
	
		XyzInternalTable xyzInternalTable_0 =
			((XyzInternalTable) UsedSim.getTableManager().getTable("MeshSizeRefinement"));
	
		xyzInternalTable_0.extract();
	
		dualAutoMesher_0.setMeshSizeTable(xyzInternalTable_0);
	
		resurfacerAutoMesher_0.setMeshSizeTable(xyzInternalTable_0);
	
		autoMeshOperation_0.execute();
	
//		stepStoppingCriterion_0.setMaximumNumberSteps(2000);
	
		/*UsedSim.getSimulationIterator().runAutomation();*/
	}
	
	public static void exportPressureTable (Simulation theSim, String tableName, String filePath, int counter, double sDgetAngle){
		
		XyzInternalTable xyzIT_table =
			((XyzInternalTable) theSim.getTableManager().getTable(tableName));
		
		xyzIT_table.extract();
		
		String filePathFull = filePath + "\\" + tableName + "\\";
//		String fileName = filePath + tableName + ".csv";
		String fileName = filePathFull + counter + ". " + tableName + " угол атаки " + sDgetAngle + ".csv";
		
		File dirForTables = new File(filePathFull);
//		File fileForTable = new File(fileName);
		
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
}
