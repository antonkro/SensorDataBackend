package sensordata.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bson.types.CodeWScope;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.CriteriaContainer;
import org.mongodb.morphia.query.CriteriaContainerImpl;
import org.mongodb.morphia.query.FieldEnd;
import org.mongodb.morphia.query.MorphiaIterator;
import org.mongodb.morphia.query.MorphiaKeyIterator;
import org.mongodb.morphia.query.Query;

import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.FongoDB;
import com.mongodb.MockMongoClient;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import com.mongodb.ReadPreference;

import sensordata.app.AppLayer;
import sensordata.app.exceptions.SensorAllreadyExistException;
import sensordata.app.exceptions.SensorNotFoundException;
import sensordata.app.exceptions.SensorParamAllreadyExistException;
import sensordata.persistence.Sensor;
import sensordata.persistence.SensorDao;
import sensordata.persistence.SensorParam;
import sensordata.persistence.SensorParamDao;
import sensordata.utils.ParamType;

public class AppLayerTest {
	private static DatastoreImpl datastore;
	private static SensorDao sensorDao;
	private static SensorParamDao sensorParamDao;
	private static Fongo fongo;
	private static FongoDB fongoDB;
	private static MockMongoClient mockClient;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void SetUp() {
		MongoClient mongoClient = new MongoClient();
		mongoClient.dropDatabase("sensordataDB");
		datastore = new DatastoreImpl(new Morphia(), mongoClient, "sensordataDBs");
		sensorDao = new SensorDao(Sensor.class, datastore);
		sensorParamDao = new SensorParamDao(SensorParam.class, datastore);
	}
	
	@Test
	public void testCreateSensor() throws SensorAllreadyExistException, SensorNotFoundException {

		String sensorName = "msp430";

		AppLayer.getInstance().createSensor(sensorName);
		AppLayer.getInstance().getSensor(sensorName);
		exception.expect(SensorAllreadyExistException.class);
		AppLayer.getInstance().createSensor(sensorName); //
	}

	@Test
	public void testGetSensor() throws Exception {
		try {
			String sensorname = "testGetSensor";
			AppLayer.getInstance().createSensor(sensorname);
			Sensor sensor = AppLayer.getInstance().getSensor(sensorname);
			assertNotNull(sensor);
			exception.expect(SensorNotFoundException.class);
			AppLayer.getInstance().getSensor("notfound");

		} catch (SensorAllreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddParamToSensor() throws SensorParamAllreadyExistException, SensorNotFoundException, SensorAllreadyExistException {

		String sensorName = "testAddParam_SENSOR";
		String paramName = "temperatur";
		ParamType type = ParamType.PUSHDATA;
			AppLayer.getInstance().createSensor(sensorName);
			AppLayer.getInstance().addParamToSensor(sensorName, paramName);
			
			exception.expect(SensorNotFoundException.class);
			AppLayer.getInstance().addParamToSensor("notfound", "notFoundParam");
	}

	@Test
	public void testPushDataStringStringStringBSONTimestamp() {
		fail("Not yet implemented");
	}

	@Test
	public void testPushDataBSON() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDataByParam() {
		fail("Not yet implemented");
	}

}
