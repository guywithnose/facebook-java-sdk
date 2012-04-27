/*
 * File: facebookTest.java Author: Robert Bittle <guywithnose@gmail.com>
 */
package facebook.tests;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import facebook.BaseFacebook;
import facebook.Facebook;
import facebook.FacebookApiException;
import facebook.tests.helpers.FBAccessToken;
import facebook.tests.helpers.FBCode;
import facebook.tests.helpers.FBGetCurrentURLFacebook;
import facebook.tests.helpers.FBGetSignedRequestCookieFacebook;
import facebook.tests.helpers.FBPublic;
import facebook.tests.helpers.FBRecordURL;
import facebook.tests.helpers.HttpServletRequestMock;
import facebook.tests.helpers.HttpServletResponseMock;
import facebook.tests.helpers.PersistentFBPublic;
import facebook.tests.helpers.TransientFacebook;

/**
 * The Class facebookTest.
 */
@SuppressWarnings(
{
    "unused", "static-method"
})
public class facebookTest
{

  /** The app id. */
  private final String APP_ID = "117743971608120";

  /** The SECRET. */
  private final String SECRET = "943716006e74d9b9283d4d5d8ab93204";

  /** The config. */
  private JSONObject config;

  /** The MIGRATE d_ ap p_ id. */
  private final String MIGRATED_APP_ID = "174236045938435";

  /** The MIGRATE d_ secret. */
  private final String MIGRATED_SECRET = "0073dce2d95c4a5c2922d1827ea0cca6";

  /** The Constant kExpiredAccessToken. */
  private static final String kExpiredAccessToken = "206492729383450|2.N4RKywNPuHAey7CK56_wmg__.3600.1304560800.1-214707|6Q14AfpYi_XJB26aRQumouzJiGA";

  /** The Constant kValidSignedRequest. */
  private static final String kValidSignedRequest = "1sxR88U4SW9m6QnSxwCEw_CObqsllXhnpP5j2pxD97c.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImV4cGlyZXMiOjEyODEwNTI4MDAsIm9hdXRoX3Rva2VuIjoiMTE3NzQzOTcxNjA4MTIwfDIuVlNUUWpub3hYVVNYd1RzcDB1U2g5d19fLjg2NDAwLjEyODEwNTI4MDAtMTY3Nzg0NjM4NXx4NURORHBtcy1nMUM0dUJHQVYzSVdRX2pYV0kuIiwidXNlcl9pZCI6IjE2Nzc4NDYzODUifQ";

  /** The Constant kNonTosedSignedRequest. */
  private static final String kNonTosedSignedRequest = "c0Ih6vYvauDwncv0n0pndr0hP0mvZaJPQDPt6Z43O0k.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiJ9";

  /** The Constant kSignedRequestWithBogusSignature. */
  private static final String kSignedRequestWithBogusSignature = "1sxR32U4SW9m6QnSxwCEw_CObqsllXhnpP5j2pxD97c.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImV4cGlyZXMiOjEyODEwNTI4MDAsIm9hdXRoX3Rva2VuIjoiMTE3NzQzOTcxNjA4MTIwfDIuVlNUUWpub3hYVVNYd1RzcDB1U2g5d19fLjg2NDAwLjEyODEwNTI4MDAtMTY3Nzg0NjM4NXx4NURORHBtcy1nMUM0dUJHQVYzSVdRX2pYV0kuIiwidXNlcl9pZCI6IjE2Nzc4NDYzODUifQ";

  /**
   * Tests the constructor method.
   */
  @Test
  public void testConstructor()
  {
    BaseFacebook facebook = new TransientFacebook(config,
        new HttpServletRequestMock(), new HttpServletResponseMock());
    assertEquals("Expect the App ID to be set.", facebook.getAppId(), APP_ID);
    assertEquals("Expect the API secret to be set.", facebook.getAppSecret(),
        SECRET);
  }

  /**
   * Tests the constructorWithFileUpload method.
   */
  @Test
  public void testConstructorWithFileUpload()
  {
    try
    {
      config.put("fileUpload", true);
    } catch (JSONException e)
    {
      e.printStackTrace();
    }
    BaseFacebook facebook = new TransientFacebook(config,
        new HttpServletRequestMock(), new HttpServletResponseMock());
    assertEquals("Expect the App ID to be set.", facebook.getAppId(), APP_ID);
    assertEquals("Expect the API secret to be set.", facebook.getAppSecret(),
        SECRET);
    assertTrue("Expect file upload support to be on.",
        facebook.getFileUploadSupport());
  }

