package com.jws.app.operater.service.impl;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.jws.app.operater.service.FactoryService;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.util.FileUtil;
@Service("factoryService")
public class FactoryServiceImpl implements FactoryService {
	
	private static String json = new String();

	@Override
	public String saveFactory(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			System.out.println(e);
		}
		json = request.getParameter("paramObject");
		return "success";
	}

	@Override
	public String upload(HttpServletRequest request) {
		String filePath = ConfigConstants.UPLOAD_FILE_URL_FORMAL+File.separator+"factory";
		File repositoryFile = new File(filePath);
		try {
			if (!repositoryFile.exists()) {
				repositoryFile.mkdirs();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		List<?> items = null;
		try {
			items = FileUtil.parseRequest(repositoryFile, request);
		} catch (FileUploadException e) {
			System.out.println(e);
		}
		if (null == items || items.size()==0) {
			return "";
		}
		//保存文件
		JSONObject  paramJson = new JSONObject();
		try {
			paramJson = FileUtil.saveStream(repositoryFile, items);
			String fileKey = paramJson.optString("fileKey");
			if (StringUtils.isNotEmpty(fileKey)) {
				fileKey = fileKey.substring(fileKey.lastIndexOf("mate"+File.separator));
				return ConfigConstants.FILE_SERVER+fileKey.replaceAll("\\\\", "/");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return "";
	}

	@Override
	public String getFactory(HttpServletRequest request) {
		return json;
	}

}
