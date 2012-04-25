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
   * @param config
   *          the config
   * @param Req
   *          the req
   */
  public FBAccessToken(JSONObject config, HttpServletRequest Req)
  {
    super(config, Req);
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
