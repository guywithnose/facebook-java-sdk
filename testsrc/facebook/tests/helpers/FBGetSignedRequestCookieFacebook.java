package facebook.tests.helpers;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 * The Class FBGetSignedRequestCookieFacebook.
 */
public class FBGetSignedRequestCookieFacebook extends TransientFacebook
{

  /**
   * Instantiates a new fB get signed request cookie facebook.
   * 
   * @param config
   *          the config
   * @param Req
   *          the req
   */
  public FBGetSignedRequestCookieFacebook(JSONObject config,
      HttpServletRequest Req)
  {
    super(config, Req);
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
  
}
