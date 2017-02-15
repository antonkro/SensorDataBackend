package servlets;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

import sensordata.app.AppLayer;

@Path("/getSensorGroups")
public class GetSensorGroupDataServlet {

	@GET
	@Produces("application/json")
	public String getAllSensorGroups(){
		List<String> sensorgroups = AppLayer.getInstance().getAllSensorGroupNames();
		String strJSON = new Gson().toJson(sensorgroups);
		return strJSON;
	}
	
	
	
}
