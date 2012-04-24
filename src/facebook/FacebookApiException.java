package facebook;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Class FacebookApiException.
 */
@SuppressWarnings("static-method")
public class FacebookApiException extends Exception
{
  /**
   * The result from the API server that represents the exception information.
   */
  protected JSONObject result;

  /** The code. */
  protected int code;

  /** The message. */
  protected String message;

  /**
   * Make a new API Exception with the given result.
   * 
   * @param Result
   *          the result
   */
  public FacebookApiException(JSONObject Result)
  {
    result = Result;

    try
    {
      code = result.getInt("error_code") != 0 ? Integer.valueOf(result
          .getInt("error_code")) : 0;
    } catch (JSONException e)
    {
      e.printStackTrace();
    }

    /*
     * TODO translate this if (isset($result['error_description'])) { // OAuth
     * 2.0 Draft 10 style $msg = $result['error_description']; } else if
     * (isset($result['error']) && is_array($result['error'])) { // OAuth 2.0
     * Draft 00 style $msg = $result['error']['message']; } else if
     * (isset($result['error_msg'])) { // Rest server style $msg =
     * $result['error_msg']; } else { $msg = 'Unknown Error. Check getResult()';
     * }
     * 
     * parent::__construct($msg, $code);
     */
  }

  /**
   * Return the associated result object returned by the API server.
   * 
   * @return array The result from the API server
   */
  public JSONObject getResult()
  {
    return result;
  }

  /**
   * Returns the associated type for the error. This will default to 'Exception'
   * when a type is not available.
   * 
   * @return string
   */
  public String getType()
  {
    /*
     * TODO translate this if (isset($this->result['error'])) { $error =
     * $this->result['error']; if (is_string($error)) { // OAuth 2.0 Draft 10
     * style return $error; } else if (is_array($error)) { // OAuth 2.0 Draft 00
     * style if (isset($error['type'])) { return $error['type']; } } }
     */

    return "Exception";
  }

  /**
   * To make debugging easier.
   * 
   * @return string The string representation of the error
   */
  @Override
  public String toString()
  {
    String str = getType() + ": ";
    if (code != 0)
    {
      str += String.valueOf(code) + ": ";
    }
    return str + message;
  }
}
