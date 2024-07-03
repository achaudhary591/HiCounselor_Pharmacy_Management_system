package com.MangementApplication.service;

import com.MangementApplication.responses.ProductResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MangementApplication.entity.Batch;
import com.MangementApplication.entity.Product;
import com.MangementApplication.entity.ProductRequestDTO;
import com.MangementApplication.repository.BatchRepository;
import com.MangementApplication.repository.ProductRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;
    
    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return (List<ProductResponse>) products.stream().map((product -> {
            ProductResponse productResponse = new ProductResponse();
            BeanUtils.copyProperties(product, productResponse);
            return productResponse;
        })).collect(Collectors.toList());
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }


    public ProductResponse addProduct(ProductRequestDTO productRequest) {
        if (productRepository.existsByName(productRequest.getName())) {
            throw new IllegalArgumentException("Product with the given name already exists.");
        }

        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        return productResponse;
    }

    @Transactional
    public ProductResponse updateProduct(Long id, String name, double price) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (isValidProduct(name, price, product.getId())) {
                product.builder()
                        .name(name)
                        .price(price)
                        .updatedAt(LocalDate.now())
                        .build();
                ProductResponse updatedProduct = new ProductResponse();
                productRepository.save(product);
                BeanUtils.copyProperties(product, updatedProduct);
                return updatedProduct;
            }
        }
        return null; 
    }

    public Product updateProduct(Product product) {
        product.setUpdatedAt(LocalDate.now());
        return productRepository.save(product);
    }


    private boolean isValidProduct(String name, double price, Long id) {
        if (name == null || name.trim().isEmpty()) {
          return false;
        }
        if (price <= 0) {
            return false;
        }
        Product existingProduct = productRepository.findByNameAndIdNot(name, id);
        return existingProduct == null; 
    }

    @Transactional
    public boolean deleteProduct(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Transactional
    public void updateProductQuantityAndDate(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            int totalQty = product.getBatches().stream().mapToInt(Batch::getQty).sum();
            product.setQty(totalQty);
            product.setUpdatedAt(LocalDate.now());
            productRepository.save(product);
        }
    }
    
}

