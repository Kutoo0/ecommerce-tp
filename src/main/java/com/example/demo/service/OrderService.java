package com.example.demo.service;

import com.example.demo.models.Order;
import com.example.demo.models.Product;
import com.example.demo.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository,
                        ClientService clientService,
                        ProductService productService) {
        this.orderRepository = orderRepository;
        this.clientService = clientService;
        this.productService = productService;
    }

    public Order createOrder(Order request) {
        // 1. productIds not empty, no duplicates -> 400
        List<String> productIds = request.getProductIds();
        if (productIds == null || productIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "productIds must contain at least one id");
        }
        if (new HashSet<>(productIds).size() != productIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "productIds must not contain duplicates");
        }
        String clientId = request.getClientId();
        if (clientId == null || clientId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "clientId is required");
        }

        // 2. client exists -> otherwise 404
        clientService.getClientById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Client not found: " + clientId));

        // 3 + 4. fetch each product, add up the prices -> otherwise 404
        double total = 0.0;
        for (String productId : productIds) {
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Product not found: " + productId));
            total += product.getPrice();
        }

        // 5. save (any totalAmount sent by the caller is ignored)
        Order order = new Order(clientId, new ArrayList<>(productIds), total);
        // 6. return the saved order
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }
}