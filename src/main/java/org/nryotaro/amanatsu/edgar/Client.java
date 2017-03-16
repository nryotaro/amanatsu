package org.nryotaro.amanatsu.edgar;


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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class Client {
    @Value("${edgar.url.host}")
    private String host;


    private Channel channel ;
    public Client() {

    }

    public void prepare() throws InterruptedException, URISyntaxException, SSLException {

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        SslContext ctx =  SslContext.newClientContext(SslProvider.JDK, InsecureTrustManagerFactory.INSTANCE);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pl = ch.pipeline();

                pl.addLast(ctx.newHandler(  ch.alloc()));
                pl.addLast(new HttpClientCodec());
                // Remove the following line if you don't want automatic content decompression.
                pl.addLast(new HttpContentDecompressor());
                pl.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
                pl.addLast(new DownloadHandler());
            }
        });

        channel = bootstrap.connect(host,443).sync().channel();

    }

    public void request(URI uri) {
        HttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
        request.headers().set(HttpHeaderNames.HOST, uri.getHost());
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
        channel.writeAndFlush(request);
    }


    private void  sync () throws InterruptedException {
        channel.closeFuture().sync();
    }
}
