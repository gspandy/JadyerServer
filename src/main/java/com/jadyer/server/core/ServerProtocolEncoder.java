package com.jadyer.server.core;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import com.jadyer.server.util.ConfigUtil;

/**
 * Server端协议编码器
 * @see 用于编码响应给Client的报文
 * @create Dec 21, 2012 1:28:13 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class ServerProtocolEncoder implements MessageEncoder<String> {
	@Override
	public void encode(IoSession session, String message, ProtocolEncoderOutput out) throws Exception {
		String charset = null;
		if(session.getLocalAddress().toString().contains(":" + ConfigUtil.INSTANCE.getProperty("server.port.tcp"))){
			charset = ConfigUtil.INSTANCE.getProperty("sys.charset.tcp");
		}else if(session.getLocalAddress().toString().contains(":" + ConfigUtil.INSTANCE.getProperty("server.port.http"))){
			charset = ConfigUtil.INSTANCE.getProperty("sys.charset.http");
		}else{
			charset = "UTF-8";
		}
		IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);
		buffer.putString(message, Charset.forName(charset).newEncoder());
		buffer.flip();
		out.write(buffer);
	}
}