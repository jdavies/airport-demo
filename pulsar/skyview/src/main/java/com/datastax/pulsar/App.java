package com.datastax.pulsar;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.AuthenticationFactory;
import java.util.UUID;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final String SERVICE_URL = "pulsar+ssl://pulsar-azure-westus2.streaming.datastax.com:6551";
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException
    {
        logger.debug("Hello World");

        try (
            // Create client object
            PulsarClient client = PulsarClient.builder()
            .serviceUrl(SERVICE_URL)
            .authentication(
                AuthenticationFactory.token("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnQ7NDRjNGU5N2MtNzEwYS00OWE2LTkxNWQtNzllNjExN2M4MDczO1lXbHljRzl5ZEMxbGRtVnVkSE09In0.mOLVdeDZGSHms3JvldDthEQDe3EP5vIhMJ456pmVIK08vGQ-XH0zQ2Qwz9enbsxyFI2pFgWCv5VTsDVnpwLGzFgEvygHnU-T3-GY6GPxqbltH42xcdiiokttEcougpmu3rxjePZNVstisKAPe6Yn5AlEM8L9HngQ7JHA8X9c0XUaQhfq5w09JUgz5NgpvzobIbAY04bBf2I7_C46ehNEnZo8Z3JIKm9h29rf5Vhuk8SghKIZ-f-w2T2eZCDy3bMTdPglYVJr4V2JWR2UXClqt1cFkClfHstCEteVOxQDjad4YQwYimptJ62KRZEwB4WR86XxI4ftnxrZcVkDGlqYoQ")
            )
            .build();

            // Create producer on a topic
            Producer<byte[]> producer = client.newProducer()
            .topic("persistent://airport-events/default/object-location")
            .create();
        ) {
            UUID uuid = UUID.randomUUID();

            ObjectLocation objLoc = new ObjectLocation(uuid, "fuel_truck", "F372", 0.0, 0.0);
            // Send a message to the topic
            logger.debug("Sedings a message...");
            byte[] bytes = objLoc.getBytes();
            String str = bytesToHex(bytes);
            logger.debug(str);
            producer.send(objLoc.getBytes());
            logger.debug("message...");
        } catch(Exception ex) {
            logger.error(ex.getMessage());
        }

    }

    /**
     * Convert a byte[] into a human-readable String
     */
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

}