package iothtr;

public class WaterSensor extends Sensor{
	//Value storing the amount of sensors created;
	private static int NUM_SENSORS = 0;
	
	//The given name of the Sensor and the wind value the sensor will store
	private Boolean waterValue = false;
	
	//Instantiation of the class without a name.
	//Name will be automatically generated based off how many sensors were created.
	public WaterSensor() {
		NUM_SENSORS++;
		name = "WaterSensor" + String.valueOf(NUM_SENSORS);
	}
	
	//Instantiation of the class with a given name
	public WaterSensor(String _name) {
		name = _name;
		NUM_SENSORS++;
	}
	
	//Method that receives and stores the value the sensor will receive
	public void receiveData(Boolean waterVal) {
		waterValue = waterVal;
	}
	
	
	//Returns the water value the sensor is currently receiving
	public Boolean getData() {
		return waterValue;
	}
}
