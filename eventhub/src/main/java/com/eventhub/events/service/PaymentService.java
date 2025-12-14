package com.eventhub.events.service;

import com.safaricom.mpesa.Mpesa;
import com.eventhub.events.dao.PaymentDao;
import com.eventhub.events.model.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

 // Added fresh imports
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//

import java.io.IOException;
import java.util.Base64;
import java.util.Map;


@Service
public class PaymentService {

    private final Mpesa mpesa;
    private final PaymentDao paymentDao;
    private final String appSecret;
    private final String appKey;
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

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

        this.appKey = appKey;
        this.appSecret = appSecret;
        this.mpesa = new Mpesa(appKey, appSecret);
        this.paymentDao = paymentDao;
    }

    public String authenticate() throws IOException {

        String credentials = appKey + ":" + appSecret;
        // byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.ISO_8859_1));


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .get()
                .addHeader("authorization", "Basic "+encoded)
                .addHeader("cache-control", "no-cache")

                .build();

        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                String errorBody = response.body() != null ? response.body().string() : "empty body";

                log.error(
                        "Mpesa OAUTH Failure! | HTTP {} | Message: {} | Body: {}",
                        response.code(),
                        response.message(),
                        errorBody
                );
                throw new IOException("Failed to authenticate with Mpesa: " + response);
            }
            JSONObject jsonObject=new JSONObject(response.body().string());
            return jsonObject.getString("access_token");
        }
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
        payment.setPaymentRef(ref);

        paymentDao.save(payment);
    }
}
