# reconciliation
此项目提供一个对账逻辑组件，抽象对账逻辑，减少重复开发工作；主要逻辑有：<br>
AuditFactory: 对账工厂类，提供创建对账文件和解析对账文件的方法，提供创建对账生产者和消费者服务实例的方法<br>
AuditFileBuilder: 生成对账文件和上传<br>
AuditFileResolver：解析对账文件<br>
AuditProducer: 对账逻辑生产者<br>
AuditConsumer: 对账逻辑消费者<br>
FileHandleRecorder: 文件处理记录器，用于对账文件生成和消费记录<br>
ConsumeRecorder: 消费记录器，记录消费文件记录，已经对账明细<br>
DetailHandler: 明细处理接口，用于代理消费对账文件明细数据的逻辑行为<br>
Iteratorable: 定义生产对账文件数据的统一视图<br>
DataFetcher: 数据获取接口，支持分批获取数据
<br>
常见用例详见测试类：<br>
com.samlic.accumulation.ecosystem.reconciliation.AuditFileTest<br>