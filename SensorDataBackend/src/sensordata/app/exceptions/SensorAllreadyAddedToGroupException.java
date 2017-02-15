package sensordata.app.exceptions;

import sensordata.app.utils.Constants;

public class SensorAllreadyAddedToGroupException extends Exception {
	public SensorAllreadyAddedToGroupException() {
		super(Constants.SensorAllreadyAddedToGroupException);
	}

}
