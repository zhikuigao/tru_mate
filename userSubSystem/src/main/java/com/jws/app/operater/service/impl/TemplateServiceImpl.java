package com.jws.app.operater.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jws.app.operater.data.template.IdeaAttachHistoryMapper;
import com.jws.app.operater.service.ITemplateService;

@Service("templateService")
public class TemplateServiceImpl implements ITemplateService{
	@Resource
	private IdeaAttachHistoryMapper ideaAttachHistoryMapper;
	@Override
	public List<HashMap<String, Object>> selectAll() {
		// TODO Auto-generated method stub
		return ideaAttachHistoryMapper.selectAll();
	}

}
