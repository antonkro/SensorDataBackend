function loadSensorGroups(){
	
	var xmlhttp = new XMLHttpRequest();
	
	xmlhttp.onreadystatechange= function(){
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
			var target = document.getElementById("payload");
			var echo = "";
			var strJSON = xmlhttp.responseText;
			if(strJSON == "[]"){
				echo = '<h2 class="server response">Keine Sensorgruppen gefunden</h2>'
			}else {
				var objJSON = JSON.parse(strJSON);
				echo += '<h2 class="server">Ich hab folgende Sensorgruppen gefunden:</h2><ul>';
				for(var i=0; i<objJSON.length; i++){
					echo += '<li class="server response">' + objJSON[i] + '</li>';
				}
				echo += '</ul>';
			}
			
			target.innerHTML = echo;
		}
	}
	
	xmlhttp.open("GET", "../../service/getSensorGroups", true);
	xmlhttp.send();
}

function searchSensorGroup(){
	var groupName = document.getElementById("groupNameInput").value;
	if(groupName == ""){
		var divInfo = document.createElement("div");
		divInfo.setAttribute("class", "alert info");
		divInfo.innerHTML = '<span class="closebtn" onclick="this.parentElement.style.display=\'none\';">&times;</span>' + 
			  '<strong>Hinweis!</strong><p class="server">Bitte teilen Sie mir den Namen der Sensorgruppe mit!</p>'
		document.body.appendChild(divInfo);
		return false;
	}
	
	var xmlhttp = new XMLHttpRequest();
	
	xmlhttp.onreadystatechange= function(){
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
			var target = document.getElementById("payload");
			sensorGroupName = xmlhttp.responseText;
			if(sensorGroupName == "0"){
				target.innerHTML = '<p class="server">Ich konnte keine Sensorengruppen finden!</p>';
				return;
			}
			if(sensorGroupName == "1"){
				target.innerHTML = '<p class="server response">Unbekannter Fehler</p>';
				return;
			}
			target.innerHTML = '<p class="server"><strong>Gruppenname: </strong></p> <p class="server response">' + sensorGroupName + '</p>';
		}
	}

	xmlhttp.open("GET", encodeURI("../../service/searchSensorGroup?name="+groupName), true);
	xmlhttp.send();
}

function searchSensorGroupForEdit(){
	var groupName = document.getElementById("groupNameInput").value;
	if(groupName == ""){
		var divInfo = document.createElement("div");
		divInfo.setAttribute("class", "alert info");
		divInfo.innerHTML = '<span class="closebtn" onclick="this.parentElement.style.display=\'none\';">&times;</span>' + 
			  '<strong>Hinweis!</strong><p class="server">Bitte teilen Sie mir den Namen der Sensorgruppe mit!</p>'
		document.body.appendChild(divInfo);
		return false;
	}
	
	var xmlhttp = new XMLHttpRequest();
	
	xmlhttp.onreadystatechange= function(){
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
			var target = document.getElementById("payload");
			var sensorGroupName = xmlhttp.responseText;
			if(sensorGroupName == "0"){
				target.innerHTML = '<h2 class="server">Ich konnte die Sensorgruppe nicht finden!</h2>'
					return;
			}
			if(sensorGroupName == "1"){
				target.innerHTML = '<h2 class="server">Ein unbekannter Fehler ist aufgetreten!</h2>'
					return;
			}
			target.innerHTML = '<h2 class="server">Ich habe die Gruppe gefunden!</h2>' +
				'<p class="server">Wie soll der neue Grupenname lauten?</p>' +
				'<form method="POST" action="../../service/update/sensorGroupName">' +
				'<input type="hidden" name="nameAlt" value="' + groupName + '"></input>' +
				'<input type="text" name="nameNeu" class="addTextCenter" placeholder="Neuer Gruppenname" required></input>' +
				'<input type="submit" class="addSubmitCenter" value="Grupenname ändern"></input></form>';
		}
	}
	/*
	xmlhttp.open("POST", "../../service/search	Sensor", true);
	xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded;");
	*/
	xmlhttp.open("GET", encodeURI("../../service/searchSensorGroup?name="+groupName), true);
	xmlhttp.send();
}

function addSensorGroupInput(){
	var inputForm = document.getElementById("addSensorGroupsForm");
	
	var ctr = inputForm.childElementCount;
		
	var br = document.createElement("br");
	br.setAttribute("id", ctr);
	
	var textInput = document.createElement("input");
	textInput.setAttribute("class", "addText");
	textInput.setAttribute("type", "text");
	textInput.setAttribute("placeholder", "Gruppenname");
	textInput.required = true;
	textInput.setAttribute("name", "name");
	textInput.setAttribute("id",  String(ctr));
	
	var remove = document.createElement("input")
	remove.setAttribute("class", "remClick");
	remove.setAttribute("type", "button");
	remove.setAttribute("value", "Feld entfernen");
	remove.setAttribute("onclick", "removeSensorGroupInput(this)");
	remove.setAttribute("id", String(ctr));
	
	var button = inputForm.children[inputForm.childElementCount-2];
	inputForm.insertBefore(br, button);
	inputForm.insertBefore(textInput, button);
	inputForm.insertBefore(remove, button);
}

function removeSensorGroupInput(row_element){
	for(var i=0; i<3; i++){
		var eRemove = document.getElementById(row_element.id);
		eRemove.parentNode.removeChild(eRemove);
	}
}

function changeToLoadIcon(){
	document.getElementById("payload").innerHTML = '<h2 class="server">Ich bearbeite ihre Anfrage...</h2>' +
		'<img src="../../img/ajax-loader.gif"/>';
}

function addLoadIcon(){
	document.getElementById("payload").innerHTML = '<p class="server">Ich bearbeite ihre Anfrage...</p>' +
		'<img src="../../img/ajax-loader.gif"/>';
}