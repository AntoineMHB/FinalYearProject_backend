package com.antoine.springJwt.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.antoine.springJwt.model.PaymentRequest;
import com.antoine.springJwt.service.ReceiptService;

import org.springframework.core.io.Resource;



@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${flutterwave.secret-key}")
    private String secretKey;

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/initialize")
    public ResponseEntity<?> initializePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + secretKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> payload = new HashMap<>();
            payload.put("tx_ref", UUID.randomUUID().toString());
            payload.put("amount", paymentRequest.getAmount());
            payload.put("currency", "USD");
            payload.put("redirect_url", "http://localhost:5173/payment-success");
            payload.put("customer", Map.of(
                "email", paymentRequest.getEmail(),
                "name", paymentRequest.getName()
            ));
            payload.put("customizations", Map.of(
                "title", "LIKUTA Pay",
                "description", "Payment for service"
            ));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.flutterwave.com/v3/payments", entity, Map.class
            );

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to initialize payment");
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody Map<String, Object> payload, 
                                           @RequestHeader("verif-hash") String hash) throws IOException {
        // Verify the hash if needed (security)
        String status = (String) payload.get("status");
        if ("successful".equals(status)) {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            String email;
            email = (String) ((Map<String, Object>) data.get("customer")).get("email");
            double amount = Double.parseDouble(data.get("amount").toString());
            String txRef = (String) data.get("tx_ref");

            // Save transaction, generate PDF
            receiptService.generateAndSaveReceipt(email, amount, txRef);
        }
        return ResponseEntity.ok("Webhook received");
    }

    @GetMapping("/receipt/{txRef}")
    public ResponseEntity<Resource> downloadReceipt(@PathVariable String txRef) throws IOException {
        File receipt = new File("receipts/" + txRef + ".pdf");

        if (!receipt.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(receipt));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + receipt.getName())
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
    }
}
