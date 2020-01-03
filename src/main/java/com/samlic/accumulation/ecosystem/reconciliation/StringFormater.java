package com.samlic.accumulation.ecosystem.reconciliation;

/**
 * 字符串格式化接口定义,用于将不同类型的实例格式化成字符串
 * @author yuanpeng
 *
 */
public interface StringFormater {
	/**
	 * 将对象格式化成字符串
	 * @param data 数据对象
	 * @return 结果字符串
	 */
	String format(Object data);
}
