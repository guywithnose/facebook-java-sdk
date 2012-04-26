/*
 * File:         FBCode.java
 * Author:       Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import facebook.Facebook;

/**
 * The Class FBCode.
 */
public class FBCode extends Facebook {
  
  private String errorMessage;

  /**
   * Instantiates a new fB code.
   *
   * @param config the config
   * @param Req the req
   * @param Resp the resp
   */
  public FBCode(JSONObject config, HttpServletRequest Req,
      HttpServletResponse Resp)
  {
    super(config, Req, Resp);
  }

  /**
   * Public get code.
   *
   * @return the string
   */
  public String publicGetCode() {
    return getCode();
  }

  /**
   * Sets the csrf state token.
   */
  public void setCSRFStateToken() {
    establishCSRFTokenState();
  }

  /**
   * Get CSRF state token.
   *
   * @return the CSRF state token
   */
  public String getCSRFStateToken() {
    return getPersistentData("state");
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