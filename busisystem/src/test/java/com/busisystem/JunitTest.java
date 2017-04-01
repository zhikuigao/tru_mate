package com.busisystem;

import java.util.UUID;

public class JunitTest {
	
	
	
	
	public static void main(String[] args) {
		
//		System.out.println(TestEnum.User);
		String id= UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println(id);
	}

}
