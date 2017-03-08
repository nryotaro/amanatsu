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

import java.net.URI;
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
                    pl.addLast("codec", new HttpClientCodec());
                    /*
                    pl.addLast("decoder", new HttpResponseDecoder());
                    pl.addLast("encoder", new HttpRequestEncoder());
                    */
                    pl.addLast(new TimeClientHandler());
                }
            });
            Channel f = b.connect("localhost", 8080).sync().channel(); // (5)

            URI url = new URI("http://localhost:8080/");
            HttpRequest postReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.GET, url.getRawPath());

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

class TimeClientHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            System.out.println(msg);
            ctx.close();
        } finally {
        //    m.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
