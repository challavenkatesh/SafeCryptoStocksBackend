package com.safecryptostocks.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {

    @GetMapping("/realtime/{symbol}")
    public ResponseEntity<?> getStockData(@PathVariable String symbol) {
        try {
            // ✅ Yahoo Finance public chart API
            String url = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching stock data: " + e.getMessage());
        }
    }
}
