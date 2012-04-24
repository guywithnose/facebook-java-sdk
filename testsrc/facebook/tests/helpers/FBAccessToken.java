/*
 * File:         FBAccessToken.java
 * Author:       Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import org.json.JSONObject;

/**
 * The Class FBAccessToken.
 */
public class FBAccessToken extends TransientFacebook {
  
  /**
   * Instantiates a new fB access token.
   *
   * @param config the config
   */
  public FBAccessToken(JSONObject config)
  {
    super(config);
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
