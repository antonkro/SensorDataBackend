package sensordata.app.exceptions;

import sensordata.app.utils.Constants;

public class SensorAllreadyExistException extends Exception {
	public SensorAllreadyExistException() {
		super(Constants.SensorAllreadyExistException);
	}
	public SensorAllreadyExistException(String sn){
		super(sn);
	}
}
