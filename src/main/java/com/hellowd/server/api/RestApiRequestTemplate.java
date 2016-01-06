package com.hellowd.server.api;

import com.google.gson.JsonObject;
import com.hellowd.server.api.exception.RequestParamException;
import com.hellowd.server.api.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-31
 * Time : 오후 4:53
 * To change this template use File | Settings | File and Code Templates.
 */
public abstract class RestApiRequestTemplate implements RestApiRequest {

    protected Logger logger;

    protected Map<String,String> reqData;

    protected JsonObject apiResult;

    public RestApiRequestTemplate(Map<String,String> reqData){
        this.logger = LoggerFactory.getLogger(RestApiRequestTemplate.class);
        this.apiResult = new JsonObject();
        this.reqData = reqData;
    }


    public void executeService(){
        try{
            this.requestParamValidation();
            this.service();
        }catch (RequestParamException e){
            logger.error(e.toString(),e);
            this.apiResult.addProperty("resultCode" , "405");
        }catch(ServiceException e){
            logger.error(e.toString(),e);
            this.apiResult.addProperty("resultCode" , "501");
        }
    }

    public JsonObject getApiResult(){
        return this.apiResult;
    }

}
