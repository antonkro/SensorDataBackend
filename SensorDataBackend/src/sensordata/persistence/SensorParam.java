package sensordata.persistence;

import java.util.List;

import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import sensordata.app.utils.ParamType;
/**
 * represents the a sensor parameter in DB
 * @author Anton Kroisant
 *
 */
@Entity(value = "sensorParam", noClassnameStored = true)
public class SensorParam {
	@Id
	private ObjectId id;

	private String paramName;
	private String paramType;

	protected SensorParam() {
	}

	@Reference(value = "sensorId", lazy = false, idOnly = true)
	private Sensor sensor;

	public SensorParam(String paramName, ParamType paramType, Sensor sensor) {
		this.paramName = paramName;
		this.paramType = paramType.name();
		this.sensor = sensor;
	}

	public ObjectId getId() {
		return id;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(ParamType paramType) {
		this.paramType = paramType.name();
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
}
