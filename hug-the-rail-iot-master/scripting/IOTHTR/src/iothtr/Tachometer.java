package iothtr;

public class Tachometer extends Sensor{
	//Value storing the amount of sensors created;
	private static int NUM_SENSORS = 0;
	
	//The given name of the Sensor and the RPM the sensor will store
	private double RPM;
	
	//Instantiation of the class without a name.
	//Name will be automatically generated based off how many sensors were created.
	public Tachometer() {
		NUM_SENSORS++;
		name = "Tachometer" + String.valueOf(NUM_SENSORS);
	}
	
	//Instantiation of the class with a given name
	public Tachometer(String _name) {
		name = _name;
		NUM_SENSORS++;
	}
	
	//Method that receives and stores the value the sensor will receive
	public void receiveData(double rpm) {
		RPM = rpm;
	}
	
	
	//Returns the RPM the sensor is currently receiving
	public double getData() {
		return RPM;
	}
}
