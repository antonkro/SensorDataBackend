function loadSensors(){
	var xmlhttp = new XMLHttpRequest();
	
	xmlhttp.onreadystatechange= function(){
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
			var target = document.getElementById("payload");
			var echo = "";
			var strJSON = xmlhttp.responseText;
			if(strJSON == "[]"){
				echo = '<h2 class="server response">Keine Sensoren gefunden</h2>'
			}else {
				var objJSON = JSON.parse(strJSON);
				echo += '<h2 class="server">Ich hab folgende Sensoren gefunden:</h2><ul>';
				for(var i=0; i<objJSON.length; i++){
					echo += '<li class="server response">' + objJSON[i] + '</li>';
				}
				echo += '</ul>';
			}
			
			target.innerHTML = echo;
		}
	}
	
	xmlhttp.open("GET", "../../service/getSensors", true);
	xmlhttp.send();
}

function loadNotConfiguredSensors(){
	var xmlhttp = new XMLHttpRequest();
	
	xmlhttp.onreadystatechange= function(){
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
			var target = document.getElementById("payload");
			var echo = "";
			var strJSON = xmlhttp.responseText;
			if(strJSON == "[]"){
				echo = '<h2 class="server response">Keine Sensoren gefunden</h2>'
			}else {
				var objJSON = JSON.parse(strJSON);
				echo += '<h2 class="server">Ich hab folgende Sensoren gefunden:</h2><ul>';
				for(var i=0; i<objJSON.length; i++){
					echo += '<li class="server response">' + objJSON[i] + '</li>';
				}
				echo += '</ul>';
			}
			
			target.innerHTML = echo;
		}
	}
	
	xmlhttp.open("GET", "../../service/getSensors/notConfigured", true);
	xmlhttp.send();
}

function searchSensor(){
	var sensorName = document.getElementById("sensorNameInput").value;
	if(sensorName == ""){
		var divInfo = document.createElement("div");
		divInfo.setAttribute("class", "alert info");
		divInfo.innerHTML = '<span class="closebtn" onclick="this.parentElement.style.display=\'none\';">&times;</span>' + 
			  '<strong>Hinweis!</strong><p class="server">Bitte teilen Sie mir den Namen des Sensors mit!</p>'
		document.body.appendChild(divInfo);
		return false;
	}
	
	var xmlhttp = new XMLHttpRequest();
	
	xmlhttp.onreadystatechange= function(){
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
			var target = document.getElementById("payload");
			var echo = "";
			var parameterName = "";
			var sensorName = "";
			var ctr_values = 1;
			var strJson = xmlhttp.responseText;
			console.log(strJson);
			if(strJson == "0"){
				echo = '<p class="server">Ich konnte den angegebenen Sensor nicht finden!</p>';
			}else{
				var objJson = JSON.parse(strJson);
				echo = '<table><tr>' +
					'<th class="server">Sensorname</th>' +
					'<td>' + objJson[0] + '</td>' + 
					'</tr><tr><th class="server">Sensorgruppe</th>' + 
					'<td>' + objJson[1] + '</td></tr><tr>';;
				for(var i=0; i<objJson[2].length; i++){
					if(objJson[2][i].first != parameterName){
						ctr_values = 1;
						echo += '<td>Messungen</td><th class="server">' + objJson[2][i].first + '</th></tr><tr>';
						parameterName = objJson[2][i].first;
						if(objJson[2][i].second === null){
							echo += '<td class="server"></td><td class="server">' + objJson[2][i].second + '</td>';
						}else{
							echo += '<td class="server">#' + ctr_values + '</td><td class="server">' + objJson[2][i].second + '</td>';
						}
						echo += '<td class="server">' + objJson[2][i].third + '</td></tr><tr>';
					}else{
						ctr_values += 1;
						echo += '<td class="server">#' + ctr_values + '</td><td class="server">' + objJson[2][i].second + '</td>';
						echo += '<td class="server">' + objJson[2][i].third + '</td></tr><tr>';
					}
				}
				echo += '</tr></table>';
			}
			target.innerHTML = echo;
		}
	}
	xmlhttp.open("GET", encodeURI("../../service/searchSensor?name="+sensorName), true);
	xmlhttp.send();
}

