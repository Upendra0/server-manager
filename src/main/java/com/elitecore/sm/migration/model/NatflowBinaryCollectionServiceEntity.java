//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.22 at 06:41:03 PM IST 
//


package com.elitecore.sm.migration.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dozer.Mapping;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "serviceAddress",
    "startupMode",
    "socketReceiveBufferSize",
    "socketSendBufferSize",
    "queueSize",
    "minimumThread",
    "maximumThread",
    "bulkWriteLimit",
    "maxWritebufferInMb",
    "maxPacketSizeInByte",
    "clients",
    "parallelFileWriteCount"
})
@XmlRootElement(name = "natflow-binary-collection-service")
public class NatflowBinaryCollectionServiceEntity {

    @XmlElement(name = "service-address", required = true)
    //@Mapping("serverIp")
    protected String serviceAddress;
    
    @XmlElement(name = "startup-mode", required = true)
    protected String startupMode;
    
    @XmlElement(name = "socket-receive-buffer-size")
    @Mapping("sktRcvBufferSize")
    protected int socketReceiveBufferSize;
    
    @XmlElement(name = "socket-send-buffer-size")
    @Mapping("sktSendBufferSize")
    protected int socketSendBufferSize;
    
    @XmlElement(name = "queue-size")
    @Mapping("svcExecParams.queueSize")
    protected int queueSize;
    
    @XmlElement(name = "minimum-thread")
    @Mapping("svcExecParams.minThread")
    protected int minimumThread;
    
    @XmlElement(name = "maximum-thread")
    @Mapping("svcExecParams.maxThread")
    protected int maximumThread;
    
    @XmlElement(name = "bulk-write-limit")
    @Mapping("bulkWriteLimit")
    protected int bulkWriteLimit;
    
    @XmlElement(name = "max-writebuffer-in-mb")
    @Mapping("maxWriteBufferSize")
    protected int maxWritebufferInMb;
    
    @XmlElement(name = "max-packet-size-in-byte")
    @Mapping("maxPktSize")
    protected int maxPacketSizeInByte;
    
    @XmlElement(required = true)
    protected NatflowBinaryCollectionServiceEntity.Clients clients;
    
    @XmlElement(name = "parallel-file-write-count")
    @Mapping("parallelFileWriteCount")
    protected int parallelFileWriteCount;

    /**
     * Gets the value of the serviceAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceAddress() {
        return serviceAddress;
    }

    /**
     * Sets the value of the serviceAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceAddress(String value) {
        this.serviceAddress = value;
    }

    /**
     * Gets the value of the startupMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartupMode() {
        return startupMode;
    }

    /**
     * Sets the value of the startupMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartupMode(String value) {
        this.startupMode = value;
    }

    /**
     * Gets the value of the socketReceiveBufferSize property.
     * 
     */
    public int getSocketReceiveBufferSize() {
        return socketReceiveBufferSize;
    }

    /**
     * Sets the value of the socketReceiveBufferSize property.
     * 
     */
    public void setSocketReceiveBufferSize(int value) {
        this.socketReceiveBufferSize = value;
    }

    /**
     * Gets the value of the socketSendBufferSize property.
     * 
     */
    public int getSocketSendBufferSize() {
        return socketSendBufferSize;
    }

    /**
     * Sets the value of the socketSendBufferSize property.
     * 
     */
    public void setSocketSendBufferSize(int value) {
        this.socketSendBufferSize = value;
    }

    /**
     * Gets the value of the queueSize property.
     * 
     */
    public int getQueueSize() {
        return queueSize;
    }

    /**
     * Sets the value of the queueSize property.
     * 
     */
    public void setQueueSize(int value) {
        this.queueSize = value;
    }

    /**
     * Gets the value of the minimumThread property.
     * 
     */
    public int getMinimumThread() {
        return minimumThread;
    }

    /**
     * Sets the value of the minimumThread property.
     * 
     */
    public void setMinimumThread(int value) {
        this.minimumThread = value;
    }

    /**
     * Gets the value of the maximumThread property.
     * 
     */
    public int getMaximumThread() {
        return maximumThread;
    }

    /**
     * Sets the value of the maximumThread property.
     * 
     */
    public void setMaximumThread(int value) {
        this.maximumThread = value;
    }

    /**
     * Gets the value of the bulkWriteLimit property.
     * 
     */
    public int getBulkWriteLimit() {
        return bulkWriteLimit;
    }

    /**
     * Sets the value of the bulkWriteLimit property.
     * 
     */
    public void setBulkWriteLimit(int value) {
        this.bulkWriteLimit = value;
    }

