package facebook;

import org.json.JSONObject;

/**
 * The Class Facebook.
 */
@SuppressWarnings("unused")
public class Facebook extends BaseFacebook
{

  /**
   * Instantiates a new facebook.
   * 
   * @param config
   *          the config
   */
  public Facebook(JSONObject config)
  {
    super(config);
  }

  /* (non-Javadoc)
   * @see facebook.BaseFacebook#setPersistentData(java.lang.String, java.lang.String)
   */
  @Override
  protected void setPersistentData(String key, String value)
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see facebook.BaseFacebook#getPersistentData(java.lang.String, java.lang.String)
   */
  @Override
  protected String getPersistentData(String key, String Default)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see facebook.BaseFacebook#clearPersistentData(java.lang.String)
   */
  @Override
  protected void clearPersistentData(String key)
  {
    // TODO Auto-generated method stub
  }

  /* (non-Javadoc)
   * @see facebook.BaseFacebook#clearAllPersistentData()
   */
  @Override
  protected void clearAllPersistentData()
  {
    // TODO Auto-generated method stub

  }

}
