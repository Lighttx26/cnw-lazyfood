package lazyfood.demo.utils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

import javax.sql.rowset.serial.SerialBlob;

public class general {
    public static String convertBlobToBase64(Blob bytes) {

        if (bytes == null)
            return "";

        InputStream inputStream = null;
        try {
            inputStream = bytes.getBinaryStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = outputStream.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            inputStream.close();
            outputStream.close();
            return base64Image;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return "";
            // throw new RuntimeException(e);
        }
    }

    public static String fileToBlob(InputStream file) {
        try {
            byte[] bArray = new byte[1000];

            ArrayList<Byte> byteList = new ArrayList<>();

            try (InputStream fis = file) {

                // Converting input file in list of bytes
                while (fis.read(bArray) > 0) {
                    for (byte b : bArray)
                        byteList.add(b);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Converting list of bytes into array of bytes
            // as SerialBlob class takes array of bytes
            byte[] byteArray = new byte[byteList.size()];

            for (int i = 0; i < byteList.size(); i++) {
                byteArray[i] = (byte) byteList.get(i);
            }
            return convertBlobToBase64(new SerialBlob(byteArray));
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }



    public static String BytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static Blob Base64toBlob(String base64) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            return new SerialBlob(decodedBytes);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
