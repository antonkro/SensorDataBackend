package servlets;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import sensordata.app.AppLayer;
import sensordata.app.exceptions.SensorGroupNotFoundException;

@Path("/update")
public class UpdateSensorGroupDataServlet {
	@POST
	@Path("/sensorGroupName")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public String postUpdateSensorGroupName(
			@FormParam("nameAlt") String nameAlt,
			@FormParam("nameNeu") String nameNeu
	)
	{	
		List<String> succeed = new ArrayList<String>();
		List<String> errors = new ArrayList<String>();
		
		try{
			AppLayer.getInstance().updateSensorGroupName(nameAlt, nameNeu);
			succeed.add("<p>Ich habe die Gruppe '" + nameAlt + "' erfolgreich zu '" + nameNeu + "' umbenannt.</p>");
		}catch(SensorGroupNotFoundException e){
			errors.add(nameAlt + "<p class=\"server info\">Sensorgruppe nicht gefunden</p>");
		}catch(Exception e){
			
		}
		
		String html = "<!DOCTYPE html>"
				+ "<html lang=\"de-DE\">"
				+ "	<head>"
				+ "		<meta charset=\"utf-8\"/>"
				+ "		<link href=\"../../css/nav.css\" type=\"text/css\" rel=\"stylesheet\"/>"
				+ "		<link href=\"../../css/style.css\" type=\"text/css\" rel=\"stylesheet\"/>"
				+ "		<script type=\"text/javascript\" src=\"../../js/sensors.js\"></script>"
						+ "		<title>Sensordaten: Updaten</title>"
						+ "	</head>"
						+ "	<body>"
						+ "		<div id=\"navigation\">"
								+ "			<nav>"
								+ "				<ul>"
								+ "					<li><a href=\"../../index.html\">Startseite</a></li>"
										+ "					<li><a href=\"../../html/overview.html\">Sensoren</a>"
												+ "						<div>"
												+ "							<ul>"
												+ "								<li><a href=\"../../html/sensor/sensors.html\">Übersicht: Sensoren</a></li>"
												+ "								<li><a href=\"../../html/sensor/findSensor.html\">Sensor suchen</a></li>"
												+ "								<li><a href=\"../../html/sensorGroup/sensorGroups.html\">Übersicht: Sensorgruppen</a></li>"
												+ "								<li><a href=\"../../html/sensorGroup/findSensorGroup.html\">Sensorgruppe suchen</a></li>"
												+ "							</ul>"
				+ "						</div>"
				+ "					<li>"
				+ "					<li><a href=\"../../html/register.html\">Hinzufügen</a>"
				+ "						<div>"
				+ "							<ul>"
				+ "								<li><a href=\"../../html/register/newSensor.html\">Neuer Sensor</a></li>"
				+ "								<li><a href=\"../../html/register/newSensorGroup.html\">Neue Sensorgruppe</a></li>"
				+ "							</ul>"
				+ "						</div>"
				+ "					</li>"
				+ "					<li><a href=\"../../html/edit.html\">Bearbeiten</a>"
				+ "						<div>"
				+ "							<ul>"
				+ "								<li><a href=\"../../html/edit/sensor.html\">Sensor bearbeiten</a></li>"
				+ "								<li><a href=\"../../html/edit/notConfiguredSensors.html\">Übersicht unvollständiger Sensoren</a></li>"
				+ "								<li><a href=\"../../html/edit/sensorGroup.html\">Sensorgruppe bearbeiten</a></li>"
				+ "							</ul>"
				+ "						</div>"
				+ "					</li>"
				+ "				</ul>"
				+ "			</nav>"
				+ "		</div>	"
				+ "		<div class=\"content\">	"
				+ "			<a class=\"back\" href=\"javascript:history.back();\">Zurück</a> ";
		int i=0;
		if(!succeed.isEmpty()){
			html += "<hr>"
					+ "	<h1 class=\"Padavan\">Änderungen erfolgreich!</h1>"
					+ "	<hr>"	
					+ "	<div class=\"payload\">"
					+ "		<h2 class=\"server\">Ich habe folgende Änderungen vorgenommen:</h2>"
					+ "		<ul>";
			for(i=0; i<succeed.size(); i++){
				html += "<li class=\"server response\">" + succeed.get(i) + "</li>";
			}
		}
		if(!errors.isEmpty()){
			html += "<hr>"
					+ "	<h1 class=\"Padavan\">Änderungen fehlgeschlagen!</h1>"
					+ "	<hr>"	
					+ "	<div class=\"payload\">"
					+ "		<h2 class=\"server\">Ich konnte folgende Änderungen nicht durchführen:</h2>"
					+ "		<ul>";
			for(i=0; i<errors.size(); i++){
				html += "<li class=\"server response\">" + errors.get(i) + "</li>";
			}
		}
		html += "</div>"
				+ "		</div>"
				+ "	</body>"
				+ "</html>";
		
		return html;
		
	}
}
