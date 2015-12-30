package com.hellowd.server.init;

import com.hellowd.server.handler.TcpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-30
 * Time : 오후 6:01
 * To change this template use File | Settings | File and Code Templates.
 */
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();
/*    private ZookeeperResponse zookeeperResponse;

    public TcpServerInitializer(ZookeeperResponse zookeeperResponse){
        this.zookeeperResponse = zookeeperResponse;
    }*/

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        //DelimiterBasedFrameDecoder는 구분자 기반의 패킷을 처리
        p.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        p.addLast(DECODER);
        p.addLast(ENCODER);
        p.addLast(new TcpServerHandler());  //zookeeperResponse
    }

}