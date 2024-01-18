package lazyfood.demo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BinaryTypeHandler {
    public static byte[] InputStreamToByteArray(InputStream inputStream) {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024]; // You can adjust the buffer size as needed

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteOutputStream.write(buffer, 0, bytesRead);
            }

            return byteOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String ByteArrayToBase64(byte[] Image) {
        return java.util.Base64.getEncoder().encodeToString(Image);
    }

    public static byte[] Base64ToByteArray(String Image) {
        return java.util.Base64.getDecoder().decode(Image);
    }
}
