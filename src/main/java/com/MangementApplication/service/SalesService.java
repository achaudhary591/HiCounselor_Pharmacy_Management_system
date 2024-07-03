package com.MangementApplication.service;




import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MangementApplication.controller.AllController;
import com.MangementApplication.entity.Batch;
import com.MangementApplication.entity.Product;
import com.MangementApplication.entity.SaleItemDto;
import com.MangementApplication.entity.SaleItems;
import com.MangementApplication.entity.Sales;
import com.MangementApplication.repository.BatchRepository;
import com.MangementApplication.repository.ProductRepository;
import com.MangementApplication.repository.SaleItemsRepository;
import com.MangementApplication.repository.SalesRepository;



@Service
public class SalesService {
    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private SaleItemsRepository saleItemsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(AllController.class);

    @Autowired
    public SalesService(SalesRepository salesRepository, SaleItemsRepository saleItemsRepository,
                        ProductRepository productRepository, BatchRepository batchRepository,ProductService productService) {
        this.salesRepository = salesRepository;
        this.saleItemsRepository = saleItemsRepository;
        this.productRepository = productRepository;
        this.batchRepository = batchRepository;
        this.productService=productService;
    }

    @Transactional
public Long processOrder(List<SaleItemDto> saleItemsRequest) {
    validateOrderData(saleItemsRequest);

    Sales sales = new Sales();
    sales.setSaleDate(LocalDate.now());
    sales.setCreatedAt(LocalDate.now());
    sales.setSaleItems(new ArrayList<>()); // Initialize the saleItems list
    BigDecimal totalAmount = BigDecimal.ZERO;

    for (SaleItemDto saleItemRequest : saleItemsRequest) {
        Long productId = saleItemRequest.getProductId();
        int quantity = saleItemRequest.getQuantity();

        logger.info("Processing order for Product ID: " + productId + " with Quantity: " + quantity);

        Product product = getProduct(productId);
        List<Batch> batches = batchRepository.findByProduct(product);
        int remainingQuantity = quantity;

        for (Batch batch : batches) {
            if (remainingQuantity <= 0) {
                break;
            }

            int availableQty = batch.getQty();
            if (availableQty >= remainingQuantity) {
                batch.setQty(availableQty - remainingQuantity);
                batch.setUpdatedAt(LocalDate.now());
                batchRepository.save(batch);

                BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
                BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(remainingQuantity));
                SaleItems saleItem = createSaleItem(sales, productId, remainingQuantity, unitPrice, subtotal);
                totalAmount = totalAmount.add(subtotal);
                sales.getSaleItems().add(saleItem);

                remainingQuantity = 0;
            } else {
                batch.setQty(0);
                batch.setUpdatedAt(LocalDate.now());
                batchRepository.save(batch);

                BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
                BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(availableQty));
                SaleItems saleItem = createSaleItem(sales, productId, availableQty, unitPrice, subtotal);
                totalAmount = totalAmount.add(subtotal);
                sales.getSaleItems().add(saleItem);

                remainingQuantity -= availableQty;
            }
        }

        if (remainingQuantity > 0) {
            logger.error("Insufficient stock for product with ID: " + productId + ". Requested: " + quantity + ", Available: " + (quantity - remainingQuantity));
            throw new RuntimeException("Insufficient stock for product with ID: " + productId);
        }
    }

    sales.setTotalAmount(totalAmount);
    salesRepository.save(sales);
    return sales.getSaleId();
}


    private void validateOrderData(List<SaleItemDto> saleItems) {
        for (SaleItemDto saleItem : saleItems) {
            if (saleItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero.");
            }
        }
    }

    private Product getProduct(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new IllegalArgumentException("Product with ID: " + productId + " not found.");
        }
        return productOptional.get();
    }

    private SaleItems createSaleItem(Sales sales, Long productId, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {
        SaleItems saleItem = new SaleItems();
        saleItem.setSale(sales);
        saleItem.setProductId(productId);
        saleItem.setQuantity(quantity);
        saleItem.setUnitPrice(unitPrice);
        saleItem.setSubtotal(subtotal);
        saleItem.setCreatedAt(LocalDate.now());
        return saleItemsRepository.save(saleItem);
    }

    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    public List<SaleItems> getSaleItemsBySaleId(Long saleId) {
        return saleItemsRepository.findBySaleSaleId(saleId);
    }
  

}

