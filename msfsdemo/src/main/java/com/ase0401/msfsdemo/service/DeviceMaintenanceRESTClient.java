/**
 * 
 */
package com.ase0401.msfsdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author stela
 *
 */
@Service
public class DeviceMaintenanceRESTClient implements DeviceMaintenanceService {

	private RestTemplate restTemplate;

	private String deviceEventsURL = "http://localhost:8099/api/events/";

	@Autowired
	public DeviceMaintenanceRESTClient(RestTemplate theRestTemplate) {
		restTemplate = theRestTemplate;
	}

	@Override
	public void updateDeviceFirmware() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDeviceState(int deviceId) {
		// make REST call
		ResponseEntity<String> responseEntity = restTemplate.exchange(deviceEventsURL + deviceId, HttpMethod.GET, null,
				new ParameterizedTypeReference<String>() {
				});
		String deviceState = responseEntity.getBody();
		return deviceState;
	}

	@Override
	public void initiateReplacementShipping() {
		// TODO Auto-generated method stub

	}

}
