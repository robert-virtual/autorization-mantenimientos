package com.example.autorizationmantenimientos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicResponse<T> {
    private T data;
    private String error;
    private int data_count;
    private String data_type;
}
