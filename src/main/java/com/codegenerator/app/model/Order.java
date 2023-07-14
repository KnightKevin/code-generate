package com.codegenerator.app.model;

import lombok.Data;

@Data
public class Order {
    private String orderId;
    private String name;
    private Place place;
}
