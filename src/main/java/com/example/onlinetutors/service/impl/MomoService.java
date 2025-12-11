package com.example.onlinetutors.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class MomoService {

    @Value("${spring.momo.phone}")
    String momoPhone;

    public String handleMomoPayment(String amount, String note) throws UnsupportedEncodingException {
        log.info("Handling MoMo payment process");
        String paymentUrl =
                "https://payment.momo.vn/v2/gateway/pay?phone=" + momoPhone +
                        "&amount=" + amount +
                        "&note=" + URLEncoder.encode(note, "UTF-8");
        return paymentUrl;
    }

    // Tạo QR Code từ URL
    public String generateQRCode(String content) throws Exception {
        int size = 300;

        BitMatrix matrix = new MultiFormatWriter().encode(
                content, BarcodeFormat.QR_CODE, size, size
        );

        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        byte[] bytes = baos.toByteArray();

        return Base64.getEncoder().encodeToString(bytes);
    }
}
