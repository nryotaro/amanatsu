package org.nryotaro.amanatsu.edgar;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;

import static java.lang.System.*;


// TODO to be implemented
public class DownloadHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        FullHttpResponse httpResp = (FullHttpResponse) msg;
        Headers headers =  httpResp.headers();

        out.println(httpResp.content().toString(CharsetUtil.UTF_8));
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
