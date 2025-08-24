package com.eventhub.events.service;

import com.safaricom.mpesa.Mpesa;
import com.eventhub.events.dao.PaymentDao;
import com.eventhub.events.model.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
public class PaymentService {

    private final Mpesa mpesa;
    private final PaymentDao paymentDao;

    @Value("${mpesa.businessShortCode}")
    private String businessShortCode;

    @Value("${mpesa.passKey}")
    private String passKey;

    @Value("${mpesa.callbackUrl}")
    private String callbackUrl;

    @Value("${mpesa.timeoutUrl}")
    private String timeoutUrl;

    public PaymentService(
            @Value("${mpesa.appKey}") String appKey,
            @Value("${mpesa.appSecret}") String appSecret,
            PaymentDao paymentDao) {
        this.mpesa = new Mpesa(appKey, appSecret);
        this.paymentDao = paymentDao;
    }

    public String initiateSTKPush(String phoneNumber, String amount, String accountRef) throws IOException {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

        String password = Base64.getEncoder()
                .encodeToString((businessShortCode + passKey + timestamp).getBytes());

        return mpesa.STKPushSimulation(
                businessShortCode,
                password,
                timestamp,
                "CustomerPayBillOnline",
                amount,
                phoneNumber,
                phoneNumber,
                businessShortCode, // if you’re using a paybill/till
                callbackUrl,
                timeoutUrl,
                accountRef,
                "Payment for order " + accountRef
        );
    }

    public String checkSTKStatus(String checkoutRequestId) throws IOException {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

        String password = Base64.getEncoder()
                .encodeToString((businessShortCode + passKey + timestamp).getBytes());

        return mpesa.STKPushTransactionStatus(
                businessShortCode,
                password,
                timestamp,
                checkoutRequestId
        );
    }

    public boolean confirmPayment(String checkoutRequestId) throws IOException {
        String response = checkSTKStatus(checkoutRequestId);
        return response.contains("Success");
    }

    // Confirmation (called by controller when callback received)
    public void processConfirmation(Map<String, Object> payload) {
        String transactionId = (String) payload.get("TransID");
        String amount = String.valueOf(payload.get("TransAmount"));
        String phone = (String) payload.get("MSISDN");
        String ref = (String) payload.get("BillRefNumber");

        Payment payment = new Payment();
        payment.setTransactionId(transactionId);
        payment.setAmount(amount);
        payment.setPhone(phone);
        payment.setReference(ref);

        paymentDao.save(payment);
    }
}
