package ru;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Pract2 {

    public Pract2() {
        fun2X();
    }

    private void fun1()
    {
        try (FileChannel inChannel = new FileInputStream("../pract2/test.txt").getChannel()) {
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            buffer.load();
            System.out.println(StandardCharsets.UTF_8.decode(buffer));
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fun2()
    {
        int i, sum = 0;
        long elapsed;
        Instant start, finish;
        for(i = 0; i < 3; i++) {
            start = Instant.now();
            fun2_1();
            finish = Instant.now();
            elapsed = Duration.between(start, finish).toMillis();
            sum += elapsed;
            System.out.println("fun2_1 Прошло времени №" + (i + 1) + ", мс: " + elapsed);
        }
        System.out.println("fun2_1 Прошло времени в среднем, мс: " + (sum/3));
        sum = 0;
        for(i = 0; i < 3; i++) {
            start = Instant.now();
            fun2_2();
            finish = Instant.now();
            elapsed = Duration.between(start, finish).toMillis();
            sum += elapsed;
            System.out.println("fun2_2 Прошло времени №" + (i + 1) + ", мс: " + elapsed);
        }
        System.out.println("fun2_2 Прошло времени в среднем, мс: " + (sum/3));
        sum = 0;
        File f = new File("../pract2/test1002mb.txt");
        if(f.exists()) f.delete();
        for(i = 0; i < 3; i++) {
            start = Instant.now();
            fun2_3();
            finish = Instant.now();
            elapsed = Duration.between(start, finish).toMillis();
            f = new File("../pract2/test1002mb.txt");
            if(f.exists()) f.delete();
            sum += elapsed;
            System.out.println("fun2_3 Прошло времени №" + (i + 1) + ", мс: " + elapsed);
        }
        System.out.println("fun2_3 Прошло времени в среднем, мс: " + (sum/3));
        f = new File("../pract2/test1002mb.txt");
        if(f.exists()) f.delete();
        sum = 0;
        for(i = 0; i < 3; i++) {
            start = Instant.now();
            fun2_4();
            finish = Instant.now();
            elapsed = Duration.between(start, finish).toMillis();
            f = new File("../pract2/test1002mb.txt");
            if(f.exists()) f.delete();
            sum += elapsed;
            System.out.println("fun2_4 Прошло времени №" + (i + 1) + ", мс: " + elapsed);
        }
        System.out.println("fun2_4 Прошло времени в среднем, мс: " + (sum/3));
    }

    private void fun2X()
    {
        int i, sum = 0;
        long elapsed;
        Instant start, finish;
        for(i = 0; i < 3; i++) {
            start = Instant.now();
            fun2X_1();
            finish = Instant.now();
            elapsed = Duration.between(start, finish).toMillis();
            sum += elapsed;
            System.out.println("fun2X_1 Прошло времени №" + (i + 1) + ", мс: " + elapsed);
        }
        System.out.println("fun2X_1 Прошло времени в среднем, мс: " + (sum/3));
        sum = 0;
        for(i = 0; i < 3; i++) {
            start = Instant.now();
            fun2X_2();
            finish = Instant.now();
            elapsed = Duration.between(start, finish).toMillis();
            sum += elapsed;
            System.out.println("fun2X_2 Прошло времени №" + (i + 1) + ", мс: " + elapsed);
        }
        System.out.println("fun2X_2 Прошло времени в среднем, мс: " + (sum/3));
        sum = 0;
        for(i = 0; i < 3; i++) {
            start = Instant.now();
            fun2X_4();
            finish = Instant.now();
            elapsed = Duration.between(start, finish).toMillis();
            sum += elapsed;
            System.out.println("fun2X_4 Прошло времени №" + (i + 1) + ", мс: " + elapsed);
        }
        System.out.println("fun2X_4 Прошло времени в среднем, мс: " + (sum/3));
    }

    private void fun2_1()
    {
        try(InputStream inputStream = new FileInputStream("../pract2/test100mb.txt");
            OutputStream outputStream = new FileOutputStream("../pract2/test1001mb.txt")){

            byte[] buffer = new byte[1024];
            int length;

            while((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fun2X_1()
    {
        try(InputStream inputStream = new FileInputStream("../pract2/test100mb.txt")){

            byte[] buffer = new byte[1024];
            int length;

            while((length = inputStream.read(buffer)) > 0){
                String par = new String(buffer, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fun2_2()
    {
        try(FileInputStream inFile = new FileInputStream("../pract2/test100mb.txt");
            RandomAccessFile outFile = new RandomAccessFile("../pract2/test1002mb.txt", "rw")) {
            FileChannel inChannel = inFile.getChannel();
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            outFile.getChannel().write(buffer);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fun2X_2()
    {
        try (FileChannel inChannel = new FileInputStream("../pract2/test100mb.txt").getChannel()) {
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            buffer.load();
            String par = String.valueOf(StandardCharsets.UTF_8.decode(buffer));
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fun2_3()
    {
        try {
            FileUtils.copyFile(new File("../pract2/test100mb.txt"), new File("../pract2/test1002mb.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fun2_4()
    {
        try {
            Files.copy(Paths.get("../pract2/test100mb.txt"), Paths.get("../pract2/test1002mb.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fun2X_4()
    {
        try {
            String par = Files.readString(Paths.get("../pract2/test100mb.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fun3()
    {
        File f = new File("../pract2/post3.txt");
        try {
            sum(f);
        } catch (IOException e) {
            System.err.println(f + ": " + e);
        }
    }

    /**
     * CRC-CCITT (0xFFFF)
     * CRC16_CCITT_FALSE: Polynomial x16+x12+x5+1 (0x1021), initial value 0xFFFF, low bit is after, high bit is first, result is exclusive to 0x0000
     *
     * @return
     */
    private static int sum(ByteBuffer bb) {
        int crc = 0xFFFF;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (12, 5, 0)        x^16 + x^12 + x^5 + x^0
        byte b;
        while (bb.hasRemaining()) {
            b = bb.get();
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        return crc & 0x0000;
    }

    // Compute and print a checksum for the given file

    private static void sum(File f) throws IOException {
        // Open the file and then get a channel from the stream
        try (FileInputStream fis = new FileInputStream(f);
            FileChannel fc = fis.getChannel()) {

            // Get the file's size and then map it into memory
            int sz = (int) fc.size();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);

            // Compute and print the checksum
            int sum = sum(bb);
            int kb = (sz + 1023) / 1024;
            String s = Integer.toString(sum);
            System.out.println("sum: " + s + "\t KB:" + kb + "\t file: " + f);
        }
    }
}
