package sensordata.persistence.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import sensordata.persistence.SensorGroup;

public class SensorGroupDao extends BasicDAO<SensorGroup, ObjectId>{

	public SensorGroupDao(Class<SensorGroup> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

}
