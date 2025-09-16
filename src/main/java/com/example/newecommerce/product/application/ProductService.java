package com.example.newecommerce.product.application;


import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.product.dto.ProductResponse;

import java.util.List;
import java.util.Map;

public interface ProductService {


   public List<ProductResponse> inquiryProduct(List<Long> productIdList);

   public List<ProductResponse> inquiryBestProduct();



}
