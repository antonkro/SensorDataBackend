package sensordata.persistence.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import sensordata.persistence.SensorData;

public class DataDao extends BasicDAO<SensorData, ObjectId>{

	public DataDao(Class<SensorData> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

}
