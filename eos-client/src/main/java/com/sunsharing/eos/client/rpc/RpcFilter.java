/**
 * @(#)ServiceInvoke
 * 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 *<br> Copyright:  Copyright (c) 2014
 *<br> Company:厦门畅享信息技术有限公司
 *<br> @author ulyn
 *<br> 14-12-11 下午7:09
 *<br> @version 1.0
 *————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *————————————————————————————————
 */
package com.sunsharing.eos.client.rpc;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.eos.client.zookeeper.ServiceLocation;
import com.sunsharing.eos.common.Constants;
import com.sunsharing.eos.common.filter.*;
import com.sunsharing.eos.common.rpc.*;
import com.sunsharing.eos.common.rpc.protocol.RequestPro;
import com.sunsharing.eos.common.rpc.protocol.ResponsePro;
import com.sunsharing.eos.common.rpc.remoting.RemoteHelper;
import org.apache.log4j.Logger;

/**
 * <pre></pre>
 * <br>----------------------------------------------------------------------
 * <br> <b>功能描述:</b>
 * <br>  执行调用的过滤器
 * <br> 注意事项:
 * <br>
 * <br>
 * <br>----------------------------------------------------------------------
 * <br>
 */
public class RpcFilter extends AbstractServiceFilter {
    private Logger logger = Logger.getLogger(RpcFilter.class);

    public RpcFilter() {
    }


    /**
     * 执行过滤
     *
     * @param serviceRequest
     * @param serviceResponse
     * @param fc
     */
    @Override
    protected void doFilter(ServiceRequest serviceRequest,
                            ServiceResponse serviceResponse, FilterChain fc) throws ServiceFilterException, RpcException {
        RequestPro requestPro = serviceRequest.getRequestPro();
        //zookeeper取得服务的ip
        boolean isMock = !StringUtils.isBlank(requestPro.getMock());
        if (isMock) {
            logger.info(requestPro.getServiceId() + "." + requestPro.getInvocation().getMethodName() + " use mock:" + requestPro.getMock());
        }
        JSONObject jo = getEosLocation(requestPro.getAppId(), requestPro.getServiceId(),
                requestPro.getServiceVersion(), isMock);
        String ip = jo.getString("ip");
        int port = jo.getInteger("port");

        RemoteHelper helper = new RemoteHelper();
        try {
            ServiceResponse response = helper.call(serviceRequest, ip, port);
            serviceResponse.writeResponsePro(response.getResponsePro());
        } catch (RpcException e) {
            throw e;
        } catch (Throwable throwable) {
            throw new RpcException(RpcException.NETWORK_EXCEPTION, throwable);
        }
        fc.doFilter(serviceRequest, serviceResponse);
    }

    private JSONObject getEosLocation(String appId, String serviceId, String serviceVersion, boolean isMock) throws RpcException {
        JSONObject jo;
        if (!isMock) {
            jo = ServiceLocation.getInstance().getServiceLocation(appId, serviceId, serviceVersion);
        } else {
            jo = ServiceLocation.getInstance().getOnlineEOS();
        }
        if (jo == null) {
            if (isMock) {
                String errTip = "对于模拟调用，没有找到可用的eos,请确保服务" + appId + "-"
                        + serviceId + "-"
                        + serviceVersion + "是否有效或者eos节点已经启动！";
                logger.error(errTip);
                throw new RpcException(RpcException.SERVICE_NO_FOUND_EXCEPTION, errTip);
            } else {
                String errTip = "没有找到请求的可用的eos节点,请确保服务" + appId + "-"
                        + serviceId + "-"
                        + serviceVersion + "是否有效或者eos节点已经启动！";
                logger.error(errTip);
                throw new RpcException(RpcException.SERVICE_NO_FOUND_EXCEPTION, errTip);
            }
        }
        return jo;
    }

}

