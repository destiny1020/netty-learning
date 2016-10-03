package nia.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {

  private final String host;
  private final int port;

  public EchoClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void start() throws InterruptedException {
    // EventLoopGroup的作用：创建连接，处理inbound以及outbound数据
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      // 这里使用的是NioSocketChannel，而非Server使用的NioServerSocketChannel
      b.group(group).channel(NioSocketChannel.class)
          .remoteAddress(new InetSocketAddress(host, port))
          // 指定当连接被创建的时候需要添加的handler
          .handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast(new EchoClientHandler());
            }
          });
      // 连接到Server并等待连接建立完毕
      ChannelFuture f = b.connect().sync();
      // 一直等待到Channel关闭
      f.channel().closeFuture().sync();
    } finally {
      // 关闭线程池，并释放所有资源
      group.shutdownGracefully().sync();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    int port = 9999;
    String host = "localhost";
    if (args.length != 2) {
      System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
    } else {
      host = args[0];
      port = Integer.parseInt(args[1]);
    }

    new EchoClient(host, port).start();
  }
}
