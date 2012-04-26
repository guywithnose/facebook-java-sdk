/*
 * File: PersistentFBPublic.java Author: Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import facebook.Facebook;

/**
 * The Class PersistentFBPublic.
 */
public class PersistentFBPublic extends Facebook
{

  /**
   * Instantiates a new persistent fb public.
   * 
   * @param config
   *          the config
   * @param Req
   *          the req
   * @param Resp
   *          the resp
   */
  public PersistentFBPublic(JSONObject config, HttpServletRequest Req,
      HttpServletResponse Resp)
  {
    super(config, Req, Resp);
  }

  /**
   * Public parse signed request.
   * 
   * @param input
   *          the input
   * @return the jSON object
   */
  public JSONObject publicParseSignedRequest(String input)
  {
    return parseSignedRequest(input);
  }

  /**
   * Public set persistent data.
   * 
   * @param key
   *          the key
   * @param value
   *          the value
   */
  public void publicSetPersistentData(String key, String value)
  {
    setPersistentData(key, value);
  }
  
  /**
   * Gets the session.
   * 
   * @return the session
   */
  public HttpSession getSession()
  {
    return session;
  }

}