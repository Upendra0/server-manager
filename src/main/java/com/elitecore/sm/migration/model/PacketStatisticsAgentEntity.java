package com.elitecore.sm.migration.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dozer.Mapping;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "packet-statistics-agent")
public class PacketStatisticsAgentEntity {

    @XmlElement(name = "storage-location", required = true)
    @Mapping("storageLocation")
    protected String storageLocation;
    
    @XmlElement(name = "execution-interval")
    @Mapping("executionInterval")
    protected int executionInterval;
    
    @XmlElement(name = "service-list", required = true)
    @Mapping("this")
    protected PacketStatisticsAgentEntity.ServiceList serviceList;

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String value) {
        this.storageLocation = value;
    }

    public int getExecutionInterval() {
        return executionInterval;
    }

    public void setExecutionInterval(int value) {
        this.executionInterval = value;
    }
    public PacketStatisticsAgentEntity.ServiceList getServiceList() {
        return serviceList;
    }

    public void setServiceList(PacketStatisticsAgentEntity.ServiceList value) {
        this.serviceList = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "service"
    })
    public static class ServiceList {

    	@Mapping("serviceList")
        protected List<PacketStatisticsAgentEntity.ServiceList.Service> service;

        public List<PacketStatisticsAgentEntity.ServiceList.Service> getService() {
            if (service == null) {
                service = new ArrayList<>();
            }
            return this.service;
        }


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {

        })
        public static class Service {

            @XmlElement(required = true)
            @Mapping("service.svctype.alias")
            protected String name;
            
            @XmlElement(required = true)
            @Mapping("enable")
            protected String enable;

            public String getName() {
                return name;
            }

            public void setName(String value) {
                this.name = value;
            }

            public String getEnable() {
                return enable;
            }
            public void setEnable(String value) {
                this.enable = value;
            }

        }

    }

}
