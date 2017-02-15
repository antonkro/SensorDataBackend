package sensordata.app.exceptions;

import sensordata.app.utils.Constants;

public class SensorParamNotFoundException extends Exception {
	public SensorParamNotFoundException() {
	super(Constants.SensorParamNotFoundException);
	}
	
	public SensorParamNotFoundException(String s){
		super(s);
	}
}
