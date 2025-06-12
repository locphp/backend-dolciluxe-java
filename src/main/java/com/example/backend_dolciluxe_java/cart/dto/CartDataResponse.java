package com.example.backend_dolciluxe_java.cart.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CartDataResponse {
    private String _id;
    private String user;
    private List<CartItemResponse> items;
    private Date createdAt;
    private Date updatedAt;

    public CartDataResponse(String _id, String user, List<CartItemResponse> items,
            Date createdAt, Date updatedAt) {
        this._id = _id;
        this.user = user;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return _id;
    }

    public String getUserId() {
        return user;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

}