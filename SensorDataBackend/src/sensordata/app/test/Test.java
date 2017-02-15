package sensordata.app.test;

import sensordata.app.exceptions.SensorAllreadyExistException;
import sensordata.app.exceptions.SensorNotFoundException;
import sensordata.app.exceptions.SensorParamAllreadyExistException;

public class Test {

	public static void main(String[] args) throws SensorAllreadyExistException, SensorParamAllreadyExistException, SensorNotFoundException {
//		MongoClient mongoClient = new MongoClient();
//		mongoClient.dropDatabase("sensordataDB");
//		DatastoreImpl datastore = new DatastoreImpl(new Morphia(), mongoClient, "sensordataDBs");
//		
//		
//		String sensorname="sensordergroße";
//		String sensorparam="luft";
//		String sensorparam2="wasser";
//		AppLayer.getInstance().createSensor(sensorname);
//		AppLayer.getInstance().addParamToSensor(sensorname, sensorparam, ParamType.PUSHDATA);
//		AppLayer.getInstance().addParamToSensor(sensorname, sensorparam2, ParamType.PUSHDATA);
//		
//		
//		int currTime=(int)(System.currentTimeMillis() / 1000);
		long einsl = 1L;
		int einsint =1;
		if(einsl== einsint)
			System.out.println(true);
		
		
//		Data data = new Data(currTime, "afasdasd");
//		Query<Sensor> q = datastore.createQuery(Sensor.class);
//		q.field("sensorName").equal(sensorname);
//		q.field("sensorParams.paramName").equals(sensorparam);
//		
//		Sensor sensor = q.fetch().next();
//		System.out.println(sensor.getSensorParams().size());
//		
//		UpdateOperations<Sensor> ops = datastore.createUpdateOperations(Sensor.class).add("sensorParams.datas", data);
//		datastore.update(q, ops);

	}

}
