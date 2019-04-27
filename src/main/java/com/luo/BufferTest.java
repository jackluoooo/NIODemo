package com.luo;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * 阻塞式IO(javaSe普通Io)（Stream）
 *
 *
 *面向于缓冲区的非阻塞式IO（buffer）NIO 效率高、网络通信
 * 通道+缓冲区
 * Buffer
 * <li>ByteBuffer 用得最多</li>
 * <li>LongBuffer</li>
 * <li>InteigBuffer</li>
 * <li>DobboBuffer</li>
 */
public class BufferTest {
    @Test
    public  void test01(){
        // 1.指定缓冲区大小1024
        ByteBuffer buf = ByteBuffer.allocate(1024);
        System.out.println("--------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());
        // 2.向缓冲区存放5个数据
        buf.put("abcd1".getBytes());
        System.out.println("--------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());
        // 3.开启读模式
        buf.flip();
        System.out.println("----------开启读模式...----------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());
        byte[] bytes = new byte[buf.limit()];
        buf.get(bytes);
        System.out.println(new String(bytes, 0, bytes.length));
        System.out.println("----------重复读模式...----------");
        // 4.开启重复读模式
        buf.rewind();
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());
        byte[] bytes2 = new byte[buf.limit()];
        buf.get(bytes2);
        System.out.println(new String(bytes2, 0, bytes2.length));
        // 5.clean 清空缓冲区  数据依然存在,只不过数据被遗忘
        System.out.println("----------清空缓冲区...----------");
        buf.clear();
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());
        System.out.println((char)buf.get());

        // Invariants: mark <= position <= limit <= capacity
//        private int mark = -1;
//        private int position = 0;<缓冲区正在操作的位置>
//        private int limit;<缓冲区可以大小>
//        private int capacity;<缓冲区最大容量，一旦申明，不能改变>
        //put存放 get获取
        //清空缓冲区(名义上清空，下标还原)

    }

    /**
     * mark 与 rest区别
     */
    @Test
    public void test02(){
        ByteBuffer buf = ByteBuffer.allocate(1024);
        String str = "abcd1";
        buf.put(str.getBytes());
        // 开启读取模式
        buf.flip();
        byte[] dst = new byte[buf.limit()];
        buf.get(dst, 0, 2);
        buf.mark();
        System.out.println(new String(dst, 0, 2));
        System.out.println(buf.position());
        buf.get(dst, 2, 4);
        System.out.println(new String(dst, 2, 2));
        System.out.println(buf.position());
        buf.reset();
        System.out.println("重置恢复到mark位置..");
        System.out.println(buf.position());
    }

}
