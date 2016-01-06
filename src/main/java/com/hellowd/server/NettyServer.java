package com.hellowd.server;

import com.hellowd.server.exception.NettyServerBindException;
import com.hellowd.server.init.HttpServerInitializer;
import com.hellowd.server.init.TcpServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-29
 * Time : 오후 6:15
 * To change this template use File | Settings | File and Code Templates.
 */
@Component
public class NettyServer implements Watcher ,AsyncCallback.StatCallback {

    static Logger logger = LoggerFactory.getLogger(NettyServer.class);

/*
    //TODO: ownerBidiMapLock,appBidiMapLock이 어디에 쓰이는 건지?
    final Object ownerBidiMapLock = new Object();
    final Object appBidiMapLock = new Object();

    //TODO: 이건 지워도 될것 같은데... 아마도 채널 관련한 맵인듯
    private BidiMap ownerChannelBidiMap;
    private BidiMap appChannelBidiMap;

    //TODO : 이것도 어디에 쓰는지 체크 할 것.
    private Map<String,Integer> cidNotiMap;
    private ConcurrentHashMap<Long,Long> lastHeartbeat;
*/

    @Autowired
    @Qualifier("bossThreadCount")
    private int bossThreadCount;

    @Autowired
    @Qualifier("workerThreadCount")
    private int workerThreadCount;


    @Autowired
    @Qualifier("serviceName")
    private String serviceName;


    @Autowired
    @Qualifier("tcpPort")
    private int tcpPort;

    @Autowired
    @Qualifier("httpPort")
    private int httpPort;


    @Autowired
    @Qualifier("sessionTimeout")
    private int sessionTimeout;


    //TODO:CIDWatchClient를 추가합시다.
/*    private CIDWatchClient cidWatchClient;*/

    /**
     * ZooKeeper
     */
    private ZooKeeper zk;

    @Autowired
    @Qualifier("zookeeperHostPort")
    private String zookeeperHostPort;

    @Autowired
    @Qualifier("zookeeperSessionTimeout")
    private int zookeeperSessionTimeout;

    @Autowired
    @Qualifier("zookeeperZnode")
    private String znode;

    @Autowired
    @Qualifier("zookeeperZnodeData")
    private String znodeData;


    /**
     * JDBC Driver
     */
    @Autowired
    @Qualifier("databaseDriverClassName")
    private String driverName;

    @Autowired
    @Qualifier("databaseUrl")
    private String connectURI;

    @Autowired
    @Qualifier("databaseUname")
    private String uname;

    @Autowired
    @Qualifier("databasePasswd")
    private String passwd;



    //TODO:스케줄러에 대해서....
/*    private Scheduler scheduler;*/

    //TODO:JdbcConnectionPool 외 DAO 관련하여
/*
    private JdbcConnectionPool jdbcPool;
    private StoreDAO storeDAO;
    private StoreOwnerDAO storeOwnerDAO;
    private StoreOpenTrackingDAO storeOpenTrackingDAO;
*/


    public NettyServer(){
        //TODO:이넘의 역활을 찾아라.
 /*       lastHeartbeat = new ConcurrentHashMap<>();


        //TODO:이넘들의 역활을 찾아라.
        ownerChannelBidiMap = new TreeBidiMap();
        appChannelBidiMap	= new TreeBidiMap();
        cidNotiMap			= new ConcurrentHashMap<>();


        //TODO:스케줄러가 돌아가나보다.
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();


        jdbcPool = new JdbcConnectionPool();
        storeDAO = new StoreJdbcDAO();
        storeOwnerDAO = new StoreOwnerJdbcDAO();
        storeOpenTrackingDAO = new StoreOpenTrackingJdbcDAO();*/
    }


    public void start() {

        //TODO : JdbcPoll start 근데... 이렇게 직접 하는 것이 맞는건가?
        //jdbcPool.setupDriver(driverName, connectURI, uname,passwd);


        /*********************************************************
         * Zookeeper session start
         ********************************************************/
        try{
            if(!StringUtils.isEmpty(zookeeperHostPort))
                zk = new ZooKeeper(zookeeperHostPort,zookeeperSessionTimeout, this);

            logger.info(serviceName + "["+znode+", svc: "+tcpPort+", ctrl: "+httpPort+"] start.");

        }catch (IOException e) {
            logger.error(e.toString(),e);
        } catch (Exception e){
            logger.error("zookeeper 에러다 " + e.toString(),e);
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(bossThreadCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerThreadCount);

        try{

            /*********************************************************
             * tcp channel start
             ********************************************************/
            ServerBootstrap b2 = new ServerBootstrap();
            b2.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new TcpServerInitializer());  //zookeeperResponse
            Channel tcpChannel =b2.bind(tcpPort).sync().channel();

            /*********************************************************
             * http channel start
             ********************************************************/
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new HttpServerInitializer());
            Channel httpChannel = b.bind(httpPort).sync().channel();

            /**
             * double port channel
             */
            tcpChannel.closeFuture().sync();
            httpChannel.closeFuture().sync();

            if(!tcpChannel.isActive()) {
                throw new NettyServerBindException("Exception : Ctrl Port(httpPort) : " + httpPort + " is not active.");
            }

            if(!httpChannel.isActive()) {
                throw new NettyServerBindException("Exception : Service Port(tcpPort) : " + tcpPort + " is not active.");
            }

        }catch (InterruptedException | NettyServerBindException e){
            logger.info("Netty Server Binding Exception 발생 되었습니다.");
            logger.error(e.toString(),e);
        } finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

