package nia.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

  /**
   * ��channel��״̬��Ϊactiveʱ���õ�֪ͨ��
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
  }

  /**
   * ֻ����һ�������Ǳ���ʵ�ֵġ�
   * ���յ�����server����Ϣʱ�ᱻ���á� 
   * �ڴ˷������غ�SimpleChannelInboundHandler���ͷ���ByteBuf���������Ϣ��
   * ��һ���Serverʹ�õ�ChannelInboundHandlerAdapter��һ������ChannelInboundHandlerAdapter��channelRead����
   * �����󣬲������ͷš�ֻ����channelReadComplete�����е�writeAndFlush()�����ú�Ż��ͷš�
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }

}
