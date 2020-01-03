package com.samlic.accumulation.ecosystem.reconciliation;

import java.util.List;

/**
 * 定义生产对账文件数据的统一视图
 * @author yuanpeng
 *
 */
public interface Iteratorable {
	/**
	 * 关键字列表，定义了数据列在对账文件中顺序
	 * @return 关键字列表
	 */
	List<String> getKeyList();
	/**
	 * 根据关键字获取对应的值
	 * @param name 关键字
	 * @return 数据对象
	 */
	Object getValue(String name);	
}
