/**
 * 
 */
package com.ase0401.msfsdemo.service;

import java.util.List;

import org.emfjson.jackson.module.EMFModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ase0401.msfsdemo.service.model.LogModel;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author stela
 *
 */
@Service
public class DeviceControlRESTClient implements DeviceControlService {
	
	private RestTemplate restTemplate;

	private String sensorDataURL = "http://localhost:8099/api/sensordata/";
	private String actuatorDataURL = "http://localhost:8099/api/actuatordata/";
	private String logsURL = "http://localhost:8099/api/logs/";
	private String stateURL = "http://localhost:8099/api/state/";
	private String configURL = "http://localhost:8099/api/config/";
	private String measureURL = "http://localhost:8099/api/measure/";
	private String registerURL = "http://localhost:8099/api/register/";
	private String messageURL = "http://localhost:8099/api/message/";
	private String clearTasksURL = "http://localhost:8099/api/clearschedule/";
	
	ObjectMapper mapper = EMFModule.setupDefaultMapper();
	
	@Autowired
	public DeviceControlRESTClient(RestTemplate theRestTemplate) {
		restTemplate = theRestTemplate;
	}

	@Override
	public List<LogModel> getSensorData(int deviceId) {
		// make REST call
		ResponseEntity<List<LogModel>> response = restTemplate.exchange(sensorDataURL + deviceId, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<LogModel>>() {
				});
		
		List<LogModel> sensorData = response.getBody();

		return sensorData;
	}

	@Override
	public List<LogModel> getLogs(int deviceId) {
		// make REST call
		ResponseEntity<List<LogModel>> responseEntity = restTemplate.exchange(logsURL + deviceId, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<LogModel>>() {
				});
		List<LogModel> logs = responseEntity.getBody();
		return logs;
	}

	@Override
	public void toggleState(int deviceId, String state) {
		restTemplate.exchange(stateURL + deviceId + "/" + state, HttpMethod.POST, null,
				new ParameterizedTypeReference<Void>() {
				});
	}

	@Override
	public void setAction(int deviceId, int actionId, int pos, double value, String unit) {
		restTemplate.exchange(configURL + deviceId + "/" + pos + "/" + actionId + "/" + value + "/" + unit, HttpMethod.POST, null,
				new ParameterizedTypeReference<Void>() {
				});
	}

	@Override
	public void setMeasurement(int deviceId, int measureId, int pos) {
		restTemplate.exchange(measureURL + deviceId + "/" + pos + "/" + measureId, HttpMethod.POST, null,
				new ParameterizedTypeReference<Void>() {
				});
	}
	
	@Override
	public void registerDevice(int deviceId) {
		restTemplate.exchange(registerURL + deviceId, HttpMethod.POST, null,
				new ParameterizedTypeReference<Void>() {
				});
		System.out.println("Done!");
	}

	@Override
	public String getMessage(int deviceId) {
		ResponseEntity<String> responseEntity = restTemplate.exchange(messageURL + deviceId, HttpMethod.GET, null,
				new ParameterizedTypeReference<String>() {
				});
		String msg = responseEntity.getBody();
		return msg;
	}

	@Override
	public void clearSchedule(int deviceId) {
		restTemplate.exchange(clearTasksURL + deviceId, HttpMethod.POST, null,
				new ParameterizedTypeReference<Void>() {
				});
		
	}

	@Override
	public List<LogModel> getActuatorData(int deviceId) {
		ResponseEntity<List<LogModel>> response = restTemplate.exchange(actuatorDataURL + deviceId, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<LogModel>>() {
				});
		
		List<LogModel> actuatorData = response.getBody();

		return actuatorData;
	}


}
