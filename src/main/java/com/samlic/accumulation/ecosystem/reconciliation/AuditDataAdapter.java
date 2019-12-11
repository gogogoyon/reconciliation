package com.samlic.accumulation.ecosystem.reconciliation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * AuditIterator适配器
 * @author yuanpeng
 *
 */
public class AuditDataAdapter {
	
	private AuditDataAdapter() {}
	
	/**
	 * 适配分页获取数据
	 * @param dataFetcher
	 * @param keyList
	 * @param formatMap
	 * @return
	 */
	public static Iterator<AuditIterator> adapt(final DataFetcher dataFetcher, final List<String> keyList) {
		return adapt(dataFetcher, keyList, null);
	}
	
	/**
	 * 适配分页获取数据
	 * @param dataFetcher
	 * @param keyList
	 * @param formatMap
	 * @return
	 */
	public static Iterator<AuditIterator> adapt(final DataFetcher dataFetcher, final List<String> keyList,
			final Map<Class<?>, StringFormater> formatMap) {
		
		final int totalCount = dataFetcher.getTotalCount();
		final int pageSize = dataFetcher.getPageSize();
		if(dataFetcher.isPageSupported() && totalCount > pageSize) {
			return new Iterator<AuditIterator>() {
				int i = 0;
				Iterator<Iteratorable> dataIterator;
				
				@Override
				public boolean hasNext() {				
					return i < totalCount;
				}
				
				@Override
				public AuditIterator next() {
					if(i % pageSize == 0) {						
						dataIterator = dataFetcher.getPageData(i, pageSize).iterator();
					}
					
					if(!dataIterator.hasNext()) {
						throw new IllegalStateException("No more data.");
					}
					
					i++;					
				
					return AuditDataAdapter.adapt(dataIterator.next(), formatMap);				
				}
				
				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
		
		return adapt(dataFetcher.getAllDatas(), formatMap);
	}
	
	/**
	 * 适配固定MAP数据列表
	 * @param dataList  存储在Map结构中的数据列表
	 * @param keyList 迭代数据的Key列表
	 * @return 返回Map数据的对账迭代器
	 */
	public static Iterator<AuditIterator> adapt(List<Map<String, Object>> dataList, final List<String> keyList) {
		return adapt(dataList, keyList, null);
	}
	
	/**
	 * 适配固定MAP数据列表
	 * @param dataList  存储在Map结构中的数据列表
	 * @param keyList 迭代数据的Key列表
	 * @param formatMap 提供Object格式化类
	 * @return 返回Map数据的对账迭代器
	 */
	public static Iterator<AuditIterator> adapt(List<Map<String, Object>> dataList, final List<String> keyList,
			final Map<Class<?>, StringFormater> formatMap) {
		List<AuditIterator> list = new ArrayList<>();
		
		if(dataList != null) {
			for(final Map<String, Object> data : dataList) {
				AuditIterator auditIter = adapt(data, keyList, formatMap);			
				list.add(auditIter);
			}
		}
		
		return list.iterator();
	}
	
	/**
	 * 适配MAP数据
	 * @param data  存储在Map结构中的数据
	 * @param keyList 迭代数据的Key列表
	 * @param formatMap 提供Object格式化类
	 * @return 返回Map数据的对账迭代器
	 */
	public static AuditIterator adapt(final Map<String, Object> data, final List<String> keyList,
			final Map<Class<?>, StringFormater> formatMap) {
		return adapt(new MapIteratorableWrapper(data, keyList), formatMap);
	}
	
	/**
	 * 适配Iteratorable列表数据
	 * @param data  Iteratorable列表数据
	 * @param keyList 迭代数据的Key列表
	 * @return 返回Map数据的对账迭代器
	 */
	public static Iterator<AuditIterator> adapt(List<Iteratorable> dataList) {		
		return adapt(dataList, null);
	}
	
	/**
	 * 适配Iteratorable列表数据
	 * @param data  Iteratorable列表数据
	 * @param keyList 迭代数据的Key列表
	 * @param formatMap 提供Object格式化类
	 * @return 返回Map数据的对账迭代器
	 */
	public static Iterator<AuditIterator> adapt(List<Iteratorable> dataList,
			final Map<Class<?>, StringFormater> formatMap) {
		List<AuditIterator> list = new ArrayList<>();
		
		if(dataList != null) {
			for(final Iteratorable data : dataList) {
				AuditIterator auditIter = adapt(data, formatMap);			
				list.add(auditIter);
			}
		}
		
		return list.iterator();
	}
	
	/**
	 * 适配Iteratorable数据
	 * @param data  Iteratorable形式的数据
	 * @param keyList 迭代数据的Key列表
	 * @param formatMap 提供Object格式化类
	 * @return 返回Map数据的对账迭代器
	 */
	public static AuditIterator adapt(final Iteratorable data, 
			final Map<Class<?>, StringFormater> formatMap) {
		return new AuditIterator() {
			int i = 0;
			List<String> keyList = data.getKeyList();
			int length = (data == null || keyList == null) ? 0 : keyList.size();
			
			@Override
			public boolean hasNext() {
				return i < length;
			}

			@Override
			public String next() {
				if(!hasNext()) {
					throw new NoSuchElementException();
				}
				
				String name = keyList.get(i++);				
				return objectToString(data.getValue(name), formatMap);
			}
			
		};
	}
	
	private static String objectToString(Object obj, Map<Class<?>, StringFormater> formatMap) {
		if(obj == null) return "";
		
		String result = obj.toString();
		
		if(formatMap != null && formatMap.containsKey(obj.getClass())) {
			StringFormater format = formatMap.get(obj.getClass());
			result = format.format(obj);
		}
		
		return result;
	}
}
