package com.service;

import net.sf.json.JSONObject;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.jws.app.operater.service.impl.InterfaceInvoking;

public class InterfaceInvokingTest {
	@InjectMocks
	private InterfaceInvoking interfaceInvoking;

	@Test
	public void testInvoking() {
		JSONObject oldParam = new JSONObject();
		oldParam.put("device", "");
		oldParam.put("token", "");
		oldParam.put("language", "");
	}

}
