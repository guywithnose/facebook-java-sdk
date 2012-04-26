/*
 * File: FBPublic.java Author: Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 * The Class FBPublic.
 */
public class FBPublic extends TransientFacebook
{

  /**
   * Instantiates a new fB public.
   * 
   * @param config
   *          the config
   * @param Req
   *          the req
   * @param Resp
   *          the resp
   */
  public FBPublic(JSONObject config, HttpServletRequest Req,
      HttpServletResponseMock Resp)
  {
    super(config, Req, Resp);
  }

  /**
   * Public base64 url decode.
   * 
   * @param input
   *          the input
   * @return the string
   */
  public static String publicBase64UrlDecode(String input)
  {
    return base64UrlDecode(input);
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

}
