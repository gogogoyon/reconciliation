package com.samlic.accumulation.ecosystem.reconciliation;

public abstract class BaseConsumeItem {
	//对账主记录标识
	private Long auditId;
	//行号
	private int lineNumber;
	//对账状态
	private String auditStatus;
	//对账描述
	private String auditDesc;
	
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public Long getAuditId() {
		return auditId;
	}
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditDesc() {
		return auditDesc;
	}
	public void setAuditDesc(String auditDesc) {
		this.auditDesc = auditDesc;
	}
	@Override
	public String toString() {
		return "BaseConsumeItem [auditId=" + auditId + ", lineNumber=" + lineNumber + ", auditStatus=" + auditStatus
				+ ", auditDesc=" + auditDesc + "]";
	}
}
