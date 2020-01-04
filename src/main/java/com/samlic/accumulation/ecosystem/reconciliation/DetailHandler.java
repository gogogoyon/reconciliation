package com.samlic.accumulation.ecosystem.reconciliation;

/**
 * 明细数据处理器
 * @author yuanpeng
 *
 * @param <T> 明细记录对象类型
 */
public interface DetailHandler<T extends BaseConsumeItem> {
	/**
	 * 解析行数据
	 * @param lineItems 明细数据
	 * @return 返回数据实体类实例
	 */
	T parse(String[] lineItems);
	
	/**
	 * 执行个性化对账逻辑
	 * @param record 文件处理记录
	 * @param datail 明细记录
	 * @return 处理结果
	 */
	Result handle(FileHandleRecord record, T datail);
	
	/**
	 * 自定义处理结果
	 * @author yuanpeng
	 *
	 */
	static class Result {
		private String retCode;
		private String retDesc;
		public String getRetCode() {
			return retCode;
		}
		public void setRetCode(String retCode) {
			this.retCode = retCode;
		}
		public String getRetDesc() {
			return retDesc;
		}
		public void setRetDesc(String retDesc) {
			this.retDesc = retDesc;
		}
	}
}
