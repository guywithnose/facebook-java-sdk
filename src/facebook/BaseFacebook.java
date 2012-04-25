/*
 * File:         BaseFacebook.java
 * Author:       Robert Bittle <guywithnose@gmail.com>
 */
package facebook;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import facebook.tests.helpers.HttpServletRequestMock;

/**
 * Provides access to the Facebook Platform. This class provides a majority of
 * the functionality needed, but the class is abstract because it is designed to
 * be sub-classed. The subclass must implement the four abstract methods listed
 * at the bottom of the file.
 */
@SuppressWarnings({ "static-method", "unused" })
abstract public class BaseFacebook
{

  /**
   * Version.
   */
  public static final String VERSION = "3.1.1";

  /**
   * List of query parameters that get automatically dropped when rebuilding the
   * current URL.
   */
  protected static String[] DROP_QUERY_PARAMS = new String[] { "code", "state",
      "signed_request", };

  /**
   * Maps aliases to Facebook domains.
   */
  public static HashMap<String, String> DOMAIN_MAP = new HashMap<String, String>()
  {
    {
      put("api", "https://api.facebook.com/");
      put("api_video", "https://api-video.facebook.com/");
      put("api_read", "https://api-read.facebook.com/");
      put("graph", "https://graph.facebook.com/");
      put("graph_video", "https://graph-video.facebook.com/");
      put("www", "https://www.facebook.com/");
    }
  };

  /**
   * The Application ID.
   * 
   * @var string
   */
  protected String appId;

  /**
   * The Application App Secret.
   * 
   * @var string
   */
  protected String appSecret;

  /**
   * The ID of the Facebook user, or 0 if the user is logged out.
   * 
   * @var integer
   */
  protected long user;

  /**
   * The data from the signed_request token.
   */
  protected String signedRequest;

  /**
   * A CSRF state variable to assist in the defense against CSRF attacks.
   */
  protected String state;

  /**
   * The OAuth access token received in exchange for a valid authorization code.
   * null means the access token has yet to be determined.
   * 
   * @var string
   */
  protected String accessToken = null;

  /**
   * Indicates if the CURL based @ syntax for file uploads is enabled.
   * 
   * @var boolean
   */
  protected boolean fileUploadSupport = false;

  protected HttpServletRequest req;
  
  /**
   * Initialize a Facebook Application.
   * 
   * The configuration: - appId: the application ID - secret: the application
   * secret - fileUpload: (optional) boolean indicating if file uploads are
   * enabled
   * 
   * @param config
   *          the config
   * @param Req
   *          the req
   */
  public BaseFacebook(JSONObject config, HttpServletRequest Req)
  {
    initialize(config, Req);
  }
  
