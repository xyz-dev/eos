/**
 * @(#)ServiceFilterException
 * 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 *<br> Copyright:  Copyright (c) 2014
 *<br> Company:厦门畅享信息技术有限公司
 *<br> @author ulyn
 *<br> 14-12-15 上午10:29
 *<br> @version 1.0
 *————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *————————————————————————————————
 */
package com.sunsharing.eos.common.filter;

/**
 * <pre></pre>
 * <br>----------------------------------------------------------------------
 * <br> <b>功能描述:</b>
 * <br>
 * <br> 注意事项:
 * <br>
 * <br>
 * <br>----------------------------------------------------------------------
 * <br>
 */
public class ServiceFilterException extends Exception {

    public ServiceFilterException() {
        super();
    }

    public ServiceFilterException(String message) {
        super(message);
    }

    public ServiceFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceFilterException(Throwable cause) {
        super(cause);
    }
}

