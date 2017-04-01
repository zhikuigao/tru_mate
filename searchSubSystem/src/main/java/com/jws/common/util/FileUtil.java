package com.jws.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

public class FileUtil {
	
	/**
     * 生产文件 如果文件所在路径不存在则生成路径
     * 
     * @param fileName
     *            文件名 带路径
     * @param isDirectory 是否为路径
     * @return
     * @author yayagepei
     * @date 2008-8-27
     */
	public static File buildFile(String fileName, boolean isDirectory) {
		File target = new File(fileName);
		if (isDirectory) {
			target.mkdirs();
		} else {
			if (!target.getParentFile().exists()) {
				target.getParentFile().mkdirs();
				target = new File(target.getAbsolutePath());
			}
		}
		return target;
	}
    
    public static List<?> parseRequest(File repositoryFile, HttpServletRequest request) throws FileUploadException {
		// 将请求信息流上传到该路径下
		FileItemFactory factory = new DiskFileItemFactory(1024 * 32, repositoryFile);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("utf-8");
		upload.setSizeMax(upload.getSizeMax());
		return upload.parseRequest(request);
	}

	/**
	 * 删除文件夹及文件夹下的文件
	 * 
	 * @param oldPath
	 */
	public static void deleteFile(File oldPath) {
		if (oldPath.isDirectory()) {
			File[] files = oldPath.listFiles();
			for (File file : files) {
				deleteFile(file);
			}
		} else {
			oldPath.getAbsoluteFile().delete();
		}
		oldPath.getAbsoluteFile().delete();
	}

	/**
	 * 保存流文件
	 * 
	 * @param path
	 * @param items
	 * @return
	 * @throws Exception
	 */
	public static JSONObject saveStream(File file, List<?> items) throws Exception {
		JSONObject json = new JSONObject();
		Iterator<?> iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			if (item.isFormField()) {
				json.put(item.getFieldName(), item.getString());
			} else {
				item.write(new File(file, item.getName()));
				json.put(item.getFieldName(), file.getPath() + File.separator + item.getName());
			}
		}
		return json;
	}
	
	/**
     * 从指定文件中读取内容
     * @param fromFile 目标文件
     * @return -1：失败； 0：无记录； 1： 成功；
     */
    public static JSONObject readFromFile(File fromFile) {
    	JSONObject resultObject = new JSONObject();
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        if (!fromFile.exists()) {
        	resultObject.put("code", "-1");
        	return resultObject;
        }
        
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fromFile), "gbk"));
            String line;
            while((line= bufferedReader.readLine()) != null && line.length() > 0) {
                result.append(line);
            }
        } catch (IOException e) {
        	resultObject.put("code", "-1");
        	return resultObject;
        } finally {
            if(bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String resultStr = result.toString();
        if (StringUtils.isEmpty(resultStr)) {
        	resultObject.put("code", "0");
        } else {
	    	resultObject.put("code", "1");
        }
    	resultObject.put("result", resultStr);
    	return resultObject;
    }
}
