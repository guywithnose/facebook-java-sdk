package facebook.tests.helpers;

import org.json.JSONObject;

import facebook.BaseFacebook;

/**
 * The Class TransientFacebook.
 */
@SuppressWarnings("unused")
public class TransientFacebook extends BaseFacebook
{
  
  /**
   * Instantiates a new transient facebook.
   * 
   * @param config
   *          the config
   */
  public TransientFacebook(JSONObject config)
  {
    super(config);
  }

  /**
   * Sets the persistent data.
   * 
   * @param key
   *          the key
   * @param value
   *          the value
   */
  @Override
  protected void setPersistentData(String key, String value)
  {
    //Do Nothing
  }

  /**
   * Gets the persistent data.
   * 
   * @param key
   *          the key
   * @return the persistent data
   */
  @Override
  protected String getPersistentData(String key)
  {
    return getPersistentData(key, "false");
  }

  /**
   * Gets the persistent data.
   * 
   * @param key
   *          the key
   * @param isDefault
   *          the is default
   * @return the persistent data
   */
  @Override
  protected String getPersistentData(String key, String Default)
  {
    return Default;
  }

  /**
   * Clear persistent data.
   * 
   * @param key
   *          the key
   */
  @Override
  protected void clearPersistentData(String key)
  {
    //Do Nothing
  }

  /**
   * Clear all persistent data.
   */
  @Override
  protected void clearAllPersistentData()
  {
    //Do Nothing
  }

}
