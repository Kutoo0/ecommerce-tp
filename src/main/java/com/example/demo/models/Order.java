package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String clientId;
    private List<String> productIds;
    private Double totalAmount;

    public Order() {}

    public Order(String clientId, List<String> productIds, Double totalAmount) {
        this.clientId = clientId;
        this.productIds = productIds;
        this.totalAmount = totalAmount;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public List<String> getProductIds() { return productIds; }
    public void setProductIds(List<String> productIds) { this.productIds = productIds; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}