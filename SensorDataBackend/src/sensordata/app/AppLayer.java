package sensordata.app;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bson.BSON;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.MongoClient;

import sensordata.app.exceptions.SensorAllreadyExistException;
import sensordata.app.exceptions.SensorGroupAllreadyExistException;
import sensordata.app.exceptions.SensorGroupNotFoundException;
import sensordata.app.exceptions.SensorNotFoundException;
import sensordata.app.exceptions.SensorParamAllreadyExistException;
import sensordata.app.exceptions.SensorParamNotFoundException;
import sensordata.app.utils.ParamType;
import sensordata.app.utils.Triple;
import sensordata.persistence.Sensor;
import sensordata.persistence.SensorData;
import sensordata.persistence.SensorGroup;
import sensordata.persistence.SensorParam;
import sensordata.persistence.dao.SensorDao;
import sensordata.persistence.dao.SensorGroupDao;
import sensordata.persistence.dao.SensorParamDao;

/**
 * Represents the app layer the app layer interacts with the database and
 * provides data to other layer
 * 
 * @author Anton Kroisant
 *
 */
public class AppLayer {
	private static AppLayer instance;
	// protected static final Logger logger =
	// LogManager.getLogger(AppLayer.class.getName());

	private DatastoreImpl datastore;

	protected AppLayer() {
	}

	/**
	 * singelton pattern method for AppLayer
	 * 
	 * @return
	 */
	public static AppLayer getInstance() {
		if (instance == null) {
			instance = new AppLayer();
			instance.datastore = new DatastoreImpl(new Morphia(), new MongoClient("localhost"), "sensordataDB");
			instance.sensorDao = new SensorDao(Sensor.class, instance.datastore);
			instance.sensorParamDao = new SensorParamDao(SensorParam.class, instance.datastore);
			instance.sensorGroupDao = new SensorGroupDao(SensorGroup.class, instance.datastore);
		}
		return AppLayer.instance;
	}

	@SuppressWarnings("rawtypes")
	private boolean checkIfExist(Class clazz, String sensorName, String paramName, String groupName, ParamType type) {
		if (clazz.getSimpleName().equals(Sensor.class.getSimpleName())) {
			return sensorDao.exists("sensorName", sensorName);
		}

		if (clazz.getSimpleName().equals(SensorParam.class.getSimpleName())) {
			Query<SensorParam> querySensor = datastore.createQuery(SensorParam.class);
			try {
				querySensor.field("sensor").equal(getSensor(sensorName));
			} catch (SensorNotFoundException e) {
				e.printStackTrace();
			}
			querySensor.field("paramName").equal(paramName);
			querySensor.field("paramType").equal(type.name());
			return sensorParamDao.exists(querySensor);
		}
		if (clazz.getSimpleName().equals(SensorGroup.class.getSimpleName())) {
			return sensorGroupDao.exists("groupName", groupName);
		}
		return true;
	}

	/**
	 * creates the Group if a Sensor with this name exist a
	 * GroupAllredyExistException will be thrown
	 * 
	 * @param groupName
	 * @throws SensorGroupAllreadyExistException
	 */
	public void createSensorGroup(String groupName) throws SensorGroupAllreadyExistException {
		if (!checkIfExist(SensorGroup.class, "", "", groupName, null))
			datastore.save(new SensorGroup(groupName));
		else
			throw new SensorGroupAllreadyExistException(groupName);
	}

	/**
	 * creates the Sensor if a Sensor with this name exist a
	 * SensorAllredyExistException will be thrown
	 * 
	 * @param sensorName
	 * @throws SensorAllreadyExistException
	 */
	public void createSensor(String sensorName) throws SensorAllreadyExistException {
		Sensor sensor = new Sensor(sensorName);
		if (!checkIfExist(Sensor.class, sensorName, "", "", null)) {
			datastore.save(sensor);
		} else
			throw new SensorAllreadyExistException(sensorName);
	}

	/**
	 * retrieves Sensor from Database
	 * 
	 * @param sensorName
	 * @return
	 * @throws SensorNotFoundException
	 */
	public Sensor getSensor(String sensorName) throws SensorNotFoundException {
		Query q = datastore.createQuery(Sensor.class);
		q.field("sensorName").equal(sensorName);
		if (sensorDao.exists(q))
			return sensorDao.find(q).get();
		else
			throw new SensorNotFoundException();
	}

	/**
	 * retrieves the SensorGroup by GroupName
	 * 
	 * @param groupName
	 * @return
	 * @throws SensorGroupNotFoundException
	 */
	public SensorGroup getSensorGroup(String groupName) throws SensorGroupNotFoundException {
		Query q = datastore.createQuery(SensorGroup.class).field("groupName").equal(groupName);
		if (sensorGroupDao.exists(q))
			return (SensorGroup) q.fetch().next();
		else
			throw new SensorGroupNotFoundException();

	}

