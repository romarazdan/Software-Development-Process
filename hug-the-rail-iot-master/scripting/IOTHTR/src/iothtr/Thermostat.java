package iothtr;

public class Thermostat extends Sensor{
	
	//Value storing the amount of sensors created;
	private static int NUM_SENSORS = 0;
	
	//The given name of the Sensor and the temperature value the sensor will store
	private double temperature;
	
	//Instantiation of the class without a name.
	//Name will be automatically generated based off how many sensors were created.
	public Thermostat() {
		NUM_SENSORS++;
		name = "Thermostat" + String.valueOf(NUM_SENSORS);
	}
	
	//Instantiation of the class with a given name;
	public Thermostat(String _name) {
		name = _name;
		NUM_SENSORS++;
	}
	
	//Method that receives and stores the value the sensor will receive
	public void receiveData(double temp) {
		temperature = temp;
	}
	
	//Returns the temperature the sensor is currently receiving
	public double getData() {
		return temperature;
	}
}
