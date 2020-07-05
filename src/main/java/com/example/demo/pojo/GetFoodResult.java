package com.example.demo.pojo;

import lombok.Data;

import java.util.List;

@Data
public class GetFoodResult {

    Integer count;
    String next;
    String previous;
    List<Object> result;

}
