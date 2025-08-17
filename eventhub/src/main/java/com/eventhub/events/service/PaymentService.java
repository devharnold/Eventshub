package com.eventhub.events.service;

import com.safaricom.mpesa.Mpesa;
import com.eventhub.events.dao.PaymentDao;
import com.eventhub.events.model.Payment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
public class PaymentService {

    private final Mpesa mpesa;
    private final PaymentDao paymentDao;

    public PaymentService(PaymentDao paymentDao) {
        this.mpesa = new Mpesa("appKey", "appSecret");
        this.paymentDao = paymentDao;
    }

    public String initiateSTKPush(String phoneNumber, String amount, String accountRef) throws IOException {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        String password = Base64.getEncoder().encodeToString(("Business-short-code" + "PassKey" + timestamp).getBytes());

        return mpesa.STKPushSimulation(
                "Business-short-code",
                password,
                timestamp,
                "CustomerPayBillOnline",
                amount,
                phoneNumber,
                phoneNumber,
                "Till/PayBill",
                "https://domain.com/api/payments/confirmation",
                "https://domain.com/api/payments/timeout",
                accountRef,
                "Payment for order " + accountRef
        );
    }

    public String checkSTKStatus(String checkoutRequestId) throws IOException {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        String password = Base64.getEncoder().encodeToString(("Business-short-code" + "PassKey" + timestamp).getBytes());

        return mpesa.STKPushTransactionStatus(
                "Business-short-code",
                password,
                timestamp,
                checkoutRequestId
        );
    }

    // Confirmation (called by controller when callback received)
    public void processConfirmation(Map<String, Object> payload) {
        // Extract Fields
        String transactionId = (String) payload.get("TransID");
        String amount = String.valueOf(payload.get("TransAmount"));
        String phone = (String) payload.get("MSISDN");
        String ref = (String) payload.get("BillRefNumber");

        // TODO: Save to DB
        Payment payment = new Payment();
        payment.setTransactionId(transactionId);
        payment.setAmount(amount);
        payment.setPhone(phone);
        payment.setReference(ref);

        paymentDao.save(payment);
    }
}
