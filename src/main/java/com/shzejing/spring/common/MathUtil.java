package com.shzejing.spring.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class MathUtil {

//	public static void main(String[] args) throws IOException {
//		List<String> s = new ArrayList<String>();
//		for (int i = 0; i < 50; i++) {
//			s.add(randQ());
//			FileUtils.writeLines(new File("d:/1.csv"), s);
//		}
//	}
	
	public static long random(int min, int max) {
		return Math.round(Math.random()*(max - min) + min);
	}
	
	public static String randQ() {
		long num1,num2,num3;
		num1 = random(100, 999);
		num2 = random(-999, 999);
		while(num1 + num2 < 0 || num1 + num2 > 1000) {
			num2 = random(-999, 999);
		}
		num3 = random(-999, 999);
		while(num1 + num2 + num3 < 0 || num1 + num2 + num3 > 1000) {
			num3 = random(-999, 999);
		}
		return num1 + (num2 < 0 ? (num2 + "") : ("+"+num2)) + (num3 < 0 ? (num3 + "") : ("+"+num3)) + "=," + (num1+num2+num3);
	}
}
