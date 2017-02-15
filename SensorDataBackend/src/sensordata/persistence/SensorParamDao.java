package sensordata.persistence;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class SensorParamDao extends BasicDAO<SensorParam, ObjectId>{

	public SensorParamDao(Class<SensorParam> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

}
