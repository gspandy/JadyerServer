package com.jadyer.server;

import java.util.Enumeration;
import java.util.Properties;

import org.junit.Test;

import com.jadyer.server.util.HttpUtil;
import com.jadyer.server.util.MinaUtil;

/**
 * Server端单元测试类
 * @see 仅提供本系统充当Server时的相关接口单元测试
 * @create Dec 21, 2012 4:45:16 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class ServerTest {
	@Test
	public void commonTest(){
		Properties properties = System.getProperties();
		Enumeration<Object> enums = properties.keys();
		while(enums.hasMoreElements()){
			Object key = enums.nextElement();
			System.out.println(key + "=" + properties.get(key));
		}
		System.out.println("---------" + System.getProperty("java.io.tmpdir"));
	}
	
	
	/**
	 * 模拟Client发起TCP请求本系统
	 */
	@Test
	public void TCPRequestTest(){
		String message = "0003721000510110199201209222240000020120922000069347814303000700000813``中国联通交费充值`为号码18655228826交费充值100.00元`UDP1209222238312219411`10000```20120922`chinaunicom-payFeeOnline`UTF-8`20120922223831`MD5`20120922020103806276`1`02`10000`20120922223954`20120922`BOCO_B2C```http://192.168.20.2:5545/ecpay/pay/elecChnlFrontPayRspBackAction.action`1`立即支付,交易成功`";
		String respData = MinaUtil.sendTCPMessage(message, "127.0.0.1", 9901, "GB18030");
		System.out.println("收到服务端反馈[" + respData + "]");
	}
	
	
	/**
	 * 模拟Client发起HTTP_GET请求本系统
	 */
	@Test
	public void HTTPGetRequestTest(){
		String reqURL = "http://127.0.0.1/notify_boc?username=admin&password=123456";
		String respData = HttpUtil.sendGetRequest(reqURL);
		System.out.println("收到服务端反馈[" + respData + "]");
	}
	
	
	/**
	 * 模拟Client发起HTTP_POST请求本系统
	 */
	@Test
	public void HTTPPostRequestTest(){
		String reqURL = "http://127.0.0.1/notify_boc";
		String sendData = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		String respData = HttpUtil.sendPostRequest(reqURL, sendData, "UTF-8");
		System.out.println("收到服务端反馈[" + respData + "]");
	}
}