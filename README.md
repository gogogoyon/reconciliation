# reconciliation
此项目提供一个对账逻辑组件，抽象对账逻辑，减少重复开发工作；主要逻辑有：
AuditFactory: 对账工厂类，提供创建对账文件和解析对账文件的方法，提供创建对账生产者和消费者服务实例的方法
AuditFileBuilder: 生成对账文件和上传
AuditFileResolver：解析对账文件
AuditProducer: 对账逻辑生产者
AuditConsumer: 对账逻辑消费者
FileHandleRecorder: 文件处理记录器，用于对账文件生成和消费记录
ConsumeRecorder: 消费记录器，记录消费文件记录，已经对账明细
DetailHandler: 明细处理接口，用于代理消费对账文件明细数据的逻辑行为
Iteratorable: 定义生产对账文件数据的统一视图

常见用例详见测试类：
com.samlic.accumulation.ecosystem.reconciliation.AuditFileTest