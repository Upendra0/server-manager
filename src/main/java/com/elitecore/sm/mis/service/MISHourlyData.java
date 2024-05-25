package com.elitecore.sm.mis.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class MISHourlyData implements Comparable<MISHourlyData>
{
	private String serviceType;
	private Integer serviceId;
	private String serviceName;
	private Date startDate;
	private Date endDate;
	private String lastHourRecord;
	private String data;
	
	public MISHourlyData(String serviceType, int serviceId, String serviceName) {
		this.serviceType = serviceType;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
	}
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLastHourRecord() {
		return lastHourRecord;
	}

	public void setLastHourRecord(String lastHourRecord) {
		this.lastHourRecord = lastHourRecord;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public int compareTo(MISHourlyData o) { //NOSONAR
		
		if(this == o) {
			return 0;
		}
		
		int i = this.serviceType.compareTo(o.serviceType);
		if(i != 0) {
			return i;
		}
		
		i = this.serviceId.compareTo(o.serviceId);
		if(i != 0) {
			return i;
		}
		
		i = this.startDate.compareTo(o.startDate);
		if(i != 0) {
			return i;
		}
		
		i = this.endDate.compareTo(o.endDate);
		return i;
	}

	@Override                                                        
    public String toString() {                                        
        
		StringWriter out = new StringWriter();                        
        PrintWriter writer = new PrintWriter(out);
        writer.println();                    
        writer.println("------------MISHourlyData-----------------");
        writer.println("serviceType=" +serviceType);                                     
        writer.println("serviceId=" +serviceId);                                     
        writer.println("serviceName=" +serviceName);                                     
        writer.println("startDate=" +startDate);                                     
        writer.println("endDate=" +endDate);                                     
        writer.println("lastHourRecord=" +lastHourRecord);
        writer.println("data=" +data);                                     
        writer.println("-------------------------------------------");
        writer.close();                                               
        return out.toString();
    }
	
	
	
}

