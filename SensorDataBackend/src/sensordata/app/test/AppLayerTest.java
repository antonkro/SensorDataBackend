package sensordata.app.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import sensordata.app.AppLayer;
import sensordata.app.exceptions.SensorAllreadyExistException;
import sensordata.app.exceptions.SensorGroupAllreadyExistException;
import sensordata.app.exceptions.SensorGroupNotFoundException;
import sensordata.app.exceptions.SensorNotFoundException;
import sensordata.app.exceptions.SensorParamAllreadyExistException;
import sensordata.app.exceptions.SensorParamNotFoundException;
import sensordata.app.utils.ParamType;
import sensordata.app.utils.Triple;
import sensordata.persistence.Sensor;
import sensordata.persistence.SensorGroup;
import sensordata.persistence.SensorParam;
import sensordata.persistence.dao.SensorDao;
import sensordata.persistence.dao.SensorGroupDao;
import sensordata.persistence.dao.SensorParamDao;
/**
 * junit test class for {@link AppLayer}
 * @author Anton Kroisant
 *
 */
public class AppLayerTest {
	private static DatastoreImpl datastore;
	private static SensorDao sensorDao;
	private static SensorParamDao sensorParamDao;
	private static SensorGroupDao sensorGroupDao;
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void SetUp() {
		MongoClient mongoClient = new MongoClient();
		mongoClient.dropDatabase("sensordataDB");
		datastore = new DatastoreImpl(new Morphia(), mongoClient, "sensordataDBs");
		sensorDao = new SensorDao(Sensor.class, datastore);
		sensorParamDao = new SensorParamDao(SensorParam.class, datastore);
		sensorGroupDao = new SensorGroupDao(SensorGroup.class, datastore);
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
	public void testAddSensorToGroup() throws SensorAllreadyExistException, SensorGroupAllreadyExistException,
			SensorNotFoundException, SensorGroupNotFoundException {
		String sensorname1 = "addSensorToGroup_Sensor";
		String groupname = "addSensorToGroup_Group";

		AppLayer.getInstance().createSensor(sensorname1);
		AppLayer.getInstance().createSensorGroup(groupname);

		AppLayer.getInstance().addSensorToGroup(sensorname1, groupname);
		Sensor sensor = AppLayer.getInstance().getSensor(sensorname1);

		boolean result = false;
		if (sensor.getSensorGroup().getGroupName().equals(groupname))
			result = true;
		assertTrue(result);

		exception.expect(SensorNotFoundException.class);
		AppLayer.getInstance().addSensorToGroup("notfound", groupname);
		exception.expect(SensorGroupNotFoundException.class);
		AppLayer.getInstance().addSensorToGroup(sensorname1, "notfound");
	}

	@Test
	public void testAddParamToSensor() throws SensorParamAllreadyExistException, SensorNotFoundException,
			SensorAllreadyExistException, SensorParamNotFoundException {

		String sensorName = "testAddParam_SENSOR";
		String sensorName2 = "testAddParam_SENSOR2";
		String paramName = "temperatur";
		String paramName2 = "adasd   asdasdasd";
		AppLayer.getInstance().createSensor(sensorName);
		AppLayer.getInstance().createSensor(sensorName2);

		AppLayer.getInstance().addParamToSensor(sensorName, paramName);
		AppLayer.getInstance().addParamToSensor(sensorName, paramName2);
		AppLayer.getInstance().addParamToSensor(sensorName2, paramName);

		assertNotNull(AppLayer.getInstance().getSensorParam(sensorName, paramName, ParamType.PUSHDATA));
		assertNotNull(AppLayer.getInstance().getSensorParam(sensorName, paramName2, ParamType.PUSHDATA));

		exception.expect(SensorNotFoundException.class);
		AppLayer.getInstance().addParamToSensor("notfound", paramName);

		exception.expect(SensorParamAllreadyExistException.class);
		AppLayer.getInstance().addParamToSensor(sensorName, paramName);

	}

	@Test
	public void testCreateSensorGroup() throws SensorGroupAllreadyExistException, SensorGroupNotFoundException {
		String groupName1 = "gateway_create_1";
		String groupName2 = "gateway_create_2";
		String groupName3 = "gateway_create_3";

		AppLayer.getInstance().createSensorGroup(groupName1);
		AppLayer.getInstance().createSensorGroup(groupName2);
		AppLayer.getInstance().createSensorGroup(groupName3);

		assertNotNull(AppLayer.getInstance().getSensorGroup(groupName1));
		assertNotNull(AppLayer.getInstance().getSensorGroup(groupName2));
		assertNotNull(AppLayer.getInstance().getSensorGroup(groupName3));

		exception.expect(SensorGroupAllreadyExistException.class);
		AppLayer.getInstance().createSensorGroup(groupName1);
	}

	/**
	 * Test method for
	 * {@link sensordata.app.AppLayer#getSensorGroup(java.lang.String)}.
	 * 
	 * @throws SensorGroupAllreadyExistException
	 */
	@Test
	public void testGetSensorGroup() throws SensorGroupNotFoundException, SensorGroupAllreadyExistException {
		String groupName1 = "gateway_get_1";
		String groupName2 = "gateway_get_2";

		AppLayer.getInstance().createSensorGroup(groupName1);
		AppLayer.getInstance().createSensorGroup(groupName2);

		SensorGroup sensorGroup1 = AppLayer.getInstance().getSensorGroup(groupName1);
		SensorGroup sensorGroup2 = AppLayer.getInstance().getSensorGroup(groupName2);

		assertNotNull(sensorGroup1);
		assertNotNull(sensorGroup2);
		assertEquals(groupName1, sensorGroup1.getGroupName());
		assertEquals(groupName2, sensorGroup2.getGroupName());

		exception.expect(SensorGroupNotFoundException.class);
		AppLayer.getInstance().getSensorGroup("notfound");

	}

	/**
	 * Test method for {@link sensordata.app.AppLayer#pushData(org.bson.BSON)}.
	 */
	@Test
	public void testPushDataBSON() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for
	 * {@link sensordata.app.AppLayer#getDataByParam(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDataByParam() throws Exception {
		String sensorname1 = "getDataByParam_Sensor";
		String paramname1 = "getDataByParamSensor_param1";
		String data1 = "data1_dnfsdklfalsdfjlasdjflasjdflasdASDASD2131231kjf";
		String data2 = "data2_asdasdasdasdasdasdasdasdasdas";

		AppLayer.getInstance().createSensor(sensorname1);
		AppLayer.getInstance().addParamToSensor(sensorname1, paramname1);
		int timestamp1 = (int) (System.currentTimeMillis() / 1000);
		AppLayer.getInstance().pushData(sensorname1, paramname1, data1, timestamp1);
		AppLayer.getInstance().pushData(sensorname1, paramname1, data2, timestamp1);

		String returndata = AppLayer.getInstance().getDataByParam(sensorname1, paramname1);
	}

	/**
	 * Test method for
	 * {@link sensordata.app.AppLayer#pushData(java.lang.String, java.lang.String, java.lang.String, int)}
	 * .
	 * 
	 * @throws SensorParamNotFoundException
	 * @throws SensorAllreadyExistException
	 * @throws SensorNotFoundException
	 * @throws SensorParamAllreadyExistException
	 */
	@Test
	public void testPushDataStringStringStringInt() throws SensorParamNotFoundException, SensorAllreadyExistException,
			SensorParamAllreadyExistException, SensorNotFoundException {
		String sensorname1 = "PushDataSimple1_simple";
		String sensorname2 = "PushDataSimple2_simple";
		String param1 = "abc123_simple";
		String param2 = "asdasdassdasdasdasas";
		String data1 = "dnfsdklfalsdfjlasdjflasjdflasdASDASD2131231kjf";
		String data2 = "asdasdasdasdasdasdasdasdasdas";

		AppLayer.getInstance().createSensor(sensorname1);
		AppLayer.getInstance().createSensor(sensorname2);

		AppLayer.getInstance().addParamToSensor(sensorname1, param1);
		AppLayer.getInstance().addParamToSensor(sensorname2, param2);

		int timestamp1 = (int) (System.currentTimeMillis() / 1000);
		int timestamp2 = (int) (System.currentTimeMillis() / 1000) + 1;

		AppLayer.getInstance().pushData(sensorname1, param1, data1, timestamp1);
		AppLayer.getInstance().pushData(sensorname2, param2, data1, timestamp2);
		AppLayer.getInstance().pushData(sensorname2, param2, data2, timestamp2);

		exception.expect(SensorNotFoundException.class);
		AppLayer.getInstance().pushData("notfound", param2, data2, timestamp2);

		exception.expect(SensorParamNotFoundException.class);
		AppLayer.getInstance().pushData(sensorname2, "notfound", data2, timestamp2);
	}

	/**
	 * Test method for
	 * {@link sensordata.app.AppLayer#getSensorParam(java.lang.String, java.lang.String, sensordata.app.utils.ParamType)}
	 * .
	 */
	@Test
	public void testGetSensorParam() throws Exception {
		String sensorname1 = "getSensorParam_Sensor1";
		String sensorname2 = "getSensorParam_Sensor2";

		String paramname1 = "getSensorParam_param1";
		String paramname2 = "getSensorParam_param2";

		AppLayer.getInstance().createSensor(sensorname1);
		AppLayer.getInstance().createSensor(sensorname2);

		AppLayer.getInstance().addParamToSensor(sensorname1, paramname1);
		AppLayer.getInstance().addParamToSensor(sensorname2, paramname2);

		SensorParam sensorParam1 = AppLayer.getInstance().getSensorParam(sensorname1, paramname1, ParamType.PUSHDATA);
		SensorParam sensorParam2 = AppLayer.getInstance().getSensorParam(sensorname2, paramname2, ParamType.PUSHDATA);

		assertNotNull(sensorParam1);
		assertNotNull(sensorParam2);

		exception.expect(SensorNotFoundException.class);
		AppLayer.getInstance().getSensorParam("notfound", paramname1, ParamType.PUSHDATA);

		exception.expect(SensorParamNotFoundException.class);
		AppLayer.getInstance().getSensorParam(sensorname1, paramname1, null);

		exception.expect(SensorParamNotFoundException.class);
		AppLayer.getInstance().getSensorParam(sensorname1, "notfound", ParamType.PUSHDATA);

	}

	@Test
	public void testGetAllSensorNames() throws Exception {
		// AppLayer.getInstance().createSensor("getAllSensorNames_SENOR1");
		// AppLayer.getInstance().createSensor("getAllSensorNames_SENOR2");
		//
		// boolean result =false;
		// List<String> sensornames=AppLayer.getInstance().getAllSensorNames();
		// if(sensornames.size()==2)
		// result=true;
		// assertTrue(result);
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testGetAllSensorGroupNames() throws Exception {
		// AppLayer.getInstance().createSensorGroup("getAllSensorGroupNames_GROUP1");
		// AppLayer.getInstance().createSensorGroup("getAllSensorGroupNames_GROUP2");
		// boolean result =false;
		// List<String>
		// sensorGroupNames=AppLayer.getInstance().getAllSensorGroupNames();
		// if(sensorGroupNames.size()== 2)
		// result=true;
		// assertTrue(result);
		// System.out.println(sensorGroupNames.toString());
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testGetGroupNameFromSensor() throws SensorAllreadyExistException, SensorGroupAllreadyExistException,
			SensorGroupNotFoundException, SensorNotFoundException {
		String sensorname = "getGroupNameFromSensor_Sensor";
		String sensornameWithoutGroup = "getGroupNameFromSensor_SensorWithoutGroup";
		String sensorGroupName = "getGroupNameFromSensor_Group";

		AppLayer.getInstance().createSensor(sensorname);
		AppLayer.getInstance().createSensor(sensornameWithoutGroup);
		AppLayer.getInstance().createSensorGroup(sensorGroupName);
		AppLayer.getInstance().addSensorToGroup(sensorname, sensorGroupName);
		Sensor sensor = AppLayer.getInstance().getSensor(sensorname);

		boolean result = false;
		// check sensor with group
		if (AppLayer.getInstance().getGroupNameFromSensor(sensorname).equals(sensorGroupName))
			result = true;
		assertTrue(result);
		result = false;

		// check sensor without group
		if (AppLayer.getInstance().getGroupNameFromSensor(sensornameWithoutGroup).equals(""))
			result = true;
		assertTrue(result);

		exception.expect(SensorNotFoundException.class);
		AppLayer.getInstance().getGroupNameFromSensor("notfound");
	}

	/**
	 * Test method for
	 * {@link sensordata.app.AppLayer#updateSensorName(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testUpdateSensorName() throws Exception {
		String sensorname = "updateSensorName_Sensor";
		String newSensorName = "updateSensorName_UpdatedSensor";
		AppLayer.getInstance().createSensor(sensorname);
		Sensor sensor = AppLayer.getInstance().getSensor(sensorname);
		ObjectId id = sensor.getId();

		AppLayer.getInstance().updateSensorName(sensorname, newSensorName);
		Sensor updatedSensor = AppLayer.getInstance().getSensor(newSensorName);
		ObjectId expectedid = sensor.getId();
		boolean result = false;
		if (id.equals(expectedid))
			result = true;
		assertTrue(result);
		result = false;
		if (updatedSensor.getSensorName().equals(newSensorName))
			result = true;
		assertTrue(result);

		exception.expect(SensorAllreadyExistException.class);
		AppLayer.getInstance().updateSensorName(newSensorName, newSensorName);

		exception.expect(SensorNotFoundException.class);
		AppLayer.getInstance().updateSensorName("nofound", "newname");
	}

	/**
	 * Test method for
	 * {@link sensordata.app.AppLayer#updateParameterName(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testUpdateParameterName() throws Exception {
		String sensorname = "testUpdateParameterName_sensor";
		String paramname = "testUpdateParameterName_paramName";
		String newParamname = "testUpdateParameterName_newParamname";

		AppLayer.getInstance().createSensor(sensorname);
		AppLayer.getInstance().addParamToSensor(sensorname, paramname);
		SensorParam before = AppLayer.getInstance().getSensorParam(sensorname, paramname, ParamType.PUSHDATA);
		ObjectId id = before.getId();
		AppLayer.getInstance().updateParameterName(sensorname, paramname, newParamname);
		SensorParam after = AppLayer.getInstance().getSensorParam(sensorname, newParamname, ParamType.PUSHDATA);
		ObjectId idAfter = after.getId();

		boolean result = false;
		if (id.equals(idAfter))
			result = true;
		assertTrue(result);
		result = false;
		if (after.getParamName().equals(newParamname))
			result = true;
		assertTrue(result);

		exception.expect(SensorNotFoundException.class);
		AppLayer.getInstance().updateParameterName("notfound", newParamname, newParamname);
		exception.expect(SensorParamNotFoundException.class);
		AppLayer.getInstance().updateParameterName(sensorname, "notfound", newParamname);
		exception.expect(SensorParamAllreadyExistException.class);
		AppLayer.getInstance().updateParameterName(sensorname, newParamname, newParamname);
	}

	/**
	 * Test method for
	 * {@link sensordata.app.AppLayer#getAllParameterAllData(java.lang.String)}.
	 */
	@Test
	public void testGetAllParameterAllData() throws Exception {
		String sensorname = "testGetAllParameterAllData_Sensor";
		String param1 = "testGetAllParameterAllData_Param1";
		String param2 = "testGetAllParameterAllData_Param2";
		String param3 = "testGetAllParameterAllData_ParamWithoutData";
		String data1 = "testGetAllParameterAllData_Data1_0";
		String data2 = "testGetAllParameterAllData_Data1_1";
		String data3 = "testGetAllParameterAllData_Data2_0";

		AppLayer.getInstance().createSensor(sensorname);
		AppLayer.getInstance().addParamToSensor(sensorname, param1);
		AppLayer.getInstance().addParamToSensor(sensorname, param2);
		AppLayer.getInstance().addParamToSensor(sensorname, param3);

		int timestamp = (int) (System.currentTimeMillis() / 1000);
		AppLayer.getInstance().pushData(sensorname, param1, data1, timestamp);
		AppLayer.getInstance().pushData(sensorname, param1, data2, timestamp + 1);
		AppLayer.getInstance().pushData(sensorname, param2, data3, timestamp + 2);

		List<Triple<String, String, String>> resultList = AppLayer.getInstance().getAllParameterAllData(sensorname);
		boolean result = false;
		if (resultList.size() == 4)
			result = true;
		assertTrue(result);
		
		for (Triple<String, String, String> triple : resultList) {
			result=false;
			if((triple.first.equals(param1) &&triple.second.equals(data1)))
				result=true;
			if((triple.first.equals(param1) &&triple.second.equals(data2)))
				result=true;
			if((triple.first.equals(param2) &&triple.second.equals(data3)))
				result=true;
			if(triple.first.equals(param3))
				result=true;
		}
		System.out.println();
		assertTrue(result);

	}

	/**
	 * Test method for {@link sensordata.app.AppLayer#updateSensorGroupName(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdateSensorGroupName() throws Exception {
		String groupname = "testUpdateSensorGroupName_groupname";
		String newGroupName= "testUpdateSensorGroupName_newgroupname";
		
		AppLayer.getInstance().createSensorGroup(groupname);
		SensorGroup before = AppLayer.getInstance().getSensorGroup(groupname);
		
		ObjectId beforeId=before.getId();
		AppLayer.getInstance().updateSensorGroupName(groupname, newGroupName);
		
		SensorGroup after = AppLayer.getInstance().getSensorGroup(newGroupName);
		ObjectId afterId=after.getId();
		
		boolean result =false;
		
		if(beforeId.equals(afterId) && after.getGroupName().equals(newGroupName))
			result=true;
		
		
		assertTrue(result);
		
		exception.expect(SensorGroupNotFoundException.class);
		AppLayer.getInstance().updateSensorGroupName("notfound", newGroupName);
		
		exception.expect(SensorGroupAllreadyExistException.class);
		AppLayer.getInstance().updateSensorGroupName(newGroupName, newGroupName);
	}

}
