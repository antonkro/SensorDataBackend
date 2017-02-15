package servlets;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

import sensordata.app.AppLayer;
import sensordata.app.exceptions.SensorNotFoundException;

@Path("/getSensors")
public class GetSensorDataServlet {

	@GET
	@Produces("application/json")
	public String getAllSensors(){
		List<String> sensors = AppLayer.getInstance().getAllSensorNames();
		String strJSON = new Gson().toJson(sensors);
		return strJSON;
	}
	
	@GET
	@Path("/notConfigured")
	@Produces("application/json")
	public String getAllNotConfiguredSensors(){
		List<String> sensors = AppLayer.getInstance().getAllSensorNames();
		Iterator<String> i = sensors.iterator();
		while(i.hasNext()){
			try{
				String s = i.next();
				if(!(AppLayer.getInstance().getSensor(s).getSensorGroup().getGroupName().isEmpty())){
					i.remove();
				}
				
			}catch (SensorNotFoundException e){
				
			}catch (Exception e){
				
			}
				
		}
		String strJSON = new Gson().toJson(sensors);
		return strJSON;
	}
	
	
	
}
