/*
 * File:         FBAccessToken.java
 * Author:       Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 * The Class FBAccessToken.
 */
public class FBAccessToken extends TransientFacebook {
  
  /**
   * Instantiates a new fB access token.
   *
   * @param config the config
   * @param Req the req
   * @param Resp the resp
   */
  public FBAccessToken(JSONObject config, HttpServletRequest Req,
      HttpServletResponseMock Resp)
  {
    super(config, Req, Resp);
  }

  /**
   * Public get application access token.
   *
   * @return the string
   */
  public String publicGetApplicationAccessToken() {
    return getApplicationAccessToken();
  }
  
}
