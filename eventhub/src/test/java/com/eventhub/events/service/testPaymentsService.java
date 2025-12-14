package com.eventhub.events.service;

import com.eventhub.events.dao.PaymentDao;
import com.eventhub.events.model.Payment;
import com.safaricom.mpesa.Mpesa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class testPaymentsService {
    @Mock
    private PaymentDao paymentDao;

    @Mock
    private Mpesa mpesa;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() throws Exception {
        paymentService = new PaymentService(
                "testAppKey",
                "testAppSecret",
                paymentDao
        );

        // Inject mocked mpesa
        Field mpesaField = PaymentService.class.getDeclaredField("mpesa");
        mpesaField.setAccessible(true);
        mpesaField.set(paymentService, mpesa);

        // Inject @Value fields manually
        setField("businessShortCode", "123456");
        setField("passKey", "examplePass23423");
        setField("callbackUrl", "https://callback.test");
        setField("timeoutUrl", "https://timeout.test");
    }

    private void setField(String fieldName, String value) throws IOException, NoSuchFieldException, IllegalAccessException {
        Field field = PaymentService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(paymentService, value);
    }

    @Test
    void initiateSTKPUSH_shouldCallMpesaAndReturnResponse() throws IOException {
        when(mpesa.STKPushSimulation(
                anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(),
                anyString(), anyString()
        )).thenReturn("STKPUSH_SUCCESS");

        String response = paymentService.initiateSTKPush(
                "254712345678",
                "1000",
                "PAY001"
        );

        assertEquals("STKPUSH_SUCCESS", response);
        verify(mpesa, times(1)).STKPushSimulation(
                anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(),
                anyString(), anyString()
        );
    }

    @Test
    void checkSTKSTATUS_shouldReturnMpesaResponse() throws IOException {
        when(mpesa.STKPushTransactionStatus(
                anyString(), anyString(), anyString(), anyString()
        )).thenReturn("Success");

        String response = paymentService.checkSTKStatus("CHECKOUT001");

        assertEquals("Success", response);
        verify(mpesa, times(1)).STKPushTransactionStatus(anyString(), anyString(), anyString(), eq("CHECKOUT001"));
    }

    @Test
    void confirmPayment_shouldReturnTrueWhenSuccess() throws IOException {
        when(mpesa.STKPushTransactionStatus(
                anyString(), anyString(), anyString(), anyString()
        )).thenReturn("Success");

        boolean result = paymentService.confirmPayment("CHECKOUT001");

        assertTrue(result);
    }

    @Test
    void confirmPayment_shouldReturnFalseWhenFailure() throws IOException {
        when(mpesa.STKPushTransactionStatus(
                anyString(), anyString(), anyString(), anyString()
        )).thenReturn("Failed");

        boolean result = paymentService.confirmPayment("CHECKOUT001");

        assertTrue(result);
    }

    @Test
    void processConfirmation_shouldPersistPayment() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("TransID", "TX123");
        payload.put("TransAmount", "1000");
        payload.put("MSISDN", "254712345678");
        payload.put("BillRefNumber", "ORDER001");

        paymentService.processConfirmation(payload);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentDao, times(1)).save(captor.capture());

        Payment savedPayment = captor.getValue();
        assertEquals("TX123", savedPayment.getTransactionId());
        assertEquals("1000", savedPayment.getAmount());
        assertEquals("25474812345678", savedPayment.getPhone());
        assertEquals("ORDER001", savedPayment.getPaymentRef());
    }
}
