package sensordata.app.exceptions;

import sensordata.app.utils.Constants;

public class SensorParamAllreadyExistException extends Exception {
	public SensorParamAllreadyExistException() {
	super(Constants.SensorParamAllreadyExistException);
	}
	
	public SensorParamAllreadyExistException(String s){
		super(s);
	}
	

}
