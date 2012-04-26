/*
 * File:         FBGetSignedRequestCookieFacebook.java
 * Author:       Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 * The Class FBGetSignedRequestCookieFacebook.
 */
public class FBGetSignedRequestCookieFacebook extends TransientFacebook
{

  private String errorMessage;

  /**
   * Instantiates a new fB get signed request cookie facebook.
   *
   * @param config the config
   * @param Req the req
   * @param Resp the resp
   */
  public FBGetSignedRequestCookieFacebook(JSONObject config,
      HttpServletRequest Req,
      HttpServletResponseMock Resp)
  {
    super(config, Req, Resp);
  }

  /**
   * Public get signed request.
   * 
   * @return the jSON object
   */
  public JSONObject publicGetSignedRequest()
  {
    return getSignedRequest();
  }

  /**
   * Public get signed request cookie name.
   * 
   * @return the string
   */
  public String publicGetSignedRequestCookieName()
  {
    return getSignedRequestCookieName();
  }
  
  @Override
  protected void errorLog(String msg)
  {
    errorMessage = msg;
  }
  
  /**
   * Gets the last error.
   * 
   * @return the last error
   */
  public String getLastError()
  {
    return errorMessage;
  }
  
}