  /**
   * Initialize.
   * 
   * @param config
   *          the config
   * @param Req
   *          the req
   */
  protected void initialize(JSONObject config, HttpServletRequest Req)
  {
    req = Req;
    try
    {
      setAppId(config.getString("appId"));
      setAppSecret(config.getString("secret"));
      if (config.has("fileUpload"))
      {
        setFileUploadSupport(config.getBoolean("fileUpload"));
      }

      state = getPersistentData("state");
      if (state != null)
      {
        state = getPersistentData("state");
      }
    } catch (JSONException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Set the Application ID.
   * 
   * @param AppId
   *          the app id
   * @return BaseFacebook
   */
  public BaseFacebook setAppId(String AppId)
  {
    appId = AppId;
    return this;
  }

  /**
   * Get the Application ID.
   * 
   * @return string the Application ID
   */
  public String getAppId()
  {
    return appId;
  }

  /**
   * Set the App Secret.
   * 
   * @param AppSecret
   *          the app secret
   * @return BaseFacebook
   */
  public BaseFacebook setAppSecret(String AppSecret)
  {
    appSecret = AppSecret;
    return this;
  }

  /**
   * Get the App Secret.
   * 
   * @return string the App Secret
   */
  public String getAppSecret()
  {
    return appSecret;
  }

  /**
   * Set the file upload support status.
   * 
   * @param FileUploadSupport
   *          the file upload support
   * @return BaseFacebook
   */
  public BaseFacebook setFileUploadSupport(boolean FileUploadSupport)
  {
    fileUploadSupport = FileUploadSupport;
    return this;
  }

  /**
   * Get the file upload support status.
   * 
   * @return boolean true if and only if the server supports file upload.
   */
  public boolean getFileUploadSupport()
  {
    return fileUploadSupport;
  }

  /**
   * Sets the access token for api calls. Use this if you get your access token
   * by other means and just want the SDK to use it.
   * 
   * @param Access_token
   *          the access_token
   * @return BaseFacebook
   */
  public BaseFacebook setAccessToken(String Access_token)
  {
    accessToken = Access_token;
    return this;
  }

  /**
   * Determines the access token that should be used for API calls. The first
   * time this is called, accessToken is set equal to either a valid user access
   * token, or it's set to the application access token if a valid user access
   * token wasn't available. Subsequent calls return whatever the first call
   * returned.
   * 
   * @return string The access token
   */
  public String getAccessToken()
  {
    if (accessToken != null)
    {
      // we've done this already and cached it. Just return.
      return accessToken;
    }

    // first establish access token to be the application
    // access token, in case we navigate to the /oauth/access_token
    // endpoint, where SOME access token is required.
    setAccessToken(getApplicationAccessToken());
    String user_access_token = getUserAccessToken();
    if (user_access_token != null)
    {
      setAccessToken(user_access_token);
    }

    return accessToken;
  }

  /**
   * Determines and returns the user access token, first using the signed
   * request if present, and then falling back on the authorization code if
   * present. The intent is to return a valid user access token, or false if one
   * is determined to not be available.
   * 
   * @return string A valid user access token, or false if one could not be
   *         determined.
   */
  protected String getUserAccessToken()
  {
    /*
     * TODO Translate // first, consider a signed request if it's supplied. //
     * if there is a signed request, then it alone determines // the access
     * token. String code; JSONObject signed_request = getSignedRequest(); if
     * (signed_request != null) { // apps.facebook.com hands the access_token in
     * the signed_request if (signed_request.has("oauth_token")) { access_token
     * = signed_request.getString("oauth_token");
     * setPersistentData("access_token", access_token); return access_token; }
     * 
     * // the JS SDK puts a code in with the redirect_uri of '' if
     * (signed_request.has("code")){ code = signed_request.getString("code");
     * access_token = getAccessTokenFromCode(code, ""); if (access_token) {
     * setPersistentData("code", code); setPersistentData("access_token",
     * access_token); return access_token; } }
     * 
     * // signed request states there's no access token, so anything // stored
     * should be cleared. clearAllPersistentData(); return null; // respect the
     * signed request's data, even // if there's an authorization code or
     * something else }
     * 
     * code = getCode(); if (code != null && code != getPersistentData("code"))
     * { access_token = getAccessTokenFromCode(code); if (access_token) {
     * setPersistentData("code", code); setPersistentData("access_token",
     * access_token); return access_token; }
     * 
     * // code was bogus, so everything based on it should be invalidated.
     * clearAllPersistentData(); return false; }
     * 
     * // as a fallback, just return whatever is in the persistent // store,
     * knowing nothing explicit (signed request, authorization // code, etc.)
     * was present to shadow it (or we saw a code in $_REQUEST, // but it's the
     * same as what's in the persistent store) return
     * getPersistentData("access_token");
     */
    return null;
  }

  /**
   * Retrieve the signed request, either from a request parameter or, if not
   * present, from a cookie.
   * 
   * @return string the signed request, if available, or null otherwise.
   */
  public JSONObject getSignedRequest()
  {
    /*
     * TODO Translate if (!signedRequest) { if
     * (isset($_REQUEST['signed_request'])) { signedRequest =
     * parseSignedRequest( $_REQUEST['signed_request']); } else if
     * (isset($_COOKIE[getSignedRequestCookieName()])) { signedRequest =
     * parseSignedRequest( $_COOKIE[getSignedRequestCookieName()]); } } return
     * signedRequest;
     */
    return null;
  }

  /**
   * Get the UID of the connected user, or 0 if the Facebook user is not
   * connected.
   * 
   * @return string the UID if available.
   */
  public long getUser()
  {
    if (user != 0)
    {
      // we've already determined this and cached the value.
      return user;
    }

    return user = getUserFromAvailableData();
  }

  /**
   * Determines the connected user by first examining any signed requests, then
   * considering an authorization code, and then falling back to any persistent
   * store storing the user.
   * 
   * @return integer The id of the connected Facebook user, or 0 if no such user
   *         exists.
   */
  protected long getUserFromAvailableData()
  {
    /*
     * TODO Translate // if a signed request is supplied, then it solely
     * determines // who the user is. $signed_request = getSignedRequest(); if
     * ($signed_request) { if (array_key_exists("user_id", signed_request)) {
     * $user = $signed_request["user_id"]; setPersistentData("user_id",
     * $signed_request["user_id"]); return $user; }
     * 
     * // if the signed request didn't present a user id, then invalidate // all
     * entries in any persistent store. clearAllPersistentData(); return 0; }
     * 
     * $user = getPersistentData("user_id", $default = 0);
     * $persisted_access_token = getPersistentData("access_token");
     * 
     * // use access_token to fetch user id if we have a user access_token, or
     * if // the cached access token has changed. $access_token =
     * getAccessToken(); if ($access_token && $access_token !=
     * getApplicationAccessToken() && !($user && $persisted_access_token ==
     * $access_token)) { $user = getUserFromAccessToken(); if ($user) {
     * setPersistentData('user_id', $user); } else { clearAllPersistentData(); }
     * }
     * 
     * return $user;
     */
    return 0;
  }

  /**
   * Get a Login URL for use with redirects. By default, full page redirect is
   * assumed. If you are using the generated URL with a window.open() call in
   * JavaScript, you can pass in display=popup as part of the $params.
   * 
   * The parameters: - redirect_uri: the url to go to after a successful login -
   * scope: comma separated list of requested extended perms
   * 
   * @return string The URL for the login flow
   */
  public String getLoginUrl()
  {
    return getLoginUrl(new HashMap<String, String>());
  }

  /**
   * Get a Login URL for use with redirects. By default, full page redirect is
   * assumed. If you are using the generated URL with a window.open() call in
   * JavaScript, you can pass in display=popup as part of the $params.
   * 
   * The parameters: - redirect_uri: the url to go to after a successful login -
   * scope: comma separated list of requested extended perms
   * 
   * @param params
   *          the params
   * @return string The URL for the login flow
   */
  public String getLoginUrl(HashMap<String, String> params)
  {
    establishCSRFTokenState();
    String currentUrl = getCurrentUrl();

    params.put("client_id", getAppId());
    params.put("redirect_uri", currentUrl);
    params.put("state", state);

    return getUrl("www", "dialog/oauth", params);
  }

  /**
   * Get a Logout URL suitable for use with redirects.
   * 
   * The parameters: - next: the url to go to after a successful logout
   * 
   * @return string The URL for the logout flow
   */
  public String getLogoutUrl()
  {
    return getLogoutUrl(new HashMap<String, String>());
  }

  /**
   * Get a Logout URL suitable for use with redirects.
   * 
   * The parameters: - next: the url to go to after a successful logout
   * 
   * @param params
   *          the params
   * @return string The URL for the logout flow
   */
  public String getLogoutUrl(HashMap<String, String> params)
  {
    /*
     * TODO Translate return getUrl( 'www', 'logout.php', array_merge(array(
     * 'next' => getCurrentUrl(), 'access_token' => getAccessToken(), ),
     * $params) );
     */
    return null;
  }

  /**
   * Get a login status URL to fetch the status from Facebook.
   * 
   * The parameters: - ok_session: the URL to go to if a session is found -
   * no_session: the URL to go to if the user is not connected - no_user: the
   * URL to go to if the user is not signed into facebook
   * 
   * @return string The URL for the logout flow
   */
  public String getLoginStatusUrl()
  {
    return getLoginStatusUrl(new HashMap<String, String>());
  }

  /**
   * Get a login status URL to fetch the status from Facebook.
   * 
   * The parameters: - ok_session: the URL to go to if a session is found -
   * no_session: the URL to go to if the user is not connected - no_user: the
   * URL to go to if the user is not signed into facebook
   * 
   * @param params
   *          the params
   * @return string The URL for the logout flow
   */
  public String getLoginStatusUrl(HashMap<String, String> params)
  {
    params.put("api_key", getAppId());
    params.put("no_user", getCurrentUrl());
    params.put("no_session", getCurrentUrl());
    params.put("ok_session", getCurrentUrl());
    params.put("session_version", "3");
    return getUrl( "www", "extern/login_status.php", params);
  }

  /**
   * Make an API call.
   * 
   * @return mixed The decoded response
   */
  public Object api(/* polymorphic */)
  {
    /*
     * TODO Translate $args = func_get_args(); if (is_array($args[0])) { return
     * _restserver($args[0]); } else { return call_user_func_array(array($this,
     * '_graph'), $args); }
     */
    return null;
  }

  /**
   * Constructs and returns the name of the cookie that potentially houses the
   * signed request for the app user. The cookie is not set by the BaseFacebook
   * class, but it may be set by the JavaScript SDK.
   * 
   * @return string the name of the cookie that would house the signed request
   *         value.
   */
  protected String getSignedRequestCookieName()
  {
    return "fbsr_" + getAppId();
  }

  /**
   * Constructs and returns the name of the coookie that potentially contain
   * metadata. The cookie is not set by the BaseFacebook class, but it may be
   * set by the JavaScript SDK.
   * 
   * @return string the name of the cookie that would house metadata.
   */
  protected String getMetadataCookieName()
  {
    return "fbm_" + getAppId();
  }

  /**
   * Get the authorization code from the query parameters, if it exists, and
   * otherwise return false to signal no authorization code was discoverable.
   * 
   * @return mixed The authorization code, or false if the authorization code
   *         could not be determined.
   */
  protected String getCode()
  {
    if (req.getParameter("code") != null)
    {
      if (state != null && req.getParameter("state") == state)
      {
        state = null;
        clearPersistentData("state");
        return req.getParameter("code");
      }
      errorLog("CSRF state token does not match one provided.");
      return null;
    }
    return null;
  }

  /**
   * Retrieves the UID with the understanding that accessToken has already been
   * set and is seemingly legitimate. It relies on Facebook's Graph API to
   * retrieve user information and then extract the user ID.
   * 
   * @return integer Returns the UID of the Facebook user, or 0 if the Facebook
   *         user could not be determined.
   */
  protected long getUserFromAccessToken()
  {
    /*
     * TODO Translate try { $user_info = api('/me'); return $user_info['id']; }
     * catch (FacebookApiException $e) { return 0; }
     */
    return 0;
  }

  /**
   * Returns the access token that should be used for logged out users when no
   * authorization code is available.
   * 
   * @return string The application access token, useful for gathering public
   *         information about users and applications.
   */
  protected String getApplicationAccessToken()
  {
    return appId + '|' + appSecret;
  }

  /**
   * Lays down a CSRF state token for this process.
   */
  protected void establishCSRFTokenState()
  {
    if (state == null)
    {
      state = md5();
      setPersistentData("state", state);
    }
  }

  /**
   * Md5.
   * 
   * @return the string
   */
  protected static String md5()
  {
    try
    {
      String s = String.valueOf(Math.random());
      MessageDigest m;
      m = MessageDigest.getInstance("MD5");
      m.update(s.getBytes(), 0, s.length());
      String MD5 = new BigInteger(1, m.digest()).toString(16);
      while (MD5.length() < 32)
      {
        MD5 = "f" + MD5;
      }
      return MD5;
    } catch (Exception e)
    {
      return "";
    }
  }

  /**
   * Retrieves an access token for the given authorization code (previously
   * generated from www.facebook.com on behalf of a specific user). The
   * authorization code is sent to graph.facebook.com and a legitimate access
   * token is generated provided the access token and the user for which it was
   * generated all match, and the user is either logged in to Facebook or has
   * granted an offline access permission.
   * 
   * @param code
   *          the code
   * @return mixed An access token exchanged for the authorization code, or
   *         false if an access token could not be generated.
   */
  protected String getAccessTokenFromCode(String code)
  {
    return getAccessTokenFromCode(code, null);
  }

  /**
   * Retrieves an access token for the given authorization code (previously
   * generated from www.facebook.com on behalf of a specific user). The
   * authorization code is sent to graph.facebook.com and a legitimate access
   * token is generated provided the access token and the user for which it was
   * generated all match, and the user is either logged in to Facebook or has
   * granted an offline access permission.
   * 
   * @param code
   *          the code
   * @param redirect_uri
   *          the redirect_uri
   * @return mixed An access token exchanged for the authorization code, or
   *         false if an access token could not be generated.
   */
  protected String getAccessTokenFromCode(String code, String redirect_uri)
  {
    /*
     * TODO Translate if (empty($code)) { return false; }
     * 
     * if ($redirect_uri === null) { $redirect_uri = getCurrentUrl(); }
     * 
     * try { // need to circumvent json_decode by calling _oauthRequest //
     * directly, since response isn't JSON format. $access_token_response =
     * _oauthRequest( getUrl("graph", "/oauth/access_token"), $params =
     * array("client_id" => getAppId(), "client_secret" => getAppSecret(),
     * "redirect_uri" => $redirect_uri, "code" => $code)); } catch
     * (FacebookApiException $e) { // most likely that user very recently
     * revoked authorization. // In any event, we don't have an access token, so
     * say so. return false; }
     * 
     * if (empty($access_token_response)) { return false; }
     * 
     * $response_params = array(); parse_str($access_token_response,
     * $response_params); if (!isset($response_params['access_token'])) { return
     * false; }
     * 
     * return $response_params['access_token'];
     */
    return null;
  }

  /**
   * Invoke the old restserver.php endpoint.
   * 
   * @param params
   *          the params
   * @return mixed The decoded response object
   */
  protected JSONObject _restserver(HashMap<String, String> params)
  {
    /*
     * TODO Translate // generic application level parameters $params['api_key']
     * = getAppId(); $params['format'] = 'json-strings';
     * 
     * $result = json_decode(_oauthRequest( getApiUrl($params['method']),
     * $params ), true);
     * 
     * // results are returned, errors are thrown if (is_array($result) &&
     * isset($result['error_code'])) { throwAPIException($result); }
     * 
     * if ($params['method'] === 'auth.expireSession' || $params['method'] ===
     * 'auth.revokeAuthorization') { destroySession(); }
     * 
     * return $result;
     */
    return null;
  }

  /**
   * Return true if this is video post.
   * 
   * @param path
   *          the path
   * @return boolean true if this is video post
   */
  protected boolean isVideoPost(String path)
  {
    return isVideoPost(path, "GET");
  }

  /**
   * Return true if this is video post.
   * 
   * @param path
   *          the path
   * @param method
   *          the method
   * @return boolean true if this is video post
   */
  protected boolean isVideoPost(String path, String method)
  {
    /*
     * TODO Translate if ($method == 'POST' &&
     * preg_match("/^(\\/)(.+)(\\/)(videos)$/", $path)) { return true; }
     */
    return false;
  }

  /**
   * Invoke the Graph API.
   * 
   * @param path
   *          the path
   * @return mixed The decoded response object
   */
  protected JSONObject _graph(String path)
  {
    return _graph(path, "GET");
  }

  /**
   * Invoke the Graph API.
   * 
   * @param path
   *          the path
   * @param method
   *          the method
   * @return mixed The decoded response object
   */
  protected JSONObject _graph(String path, String method)
  {
    return _graph(path, method, new HashMap<String, String>());
  }

  /**
   * Invoke the Graph API.
   * 
   * @param path
   *          the path
   * @param method
   *          the method
   * @param params
   *          the params
   * @return mixed The decoded response object
   */
  protected JSONObject _graph(String path, String method,
      HashMap<String, String> params)
  {
    /*
     * TODO Translate if (is_array($method) && empty($params)) { $params =
     * $method; $method = 'GET'; } $params["method"] = $method; // method
     * override as we always do a POST
     * 
     * if (isVideoPost($path, $method)) { $domainKey = "graph_video"; } else {
     * $domainKey = "graph"; }
     * 
     * $result = json_decode(_oauthRequest( getUrl($domainKey, $path), $params
     * ), true);
     * 
     * // results are returned, errors are thrown if (is_array($result) &&
     * isset($result['error'])) { throwAPIException($result); }
     * 
     * return $result;
     */
    return null;
  }

  /**
   * Make a OAuth Request.
   * 
   * @param url
   *          the url
   * @param params
   *          the params
   * @return string The decoded response object
   */
  protected String _oauthRequest(String url, String params)
  {
    /*
     * TODO Translate if (!isset($params['access_token'])) {
     * $params['access_token'] = getAccessToken(); }
     * 
     * // json_encode all params values that are not strings foreach ($params as
     * $key => $value) { if (!is_string($value)) { $params[$key] =
     * json_encode($value); } }
     * 
     * return makeRequest($url, $params);
     */
    return null;
  }

  /**
   * Makes an HTTP request. This method can be overridden by subclasses if
   * developers want to do fancier things or use something other than curl to
   * make the request.
   * 
   * @param url
   *          the url
   * @param params
   *          the params
   * @return string The response text
   */
  protected String makeRequest(String url, String params)
  {
    JavaCurl.getUrl(url);
    /*
     * 
     * public static $CURL_OPTS = array( CURLOPT_CONNECTTIMEOUT => 10,
     * CURLOPT_RETURNTRANSFER => true, CURLOPT_TIMEOUT => 60, CURLOPT_USERAGENT
     * => 'facebook-php-3.1', );
     * 
     * $opts = self::$CURL_OPTS; if (getFileUploadSupport()) {
     * $opts[CURLOPT_POSTFIELDS] = $params; } else { $opts[CURLOPT_POSTFIELDS] =
     * http_build_query($params, null, '&'); } $opts[CURLOPT_URL] = $url;
     * 
     * // disable the 'Expect: 100-continue' behaviour. This causes CURL to wait
     * // for 2 seconds if the server does not support this header. if
     * (isset($opts[CURLOPT_HTTPHEADER])) { $existing_headers =
     * $opts[CURLOPT_HTTPHEADER]; $existing_headers[] = 'Expect:';
     * $opts[CURLOPT_HTTPHEADER] = $existing_headers; } else {
     * $opts[CURLOPT_HTTPHEADER] = array('Expect:'); }
     * 
     * curl_setopt_array($ch, $opts); $result = curl_exec($ch);
     * 
     * if (curl_errno($ch) == 60) { // CURLE_SSL_CACERT self::errorLog('Invalid
     * or no certificate authority found, '. 'using bundled information');
     * curl_setopt($ch, CURLOPT_CAINFO, dirname(__FILE__) .
     * '/fb_ca_chain_bundle.crt'); $result = curl_exec($ch); }
     * 
     * if ($result === false) { $e = new FacebookApiException(array(
     * 'error_code' => curl_errno($ch), 'error' => array( 'message' =>
     * curl_error($ch), 'type' => 'CurlException', ), )); curl_close($ch); throw
     * $e; } curl_close($ch); return $result;
     */
    return null;
  }

  /**
   * Parses a signed_request and validates the signature.
   * 
   * @param signed_request
   *          the signed_request
   * @return array The payload inside it or null if the sig is wrong
   */
  protected JSONObject parseSignedRequest(String signed_request)
  {
    /*
     * TODO Translate list($encoded_sig, $payload) = explode('.',
     * $signed_request, 2);
     * 
     * // decode the data $sig = self::base64UrlDecode($encoded_sig); $data =
     * json_decode(self::base64UrlDecode($payload), true);
     * 
     * if (strtoupper($data['algorithm']) !== 'HMAC-SHA256') {
     * self::errorLog('Unknown algorithm. Expected HMAC-SHA256'); return null; }
     * 
     * // check sig $expected_sig = hash_hmac('sha256', $payload,
     * getAppSecret(), $raw = true); if ($sig !== $expected_sig) {
     * self::errorLog('Bad Signed JSON signature!'); return null; }
     * 
     * return $data;
     */
    return null;
  }

  /**
   * Build the URL for api given parameters.
   * 
   * @param method
   *          the method
   * @return string The URL for the given parameters
   */
  protected String getApiUrl(String method)
  {
    HashSet<String> READ_ONLY_CALLS = new HashSet<String>()
    {
      {
        add("admin.getallocation");
        add("admin.getappproperties");
        add("admin.getbannedusers");
        add("admin.getlivestreamvialink");
        add("admin.getmetrics");
        add("admin.getrestrictioninfo");
        add("application.getpublicinfo");
        add("auth.getapppublickey");
        add("auth.getsession");
        add("auth.getsignedpublicsessiondata");
        add("comments.get");
        add("connect.getunconnectedfriendscount");
        add("dashboard.getactivity");
        add("dashboard.getcount");
        add("dashboard.getglobalnews");
        add("dashboard.getnews");
        add("dashboard.multigetcount");
        add("dashboard.multigetnews");
        add("data.getcookies");
        add("events.get");
        add("events.getmembers");
        add("fbml.getcustomtags");
        add("feed.getappfriendstories");
        add("feed.getregisteredtemplatebundlebyid");
        add("feed.getregisteredtemplatebundles");
        add("fql.multiquery");
        add("fql.query");
        add("friends.arefriends");
        add("friends.get");
        add("friends.getappusers");
        add("friends.getlists");
        add("friends.getmutualfriends");
        add("gifts.get");
        add("groups.get");
        add("groups.getmembers");
        add("intl.gettranslations");
        add("links.get");
        add("notes.get");
        add("notifications.get");
        add("pages.getinfo");
        add("pages.isadmin");
        add("pages.isappadded");
        add("pages.isfan");
        add("permissions.checkavailableapiaccess");
        add("permissions.checkgrantedapiaccess");
        add("photos.get");
        add("photos.getalbums");
        add("photos.gettags");
        add("profile.getinfo");
        add("profile.getinfooptions");
        add("stream.get");
        add("stream.getcomments");
        add("stream.getfilters");
        add("users.getinfo");
        add("users.getloggedinuser");
        add("users.getstandardinfo");
        add("users.hasapppermission");
        add("users.isappuser");
        add("users.isverified");
        add("video.getuploadlimits");
      }
    };

    String name = "api";
    if (READ_ONLY_CALLS.contains(method.toLowerCase()))
    {
      name = "api_read";
    } else if ("video.upload".equals(method.toLowerCase()))
    {
      name = "api_video";
    }
    return getUrl(name, "restserver.php");
  }

  /**
   * Build the URL for given domain alias, path and parameters.
   * 
   * @param name
   *          the name
   * @return string The URL for the given parameters
   */
  protected String getUrl(String name)
  {
    return getUrl(name, "");
  }

  /**
   * Build the URL for given domain alias, path and parameters.
   * 
   * @param name
   *          the name
   * @param path
   *          the path
   * @return string The URL for the given parameters
   */
  protected String getUrl(String name, String path)
  {
    return getUrl(name, path, new HashMap<String, String>());
  }

  /**
   * Build the URL for given domain alias, path and parameters.
   * 
   * @param name
   *          the name
   * @param path
   *          the path
   * @param params
   *          the params
   * @return string The URL for the given parameters
   */
  protected String getUrl(String name, String path,
      HashMap<String, String> params)
  {
    String url = DOMAIN_MAP.get(name);
    if (path.length() > 0)
    {
      if (path.charAt(0) == '/')
      {
        path = path.substring(1);
      }
      url += path;
    }
    if (params.size() > 0)
    {
      url += '?' + http_build_query(params);
    }

    return url;
  }

  private String http_build_query(HashMap<String, String> params)
  {
    StringBuilder query = new StringBuilder();
    for (String key : params.keySet())
    {
      query.append(key);
      query.append("=");
      query.append(params.get(key));
      query.append("&");
    }
    query.deleteCharAt(query.length() - 1);
    return query.toString();
  }

  /**
   * Returns the Current URL, stripping it of known FB parameters that should
   * not persist.
   * 
   * @return string The current URL
   */
  protected String getCurrentUrl()
  {
    String currentUrl = req.getRequestURL().toString();
    String query = req.getQueryString();
    if (query != null)
    { // drop known fb params
      String[] params = query.split("&");
      ArrayList<String> retained_params = new ArrayList<String>();
      for (String param : params)
      {
        if (shouldRetainParam(param))
        {
          retained_params.add(param);
        }
      }

      if (retained_params.size() > 0)
      {
        query = "?";
        StringBuilder queryBuilder = new StringBuilder();
        for (String param : retained_params)
        {
          queryBuilder.append(param);
          queryBuilder.append("&");
        }
        queryBuilder.deleteCharAt(queryBuilder.length()-1);
        query += queryBuilder.toString();
      }
    }

    // use port if non default
    int port = req.getServerPort();
    if ((port == 80 && "http".equals(req.getProtocol()))
        || (port == 443 && "https".equals(req.getProtocol())))
    {
      port = -1;
    }

    return req.getProtocol() + "://" + req.getServerName() + (port != -1 ? port : "")
        + req.getRequestURI() + (query != null ?query:"");
  }

  /**
   * Returns true if and only if the key or key/value pair should be retained as
   * part of the query string. This amounts to a brute-force search of the very
   * small list of Facebook-specific params that should be stripped out.
   * 
   * @param param
   *          the param
   * @return boolean
   */
  protected boolean shouldRetainParam(String param)
  {
    for (String drop_query_param : DROP_QUERY_PARAMS)
    {
      if (param.indexOf(drop_query_param + "=") == 0)
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Analyzes the supplied result to see if it was thrown because the access
   * token is no longer valid. If that is the case, then we destroy the session.
   * 
   * @param result
   *          the result
   */
  protected void throwAPIException(JSONObject result)
  {
    /*
     * TODO Translate $e = new FacebookApiException($result); switch
     * ($e->getType()) { // OAuth 2.0 Draft 00 style case 'OAuthException': //
     * OAuth 2.0 Draft 10 style case 'invalid_token': // REST server errors are
     * just Exceptions case 'Exception': $message = $e->getMessage(); if
     * ((strpos($message, 'Error validating access token') !== false) ||
     * (strpos($message, 'Invalid OAuth access token') !== false) ||
     * (strpos($message, 'An active access token must be used') !== false) ) {
     * destroySession(); } break; }
     * 
     * throw $e;
     */
  }

  /**
   * Prints to the error log if you aren't in command line mode.
   * 
   * @param msg
   *          the msg
   */
  protected void errorLog(String msg)
  {
    System.err.println(msg);
  }

  /**
   * Base64 encoding that doesn't need to be urlencode()ed. Exactly the same as
   * base64_encode except it uses - instead of + _ instead of /
   * 
   * @param input
   *          the input
   * @return string
   */
  protected static String base64UrlDecode(String input)
  {
    /*
     * TODO Translate return base64_decode(strtr($input, '-_', '+/'));
     */
    return null;
  }

  /**
   * Destroy the current session.
   */
  public void destroySession()
  {
    /*
     * TODO Translate accessToken = null; signedRequest = null; user = null;
     * clearAllPersistentData();
     * 
     * // Javascript sets a cookie that will be used in getSignedRequest that we
     * // need to clear if we can $cookie_name = getSignedRequestCookieName();
     * if (array_key_exists($cookie_name, $_COOKIE)) {
     * unset($_COOKIE[$cookie_name]); if (!headers_sent()) { // The base domain
     * is stored in the metadata cookie if not we fallback // to the current
     * hostname $base_domain = '.'. $_SERVER['HTTP_HOST'];
     * 
     * $metadata = getMetadataCookie(); if (array_key_exists('base_domain',
     * $metadata) && !empty($metadata['base_domain'])) { $base_domain =
     * $metadata['base_domain']; }
     * 
     * setcookie($cookie_name, '', 0, '/', $base_domain); } else {
     * self::errorLog( 'There exists a cookie that we wanted to clear that we
     * couldn\'t '. 'clear because headers was already sent. Make sure to do the
     * first '. 'API call before outputing anything' ); } }
     */
  }

  /**
   * Parses the metadata cookie that our Javascript API set.
   * 
   * @return an array mapping key to value
   */
  protected String getMetadataCookie()
  {
    /*
     * TODO Translate $cookie_name = getMetadataCookieName(); if
     * (!array_key_exists($cookie_name, $_COOKIE)) { return array(); }
     * 
     * // The cookie value can be wrapped in "-characters so remove them
     * $cookie_value = trim($_COOKIE[$cookie_name], '"');
     * 
     * if (empty($cookie_value)) { return array(); }
     * 
     * $parts = explode('&', $cookie_value); $metadata = array(); foreach
     * ($parts as $part) { $pair = explode('=', $part, 2); if (!empty($pair[0]))
     * { $metadata[urldecode($pair[0])] = (count($pair) > 1) ?
     * urldecode($pair[1]) : ''; } }
     * 
     * return $metadata;
     */
    return null;
  }

  /**
   * Each of the following four methods should be overridden in a concrete
   * subclass, as they are in the provided Facebook class. The Facebook class
   * uses PHP sessions to provide a primitive persistent store, but another
   * subclass--one that you implement-- might use a database, memcache, or an
   * in-memory cache.
   * 
   * @param key
   *          the key
   * @param value
   *          the value
   * @see Facebook
   */

  /**
   * Stores the given ($key, $value) pair, so that future calls to
   * getPersistentData($key) return $value. This call may be in another request.
   * 
   * @param string
   *          $key
   * @param array
   *          $value
   */
  abstract protected void setPersistentData(String key, String value);

  /**
   * Get the data for $key, persisted by BaseFacebook::setPersistentData().
   * 
   * @param key
   *          the key
   * @return mixed
   */
  protected String getPersistentData(String key)
  {
    return getPersistentData(key, null);
  }

  /**
   * Get the data for $key, persisted by BaseFacebook::setPersistentData().
   * 
   * @param key
   *          the key
   * @param Default
   *          the default
   * @return mixed
   */
  abstract protected String getPersistentData(String key, String Default);

  /**
   * Clear the data with $key from the persistent storage.
   * 
   * @param key
   *          the key
   */
  abstract protected void clearPersistentData(String key);

  /**
   * Clear all data from the persistent storage.
   */
  abstract protected void clearAllPersistentData();

}
