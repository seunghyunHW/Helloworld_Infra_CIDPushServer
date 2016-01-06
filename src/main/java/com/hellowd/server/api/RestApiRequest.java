package com.hellowd.server.api;

import com.google.gson.JsonObject;
import com.hellowd.server.api.exception.RequestParamException;
import com.hellowd.server.api.exception.ServiceException;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-31
 * Time : 오후 4:52
 * To change this template use File | Settings | File and Code Templates.
 */
public interface RestApiRequest {

    //API를 호출하는 HTTP 요청의 파라미터 값이 입력되었는지 검증하는 메서드
    void requestParamValidation() throws RequestParamException;
    //각 서비스에 따른 개별 구별 구현 메서드
    void service() throws ServiceException;
    //서비스 API의 호출 시작 메서드
    void executeService();
    //API 서비스의 처리 결과를 조회하는 메서드
    JsonObject getApiResult();
}
