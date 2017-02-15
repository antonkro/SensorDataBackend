package servlets;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;

import sensordata.app.AppLayer;

import sensordata.app.exceptions.SensorNotFoundException;

import sensordata.app.utils.Triple;

@Path("/searchSensor")
public class SearchSensor {
	
	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json")
	public String getAllSensorData(@Context UriInfo ui){
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
				
		try{
			List<Object> jsonObj = new ArrayList<Object>();
			
			String sensorName = "";
			try{						
				sensorName = AppLayer.getInstance().getSensor(queryParams.getFirst("name")).getSensorName();
			}catch (SensorNotFoundException e){
				sensorName = "";
			}
			
			String sensorGroupName = "";
			try{
				sensorGroupName = AppLayer.getInstance().getGroupNameFromSensor(queryParams.getFirst("name"));
			}catch (SensorNotFoundException e){
				sensorGroupName = "";
			}
		
			List<Triple<String, String, String>> parameterTriple = AppLayer.getInstance().getAllParameterAllData(sensorName);
			jsonObj.add(sensorName);
			jsonObj.add(sensorGroupName);
			jsonObj.add(parameterTriple);
			String strJson = new Gson().toJson(jsonObj);
			return strJson;
			
		}catch (Exception e){
			return "0";
		}
	}
	
}