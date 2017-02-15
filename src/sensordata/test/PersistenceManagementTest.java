//package sensordata.test;
//
//import static org.junit.Assert.*;
//
//import org.junit.BeforeClass;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.mongodb.morphia.DatastoreImpl;
//import org.mongodb.morphia.Morphia;
//
//import com.mongodb.MongoClient;
//
//import sensordata.app.AppLayer;
//import sensordata.app.PersistenceManagement;
//import sensordata.app.exceptions.SensorAllreadyExistException;
//import sensordata.app.exceptions.SensorNotFoundException;
//import sensordata.persistence.Sensor;
//import sensordata.persistence.SensorDao;
//import sensordata.persistence.SensorParam;
//import sensordata.persistence.SensorParamDao;
//
//public class PersistenceManagementTest {
//	private static DatastoreImpl datastore;
//	private static SensorDao sensorDao;
//	private static SensorParamDao sensorParamDao;
//
//	@Rule
//	public ExpectedException exception = ExpectedException.none();
//	
//	@BeforeClass
//	public static void SetUp() {
//		MongoClient mongoClient = new MongoClient();
//		mongoClient.dropDatabase("sensordataDB");
//		datastore = new DatastoreImpl(new Morphia(), mongoClient, "sensordataDBs");
//		sensorDao = new SensorDao(Sensor.class, datastore);
//		sensorParamDao = new SensorParamDao(SensorParam.class, datastore);
//	}
//	
//	@Test
//	public void testCheckIfExist() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSensor() throws Exception {
//			String sensorname = "testGetSensor";
//			sensorDao.save(new Sensor(sensorname));
//			Sensor sensor = PersistenceManagement.getInstance().getSensor(sensorname);
//			assertNotNull(sensor);
//			exception.expect(SensorNotFoundException.class);
//			PersistenceManagement.getInstance().getSensor("notfound");
//
//	}
//
//}
