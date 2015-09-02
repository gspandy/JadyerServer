package com.jadyer.server.action;

import org.springframework.stereotype.Controller;

import com.jadyer.server.core.GenericAction;
import com.jadyer.server.util.LogUtil;
import com.jadyer.server.util.builder.MessageBuilder;

/**
 * 商户订单结果通知
 * @create Sep 3, 2013 8:38:30 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
@Controller
public class OrderResultNotifyAction implements GenericAction{
	@Override
	public String execute(String message) {
		LogUtil.getLogger().info("商户订单结果通知-->收到请求报文[" + message + "]");
		return MessageBuilder.buildHTTPResponseMessage("您已成功连接本系统TCP服务器...");
	}
}