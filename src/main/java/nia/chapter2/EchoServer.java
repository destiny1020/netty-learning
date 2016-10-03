package nia.chapter2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

  private final int port;

  public EchoServer(int port) {
    this.port = port;
  }

  public static void main(String[] args) throws InterruptedException {
    int port = 9999;
    if (args.length != 1) {
      System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
    } else {
      port = Integer.parseInt(args[0]);
    }

    new EchoServer(port).start();
  }

  public void start() throws InterruptedException {
    final EchoServerHandler serverHandler = new EchoServerHandler();
    // 创建EventLoopGroup
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      // 创建ServerBootstrap
      ServerBootstrap b = new ServerBootstrap();
      // 声明使用NIO transport channel
      b.group(group).channel(NioServerSocketChannel.class)
      // 使用指定的port
          .localAddress(new InetSocketAddress(port))
          // 添加指定的handler到ChannelPipeline
          // 当新连接被建立的时候，一个新的子Channel会被创建，ChannelInitializer的功能就是初始化这个新的子Channel
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast(serverHandler);
            }
          });
      // sync方法会一直等待，直到bind完成
      ChannelFuture f = b.bind().sync();
      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }
  }
}