            logger.info("셧다운 되었습니다.");
        }

        /**
         * CID Watcher Client Start!!!
         */
        //TODO: CID Watcher Client  Check!!!
        //cidWatchClient.run();

        /**
         * Scheduler
         */
        //TODO: Scheduler Check!!!
        /*scheduler.start();

        JobDetail trackingJob = new JobDetail("trackingJob" , "TRK" , StoreOpenTacker.class);
        trackingJob.getJobDataMap().put("pushServer",this);
        CronTrigger trackingTrigger = new CronTrigger("trackingTrigger" , "TRK" , "0 0 * * * ?");
        scheduler.scheduleJob(trackingJob, trackingTrigger);

        JobDetail deadSessionClearJob = new JobDetail("deadSessionClearJob" , "TRK" , DeadSessionClear.class);
        deadSessionClearJob.getJobDataMap().put("pushServer" , this);
        CronTrigger deadSessionClearTrigger = new CronTrigger("deadSessionClearTrigger","TRK" , "0/10 * * * * ?");
        scheduler.scheduleJob(deadSessionClearJob, deadSessionClearTrigger);

        JobDetail cidwatchClientHealthCheckJob = new JobDetail("cidwatchClientHealthCheckJob", "TRK", CIDWatchClientHealthCheck.class);
        cidwatchClientHealthCheckJob.getJobDataMap().put("cidwatchClient", cidwatchClient);
        CronTrigger cidwatchClientHealthCheckTrigger = new CronTrigger("cidwatchClientHealthCheckTrigger", "TRK", "0/15 * * * * ?");
        scheduler.scheduleJob(cidwatchClientHealthCheckJob, cidwatchClientHealthCheckTrigger);*/


    }


    /**
     * Zookeeper 왜 넣는 건가?
     * lookup 서버를 별도로 만들어 놓고, 클라이언트에서는 해당 lookup을 보고 이 Push 서버로 접속을 하는건데..
     * zookeeper 내에 이 Push Server의 상태 값이 있는지 여부를 확인하고,
     * 만약 이 Push Server가 없다면...
     * zk.exists를 호출해서 다시 찾는 거죠...
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        logger.info("Watcher Process가 시작되었습니다.");

        if(watchedEvent.getType() == Event.EventType.None){
            Event.KeeperState state = watchedEvent.getState();

            if(state == Event.KeeperState.SyncConnected){
                logger.info("주키퍼 세션이 (재)연결 되었습니다.");

                //
                zk.exists("/S3P",false,this,null);

            }else if(state == Event.KeeperState.Disconnected){
                logger.warn("연결이 끊겼네요... 연결이 될때까지 계속 연결 시도를 하겠습니다.");
            }else if(state == Event.KeeperState.Expired){
                logger.warn("주키퍼 세션이 만료되었습니다. 이 서버는 세션을 재생성 할것입니다.");

                try{
                    if(zk != null)
                        zk.close();
                    if(!StringUtils.isEmpty(zookeeperHostPort))
                        zk = new ZooKeeper(zookeeperHostPort,zookeeperSessionTimeout, this);
                }catch (IOException | InterruptedException e){
                    logger.error(e.toString(), e);
                }
            }
        }


    }

    /**
     * 다시 찾는데.. Zookeeper상에 NODE가 없으면, 생성을 요청을 합니다.
     * 그리고 또 찾는데... 또 /S3P/P1 을 검색하는데... 없으면...생성을 요청합니다.
     * 테스트를 해보면 되겠죠.
     *
     * @param rc
     * @param path
     * @param ctx
     * @param stat
     */
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        logger.info("AsyncCallback.StatCallback의 processResult가 시작되었습니다.");
        KeeperException.Code code = KeeperException.Code.get(rc);

        if(code == KeeperException.Code.NONODE){
            try{
                zk.create("/S3P", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }catch (Exception e){
                logger.error(e.toString(),e);
            }
        }

        try{
            if(zk.exists(znode, false) == null){
                zk.create(znode, znodeData.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
            }
        }catch (Exception e){
            logger.error(e.toString(),e);
        }
    }
}
