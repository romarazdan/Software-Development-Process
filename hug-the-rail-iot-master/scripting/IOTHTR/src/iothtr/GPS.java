package iothtr;

public class GPS extends Sensor{
	//Value storing the amount of sensors created;
	private static int NUM_SENSORS = 0;
	
	//The given name of the Sensor and the coordinate values the sensor will store
	private double[] coordinates = new double[2];
	private double[] prevCoordinates = new double[2];
	
	//Instantiation of the class without a name.
	//Name will be automatically generated based off how many sensors were created.
	public GPS() {
		NUM_SENSORS++;
		name = "GPS" + String.valueOf(NUM_SENSORS);
	}
	
	//Instantiation of the class with a given name;
	public GPS(String _name) {
		name = _name;
		NUM_SENSORS++;
	}
	
	//Method that receives and stores the value the sensor will receive
	public void receiveData(double longitude, double latitude) {
		prevCoordinates[0] = coordinates[0];
		prevCoordinates[1] = coordinates[1];
		coordinates[0] = longitude;
		coordinates[1] = latitude;
	}
	
	
	//Returns the coordinates the sensor is currently receiving
	public double[] getData() {
		return coordinates;
	}
	
	//Returns the previous coordinates the sensor is received
	public double[] getPrevData() {
		return prevCoordinates;
	}
}