    /**
     * Gets the value of the maxWritebufferInMb property.
     * 
     */
    public int getMaxWritebufferInMb() {
        return maxWritebufferInMb;
    }

    /**
     * Sets the value of the maxWritebufferInMb property.
     * 
     */
    public void setMaxWritebufferInMb(int value) {
        this.maxWritebufferInMb = value;
    }

    /**
     * Gets the value of the maxPacketSizeInByte property.
     * 
     */
    public int getMaxPacketSizeInByte() {
        return maxPacketSizeInByte;
    }

    /**
     * Sets the value of the maxPacketSizeInByte property.
     * 
     */
    public void setMaxPacketSizeInByte(int value) {
        this.maxPacketSizeInByte = value;
    }

    /**
     * Gets the value of the clients property.
     * 
     * @return
     *     possible object is
     *     {@link NatflowBinaryCollectionServiceEntity.Clients }
     *     
     */
    public NatflowBinaryCollectionServiceEntity.Clients getClients() {
        return clients;
    }

    /**
     * Sets the value of the clients property.
     * 
     * @param value
     *     allowed object is
     *     {@link NatflowBinaryCollectionServiceEntity.Clients }
     *     
     */
    public void setClients(NatflowBinaryCollectionServiceEntity.Clients value) {
        this.clients = value;
    }

    /**
     * Gets the value of the parallelFileWriteCount property.
     * 
     */
    public int getParallelFileWriteCount() {
        return parallelFileWriteCount;
    }

