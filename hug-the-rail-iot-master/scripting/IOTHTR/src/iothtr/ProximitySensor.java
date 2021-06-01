package iothtr;

public class ProximitySensor extends Sensor{
	//Value storing the amount of sensors created;
	private static int NUM_SENSORS = 0;
	
	//The given name of the Sensor and the distance the sensor will store
	private double distance = 1001;
	private double prevDistance = 1001;
	
	//Instantiation of the class without a name.
	//Name will be automatically generated based off how many sensors were created.
	public ProximitySensor() {
		NUM_SENSORS++;
		name = "ProximitySensor" + String.valueOf(NUM_SENSORS);
	}
	
	//Instantiation of the class with a given name
	public ProximitySensor(String _name) {
		name = _name;
		NUM_SENSORS++;
	}
	
	//Method that receives and stores the value the sensor will receive
	public void receiveData(double dist) {
		prevDistance = distance;
		distance = dist;
	}
	
	//Returns the current distance the sensor is currently receiving
	public double getData() {
		return distance;
	}
	
	//Returns the distance the sensor is currently receiving
	public double getPrevData() {
		return prevDistance;
	}
}
