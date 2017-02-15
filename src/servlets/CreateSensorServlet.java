package servlets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import sensordata.app.AppLayer;
import sensordata.app.exceptions.SensorAllreadyExistException;

@Path("/newSensors")
public class CreateSensorServlet {
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public String servletCreateSensors(@FormParam("name") List<String> list){
		List<String> errors = new ArrayList<String>();
		String responseText = "<!DOCTYPE html>"
			+ "<html lang=\"de-DE\">"
			+ "	<head>"
			+ "		<meta charset=\"utf-8\"/>"
			+ "		<link href=\"../css/nav.css\" type=\"text/css\" rel=\"stylesheet\"/>"
			+ "		<link href=\"../css/style.css\" type=\"text/css\" rel=\"stylesheet\"/>"
			+ "		<script type=\"text/javascript\" src=\"../js/sensors.js\"></script>"
					+ "		<title>Sensordaten: Sensor hinzufügen</title>"
					+ "	</head>"
					+ "	<body>"
					+ "		<div id=\"navigation\">"
							+ "			<nav>"
							+ "				<ul>"
							+ "					<li><a href=\"../index.html\">Startseite</a></li>"
									+ "					<li><a href=\"../html/overview.html\">Sensoren</a>"
											+ "						<div>"
											+ "							<ul>"
											+ "								<li><a href=\"../html/sensor/sensors.html\">Übersicht: Sensoren</a></li>"
											+ "								<li><a href=\"../html/sensor/findSensor.html\">Sensor suchen</a></li>"
											+ "								<li><a href=\"../html/sensorGroup/sensorGroups.html\">Übersicht: Sensorgruppen</a></li>"
											+ "								<li><a href=\"../html/sensorGroup/findSensorGroup.html\">Sensorgruppe suchen</a></li>"
											+ "							</ul>"
			+ "						</div>"
			+ "					<li>"
			+ "					<li><a href=\"../html/register.html\">Hinzufügen</a>"
			+ "						<div>"
			+ "							<ul>"
			+ "								<li><a href=\"../html/register/newSensor.html\">Neuer Sensor</a></li>"
			+ "								<li><a href=\"../html/register/newSensorGroup.html\">Neue Sensorgruppe</a></li>"
			+ "							</ul>"
			+ "						</div>"
			+ "					</li>"
			+ "					<li><a href=\"../html/edit.html\">Bearbeiten</a>"
			+ "						<div>"
			+ "							<ul>"
			+ "								<li><a href=\"../html/edit/sensor.html\">Sensor bearbeiten</a></li>"
			+ "								<li><a href=\"../html/edit/notConfiguredSensors.html\">Übersicht unvollständiger Sensoren</a></li>"
			+ "								<li><a href=\"../html/edit/sensorGroup.html\">Sensorgruppe bearbeiten</a></li>"
			+ "							</ul>"
			+ "						</div>"
			+ "					</li>"
			+ "				</ul>"
			+ "			</nav>"
			+ "		</div>	"
			+ "		<div class=\"content\">	"
			+ "			<a class=\"back\" href=\"javascript:history.back();\">Zurück</a> ";
		
		Iterator<String> i = list.iterator();
		while (i.hasNext()) {
			try{
				String s = i.next();
				AppLayer.getInstance().createSensor(s);
			}catch (SensorAllreadyExistException e){
				errors.add(e.getMessage() + "<p class=\"server info\">Sensor bereits vorhanden!</p>");
				i.remove();
			}catch(Exception e) {
				errors.add(i.next() + "<p class=\"server info\">Unbekannter Fehler!</p>");
				i.remove();
			}
		}
		if(!list.isEmpty()){

			responseText += "<hr>"
				+ "	<h1 class=\"Padavan\">Hinzufügen erfolgreich!</h1>"
				+ "	<hr>"	
				+ "	<div class=\"payload\">"
				+ "		<h2 class=\"server\">Ich habe folgende Sensoren hinzugefügt:</h2>"
				+ "		<ul>";
 				for(String sensor: list){
					responseText += "<li class=\"server response\">" + sensor + "</li>";
				}
 				responseText += "</ul>";
 				if(!errors.isEmpty()){
 					responseText += "<h2 class=\"server\">Folgende Sensoren konnten ich nicht hinzufügen:</h2>"
	 						+ "<ul>";
	 				for(int j=0; j<errors.size(); j++){
	 					responseText += "<li class=\"sensor\">" + errors.get(j) + "</li>";
	 				}
 				}

		}else {
			responseText += "<hr>"
					+ "	<h1 class=\"Padavan\">Hinzufügen fehlgeschlagen!</h1>"
					+ "	<hr>"	
					+ "	<div class=\"payload\">"
					+ "		<h2 class=\"server\">Ich konnte die Sensoren nicht hinzufügen!</h2> <br/>"
					+ "		<ul>";
			for(int j=0; j<errors.size(); j++){
				responseText += "<li class=\"server\">" + errors.get(j) + "</li>";
			}
		}
		responseText += "</div>"
				+ "		</div>"
				+ "	</body>"
				+ "</html>";
		
		return responseText;
	}
}
