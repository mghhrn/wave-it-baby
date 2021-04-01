package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RawRead {

    public static void main(String[] args) throws IOException {
        File file = new File("/home/meti/Music/Pink Noise/pink_88k_-3dBFS.wav");
        FileInputStream fileInputStream = new FileInputStream(file);

        byte[] fourBytes = new byte[4];
        byte[] twoBytes = new byte[2];

        fileInputStream.read(fourBytes, 0, 4);
        String chunkId = new String(fourBytes);
        System.out.println("chunkId : " + chunkId);

        fileInputStream.read(fourBytes, 0, 4);
        int chunkSize = ByteBuffer.wrap(fourBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        System.out.println("chunk size : " + chunkSize);

        fileInputStream.read(fourBytes, 0, 4);
        System.out.println("format: " + new String(fourBytes));

        fileInputStream.read(fourBytes, 0, 4);
        System.out.println("sub chunk 1 id: " + "'" +new String(fourBytes) + "'");

        fileInputStream.read(fourBytes, 0, 4);
        chunkSize = ByteBuffer.wrap(fourBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        System.out.println("sub chunk 1 size: " + chunkSize);

        fileInputStream.read(twoBytes, 0, 2);
        int audioFormat = ByteBuffer.wrap(twoBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        System.out.println("audio format: " + audioFormat);

        fileInputStream.read(twoBytes, 0, 2);
        int numOfChannels = ByteBuffer.wrap(twoBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        System.out.println("number of channels: " + numOfChannels);

        fileInputStream.read(fourBytes, 0, 4);
        int sampleRate = ByteBuffer.wrap(fourBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        System.out.println("sample rate: " + sampleRate);

        fileInputStream.read(fourBytes, 0, 4);
        int byteRate = ByteBuffer.wrap(fourBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        System.out.println("byte rate: " + byteRate);

        fileInputStream.read(twoBytes, 0, 2);
        int blockAlign = ByteBuffer.wrap(twoBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        System.out.println("block align: " + blockAlign);

        fileInputStream.read(twoBytes, 0, 2);
        int bitsPerSample = ByteBuffer.wrap(twoBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        System.out.println("bits per sample: " + bitsPerSample);

        fileInputStream.read(fourBytes, 0, 4);
        System.out.println("sub chunk 2 id: " + "'" +new String(fourBytes) + "'");

        fileInputStream.read(fourBytes, 0, 4);
        chunkSize = ByteBuffer.wrap(fourBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        System.out.println("sub chunk 2 size: " + chunkSize);

        long numberOfSamples = 0;
        int printFrames = 20;
        int frameNum = 0;
        while( fileInputStream.read(twoBytes, 0, 2) != -1) {
            if (printFrames > 0) {
                System.out.println("Frame " + frameNum + ": " + ByteBuffer.wrap(twoBytes).order(ByteOrder.LITTLE_ENDIAN).getShort());
                printFrames--;
                frameNum++;
            }

            numberOfSamples++;
        }
        System.out.println("total number of samples: " + numberOfSamples);
        System.out.println("duration (in seconds): " + numberOfSamples / sampleRate);

        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
