package com.project.gtps.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by suresh on 1/5/17.
 */
@Entity
@Table(name = "transaction")
@XmlRootElement
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tid")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "requestTo")
    private String requestToId;

    @Column(name = "requestFrom")
    private String requestFromId;

    @Column(name = "tx_date")
    private Date date;

    public String getRequestFromId() {
        return requestFromId;
    }

    public void setRequestFromId(String requestFromId) {
        this.requestFromId = requestFromId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestToId() {
        return requestToId;
    }

    public void setRequestToId(String requestToId) {
        this.requestToId = requestToId;
    }
}
