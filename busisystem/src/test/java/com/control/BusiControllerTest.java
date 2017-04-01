package com.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jws.app.operater.control.BusiController;
import com.jws.app.operater.service.BusiService;

public class BusiControllerTest {
	@InjectMocks
	private BusiController control = new BusiController();
	@Mock
	private BusiService busiService;
	@Mock
	private HttpServletRequest request =initRequest() ;
	
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));	
    }
	
	@Test
	public void testBusiSystemInterface() {
		Mockito.when(busiService.commonEntry(Mockito.any(HttpServletRequest.class))).thenReturn(null);
		Assert.assertNull(control.busiSystemInterface());
	}

	@Test
	public void testBusiFileSystemInterface() {
		Mockito.when(busiService.commonFileEntry(Mockito.any(HttpServletRequest.class))).thenReturn(null);
		Assert.assertNull(control.busiFileSystemInterface());
	}

	@Test
	public void testGetSysTime() {
		Mockito.when(busiService.getSysTime()).thenReturn(123L);
		Assert.assertEquals(123L, control.getSysTime());
	}
	
	private HttpServletRequest initRequest(){  
	    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);  
	    Mockito.when(request.getPathInfo()).thenReturn("/zjtv/service/news");  
	    Mockito.when(request.getRequestURI()).thenReturn("/zjtv/service/news");  
	    Mockito.when(request.getContextPath()).thenReturn("/zjtv");  
	    Mockito.when(request.getMethod()).thenReturn("GET");  
	    // 设置参数  
	    Mockito.when(request.getParameter("a")).thenReturn("aaa");  
	    
	    final Map<String, Object> hash = new HashMap<String, Object>();  
	    Answer<String> aswser = new Answer<String>() {    
	        public String answer(InvocationOnMock invocation) {    
	            Object[] args = invocation.getArguments();    
	            return hash.get(args[0].toString()).toString();    
	        }    
	    };
	    Mockito.when(request.getAttribute("isRawOutput")).thenReturn(true);    
	    Mockito.when(request.getAttribute("errMsg")).thenAnswer(aswser);    
	    Mockito.when(request.getAttribute("msg")).thenAnswer(aswser);    
	    return request;  
	} 

}
