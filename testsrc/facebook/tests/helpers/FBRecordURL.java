/*
 * File: FBRecordURL.java Author: Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import org.json.JSONObject;

/**
 * The Class FBRecordURL.
 */
@SuppressWarnings("unused")
public class FBRecordURL extends TransientFacebook
{

  /**
   * Instantiates a new fB record url.
   * 
   * @param config
   *          the config
   */
  public FBRecordURL(JSONObject config)
  {
    super(config);
  }

  /** The url. */
  private String url;

  /*
   * (non-Javadoc)
   * 
   * @see facebook.BaseFacebook#_oauthRequest(java.lang.String,
   * java.lang.String)
   */
  @Override
  protected String _oauthRequest(String Url, String params)
  {
    url = Url;
    return "";
  }

  /**
   * Get Requested url.
   * 
   * @return the requested url
   */
  public String getRequestedURL()
  {
    return url;
  }
}
