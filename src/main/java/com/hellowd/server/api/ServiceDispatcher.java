package com.hellowd.server.api;

import com.hellowd.server.handler.HttpServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-31
 * Time : 오후 4:54
 * To change this template use File | Settings | File and Code Templates.
 */
@Component
public class ServiceDispatcher {

    static Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    private static ApplicationContext springContext;

    @Autowired
    public void init(ApplicationContext springContext){
        ServiceDispatcher.springContext = springContext;
    }

    public static RestApiRequest dispatch(Map<String,String> requestMap){
        String serviceUri = requestMap.get("REQUEST_URI");
        String httpMethod = requestMap.get("REQUEST_METHOD");
        String beanName;

        if(serviceUri == null || !httpMethod.equals("GET")){
            beanName = "BadRequest";
        }else{
            if(serviceUri.startsWith("/manager/status")){

                /**
                 * TODO: 1.owners 가 포함된 파라미터가 있으면...ownerSeqs = pushServer.getOwnerSeqs();
                 *
                 if( params.contains("owners") ) {
                    long[] ownerSeqs = pushServer.getOwnerSeqs();

                    if( ownerSeqs != null && ownerSeqs.length > 0 ) {
                        StringBuffer sb = new StringBuffer();
                        for(int i=0; i<ownerSeqs.length; i++) {
                            sb.append(ownerSeqs[i]);
                            if( i < ownerSeqs.length-1 )
                                sb.append(",");
                        }
                        owners = sb.toString();
                    }
                }
                 ***/


                /**
                 * TODO : .cidWacths 가 있으면 pushServer.getCidWatchNumbers();
                 * String cidWatchNumbers = pushServer.getCidWatchNumbers();
                 *
                 * TODO : 그리고 난 뒤에 두개의 값을 추가 하면 끝.
                 if( StringUtils.isNotEmpty(ownerSeqs) ) {
                 sb.append(",\"owners\":\"");
                 sb.append(ownerSeqs);
                 sb.append("\"");
                 }
                 if( StringUtils.isNotEmpty(cidWatchNumbers) ) {
                 sb.append(",\"cidWatchNumbers\":\"");
                 sb.append(cidWatchNumbers);
                 sb.append("\"");
                 }
                 */


                beanName = "ServerMonitoring";
            }else if(serviceUri.startsWith("/manager/healthcheck")){
                beanName = "HealthCheck";
            }else{
                beanName = "NotFoundRequest";
            }
        }

        RestApiRequest restApiRequest;
        try{
            restApiRequest = (RestApiRequest)springContext.getBean(beanName,requestMap);
        }catch (Exception e){
            logger.error(e.toString(),e);
            restApiRequest = (RestApiRequest)springContext.getBean("BadRequest",requestMap);
        }
        return restApiRequest;
    }
}
