# reconciliation
此项目提供一个对账逻辑组件，抽象对账逻辑，减少重复开发工作；主要逻辑类有：<br>
- AuditFactory: 对账工厂类，提供创建对账文件和解析对账文件的方法，提供创建对账生产者和消费者服务实例的方法<br>
- AuditFileBuilder: 生成对账文件和上传<br>
- AuditFileResolver：解析对账文件<br>
- AuditProducer: 对账逻辑生产者<br>
- AuditConsumer: 对账逻辑消费者<br>

## 添加Maven依赖

```xml
    <dependency>
	  <groupId>com.samlic</groupId>
	  <artifactId>reconciliation</artifactId>
	  <version>1.0.0</version>
	</dependency>
```

## 代码示例
### 以Map结构数据为数据源的示例
```java
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
	
    //根据数据生成对账文件	
    AuditFileBuilder auditFileBuilder = AuditFactory.createFileBuilder(fileNamePattern, dataList, keyList);
    AuditFactory.createProducer(auditFileBuilder.period(AuditPeriod.Day)).produce();
    File file = auditFileBuilder.getResult();
    
	//解析处理对账文件
    AuditFileResolver auditFileResolver = AuditFactory.createFileResolver(file);
    AuditFactory.createConsumer(auditFileResolver, new MyDetailHandler()).consume();
```
### 以Iteratorable类数据为数据源的示例
```java
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
	
	//根据数据生成对账文件
    AuditFileBuilder auditFileBuilder = AuditFactory.createFileBuilder(fileNamePattern, dataList, formatMap);
    AuditFactory.createProducer(auditFileBuilder.period(AuditPeriod.Day)).produce();
    File file = auditFileBuilder.getResult();
    
	//解析处理对账文件
    AuditFileResolver auditFileResolver = AuditFactory.createFileResolver(file);
    AuditFactory.createConsumer(auditFileResolver, new MyDetailHandler()).consume();
```
## 详细用例代码查看测试类
    com.samlic.accumulation.ecosystem.reconciliation.AuditFileTest