  /**
   * Tests the setAppId method.
   */
  @Test
  public void testSetAppId()
  {
    BaseFacebook facebook = new TransientFacebook(config,
        new HttpServletRequestMock(), new HttpServletResponseMock());
    facebook.setAppId("dummy");
    assertEquals("Expect the App ID to be dummy.", facebook.getAppId(), "dummy");
  }

  /**
   * Tests the setAPPSecret method.
   */
  @Test
  public void testSetAPPSecret()
  {
    BaseFacebook facebook = new TransientFacebook(config,
        new HttpServletRequestMock(), new HttpServletResponseMock());
    facebook.setAppSecret("dummy");
    assertEquals("Expect the API secret to be dummy.", facebook.getAppSecret(),
        "dummy");
  }

  /**
   * Tests the setAccessToken method.
   */
  @Test
  public void testSetAccessToken()
  {
    BaseFacebook facebook = new TransientFacebook(config,
        new HttpServletRequestMock(), new HttpServletResponseMock());
    facebook.setAccessToken("saltydog");
    assertEquals("Expect installed access token to remain \"saltydog\"",
        facebook.getAccessToken(), "saltydog");

  }

  /**
   * Tests the setFileUploadSupport method.
   */
  @Test
  public void testSetFileUploadSupport()
  {
    BaseFacebook facebook = new TransientFacebook(config,
        new HttpServletRequestMock(), new HttpServletResponseMock());
    assertFalse("Expect file upload support to be off.",
        facebook.getFileUploadSupport());
    facebook.setFileUploadSupport(true);
    assertTrue("Expect file upload support to be on.",
        facebook.getFileUploadSupport());
  }

  /**
   * Tests the getCurrentURL method.
   */
  @Test
  public void testGetCurrentURL()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    req.setRequestString("http://www.test.com/unit-tests.php?one=one&two=two&three=three");
    FBGetCurrentURLFacebook facebook = new FBGetCurrentURLFacebook(config, req,
        new HttpServletResponseMock());

    String current_url = facebook.publicGetCurrentUrl();
    assertEquals("getCurrentUrl void is changing the current URL",
        "http://www.test.com/unit-tests.php?one=one&two=two&three=three",
        current_url);

    // ensure structure of valueless GET params is retained (sometimes
    // an = sign was present, and sometimes it was not)
    // first test when equal signs are present

    req.setRequestString("http://www.test.com/unit-tests.php?one=&two=&three=");

    current_url = facebook.publicGetCurrentUrl();
    assertEquals("getCurrentUrl void is changing the current URL",
        "http://www.test.com/unit-tests.php?one=&two=&three=", current_url);

    // then test when equal signs are not present

