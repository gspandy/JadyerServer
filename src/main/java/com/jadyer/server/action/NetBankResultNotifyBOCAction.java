package com.jadyer.server.action;

import org.springframework.stereotype.Controller;

import com.jadyer.server.core.GenericAction;
import com.jadyer.server.util.LogUtil;
import com.jadyer.server.util.builder.MessageBuilder;

/**
 * 中国银行网银结果通知
 * @create Sep 3, 2013 8:40:54 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
@Controller
public class NetBankResultNotifyBOCAction implements GenericAction{
	@Override
	public String execute(String message) {
		LogUtil.getLogger().info("中行网银结果通知-->收到请求报文[" + message + "]");
		return MessageBuilder.buildHTTPResponseMessage("您已成功连接本系统HTTP服务器...");
	}
}