package facebook;

import java.util.HashMap;
import javax.print.attribute.HashAttributeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Class Facebook.
 */
@SuppressWarnings("unused")
public class Facebook extends BaseFacebook
{
  
  HttpSession session;
  
  /**
   * Instantiates a new facebook.
   * 
   * @param config
   *          the config
   * @param Req
   *          the req
   */
  public Facebook(JSONObject config, HttpServletRequest Req)
  {
    super(config, Req);
  }
  
  @Override
  protected void initialize(JSONObject config, HttpServletRequest Req)
  {
    session = Req.getSession(true);
    super.initialize(config, Req);
  }

  /* (non-Javadoc)
   * @see facebook.BaseFacebook#setPersistentData(java.lang.String, java.lang.String)
   */
  @Override
  protected void setPersistentData(String key, String value)
  {
    session.setAttribute(key, value);
  }

  /* (non-Javadoc)
   * @see facebook.BaseFacebook#getPersistentData(java.lang.String, java.lang.String)
   */
  @Override
  protected String getPersistentData(String key, String Default)
  {
    if(session.getAttribute(key) != null)
      return (String)session.getAttribute(key);
    return Default;
  }

  /* (non-Javadoc)
   * @see facebook.BaseFacebook#clearPersistentData(java.lang.String)
   */
  @Override
  protected void clearPersistentData(String key)
  {
    session.removeAttribute(key);
  }

  /* (non-Javadoc)
   * @see facebook.BaseFacebook#clearAllPersistentData()
   */
  @Override
  protected void clearAllPersistentData()
  {
    session.invalidate();
    session = req.getSession(true);
  }

}
