package com.samlic.accumulation.ecosystem.reconciliation;

/**
 * 明细数据解析器
 * @author yuanpeng
 *
 * @param <T>
 */
public interface DetailHandler<T extends BaseConsumeItem> {
	/**
	 * 解析行数据
	 * @param lineItems
	 * @return 返回数据实体类实例
	 */
	T parse(String[] lineItems);
	
	/**
	 * 执行个性化对账逻辑
	 * @param record
	 * @param datail
	 * @return 成功，失败
	 */
	Result handle(FileHandleRecord record, T datail);
	
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
