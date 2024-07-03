package com.MangementApplication.controller;

import com.MangementApplication.entity.*;
import com.MangementApplication.repository.SalesRepository;
import com.MangementApplication.requests.SaleItemsRequest;
import com.MangementApplication.responses.BatchResponse;
import com.MangementApplication.responses.ProductResponse;
import com.MangementApplication.service.BatchService;
import com.MangementApplication.service.ProductService;
import com.MangementApplication.service.SalesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class AllController {
    private static final Logger logger = LoggerFactory.getLogger(AllController.class);
    private ProductService productService;
    private BatchService batchService;
    private SalesService salesService;
    @Autowired
    private SalesRepository salesRepository;


    @Autowired
    public AllController(ProductService productService, BatchService batchService, SalesService salesService) {
        this.productService = productService;
        this.batchService = batchService;
        this.salesService = salesService;
    }

    //Endpoint 1
    @PostMapping("/product/add")
    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody ProductRequestDTO productRequest) {
        try {
            if (productRequest.getName() == null || productRequest.getName().isEmpty()) {
                return ResponseEntity.badRequest().body(createResponseMessage(false, "Product name cannot be blank or null"));
            }

            if (productRequest.getPrice() <= 0) {
                return ResponseEntity.badRequest().body(createResponseMessage(false, "Product price must be greater than 0"));
            }

            ProductResponse newProduct = productService.addProduct(productRequest);
            return ResponseEntity.ok(createResponseMessage(true, newProduct)); // Return HttpStatus.OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(createResponseMessage(false, e.getMessage()));
        }


    }

    //Endpoint 2
    @GetMapping("/product")
    public ResponseEntity<Object> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.status(404).body(createResponseMessage(false, "No product list found for pharmacy"));
        } else {
            return ResponseEntity.ok(products);
        }


    }

    //Endpoint 3
    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(createResponseMessage(false, "Product not found")));

    }

    //Endpoint 4
    @PutMapping("/product/update/{product_id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("product_id") Long productId, @RequestBody ProductRequestDTO updateProductRequest) {
        ProductResponse updatedProduct = productService.updateProduct(productId, updateProductRequest.getName(), updateProductRequest.getPrice());

        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.status(404).body(createResponseMessage(false, "Product not found or validation failed"));
        }

    }

    //Endpoint 5
    @DeleteMapping("/product/delete/{product_id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("product_id") Long productId) {
        boolean deletionSuccessful = productService.deleteProduct(productId);

        if (deletionSuccessful) {
            return ResponseEntity.ok(createResponseMessage(true, "Product deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(createResponseMessage(false, "Product not found or deletion failed"));
        }

    }


    //Endpoint 6
    @PostMapping("/product/batch/add/{product_id}")
    public ResponseEntity<Object> addBatch(@PathVariable("product_id") Long productId, @RequestBody BatchRequestDTO batchRequest) {
        try {
            logger.info("Received request to add batch for product ID: {}", productId);
            Batch newBatch = batchService.addBatch(productId, batchRequest);
            logger.info("Batch added successfully for product ID: {}", productId);

            // Prepare the response structure
            Map<String, Object> response = new HashMap<>();
            response.put("batchId", newBatch.getBatchId());
            response.put("productId", newBatch.getProduct().getId());
            response.put("qty", newBatch.getQty());
            response.put("expiryDate", newBatch.getExpiryDate());
            response.put("createdAt", newBatch.getCreatedAt());
            response.put("updatedAt", newBatch.getUpdatedAt());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error adding batch for product ID: {}: {}", productId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponseMessage(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error adding batch for product ID: {}: {}", productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponseMessage(false, "Internal Server Error: " + e.getMessage()));
        }
    }

    //Endpoint 7
    @GetMapping("/product/batch")
    public ResponseEntity<Object> getAllBatches() {
        List<BatchResponse> batches = batchService.getAllBatches();
        if (batches.isEmpty()) {
            return ResponseEntity.status(404).body(createResponseMessage(false, "No batches found"));
        } else {
            return ResponseEntity.ok(batches);
        }
    }

    //Endpoint 8
    @GetMapping("/product/batchById/{batch_id}")
    public ResponseEntity<Object> getBatchById(@PathVariable("batch_id") Long batchId) {
        Optional<Batch> batch = batchService.getBatchById(batchId);

        if (batch.isPresent()) {
            Batch foundBatch = batch.get();
            Map<String, Object> response = new HashMap<>();
            response.put("batchId", foundBatch.getBatchId());
            response.put("productId", foundBatch.getProduct().getId());
            response.put("qty", foundBatch.getQty());
            response.put("expiryDate", foundBatch.getExpiryDate());
            response.put("createdAt", foundBatch.getCreatedAt());
            response.put("updatedAt", foundBatch.getUpdatedAt());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(createResponseMessage(false, "Batch not found"));
        }

    }

    //Endpoint 9
    @PostMapping("/product/batch/update/{batch_id}")
    public ResponseEntity<Object> updateBatch(@PathVariable("batch_id") Long batchId, @RequestBody Map<String, Integer> requestDTO) {
         Integer newQty = requestDTO.get("qty");

    if (newQty == null || newQty == 0) {
        return ResponseEntity.status(400).body(createResponseMessage(false, "Quantity cannot be null or empty"));
    }

    try {
        Optional<Batch> updatedBatch = batchService.updateBatch(batchId, newQty);
        if (!updatedBatch.isPresent()) {
            return ResponseEntity.status(400).body(createResponseMessage(false, "Batch expired and deleted"));
        }

        Batch batch = updatedBatch.get();
        Map<String, Object> response = new HashMap<>();
        response.put("batchId", batch.getBatchId());
        response.put("productId", batch.getProduct().getId());
        response.put("qty", batch.getQty());
        response.put("expiryDate", batch.getExpiryDate());
        response.put("createdAt", batch.getCreatedAt());
        response.put("updatedAt", batch.getUpdatedAt());

        return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(400).body(createResponseMessage(false, e.getMessage()));
    }

    }

    //Endpoint 10
    @PostMapping("/product/batch/delete/{batch_id}")
    public ResponseEntity<Object> deleteBatch(@PathVariable("batch_id") Long batchId) {
        boolean isDeleted = batchService.deleteBatch(batchId);

        Map<String, Object> response = new HashMap<>();
        if (isDeleted) {
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Batch with the given ID does not exist.");
            return ResponseEntity.status(400).body(response);
        }
        
    }

    //Endpoint 11
    @GetMapping("/product/stock/{product_id}")
    public ResponseEntity<Object> getProductStockDetails(@PathVariable("product_id") Long productId) {
        Optional<Product> optionalProduct = batchService.getProductById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            List<Batch> batches = batchService.getBatchesByProduct(product);

            int totalQuantity = batches.stream().mapToInt(Batch::getQty).sum();
            String alertMessage = totalQuantity >= 10 ? "Enough stock" : "Add stock";

            Map<String, Object> response = new HashMap<>();
            response.put("productName", product.getName());
            response.put("productId", product.getId());
            response.put("batches", batches.stream().map(batch -> {
                Map<String, Object> batchDetails = new HashMap<>();
                batchDetails.put("batchId", batch.getBatchId());
                batchDetails.put("expiryDate", batch.getExpiryDate());
                batchDetails.put("quantity", batch.getQty());
                return batchDetails;
            }).toList());
            response.put("totalQuantity", totalQuantity);
            response.put("alertMessage", alertMessage);

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Product with the given ID does not exist.");
            return ResponseEntity.badRequest().body(response);
        }

    }

    //Endpoint 12
    @PostMapping("/processOrder")
    public ResponseEntity<Object> processOrder(@RequestBody SaleItemsRequest saleItemsRequest) {
        try {
            List<SaleItemDto> saleItems = saleItemsRequest.getSaleItems();
        Long saleId = salesService.processOrder(saleItems);

        // Fetch the Sales object to get the details
        Optional<Sales> salesOptional = salesRepository.findById(saleId);
        if (!salesOptional.isPresent()) {
            return ResponseEntity.status(400).body(createResponseMessage(false, "Sale not found"));
        }

        Sales sales = salesOptional.get();
        Map<String, Object> response = new HashMap<>();
        response.put("saleId", sales.getSaleId());
        response.put("saleDate", sales.getSaleDate());
        response.put("totalAmount", sales.getTotalAmount());
        response.put("createdAt", sales.getCreatedAt());
        response.put("message", "Order processed successfully");

        return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(400).body(createResponseMessage(false, e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(400).body(createResponseMessage(false, "An unexpected error occurred."));
        }
}


    //Endpoint 13
    @GetMapping("/allSales")
    public ResponseEntity<Object> getAllSales() {
        List<Sales> allSales = salesService.getAllSales();
        if (allSales.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            System.out.println(allSales);
            return ResponseEntity.ok(allSales);
        }
        
    }

    //Endpoint 14
    @GetMapping("/sales/{saleId}/items")
    public ResponseEntity<Object> getSaleItemsBySaleId(@PathVariable Long saleId) {
        List<SaleItems> saleItems = salesService.getSaleItemsBySaleId(saleId);
    if (saleItems.isEmpty()) {
        return ResponseEntity.notFound().build();
    } else {
        List<Map<String, Object>> response = new ArrayList<>();
        for (SaleItems item : saleItems) {
            Map<String, Object> saleItemMap = new HashMap<>();
            saleItemMap.put("saleItemId", item.getSaleItemId());
            saleItemMap.put("saleId", item.getSale().getSaleId()); // Include saleId here
            saleItemMap.put("productId", item.getProductId());
            saleItemMap.put("quantity", item.getQuantity());
            saleItemMap.put("unitPrice", item.getUnitPrice());
            saleItemMap.put("subtotal", item.getSubtotal());
            saleItemMap.put("createdAt", item.getCreatedAt());
            response.add(saleItemMap);
        }
        return ResponseEntity.ok(response);

    }
    }


    private Map<String, Object> createResponseMessage(Boolean status, Object message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", status);
        response.put("message", message);
        return response;
    }


}

    
    






