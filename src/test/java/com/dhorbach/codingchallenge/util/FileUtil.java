package com.dhorbach.codingchallenge.util;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {

    public static String readFromFileToString(final String filePath) throws IOException {
        final File resource = new ClassPathResource(filePath).getFile();
        final byte[] byteArray = Files.readAllBytes(resource.toPath());
        return new String(byteArray);
    }
}
