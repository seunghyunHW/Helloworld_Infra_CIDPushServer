package com.hellowd.server.handler;

import com.google.gson.JsonObject;
import com.hellowd.server.api.RestApiRequest;
import com.hellowd.server.api.ServiceDispatcher;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-31
 * Time : 오후 4:50
 * To change this template use File | Settings | File and Code Templates.
 */

@ChannelHandler.Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpMessage> {

    static Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    private HttpRequest request;

    private Map<String,String> reqData = new HashMap<>();

    private JsonObject apiResult;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
        //Request header
        if(msg instanceof HttpRequest){
            this.request = (HttpRequest)msg;
            //헤더에서 값을 추출한다.
            reqData.put("REQUEST_URI",request.getUri());
            reqData.put("REQUEST_METHOD",request.getMethod().name());
            reqData.put("REQUEST_ALL", request.toString());
            RestApiRequest restApiRequest = ServiceDispatcher.dispatch(reqData);
            try{
                restApiRequest.executeService();
                apiResult = restApiRequest.getApiResult();
            }finally {
                reqData.clear();
            }

            if(!writeResponse(ctx,msg)){
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                        .addListener(ChannelFutureListener.CLOSE);
            }
            reset();
        }
    }

    private boolean writeResponse(ChannelHandlerContext ctx, HttpObject currentObj) {

        boolean keepAlive = HttpHeaders.isKeepAlive(request);

        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                currentObj.getDecoderResult().isSuccess() ? OK : BAD_REQUEST,
                Unpooled.copiedBuffer(apiResult.toString(), CharsetUtil.UTF_8));

        response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // -
            // http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        // Write the response.
        ctx.write(response);

        long a = System.currentTimeMillis();
        Instant start = Instant.now();
        Instant end = Instant.now();
        Duration elapsed = Duration.between(start,end);
        long b = elapsed.toMillis();

        elapsed.plus(2L, ChronoUnit.SECONDS);



        return keepAlive;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("요청 처리 완료");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.toString());
        ctx.close();
    }

    private void reset() {
        request = null;
    }


}
