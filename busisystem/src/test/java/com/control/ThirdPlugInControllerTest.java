package com.control;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jws.app.operater.control.ThirdPlugInController;
import com.jws.app.operater.serviceBlock.plugIn.service.PlugInService;

public class ThirdPlugInControllerTest {
	@InjectMocks
	private ThirdPlugInController thirdPlugInController = new ThirdPlugInController();
	@Mock
	private PlugInService plugInService;
	@Mock
	private HttpServletRequest request =initRequest() ;
	
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));	
    }

	@Test
	public void testGitLab() throws IOException {
		Mockito.when(plugInService.accessPoint(Mockito.any(HttpServletRequest.class),Mockito.anyString()))
				.thenReturn(null);
		Assert.assertNull(thirdPlugInController.gitLab());
	}

	@Test
	public void testRedMine() throws IOException {
		Mockito.when(plugInService.accessPoint(Mockito.any(HttpServletRequest.class),Mockito.anyString()))
		.thenReturn(null);
		Assert.assertNull(thirdPlugInController.redMine());
	}
	
	private HttpServletRequest initRequest(){  
	    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);  
	    return request;  
	}

}
