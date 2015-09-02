package com.jadyer.server.core;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * 组装服务端的编解码器的工厂
 * @see 暂不提供客户端编解码器,目前系统使用com.cucpay.fundswap.util.MinaUtil应对客户端场景
 * @see ====================================================================================
 * @see 其内部维护了一个MessageDecoder数组,用于保存添加的所有解码器
 * @see 每次decode()的时候就调用每个MessageDecoder的decodable()逐个检查
 * @see 只要发现一个MessageDecoder不是对应的解码器,就从数组中移除,知道找到合适的MessageDecoder
 * @see 如果最后发现数组为空,就表示没有找到对应的MessageDecoder,最后抛出异常
 * @see ====================================================================================
 * @create Dec 22, 2012 7:24:47 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class ServerProtocolCodecFactory extends DemuxingProtocolCodecFactory {
	public ServerProtocolCodecFactory(){
		super.addMessageEncoder(String.class, ServerProtocolEncoder.class);
		super.addMessageDecoder(ServerProtocolTCPDecoder.class);
		super.addMessageDecoder(ServerProtocolHTTPDecoder.class);
	}
}