    req.setRequestString("http://www.test.com/unit-tests.php?one&two&three");
    current_url = facebook.publicGetCurrentUrl();
    assertEquals("getCurrentUrl void is changing the current URL",
        "http://www.test.com/unit-tests.php?one&two&three", current_url);
  }

  /**
   * Tests the getLoginURL method.
   */
  @Test
  public void testGetLoginURL()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    req.setRequestString("http://www.test.com/unit-tests.php");
    Facebook facebook = new Facebook(config, req, new HttpServletResponseMock());

    HashMap<String, String> login_url = parse_url(facebook.getLoginUrl());
    assertEquals("https", login_url.get("scheme"));
    assertEquals("www.facebook.com", login_url.get("host"));
    assertEquals("/dialog/oauth", login_url.get("path"));
    HashMap<String, String> expected_login_params = new HashMap<String, String>()
    {
      {
        put("client_id", APP_ID);
        put("redirect_uri", "http://www.test.com/unit-tests.php");
      }
    };

    HashMap<String, String> query_map = BaseFacebook.parse_str(login_url
        .get("query"));
    assertIsSubset(expected_login_params, query_map);
    // we don't know what the state is, but we know it"s an md5 and should
    // be 32 characters long.
    assertEquals(32, query_map.get("state").length());
  }

  /**
   * Tests the getLoginURL method using extra params.
   */
  @Test
  public void testGetLoginURL_ExtraParams()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    req.setRequestString("http://www.test.com/unit-tests.php");
    Facebook facebook = new Facebook(config, req, new HttpServletResponseMock());

    HashMap<String, String> extra_params = new HashMap<String, String>()
    {
      {
        put("scope", "email, sms");
        put("nonsense", "nonsense");
      }
    };
    HashMap<String, String> login_url = parse_url(facebook
        .getLoginUrl(extra_params));
    assertEquals(login_url.get("scheme"), "https");
    assertEquals(login_url.get("host"), "www.facebook.com");
    assertEquals(login_url.get("path"), "/dialog/oauth");
    HashMap<String, String> expected_login_params = extra_params;
    expected_login_params.put("client_id", APP_ID);
    expected_login_params.put("redirect_uri",
        "http://www.test.com/unit-tests.php");
    HashMap<String, String> query_map = BaseFacebook.parse_str(login_url
        .get("query"));
    assertIsSubset(expected_login_params, query_map);
    // we don't know what the state is, but we know it"s an md5 and should
    // be 32 characters long.
    assertEquals(32, query_map.get("state").length());
  }

  /**
   * Tests the getCode method using valid csrf state.
   */
  @Test
  public void testGetCode_ValidCSRFState()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    FBCode facebook = new FBCode(config, req, new HttpServletResponseMock());

    facebook.setCSRFStateToken();
    String code = TransientFacebook.md5();
    req.setParameter("code", code);
    req.setParameter("state", facebook.getCSRFStateToken());
    assertEquals("Expect code to be pulled from $_REQUEST[\"code\"]", code,
        facebook.publicGetCode());
  }

  /**
   * Tests the getCode method using invalid csrf state.
   */
  @Test
  public void testGetCode_InvalidCSRFState()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    FBCode facebook = new FBCode(config, req, new HttpServletResponseMock());

    facebook.setCSRFStateToken();
    req.setParameter("code", TransientFacebook.md5());
    req.setParameter("state", facebook.getCSRFStateToken() + "forgery!!!");
    assertNull("Expect getCode to fail, CSRF state should not match.",
        facebook.publicGetCode());
    assertEquals("CSRF state token does not match one provided.",
        facebook.getLastError());
  }

  /**
   * Tests the getCode method using missing csrf state.
   */
  @Test
  public void testGetCode_MissingCSRFState()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    FBCode facebook = new FBCode(config, req, new HttpServletResponseMock());

    facebook.setCSRFStateToken();
    req.setParameter("code", TransientFacebook.md5());
    assertNull("Expect getCode to fail, CSRF state not sent back.",
        facebook.publicGetCode());
    assertEquals("CSRF state token does not match one provided.",
        facebook.getLastError());
  }

  /**
   * Tests the getUser method using signed request.
   */
  @Test
  public void testGetUser_SignedRequest()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    req.setParameter("signed_request", kValidSignedRequest);
    assertEquals("Failed to get user ID from a valid signed request.",
        1677846385, facebook.getUser());
  }

  /**
   * Tests the getSignedRequest method using cookie.
   */
  @Test
  public void testGetSignedRequest_Cookie()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    FBGetSignedRequestCookieFacebook facebook = new FBGetSignedRequestCookieFacebook(
        config, req, new HttpServletResponseMock());

    req.addCookie(facebook.publicGetSignedRequestCookieName(),
        kValidSignedRequest);
    assertNotNull(facebook.publicGetSignedRequest());
    assertEquals("Failed to get user ID from a valid signed request.",
        1677846385, facebook.getUser());
  }

  /**
   * Tests the getSignedRequest method using incorrect signature.
   */
  @Test
  public void testGetSignedRequest_IncorrectSignature()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    FBGetSignedRequestCookieFacebook facebook = new FBGetSignedRequestCookieFacebook(
        config, req, new HttpServletResponseMock());

    req.addCookie(facebook.publicGetSignedRequestCookieName(),
        kSignedRequestWithBogusSignature);
    assertNull(facebook.publicGetSignedRequest());
    assertEquals("Bad Signed JSON signature!", facebook.getLastError());
  }

  /**
   * Tests the nonUserAccessToken method.
   */
  @Test
  public void testNonUserAccessToken()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    FBAccessToken facebook = new FBAccessToken(config, req,
        new HttpServletResponseMock());

    // no cookies, and no request params, so no user or code,
    // so no user access token (even with cookie support)
    assertEquals("Access token should be that for logged out users.",
        facebook.publicGetApplicationAccessToken(), facebook.getAccessToken());
  }

  /**
   * Tests the API method using logged out users.
   * 
   * @throws JSONException
   *           the jSON exception
   * @throws FacebookApiException
   *           the facebook api exception
   */
  @Test
  public void testAPI_LoggedOutUsers() throws JSONException,
      FacebookApiException
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    JSONObject response = facebook.api(new HashMap<String, String>()
    {
      {
        put("method", "fql.query");
        put("query", "SELECT name FROM user WHERE uid=4");
      }
    });
    assertEquals("Expect one row back.",
        response.getJSONArray("data").length(), 1);
    assertEquals("Expect the name back.", response.getJSONArray("data")
        .getJSONObject(0).getString("name"), "Mark Zuckerberg");
  }

  /**
   * Tests the API method using bogus access token.
   */
  @Test
  public void testAPI_BogusAccessToken()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    facebook.setAccessToken("this-is-not-really-an-access-token");
    // if we don't set an access token and there"s no way to
    // get one, then the FQL query below works beautifully, handing
    // over Zuck"s public data. But if you specify a bogus access
    // token as I have right here, then the FQL query should fail.
    // We could return just Zuck"s public data, but that wouldn't
    // advertise the issue that the access token is at worst broken
    // and at best expired.
    try
    {
      Object response = facebook.api(new HashMap<String, String>()
      {
        {
          put("method", "fql.query");
          put("query", "SELECT name FROM user WHERE uid=4");
        }
      });
      fail("Should not get here.");
    } catch (FacebookApiException e)
    {
      JSONObject result = e.getResult();
      try
      {
        assertEquals(190, result.getInt("error_code"));
      } catch (JSONException e1)
      {
        fail(e1.getMessage());
      }
    }
  }

  /**
   * Tests the APIGraph method using public data.
   * 
   * @throws JSONException
   *           the jSON exception
   * @throws FacebookApiException
   *           the facebook api exception
   */
  @Test
  public void testAPIGraph_PublicData() throws JSONException,
      FacebookApiException
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    JSONObject response = facebook.api("/jerry");
    assertEquals("should get expected id.", "214707", response.get("id"));
  }

  /**
   * Tests the graphAPI method using bogus access token.
   */
  @Test
  public void testGraphAPI_BogusAccessToken()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    facebook.setAccessToken("this-is-not-really-an-access-token");
    try
    {
      JSONObject response = facebook.api("/me");
      fail("Should not get here.");
    } catch (FacebookApiException e)
    {
      // means the server got the access token and didn't like it
      String msg = "OAuthException: Invalid OAuth access token.";
      assertEquals("Expect the invalid OAuth token message.", msg, e.toString());
    }
  }

  /**
   * Tests the graphAPI method using expired access token.
   */
  @Test
  public void testGraphAPI_ExpiredAccessToken()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    facebook.setAccessToken(kExpiredAccessToken);
    try
    {
      JSONObject response = facebook.api("/me");
      fail("Should not get here.");
    } catch (FacebookApiException e)
    {
      // means the server got the access token and didn't like it
      String error_msg_start = "OAuthException: Error invalidating access token:";
      assertEquals("Expect the token validation error message.", e.toString()
          .substring(0, 48), error_msg_start);
    }
  }

  /**
   * Tests the graphAPIMethod method.
   */
  @Test
  public void testGraphAPIMethod()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    try
    {
      // naitik being bold about deleting his entire record....
      // let's hope this never actually passes.
      JSONObject response = facebook.api("/naitik", "DELETE");
      fail("Should not get here.");
    } catch (FacebookApiException e)
    {
      // ProfileDelete means the server understood the DELETE
      String msg = "OAuthException: (#200) User cannot access this application";
      assertEquals("Expect the invalid session message.", msg, e.toString());
    }
  }

  /**
   * Tests the graphAPI method using o auth spec error.
   * 
   * @throws JSONException
   *           the jSON exception
   */
  @Test
  public void testGraphAPI_OAuthSpecError() throws JSONException
  {
    config.put("appId", MIGRATED_APP_ID);
    config.put("secret", MIGRATED_SECRET);
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    try
    {
      JSONObject response = facebook.api("/me", new HashMap<String, String>()
      {
        {
          put("client_id", MIGRATED_APP_ID);
        }
      });

      fail("Should not get here.");
    } catch (FacebookApiException e)
    {
      // means the server got the access token
      String msg = "invalid_request: An active access token must be used to query information about the current user.";
      assertEquals("Expect the invalid session message.", msg, e.toString());
    }

  }

  /**
   * Tests the graphAPI method using valid oauth.
   * 
   * @throws JSONException
   *           the jSON exception
   */
  @Test
  public void testGraphAPI_ValidOAuthStringHashMap() throws JSONException, FacebookApiException
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    JSONObject response = facebook.api("/jerry", new HashMap<String, String>());
    assertEquals(214707, response.getLong("id"));
  }

  /**
   * Tests the graphAPI method using valid oauth.
   * 
   * @throws JSONException
   *           the jSON exception
   */
  @Test
  public void testGraphAPI_ValidOAuthStringString() throws JSONException, FacebookApiException
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    JSONObject response = facebook.api("/jerry", "GET");
    assertEquals(214707, response.getLong("id"));
  }

  /**
   * Tests the graphAPI method using valid oauth.
   * 
   * @throws JSONException
   *           the jSON exception
   */
  @Test
  public void testGraphAPI_ValidOAuthStringStringHashMap() throws JSONException, FacebookApiException
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    JSONObject response = facebook.api("/jerry", "GET", new HashMap<String, String>());
    assertEquals(214707, response.getLong("id"));
  }

  /**
   * Tests the graphAPI method using method o auth spec error.
   * 
   * @throws JSONException
   *           the jSON exception
   */
  @Test
  public void testGraphAPI_MethodOAuthSpecError() throws JSONException
  {
    config.put("appId", MIGRATED_APP_ID);
    config.put("secret", MIGRATED_SECRET);
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    try
    {
      JSONObject response = facebook.api("/daaku.shah", "DELETE",
          new HashMap<String, String>()
          {
            {
              put("client_id", MIGRATED_APP_ID);
            }
          });

      fail("Should not get here.");
    } catch (FacebookApiException e)
    {
      assertEquals(e.toString().substring(0, 15), "invalid_request");
    }
  }

  /**
   * Tests the curlFailure method.
   */
  @Test
  public void testCurlFailure()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    FacebookApiException exception = null;
    try
    {
      // we dont expect facebook will ever return in 1ms
      BaseFacebook.timeout = 1;
      facebook.api("/naitik");
    } catch (FacebookApiException e)
    {
      exception = e;
    }

    if (exception == null)
    {
      fail("no exception was thrown on timeout.");
    } else
    {
      assertEquals(28, exception.getCode());
      assertEquals("CurlException", exception.getType());
    }
  }

  /**
   * Tests the graphAPI method using only params.
   * 
   * @throws FacebookApiException
   *           the facebook api exception
   */
  @Test
  public void testGraphAPI_OnlyParams() throws FacebookApiException
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    JSONObject response = facebook.api("/jerry");
    assertTrue("User ID should be public.", response.has("id"));
    assertTrue("User\'s name should be public.", response.has("name"));
    assertTrue("User\'s first name should be public.",
        response.has("first_name"));
    assertTrue("User\'s last name should be public.", response.has("last_name"));
    assertFalse("User\'s work history should only be available with "
        + "a valid access token.", response.has("work"));
    assertFalse("User\'s education history should only be "
        + "available with a valid access token.", response.has("education"));
    assertFalse("User\'s verification status should only be "
        + "available with a valid access token.", response.has("verified"));
  }

  /**
   * Tests the loginURL method using defaults.
   */
  @Test
  public void testLoginURL_Defaults()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    req.setRequestString("http://fbrell.com/examples");
    String encodedUrl = "";
    try
    {
      encodedUrl = URLEncoder
          .encode("http://fbrell.com/examples", "ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    assertTrue("Expect the current url to exist.", facebook.getLoginUrl()
        .indexOf(encodedUrl) != -1);
  }

  /**
   * Tests the loginURL method using defaults drop state query param.
   */
  @Test
  public void testLoginURL_DefaultsDropStateQueryParam()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    req.setRequestString("http://fbrell.com/examples?state=xx42xx");
    String expectEncodedUrl = "";
    try
    {
      expectEncodedUrl = URLEncoder.encode("http://fbrell.com/examples",
          "ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    assertFalse("Expect the current url to exist.", facebook.getLoginUrl()
        .indexOf(expectEncodedUrl) == -1);
    assertTrue("Expect the session param to be dropped.", facebook
        .getLoginUrl().indexOf("xx42xx") == -1);
  }

  /**
   * Tests the loginURL method using defaults drop code query param.
   */
  @Test
  public void testLoginURL_DefaultsDropCodeQueryParam()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    req.setRequestString("http://fbrell.com/examples?code=xx42xx");
    String expectEncodedUrl = "";
    try
    {
      expectEncodedUrl = URLEncoder.encode("http://fbrell.com/examples",
          "ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    assertFalse("Expect the current url to exist.", facebook.getLoginUrl()
        .indexOf(expectEncodedUrl) == -1);
    assertTrue("Expect the session param to be dropped.", facebook
        .getLoginUrl().indexOf("xx42xx") == -1);
  }

  /**
   * Tests the loginURL method using defaults drop signed request param but not
   * others.
   */
  @Test
  public void testLoginURL_DefaultsDropSignedRequestParamButNotOthers()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    req.setRequestString("http://fbrell.com/examples?signed_request=xx42xx&do_not_drop=xx43xx");
    String expectEncodedUrl = "";
    try
    {
      expectEncodedUrl = URLEncoder.encode(
          "http://fbrell.com/examples?do_not_drop=xx43xx", "ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    assertFalse("Expect the current url to exist.", facebook.getLoginUrl()
        .indexOf(expectEncodedUrl) == -1);
    assertTrue("Expect the session param to be dropped.", facebook
        .getLoginUrl().indexOf("xx42xx") == -1);
    assertFalse("Expect the session param to be dropped.", facebook
        .getLoginUrl().indexOf("xx43xx") == -1);
  }

  /**
   * Tests the loginURL method using custom next.
   */
  @Test
  public void testLoginURL_CustomNext()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    req.setRequestString("http://fbrell.com/examples");
    String next = "http://fbrell.com/custom";
    String loginUrl = facebook.getLoginUrl(new HashMap<String, String>()
    {
      {
        put("redirect_uri", "http://fbrell.com/custom");
        put("cancel_url", "http://fbrell.com/custom");
      }
    });
    String currentEncodedUrl = "";
    String expectedEncodedUrl = "";
    try
    {
      currentEncodedUrl = URLEncoder.encode("http://fbrell.com/examples",
          "ISO-8859-1");
      expectedEncodedUrl = URLEncoder.encode(next, "ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    assertFalse("Expect the custom url to exist.",
        loginUrl.indexOf(expectedEncodedUrl) == -1);
    assertTrue("Expect the current url to not exist.",
        loginUrl.indexOf(currentEncodedUrl) == -1);
  }

  /**
   * Tests the logoutURL method using defaults.
   */
  @Test
  public void testLogoutURL_Defaults()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    req.setRequestString("http://fbrell.com/examples");
    String encodedUrl = "";
    try
    {
      encodedUrl = URLEncoder
          .encode("http://fbrell.com/examples", "ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    assertFalse("Expect the current url to exist.", facebook.getLogoutUrl()
        .indexOf(encodedUrl) == -1);
  }

  /**
   * Tests the loginStatusURL method using defaults.
   */
  @Test
  public void testLoginStatusURL_Defaults()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    req.setRequestString("http://fbrell.com/examples");
    String encodedUrl = "";
    try
    {
      encodedUrl = URLEncoder
          .encode("http://fbrell.com/examples", "ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    assertFalse("Expect the current url to exist.", facebook
        .getLoginStatusUrl().indexOf(encodedUrl) == -1);
  }

  /**
   * Tests the loginStatusURL method using custom.
   */
  @Test
  public void testLoginStatusURL_Custom()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    req.setRequestString("http://fbrell.com/examples");
    String encodedUrl1 = "";
    String encodedUrl2 = "";
    String okUrl = "http://fbrell.com/here1";
    try
    {
      encodedUrl1 = URLEncoder.encode("http://fbrell.com/examples",
          "ISO-8859-1");
      encodedUrl2 = URLEncoder.encode(okUrl, "ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    String loginStatusUrl = facebook
        .getLoginStatusUrl(new HashMap<String, String>()
        {
          {
            put("ok_session", "http://fbrell.com/here1");
          }
        });
    assertFalse("Expect the current url to exist.",
        loginStatusUrl.indexOf(encodedUrl1) == -1);
    assertFalse("Expect the custom url to exist.",
        loginStatusUrl.indexOf(encodedUrl2) == -1);
  }

  /**
   * Tests the getLoginUrl method using non default port.
   */
  @Test
  public void testGetLoginUrl_NonDefaultPort()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    req.setRequestString("http://fbrell.com:8080/examples");
    String encodedUrl = "";
    try
    {
      encodedUrl = URLEncoder.encode("http://fbrell.com:8080/examples",
          "ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    assertFalse("Expect the current url to exist.", facebook.getLoginUrl()
        .indexOf(encodedUrl) == 1);
  }

  /**
   * Tests the getLoginUrl method using secure current url.
   */
  @Test
  public void testGetLoginUrl_SecureCurrentUrl()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    req.setRequestString("https://fbrell.com/examples");
    String encodedUrl = "";
    try
    {
      encodedUrl = URLEncoder.encode("https://fbrell.com/examples",
          "ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    assertFalse("Expect the current url to exist.", facebook.getLoginUrl()
        .indexOf(encodedUrl) == -1);
  }

  /**
   * Tests the getLoginUrl method using secure current url with non default
   * port.
   */
  @Test
  public void testGetLoginUrl_SecureCurrentUrlWithNonDefaultPort()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    req.setRequestString("https://fbrell.com:8080/examples");
    String encodedUrl = "";
    try
    {
      encodedUrl = URLEncoder.encode("https://fbrell.com:8080/examples",
          "ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    assertFalse("Expect the current url to exist.", facebook.getLoginUrl()
        .indexOf(encodedUrl) == -1);
  }

  /**
   * Tests the appSecretCall method.
   */
  @Test
  public void testAppSecretCall()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());
    try
    {
      JSONObject response = facebook.api("/" + APP_ID + "/insights");
      fail("Desktop applications need a user token for insights.");
    } catch (FacebookApiException e)
    {
      assertTrue(
          "Incorrect exception type thrown when trying to gain "
              + "insights for desktop app without a user access token.",
          e.getMessage().indexOf(
              "An access token is required to request this resource.") != -1);
    } catch (Exception $e)
    {
      fail("Incorrect exception type thrown when trying to gain "
          + "insights for desktop app without a user access token.");
    }
  }

  /**
   * Tests the base64UrlEncode method.
   */
  @Test
  public void testBase64UrlEncode()
  {
    String input = "Facebook rocks";
    String output = "RmFjZWJvb2sgcm9ja3M";

    assertEquals(input, FBPublic.publicBase64UrlDecode(output));
  }

  /**
   * Tests the signedToken method.
   */
  @Test
  public void testSignedToken()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    FBPublic facebook = new FBPublic(config, req, new HttpServletResponseMock());
    JSONObject payload = facebook.publicParseSignedRequest(kValidSignedRequest);
    assertNotNull("Expected token to parse", payload);
    assertNull(facebook.getSignedRequest());
    req.setParameter("signed_request", kValidSignedRequest);
    assertEquals(facebook.getSignedRequest().toString(), payload.toString());
  }

  /**
   * Tests the signedToken method using non tossed signedtoken.
   * 
   * @throws JSONException
   *           the jSON exception
   */
  @Test
  public void testSignedToken_NonTossedSignedtoken() throws JSONException
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    FBPublic facebook = new FBPublic(config, req, new HttpServletResponseMock());

    JSONObject payload = facebook
        .publicParseSignedRequest(kNonTosedSignedRequest);
    assertNotNull("Expected token to parse", payload);
    assertNull(facebook.getSignedRequest());
    req.setParameter("signed_request", kNonTosedSignedRequest);
    assertEquals(facebook.getSignedRequest().get("algorithm"), "HMAC-SHA256");
  }

  /**
   * Tests the bundledCACert method.
   *
   * @throws JSONException the jSON exception
   * @throws FacebookApiException the facebook api exception
   */
  @Test
  public void testBundledCACert() throws JSONException, FacebookApiException
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    TransientFacebook facebook = new TransientFacebook(config, req,
        new HttpServletResponseMock());

    // use the bundled cert from the start
    BaseFacebook.certFile = "resource/fb_ca_chain_bundle.pfx";
    BaseFacebook.password = "your_private_keypass";
    JSONObject response = facebook.api("/naitik");
    assertEquals("should get expected id.", 5526183, response.getLong("id"));
  }

  /**
   * Tests the videoUpload method.
   * 
   * @throws FacebookApiException
   *           the facebook api exception
   */
  @Test
  public void testVideoUpload() throws FacebookApiException
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    FBRecordURL facebook = new FBRecordURL(config, req,
        new HttpServletResponseMock());
    facebook.api(new HashMap<String, String>()
    {
      {
        put("method", "video.upload");
      }
    });
    assertTrue("video.upload should go against api-video", facebook
        .getRequestedURL().indexOf("//api-video.") != -1);
  }

  /**
   * Tests the getUserAndAccessToken method using session.
   */
  @Test
  public void testGetUserAndAccessToken_Session()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    PersistentFBPublic facebook = new PersistentFBPublic(config, req,
        new HttpServletResponseMock());
    facebook.publicSetPersistentData("access_token", kExpiredAccessToken);
    facebook.publicSetPersistentData("user_id", "12345");
    assertEquals("Get access token from persistent store.",
        kExpiredAccessToken, facebook.getAccessToken());
    assertEquals("Get user id from persistent store.", 12345,
        facebook.getUser());
  }

  /**
   * Tests the getUserAndAccessToken method using signed request not session.
   */
  @Test
  public void testGetUserAndAccessToken_SignedRequestNotSession()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    PersistentFBPublic facebook = new PersistentFBPublic(config, req,
        new HttpServletResponseMock());

    req.setParameter("signed_request", kValidSignedRequest);
    facebook.publicSetPersistentData("user_id", "41572");
    facebook.publicSetPersistentData("access_token", kExpiredAccessToken);
    assertTrue("Got user from session instead of signed request.",
        41572 != facebook.getUser());
    assertEquals("Failed to get correct user ID from signed request.",
        1677846385, facebook.getUser());
    assertTrue("Got access token from session instead of signed request.",
        !kExpiredAccessToken.equals(facebook.getAccessToken()));
    assertNotNull("Failed to extract an access token from the signed request.",
        facebook.getAccessToken());
    assertTrue("Failed to extract an access token from the signed request.",
        facebook.getAccessToken().length() > 0);
  }

  /**
   * Tests the getUser method using no code or signed request or session.
   */
  @Test
  public void testGetUser_NoCodeOrSignedRequestOrSession()
  {
    HttpServletRequestMock req = new HttpServletRequestMock();
    PersistentFBPublic facebook = new PersistentFBPublic(config, req,
        new HttpServletResponseMock());

    // deliberately leave $_REQUEST and _$SESSION empty
    assertTrue("GET, POST, and COOKIE params exist even though "
        + "they should.  Test cannot succeed unless all of "
        + "$_REQUEST is empty.", req.getParameterMap().size() == 0);
    
    assertFalse("Session is carrying state and should not be.", facebook
        .getSession().getAttributeNames().hasMoreElements());
    
    assertEquals("Got a user id, even without a signed request, "
        + "access token, or session variable.", 0, facebook.getUser());

    assertFalse("Session superglobal incorrectly populated by getUser.",
        facebook.getSession().getAttributeNames().hasMoreElements());

  }

  /**
   * Sets up.
   */
  @Before
  public void setUp()
  {
    BaseFacebook.timeout = 10000;
    BaseFacebook.certFile = null;
    BaseFacebook.password = null;
    try
    {
      config = new JSONObject("{\"appId\": \"" + APP_ID + "\",\"secret\": \""
          + SECRET + "\"}");
    } catch (JSONException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Checks that the correct args are a subset of the returned obj.
   * 
   * @param correct
   *          the correct
   * @param actual
   *          the actual
   */
  protected void assertIsSubset(HashMap<String, String> correct,
      HashMap<String, String> actual)
  {
    assertIsSubset(correct, actual, "");
  }

  /**
   * Checks that the correct args are a subset of the returned obj.
   * 
   * @param correct
   *          the correct
   * @param actual
   *          the actual
   * @param msg
   *          the msg
   */
  protected void assertIsSubset(HashMap<String, String> correct,
      HashMap<String, String> actual, String msg)
  {
    for (String key : correct.keySet())
    {
      String actual_value = actual.get(key);
      String newMsg = msg.length() != 0 ? msg + " " : "" + "Key: " + key;
      assertEquals(newMsg, correct.get(key), actual_value);
    }
  }

  /**
   * Parse_url.
   * 
   * @param url
   *          the url
   * @return the hash map
   */
  protected HashMap<String, String> parse_url(String url)
  {
    HashMap<String, String> urlParts = new HashMap<String, String>();
    int scheme = url.indexOf("://") + 3;
    int port = url.indexOf(":", scheme);
    urlParts.put("scheme", url.substring(0, scheme - 3));
    if (port == -1 || port > url.indexOf("/", scheme))
      port = url.indexOf("/", scheme);
    else
      urlParts.put("port", url.substring(port, url.indexOf("/", port)));
    urlParts.put("host", url.substring(scheme, port));
    int path = url.indexOf("/", port);
    urlParts.put("path", url.substring(path, url.indexOf("?", path)));
    if (url.indexOf("?") != -1)
      urlParts.put("query", url.substring(url.indexOf("?") + 1));
    return urlParts;
  }

}
