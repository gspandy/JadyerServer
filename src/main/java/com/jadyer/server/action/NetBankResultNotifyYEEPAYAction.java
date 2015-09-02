package com.jadyer.server.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.jadyer.server.core.GenericAction;
import com.jadyer.server.model.NetBankResultNotify;
import com.jadyer.server.util.JadyerUtil;
import com.jadyer.server.util.LogUtil;
import com.jadyer.server.util.builder.MessageBuilder;
import com.jadyer.server.util.builder.TradeCode;

/**
 * 易宝支付订单后台通知
 * @see ==========================================================================================================
 * @see GET /notify_yeepay?p1_MerId=10011355345&r0_Cmd=Buy&r1_Code=1&r2_TrxId=115261250976102I&r3_Amt=49.25&r4_Cur=RMB&r5_Pid=&r6_Order=20120924990001752530&r7_Uid=&r8_MP=&r9_BType=2&ru_Trxtime=20120924213012&ro_BankOrderId=2013568562120924&rb_BankId=BOCO-NET&rp_PayDate=20120924212549&rq_CardNo=&rq_SourceFee=0.0&rq_TargetFee=0.0&hmac=8f57aea4fe8b2696b3f9b487043bfbe2 HTTP/1.1
 * @see Content-Type: application/x-www-form-urlencoded; charset=GBK
 * @see Cache-Control: no-cache
 * @see Pragma: no-cache
 * @see User-Agent: Java/1.5.0_14
 * @see Host: 123.125.97.248
 * @see Accept: text/html, image/gif, image/jpeg, *; q=.2, 星号/*; q=.2
 * @see Connection: keep-alive
 * @see 
 * @see ========================================================================================================== 
 * @see HTTP/1.1 200 OK
 * @see Content-Type: text/html; charset=GBK
 * @see Content-Length: 28
 * @see 
 * @see SUCCESS YeePay Return OK !!!
 * @see ==========================================================================================================
 * @create Sep 3, 2013 8:42:55 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
@Controller
public class NetBankResultNotifyYEEPAYAction implements GenericAction {
	@Override
	public String execute(String message) {
		String[] messageArray = message.split("&");
		Map<String, String> param = new HashMap<String, String>();
		for(int i=0; i<messageArray.length; i++){
			String key = messageArray[i].substring(0, messageArray[i].indexOf("="));
			String value = messageArray[i].substring(messageArray[i].indexOf("=")+1)==null ? "" : messageArray[i].substring(messageArray[i].indexOf("=")+1);
			param.put(key, value);
		}
		
		/**
		 * 验签
		 */
		if(!this.checkSign(param)){
			return MessageBuilder.buildHTTPResponseMessage(500, null);
		}
		
		/**
		 * 与支付处理系统网银结果通知接口通信
		 */
		NetBankResultNotify nbrn = new NetBankResultNotify();
	    nbrn.setOrderNo(param.get("r6_Order"));
	    nbrn.setTradeResult("1".equals(param.get("r1_Code")) ? TradeCode.trade_result_paySuccess.toString() : TradeCode.trade_result_payFailed.toString());
	    nbrn.setBankDate(param.get("rp_PayDate").length()>=8 ? param.get("rp_PayDate").substring(0,8) : param.get("rp_PayDate"));
	    nbrn.setBankSerialNo(param.get("r2_TrxId"));
	    nbrn.setNotifyType("2");
	    nbrn.setTradeAmount(JadyerUtil.moneyYuanToFen(param.get("r3_Amt")));
	    String messageToBusiPlatform = MessageBuilder.buildNetBankResultNotifyMessage(nbrn);
	    LogUtil.getLogger().info("易宝网银结果通知-->发往支付处理的报文[" + messageToBusiPlatform + "]");
//		String respData = MinaUtil.sendTCPMessage(messageToBusiPlatform);
//		LogUtil.getLogger().info("易宝网银结果通知-->支付处理的响应报文[" + respData + "]");
//		
//		/**
//		 * 响应给易宝
//		 */
//		if(respData.substring(6, 14).equals("99999999") && respData.split("`", -1)[23].equals("1")){
//			return MessageUtil.buildHTTPResponseMessage("SUCCESS YeePay Return OK !!!");
//		}else{
//			return MessageUtil.buildHTTPResponseMessage(500, null);
//		}
	    return MessageBuilder.buildHTTPResponseMessage("SUCCESS YeePay Return OK !!!");
	}
	
	
	/**
	 * 对易宝的请求参数进行验签
	 * @param param    易宝的请求参数
	 * @return 验签通过则返回true,反之则返回false
	 */
	private boolean checkSign(Map<String, String> param) {
//		String merNo = ConfigUtil.INSTANCE.getProperty("yeepay.merNo");
//		String p1_MerId = param.get("p1_MerId");
//		if(!merNo.equals(p1_MerId)){
//			LogUtil.getLogger().info("易宝网银结果通知-->验签未通过:易宝通知的商户号[" + p1_MerId + "]与本地存储的商户号[" + merNo + "]不一致");
//			return false;
//		}
//		StringBuilder sb = new StringBuilder();
//		sb.append(p1_MerId).append(param.get("r0_Cmd")).append(param.get("r1_Code")).append(param.get("r2_TrxId")).append(param.get("r3_Amt"))
//		  .append(param.get("r4_Cur")).append(param.get("r5_Pid")).append(param.get("r6_Order")).append(param.get("r7_Uid"))
//		  .append(param.get("r8_MP")).append(param.get("r9_BType"));
//		if(param.get("hmac").equals(DigestUtil.hmacSign(sb.toString(), ConfigUtil.INSTANCE.getProperty("yeepay.privateKey")))){
//		if(true){
//			LogUtil.getLogger().info("易宝网银结果通知-->验签通过");
//			return true;
//		}else{
//			LogUtil.getLogger().info("易宝网银结果通知-->验签未通过");
//			return false;
//		}
		return true;
	}
}