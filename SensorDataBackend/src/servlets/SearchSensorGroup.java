package servlets;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import sensordata.app.AppLayer;
import sensordata.app.exceptions.SensorGroupNotFoundException;
import sensordata.persistence.SensorGroup;

@Path("/searchSensorGroup")
public class SearchSensorGroup {
	
	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String getSensorGroup(@Context UriInfo ui){
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		try{
		SensorGroup sg = AppLayer.getInstance().getSensorGroup(queryParams.getFirst("name"));
		return(sg.getGroupName());
		}catch(SensorGroupNotFoundException e){
			return "0";
		}catch(Exception e){
			return "1";
		}
	}
	
}