function searchSensorForEdit(){
	var sensorName = document.getElementById("sensorNameInput").value;
	if(sensorName == ""){
		var divInfo = document.createElement("div");
		divInfo.setAttribute("class", "alert info");
		divInfo.innerHTML = '<span class="closebtn" onclick="this.parentElement.style.display=\'none\';">&times;</span>' + 
			  '<strong>Hinweis!</strong><p class="server">Bitte teilen Sie mir den Namen des Sensors mit!</p>'
		document.body.appendChild(divInfo);
		return false;
	}
	
	var xmlhttp = new XMLHttpRequest();
	
	xmlhttp.onreadystatechange= function(){
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
			var target = document.getElementById("payload");
			var echo = "";
			var parameterName = "";
			var sensorName = "";
			var strJson = xmlhttp.responseText;
			if(strJson == "0"){
				echo = '<p class=server>Ein unbekannter Fehler ist aufgetreten!</p>'
			}
			var objJson = JSON.parse(strJson);
			echo = '<h2 class="server">Welche Änderungen möchten Sie vornehmen?</h2>' +
				'<form method="POST" action="../../service/update/sensorParams"><table><tr>' +
				'<th class="serverRes">Sensorname</th>' +
				'<td>' + objJson[0] + '</td>' +
				'<td><input type="hidden" name="nameSensorAlt" value="' + objJson[0] + '"></input>' +
				'<input type="text" name="nameSensorNeu" class="addTextCenter" placeholder="Neuer Sensorname"></input></td>' +
				'</tr><tr><th class="serverRes">Sensorgruppe</th>' + 
				'<td>' + objJson[1] + '</td>' + 
				'<td><input type="hidden" name="nameSensorGruppeAlt" value="' + objJson[1] + '"></input>' +
				'<input type="text" name="nameSensorGruppeNeu" class="addTextCenter" placeholder="Neuer Gruppenname"></input></td></tr><tr>';;
				
			for(var i=0; i<objJson[2].length; i++){
				if(objJson[2][i].first != parameterName){
					echo += '<th class="serverRes">Parameter Bezeichnung</th><td>' + objJson[2][i].first + '</td>' +
					'<td><input type="hidden" name="nameParameterAlt" value="' + objJson[2][i].first + '"></input>' +
					'<input type="text" name="nameParameterNeu" class="addTextCenter" placeholder="Neue Parameter Bezeichnung"></input></td>' +
					'</tr><tr>';
					parameterName = objJson[2][i].first;
				}
			}
			echo += '<td></td><td></td><td><input type="submit" class="addSubmitCenter" value="Änderungen übernehmen"></input></td></tr></table></form>';
			
			/*Add Parameter Form*/
			echo += '<h2 class="server">Ich kann dem Sensor auch neue Mess-Parameter hinzufügen!<h2>'
			echo += '<form id="addParameterToSensorForm" method="POST" action="../../service/update/addParameter">' +
				'<input type="hidden" name="nameSensor" value="' + objJson[0] + '"></input>' +
				'<input class="addTextCenter" type="text" name="parameterName" placeholder="Parameterbezeichnung"></input>' +
				'<input class="addClickCenter" type="button" value="Parameter hinzufügen" onclick="addParameterInput();"/>' +
				'<input type="submit" class="addSubmitCenter" value="Parameter hinzufügen"></input></form>';
			target.innerHTML = echo;
		}
	}
	xmlhttp.open("GET", "../../service/searchSensor?name="+sensorName, true);
	xmlhttp.send();
}

function addParameterInput(){
	var inputForm = document.getElementById("addParameterToSensorForm");
	
	var br = document.createElement("br");
	
	var textInput = document.createElement("input");
	textInput.setAttribute("class", "addTextCenter");
	textInput.setAttribute("type", "text");
	textInput.setAttribute("placeholder", "Parameterbezeichnung");
	textInput.setAttribute("name", "parameterName");
	
	var button = inputForm.children[inputForm.childElementCount-2];
	inputForm.insertBefore(br, button);
	inputForm.insertBefore(textInput, button);
}

function addSensorInput(){
	var inputForm = document.getElementById("addSensorsForm");
	
	var ctr = inputForm.childElementCount;
		
	var br = document.createElement("br");
	br.setAttribute("id", ctr);
	
	var textInput = document.createElement("input");
	textInput.setAttribute("class", "addText");
	textInput.setAttribute("type", "text");
	textInput.setAttribute("placeholder", "Sensorname");
	textInput.required = true;
	textInput.setAttribute("name", "name");
	textInput.setAttribute("id",  String(ctr));
	
	var remove = document.createElement("input")
	remove.setAttribute("class", "remClick");
	remove.setAttribute("type", "button");
	remove.setAttribute("value", "Feld entfernen");
	remove.setAttribute("onclick", "removeSensorInput(this)");
	remove.setAttribute("id", String(ctr));
	
	var button = inputForm.children[inputForm.childElementCount-2];
	inputForm.insertBefore(br, button);
	inputForm.insertBefore(textInput, button);
	inputForm.insertBefore(remove, button);
}

function removeSensorInput(row_element){
	for(var i=0; i<3; i++){
		var eRemove = document.getElementById(row_element.id);
		eRemove.parentNode.removeChild(eRemove);
	}
}

function changeToLoadIcon(){
	document.getElementById("payload").innerHTML += '<h2 class="server">Ich bearbeite ihre Anfrage...</h2>' +
		'<img src="../../img/ajax-loader.gif"/>';
}

function addLoadIcon(){
	document.getElementById("payload").innerHTML = '<p class="server">Ich bearbeite ihre Anfrage...</p>' +
		'<img src="../../img/ajax-loader.gif"/>';
}