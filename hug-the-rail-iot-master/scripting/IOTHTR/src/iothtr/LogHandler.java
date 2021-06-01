package iothtr;

import java.util.Hashtable;



public class LogHandler {
	
	 public static Hashtable<String, Log> LOG_LIST = new Hashtable<String, Log>();
	 
	 public static void init() {
		 
		 LOG_LIST.put("Main_Log", new Log("Main_Log"));
		 LOG_LIST.put("Anemometer", new Log("Anemometer"));
		 LOG_LIST.put("Display", new Log("Display"));
		 LOG_LIST.put("GPS", new Log("GPS"));
		 LOG_LIST.put("IoTEngine", new Log("IoTEngine"));
		 LOG_LIST.put("OpticalSensor", new Log("OpticalSensor"));
		 LOG_LIST.put("ProximitySensor", new Log("ProximitySensor"));
		 LOG_LIST.put("Tachometer", new Log("Tachometer"));
		 LOG_LIST.put("Thermostat", new Log("Thermostat"));
		 LOG_LIST.put("Timer", new Log("Timer"));
		 LOG_LIST.put("WaterSensor", new Log("WaterSensor"));
	 }
	 
	 public static Log getLog(String logName) {
		 return LOG_LIST.get(logName);
	 }
	 
	 public static void writeLog(String logName, String message) {
		 LogHandler.getLog(logName).addEntry(message);
		 LogHandler.getLog("Main_Log").addEntry(message);
	 }

}
