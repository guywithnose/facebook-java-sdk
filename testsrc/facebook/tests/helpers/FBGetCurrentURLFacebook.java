/*
 * File:         FBGetCurrentURLFacebook.java
 * Author:       Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 * The Class FBGetCurrentURLFacebook.
 */
public class FBGetCurrentURLFacebook extends TransientFacebook {
  
  /**
   * Instantiates a new fB get current url facebook.
   *
   * @param config the config
   */
  public FBGetCurrentURLFacebook(JSONObject config)
  {
    super(config);
  }

  /**
   * Public get current url.
   *
   * @param req the req
   * @return the string
   */
  public String publicGetCurrentUrl(HttpServletRequest req) {
    return getCurrentUrl(req);
  }
  
}
