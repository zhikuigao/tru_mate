package com.jwis.service.curater;

import java.net.InetAddress;
import java.net.UnknownHostException;

//import org.htmlparser.Node;
//import org.htmlparser.Parser;
//import org.htmlparser.util.NodeList;
//import org.htmlparser.util.ParserException;

public class IPUtil {
//	private final String ipAutoQueryUrl = "http://1212.ip138.com/ic.asp";
	
	public String getIntranetIpAddress() throws UnknownHostException{
		InetAddress ia = InetAddress.getLocalHost();  
		return ia.getHostAddress(); 
	} 
	
//	public String getExtranetIpAddress() throws ParserException{
//		return getIpFromContent(parseContent());
//	}
//	
//	private String parseContent() throws ParserException{
//		String ipAddress = null;
//		Parser parser = new Parser(ipAutoQueryUrl);
//		NodeList list = parser.parse (null);
//		Node node = list.elementAt(0);
//		ipAddress = node.getLastChild().getChildren().elementAt(1).getText();
//		return ipAddress;
//	}
//	
//	private String getIpFromContent(String resultContent){
//		String ipAddress = null;
//		if(resultContent.contains("[") && resultContent.contains("]")){
//			String[] ipaddr1 = resultContent.split("\\[");
//			String[] ipaddr2 = ipaddr1[1].split("\\]");
//			ipAddress = ipaddr2[0];
//		}
//		return ipAddress;
//	}
}
