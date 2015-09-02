package com.jadyer.server.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jadyer.server.util.ConfigUtil;
import com.jadyer.server.util.JadyerUtil;
import com.jadyer.server.util.LogUtil;

/**
 * 商户订单处理相关的数据库操作
 * @create Dec 18, 2012 9:42:35 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
@Repository("orderDao")
public class OrderDaoJdbc {
	//全局静态变量,用于保存数据库查询时取到的商户签名key
	private static Map<String, Object> merSignKeyMap = new HashMap<String, Object>();
	
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	public void setDataSource(DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	    this.jdbcTemplate.setQueryTimeout(ConfigUtil.INSTANCE.getPropertyForInt("sys.database.timeout.query")); //单位秒
	}
	
	
	/**
	 * 获取商户签名时用到的key
	 * @see 若内存中存在该商户号的签名key,且该key从数据库取出到当前的时间还不到22分钟,则优先从内存中取签名key
	 * @param merNo 商户号
	 * @return 查询正常时返回内存or数据库中保存的签名key<br>查询异常时返回<code>"MER_SIGNKEY_NOTFOUND"</code>
	 */
	public String getMerSignKey(String merNo) {
		String merSignKey = (String)merSignKeyMap.get(merNo);
		if(JadyerUtil.isNotEmpty(merSignKey)){
			Calendar merSignKeySaveTime = (Calendar)merSignKeyMap.get(merNo+"_saveTime");
			merSignKeySaveTime.add(Calendar.MINUTE, 22);
			if((merSignKeySaveTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()) >= 0){
				LogUtil.getLogger().info("获取商户[" + merNo + "]的签名key-->已于内存中取到");
				return merSignKey;
			}
		}
		try{
			String sql = "SELECT mersignpassword FROM t_pay_merinfo WHERE merno=?";
			merSignKey = (String)this.jdbcTemplate.queryForObject(sql, new Object[]{merNo}, String.class);
			merSignKeyMap.put(merNo, merSignKey);
			merSignKeyMap.put(merNo+"_saveTime", Calendar.getInstance());
			LogUtil.getLogger().error("获取商户[" + merNo + "]的签名key-->" + merSignKey.substring(0,4) + "****" + merSignKey.substring(merSignKey.length()-4));
			return merSignKey;
		}catch(CannotGetJdbcConnectionException cgjce){
			LogUtil.getLogger().error("获取商户[" + merNo + "]的签名key-->Could not get JDBC Connection");
    		return "MER_SIGNKEY_NOTFOUND";
		}catch(EmptyResultDataAccessException erdae){
			LogUtil.getLogger().error("获取商户[" + merNo + "]的签名key-->数据库查询结果为空");
			return "MER_SIGNKEY_NOTFOUND";
    	}catch(UncategorizedSQLException ucse){
    		LogUtil.getLogger().error("获取商户[" + merNo + "]的签名key-->数据库查询超时,堆栈轨迹如下", ucse);
    		return "MER_SIGNKEY_NOTFOUND";
    	}
	}

	
	/**
	 * 获取数据库的当前时间
	 * @return 若连接数据库时发生异常,则返回<code>"DATABASE_SELECT_ERROR"</code>字符串
	 */
	public String getDatabaseTime() {
		try{
			return (String)this.jdbcTemplate.queryForObject("SELECT SYSDATE FROM dual", String.class);
		}catch(Exception e){
			return "DATABASE_SELECT_ERROR";
		}
	}
	
	
	/**
	 * 短信信息查询
	 * @param phoneNo 待查询的手机号
	 * @return 短信内容
	 */
	public String getSMSInfo(String phoneNo){
		String statusDesc = "未知";
		Object attimeDesc = "未知";
		Object content = null;
		String sql = "SELECT status,attime,content FROM (SELECT status,attime,content FROM t_pay_smsend WHERE destno=? ORDER BY id DESC) WHERE ROWNUM=1";
		try{
			List<Map<String, Object>> smsInfo = this.jdbcTemplate.queryForList(sql, new Object[]{phoneNo});
			content = smsInfo.get(0).get("CONTENT");
			attimeDesc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(smsInfo.get(0).get("ATTIME"));
			switch (Integer.parseInt(smsInfo.get(0).get("STATUS").toString())) {
				case 0: statusDesc = "未发送"; break;
				case 1: statusDesc = "已发送"; break;
				case 2: statusDesc = "发送失败"; break;
				default: statusDesc = "未知状态码[" + smsInfo.get(0).get("STATUS") + "]"; break;
			}
		}catch(CannotGetJdbcConnectionException cgjce){
			content = "DATABASE_CONNECT_EXCEPTION<br/>\r\n堆栈信息：<textarea style='vertical-align:top; background-color:#CBE0C9' cols='170' rows='30' readonly='true'>" + JadyerUtil.extractStackTrace(cgjce) + "</textarea>";
		}catch(EmptyResultDataAccessException erdae){
			content = "DATABASE_EMPTY_RESULT<br/>\r\n堆栈信息：<textarea style='vertical-align:top; background-color:#CBE0C9' cols='170' rows='30' readonly='true'>" + JadyerUtil.extractStackTrace(erdae) + "</textarea>";
    	}catch(UncategorizedSQLException ucse){
    		content = "DATABASE_SELECT_TIMEOUT<br/>\r\n堆栈信息：<textarea style='vertical-align:top; background-color:#CBE0C9' cols='170' rows='30' readonly='true'>" + JadyerUtil.extractStackTrace(ucse) + "</textarea>";
    	}catch(Exception e){
    		content = "DATABASE_SELECT_ERROR<br/>\r\n堆栈信息：<textarea style='vertical-align:top; background-color:#CBE0C9' cols='170' rows='30' readonly='true'>" + JadyerUtil.extractStackTrace(e) + "</textarea>";
		}
    	StringBuilder respMsg = new StringBuilder();
    	respMsg.append("目标手机：").append(phoneNo).append("<br/>")
    		   .append("发送时间：").append(attimeDesc).append("<br/>")
    		   .append("发送状态：").append(statusDesc).append("<br/>")
    		   .append("短信内容：").append(content);
    	return respMsg.toString();
	}
}