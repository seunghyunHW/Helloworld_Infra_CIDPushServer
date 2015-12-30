package com.hellowd.server.exception;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-30
 * Time : 오후 6:03
 * To change this template use File | Settings | File and Code Templates.
 */
public class NettyServerBindException extends Exception{

    private static final long serialVersionUID = -7925026253781179928L;

    private String errmsg;

    public NettyServerBindException(String errmsg) {
        this.errmsg = errmsg;
    }

}

