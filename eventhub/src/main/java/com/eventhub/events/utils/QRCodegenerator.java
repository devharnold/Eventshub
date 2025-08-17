package com.eventhub.events.utils;

import io.nayuki.qrcodegen.QrCode;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.time.LocalDateTime;

// TODO: Feed in some data in the Barcode, eg; username, event name,

public class QRCodegenerator {
    /**
     * generates a QR code for every ticket that contains user details and event-related details
     * @param username: name of the user
     * @param eventName: name of the event
     * @param organizationName: name of the organization
     * @param eventDate: date of the event
     * @return A buffered image of the generated QR code
     */
    public static BufferedImage generateTicketQRCode (String username, String eventName, String organizationName, LocalDateTime eventDate) throws Exception {
        String qrContent = buildQRContent(username, eventName, organizationName, eventDate);
        return generateQRCode(qrContent);
    }

    public static String buildQRContent(String username, String eventName, String organizationName, LocalDateTime eventDate) throws Exception {
        return String.format("User: %s\nEvent: %s\nOrganization: %s\nDate: %s\n", username, eventName, organizationName, eventDate);
    }

    private static BufferedImage generateQRCode(String text) throws Exception {
        QrCode qrCode = QrCode.encodeText(text, QrCode.Ecc.MEDIUM);
        return toImage(qrCode, 4, 10);
    }

    private static BufferedImage toImage(QrCode qr, int scale, int border) {
        return toImage(qr, scale, border, 0xFFFFFF, 0x000000);
    }

    private static BufferedImage toImage(QrCode qr, int scale, int border, int lightColor, int darkColor) {
        Objects.requireNonNull(qr);
        if (scale <= 0 || border < 0) {
            throw new IllegalArgumentException("Scale and border must be positive");
        }
        if (border > Integer.MAX_VALUE / 2 || qr.size + border * 2L > Integer.MAX_VALUE / scale) {
            throw new IllegalArgumentException("Scale or border too large");
        }

        int size = (qr.size + border * 2) * scale;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                boolean isDark = qr.getModule(x / scale - border, y / scale - border);
                image.setRGB(x, y, isDark ? darkColor : lightColor);
            }
        }
        return image;
    }
}