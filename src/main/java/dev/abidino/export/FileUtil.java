package dev.abidino.export;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {
    public static void convertByteArrayOutputStreamToFile(ByteArrayOutputStream byteArrayOutputStream, String filePath) {
        if (byteArrayOutputStream == null) {
            return;
        }
        File file = new File(filePath);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byteArrayOutputStream.writeTo(fileOutputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("file is successfully created : " + filePath);
    }

    public static String createFileName(String prefix, String suffix) {
        long epochSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return prefix + Math.random() + epochSecond + suffix;
    }

    public static String convertToBase64(ByteArrayOutputStream outputStream) {
        byte[] byteArray = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(byteArray);
    }
}
