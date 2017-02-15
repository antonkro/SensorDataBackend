package sensordata.persistence.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import sensordata.persistence.Sensor;

public class SensorDao extends BasicDAO<Sensor, ObjectId>{

	public SensorDao(Class<Sensor> entityClass, Datastore ds) {
		super(entityClass, ds);
	}
}
