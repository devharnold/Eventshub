package com.eventhub.events.controller;

import com.eventhub.events.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Initiate STK Push
    @PostMapping("/mpesa/initiate")
    public String initiatePayment(
            @RequestParam String phoneNumber,
            @RequestParam String amount,
            @RequestParam String accountRef) throws IOException {
        return paymentService.initiateSTKPush(phoneNumber, amount, accountRef);
    }

    // Confirms Mpesa Payments
    @PostMapping("/mpesa/confirmation")
    public String receiveConfirmation(@RequestBody Map<String, Object> payload) {
        paymentService.processConfirmation(payload);
        return "{\"ResultCode\":0,\"ResultDesc\":\"Success\"}";
    }
}
