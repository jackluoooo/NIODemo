package com.luo.pacage;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

class ServerHandler extends ChannelHandlerAdapter {
    /**
     * 当通道被调用,执行该方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收数据
        String value = (String) msg;
        System.out.println("Server msg:" + value);
        // 回复给客户端 “您好!”
        String res = "好的...";
        ctx.writeAndFlush(Unpooled.copiedBuffer(res.getBytes()));
    }

}
public class netty5Server {
    public static void main(String[] args) throws Exception{
        System.out.println("服务器端已经启动....");
        // 1.创建2个线程,一个负责接收客户端连接， 一个负责进行 传输数据
        NioEventLoopGroup pGroup = new NioEventLoopGroup();
        NioEventLoopGroup cGroup = new NioEventLoopGroup();
        // 2. 创建服务器辅助类
        ServerBootstrap b = new ServerBootstrap();
        b.group(pGroup, cGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                // 3.设置缓冲区与发送区大小
                .option(ChannelOption.SO_SNDBUF, 32 * 1024).option(ChannelOption.SO_RCVBUF, 32 * 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new StringDecoder());
                        sc.pipeline().addLast(new ServerHandler());
                    }
                });
        ChannelFuture cf = b.bind(8080).sync();
        cf.channel().closeFuture().sync();
        pGroup.shutdownGracefully();
        cGroup.shutdownGracefully();

    }
}

class ClientHandler extends ChannelHandlerAdapter {

    /**
     * 当通道被调用,执行该方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收数据
        String value = (String) msg;
        System.out.println("client msg:" + value);
    }


}

class netty5Client {
    public static void main(String[] args) throws Exception{
        System.out.println("客户端已经启动....");
        // 创建负责接收客户端连接
        NioEventLoopGroup pGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(pGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
                sc.pipeline().addLast(new StringDecoder());
                sc.pipeline().addLast(new ClientHandler());
            }
        });
        ChannelFuture cf = b.connect("127.0.0.1", 8080).sync();
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("itmayiedu".getBytes()));
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("itmayiedu".getBytes()));
        Thread.sleep(1000);
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("itmayiedu".getBytes()));
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("itmayiedu".getBytes()));
        /**
         * 解决粘包的办法（拆包）
         * 1.消息定长（不可取）
         * 2.包尾加分隔符
         * 将消息分为消息头和消息体
         *
         */



        // 等待客户端端口号关闭
        cf.channel().closeFuture().sync();
        pGroup.shutdownGracefully();

    }

    }



