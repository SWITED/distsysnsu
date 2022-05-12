package com.example;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BZip2Reader {

    public static InputStreamReader getBZip2Reader(InputStream is) throws CompressorException {
        return new InputStreamReader(new CompressorStreamFactory().createCompressorInputStream(new BufferedInputStream(is)));
    }
}
