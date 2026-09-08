package com.monikamart.service;

import com.monikamart.dao.ProductDAO;
import com.monikamart.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product saveProduct(Product product) {
        return productDAO.save(product);
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public Product getProductById(Long id) {
        return productDAO.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productDAO.deleteById(id);
    }
}

