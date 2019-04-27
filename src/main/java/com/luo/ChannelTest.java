package com.luo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ChannelTest {

    // 1.利用通道完成文件的复制(非直接缓冲区)
    static public void test1() throws IOException { // 4400
        long start = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream("f://1.mp4");
        FileOutputStream fos = new FileOutputStream("f://2.mp4");
        // ①获取通道
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        // ②分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        while (inChannel.read(buf) != -1) {
            buf.flip();// 切换为读取数据
            // ③将缓冲区中的数据写入通道中
            outChannel.write(buf);
            buf.clear();
        }
        outChannel.close();
        inChannel.close();
        fos.close();
        fis.close();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    // 使用直接缓冲区完成文件的复制(内存映射文件)
    @Deprecated
    static public void test2() throws IOException {
        long start = System.currentTimeMillis();
        FileChannel inChannel = FileChannel.open(Paths.get("f://1.mp4"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("f://2.mp4"), StandardOpenOption.WRITE,
                StandardOpenOption.READ, StandardOpenOption.CREATE);
        // 内存映射文件
        MappedByteBuffer inMappedByteBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappedByteBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
        // 直接对缓冲区进行数据的读写操作
        byte[] dsf = new byte[inMappedByteBuf.limit()];
        inMappedByteBuf.get(dsf);
        outMappedByteBuffer.put(dsf);
        inChannel.close();
        outChannel.close();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}