package org.example.vehicles_rental.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;

public class KhqrBuilder {

    public static String build(String bakongAccountId, String merchantName, String merchantCity,
                               BigDecimal amount, String currencyCode,
                               String billNumber, String mobileNumber, String storeLabel,
                               boolean dynamic) {

        StringBuilder payload = new StringBuilder();

        payload.append(tlv("00", "01"));                         // Payload Format Indicator
        payload.append(tlv("01", dynamic ? "12" : "11"));        // Point of Initiation

        // Merchant Account Info (Bakong)
        StringBuilder merchantInfo = new StringBuilder();
        merchantInfo.append(tlv("00", "bakong.gov.kh"));
        merchantInfo.append(tlv("01", bakongAccountId));
        payload.append(tlv("29", merchantInfo.toString()));

        payload.append(tlv("52", "5999"));                        // Merchant Category Code
        String currency = "USD".equalsIgnoreCase(currencyCode) ? "840" : "116"; // 840=USD, 116=KHR
        payload.append(tlv("53", currency));

        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            payload.append(tlv("54", amount.setScale(2, RoundingMode.HALF_UP).toPlainString()));
        }

        payload.append(tlv("58", "KH"));
        payload.append(tlv("59", truncate(merchantName, 25)));
        payload.append(tlv("60", truncate(merchantCity, 15)));

        // Additional Data Field (bill number / mobile / store label)
        StringBuilder additional = new StringBuilder();
        if (billNumber != null)   additional.append(tlv("01", billNumber));
        if (mobileNumber != null) additional.append(tlv("02", mobileNumber));
        if (storeLabel != null)   additional.append(tlv("03", storeLabel));
        if (additional.length() > 0) {
            payload.append(tlv("62", additional.toString()));
        }

        payload.append("6304"); // CRC tag + length placeholder
        payload.append(crc16(payload.toString()));

        return payload.toString();
    }

    private static String tlv(String tag, String value) {
        return tag + String.format("%02d", value.length()) + value;
    }

    private static String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max) : s;
    }

    private static String crc16(String data) {
        int crc = 0xFFFF;
        for (byte b : data.getBytes(StandardCharsets.UTF_8)) {
            crc ^= (b << 8) & 0xFFFF;
            for (int i = 0; i < 8; i++) {
                crc = ((crc & 0x8000) != 0) ? ((crc << 1) ^ 0x1021) & 0xFFFF : (crc << 1) & 0xFFFF;
            }
        }
        return String.format("%04X", crc);
    }
}