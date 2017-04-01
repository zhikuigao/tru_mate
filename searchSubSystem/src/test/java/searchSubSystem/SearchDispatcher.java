package searchSubSystem;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jws.common.util.JsonUtil;
import com.jws.common.util.MD5Util;

public class SearchDispatcher {
	
//	private static String urlStr = "http://192.168.1.156:8081/busisystem/entry/busiEntry.do";
//	private static String urlFileStr = "http://192.168.1.153:8080/busisystem/entry/busiFileEntry.do";
//	private static String urlStr = "http://localhost:8080/busisystem/entry/busiEntry.do";
//	private static String urlFileStr = "http://localhost:8080/busisystem/entry/busiFileEntry.do";
//	private static String urlFileStr = "http://localhost:8080/busisystem/factory/upload.do";
	private static String urlFileStr = "http://192.168.1.153:8080/busisystem/factory/upload.do";
	private static String paramKey = "paramObject";

	public static void main(String[] args) throws JSONException {
//		JSONArray jsonArray = readConfig();
//		String dataStr = "This is test data by kevin";
//		dispatcher(jsonArray, dataStr);
		net.sf.json.JSONObject paramObject = new net.sf.json.JSONObject();
		net.sf.json.JSONObject fileObject = new net.sf.json.JSONObject();
		
//		paramObject.put("busiBlock", "searchBlock");
		paramObject.put("busiBlock", "userBlock");
		paramObject.put("time", "2016-01-15 17:32:33");
		paramObject.put("source", "test");
		System.out.println(MD5Util.getMD5String("busiSystemMate"+"2016-01-15 17:32:33"));
		paramObject.put("Md5Str", MD5Util.getMD5String("busiSystemMate"+"2016-01-15 17:32:33"));
		paramObject.put("language", "ZH");
		
		// 3.1	保存搜索条件
//		paramObject.put("busiCode", "saveSearchKey");
//		paramObject.put("userId", "bb9972bd79ad491a8af86ddf323de51d");
//		paramObject.put("keyWord", "火车票售票网址");
		
		// 3.2	保存查看链接
//		paramObject.put("busiCode", "saveSearchViewed");
//		paramObject.put("userId", "bb9972bd79ad491a8af86ddf323de51d");
//		paramObject.put("url", "https://www.baidu.com/");
//		paramObject.put("keyWord", "火车票");
//		paramObject.put("preUrl", "");
//		paramObject.put("titleTxt", "12306铁路官方网址");
//		paramObject.put("abstractTxt", "全球最大的中文搜索引擎、致力于让网民更便捷地获取信息，找到所求。百度超过千亿的中文网页数据库，可以瞬间找到相关的搜索结果。");
		
		// 3.3	查询用户搜索历史
//		paramObject.put("busiCode", "querySearchKey");
//		paramObject.put("userId", "bb9972bd79ad491a8af86ddf323de51d");
////		paramObject.put("keyWord", "5");
//		paramObject.put("pageNum", 2);
//		paramObject.put("currPage", 0);
		
		// 3.4	查询搜索条件对应的所有链接
//		paramObject.put("busiCode", "querySearchViewed");
////		paramObject.put("userId", "bb9972bd79ad491a8af86ddf323de51d");
//		paramObject.put("keyWord", "4");
//		paramObject.put("pageNum", 2);
//		paramObject.put("currPage", 0);
		
//		sendHttpRequest(urlStr, paramKey, paramObject.toString());
		
		// 3.5	保存搜索文件
//		paramObject.put("busiCode", "saveSearchFile");
//		paramObject.put("searchFile", "E:\\work\\searchkey.txt");
//		paramObject.put("fileType", "searchKey");		

//		paramObject.put("busiCode", "saveSearchFile");
//		paramObject.put("searchFile", "D:\\temp\\searchViewed.txt");
//		paramObject.put("fileType", "searchViewed");
		
		//3.7保存用户行为
//		paramObject.put("busiCode", "saveUserRecord");
//		paramObject.put("userRecordFile", "D:\\2016-03-21");
		
		//3.6上传文件
//		paramObject.put("busiCode", "uploadFiles");
//		paramObject.put("userId", "juyyksdf");
//		fileObject.put("fileKey", "E:\\mate_works\\temp\\mate.db");
//		fileObject.put("fileKey", "E:\\mate_works\\temp\\url.db");
		// 1.14	用户反馈
//		paramObject.put("busiCode", "userFeedback");
//		paramObject.put("attitude", "喜欢");
//		paramObject.put("content", "小美确实很好用");
//		paramObject.put("email", "kevin.zhu@jwis.cn");
//		
		fileObject.put("fileKey", "D:\\temp\\123456.png");
		// 
//		paramObject.put("busiCode", "saveSearchShare");
//		paramObject.put("mapId", "23523532");
//		paramObject.put("userId", "b5d10a16827f4072a4489f8c556362e5");
//		paramObject.put("shareTo", "1");
		
		paramObject.put("busiCode", "uploadHeadPhoto");
		paramObject.put("userId", "b5d10a16827f4072a4489f8c556362e5");
		sendHttpRequestWithFile(urlFileStr, paramKey, paramObject.toString(),fileObject);
	}

