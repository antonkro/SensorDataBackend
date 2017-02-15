package servlets;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import sensordata.app.AppLayer;
import sensordata.app.exceptions.SensorNotFoundException;
import sensordata.app.exceptions.SensorParamNotFoundException;

@Path("/push")
public class PushDataFromSensorServlet {
	
	@POST
	@Path("/singleSensor/singleParameter")
	@Consumes("application/x-www-form-urlencoded")
	public void singleSensorSingleParam(@FormParam("strJSON") String strJSON){
		/*JSON = {"sensorName":"<String>", "paramName":"<String>", "data":"<String>, "TimeStamp(=Millisec from null)":"<int>"}*/
		JsonParser parser = new JsonParser();
		JsonObject objJSON = parser.parse(strJSON).getAsJsonObject();

		try{
			String sensorName = objJSON.get("sensorName").getAsString();
			String paramName = objJSON.get("paramName").getAsString();
			String data = objJSON.get("data").getAsString();
			int millis = objJSON.get("TimeStamp").getAsInt();
			
			
			

				try {
					AppLayer.getInstance().pushData(sensorName, paramName, data, millis);
				} catch (SensorNotFoundException | SensorParamNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}catch(Exception e){
			/*TODO debug loggin*/
		}
	}
	
	@POST
	@Path("/singleSensor/multipleParameter")
	@Consumes("application/x-www-form-urlencoded")
	public void singleSensorMultipleParameter(@FormParam("strJSON") String strJSON){
		/*JSON = {"sensorName":"<String>", "paramName":"<String>", "data":"<JsonArray>, "TimeStamp(=Millisec from null)":"<int>"}*/
		JsonParser parser = new JsonParser();
		JsonObject objJSON = parser.parse(strJSON).getAsJsonObject();

		try{
			String sensorName = objJSON.get("sensorName").getAsString();
			String paramName = objJSON.get("paramName").getAsString();
			JsonArray data = objJSON.get("data").getAsJsonArray();
			int millis = objJSON.get("TimeStamp").getAsInt();
			for(int i=0; i<data.size(); i++){
				try {
					AppLayer.getInstance().pushData(sensorName, paramName, data.get(i).getAsString(), millis);
				} catch (SensorNotFoundException | SensorParamNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			/*TODO debug loggin*/
		}
	}
	
	@POST
	@Path("/multipleSensor/multipleParameter")
	@Consumes("application/x-www-form-urlencoded")
	public void multipleSensorMultipleParameter(@FormParam("strJSON") String strJSON){
		/*JSON = {"sensorName":"<JsonArray>", "paramName":"<String>", "data":"<JsonArray>, "TimeStamp(=Millisec from null)":"<int>"}*/
		/*TODO: schreib es in die DOKU*/
	}

}
