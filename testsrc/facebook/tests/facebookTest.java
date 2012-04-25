package facebook.tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import facebook.BaseFacebook;
import facebook.Facebook;
import facebook.tests.helpers.FBCode;
import facebook.tests.helpers.FBGetCurrentURLFacebook;
import facebook.tests.helpers.HttpServletRequestMock;
import facebook.tests.helpers.TransientFacebook;

/**
 * The Class facebookTest.
 */
@SuppressWarnings({"unused","static-method"})
public class facebookTest
{

  /** The AP p_ id. */
  private final String APP_ID = "117743971608120";

  /** The SECRET. */
  private final String SECRET = "943716006e74d9b9283d4d5d8ab93204";

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
    BaseFacebook facebook = new TransientFacebook(config, new HttpServletRequestMock());
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
    BaseFacebook facebook = new TransientFacebook(config, new HttpServletRequestMock());
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
    BaseFacebook facebook = new TransientFacebook(config, new HttpServletRequestMock());
    facebook.setAppId("dummy");
    assertEquals("Expect the App ID to be dummy.", facebook.getAppId(), "dummy");
  }

  /**
   * Tests the setAPPSecret method.
   */
  @Test
  public void testSetAPPSecret()
  {
    BaseFacebook facebook = new TransientFacebook(config, new HttpServletRequestMock());
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
    BaseFacebook facebook = new TransientFacebook(config, new HttpServletRequestMock());
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
    BaseFacebook facebook = new TransientFacebook(config, new HttpServletRequestMock());
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
    // fake the request object
    HttpServletRequestMock req = new HttpServletRequestMock();
    req.setRequestString("http://www.test.com/unit-tests.php?one=one&two=two&three=three");
    FBGetCurrentURLFacebook facebook = new FBGetCurrentURLFacebook(config, req);

    String current_url = facebook.publicGetCurrentUrl();
    assertEquals("getCurrentUrl void is changing the current URL",
        "http://www.test.com/unit-tests.php?one=one&two=two&three=three",
        current_url);

    // ensure structure of valueless GET params is retained (sometimes
    // an = sign was present, and sometimes it was not)
    // first test when equal signs are present

    req.setRequestString("http://www.test.com/unit-tests.php?one=&two=&three=");
    facebook = new FBGetCurrentURLFacebook(config, req);

    current_url = facebook.publicGetCurrentUrl();
    assertEquals("getCurrentUrl void is changing the current URL",
        "http://www.test.com/unit-tests.php?one=&two=&three=", current_url);

    // then test when equal signs are not present

    req.setRequestString("http://www.test.com/unit-tests.php?one&two&three");
    facebook = new FBGetCurrentURLFacebook(config, req);
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
    // fake the request object
    HttpServletRequestMock req = new HttpServletRequestMock();
    req.setRequestString("http://www.test.com/unit-tests.php");
    Facebook facebook = new Facebook(config, req);

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

    HashMap<String, String> query_map = parse_str(login_url.get("query"));
    assertIsSubset(expected_login_params, query_map);
    // we don"t know what the state is, but we know it"s an md5 and should
    // be 32 characters long.
    assertEquals(query_map.get("state").length(), 32);
  }

  /**
   * Tests the getLoginURL method using extra params.
   */
  @Test
  public void testGetLoginURL_ExtraParams()
  {
    // fake the request object
    HttpServletRequestMock req = new HttpServletRequestMock();
    req.setRequestString("http://www.test.com/unit-tests.php");
    Facebook facebook = new Facebook(config, req);

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
    HashMap<String, String> query_map = parse_str(login_url.get("query"));
    assertIsSubset(expected_login_params, query_map);
    // we don"t know what the state is, but we know it"s an md5 and should
    // be 32 characters long.
    assertEquals(query_map.get("state").length(), 32);
  }

  /**
   * Tests the getCode method using valid csrf state.
   */
  @Test
  public void testGetCode_ValidCSRFState()
  {
    // fake the request object
    HttpServletRequestMock req = new HttpServletRequestMock();
    FBCode facebook = new FBCode(config, req);

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
    // fake the request object
    HttpServletRequestMock req = new HttpServletRequestMock();
    FBCode facebook = new FBCode(config, req);

    facebook.setCSRFStateToken();
    req.setParameter("code", TransientFacebook.md5());
    req.setParameter("state", facebook.getCSRFStateToken() + "forgery!!!");
    assertNull("Expect getCode to fail, CSRF state should not match.",
        facebook.publicGetCode());
    assertEquals("CSRF state token does not match one provided.",
        facebook.getLastError());
  }
      
      /**
       * Tests the getCodeWithMissingCSRFState method.
       */
      @Test
      public void testGetCodeWithMissingCSRFState() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new FBCode(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        $code = $_REQUEST["code"] = generateMD5HashOfRandomValue();
        // intentionally don"t set CSRF token at all
        assertFalse($facebook->publicGetCode(),
                           "Expect getCode to fail, CSRF state not sent back.");
*/
      }
      
      /**
       * Tests the getUserFromSignedRequest method.
       */
      @Test
      public void testGetUserFromSignedRequest() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        $_REQUEST["signed_request"] = self::$kValidSignedRequest;
        assertEquals("1677846385", $facebook->getUser(),
                            "Failed to get user ID from a valid signed request.");
                            */
      }
      
      /**
       * Tests the getSignedRequestFromCookie method.
       */
      @Test
      public void testGetSignedRequestFromCookie() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new FBGetSignedRequestCookieFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        $_COOKIE[$facebook->publicGetSignedRequestCookieName()] =
          self::$kValidSignedRequest;
        assertNotNull($facebook->publicGetSignedRequest());
        assertEquals("1677846385", $facebook->getUser(),
                            "Failed to get user ID from a valid signed request.");
                            */
      }
      
      /**
       * Tests the getSignedRequestWithIncorrectSignature method.
       */
      @Test
      public void testGetSignedRequestWithIncorrectSignature() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new FBGetSignedRequestCookieFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        $_COOKIE[$facebook->publicGetSignedRequestCookieName()] =
          self::$kSignedRequestWithBogusSignature;
        assertNull($facebook->publicGetSignedRequest());
        */
      }
      
      /**
       * Tests the nonUserAccessToken method.
       */
      @Test
      public void testNonUserAccessToken() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new FBAccessToken(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        // no cookies, and no request params, so no user or code,
        // so no user access token (even with cookie support)
        assertEquals($facebook->publicGetApplicationAccessToken(),
                            $facebook->getAccessToken(),
                            "Access token should be that for logged out users.");
                            */
      }
      
      /**
       * Tests the APIForLoggedOutUsers method.
       */
      @Test
      public void testAPIForLoggedOutUsers() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $response = $facebook->api(array(
          "method" => "fql.query",
          "query" => "SELECT name FROM user WHERE uid=4",
        ));
        assertEquals(count($response), 1,
                            "Expect one row back.");
        assertEquals($response[0]["name"], "Mark Zuckerberg",
                            "Expect the name back.");
                            */
      }
      
      /**
       * Tests the APIWithBogusAccessToken method.
       */
      @Test
      public void testAPIWithBogusAccessToken() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        $facebook->setAccessToken("this-is-not-really-an-access-token");
        // if we don"t set an access token and there"s no way to
        // get one, then the FQL query below works beautifully, handing
        // over Zuck"s public data.  But if you specify a bogus access
        // token as I have right here, then the FQL query should fail.
        // We could return just Zuck"s public data, but that wouldn"t
        // advertise the issue that the access token is at worst broken
        // and at best expired.
        try {
          $response = $facebook->api(array(
            "method" => "fql.query",
            "query" => "SELECT name FROM profile WHERE id=4",
          ));
          fail("Should not get here.");
        } catch(FacebookApiException $e) {
          $result = $e->getResult();
          assertTrue(is_array($result), "expect a result object");
          assertEquals("190", $result["error_code"], "expect code");
        }
        */
      }
      
      /**
       * Tests the APIGraphPublicData method.
       */
      @Test
      public void testAPIGraphPublicData() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        $response = $facebook->api("/jerry");
        assertEquals(
          $response["id"], "214707", "should get expected id.");
          */
      }
      
      /**
       * Tests the graphAPIWithBogusAccessToken method.
       */
      @Test
      public void testGraphAPIWithBogusAccessToken() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        $facebook->setAccessToken("this-is-not-really-an-access-token");
        try {
          $response = $facebook->api("/me");
          fail("Should not get here.");
        } catch(FacebookApiException $e) {
          // means the server got the access token and didn"t like it
          $msg = "OAuthException: Invalid OAuth access token.";
          assertEquals($msg, (string) $e,
                              "Expect the invalid OAuth token message.");
        }
        */
      }
      
      /**
       * Tests the graphAPIWithExpiredAccessToken method.
       */
      @Test
      public void testGraphAPIWithExpiredAccessToken() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        $facebook->setAccessToken(self::$kExpiredAccessToken);
        try {
          $response = $facebook->api("/me");
          fail("Should not get here.");
        } catch(FacebookApiException $e) {
          // means the server got the access token and didn"t like it
          $error_msg_start = "OAuthException: Error validating access token:";
          assertTrue(strpos((string) $e, $error_msg_start) === 0,
                            "Expect the token validation error message.");
        }
        */
      }
      
      /**
       * Tests the graphAPIMethod method.
       */
      @Test
      public void testGraphAPIMethod() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        try {
          // naitik being bold about deleting his entire record....
          // let"s hope this never actually passes.
          $response = $facebook->api("/naitik", $method = "DELETE");
          fail("Should not get here.");
        } catch(FacebookApiException $e) {
          // ProfileDelete means the server understood the DELETE
          $msg =
            "OAuthException: (#200) User cannot access this application";
          assertEquals($msg, (string) $e,
                              "Expect the invalid session message.");
        }
        */
      }
      
      /**
       * Tests the graphAPIOAuthSpecError method.
       */
      @Test
      public void testGraphAPIOAuthSpecError() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::MIGRATED_APP_ID,
          "secret" => self::MIGRATED_SECRET,
        ));

        try {
          $response = $facebook->api("/me", array(
            "client_id" => self::MIGRATED_APP_ID));

          fail("Should not get here.");
        } catch(FacebookApiException $e) {
          // means the server got the access token
          $msg = "invalid_request: An active access token must be used ".
                 "to query information about the current user.";
          assertEquals($msg, (string) $e,
                              "Expect the invalid session message.");
        }
        */
      }
      
      /**
       * Tests the graphAPIMethodOAuthSpecError method.
       */
      @Test
      public void testGraphAPIMethodOAuthSpecError() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::MIGRATED_APP_ID,
          "secret" => self::MIGRATED_SECRET,
        ));

        try {
          $response = $facebook->api("/daaku.shah", "DELETE", array(
            "client_id" => self::MIGRATED_APP_ID));
          fail("Should not get here.");
        } catch(FacebookApiException $e) {
          assertEquals(strpos($e, "invalid_request"), 0);
        }
        */
      }
      
      /**
       * Tests the curlFailure method.
       */
      @Test
      public void testCurlFailure() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        if (!defined("CURLOPT_TIMEOUT_MS")) {
          // can"t test it if we don"t have millisecond timeouts
          return;
        }

        $exception = null;
        try {
          // we dont expect facebook will ever return in 1ms
          Facebook::$CURL_OPTS[CURLOPT_TIMEOUT_MS] = 50;
          $facebook->api("/naitik");
        } catch(FacebookApiException $e) {
          $exception = $e;
        }
        unset(Facebook::$CURL_OPTS[CURLOPT_TIMEOUT_MS]);
        if (!$exception) {
          fail("no exception was thrown on timeout.");
        }

        assertEquals(
          CURLE_OPERATION_TIMEOUTED, $exception->getCode(), "expect timeout");
        assertEquals("CurlException", $exception->getType(), "expect type");
        */
      }
      
      /**
       * Tests the graphAPIWithOnlyParams method.
       */
      @Test
      public void testGraphAPIWithOnlyParams() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        $response = $facebook->api("/jerry");
        assertTrue(isset($response["id"]),
                          "User ID should be public.");
        assertTrue(isset($response["name"]),
                          "User\"s name should be public.");
        assertTrue(isset($response["first_name"]),
                          "User\"s first name should be public.");
        assertTrue(isset($response["last_name"]),
                          "User\"s last name should be public.");
        assertFalse(isset($response["work"]),
                           "User\"s work history should only be available with ".
                           "a valid access token.");
        assertFalse(isset($response["education"]),
                           "User\"s education history should only be ".
                           "available with a valid access token.");
        assertFalse(isset($response["verified"]),
                           "User\"s verification status should only be ".
                           "available with a valid access token.");
                           */
      }
      
      /**
       * Tests the loginURLDefaults method.
       */
      @Test
      public void testLoginURLDefaults() {
        fail("Not implemented.");
        /* TODO Translate
        $_SERVER["HTTP_HOST"] = "fbrell.com";
        $_SERVER["REQUEST_URI"] = "/examples";
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $encodedUrl = rawurlencode("http://fbrell.com/examples");
        assertNotNull(strpos($facebook->getLoginUrl(), $encodedUrl),
                             "Expect the current url to exist.");
                             */
      }
      
      /**
       * Tests the loginURLDefaultsDropStateQueryParam method.
       */
      @Test
      public void testLoginURLDefaultsDropStateQueryParam() {
        fail("Not implemented.");
        /* TODO Translate
        $_SERVER["HTTP_HOST"] = "fbrell.com";
        $_SERVER["REQUEST_URI"] = "/examples?state=xx42xx";
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $expectEncodedUrl = rawurlencode("http://fbrell.com/examples");
        assertTrue(strpos($facebook->getLoginUrl(), $expectEncodedUrl) > -1,
                          "Expect the current url to exist.");
        assertFalse(strpos($facebook->getLoginUrl(), "xx42xx"),
                           "Expect the session param to be dropped.");
                           */
      }
      
      /**
       * Tests the loginURLDefaultsDropCodeQueryParam method.
       */
      @Test
      public void testLoginURLDefaultsDropCodeQueryParam() {
        fail("Not implemented.");
        /* TODO Translate
        $_SERVER["HTTP_HOST"] = "fbrell.com";
        $_SERVER["REQUEST_URI"] = "/examples?code=xx42xx";
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $expectEncodedUrl = rawurlencode("http://fbrell.com/examples");
        assertTrue(strpos($facebook->getLoginUrl(), $expectEncodedUrl) > -1,
                          "Expect the current url to exist.");
        assertFalse(strpos($facebook->getLoginUrl(), "xx42xx"),
                           "Expect the session param to be dropped.");
                           */
      }
      
      /**
       * Tests the loginURLDefaultsDropSignedRequestParamButNotOthers method.
       */
      @Test
      public void testLoginURLDefaultsDropSignedRequestParamButNotOthers() {
        fail("Not implemented.");
        /* TODO Translate
        $_SERVER["HTTP_HOST"] = "fbrell.com";
        $_SERVER["REQUEST_URI"] =
          "/examples?signed_request=xx42xx&do_not_drop=xx43xx";
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $expectEncodedUrl = rawurlencode("http://fbrell.com/examples");
        assertFalse(strpos($facebook->getLoginUrl(), "xx42xx"),
                           "Expect the session param to be dropped.");
        assertTrue(strpos($facebook->getLoginUrl(), "xx43xx") > -1,
                          "Expect the do_not_drop param to exist.");
                          */
      }
      
      /**
       * Tests the loginURLCustomNext method.
       */
      @Test
      public void testLoginURLCustomNext() {
        fail("Not implemented.");
        /* TODO Translate
        $_SERVER["HTTP_HOST"] = "fbrell.com";
        $_SERVER["REQUEST_URI"] = "/examples";
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $next = "http://fbrell.com/custom";
        $loginUrl = $facebook->getLoginUrl(array(
          "redirect_uri" => $next,
          "cancel_url" => $next
        ));
        $currentEncodedUrl = rawurlencode("http://fbrell.com/examples");
        $expectedEncodedUrl = rawurlencode($next);
        assertNotNull(strpos($loginUrl, $expectedEncodedUrl),
                             "Expect the custom url to exist.");
        assertFalse(strpos($loginUrl, $currentEncodedUrl),
                          "Expect the current url to not exist.");
                          */
      }
      
      /**
       * Tests the logoutURLDefaults method.
       */
      @Test
      public void testLogoutURLDefaults() {
        fail("Not implemented.");
        /* TODO Translate
        $_SERVER["HTTP_HOST"] = "fbrell.com";
        $_SERVER["REQUEST_URI"] = "/examples";
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $encodedUrl = rawurlencode("http://fbrell.com/examples");
        assertNotNull(strpos($facebook->getLogoutUrl(), $encodedUrl),
                             "Expect the current url to exist.");
                             */
      }
      
      /**
       * Tests the loginStatusURLDefaults method.
       */
      @Test
      public void testLoginStatusURLDefaults() {
        fail("Not implemented.");
        /* TODO Translate
        $_SERVER["HTTP_HOST"] = "fbrell.com";
        $_SERVER["REQUEST_URI"] = "/examples";
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $encodedUrl = rawurlencode("http://fbrell.com/examples");
        assertNotNull(strpos($facebook->getLoginStatusUrl(), $encodedUrl),
                             "Expect the current url to exist.");
                             */
      }
      
      /**
       * Tests the loginStatusURLCustom method.
       */
      @Test
      public void testLoginStatusURLCustom() {
        fail("Not implemented.");
        /* TODO Translate
        $_SERVER["HTTP_HOST"] = "fbrell.com";
        $_SERVER["REQUEST_URI"] = "/examples";
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $encodedUrl1 = rawurlencode("http://fbrell.com/examples");
        $okUrl = "http://fbrell.com/here1";
        $encodedUrl2 = rawurlencode($okUrl);
        $loginStatusUrl = $facebook->getLoginStatusUrl(array(
          "ok_session" => $okUrl,
        ));
        assertNotNull(strpos($loginStatusUrl, $encodedUrl1),
                             "Expect the current url to exist.");
        assertNotNull(strpos($loginStatusUrl, $encodedUrl2),
                             "Expect the custom url to exist.");
                             */
      }
      
      /**
       * Tests the nonDefaultPort method.
       */
      @Test
      public void testNonDefaultPort() {
        fail("Not implemented.");
        /* TODO Translate
        $_SERVER["HTTP_HOST"] = "fbrell.com:8080";
        $_SERVER["REQUEST_URI"] = "/examples";
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $encodedUrl = rawurlencode("http://fbrell.com:8080/examples");
        assertNotNull(strpos($facebook->getLoginUrl(), $encodedUrl),
                             "Expect the current url to exist.");
                             */
      }
      
      /**
       * Tests the secureCurrentUrl method.
       */
      @Test
      public void testSecureCurrentUrl() {
        fail("Not implemented.");
        /* TODO Translate
        $_SERVER["HTTP_HOST"] = "fbrell.com";
        $_SERVER["REQUEST_URI"] = "/examples";
        $_SERVER["HTTPS"] = "on";
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $encodedUrl = rawurlencode("https://fbrell.com/examples");
        assertNotNull(strpos($facebook->getLoginUrl(), $encodedUrl),
                             "Expect the current url to exist.");
                             */
      }
      
      /**
       * Tests the secureCurrentUrlWithNonDefaultPort method.
       */
      @Test
      public void testSecureCurrentUrlWithNonDefaultPort() {
        fail("Not implemented.");
        /* TODO Translate
        $_SERVER["HTTP_HOST"] = "fbrell.com:8080";
        $_SERVER["REQUEST_URI"] = "/examples";
        $_SERVER["HTTPS"] = "on";
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));
        $encodedUrl = rawurlencode("https://fbrell.com:8080/examples");
        assertNotNull(strpos($facebook->getLoginUrl(), $encodedUrl),
                             "Expect the current url to exist.");
                             */
      }
      
      /**
       * Tests the appSecretCall method.
       */
      @Test
      public void testAppSecretCall() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET,
        ));

        try {
          $response = $facebook->api("/" . self::APP_ID . "/insights");
          fail("Desktop applications need a user token for insights.");
        } catch (FacebookApiException $e) {
          // this test is failing as the graph call is returning the wrong
          // error message
          assertTrue(strpos($e->getMessage(),
            "Requires session when calling from a desktop app") !== false,
            "Incorrect exception type thrown when trying to gain " .
            "insights for desktop app without a user access token.");
        } catch (Exception $e) {
          fail("Incorrect exception type thrown when trying to gain " .
            "insights for desktop app without a user access token.");
        }
        */
      }
      
      /**
       * Tests the base64UrlEncode method.
       */
      @Test
      public void testBase64UrlEncode() {
        fail("Not implemented.");
        /* TODO Translate
        $input = "Facebook rocks";
        $output = "RmFjZWJvb2sgcm9ja3M";

        assertEquals(FBPublic::publicBase64UrlDecode($output), $input);
        */
      }
      
      /**
       * Tests the signedToken method.
       */
      @Test
      public void testSignedToken() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new FBPublic(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET
        ));
        $payload = $facebook->publicParseSignedRequest(self::$kValidSignedRequest);
        assertNotNull($payload, "Expected token to parse");
        assertEquals($facebook->getSignedRequest(), null);
        $_REQUEST["signed_request"] = self::$kValidSignedRequest;
        assertEquals($facebook->getSignedRequest(), $payload);
        */
      }
      
      /**
       * Tests the nonTossedSignedtoken method.
       */
      @Test
      public void testNonTossedSignedtoken() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new FBPublic(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET
        ));
        $payload = $facebook->publicParseSignedRequest(
          self::$kNonTosedSignedRequest);
        assertNotNull($payload, "Expected token to parse");
        assertNull($facebook->getSignedRequest());
        $_REQUEST["signed_request"] = self::$kNonTosedSignedRequest;
        assertEquals($facebook->getSignedRequest(),
          array("algorithm" => "HMAC-SHA256"));
          */
      }
      
      /**
       * Tests the bundledCACert method.
       */
      @Test
      public void testBundledCACert() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new TransientFacebook(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET
        ));

          // use the bundled cert from the start
        Facebook::$CURL_OPTS[CURLOPT_CAINFO] =
          dirname(__FILE__) . "/../src/fb_ca_chain_bundle.crt";
        $response = $facebook->api("/naitik");

        unset(Facebook::$CURL_OPTS[CURLOPT_CAINFO]);
        assertEquals(
          $response["id"], "5526183", "should get expected id.");
          */
      }
      
      /**
       * Tests the videoUpload method.
       */
      @Test
      public void testVideoUpload() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new FBRecordURL(array(
          "appId"  => self::APP_ID,
          "secret" => self::SECRET
        ));

        $facebook->api(array("method" => "video.upload"));
        assertContains("//api-video.", $facebook->getRequestedURL(),
                              "video.upload should go against api-video");
                              */
      }
      
      /**
       * Tests the getUserAndAccessTokenFromSession method.
       */
      @Test
      public void testGetUserAndAccessTokenFromSession() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new PersistentFBPublic(array(
                                             "appId"  => self::APP_ID,
                                             "secret" => self::SECRET
                                           ));

        $facebook->publicSetPersistentData("access_token",
                                           self::$kExpiredAccessToken);
        $facebook->publicSetPersistentData("user_id", 12345);
        assertEquals(self::$kExpiredAccessToken,
                            $facebook->getAccessToken(),
                            "Get access token from persistent store.");
        assertEquals("12345",
                            $facebook->getUser(),
                            "Get user id from persistent store.");
                            */
      }
      
      /**
       * Tests the getUserAndAccessTokenFromSignedRequestNotSession method.
       */
      @Test
      public void testGetUserAndAccessTokenFromSignedRequestNotSession() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new PersistentFBPublic(array(
                                             "appId"  => self::APP_ID,
                                             "secret" => self::SECRET
                                           ));

        $_REQUEST["signed_request"] = self::$kValidSignedRequest;
        $facebook->publicSetPersistentData("user_id", 41572);
        $facebook->publicSetPersistentData("access_token",
                                           self::$kExpiredAccessToken);
        assertNotEquals("41572", $facebook->getUser(),
                               "Got user from session instead of signed request.");
        assertEquals("1677846385", $facebook->getUser(),
                            "Failed to get correct user ID from signed request.");
        assertNotEquals(
          self::$kExpiredAccessToken,
          $facebook->getAccessToken(),
          "Got access token from session instead of signed request.");
        assertNotEmpty(
          $facebook->getAccessToken(),
          "Failed to extract an access token from the signed request.");
          */
      }
      
      /**
       * Tests the getUserWithoutCodeOrSignedRequestOrSession method.
       */
      @Test
      public void testGetUserWithoutCodeOrSignedRequestOrSession() {
        fail("Not implemented.");
        /* TODO Translate
        $facebook = new PersistentFBPublic(array(
                                             "appId"  => self::APP_ID,
                                             "secret" => self::SECRET
                                           ));

        // deliberately leave $_REQUEST and _$SESSION empty
        assertEmpty($_REQUEST,
                           "GET, POST, and COOKIE params exist even though ".
                           "they should.  Test cannot succeed unless all of ".
                           "$_REQUEST is empty.");
        assertEmpty($_SESSION,
                           "Session is carrying state and should not be.");
        assertEmpty($facebook->getUser(),
                           "Got a user id, even without a signed request, ".
                           "access token, or session variable.");
        assertEmpty($_SESSION,
                           "Session superglobal incorrectly populated by getUser.");
                           */
      }

      /**
       * Sets up.
       */
      @Before
      public void setUp() {
        try
        {
          config = new JSONObject("{\"appId\": \""+APP_ID+"\",\"secret\": \""+SECRET+"\"}");
        } catch (JSONException e)
        {
          e.printStackTrace();
        }
      }

      /**
       * Tear down.
       */
      protected void tearDown() {
        /* TODO Translate
        clearSuperGlobals();
        parent::tearDown();
        */
      }

      /**
       * Clear super globals.
       */
      protected void clearSuperGlobals() {
        /* TODO Translate
        unset($_SERVER["HTTPS"]);
        unset($_SERVER["HTTP_HOST"]);
        unset($_SERVER["REQUEST_URI"]);
        $_SESSION = array();
        $_COOKIE = array();
        $_REQUEST = array();
        $_POST = array();
        $_GET = array();
        if (session_id()) {
          session_destroy();
        }
        */
      }

      /**
       * Checks that the correct args are a subset of the returned obj.
       * 
       * @param correct
       *          the correct
       * @param actual
       *          the actual
       */
      protected void assertIsSubset(HashMap<String, String> correct, HashMap<String, String> actual) {
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
      protected void assertIsSubset(HashMap<String, String> correct, HashMap<String, String> actual, String msg) {
        for(String key : correct.keySet()) {
          String actual_value = actual.get(key);
          String newMsg = msg.length() != 0 ? msg + " " : "" + "Key: " + key;
          assertEquals(newMsg, correct.get(key), actual_value);
        }
      }
      
      protected HashMap<String,String> parse_url(String url)
      {
        HashMap<String, String> urlParts = new HashMap<String, String>();
        int scheme = url.indexOf("://") + 3;
        int port = url.indexOf(":", scheme);
        urlParts.put("scheme", url.substring(0, scheme-3));
        if(port == -1 || port > url.indexOf("/", scheme))
          port = url.indexOf("/", scheme);
        else
          urlParts.put("port", url.substring(port, url.indexOf("/", port)));
        urlParts.put("host", url.substring(scheme, port));
        int path = url.indexOf("/", port);
        urlParts.put("path", url.substring(path, url.indexOf("?", path)));
        if(url.indexOf("?") != -1)
          urlParts.put("query", url.substring(url.indexOf("?")+1));
        return urlParts;
      }
      
      protected HashMap<String, String> parse_str(String query)
      {
        HashMap<String, String> params = new HashMap<String, String>();
        for(String param : query.split("&"))
        {
          String[] keyVal = param.split("=");
          String value = keyVal.length > 1 ? keyVal[1] : "";
          params.put(keyVal[0], value);
        }
        return params;
      }
      
}