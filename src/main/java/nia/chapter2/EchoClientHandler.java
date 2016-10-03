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
   * 当channel的状态变为active时，得到通知。
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
  }

  /**
   * 只有这一个方法是必须实现的。
   * 当收到来自server的消息时会被调用。 
   * 在此方法返回后，SimpleChannelInboundHandler会释放由ByteBuf所代表的消息。
   * 这一点和Server使用的ChannelInboundHandlerAdapter不一样，在ChannelInboundHandlerAdapter的channelRead方法
   * 结束后，并不会释放。只有在channelReadComplete方法中的writeAndFlush()被调用后才会释放。
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
