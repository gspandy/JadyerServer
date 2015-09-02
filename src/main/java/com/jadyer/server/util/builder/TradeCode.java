package com.jadyer.server.util.builder;

/**
 * 资金交换平台专用交易码
 * @see -----------------------------------------------------------------------------------------------------------
 * @see 枚举类中的这种写法,会使其自动利用构造函数传参,故此时要提供一个构造函数,并且为private的
 * @see 接下来就要定义一个私有变量,用于接收通过构造函数传进来的参数
 * @see 最后可以通过重写toString()或是自定义一个新方法(如getValue(){..})用于获取枚举类型的值
 * @see 在这个方法的内部,只要做一件事就可以了:返回通过构造函数传入的参数
 * @see 最后要注意的是:此时枚举序列的尾部要添加一个分号
 * @see -----------------------------------------------------------------------------------------------------------
 * @create Dec 26, 2012 12:06:01 AM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public enum TradeCode {
	trade_result_payFailed  ("0"),                //交易结果:支付失败,该值仅适用于支付处理系统的网银结果通知接口
	trade_result_paySuccess ("1"),                //交易结果:支付成功,该值仅适用于支付处理系统的网银结果通知接口
	mer_illegal_merno       ("ILLEGAL_MERNO"),    //商户传过来的商户号有误
	mer_illegal_argument    ("ILLEGAL_ARGUMENT"); //商户传过来的参数为空or参数内容不正确
	
	private String code;
	private TradeCode(String _code){
		this.code = _code;
	}
	@Override
	public String toString(){
		return this.code;
	}
}