package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AuditFileTest {

	/**
	 * 测试Map类型的数据源
	 */
	@Test
	public void testMapData() {
		String fileNamePattern = "order_detail_map.dat";
		List<String> keyList = new ArrayList<String>();
		keyList.add("orderId");
		keyList.add("cost");
		keyList.add("status");
		keyList.add("payTime");
		keyList.add("paySequence");
		keyList.add("createTime");
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < 10; i ++) {
			Map<String, Object> data = new HashMap<String, Object>();			
			data.put("status", "6");
			data.put("cost", "100.00");		
			data.put("orderId", "32hfy982r2u0203" + i);
			data.put("payTime", "2018030714000" + i);
			data.put("createTime", "2018030713583" + i);
			data.put("paySequence", "fdairwfe94923" + i);		
			dataList.add(data);
		}
		
		AuditFileBuilder auditFileBuilder = AuditFactory.createFileBuilder(fileNamePattern, dataList, keyList);
		
		AuditFactory.createProducer(auditFileBuilder.period(AuditPeriod.Day)).produce();
		
		File file = auditFileBuilder.getResult();
		System.out.println(file.getAbsolutePath());
		AuditFileResolver auditFileResolver = AuditFactory.createFileResolver(file);
		AuditFactory.createConsumer(auditFileResolver, new MyDetailHandler()).consume();
	}
	
	/**
	 * 测试Iteratorable类型的数据源
	 */
	@Test
	public void testIteratorableData() {
		String fileNamePattern = "order_detail_iteratorable.dat";
		
		List<Iteratorable> dataList = new ArrayList<Iteratorable>();
		Date currentDate = new Date();
		for(int i = 0; i < 10; i ++) {
			TestEntity data = new TestEntity();			
			data.setStatus(6);
			data.setCost(new BigDecimal("100.00"));
			data.setOrderId("32hfy982r2u0203" + i);
			data.setPayTime(currentDate);
			data.setCreateTime(currentDate);
			data.setPaySequence("fdairwfe94923" + i);		
			dataList.add(data);
		}
		
		Map<Class<?>, StringFormater> formatMap = new HashMap<>();
		formatMap.put(Date.class, new StringFormater() {

			@Override
			public String format(Object data) {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				return format.format(data);
			}
			
		});
		
		formatMap.put(BigDecimal.class, new StringFormater() {

			@Override
			public String format(Object data) {
				DecimalFormat format = new DecimalFormat("0.00");
				return format.format(data);
			}
			
		});
		
		AuditFileBuilder auditFileBuilder = AuditFactory.createFileBuilder(fileNamePattern, dataList, formatMap);
		
		AuditFactory.createProducer(auditFileBuilder.period(AuditPeriod.Day)).produce();
		
		File file = auditFileBuilder.getResult();
		System.out.println(file.getAbsolutePath());
		AuditFileResolver auditFileResolver = AuditFactory.createFileResolver(file);
		AuditFactory.createConsumer(auditFileResolver, new MyDetailHandler()).consume();
	}
	
	static class TestEntity extends IteratorableEntityBean {
		private String orderId;
		private BigDecimal cost;
		private int status;
		private Date payTime;
		private String paySequence;
		private Date createTime;

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public BigDecimal getCost() {
			return cost;
		}

		public void setCost(BigDecimal cost) {
			this.cost = cost;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public Date getPayTime() {
			return payTime;
		}

		public void setPayTime(Date payTime) {
			this.payTime = payTime;
		}

		public String getPaySequence() {
			return paySequence;
		}

		public void setPaySequence(String paySequence) {
			this.paySequence = paySequence;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		@Override
		public List<String> getKeyList() {
			List<String> keyList = new ArrayList<String>();
			keyList.add("orderId");
			keyList.add("cost");
			keyList.add("status");
			keyList.add("payTime");
			keyList.add("paySequence");
			keyList.add("createTime");
			
			return keyList;
		}		
	}
	
	/**
	 * 数据实体类
	 * @author yuanpeng
	 *
	 */
	static class MyConsumeItem extends BaseConsumeItem {
		private String orderId;
		private BigDecimal cost;
		private int status;
		private Date payTime;
		private String paySequence;
		private Date createTime;
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public BigDecimal getCost() {
			return cost;
		}
		public void setCost(BigDecimal cost) {
			this.cost = cost;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public Date getPayTime() {
			return payTime;
		}
		public void setPayTime(Date payTime) {
			this.payTime = payTime;
		}
		public String getPaySequence() {
			return paySequence;
		}
		public void setPaySequence(String paySequence) {
			this.paySequence = paySequence;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		@Override
		public String toString() {
			return "MyConsumeItem [orderId=" + orderId + ", cost=" + cost + ", status=" + status + ", payTime="
					+ payTime + ", paySequence=" + paySequence + ", createTime=" + createTime + ", toString()="
					+ super.toString() + "]";
		}
	}
	
	/**
	 * 单数据项处理类
	 * @author yuanpeng
	 *
	 */
	static class MyDetailHandler implements DetailHandler<MyConsumeItem> {
		@Override
		public MyConsumeItem parse(String[] lineItems) {
			int i = 0;
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			MyConsumeItem comsumeItem = new MyConsumeItem();
			comsumeItem.orderId = lineItems[i++];
			comsumeItem.cost = new BigDecimal(lineItems[i++]);
			comsumeItem.status = Integer.parseInt(lineItems[i++]);
			try {
				comsumeItem.payTime = format.parse(lineItems[i++]);			
			} catch (ParseException e) {
				System.err.println(e.toString());
			}
			comsumeItem.paySequence = lineItems[i++];
			try {					
				comsumeItem.createTime = format.parse(lineItems[i++]);					
			} catch (ParseException e) {
				System.err.println(e.toString());
			}
			
			return comsumeItem;
		}

		@Override
		public Result handle(FileHandleRecord record, MyConsumeItem datail) {
			System.out.println(datail.toString());
			Result result = new Result();
			result.setRetCode("0");
			result.setRetDesc("Success.");
			return result;
		}
	}
}
