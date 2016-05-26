package com.lsntsolutions.gtmApp.dto;

/**
 * Created by Sebastian on 10/03/2016.
 */
public class SearchProductDTO {
    private Integer id;
    private String role;
    private Integer operationId;
    private String date;
    private String username;
    private Boolean cancelled;
    private String operationDate;

    public SearchProductDTO() {}

    public SearchProductDTO(Integer id, String role, Integer operationId, String date, String username, Boolean cancelled, String operationDate) {
        super();
        this.id = id;
        this.role = role;
        this.operationId = operationId;
        this.date = date;
        this.username = username;
        this.cancelled = cancelled;
        this.operationDate = operationDate;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getOperationId() {
        return this.operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }
}