package com.vinz.tape.codec;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

@Component
public class IntListCodec {

    @PostConstruct
    public void init() {
    }

    public String compress(int[] data) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);

        DeflaterOutputStream def = new DeflaterOutputStream(baos, deflater);

        DataOutputStream dos = new DataOutputStream(def);

        for (int datum : data) {

            dos.writeInt(datum);
        }

        def.close();
        baos.close();

        return baToString(baos.toByteArray());
    }

    public int[] decompress(String data) throws IOException {

        int[] out = new int[3];

        byte[] binary = stringToBa(data);

        DataInputStream dataInputStream = new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(binary)));

        for (int i = 0; i < 3; i++) {

            out[i] = dataInputStream.readInt();
        }

        return out;
    }

    private String baToString(byte[] data) throws UnsupportedEncodingException {

        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] stringToBa(String data) {

        return Base64.getDecoder().decode(data);
    }
}
