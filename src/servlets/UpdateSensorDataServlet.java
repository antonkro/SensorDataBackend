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
import sensordata.app.exceptions.SensorAllreadyExistException;
import sensordata.app.exceptions.SensorGroupNotFoundException;
import sensordata.app.exceptions.SensorNotFoundException;
import sensordata.app.exceptions.SensorParamAllreadyExistException;
import sensordata.app.exceptions.SensorParamNotFoundException;

@Path("/update")
public class UpdateSensorDataServlet {
	
	@POST
	@Path("/sensorParams")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public String updateSensorAttr(
			@FormParam("nameSensorAlt") String sensorNameOld,
			@FormParam("nameSensorNeu") String sensorNameNew,
			@FormParam("nameSensorGruppeAlt") String sensorGroupNameOld,
			@FormParam("nameSensorGruppeNeu") String sensorGroupNameNew,
			@FormParam("nameParameterAlt") List<String> listParameterOld,
			@FormParam("nameParameterNeu") List<String> listParameterNew	//same size as listParameterOld
	)
	{
		List<String> succeed = new ArrayList<String>();
		List<String> errors = new ArrayList<String>();
		int i=0;
		for(i=0; i<listParameterOld.size(); i++){
			if(!listParameterNew.get(i).isEmpty()){
				try{
					AppLayer.getInstance().updateParameterName(sensorNameOld, listParameterOld.get(i), listParameterNew.get(i));
					succeed.add("<p>Ich habe '" + listParameterOld.get(i) + "' erfolgreich zu '" + listParameterNew.get(i) + "' geändert.</p>");
				}catch(SensorParamNotFoundException e){
					errors.add(listParameterOld.get(i) + "<p class\"server info\">Parameter nicht gefunden!</p>");
				}catch(SensorParamAllreadyExistException e){
					errors.add(listParameterNew.get(i) + "<p class\"server info\">Parameter existiert bereits!<br>" + listParameterOld.get(i) + "wurde nicht verändert!</p>");
				}catch(SensorNotFoundException e){
					errors.add(sensorNameOld + "<p class=\"server info\">Sensor nicht gefunden</p>");
				}
			}
		}
		if(!sensorGroupNameNew.isEmpty()){
			try{				
				AppLayer.getInstance().addSensorToGroup(sensorNameOld, sensorGroupNameNew);
				succeed.add("<p>Ich habe '" + sensorNameOld + "' erfolgreich zur Gruppe '" + sensorGroupNameNew + "' hinzugefügt.</p>");
			}catch(SensorGroupNotFoundException e){
				errors.add(sensorGroupNameOld + "<p class=\"server info\">Sensorgruppe nicht gefunden</p>");
			}catch (SensorNotFoundException e) {
				errors.add(sensorNameOld + "<p class=\"server info\">Sensor nicht gefunden</p>");
				e.printStackTrace();
			}
		}
		if(!sensorNameNew.isEmpty()){
			try{
				AppLayer.getInstance().updateSensorName(sensorNameOld, sensorNameNew);
				succeed.add("<p>Ich habe '" + sensorNameOld + "' erfolgreich zu '" + sensorNameNew + "' geändert.</p>");
			}catch(SensorNotFoundException e){
				errors.add(sensorNameOld + "<p class=\"server info\">Sensor nicht gefunden</p>");
			}catch(SensorAllreadyExistException e){
				errors.add(sensorNameNew + "<p class=\"server info\">Sensor existiert bereits!<br>" + sensorGroupNameOld + "wurde nicht verändert!</p>");
			}
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
	
	@POST
	@Path("/addParameter")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public String addParameterToSensor(
			@FormParam("parameterName") List<String> newParameters,
			@FormParam("nameSensor") String nameSensor
			)
	{
		List<String> succeed = new ArrayList<String>();
		List<String> errors = new ArrayList<String>();
		int i=0;
		for(i=0; i<newParameters.size(); i++){
			try{
				AppLayer.getInstance().addParamToSensor(nameSensor, newParameters.get(i));
				succeed.add("<p>Ich habe '" + newParameters.get(i) + "' erfolgreich zum Sensor '" + nameSensor + "' hinzugefügt.</p>");
			}catch(SensorNotFoundException e){
				errors.add(nameSensor + "<p class=\"server info\">Sensor nicht gefunden</p>");
				break;
			} catch (SensorParamAllreadyExistException e) {
				errors.add(newParameters.get(i) + "<p class=\"server info\">Parameter existiert bereits für den Sensor" + nameSensor + "</p>");
			}
		}
		
		String html = "<!DOCTYPE html>"
				+ "<html lang=\"de-DE\">"
				+ "	<head>"
				+ "		<meta charset=\"utf-8\"/>"
				+ "		<link href=\"../../css/nav.css\" type=\"text/css\" rel=\"stylesheet\"/>"
				+ "		<link href=\"../../css/style.css\" type=\"text/css\" rel=\"stylesheet\"/>"
				+ "		<script type=\"text/javascript\" src=\"../../js/sensors.js\"></script>"
						+ "		<title>Sensordaten: Parameter hinzufügen</title>"
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
		
		if(!succeed.isEmpty()){
			html += "<hr>"
					+ "	<h1 class=\"Padavan\">Parameter hinzufügen erfolgreich!</h1>"
					+ "	<hr>"	
					+ "	<div class=\"payload\">"
					+ "		<h2 class=\"server\">Ich habe dem Sensor '" + nameSensor + "' folgende Parameter hinzugefügt:</h2>"
					+ "		<ul>";
			for(i=0; i<succeed.size(); i++){
				html += "<li class=\"server response\">" + succeed.get(i) + "</li>";
			}
		}
		if(!errors.isEmpty()){
			html += "<hr>"
					+ "	<h1 class=\"Padavan\">Parameter hinzufügen fehlgeschlagen!</h1>"
					+ "	<hr>"	
					+ "	<div class=\"payload\">"
					+ "		<h2 class=\"server\">Ich konnte folgende Parameter nicht zum Sensor '" + nameSensor + "' hinzufügen:</h2>"
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
