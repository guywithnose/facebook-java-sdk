/*
 * File:         PersistentFBPublic.java
 * Author:       Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import org.json.JSONObject;
import facebook.Facebook;

/**
 * The Class PersistentFBPublic.
 */
public class PersistentFBPublic extends Facebook {
  
  /**
   * Instantiates a new persistent fb public.
   *
   * @param config the config
   */
  public PersistentFBPublic(JSONObject config)
  {
    super(config);
  }

  /**
   * Public parse signed request.
   *
   * @param input the input
   * @return the jSON object
   */
  public JSONObject publicParseSignedRequest(String input) {
    return parseSignedRequest(input);
  }

  /**
   * Public set persistent data.
   *
   * @param key the key
   * @param value the value
   */
  public void publicSetPersistentData(String key, String value) {
    setPersistentData(key, value);
  }
  
}