package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.File;

import com.samlic.accumulation.ecosystem.reconciliation.DetailHandler.Result;
import com.samlic.accumulation.ecosystem.reconciliation.FileHandleRecord.AuditRole;
import static com.samlic.accumulation.ecosystem.reconciliation.AuditConstants.*;

/**
 * 对账消费者业务逻辑定义
 * @author yuanpeng
 *
 */
class AuditConsumerImpl<T extends BaseConsumeItem> implements AuditConsumer {
	
	private AuditFileResolver resolver;
	
	private ConsumeRecorder<T> recorder;
	
	private DetailHandler<T> detailHandler;
	
	public AuditConsumerImpl(AuditFileResolver resolver, ConsumeRecorder<T> recorder, DetailHandler<T> detailHandler) {
		this.resolver = resolver;
		this.recorder = recorder;
		this.detailHandler = detailHandler;
	}

	@Override
	public void consume(final File file) {
		FileDataHandler dataHandler = new FileDataHandler() {

			private int currentLine = 0;
			private int i = 0;
			private FileHandleRecord record;

			@Override
			public void handleFirstLine(String[] lineItems) {
				String pattern = resolver.getFileNamePattern();
				String auditTime = lineItems[0];
				
				record = recorder.getRecord(pattern, auditTime);
				if(record == null) {
					record = new FileHandleRecord();	
					record.setAuditTime(auditTime);
					record.setFilePath(file.getAbsolutePath());
					record.setPattern(pattern);
					record.setRole(AuditRole.Consumer);
					record.setStatus(STATUS_RESOLVE_FILE_FAILED);
					record.setTotalLines(Integer.parseInt(lineItems[1]));
					record.setCreateTime(lineItems[2]);
					
					Long id = recorder.save(record);
					record.setId(id);
				} else {
					T latestItem = recorder.getLatestItem(record.getId());
					if(latestItem != null) {
						currentLine =  latestItem.getLineNumber();
					}
				}
			}

			@Override
			public void handleContent(String[] lineItems) {
				i++;
				
				if(i > currentLine) {
					T detail = detailHandler.parse(lineItems);
					detail.setAuditId(record.getId());					
					detail.setLineNumber(i);	
					
					Result result = detailHandler.handle(record, detail);
					detail.setAuditStatus(result.getRetCode());
					detail.setAuditDesc(result.getRetDesc());
					recorder.saveItem(detail);
					
					currentLine = i;					
				}
			}

			@Override
			public void handleEnd() {
				if(i == currentLine) {
					record.setStatus(STATUS_SUCCESS);
					recorder.update(record);
				}				
			}
			
		};
		
		resolver.file(file).dataHandler(dataHandler).resolver();
	}

}
