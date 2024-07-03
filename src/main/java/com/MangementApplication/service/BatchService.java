package com.MangementApplication.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.MangementApplication.responses.BatchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MangementApplication.entity.Batch;
import com.MangementApplication.entity.BatchRequestDTO;
import com.MangementApplication.entity.Product;
import com.MangementApplication.repository.BatchRepository;
import com.MangementApplication.repository.ProductRepository;



@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;
    
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    public BatchService(BatchRepository batchRepository,ProductService productService) {
        this.batchRepository = batchRepository;
        this.productService=productService;
    }

    public List<BatchResponse> getAllBatches() {
        List<Batch> batchList = batchRepository.findAll();

        List<BatchResponse> batchResponse = batchList.stream().map(batch -> {
            BatchResponse batchResponse1 = new BatchResponse();
            batchResponse1.setBatchId(batch.getBatchId());
            batchResponse1.setProductId(batch.getProduct().getId());
            batchResponse1.setQty(batch.getQty());
            batchResponse1.setExpiryDate(batch.getExpiryDate());
            batchResponse1.setCreatedAt(batch.getCreatedAt());
            batchResponse1.setUpdatedAt(batch.getUpdatedAt());
            return batchResponse1;
        }).toList();
        return batchResponse;
    }

   @Transactional
    public Batch addBatch(Long productId, BatchRequestDTO batchRequest) {
        Optional<Product> optionalProduct = productService.getProductById(productId);
        if (optionalProduct.isEmpty()) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }

        Product product = optionalProduct.get();

        validateBatchRequest(batchRequest);

        if (batchRepository.existsByProductAndExpiryDate(product, batchRequest.getExpiryDate())) {
            throw new IllegalArgumentException("A batch with the same expiry date already exists for this product");
        }

        Batch newBatch = new Batch();
        newBatch.setProduct(product);
        newBatch.setQty(batchRequest.getQty());
        newBatch.setExpiryDate(batchRequest.getExpiryDate());
        LocalDate currentDate = LocalDate.now();
        newBatch.setCreatedAt(currentDate);
        newBatch.setUpdatedAt(currentDate);

        Batch savedBatch = batchRepository.save(newBatch);

        int updatedQuantity = product.getQty() + batchRequest.getQty();
        product.setQty(updatedQuantity);
        productService.updateProduct(product);

        return savedBatch;
    }

    private void validateBatchRequest(BatchRequestDTO batchRequest) {
        if (batchRequest.getQty() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        LocalDate currentDate = LocalDate.now();
        if (batchRequest.getExpiryDate().isBefore(currentDate)) {
            throw new IllegalArgumentException("Expiry date must be in the future");
        }
    }

    public Optional<Batch> getBatchById(Long batchId) {
        return batchRepository.findById(batchId);
    }

    @Transactional
    public Optional<Batch> updateBatch(Long batchId, int newQty) {
        Optional<Batch> optionalBatch = batchRepository.findById(batchId);
        if (!optionalBatch.isPresent()) {
            return Optional.empty();
        }

        Batch batch = optionalBatch.get();
        if (batch.getExpiryDate().isBefore(LocalDate.now())) {
            Product product = batch.getProduct();
            product.setQty(product.getQty() - batch.getQty());
            batchRepository.delete(batch);
            productService.updateProductQuantityAndDate(product.getId());
            return Optional.empty(); 
        }

        if (newQty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        batch.setQty(batch.getQty() + newQty);
        batch.setUpdatedAt(LocalDate.now());
        batchRepository.save(batch);

        productService.updateProductQuantityAndDate(batch.getProduct().getId());

        return Optional.of(batch);
    }

    @Transactional
    public boolean deleteBatch(Long batchId) {
        Optional<Batch> optionalBatch = batchRepository.findById(batchId);
        if (optionalBatch.isPresent()) {
            Batch batch = optionalBatch.get();
            Product product = batch.getProduct();
            int batchQty = batch.getQty();

            batchRepository.delete(batch);

            product.setQty(product.getQty() - batchQty);
            productRepository.save(product);

            return true;
        }
        return false;
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public List<Batch> getBatchesByProduct(Product product) {
        return batchRepository.findByProduct(product);
    }


}