	/**
	 * retrieves the sensor parameter by sensorname, parametername and type of
	 * parameter
	 * 
	 * @param sensorName
	 * @param paramName
	 * @param type
	 * @return
	 * @throws SensorNotFoundException
	 * @throws SensorParamNotFoundException
	 */
	public SensorParam getSensorParam(String sensorName, String paramName, ParamType type)
			throws SensorNotFoundException, SensorParamNotFoundException {
		Query<SensorParam> query = datastore.createQuery(SensorParam.class);
		query.field("paramName").equal(paramName);
		query.field("paramType").equal(type);
		query.field("sensorId").equal(getSensor(sensorName).getId());

		Iterator<SensorParam> iterator = query.iterator();
		if (iterator.hasNext())
			return query.iterator().next();
		throw new SensorParamNotFoundException();
	}

	/**
	 * adds a sensor to a group the sensor and the group must be created before
	 * 
	 * @param sensorname
	 * @param groupname
	 * @throws SensorNotFoundException
	 * @throws SensorGroupNotFoundException
	 */
	public void addSensorToGroup(String sensorname, String groupname)
			throws SensorNotFoundException, SensorGroupNotFoundException {

		if (!checkIfExist(SensorGroup.class, "", "", groupname, null))
			throw new SensorGroupNotFoundException();
		if (!checkIfExist(Sensor.class, sensorname, "", "", null))
			throw new SensorNotFoundException();
		SensorGroup sensorGroup = getSensorGroup(groupname);
		Sensor sensor = getSensor(sensorname);
		Query<Sensor> q = datastore.createQuery(Sensor.class);
		q.field("id").equals(sensor.getId());
		q.field("sensorName").equal(sensorname);

		UpdateOperations<Sensor> ops = datastore.createUpdateOperations(Sensor.class).set("sensorGroup", sensorGroup);
		datastore.update(q, ops);

	}

	/**
	 * Adds a specifc parameter to given sensorname. Sensor should be created
	 * before the parameter will be created
	 * 
	 * @param sensorName
	 * @param paramName
	 * @throws SensorNotFoundException
	 * @throws SensorParamAllreadyExistException
	 */
	public void addParamToSensor(String sensorName, String paramName)
			throws SensorParamAllreadyExistException, SensorNotFoundException {
		if (checkIfExist(Sensor.class, sensorName, "", "", ParamType.PUSHDATA)) {
			datastore.save(new SensorParam(paramName, ParamType.PUSHDATA, getSensor(sensorName)));
		} else
			throw new SensorNotFoundException();
	}

	public void pushData(String sensorName, String paramName, String pushingData, int currentTimeMillis)
			throws SensorNotFoundException, SensorParamNotFoundException {

		SensorParam sensorParam = getSensorParam(sensorName, paramName, ParamType.PUSHDATA);
		datastore.save(new SensorData(currentTimeMillis, pushingData, sensorParam));
	}

	/**
	 * not implemented yet can be used in the future to procedures a push via
	 * bson
	 * 
	 * @param bson
	 */
	public void pushData(BSON bson) {

	}

	/**
	 * retrieves the all data with timestamp from given parameter
	 * @param sensorName
	 * @param paramName
	 * @return
	 * @throws SensorNotFoundException
	 * @throws SensorParamNotFoundException
	 */
	public String getDataByParam(String sensorName, String paramName)
			throws SensorNotFoundException, SensorParamNotFoundException {
		SensorParam sensorParam = getSensorParam(sensorName, paramName, ParamType.PUSHDATA);
		Query<SensorData> q = datastore.createQuery(SensorData.class);
		q.field("sensorParam").equal(sensorParam);
		String s = "";
		for (SensorData sensorData : q) {
			s += sensorData.toString() + "\n";
		}
		return s;
	}
	/**
	 * updates the name of a sensorgroup
	 * @param currentGroupName
	 * @param newGroupName
	 * @throws SensorGroupNotFoundException
	 * @throws SensorGroupAllreadyExistException
	 * @throws SensorAllreadyExistException
	 */
	public void updateSensorGroupName(String currentGroupName, String newGroupName)
			throws SensorGroupNotFoundException, SensorGroupAllreadyExistException{
		SensorGroup sensorGroup = AppLayer.getInstance().getSensorGroup(currentGroupName);
		if (!checkIfExist(SensorGroup.class, "", "", currentGroupName, null))
			throw new SensorGroupNotFoundException();
		if (checkIfExist(SensorGroup.class, "", "", newGroupName, null))
			throw new SensorGroupAllreadyExistException();
		Query<SensorGroup> q = datastore.createQuery(SensorGroup.class);
		Query<SensorGroup> updateQuery = datastore.createQuery(SensorGroup.class).field("id").equal(sensorGroup.getId());
		UpdateOperations<SensorGroup> ops = datastore.createUpdateOperations(SensorGroup.class).set("groupName", newGroupName);
		datastore.update(updateQuery, ops);

	}
	
