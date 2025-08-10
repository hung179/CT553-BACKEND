package com.ecommerce.studentmarket.student.ewallet.services;

import com.ecommerce.studentmarket.student.ewallet.enums.TrangThaiGiaoDich;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import jakarta.annotation.PostConstruct;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
@Transactional
public class PaypalService {

    private static final String PAYPAL_PAYOUTS_URL = "https://api-m.sandbox.paypal.com/v1/payments/payouts";

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.secret}")
    private String clientSecret;

    @Autowired
    private APIContext apiContext;

    // PayPalHttpClient cho SDK mới
    private PayPalHttpClient payPalHttpClient;

    @Autowired
    private PaymentService paymentService;

    @PostConstruct
    public void initPayPalHttpClient() {
        PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
        this.payPalHttpClient = new PayPalHttpClient(environment);
    }

    public Payment createPaymentWithPaypal(BigDecimal total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent.toUpperCase());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

    private String getAccessToken() throws IOException {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpURLConnection conn = (HttpURLConnection) new URL("https://api-m.sandbox.paypal.com/v1/oauth2/token").openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write("grant_type=client_credentials".getBytes());
        }

        String response = new String(conn.getInputStream().readAllBytes());
        return new JSONObject(response).getString("access_token");
    }

    public String sendPayout(String receiverEmail, String amount, String currency, String mssv, BigDecimal amountVND) throws IOException {
        String token = getAccessToken();
        String formattedAmount = String.format("%.2f", new BigDecimal(amount));

        String body = """
        {
          "sender_batch_header": {
            "sender_batch_id": "%s",
            "email_subject": "You have a payout!"
          },
          "items": [
            {
              "recipient_type": "EMAIL",
              "amount": {
                "value": "%s",
                "currency": "%s"
              },
              "receiver": "%s",
              "note": "%s",
              "sender_item_id": "%s"
            }
          ]
        }
        """.formatted(
                UUID.randomUUID(),
                formattedAmount,
                currency.toUpperCase(),
                receiverEmail,
                amountVND,
                mssv
        );

        HttpURLConnection conn = (HttpURLConnection) new URL(PAYPAL_PAYOUTS_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();
        String response = new String(is.readAllBytes());

        if (status >= 400) {
            throw new IOException("PayPal error: " + response);
        }
        // Trích batchId từ response JSON
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(response, new TypeReference<>() {});
        Map<String, Object> batchHeader = (Map<String, Object>) map.get("batch_header");
        return (String) batchHeader.get("payout_batch_id");
    }

    public void processWebhook(Map<String, Object> payload, Map<String, String> headers) {
        String eventType = (String) payload.get("event_type");
            Map<String, Object> resource = (Map<String, Object>) payload.get("resource");
            String payoutBatchId = (String) resource.get("payout_batch_id");
            if (payoutBatchId == null) {
                throw new IllegalStateException("payoutBatchId is missing in webhook payload");
            }
        if ("PAYMENT.PAYOUTS-ITEM.SUCCEEDED".equals(eventType)) {
            paymentService.updateTransactionByBatchId(payoutBatchId, TrangThaiGiaoDich.DATHANHTOAN);
        }else{
            paymentService.updateTransactionByBatchId(payoutBatchId, TrangThaiGiaoDich.CHUATHANHTOAN);
        }
    }

}
