package com.task;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jws.app.operater.service.CalculationAppService;
import com.jws.common.task.AppUsePercentTask;

public class AppUsePercentTaskTest {
	@InjectMocks
	private AppUsePercentTask appUsePercentTask = new AppUsePercentTask();
	@Mock
	private CalculationAppService calculationAppService;
	
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
    }

	@Test
	public void testCalculationYes() throws RuntimeException{
		Mockito.doThrow(new RuntimeException()).doNothing().when(calculationAppService).calculationYes();
		try {
			appUsePercentTask.calculationYes();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.toString().contains("RuntimeException"));
		}
		
	}
	@Test
	public void testCalculationWeek() {
		Mockito.doThrow(new RuntimeException()).doNothing().when(calculationAppService).calculationWeek();
		try {
			appUsePercentTask.calculationWeek();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.toString().contains("RuntimeException"));
		}
		
	}

	@Test
	public void testCalculationMonth() {
		Mockito.doThrow(new RuntimeException()).doNothing().when(calculationAppService).calculationMonth();
		try {
			appUsePercentTask.calculationMonth();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.toString().contains("RuntimeException"));
		}
		
	}

}
