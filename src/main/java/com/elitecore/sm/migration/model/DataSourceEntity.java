package com.elitecore.sm.migration.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dozer.Mapping;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "datasource"
})
@XmlRootElement(name = "datasources")
public class DataSourceEntity {

    @XmlElement(required = true)
    @Mapping("this")
    protected DataSourceEntity.Datasource datasource;

    public DataSourceEntity.Datasource getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSourceEntity.Datasource value) {
        this.datasource = value;
    }


    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "datasourceName",
        "datasourceType",
        "connectionUrl",
        "userName",
        "password",
        "minimumPoolSize",
        "maximumPoolSize"
    })
    public static class Datasource {

        @XmlElement(name = "datasource-name", required = true)
        @Mapping("name")
        protected String datasourceName;
        
        @XmlElement(name = "datasource-type")
        @Mapping("type")
        protected byte datasourceType;
        
        @XmlElement(name = "connection-url", required = true)
        @Mapping("connURL")
        protected String connectionUrl;
        
        @XmlElement(name = "user-name", required = true)
        @Mapping("username")
        protected String userName;
        
        @XmlElement(required = true)
        @Mapping("password")
        protected String password;
        
        @XmlElement(name = "minimum-pool-size")
        @Mapping("maxPoolsize")
        protected byte minimumPoolSize;
        
        @XmlElement(name = "maximum-pool-size")
        @Mapping("maxPoolsize")
        protected byte maximumPoolSize;

        
        public String getDatasourceName() {
            return datasourceName;
        }

        
        public void setDatasourceName(String value) {
            this.datasourceName = value;
        }

        
        public byte getDatasourceType() {
            return datasourceType;
        }

        
        public void setDatasourceType(byte value) {
            this.datasourceType = value;
        }

        
        public String getConnectionUrl() {
            return connectionUrl;
        }

        
        public void setConnectionUrl(String value) {
            this.connectionUrl = value;
        }

        
        public String getUserName() {
            return userName;
        }

        
        public void setUserName(String value) {
            this.userName = value;
        }

        
        public String getPassword() {
            return password;
        }

        
        public void setPassword(String value) {
            this.password = value;
        }

        
        public byte getMinimumPoolSize() {
            return minimumPoolSize;
        }

        
        public void setMinimumPoolSize(byte value) {
            this.minimumPoolSize = value;
        }

        
        public byte getMaximumPoolSize() {
            return maximumPoolSize;
        }

        
        public void setMaximumPoolSize(byte value) {
            this.maximumPoolSize = value;
        }
    }

}
