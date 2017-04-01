package com.util;

import org.junit.Assert;
import org.junit.Test;
import com.jws.common.util.MD5Util;

public class MD5UtilTest {

	@Test
	public void test() {
		Assert.assertNull(MD5Util.getMD5String(null));
		String str="2016-01-15 17:32:33" + "busiSystemmate";
		Assert.assertEquals(str, MD5Util.convertMD5(MD5Util.convertMD5(str)));
	}

}
