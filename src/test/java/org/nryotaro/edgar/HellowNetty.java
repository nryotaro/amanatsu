package org.nryotaro.edgar;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HellowNetty {

    @Test
    public void test() throws Exception {

        ExecutorService exec = Executors.newSingleThreadExecutor();

        Future<String> str2 = exec.submit(() -> "drop");
        Future<String> str =
                exec.submit(() -> "hoge");


        String bar = "render";
        String s = str.get() + bar + str2.get();
        System.out.print("");
    }

    @Test
    public void helloTest() throws Exception {
        EventLoopGroup workerGroup  = new NioEventLoopGroup();
        try {
            Bootstrap b  = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pl = ch.pipeline();
                    pl.addLast("decoder", new HttpResponseDecoder());
                    pl.addLast("encoder", new HttpRequestEncoder());
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });
            Channel f = b.connect("localhost", 8080).sync().channel(); // (5)

            HttpRequest postReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.GET, "localhost");
            /*
            postReq.headers().set(HttpHeaders.Names.HOST, "localhost");
            postReq.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
            postReq.headers().set(HttpHeaders.Names.CONTENT_TYPE,"application/x-www-form-urlencoded");
            */
            f.writeAndFlush(postReq);
            f.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }
}
/*
@ChannelHandler.Sharable
public class EchoClientHandler extends
        SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",
                CharsetUtil.UTF_8);
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        System.out.println(
                "Client received: " + in.toString(CharsetUtil.UTF_8));
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

    }
}
*/

class TimeClientHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg; // (1)
        try {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        } finally {
            m.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
