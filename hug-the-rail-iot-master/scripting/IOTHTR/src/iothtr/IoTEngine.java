package iothtr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class IoTEngine {
	
	public static String CWD = System.getProperty("user.dir");
	
	private static String CURRENT_USER;
	//privilege 0 is operator
	//privilege 1 is admin
	private static int CURRENT_PRIVILEGE;
	public static String PRIV_STR[] = {"operator", "admin"};
	
	private static int TRAIN_SPEED = 0;
	public static double[] GATE_COORD = new double[2];
	
	private static Boolean IS_RUNNING; 
	
	public static Hashtable<String, Sensor> SENSORS = new Hashtable<String, Sensor>(); 
	
	private static int LOOP_TIME = 250;
	private static double WHEEL_RADIUS = 1.27;
	private static int PREVDISTDET = -1;
	private static int PREV_WEATHER = -1;
	private static int PREV_WHEELSLIP = -1;
	private static int PREV_HORN = -1;
	
	
	//Set the Current User of the IoTEngine
	private static void setCurrentUser(String username) {
		CURRENT_USER = username;
	}
	
	//Set the Current Privileges of the IoTEngine
	private static void setCurrentPrivileges(int privilege) {
		CURRENT_PRIVILEGE = privilege;
	}
	
	//get the Current User of the IoTEngine
	public static String getCurrentUser() {
		return CURRENT_USER;
	}
	
	//get the Current Privileges of the IoTEngine
	public static int getCurrentPrivilage() {
		return CURRENT_PRIVILEGE;
	}
	
	//set the speed of the train and displays it on display
	public static void setCurrentTrainSpeed(int speed) {
		TRAIN_SPEED = speed;
		Display.updateSpeed();
	}
	
	//gets the speed of the train
	public static int getCurrentTrainSpeed() {
		return TRAIN_SPEED;
	}
	
	//gets the coordinates of the next gate in longitude and latitude
	public static double[] getGateCoord() {
		return GATE_COORD;
	}
	
	//sets the coordinates of the next gate
	public static void setGateCoord(int longitude, int latitude) {
		GATE_COORD[0] = longitude;
		GATE_COORD[1] = latitude;
	}
	
	//Encrypt the string provided in the argument with SHA-512
	private static String encrypt(String plaintext) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] messageDigest = md.digest(plaintext.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashText = number.toString(16);
			while (hashText.length() < 32) {
				hashText = "0" + hashText;
			}
			return hashText;
		}catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	//checks if a user exists and authenticates the user with the 
	//given credentials
	public static boolean authenticateUser(String username, String password) {
		try {
			File file = new File(CWD + "/IOTHTR_FILES/credentials.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] credentials = line.split(":");
				if (username.equals(credentials[0]) && IoTEngine.encrypt(password).equals(credentials[1])) {
					IoTEngine.setCurrentUser(credentials[0]);
					IoTEngine.setCurrentPrivileges(Integer.parseInt(credentials[2]));
					
					fr.close();
					br.close();
					return true;
				}
			}
			br.close();
			fr.close();
			return false;
		}
		catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	//creates a new user with operator privilages
	public static Boolean createUser(String username, String password, String confPassword) {
		if (password.equals(confPassword)) {
			try {
				File file = new File(CWD + "/IOTHTR_FILES/credentials.txt");
				Writer output;
				String line = "\n" + username + ":" + encrypt(password) + ":" + "0";
				
				output = new BufferedWriter(new FileWriter(file, true));  //clears file every time
				output.append(line);
				output.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
			
			return true;
		}
		else {
			return false;
		}
	}
	
	//deletes the user with the username from the functions parameter
	public static void deleteUser(String username) {
		
		try {
			File file = new File(CWD + "/IOTHTR_FILES/credentials.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] credentials = line.split(":");
				if (credentials[0].equals(username)) {
					Scanner sc = new Scanner(file);
					StringBuffer buffer = new StringBuffer();
					while (sc.hasNextLine()) {
						buffer.append(sc.nextLine()+System.lineSeparator());
					}
					String fileContents = buffer.toString();
					sc.close();
					String oldline = line;
					String newline = "";
					fileContents = fileContents.replaceAll(oldline, newline);
					FileWriter writer = new FileWriter(file);
					writer.append(fileContents);
					writer.flush();
					writer.close();
					br.close();
					fr.close();
				}
			}
			br.close();
			fr.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//returns all of the users and their privileges in the system as an array list
	public static ArrayList<String[]> getAllUsers() {
		ArrayList<String[]> allUsers = new ArrayList<String[]>();
		
		try {
			File file = new File(CWD + "/IOTHTR_FILES/credentials.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] credentials = line.split(":");
				String[] nameAndPrivs = {credentials[0], credentials[1]};
				allUsers.add(nameAndPrivs);
			}
			br.close();
			fr.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return allUsers;
	}
	
	//changes the password of the user. also checks if the new password 
	//and confirmation password matches 
	public static Boolean changePassword(String name, String userPass, String confPass) {
		try {
			File file = new File(CWD + "/IOTHTR_FILES/credentials.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] credentials = line.split(":");
				if (credentials[0].equals(name)) {
					if (userPass.equals(confPass)) {
						Scanner sc = new Scanner(file);
						StringBuffer buffer = new StringBuffer();
						while (sc.hasNextLine()) {
					         buffer.append(sc.nextLine()+System.lineSeparator());
					    }
						String fileContents = buffer.toString();
						sc.close();
						String oldline = line;
						String newline = credentials[0] + ":" + encrypt(userPass) + ":" + credentials[2];
						fileContents = fileContents.replaceAll(oldline, newline);
						FileWriter writer = new FileWriter(file);
						writer.append(fileContents);
						writer.flush();
						writer.close();
						br.close();
						return true;
					}
					br.close();
					return false;
				}
			}
			br.close();
			fr.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//starts the IOT engine by initializing the Sensors 
	private static void startEngine() {
		Anemometer anem = new Anemometer();
		GPS gps = new GPS();
		ProximitySensor prox = new ProximitySensor();
		Tachometer tach = new Tachometer();
		Thermostat thermo = new Thermostat();
		WaterSensor watSen = new WaterSensor();
		
		SENSORS.put(anem.getSensorName(), anem);
		SENSORS.put(gps.getSensorName(), gps);
		SENSORS.put(prox.getSensorName(), prox);
		SENSORS.put(tach.getSensorName(), tach);
		SENSORS.put(thermo.getSensorName(), thermo);
		SENSORS.put(watSen.getSensorName(), watSen);
		
		LogHandler.init();
		
		IS_RUNNING = true;
	}
	
	//handles all of the code for the distance detection feature
	private static void distanceDetection(ProximitySensor prox) { 
		double prevDist = prox.getPrevData();
		double curDist = prox.getData();
		
		double convertedTrainSpeed = ((double)TRAIN_SPEED / 3.6);
		double convertedTime = ((double)LOOP_TIME / 1000);
		
		int speed = (int) (3.6 * (((curDist - prevDist) / convertedTime) + convertedTrainSpeed)); 
		
		if (prox.getData() == 1001) {
			Display.setObjectType(Display.OBJECT_NONE);
			Display.setObjectSpeed(0);
			Display.setDistanceBar(0);
		}
		else if(speed == 0) {
			Display.setObjectType(Display.OBJECT_STAND);
			Display.setObjectSpeed(speed);
			Display.setDistanceBar((int) curDist);
		}
		else {
			Display.setObjectType(Display.OBJECT_MOVING);
			Display.setObjectSpeed(speed);
			Display.setDistanceBar((int) curDist);
		}
		
		String message;
		int severity;
		
		if (curDist <= 500 && speed != 0 && PREVDISTDET != 0) {
			message = "Warning: Moving Object Detected. Please Slow Down.";
			severity = ConsoleBox.WARNING;
			Display.sendAlert(message, severity);
			PREVDISTDET = 0;
		}
		else if (curDist <= 1000 && speed == 0 && PREVDISTDET != 1) {
			message = "Warning: Stationary Object Detected. Please Brake.";
			severity = ConsoleBox.EMERGENCY;
			Display.sendAlert(message, severity);
			PREVDISTDET = 1;
		}
		
	}
	
	
	//handles all of the code for the wheel slip detection feature
	private static void detectWheelSlip(Tachometer tachometer, GPS gps) {
		double gpsDistance = getGpsDistance(gps, gps.getPrevData());
		double gpsSpeed = gpsDistance / (6.9444e-5); 
		
		double rpmSpeed = (3600/1000)* WHEEL_RADIUS * ((2*Math.PI) / 60) * tachometer.getData();  
		
		if (Math.abs(gpsSpeed - rpmSpeed) > 1.5 && PREV_WHEELSLIP != 0) {
			String message = "Warning: Wheel slippage detected. Please Slow Down.";
			int severity = ConsoleBox.WARNING;
			Display.sendAlert(message, severity);
			Display.updateSlipIcon(true);
			PREV_WHEELSLIP = 0;
		}
		else if (Math.abs(gpsSpeed - rpmSpeed) > 1.5) {
			PREV_WHEELSLIP = 0;
		}
		else {
			Display.updateSlipIcon(false);
			PREV_WHEELSLIP = 1;
		}
		

	}
	
	
	//handles all of the code to detect and display the weather type
	private static void detectWeather(WaterSensor waterSensor, Anemometer anemometer, Thermostat thermometer) {
		String message;
		int severity;
		
		if (waterSensor.getData() == true && thermometer.getData() < 0) {
			if (PREV_WEATHER != 0) {
				message = "Warning: Snow detected. Please Slow Down.";
				severity = ConsoleBox.WARNING;
				Display.sendAlert(message, severity);
			}
			Display.updateWeather(Display.WEATHER_SNOWING);
			PREV_WEATHER = 0;
		}
		else if (waterSensor.getData() == true ) {
			if (PREV_WEATHER != 1) {
				message = "Warning: Rain detected. Please Slow Down.";
				severity = ConsoleBox.WARNING;
				Display.sendAlert(message, severity);
			}
			Display.updateWeather(Display.WEATHER_RAINING);
			PREV_WEATHER = 1;
		}
		
		else if (anemometer.getData() >= 50) {
			if (PREV_WEATHER != 2) {
				message = "Warning: High winds detected. Please Slow Down.";
				severity = ConsoleBox.WARNING;
				Display.sendAlert(message, severity);
			}
			Display.updateWeather(Display.WEATHER_HIGHWINDS);
			PREV_WEATHER = 2;
		}
		
		else {
			
			if (PREV_WEATHER != 3) {
				message = "You have a sunny ride ahead of you. Have a safe trip!";
				severity = ConsoleBox.STANDARD;
				Display.sendAlert(message, severity);
			}
			Display.updateWeather(Display.WEATHER_SUNNY);
			PREV_WEATHER = 3;
		}
		
		if(thermometer.getData() < 0 && PREV_WEATHER != 4 && PREV_WEATHER != 0) {
			message = "Warning: Low Temperature. Please Slow Down.";
			severity = ConsoleBox.WARNING;
			Display.sendAlert(message, severity);
			PREV_WEATHER = 4;
		}
		
		else if(thermometer.getData() > 100 && PREV_WEATHER != 5) {
			message = "Warning: High Temperature. Please Slow Down.";
			severity = ConsoleBox.WARNING;
			Display.sendAlert(message, severity);
			PREV_WEATHER = 5;
		}
	}
	
	
	//code to determine the points between 2 gps coordinates
	private static double getGpsDistance(GPS gps, double[] point2) {
		double lon1 = gps.getData()[1];
		double lat1 = gps.getData()[0];
		double lon2 = point2[1];
		double lat2 = point2[0];
		
		double radius = 6371;
		
		double phi_1 = Math.toRadians(lat1);
		double phi_2 = Math.toRadians(lat2);
		
		double delta_phi = Math.toRadians(lat2-lat1);
		double delta_lambda = Math.toRadians(lon2-lon1);
		
		
		double a = (Math.sin(delta_phi/2.0) * Math.sin(delta_phi/2.0)) +
			    (Math.cos(phi_1) * Math.cos(phi_2) * 
			    (Math.sin(delta_lambda/2.0) * Math.sin(delta_lambda/2.0)));
		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return radius *c;
	}
	
	
	//handles all of the code for the horn feature
	private static void hornBlow(GPS gps) {
		
		double gpsDistance = getGpsDistance(gps, GATE_COORD);
		
		String message;
		int severity;
		if (gpsDistance < 0.005 && PREV_HORN != 0) {
			Display.startHornCountdown(5000);
			Display.updateHornIcon(true);
			message = "Arrived at Gate. Activate horn for 5s.";
			severity = ConsoleBox.STANDARD;
			Display.sendAlert(message, severity);
			PREV_HORN = 0;
		}
		else if (gpsDistance >= 1.5 && gpsDistance <= 1.7 && PREV_HORN != 1) {
			
			Display.startHornCountdown(15000);
			Display.updateHornIcon(true);
			message = "Gate is about 1.6 km away. Activate horn for 15s.";
			severity = ConsoleBox.STANDARD;
			Display.sendAlert(message, severity);
			PREV_HORN = 1;
		}
		
		if (gpsDistance < 1.7) {
			Display.updateGateIcon(true);
		}
		else {
			Display.updateGateIcon(false);
		}
		
	}
	

	//Main function
	public static void main(String[] args) throws InterruptedException {
		Display.loginInit();
		
		startEngine();
		while(IS_RUNNING) {
			Thread.sleep(LOOP_TIME);
			if (Display.RUNNING) {
				
				distanceDetection((ProximitySensor) SENSORS.get("ProximitySensor1"));
				
				detectWheelSlip((Tachometer) SENSORS.get("Tachometer1"), (GPS) SENSORS.get("GPS1"));
				
				detectWeather((WaterSensor) SENSORS.get("WaterSensor1"), (Anemometer)SENSORS.get("Anemometer1"), (Thermostat) SENSORS.get("Thermostat1"));
				
				hornBlow((GPS) SENSORS.get("GPS1"));
			}
		}
	}

}
