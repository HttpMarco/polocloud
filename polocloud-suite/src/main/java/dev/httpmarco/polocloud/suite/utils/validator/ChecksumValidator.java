package dev.httpmarco.polocloud.suite.utils.validator;

import org.w3c.dom.Document;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class ChecksumValidator {

    public static String calculateSHA1Checksum(Document doc) throws NoSuchAlgorithmException, TransformerException {
        return sha1Hash(documentToBytes(doc));
    }

    private static byte[] documentToBytes(Document doc) throws TransformerException {
        var transformerFactory = TransformerFactory.newInstance();
        var transformer = transformerFactory.newTransformer();
        var outputStream = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(outputStream));
        return outputStream.toByteArray();
    }

    private static String sha1Hash(byte[] data) throws NoSuchAlgorithmException {
        var digest = MessageDigest.getInstance("SHA-1");
        var hashBytes = digest.digest(data);
        return bytesToHex(hashBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        var hexString = new StringBuilder();
        for (var b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static boolean validateSHA1Checksum(Document document, String expectedChecksum) throws NoSuchAlgorithmException, TransformerException {
        return calculateSHA1Checksum(document).equalsIgnoreCase(expectedChecksum);
    }
}