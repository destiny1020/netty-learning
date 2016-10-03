package nia.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

// 表明此ChannelHandler可以被多个Channels共享
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf in = (ByteBuf) msg;
    // 使用ByteBuf的toString方法，结合CharsetUtil转换成可输出的字符串
    System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
    // 此时只是写操作，并没有flush --- 实现ECHO逻辑
    ctx.write(in);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    // flush操作，此时消息才会真正传送到remote peer。
    // 并且关闭channel，注意这里关闭的是和Client连通的channel。
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    // 如果发生异常会关闭channel
    ctx.close();
  }

}
