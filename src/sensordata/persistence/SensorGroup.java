package sensordata.persistence;

import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
/**
 * represents the a sensor group in DB
 * @author Anton Kroisant
 *
 */
@Entity("sensorGroup")
public class SensorGroup {

	@Id
	private ObjectId id;
	private String groupName;
	
//	@Reference(value = "sensors", lazy = false, idOnly = true)
//	private List<Sensor> sensors;
	


//	public List<Sensor> getSensors() {
//		return sensors;
//	}


	protected SensorGroup() {
	}
	

	public SensorGroup(String groupName){
		this.groupName=groupName;
		
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public ObjectId getId() {
		return id;
	}
	
	

}
