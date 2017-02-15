package sensordata.persistence.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import sensordata.persistence.SensorParam;

public class SensorParamDao extends BasicDAO<SensorParam, ObjectId>{

	public SensorParamDao(Class<SensorParam> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

}
