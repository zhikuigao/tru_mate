package com.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jws.app.operater.control.AppController;
import com.jws.app.operater.service.AppService;

public class AppControllerTest {
	@InjectMocks
	private AppController app = new AppController();
	@Mock
	private AppService appService;
	
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
    }

	@Test
	public void testUserBlockEntry() {
		Mockito.when(appService.commonEntry(Mockito.anyString())).thenReturn(null);
		Assert.assertNull(app.userBlockEntry(""));
	}

}
