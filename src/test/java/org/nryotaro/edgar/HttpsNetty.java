package org.nryotaro.edgar;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.junit.Test;

import java.net.URI;

public class HttpsNetty {


    @Test
    public void helloHttps() throws Exception {

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            String path = System.getProperty("java.library.path");
            SslContext ctx =  SslContext.newClientContext(SslProvider.JDK, InsecureTrustManagerFactory.INSTANCE);
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);

            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pl = ch.pipeline();

                    pl.addLast(ctx.newHandler(  ch.alloc()));
                    pl.addLast(new HttpClientCodec());
                    // Remove the following line if you don't want automatic content decompression.
                    pl.addLast(new HttpContentDecompressor());
                    pl.addLast(new HttpObjectAggregator(1048576));
                    pl.addLast(new TimeClientHandler());
                }
            });
            URI uri =  new URI("https://www.sec.gov/Archives/edgar/daily-index/2017/QTR1/crawler.20170310.idx");


            Channel f = b.connect(uri.getHost(),443).sync().channel(); // (5)

            // Prepare the HTTP request.
            HttpRequest request = new DefaultFullHttpRequest(
            HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
            request.headers().set(HttpHeaderNames.HOST, uri.getHost());
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

            f.writeAndFlush(request);
            f.closeFuture().sync();

        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}
