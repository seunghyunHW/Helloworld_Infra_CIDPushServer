package com.hellowd.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-30
 * Time : 오후 6:12
 * To change this template use File | Settings | File and Code Templates.
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<String> {

    static Logger logger = LoggerFactory.getLogger(TcpServerHandler.class);

    private Map<String,String> reqData = new HashMap<>();

//    private ZookeeperResponse zookeeperResponse;
//
//    private ServerMonitor serverMonitor;
//
//
//    public TcpServerHandler(ZookeeperResponse zookeeperResponse){
//        this.zookeeperResponse = zookeeperResponse;
//        this.serverMonitor = zookeeperResponse.getServerMonitor();
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    //    serverMonitor.incrementAndGetConnections();

        logger.info("welcome to this server, your info {} " ,
                InetAddress.getLocalHost().getHostName() );
        ctx.write("welcome to this server (" + ctx.channel().remoteAddress().toString() + ")\r\n");
        ctx.write("it's " + new Date() + "\r\n");
        ctx.flush();


    }

    /**
     * <strong>Please keep in mind that this method will be renamed to
     * {@code messageReceived(ChannelHandlerContext, I)} in 5.0.</strong>
     * <p>
     * Is called for each message of type {@link }.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception is thrown if an error occurred
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        logger.info("msg = " + msg);

/*        RouteProtocol route = new RouteProtocol(msg);
        int recvedBytesLength = msg.getBytes().length;
        zookeeperResponse.findPushServer(route, recvedBytesLength);
        ctx.write(route.getResult());
        ctx.flush();*/
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        logger.info("close this server, bye bye ~~:D ");
    //    serverMonitor.decrementAndGetConnections();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
  //      serverMonitor.incrementAndGetErrors();
        //Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
