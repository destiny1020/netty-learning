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
    // EventLoopGroup�����ã��������ӣ�����inbound�Լ�outbound����
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      // ����ʹ�õ���NioSocketChannel������Serverʹ�õ�NioServerSocketChannel
      b.group(group).channel(NioSocketChannel.class)
          .remoteAddress(new InetSocketAddress(host, port))
          // ָ�������ӱ�������ʱ����Ҫ��ӵ�handler
          .handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast(new EchoClientHandler());
            }
          });
      // ���ӵ�Server���ȴ����ӽ������
      ChannelFuture f = b.connect().sync();
      // һֱ�ȴ���Channel�ر�
      f.channel().closeFuture().sync();
    } finally {
      // �ر��̳߳أ����ͷ�������Դ
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
