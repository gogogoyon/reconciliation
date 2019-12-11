package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.samlic.accumulation.ecosystem.reconciliation.FileHandleRecord.AuditRole;

/**
 * 对账工厂类，提供创建对账文件和解析对账文件的方法，提供创建对账生产者和消费者服务实例的方法
 * @author yuanpeng
 *
 */
public class AuditFactory {
	private AuditFactory() {
	}
	
	/**
	 * 创建对账文件生成器实例
	 * @param fileNamePattern   文件名称模式
	 * @param dataList          对账数据列表
	 * @return
	 */
	public static AuditFileBuilder createFileBuilder(String fileNamePattern, List<Iteratorable> dataList) {
		return new AuditFileBuilder().fileNamePattern(fileNamePattern).uploader(NothingUploader.INSTANCE)
				.content(AuditDataAdapter.adapt(dataList), dataList.size());
	}
	
	/**
	 * 创建对账文件生成器实例
	 * @param fileNamePattern   文件名称模式
	 * @param dataList          对账数据列表
	 * @param formatMap       字符串格式化实例映射
	 * @return
	 */
	public static AuditFileBuilder createFileBuilder(String fileNamePattern, List<Iteratorable> dataList, 
			Map<Class<?>, StringFormater> formatMap) {
		return new AuditFileBuilder().fileNamePattern(fileNamePattern).uploader(NothingUploader.INSTANCE)
				.content(AuditDataAdapter.adapt(dataList, formatMap), dataList.size());
	}

	/**
	 * 创建对账文件生成器实例
	 * @param fileNamePattern   文件名称模式
	 * @param dataList          对账数据列表
	 * @param keyList           获取对账数据项的关键字列表，用于组成数据行，列表中顺序决定了数据项的顺序
	 * @return
	 */
	public static AuditFileBuilder createFileBuilder(String fileNamePattern, List<Map<String, Object>> dataList,
			List<String> keyList) {
		return new AuditFileBuilder().fileNamePattern(fileNamePattern).uploader(NothingUploader.INSTANCE)
				.content(AuditDataAdapter.adapt(dataList, keyList), dataList.size());
	}

	/**
	 * 创建对账文件生成器实例
	 * @param fileNamePattern  文件名称模式
	 * @param dataFetcher      数据获取器
	 * @param keyList          获取对账数据项的关键字列表，用于组成数据行，列表中顺序决定了数据项的顺序
	 * @return
	 */
	public static AuditFileBuilder createFileBuilder(String fileNamePattern, DataFetcher dataFetcher, 
			List<String> keyList) {
		return new AuditFileBuilder().fileNamePattern(fileNamePattern).uploader(NothingUploader.INSTANCE)
				.content(AuditDataAdapter.adapt(dataFetcher, keyList), dataFetcher.getTotalCount());
	}

	
	/**
	 * 创建对账文件生成器实例
	 * @param fileNamePattern 文件名称模式
	 * @param dataList        对账数据列表
	 * @param keyList         获取对账数据项的关键字列表，用于组成数据行，列表中顺序决定了数据项的顺序
	 * @param formatMap       字符串格式化实例映射
	 * @return
	 */
	public static AuditFileBuilder createFileBuilder(String fileNamePattern, List<Map<String, Object>> dataList,
			List<String> keyList, Map<Class<?>, StringFormater> formatMap) {
		return new AuditFileBuilder().fileNamePattern(fileNamePattern).uploader(NothingUploader.INSTANCE)
				.content(AuditDataAdapter.adapt(dataList, keyList, formatMap), dataList.size());
	}

	/**
	 * 创建对账文件生成器实例
	 * @param fileNamePattern  文件名称模式
	 * @param dataFetcher      数据获取器
	 * @param keyList          获取对账数据项的关键字列表，用于组成数据行，列表中顺序决定了数据项的顺序
	 * @param formatMap        字符串格式化实例映射
	 * @return
	 */
	public static AuditFileBuilder createFileBuilder(String fileNamePattern, DataFetcher dataFetcher, 
			List<String> keyList, Map<Class<?>, StringFormater> formatMap) {
		return new AuditFileBuilder().fileNamePattern(fileNamePattern).uploader(NothingUploader.INSTANCE)
				.content(AuditDataAdapter.adapt(dataFetcher, keyList, formatMap), dataFetcher.getTotalCount());
	}

