/*
 * File:         JavaCurl.java
 * Author:       Robert Bittle <guywithnose@gmail.com>
 */
package data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * The Class javaCurl.
 */
public class JavaCurl
{

  /**
   * Get Url.
   * 
   * @param url
   *          the url
   * @return the url
   * @throws SocketTimeoutException
   *           the socket timeout exception
   */
  public static String getUrl(String url) throws SocketTimeoutException
  {
    return getUrl(url, "GET", new HashMap<String, String>());
  }

  /**
   * Async get url.
   * 
   * @param url
   *          the url
   */
  public static void asyncGetUrl(String url)
  {
    asyncGetUrl(url, "GET", new HashMap<String, String>());
  }

  /**
   * Async get url.
   * 
   * @param url
   *          the url
   * @param method
   *          the method
   * @param params
   *          the params
   */
  @SuppressWarnings("unused")
  public static void asyncGetUrl(String url, String method,
      HashMap<String, String> params)
  {
    class aSync implements Runnable
    {

      private String urlASync;
      private String methodASync;
      private HashMap<String, String> paramsASync;

      @Override
      public void run()
      {
        try
        {
          getUrl(urlASync, methodASync, paramsASync);
        } catch (SocketTimeoutException e)
        {
          e.printStackTrace();
        }
      }

      public aSync(String Url, String Method, HashMap<String, String> Params)
      {
        this.urlASync = Url;
        this.methodASync = Method;
        this.paramsASync = Params;
        run();
      }

    }
    // Run as thread
    aSync a = new aSync(url, method, params);
  }

  /**
   * Gets the url.
   * 
   * @param url
   *          the url
   * @param method
   *          the method
   * @param params
   *          the params
   * @return the url
   * @throws SocketTimeoutException
   *           the socket timeout exception
   */
  public static String getUrl(String url, String method,
      HashMap<String, String> params) throws SocketTimeoutException
  {
    return getUrl(url, method, params, new HashMap<String, String>());
  }

  /**
   * Gets the url.
   * 
   * @param url
   *          the url
   * @param method
   *          the method
   * @param params
   *          the params
   * @param headers
   *          the headers
   * @return the url
   * @throws SocketTimeoutException
   *           the socket timeout exception
   */
  public static String getUrl(String url, String method,
      HashMap<String, String> params, HashMap<String, String> headers)
      throws SocketTimeoutException
  {
    return getUrl(url, method, params, headers, 5000);
  }
  
  /**
   * Get Url.
   *
   * @param url the url
   * @param method the method
   * @param params the params
   * @param headers the headers
   * @param timeout the timeout
   * @return the url
   * @throws SocketTimeoutException the socket timeout exception
   */
  public static String getUrl(String url, String method,
      HashMap<String, String> params, HashMap<String, String> headers, int timeout)
      throws SocketTimeoutException
  {
    return getUrl(url, method, params, headers, timeout, null, null);
  }

  /**
   * Get Url.
   * 
   * @param url
   *          the url
   * @param method
   *          the method
   * @param params
   *          the params
   * @param headers
   *          the headers
   * @param timeout
   *          the timeout
   * @param certFile
   *          the cert file
   * @param password
   *          the password
   * @return the url
   * @throws SocketTimeoutException
   *           the socket timeout exception
   */
  public static String getUrl(String url, String method,
      HashMap<String, String> params, HashMap<String, String> headers,
      int timeout, String certFile, String password) throws SocketTimeoutException
  {
    try
    {
      HttpURLConnection http = getConnection(url, certFile, password);
      http.setRequestMethod(method);
      http.setUseCaches(false);
      http.setReadTimeout(timeout);
      http.setConnectTimeout(0);
      for (String headerName : headers.keySet())
      {
        http.setRequestProperty(headerName, headers.get(headerName));
      }
      if (params.size() > 0)
      {
        http.setDoOutput(true);
        http.setDoInput(true);
        String key = "";
        Iterator<String> i = params.keySet().iterator();
        String urlParameters = "";
        while (i.hasNext())
        {
          key = i.next();
          urlParameters += key + "=" + params.get(key) + "&";
        }

        DataOutputStream wr = new DataOutputStream(http.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
      } else
      {
        http.connect();
      }

      InputStream is;
      StringBuilder stringBuilder;
      try
      {
        is = http.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
          stringBuilder.append(line + "\n");
        }
      } catch (IOException e)
      {
        is = http.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
          stringBuilder.append(line + "\n");
        }
      }

      return stringBuilder.toString();
    } catch (Exception e)
    {
      throw new SocketTimeoutException(e.getMessage());
    }
  }

  private static HttpURLConnection getConnection(String url, String certFile,
      String password) throws IOException
  {
    String protocol = url.substring(0, 5);
    URL u = new URL(url);
    if ("https".equals(protocol) && certFile != null && password != null)
    {
      HttpsURLConnection http = (HttpsURLConnection) u.openConnection();
      http.setSSLSocketFactory(getSocketFactory(certFile, password));
      return http;
    }
    HttpURLConnection http = (HttpURLConnection) u.openConnection();
    return http;
  }
  
  private static SSLSocketFactory getSocketFactory(String certFile, String pKeyPassword)
  {
    try
    {
      File pKeyFile = new File(certFile);
      KeyManagerFactory keyManagerFactory = KeyManagerFactory
          .getInstance("SunX509");
      KeyStore keyStore = KeyStore.getInstance("PKCS12");
      InputStream keyInput = new FileInputStream(pKeyFile);
      keyStore.load(keyInput, pKeyPassword.toCharArray());
      keyInput.close();
      keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());
      SSLContext context;
      context = SSLContext.getInstance("TLS");
      context
          .init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
      SSLSocketFactory sockFact = context.getSocketFactory();
      return sockFact;
    } catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

}
