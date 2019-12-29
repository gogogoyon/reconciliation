package com.samlic.accumulation.ecosystem.reconciliation;

public class AuditFileNameConstructor implements FileNameConstructor {
	
	private String fileNamePattern;
	private String fileNameSeparator;
	private String auditTimeStr;
	
	public AuditFileNameConstructor(String fileNamePattern, String fileNameSeparator, String auditTimeStr) {
		this.fileNamePattern = fileNamePattern;
		this.fileNameSeparator = fileNameSeparator;
		this.auditTimeStr = auditTimeStr;
	}

	@Override
	public String getFileName() {
		String fileName = this.fileNamePattern;		
		int index = this.fileNamePattern.lastIndexOf(".");
		if(index > -1) {
			String pattern = fileName.substring(0, index);	
			String suffix = fileName.substring(index, fileName.length());
			fileName = pattern + fileNameSeparator + auditTimeStr + suffix;
		} else {
			fileName += fileNameSeparator + auditTimeStr;
		}
		return fileName;
	}
}
