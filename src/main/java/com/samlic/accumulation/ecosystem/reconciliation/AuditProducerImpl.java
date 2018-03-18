package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.samlic.accumulation.ecosystem.reconciliation.FileHandleRecord.AuditRole;
import static com.samlic.accumulation.ecosystem.reconciliation.AuditConstants.*;

/**
 * 对账生产者业务逻辑定义
 * @author yuanpeng
 *
 */
class AuditProducerImpl implements AuditProducer {	
	
	private static final Logger logger = LoggerFactory.getLogger(AuditProducerImpl.class);
	
	private AuditFileBuilder auditFileBuilder;
	
	private FileHandleRecorder recorder;
	
	public AuditProducerImpl(AuditFileBuilder auditFileBuilder, FileHandleRecorder recorder) {
		this.auditFileBuilder = auditFileBuilder;		
		this.recorder = recorder;
	}
	
	@Override
	public void produce() {
		String auditTime = auditFileBuilder.getPeriod().getAuditTimeStr();
		produce(auditTime);
	}

	@Override
	public void produce(String auditTime) {
		auditFileBuilder.getPeriod().checkAuditTime(auditTime);
		FileHandleRecord record = recorder.getRecord(auditFileBuilder.getFileNamePattern(), auditTime);
		if(record != null && STATUS_SUCCESS.equals(record.getStatus())) {
			return;
		}
		
		if(record == null || STATUS_MAKE_FILE_FAILED.equals(record.getStatus())) {
			if(record == null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				record = new FileHandleRecord();		
				record.setRole(AuditRole.Producer);
				record.setAuditTime(auditTime);
				record.setPattern(auditFileBuilder.getFileNamePattern());
				record.setStatus(STATUS_MAKE_FILE_FAILED);
				record.setTotalLines(auditFileBuilder.getTotalSize());
				record.setCreateTime(format.format(new Date()));
				recorder.save(record);
			} 

			auditFileBuilder.build();
			
			record.setFilePath(auditFileBuilder.getResult().getAbsolutePath());
			record.setStatus(STATUS_UPLOAD_FILE_FAILED);
			recorder.update(record);		
			
			auditFileBuilder.upload();
			record.setStatus(STATUS_SUCCESS);
			recorder.update(record);			
		} else if(STATUS_UPLOAD_FILE_FAILED.equals(record.getStatus())) {
			try {
				auditFileBuilder.getUploader().upload(new File(record.getFilePath()));
				record.setStatus(STATUS_SUCCESS);
				recorder.update(record);
			} catch (IOException e) {
				logger.error("Failed to upload file.", e);
			}
		}		
	}
}
