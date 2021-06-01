package iothtr;

public class Anemometer extends Sensor {
	//Value storing the amount of sensors created;
	private static int NUM_SENSORS = 0;
		
	//The given name of the Sensor and the wind value the sensor will store
	private double windSpeed;
		
	//Instantiation of the class without a name.
	//Name will be automatically generated based off how many sensors were created.
	public Anemometer() {
		NUM_SENSORS++;
		name = "Anemometer" + String.valueOf(NUM_SENSORS);
	}
		
	//Instantiation of the class with a given name
	public Anemometer(String _name) {
		name = _name;
		NUM_SENSORS++;
	}
		
	//Method that receives and stores the value the sensor will receive
	public void receiveData(double wind) {
		windSpeed = wind;
	}
		
		
	//Returns the wind speed the sensor is currently receiving
	public double getData() {
		return windSpeed;
	}
}