	/**
	 * 创建对账生成者实例
	 * @param auditFileBuilder   对账文件生成器
	 * @param recorder           对账文件记录器
	 * @return
	 */
	public static AuditProducer createProducer(AuditFileBuilder auditFileBuilder, FileHandleRecorder recorder) {
		return new AuditProducerImpl(auditFileBuilder, recorder);
	}

	/**
	 * 创建对账生成者实例， 此实例使用空的对账文件记录器
	 * @param auditFileBuilder  对账文件生成器
	 * @return
	 */
	public static AuditProducer createProducer(AuditFileBuilder auditFileBuilder) {
		return new AuditProducerImpl(auditFileBuilder, NothingFileHandleRecorder.INSTANCE);
	}
	
	/**
	 * 创建对账文件解析器
	 * @param file  对账文件
	 * @return
	 */
	public static AuditFileResolver createFileResolver(File file) {
		return new AuditFileResolver().file(file);				
	}
	
	/**
	 * 创建对账文件解析器
	 * @param downloader  对账文件下载器
	 * @param suffix  对账文件后缀
	 * @return
	 */
	public static AuditFileResolver createFileResolver(Downloader downloader, String suffix) {
		return new AuditFileResolver().addDownloader(downloader).suffix(suffix);			
	}
	
	/**
	 * 创建对账文件消费者实例
	 * @param auditFileResolver  对账文件解析器
	 * @param recorder           消费记录器
	 * @param detailHandler      对账明细处理实例
	 * @return
	 */
	public static <T extends BaseConsumeItem> AuditConsumer createConsumer(AuditFileResolver auditFileResolver, ConsumeRecorder<T> recorder, DetailHandler<T> detailHandler) {
		return new AuditConsumerImpl<T>(auditFileResolver, recorder, detailHandler);
	}
	
	/**
	 * 创建对账文件消费者实例, 使用空的消费记录器
	 * @param auditFileResolver  对账文件解析器
	 * @param detailHandler      对账明细处理实例
	 * @return
	 */
	public static <T extends BaseConsumeItem> AuditConsumer createConsumer(AuditFileResolver auditFileResolver, DetailHandler<T> detailHandler) {
		return new AuditConsumerImpl<T>(auditFileResolver, new NothingConsumeRecorder<T>(), detailHandler);
	}

	/**
	 * 空上传实现类
	 * 
	 * @author yuanpeng
	 *
	 */
	static class NothingUploader implements Uploader {
		private NothingUploader() {
		}

		static final NothingUploader INSTANCE = new NothingUploader();

		@Override
		public void upload(File file) throws IOException {
			// Nothing to do.
		}

	}
	
	/**
	 * 空的文件处理记录器
	 * @author yuanpeng
	 *
	 */
	static class NothingFileHandleRecorder implements FileHandleRecorder {
		private NothingFileHandleRecorder() {
		}
		
		static final NothingFileHandleRecorder INSTANCE = new NothingFileHandleRecorder();
		
		@Override
		public FileHandleRecord getRecord(AuditRole role, String pattern, String auditTime) {
			return null;
		}

		@Override
		public Long save(FileHandleRecord record) {
			return 0L;
		}

		@Override
		public void update(FileHandleRecord record) {
			
		}

	}
	
	/**
	 * 空的消费记录器
	 * @author yuanpeng
	 *
	 * @param <T>
	 */
	static class NothingConsumeRecorder<T extends BaseConsumeItem> extends NothingFileHandleRecorder implements ConsumeRecorder<T> {
		
		@Override
		public T getLatestItem(Long auditId) {			
			return null;
		}

		@Override
		public void saveItem(T record) {
		
		}
		
	}
}
