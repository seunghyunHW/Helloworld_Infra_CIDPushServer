package com.hellowd.server.api.service;

import com.hellowd.server.api.RestApiRequestTemplate;
import com.hellowd.server.api.exception.RequestParamException;
import com.hellowd.server.api.exception.ServiceException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-31
 * Time : 오후 4:56
 * To change this template use File | Settings | File and Code Templates.
 */
@Service("BadRequest")
@Scope("prototype")
public class BadRequest extends RestApiRequestTemplate {

    public BadRequest(Map<String,String> reqData){
        super(reqData);
    }

    @Override
    public void requestParamValidation() throws RequestParamException {
    }

    @Override
    public void service() throws ServiceException {
        this.apiResult.addProperty("resultCode", "400");
        this.apiResult.addProperty("resultMessage", "잘못된 요청입니다.");
    }
}