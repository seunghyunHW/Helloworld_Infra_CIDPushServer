package com.hellowd.server;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-30
 * Time : 오전 11:28
 * To change this template use File | Settings | File and Code Templates.
 */
@Component
public class ServerMonitor {

    static Logger logger = LoggerFactory.getLogger(ServerMonitor.class);

    private Runtime runtime = Runtime.getRuntime();

    static int mb = 1024 * 1024;

    private long startTime;
    private AtomicLong requests;
    private AtomicLong errors;
    private AtomicLong connections;
    private AtomicLong receivedBytes;
    private AtomicLong sendBytes;
    private AtomicLong currentProcessTime;
    private AtomicLong totalProcessTime;

    private String osName;
    private String osVersion;
    private String osArch;

    //Lookup과 다르게 추가 된것
    private Date cidWatcherLastSendHeartbeat;
    private Date cidWatcherLastRecvHeartbeat;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String owners;
    private AtomicLong cidWatchNumbers;

    public ServerMonitor() {
        this(new Date().getTime());
        logger.info("서버 모니터링 시작");
    }

    public ServerMonitor(long startTime) {
        this.startTime = startTime;
        this.requests = new AtomicLong(0);
        this.requests = new AtomicLong(0);
        this.errors = new AtomicLong(0);
        this.connections = new AtomicLong(0);
        this.receivedBytes = new AtomicLong(0);
        this.sendBytes = new AtomicLong(0);
        this.currentProcessTime = new AtomicLong(0);
        this.totalProcessTime = new AtomicLong(0);

        this.osName = System.getProperty("os.name");
        this.osVersion = System.getProperty("os.version");
        this.osArch = System.getProperty("os.arch");

        this.owners="";
        this.cidWatchNumbers = new AtomicLong(0);

        this.cidWatcherLastSendHeartbeat = new Date();
        this.cidWatcherLastRecvHeartbeat = new Date();
    }

    public String getOsArch() {
        return osArch;
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public JsonObject getOsInfo() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", this.getOsName());
        obj.addProperty("version", this.getOsVersion());
        obj.addProperty("arch", this.getOsArch());
        obj.addProperty("processors", runtime.availableProcessors());
        return obj;
    }

    public JsonObject getMemoryInfo() {
        JsonObject obj = new JsonObject();

        long maxmem = runtime.maxMemory() / mb;
        long totalmem = runtime.totalMemory() / mb;
        long freemem = runtime.freeMemory() / mb;
        long usagemem = totalmem - freemem;

        obj.addProperty("maxmem", maxmem);
        obj.addProperty("totalmem", totalmem);
        obj.addProperty("freemem", freemem);
        obj.addProperty("usagemem", usagemem);
        return obj;
    }



    public long getUptime() {
        return getUptime(Calendar.SECOND);
    }

    public long getUptime(int unit) {
        if( unit == Calendar.SECOND ) 	 return (new Date().getTime() - startTime)/1000;
        if( unit == Calendar.MILLISECOND ) return (new Date().getTime() - startTime);
        return (new Date().getTime() - startTime)/1000;
    }

    public long getRequest() {
        return requests.get();
    }

    public long getReceivedBytes() {
        return receivedBytes.get();
    }

    public long getErrorCount() {
        return errors.get();
    }

    public long getConnectionCount() {
        return connections.get();
    }

    public long getCurrentProcessTime() {
        return (currentProcessTime.get() /1000);
    }

    public long getAverageProcessTime() {
        return (averageProcessTime() /1000);
    }

    public long averageRequests() {
        return requests.get()/getUptime();
    }

    public long averageErrors() {
        return errors.get()/getUptime();
    }

    public long averageConnections() {
        return connections.get()/getUptime();
    }

    public long averageRecvedBytes() {
        return receivedBytes.get()/getUptime();
    }

    public long averageSendBytes() {
        return sendBytes.get()/getUptime();
    }

    public long averageProcessTime() {
        return (requests.get()!=0)?totalProcessTime.get()/requests.get():0;
    }

    public long incrementAndGetRequests() {
        return requests.incrementAndGet();
    }

    public long getRequests() {
        return requests.get();
    }

    public long incrementAndGetErrors() {
        return errors.incrementAndGet();
    }

    public long getErrors() {
        return errors.get();
    }

    public long incrementAndGetConnections() {
        return connections.incrementAndGet();
    }
    public long decrementAndGetConnections() {
        return connections.decrementAndGet();
    }

    public long getConnections() {
        return connections.get();
    }

    public long addAndGetRecvedBytes(long bytes) {
        return receivedBytes.addAndGet(bytes);
    }

    public long getRecvedBytes() {
        return receivedBytes.get();
    }

    public long addAndGetSendBytes(long bytes) {
        return sendBytes.addAndGet(bytes);
    }

    public long getSendBytes() {
        return sendBytes.get();
    }

    public void setCurrentProcessTime(long processTime) {
        currentProcessTime.set(processTime);
    }

    public long addAndGetTotalProcessTime(long processTime) {
        return totalProcessTime.addAndGet(processTime);
    }

    //Lookup과 다르게 추가 된것
    public Date getCidWatcherLastSendHeartbeat() {
        return cidWatcherLastSendHeartbeat;
    }

    public void setCidWatcherLastSendHeartbeat(Date cidWatcherLastSendHeartbeat) {
        this.cidWatcherLastSendHeartbeat = cidWatcherLastSendHeartbeat;
    }

    public Date getCidWatcherLastRecvHeartbeat() {
        return cidWatcherLastRecvHeartbeat;
    }

    public void setCidWatcherLastRecvHeartbeat(Date cidWatcherLastRecvHeartbeat) {
        this.cidWatcherLastRecvHeartbeat = cidWatcherLastRecvHeartbeat;
    }


    public String getCidWatcherLastSendHeartbeatFormat(){
        return sdf.format(cidWatcherLastSendHeartbeat);
    }

    public String getCidWatcherLastRecvHeartbeatFormat(){
        return sdf.format(cidWatcherLastRecvHeartbeat);
    }

    public String getOwners(){
        return owners;
    }

    public long getCidWatchNumbers(){
        return cidWatchNumbers.get();
    }


    @Override
    public String toString() {
        return "{'os':" +
                getOsInfo() +
                ",'memory':" +
                getMemoryInfo() +
                ",'owners':" +
                getOwners() +
                ",'cidWatchNumbers':" +
                getCidWatchNumbers() +
                ",'uptime':" +
                getUptime() +
                ",'requests':" +
                getRequest() +
                ",'recvedBytes':" +
                receivedBytes.get() +
                ",'sendBytes':" +
                sendBytes.get() +
                ",'errors':" +
                errors.get() +
                ",'connections':" +
                connections.get() +
                ",'currentProcessTime':" +
                ((float) currentProcessTime.get() / 1000) +
                ",'averageProcessTime':" +
                ((float) averageProcessTime() / 1000) +
                ",'cidWatcherLastSendHeartbeat':" +
                getCidWatcherLastSendHeartbeatFormat()+
                ",'cidWatcherLastRecvHeartbeat':" +
                getCidWatcherLastRecvHeartbeatFormat() +
                "}";
    }

}

