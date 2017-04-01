package com.jws.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import sun.misc.BASE64Decoder;

import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;

@SuppressWarnings("restriction")
public class FileUtil {
	
//	public static Boolean matchPicture(String suffix){
//		if (StringUtils.equals(".jpg", suffix)
//				|| StringUtils.equals(".JPG", suffix)
//				|| StringUtils.equals(".png", suffix)
//				|| StringUtils.equals(".PNG", suffix)
//				|| StringUtils.equals(".jpeg", suffix)
//				|| StringUtils.equals(".JPEG", suffix)) {
//			return true;
//		}
//		return false;
//	}
	
	public static String saveBase64Pic(String picUrl, String allPath){
		 try {
	        	OutputStream os = null;
	        	BASE64Decoder d = new BASE64Decoder();
	        	picUrl = URLDecoder.decode(picUrl,"UTF-8");
	            byte[] bs = d.decodeBuffer(picUrl.split(",")[1]);
	            //配置文件路径，比如：D:\\works\\test\\，正式代码将该路径配置到dataConfig.properties文件内 
//	            String allPath = path+File.separator+name;
	            File file = new File(allPath);
	            if (!file.exists()) {
					file.createNewFile();
				}
	            os = new FileOutputStream(file);
				os.write(bs);
				os.close();
				return allPath.substring(allPath.indexOf(File.separator+ConfigConstants.FIRST_FOLDER_NAME+File.separator)).replaceAll(File.separator, "/");
			} catch (IOException e) {
				System.out.println(e);
			}
		return "";
	}
	
	  public static List<?>  parseRequest(File  repositoryFile, HttpServletRequest request) throws FileUploadException{
			//将请求信息流上传到该路径下
			FileItemFactory factory = new DiskFileItemFactory(1024 * 32, repositoryFile);
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("utf-8");
			upload.setSizeMax(upload.getSizeMax());
			return  upload.parseRequest(request);		
	    }
	  
