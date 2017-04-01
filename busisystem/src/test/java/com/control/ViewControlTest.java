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
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.jws.app.operater.control.ViewControl;
import com.jws.app.operater.service.ViewService;

public class ViewControlTest {
	@InjectMocks
	private ViewControl view = new ViewControl();
	@Mock
	private ViewService viewService;
	@Mock
	private HttpServletRequest request =initRequest() ;
	
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));	
    }
	
	@Test
	public void testQqcallback() {
		Mockito.when(viewService.qqcallback(Mockito.any(HttpServletRequest.class))).thenReturn(null);
		Assert.assertNull(view.qqcallback());
	}

	@Test
	public void testQqgetopenid() {
		Mockito.when(viewService.qqgetopenid(Mockito.any(HttpServletRequest.class), Mockito.any(ModelMap.class))).thenReturn(null);
		Assert.assertNull(view.qqgetopenid(null));
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
