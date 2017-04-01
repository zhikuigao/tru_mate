package com.model;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.jws.app.operater.model.AppInfo;

public class AppInfoTest {
	
	private static AppInfo app = new AppInfo();
	
	@Test
	public void testGetId() {
		app.setId("1234");
		Assert.assertEquals("1234", app.getId());
	}

//	@Test
	public void testSetId() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetTypeId() {
		app.setTypeId("1234");
		Assert.assertEquals("1234", app.getTypeId());
	}

//	@Test
	public void testSetTypeId() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetName() {
		app.setName("1234");
		Assert.assertEquals("1234", app.getName());
	}

//	@Test
	public void testSetName() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetSize() {
		app.setSize(12);
		Assert.assertEquals("12", app.getSize().toString());
	}

//	@Test
	public void testSetSize() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetLanguage() {
		app.setLanguage("zh");
		Assert.assertEquals("zh", app.getLanguage());
	}

//	@Test
	public void testSetLanguage() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetAppTag() {
		app.setAppTag("hdfs");
		Assert.assertEquals("hdfs", app.getAppTag());
	}

//	@Test
	public void testSetAppTag() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetDescribe() {
		app.setDescribe("hdfs");
		Assert.assertEquals("hdfs", app.getDescribe());
	}

//	@Test
	public void testSetDescribe() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetApplyPlatform() {
		app.setApplyPlatform("hdfs");
		Assert.assertEquals("hdfs", app.getApplyPlatform());
	}

//	@Test
	public void testSetApplyPlatform() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetCompanyUrl() {
		app.setCompanyUrl("hdfs");
		Assert.assertEquals("hdfs", app.getCompanyUrl());
	}

//	@Test
	public void testSetCompanyUrl() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetIconUrl() {
		app.setIconUrl("hdfs");
		Assert.assertEquals("hdfs", app.getIconUrl());
	}

//	@Test
	public void testSetIconUrl() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetDownloadUrl() {
		app.setDownloadUrl("hdfs");
		Assert.assertEquals("hdfs", app.getDownloadUrl());
	}

//	@Test
	public void testSetDownloadUrl() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetDownloadCount() {
		app.setDownloadCount(2);
		Assert.assertEquals("2", app.getDownloadCount().toString());
	}

//	@Test
	public void testSetDownloadCount() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetVoteCount() {
		app.setVoteCount(123);
		Assert.assertEquals("123", app.getVoteCount().toString());
	}

//	@Test
	public void testSetVoteCount() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetFlag() {
		app.setFlag("1");
		Assert.assertEquals("1", app.getFlag());
	}

//	@Test
	public void testSetFlag() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetCreateTime() {
		app.setCreateTime(new Date());
		Assert.assertNotNull(app.getCreateTime());
	}

//	@Test
	public void testSetCreateTime() {
		Assert.assertEquals("1234", "1234");
	}

	@Test
	public void testGetUpdateTime() {
		app.setUpdateTime(new Date());
		Assert.assertNotNull(app.getUpdateTime());
	}

//	@Test
	public void testSetUpdateTime() {
		Assert.assertEquals("1234", "1234");
	}

}