	/**
	 * updates the name of a sensor
	 * @param currentSensorName
	 * @param newSensorName
	 * @throws SensorNotFoundException
	 * @throws SensorAllreadyExistException
	 */
	public void updateSensorName(String currentSensorName, String newSensorName)
			throws SensorNotFoundException, SensorAllreadyExistException {
		Sensor sensor = getSensor(currentSensorName);
		if (checkIfExist(Sensor.class, newSensorName, "", "", null))
			throw new SensorAllreadyExistException();
		Query<Sensor> q = datastore.createQuery(Sensor.class);
		Query<Sensor> updateQuery = datastore.createQuery(Sensor.class).field("id").equal(sensor.getId());
		UpdateOperations<Sensor> ops = datastore.createUpdateOperations(Sensor.class).set("sensorName", newSensorName);
		datastore.update(updateQuery, ops);
	}
	/**
	 * updates the name of a sensor parameter
	 * @param sensorName
	 * @param currentParamName
	 * @param newParamName
	 * @throws SensorParamNotFoundException
	 * @throws SensorParamAllreadyExistException
	 * @throws SensorNotFoundException
	 */
	public void updateParameterName(String sensorName, String currentParamName, String newParamName)
			throws SensorParamNotFoundException, SensorParamAllreadyExistException, SensorNotFoundException {
		SensorParam sensorParam = getSensorParam(sensorName, currentParamName, ParamType.PUSHDATA);

		if (checkIfExist(SensorParam.class, sensorName, newParamName, "", ParamType.PUSHDATA))
			throw new SensorParamAllreadyExistException();
		Query<SensorParam> querySensor = datastore.createQuery(SensorParam.class);
		querySensor.field("paramName").equal(currentParamName);
		querySensor.field("paramType").equal(ParamType.PUSHDATA);
		querySensor.field("sensor").equal(getSensor(sensorName));
		UpdateOperations<SensorParam> ops = datastore.createUpdateOperations(SensorParam.class).set("paramName",
				newParamName);
		datastore.update(querySensor, ops);
	}

	/**
	 * retrieves Groupname from Sensor if the senor has no group there will be
	 * returned a empty string
	 * 
	 * @param sensorName
	 * @return
	 * @throws SensorNotFoundException
	 */
	public String getGroupNameFromSensor(String sensorName) throws SensorNotFoundException {
		if (!checkIfExist(Sensor.class, sensorName, "", "", null))
			throw new SensorNotFoundException();
		Sensor sensor = getSensor(sensorName);
		try {
			sensor.getSensorGroup().getId();
		} catch (NullPointerException e) {
			return "";
		}

		return sensorGroupDao.get(sensor.getSensorGroup().getId()).getGroupName();
	}

	/**
	 * retrieves all SensorName from Database
	 * 
	 * @return
	 */
	public List<String> getAllSensorNames() {
		List<String> sensornames = new LinkedList<String>();
		Query<Sensor> sensorQuery = datastore.find(Sensor.class);
		List<Sensor> sensors = sensorQuery.asList();
		for (Sensor sensor : sensors) {
			sensornames.add(sensor.getSensorName());
		}
		return sensornames;

	}

	/**
	 * retrieves all SensorGroupNames from Database
	 * 
	 * @return
	 */
	public List<String> getAllSensorGroupNames() {
		List<String> sensorgroupnames = new LinkedList<String>();
		Query<SensorGroup> sensorQuery = datastore.find(SensorGroup.class);
		List<SensorGroup> sensorGroups = sensorQuery.asList();
		for (SensorGroup sensorGroup : sensorGroups) {
			sensorgroupnames.add(sensorGroup.getGroupName());
		}
		return sensorgroupnames;

	}

	/**
	 * retrieves all Parameter from sensor and all the data from the parameters
	 * in a String Triple Triple = <paraname, paramvalue, valuetimestamp>
	 * 
	 * @param sensorName
	 * @return Triple<String, String, String>
	 * @throws SensorNotFoundException
	 */
	public List<Triple<String, String, String>> getAllParameterAllData(String sensorName)
			throws SensorNotFoundException {
		List<Triple<String, String, String>> returnList = new LinkedList<Triple<String, String, String>>();
		Sensor sensor = getSensor(sensorName);

		Query<SensorParam> q = datastore.createQuery(SensorParam.class);
		q.field("sensor").equal(sensor);
		List<SensorParam> paramList = q.asList();
		for (SensorParam sensorParam : paramList) {
			Query<SensorData> dataQ = datastore.createQuery(SensorData.class);
			dataQ.field("sensorParam").equal(sensorParam);
			for (SensorData sensorData : dataQ.asList()) {
				returnList.add(new Triple<String, String, String>(sensorParam.getParamName(), sensorData.getData(),
						sensorData.getTimestamp().toString()));
			}
			if(dataQ.asList().isEmpty())
				returnList.add(new Triple<String, String, String>(sensorParam.getParamName(), "", ""));
				
			
		}
		return returnList;
	}
}
