package com.ecommerce.studentmarket.common.exchangerate;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ExchangeRateService {

    public BigDecimal getUsdToVndRate() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://open.er-api.com/v6/latest/USD";

        Map response = restTemplate.getForObject(url, Map.class);

        if (response == null || !response.containsKey("rates")) {
            throw new RuntimeException("Không thể lấy được tỷ giá từ API");
        }

        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        Double rate = rates.get("VND");

        if (rate == null) {
            throw new RuntimeException("Không lấy được tỷ giá USD → VND");
        }

        return BigDecimal.valueOf(rate);
    }

    public BigDecimal convertUsdToVnd(BigDecimal usdAmount) {
        return usdAmount.multiply(getUsdToVndRate());
    }

    public BigDecimal convertVndToUsd(BigDecimal vndAmount) {
        return vndAmount.divide(getUsdToVndRate(), 2, BigDecimal.ROUND_HALF_UP);
    }
}
