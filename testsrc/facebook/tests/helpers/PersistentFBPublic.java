/*
 * File: PersistentFBPublic.java Author: Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import javax.servlet.http.HttpServletRequest;

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
   */
  public PersistentFBPublic(JSONObject config, HttpServletRequest Req)
  {
    super(config, Req);
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

}