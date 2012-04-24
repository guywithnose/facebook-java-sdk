/*
 * File:         FBGetCurrentURLFacebook.java
 * Author:       Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

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
   * @return the string
   */
  public String publicGetCurrentUrl() {
    return getCurrentUrl();
  }
  
}
