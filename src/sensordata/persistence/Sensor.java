package sensordata.persistence;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
/**
 * represents the Sensor in DB
 * @author Anton Kroisant
 *
 */
@Entity(value="sensor", noClassnameStored = true)
public class Sensor {



	@Id
	private ObjectId id;
	
	@Reference(value="sensorGroup" ,lazy=false,idOnly=true)
	private SensorGroup sensorGroup;
	

	public SensorGroup getSensorGroup() {
		return sensorGroup;
	}

	private String sensorName;

//	@Embedded
//	private List<SensorParam> sensorParams;

	protected Sensor() {
	}

	public Sensor(String sensorname) {
		this.sensorName = sensorname;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public ObjectId getId() {
		return id;
	}
}
