package com.jadyer.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 玄玉服务器启动类
 * @create Sep 3, 2013 8:14:42 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class MainApp {
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("applicationContext.xml");
	}
}