	/**
	 * 读取第三方系统的配置文件
	 * @return
	 */
//	public static JSONArray readConfig() {
//		File file = new File(INTERFACE_CONFIG_FILE);
//		try {
//			JSONArray json = JSONUtilities.readJSONArray(new FileInputStream(file));
//			return json;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	/**
	 * 分发data 到每一个配置了的接口
	 * @param jsonArray		配置接口 array
	 * @param data			需要分发的 JSONObject data
	 */
	public static void dispatcher(JSONArray jsonArray, JSONObject data) {
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				System.out.println(jsonArray.get(i));
				sendHttpRequest(jsonArray.getJSONObject(i), data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 分发data 到每一个配置了的接口
	 * @param jsonArray		配置接口 array
	 * @param data			需要分发的 String data
	 */
	public static void dispatcher(JSONArray jsonArray, String data) {
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				System.out.println(jsonArray.get(i));
				sendHttpRequest(jsonArray.getJSONObject(i), data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送http 请求
	 * @param interfaceParam		第三方接口的配置参数
	 * @param dataJson				需要分发的 JSONObject dataJson
	 */
	private static void sendHttpRequest(JSONObject interfaceParam, JSONObject dataJson) {
		sendHttpRequest(interfaceParam, dataJson.toString());
	}

	/**
	 * 发送http 请求
	 * @param interfaceParam		第三方接口的配置参数
	 * @param dataJson				需要分发的 String dataStr
	 */
	private static void sendHttpRequest(JSONObject interfaceParam, String dataStr) {
		try {
			sendHttpRequest(interfaceParam.getString("url"), interfaceParam.getString("paramKey"), dataStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 发送http 请求
	 * @param interfaceParam		第三方接口的配置参数
	 * @param dataJson				需要分发的 String dataStr
	 */
	private static void sendHttpRequest(String urlStr, String paramKey, String dataStr) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setReadTimeout(120000);
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Connection", "Keep-Alive");

			// 表单参数提交，输入参数
			StringBuffer params = new StringBuffer();
			params.append(paramKey).append("=").append(dataStr);
			byte[] bytes = params.toString().getBytes();
			connection.getOutputStream().write(bytes);

			int responseCode = connection.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			String result = "";
			while((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response : " + result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void sendHttpRequestWithFile(String urlStr, String paramKey, String dataStr, net.sf.json.JSONObject fileObject) {
		System.out.println(dataStr);

		HashMap<String, String> paraMap = JsonUtil.toHashMap(dataStr);
		
        HttpURLConnection connection = null;
        DataOutputStream outStream = null;

        String SEND_TYPE = "POST";
        int CONNECT_TIME_OUT = 5000;            //连接超时时间
        int READ_TIME_OUT = 120000;            //超时时间
        String CHARSET = "utf-8";        //编码格式
        String PREFIX = "--";
        String LINE_END = "\r\n";
        String BOUNDARY = "--------------Innovation";

        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(READ_TIME_OUT);
            connection.setConnectTimeout(CONNECT_TIME_OUT);
            connection.setRequestMethod(SEND_TYPE);
            connection.setRequestProperty("Charset", CHARSET);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

            outStream = new DataOutputStream(connection.getOutputStream());
            
            if (paraMap.get("busiBlock") != null) {
                StringBuilder sBuffer = new StringBuilder();
                //上传表单参数部分
                for (Map.Entry<String, String> entry : paraMap.entrySet()) {
                	if (entry.getKey().equals("fileKey")) {
                		continue;
                	}
                    sBuffer.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sBuffer.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"").append(LINE_END);
                    sBuffer.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END + LINE_END);
                    sBuffer.append(entry.getValue());
                    sBuffer.append(LINE_END);
                }
                outStream.write(sBuffer.toString().getBytes());
            }
            if (fileObject.size() >0) {
            	
            	ArrayList<UploadObject> files = new ArrayList<>();
            	
            	UploadObject file1 = new UploadObject();
            	file1.formName = "fileKey";
            	file1.contentType = "application/octet-stream";
            	file1.filePath = "D:\\works\\test\\test12.png";
            	files.add(file1);
//            	UploadObject file2 = new UploadObject();
//            	file2.formName = "fileKey";
//            	file2.contentType = "application/octet-stream";
//            	file2.filePath = "E:\\mate_works\\temp\\url.db";
//            	files.add(file2);
//            	files.add(file1);
//            	files.add(file1);
//            	files.add(file1);
//            	files.add(file1);
//            	files.add(file1);
//            	files.add(file1);
//            	files.add(file1);
//            	files.add(file1);
//            	files.add(file1);
            	
                //上传文件部分
                for (UploadObject uploadObject : files) {
                    File file = new File(uploadObject.filePath);
                    if (!file.exists()) {
                        continue;
                    }
                    StringBuilder fileBuffer = new StringBuilder();
                    fileBuffer.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    fileBuffer.append("Content-Disposition: form-data; name=\"").append(uploadObject.formName).append("\"; filename=\"").append(new File(uploadObject.filePath).getName()).append("\"").append(LINE_END);
                    fileBuffer.append("Content-Type: ").append(uploadObject.contentType).append("; charset=").append(CHARSET).append(LINE_END).append(LINE_END);
                    outStream.write(fileBuffer.toString().getBytes());

                    InputStream fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[32 * 1024];
                    int len;
                    while ((len = fileInputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    fileInputStream.close();
                    outStream.write(LINE_END.getBytes());
                }
            }
            String endFlag = PREFIX + BOUNDARY + PREFIX + LINE_END;
            outStream.write(endFlag.getBytes());

            outStream.flush();
            outStream.close();

            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);
            if (responseCode == 200) {
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader bf = new BufferedReader(isr);
                String tmp;
                String result = "";
                while ((tmp = bf.readLine()) != null) {
                    result += tmp;
                }
                System.out.println("response : " + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
	
	static class UploadObject {		
	    public String filePath;     // 文件路径
	    public String formName;     // 表单字段名称
	    public String contentType;  // 内容类型
	}
	
}
