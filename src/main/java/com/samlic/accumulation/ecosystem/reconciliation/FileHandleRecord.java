package com.samlic.accumulation.ecosystem.reconciliation;

/**
 * 文件处理记录
 * @author yuanpeng
 *
 */
public class FileHandleRecord {
	//唯一标识
	private Long id;
	//对账角色
	private AuditRole role;
	//对账时间
	private String auditTime;
	//文件名称模式
	private String pattern;
    //文件路径
	private String filePath;
    //当前周期的生产记录状态
	private int status;
	//总行数
	private int totalLines;	
	//文件创建时间
	private String fileCreateTime;

	public String getFileCreateTime() {
		return fileCreateTime;
	}

	public void setFileCreateTime(String fileCreateTime) {
		this.fileCreateTime = fileCreateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public int getTotalLines() {
		return totalLines;
	}

	public void setTotalLines(int totalLines) {
		this.totalLines = totalLines;
	}

	public AuditRole getRole() {
		return role;
	}

	public void setRole(AuditRole role) {
		this.role = role;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public static enum AuditRole {
		Producer, Consumer
	}
}
