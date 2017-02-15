//package sensordata.app;
//
//import org.mongodb.morphia.DatastoreImpl;
//import org.mongodb.morphia.Morphia;
//import org.mongodb.morphia.query.Query;
//
//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoDatabase;
//
//import sensordata.app.exceptions.SensorNotFoundException;
//import sensordata.persistence.Sensor;
//import sensordata.persistence.SensorDao;
//import sensordata.persistence.SensorParam;
//import sensordata.persistence.SensorParamDao;
//
//public class PersistenceManagement {
//	private static PersistenceManagement instance;
//	private DatastoreImpl datastore;
//	private SensorDao sensorDao;
//	private SensorParamDao sensorParamDao;
//	
//	
//	public static PersistenceManagement getInstance() {
//		if (PersistenceManagement.instance == null) {
//			PersistenceManagement.instance = new PersistenceManagement();
//			instance.datastore = new DatastoreImpl(new Morphia(), mongoClient, "sensordataDBs");
//			instance.sensorDao = new SensorDao(Sensor.class, datastore);
//			instance.sensorParamDao = new SensorParamDao(SensorParam.class, datastore);
//		}
//		return PersistenceManagement.instance;
//	}
//	@SuppressWarnings("rawtypes")
//	public boolean checkIfExist(Class clazz, String identifier) {
//		if (clazz.getSimpleName().equals(Sensor.class.getSimpleName())) {
//			return sensorDao.exists("sensorName", identifier);
//		}
//		return true;
//	}
//
//	public Sensor getSensor(String sensorName) throws SensorNotFoundException {
//		Query q = datastore.createQuery(Sensor.class).field("sensorName").equal(sensorName);
//		if (sensorDao.exists(q))
//			return (Sensor) q.fetch().next();
//		else
//			throw new SensorNotFoundException();
//	}
//
//}
