package demo;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="vms")
public class VM {

	@Id
	private long id;
	private String vmName;
	private String vmOS;
	private String vmCPU;
	private String vmRAM;
	private String vmDisk;
	private String template;
	private long userId;
	private String vmStatus;
	public long vmIdCounter;
	
	public long getVmIdCounter() {
		return vmIdCounter;
	}
	public void setVmIdCounter(long vmIdCounter) {
		this.vmIdCounter = vmIdCounter;
	}
	public String getVmStatus() {
		return vmStatus;
	}
	public void setVmStatus(String vmStatus) {
		this.vmStatus = vmStatus;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getVmName() {
		return vmName;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public String getVmOS() {
		return vmOS;
	}
	public void setVmOS(String vmOS) {
		this.vmOS = vmOS;
	}
	public String getVmCPU() {
		return vmCPU;
	}
	public void setVmCPU(String vmCPU) {
		this.vmCPU = vmCPU;
	}
	public String getVmRAM() {
		return vmRAM;
	}
	public void setVmRAM(String vmRAM) {
		this.vmRAM = vmRAM;
	}
	public String getVmDisk() {
		return vmDisk;
	}
	public void setVmDisk(String vmDisk) {
		this.vmDisk = vmDisk;
	}
	public String getVmIP() {
		return vmIP;
	}
	public void setVmIP(String vmIP) {
		this.vmIP = vmIP;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	private String vmIP;
	private Date createDate;
}
