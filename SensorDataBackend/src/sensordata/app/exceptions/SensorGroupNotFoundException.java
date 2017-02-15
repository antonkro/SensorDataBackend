package sensordata.app.exceptions;

import sensordata.app.utils.Constants;

public class SensorGroupNotFoundException extends Exception {
	public SensorGroupNotFoundException() {
		super(Constants.SensorGroupNotFoundException);
	}

}
