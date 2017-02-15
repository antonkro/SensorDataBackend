package sensordata.persistence;

import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
/**
 * represents sensor data in DB
 * @author Anton Kroisant
 *
 */
@Entity(value = "sensorData", noClassnameStored = true)
public class SensorData {

	@Id
	private ObjectId id;
	private BSONTimestamp timestamp;
	private String data;

	@Reference(value = "sensorParamId", lazy = false, idOnly = true)
	private SensorParam sensorParam;

	protected SensorData() {
	}

	public SensorData(int currentTimeMillis, String data, SensorParam sensorParam) {
		this.setTimestamp(new BSONTimestamp(currentTimeMillis, 1));
		this.data = data;
		this.sensorParam = sensorParam;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ObjectId getId() {
		return id;
	}

	public BSONTimestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(BSONTimestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString(){
		String s="";
		s+=timestamp.toString()+": ";
		s+=data;
		return s;
		
	}

}