	  /**
	     * 删除文件夹及文件夹下的文件
	     * @param oldPath
	     */
	    public static  void deleteFile(File oldPath) {
	        if (oldPath.isDirectory()) {
	         File[] files = oldPath.listFiles();
	         for (File file : files) {
	           deleteFile(file);
	         }
	        }else{
	          oldPath.getAbsoluteFile().delete();
	        }
	        oldPath.getAbsoluteFile().delete();
	      }
	    /**
	     * 保存流文件
	     * @param path
	     * @param items
	     * @return
	     * @throws Exception 
	     */
	    public static JSONObject saveStream( File file, List<?> items) throws Exception {
			JSONObject  json = new JSONObject();
			Iterator<?> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					json.put(item.getFieldName(),  item.getString());
				}else {
					if (StringUtils.isEmpty(item.getName())) {
						continue;
					}
					item.write(new File(file, item.getName()));
					json.accumulate(item.getFieldName(), file.getPath()+File.separator+item.getName());
				}
			}	
			return json;
		}
	    /**
	     * 返回对象是 Exception 说明程序异常，返回true,说明是append旧文件后面，false，说明是新增文件
	     * @param currRecordPath
	     * @param paraMap
	     * @return
	     */
	    public  static  Object  upLoadHdfs(String currRecordPath, JSONObject paraMap ){
	    	Path path = new Path(currRecordPath);
			Configuration conf = new Configuration();
			FileSystem fs = null;
			InputStream in = null;
			OutputStream out = null;
			Boolean  isUpdate = false;
			// 临时文件路径
			String tempFilePath = null;
			//保存用户行为，每天一个文件
			if (StringUtils.equals("saveUserRecord", paraMap.getString("busiCode"))) {
				tempFilePath = paraMap.optString("userRecordFile");
			} else if (StringUtils.equals("saveSearchFile", paraMap.getString("busiCode")))  {
				tempFilePath = paraMap.optString("searchFile");
			}
			try {
				//判断文件是否已经存在,无，直接上传，已存在，append到原文件后面
				fs = FileSystem.get(URI.create(currRecordPath), conf);
				if (fs.exists(path)) {
					in = new  BufferedInputStream(new	FileInputStream(tempFilePath));
					out = fs.append(new	Path(currRecordPath));
					IOUtils.copyBytes(in, out, 4096, true);
					 isUpdate = true;
				}else {
					fs.copyFromLocalFile(false, new Path(tempFilePath), path) ; 
				}
			} catch (Exception e) {
				return e;
//				logger.error("上传文件到hdfs异常"+e.getMessage());
//				return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1005, null, paraMap.getString("language"));
			}finally{
				try {
					if (null != in) {
						in.close();
					}
					if (null != out) {
						out.close();
					}
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	    	return isUpdate;
	    }
	    
	    /**
	     * 返回对象是 Exception 说明程序异常，返回true,说明是append旧文件后面，false，说明是新增文件
	     * @param paraMap
	     * @return
	     */
//	    public  static  JSONObject  upLoadFolder(JSONObject paraMap ){
//	    	JSONObject json = new JSONObject();
//			File file = null;
//			InputStream in = null;
//			FileOutputStream  out = null;
//			// 复制到正式目录下是否追加
//			Boolean isAppend = true;
//			Boolean isUpdate = true;
//			// 临时文件路径
//			String tempFilePath = null;
//			// 正式路径
//			String newPath = ConfigConstants.FILE_SAVE_URL_FORMAL;
//			
//			try {
//				//保存用户行为，每天一个文件
//				if (StringUtils.equals("saveUserRecord", paraMap.getString("busiCode"))) {
//					//paraMap.getString("day") = "2016-01-29"
//					tempFilePath = paraMap.getString("userRecordFile");
//					newPath = newPath + File.separator + Constants.MATE_RECORD_PATH + File.separator + paraMap.getString("day");
//				} else if (StringUtils.equals("saveSearchFile", paraMap.getString("busiCode")))  {
//					tempFilePath = paraMap.optString("searchFile");
//					newPath = newPath + File.separator + Constants.MATE_SEARCH_FILE_PATH + File.separator + paraMap.getString("day") + "_" + paraMap.optString("fileType");
//				} else {
//					json.put("code", "-1");
//					return json;
//				}
//				file =new File(newPath);
//				if (!file.exists()) {
//					isAppend = false;
//					if (!file.getParentFile().exists()) {
//						file.getParentFile().mkdirs();
//					}
//					isUpdate = false;
//				}
//				in = new  BufferedInputStream(new FileInputStream(tempFilePath));
//				out = new FileOutputStream(file, isAppend);
//				IOUtils.copyBytes(in, out, 4096, true);
//				json.put("path", newPath);
//				
//			} catch (Exception e) {
//				System.out.println(e);
//				json.put("code", "-2");
//				json.put("object", e);
//				return json;
//			}finally{
//				try {
//					if (null != in) {
//						in.close();
//					}
//					if (null != out) {
//						out.close();
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			//删除临时文件
////			if (StringUtils.equals("saveUserRecord", paraMap.getString("busiCode"))){
//				File  temp = new File(tempFilePath);
//				FileUtil.deleteFile(temp.getParentFile());
////			}
//			// 组装返回json
//			json.put("code", "1");
//			json.put("update", isUpdate);
//	    	return json;
//	    }
	    
	    public  static  JSONObject  upLoadMoreFolder(JSONObject paraMap ){
	    	JSONObject json = new JSONObject();
			InputStream in = null;
			FileOutputStream  out = null;
			// 正式路径
			String newPath = ConfigConstants.UPLOAD_FILE_URL_FORMAL;
			JSONObject result = new JSONObject();
			try {
				if (StringUtils.equals(BusiCodeConstant.USER_FEEDBACK, paraMap.getString("busiCode"))) {
					newPath = newPath + File.separator + Constants.MATE_UPLOAD_PATH + File.separator + paraMap.getString("month");
				}else if(StringUtils.equals(BusiCodeConstant.UPLOAD_FILES, paraMap.getString("busiCode"))){
					newPath = newPath + File.separator + Constants.MATE_RECORD_PATH + File.separator + paraMap.getString("userId");
				}else if(StringUtils.equals(BusiCodeConstant.SAVE_SEARCH_SHARE, paraMap.getString("busiCode"))){
					newPath = newPath + File.separator + Constants.MATE_SHARE_PATH + File.separator + paraMap.getString("userId");
				}else if(StringUtils.equals(BusiCodeConstant.USER_UPLOAD_HEAD_PHOTO, paraMap.getString("busiCode"))){
					newPath = newPath + File.separator + Constants.USER_HEAD_PHOTO + File.separator + paraMap.getString("userId");
				} else {
					json.put("code", "-1");
					return json;
				}
				File file =new File(newPath);
				if (!file.exists()) {
					file.mkdirs();
				}
				File tempFile;
				JSONArray fileArray = new JSONArray();				
				Object fileKey = paraMap.opt("fileKey");
				if (fileKey instanceof JSONArray) {
					fileArray = (JSONArray)fileKey;
					tempFile = new File(fileArray.getString(0));
				} else {
					fileArray.add(fileKey);
					tempFile = new File(fileKey.toString());
				}
				for (int i = 0; i < fileArray.size(); i++) {
					String tempPath = fileArray.get(i).toString();
					in = new  BufferedInputStream(new FileInputStream(tempPath));
					//获取后缀
					String suffix = "";
					if (tempPath.substring(tempPath.lastIndexOf(File.separator)).lastIndexOf(".")>-1) {
						suffix = tempPath.substring(tempPath.lastIndexOf("."));
					}
					String uuid = UUID.randomUUID().toString().replace("-", "");
					if (StringUtils.isNotEmpty(suffix)) {
						uuid +=suffix;
					}
					File newFile =null;
					String realpath = newPath;
					if (StringUtils.equals(BusiCodeConstant.UPLOAD_FILES, paraMap.getString("busiCode"))) {
						realpath = realpath+File.separator+paraMap.getString("month")+tempPath.substring(tempPath.lastIndexOf(File.separator)+1);
						newFile =new File(realpath);
					}else if (StringUtils.equals(BusiCodeConstant.USER_UPLOAD_HEAD_PHOTO, paraMap.getString("busiCode"))) {
						realpath = realpath+File.separator+paraMap.getString("month");
						if (tempPath.substring(tempPath.lastIndexOf(File.separator)).lastIndexOf(".")>-1) {
							realpath = realpath+tempPath.substring(tempPath.lastIndexOf("."));
						}
						newFile =new File(realpath);
					}else {
						realpath = realpath+File.separator+uuid;
						newFile =new File(realpath);
					}
					
					out = new FileOutputStream(newFile, false);
					IOUtils.copyBytes(in, out, 4096, true);
					String httpPath = realpath.substring(realpath.lastIndexOf(ConfigConstants.FIRST_FOLDER_NAME)).replaceAll("\\\\", "/");
					if (StringUtils.equals(BusiCodeConstant.USER_FEEDBACK, paraMap.getString("busiCode"))) {
						result.accumulate("fileKey", ConfigConstants.FILE_SERVER+httpPath);
					}else{
						result.accumulate("fileKey", httpPath);
					}
//					System.out.println(ConfigConstants.FILE_SERVER+httpPath+"/"+uuid);
				}
				Object resultFileKey = result.opt("fileKey"); 
				if (resultFileKey instanceof String) {
					JSONArray resultArray = new JSONArray();
					resultArray.add(resultFileKey);
					result.put("fileKey", resultArray);
				}
				//删除临时文件
				FileUtil.deleteFile(tempFile.getParentFile());
			} catch (Exception e) {
				System.out.println(e);
				json.put("code", "-2");
				json.put("object", e);
				return json;
			}finally{
				try {
					if (null != in) {
						in.close();
					}
					if (null != out) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// 组装返回json
			json.put("result", result);
			json.put("code", "1");
	    	return json;
	    }
	    
	    public  static  void deleteTempFile(JSONObject paraMap){
	    	if (paraMap.has("fileKey")) {
	    		JSONArray fileArray = new JSONArray();
	    		Object fileKey = paraMap.opt("fileKey");
				if (fileKey instanceof JSONArray) {
					fileArray = (JSONArray)fileKey;
				} else {
					fileArray.add(fileKey);
				}
				for (int i = 0; i < fileArray.size(); i++) {
					File tempFile = new File(fileArray.getString(i));
					//删除临时文件
					FileUtil.deleteFile(tempFile.getParentFile());
				}
			}
	    }    

}
