package org.nryotaro.edgar;


import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
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
                    pl.addLast(new HttpClientCodec());
                    // Remove the following line if you don't want automatic content decompression.
                    pl.addLast(new HttpContentDecompressor());
                    pl.addLast(new HttpObjectAggregator(1048576));
                    pl.addLast(new TimeClientHandler());


                }
            });

            URI uri = new URI("http://localhost:8080");

            Channel f = b.connect(uri.getHost(),uri.getPort()).sync().channel(); // (5)

            // Prepare the HTTP request.
            HttpRequest request = new DefaultFullHttpRequest(
            HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
            request.headers().set(HttpHeaderNames.HOST, uri.getHost());
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

            f.writeAndFlush(request);
            f.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }
}

class TimeClientHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if( msg instanceof DefaultHttpResponse) {
            DefaultHttpResponse resp = (DefaultHttpResponse) msg;
        } else if (msg instanceof  DefaultLastHttpContent ) {
            DefaultLastHttpContent resp =  ((DefaultLastHttpContent) msg);
            ByteBuf buf = resp.content();
            System.out.println(buf.toString(CharsetUtil.UTF_8));

        }

        System.out.println(msg);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
