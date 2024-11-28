package com.lab1.lab1.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class ProductsWrapper {
    private List<ProductDTO> products;
}
