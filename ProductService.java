package com.service;

import com.entity.Product;
import com.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> getTop2ProductsInRange(double minPrice, double maxPrice) {
        return productRepo.findTop2ByPriceBetweenOrderByPriceAsc(minPrice, maxPrice);
    }

    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepo.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            return productRepo.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    public String deleteProduct(Long id) {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
            return "Product with ID " + id + " deleted successfully.";
        } else {
            throw new RuntimeException("Product not found with id " + id);
        }
    }
}
