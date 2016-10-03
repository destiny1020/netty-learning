package nia.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

// ������ChannelHandler���Ա����Channels����
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf in = (ByteBuf) msg;
    // ʹ��ByteBuf��toString���������CharsetUtilת���ɿ�������ַ���
    System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
    // ��ʱֻ��д��������û��flush --- ʵ��ECHO�߼�
    ctx.write(in);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    // flush��������ʱ��Ϣ�Ż��������͵�remote peer��
    // ���ҹر�channel��ע������رյ��Ǻ�Client��ͨ��channel��
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    // ��������쳣��ر�channel
    ctx.close();
  }

}
