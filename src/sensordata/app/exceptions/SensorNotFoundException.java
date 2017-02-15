package sensordata.app.exceptions;

import sensordata.app.utils.Constants;

public class SensorNotFoundException extends Exception {
	public SensorNotFoundException() {
		super(Constants.SensorNotFoundException);
	}
	public SensorNotFoundException(String s){
		super(s);
	}
}
