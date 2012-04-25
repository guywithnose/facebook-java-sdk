package facebook;

import org.json.JSONArray;
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

      if (result.has("error_description"))
      {
        // OAuth 2.0 Draft 10 style
        message = result.getString("error_description");
      } else if (result.has("error"))
      {
        // OAuth 2.0 Draft 00 style
        message = result.getJSONObject("error").getString("message");
      } else if (result.has("error_msg"))
      { // Rest server style
        message = result.getString("error_msg");
      } else
      {
        message = "Unknown Error. Check getResult()";
      }
    } catch (JSONException e)
    {
      e.printStackTrace();
    }

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
  
  public String getMessage()
  {
    return message;
  }

  /**
   * Returns the associated type for the error. This will default to 'Exception'
   * when a type is not available.
   * 
   * @return string
   */
  public String getType()
  {
    try
    {
      if (result.has("error"))
      {
        Object error;
        error = result.get("error");

        if (error instanceof String)
        {
          // OAuth 2.0 Draft 10 style
          return (String) error;
        } else if (error instanceof JSONObject)
        {
          // OAuth 2.0 Draft 00 style
          if (((JSONObject) error).has("type"))
          {
            return ((JSONObject) error).getString("type");
          }
        }
      }
    } catch (JSONException e)
    {
      e.printStackTrace();
    }

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