    /**
     * Sets the value of the parallelFileWriteCount property.
     * 
     */
    public void setParallelFileWriteCount(int value) {
        this.parallelFileWriteCount = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="client">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="client-ip" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="client-port" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="file-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="file-sequence" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="sequence-range" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="file-location" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="rolling-time-unit" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="rolling-volume-unit" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="file-compression" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="is-client-enable" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="abnormal-time-alert">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="time-interval" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                           &lt;/sequence>
     *                           &lt;attribute name="enable" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "client"
    })
    public static class Clients {

        @XmlElement(required = true)
        @Mapping("netFLowClientList")
        protected List<NatflowBinaryCollectionServiceEntity.Clients.Client> client;

        /**
         * Gets the value of the client property.
         * 
         * @return
         *     possible object is
         *     {@link NatflowBinaryCollectionServiceEntity.Clients.Client }
         *     
         */
        public List<NatflowBinaryCollectionServiceEntity.Clients.Client> getClient() {
            return client;
        }

        /**
         * Sets the value of the client property.
         * 
         * @param value
         *     allowed object is
         *     {@link NatflowBinaryCollectionServiceEntity.Clients.Client }
         *     
         */
        public void setClient(List<NatflowBinaryCollectionServiceEntity.Clients.Client> value) {
            this.client = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="client-ip" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="client-port" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="file-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="file-sequence" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="sequence-range" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="file-location" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="rolling-time-unit" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="rolling-volume-unit" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="file-compression" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="is-client-enable" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="abnormal-time-alert">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="time-interval" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *                 &lt;/sequence>
         *                 &lt;attribute name="enable" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "clientIp","clientPort","fileName","fileSequence","sequenceRange","fileLocation","rollingTimeUnit","rollingVolumeUnit",
            "fileCompression","isClientEnable","abnormalTimeAlert"
        })
        public static class Client {

            @XmlElement(name = "client-ip", required = true)
            @Mapping("clientIpAddress")
            protected String clientIp;
            
            @XmlElement(name = "client-port")
            @Mapping("clientPort")
            protected int clientPort;
            
            @XmlElement(name = "file-name", required = true)
            @Mapping("fileNameFormat")
            protected String fileName;
            
            @XmlElement(name = "file-sequence", required = true)
            @Mapping("appendFileSequenceInFileName")
            protected String fileSequence;
            
            @XmlElement(name = "sequence-range", required = true)
            protected String sequenceRange;
            
            @XmlElement(name = "file-location", required = true)
            @Mapping("outFileLocation")
            protected String fileLocation;
            
            @XmlElement(name = "rolling-time-unit")
            @Mapping("timeLogRollingUnit")
            protected int rollingTimeUnit;
            
            @XmlElement(name = "rolling-volume-unit")
            @Mapping("volLogRollingUnit")
            protected int rollingVolumeUnit;
            
            @XmlElement(name = "file-compression", required = true)
            @Mapping("inputCompressed")
            protected String fileCompression;
            
            @XmlElement(name = "is-client-enable", required = true)
            @Mapping("this")
            protected String isClientEnable;
            
            @XmlElement(name = "abnormal-time-alert", required = true)
            @Mapping("this")
            protected NatflowBinaryCollectionServiceEntity.Clients.Client.AbnormalTimeAlert abnormalTimeAlert;

            /**
             * Gets the value of the clientIp property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getClientIp() {
                return clientIp;
            }

            /**
             * Sets the value of the clientIp property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setClientIp(String value) {
                this.clientIp = value;
            }

            /**
             * Gets the value of the clientPort property.
             * 
             */
            public int getClientPort() {
                return clientPort;
            }

            /**
             * Sets the value of the clientPort property.
             * 
             */
            public void setClientPort(int value) {
                this.clientPort = value;
            }

            /**
             * Gets the value of the fileName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFileName() {
                return fileName;
            }

            /**
             * Sets the value of the fileName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFileName(String value) {
                this.fileName = value;
            }

            /**
             * Gets the value of the fileSequence property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFileSequence() {
                return fileSequence;
            }

            /**
             * Sets the value of the fileSequence property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFileSequence(String value) {
                this.fileSequence = value;
            }

            /**
             * Gets the value of the sequenceRange property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSequenceRange() {
                return sequenceRange;
            }

            /**
             * Sets the value of the sequenceRange property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSequenceRange(String value) {
                this.sequenceRange = value;
            }

            /**
             * Gets the value of the fileLocation property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFileLocation() {
                return fileLocation;
            }

            /**
             * Sets the value of the fileLocation property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFileLocation(String value) {
                this.fileLocation = value;
            }

            /**
             * Gets the value of the rollingTimeUnit property.
             * 
             */
            public int getRollingTimeUnit() {
                return rollingTimeUnit;
            }

            /**
             * Sets the value of the rollingTimeUnit property.
             * 
             */
            public void setRollingTimeUnit(int value) {
                this.rollingTimeUnit = value;
            }

            /**
             * Gets the value of the rollingVolumeUnit property.
             * 
             */
            public int getRollingVolumeUnit() {
                return rollingVolumeUnit;
            }

            /**
             * Sets the value of the rollingVolumeUnit property.
             * 
             */
            public void setRollingVolumeUnit(int value) {
                this.rollingVolumeUnit = value;
            }

            /**
             * Gets the value of the fileCompression property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFileCompression() {
                return fileCompression;
            }

            /**
             * Sets the value of the fileCompression property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFileCompression(String value) {
                this.fileCompression = value;
            }

            /**
             * Gets the value of the isClientEnable property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getIsClientEnable() {
                return isClientEnable;
            }

            /**
             * Sets the value of the isClientEnable property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setIsClientEnable(String value) {
                this.isClientEnable = value;
            }

            /**
             * Gets the value of the abnormalTimeAlert property.
             * 
             * @return
             *     possible object is
             *     {@link NatflowBinaryCollectionServiceEntity.Clients.Client.AbnormalTimeAlert }
             *     
             */
            public NatflowBinaryCollectionServiceEntity.Clients.Client.AbnormalTimeAlert getAbnormalTimeAlert() {
                return abnormalTimeAlert;
            }

            /**
             * Sets the value of the abnormalTimeAlert property.
             * 
             * @param value
             *     allowed object is
             *     {@link NatflowBinaryCollectionServiceEntity.Clients.Client.AbnormalTimeAlert }
             *     
             */
            public void setAbnormalTimeAlert(NatflowBinaryCollectionServiceEntity.Clients.Client.AbnormalTimeAlert value) {
                this.abnormalTimeAlert = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="time-interval" type="{http://www.w3.org/2001/XMLSchema}int"/>
             *       &lt;/sequence>
             *       &lt;attribute name="enable" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "timeInterval"
            })
            public static class AbnormalTimeAlert {

                @XmlElement(name = "time-interval")
                @Mapping("alertInterval")
                protected int timeInterval;
                
                @XmlAttribute(name = "enable")
                @Mapping("snmpAlertEnable")
                protected String enable;

                /**
                 * Gets the value of the timeInterval property.
                 * 
                 */
                public int getTimeInterval() {
                    return timeInterval;
                }

                /**
                 * Sets the value of the timeInterval property.
                 * 
                 */
                public void setTimeInterval(int value) {
                    this.timeInterval = value;
                }

                /**
                 * Gets the value of the enable property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getEnable() {
                    return enable;
                }

                /**
                 * Sets the value of the enable property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setEnable(String value) {
                    this.enable = value;
                }

            }

        }

    }

}
