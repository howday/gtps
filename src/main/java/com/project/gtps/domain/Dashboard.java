package com.project.gtps.domain;

import java.io.Serializable;

/**
 * Created by suresh on 1/7/17.
 */
public class Dashboard implements Serializable {

    private Integer totalGroups;
    private Integer totalTransactions;
    private Integer totalConnections;
    private Integer totalPendingRequest;

    public Integer getTotalGroups() {
        return totalGroups;
    }

    public void setTotalGroups(Integer totalGroups) {
        this.totalGroups = totalGroups;
    }

    public Integer getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Integer getTotalConnections() {
        return totalConnections;
    }

    public void setTotalConnections(Integer totalConnections) {
        this.totalConnections = totalConnections;
    }

    public Integer getTotalPendingRequest() {
        return totalPendingRequest;
    }

    public void setTotalPendingRequest(Integer totalPendingRequest) {
        this.totalPendingRequest = totalPendingRequest;
    }
}
