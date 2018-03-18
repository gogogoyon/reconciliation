package com.samlic.accumulation.ecosystem.reconciliation;

import java.util.List;

/**
 * 数据获取器，支持分页获取
 * @author yuanpeng
 *
 */
public interface DataFetcher {
	/**
	 * 获取所有数据
	 * @return
	 */
	List<Iteratorable> getAllDatas();
	/**
	 * 是否支持分页
	 * @return
	 */
	boolean isPageSupported();
	/**
	 * 分页数据记录数，若不支持分页，抛出UnsupportedOperationException异常
	 * @return
	 */
	int getPageSize();
	/**
	 * 数据总记录数
	 * @return
	 */
	int getTotalCount();
	/**
	 * 分页获取数据记录，若不支持分页，抛出UnsupportedOperationException异常
	 * @param start  开始记录
	 * @param count  记录数量
	 * @return
	 */
	List<Iteratorable> getPageData(int start, int count);
}
