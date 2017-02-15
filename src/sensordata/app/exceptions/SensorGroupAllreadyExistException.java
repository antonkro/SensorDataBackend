package sensordata.app.exceptions;

import sensordata.app.utils.Constants;

public class SensorGroupAllreadyExistException extends Exception {
	public SensorGroupAllreadyExistException() {
		super(Constants.SensorGroupAllreadyExistException);
	}
	
	public SensorGroupAllreadyExistException(String s) {
		super(s);
	}

}
