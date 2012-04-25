/*
 * File: FBGetCurrentURLFacebook.java Author: Robert Bittle
 * <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 * The Class FBGetCurrentURLFacebook.
 */
public class FBGetCurrentURLFacebook extends TransientFacebook
{

  /**
   * Instantiates a new fB get current url facebook.
   * 
   * @param config
   *          the config
   * @param Req
   *          the req
   */
  public FBGetCurrentURLFacebook(JSONObject config, HttpServletRequest Req)
  {
    super(config, Req);
  }

  /**
   * Public get current url.
   * 
   * @return the string
   */
  public String publicGetCurrentUrl()
  {
    return getCurrentUrl();
  }

}
