package com.hellowd.server.api.service;

import com.hellowd.server.ServerMonitor;
import com.hellowd.server.api.RestApiRequestTemplate;
import com.hellowd.server.api.exception.RequestParamException;
import com.hellowd.server.api.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-31
 * Time : 오후 4:55
 * To change this template use File | Settings | File and Code Templates.
 */
@Service("ServerMonitoring")
@Scope("prototype")
public class ServerMonitoring  extends RestApiRequestTemplate {

    @Autowired
    private ServerMonitor serverMonitor;

    public ServerMonitoring(Map<String,String> reqData){
        super(reqData);
    }

    @Override
    public void requestParamValidation() throws RequestParamException {
    }

    @Override
    public void service() throws ServiceException {

        this.apiResult.add("os", serverMonitor.getOsInfo());
        this.apiResult.add("memory", serverMonitor.getMemoryInfo());
        this.apiResult.addProperty("owners", serverMonitor.getOwners());
        this.apiResult.addProperty("cidWatchNumbers", serverMonitor.getCidWatchNumbers());
        this.apiResult.addProperty("uptime", serverMonitor.getUptime());
        this.apiResult.addProperty("requests", serverMonitor.getRequest());
        this.apiResult.addProperty("recvedBytes", serverMonitor.getReceivedBytes());
        this.apiResult.addProperty("sendBytes", serverMonitor.getSendBytes());
        this.apiResult.addProperty("errors", serverMonitor.getErrorCount());
        this.apiResult.addProperty("connections", serverMonitor.getConnectionCount());
        this.apiResult.addProperty("currentProcessTime", serverMonitor.getCurrentProcessTime());
        this.apiResult.addProperty("averageProcessTime", serverMonitor.getAverageProcessTime());
        this.apiResult.addProperty("cidWatcherLastSendHeartbeat", serverMonitor.getCidWatcherLastSendHeartbeatFormat());
        this.apiResult.addProperty("cidWatcherLastRecvHeartbeat", serverMonitor.getCidWatcherLastRecvHeartbeatFormat());

    }
}

