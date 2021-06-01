package iothtr;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
	
	public static String LOG_DIR = IoTEngine.CWD + "/IOTHTR_FILES/LOGS/";

	public String logName;
	public File logFile; 

    public Log(String _logName) {
        logName = _logName;
        createNewLog(logName);
    }
    
    public void addEntry(String message) {
    	try {
    		File file = logFile; 
	        FileWriter writer = new FileWriter(file, true);
	        BufferedWriter bw = new BufferedWriter(writer);
	        Date currentTime = new Date();
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	        bw.write(formatter.format(currentTime) + ": " + message + "\n");
	        bw.close();
    	}
        
        catch (IOException e){
            System.err.println("An error occured");
        }
    }
    
    public void clearLog() {
        try {
        	File file = logFile; 
	        FileWriter writer = new FileWriter(file, false);
	        writer.close();
        }
        catch (IOException e) {
            System.err.println("An error occured");
        } 
    }
    
    public ArrayList<String> logAsArrLst() {
    	try {
	    	File file = logFile; 
	    	Scanner sc = new Scanner(file);
			ArrayList<String> output = new ArrayList<String>();
			while (sc.hasNextLine()) {
		         output.add(sc.nextLine()+System.lineSeparator());
		    }
			sc.close();
			return output;
    	}
        
        catch (IOException e){
            System.err.println("An error occured");
        }
    	return null;
    }
    

    //creates a new log
    public void createNewLog(String logName) {
        try {
            String fullLogName = logName + ".log";
            logFile = new File(LOG_DIR + fullLogName);
            if (logFile.createNewFile()) {
            	if (logName == "Main_Log") {
            		this.addEntry(logName + " created.");
            	}
            	else {
            		LogHandler.getLog("Main_Log").addEntry(logName + " created.");
            		this.addEntry(logName + " created.");
            	}
            }
        } 
        catch (IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
        }
    }
}