/*
 * File: HttpServletRequestMock.java Author: Robert Bittle
 * <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The Class HttpServletRequestMock.
 */
@SuppressWarnings("unused")
public class HttpServletRequestMock implements HttpServletRequest
{

  /**
   * The Class ParameterNameEnumeration.
   */
  class ParameterNameEnumeration implements Enumeration<String>
  {

    /** The parameter names. */
    String[] parameterNames;

    /** The position. */
    int position;

    /**
     * Instantiates a new parameter name enumeration.
     * 
     * @param names
     *          the names
     */
    ParameterNameEnumeration(String[] names)
    {
      parameterNames = names;
      position = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Enumeration#hasMoreElements()
     */
    @Override
    public boolean hasMoreElements()
    {
      return parameterNames.length > position;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Enumeration#nextElement()
     */
    @Override
    public String nextElement()
    {
      if (hasMoreElements())
      {
        String retVal = parameterNames[position];
        position++;
        return retVal;
      }
      return "";
    }

  }

  /** The parameters. */
  Map<String, String> parameters;

  /** The cookies. */
  ArrayList<Cookie> cookies;

  /** The port. */
  private int port;

  private String serverName;

  private String url;

  private String method;

  /**
   * Instantiates a new http servlet request mock.
   * 
   * @param Parameters
   *          the parameters
   */
  public HttpServletRequestMock(Map<String, String> Parameters)
  {
    parameters = Parameters;
  }

  /**
   * Instantiates a new http servlet request mock.
   */
  public HttpServletRequestMock()
  {
    this(new HashMap<String, String>());
  }

  /**
   * Instantiates a new http servlet request mock.
   * 
   * @param Parameters
   *          the parameters
   * @param Cookies
   *          the cookies
   */
  public HttpServletRequestMock(Map<String, String> Parameters,
      ArrayList<Cookie> Cookies)
  {
    parameters = Parameters;
    cookies = Cookies;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getInputStream()
   */
  @Override
  public ServletInputStream getInputStream() throws IOException
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
   */
  @Override
  public String getParameter(String arg0)
  {
    if (parameters.containsKey(arg0))
      return parameters.get(arg0);
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getParameterMap()
   */
  @Override
  public Map<String, String> getParameterMap()
  {
    return parameters;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getParameterNames()
   */
  @Override
  public Enumeration<String> getParameterNames()
  {
    String[] names = new String[parameters.size()];
    Iterator<String> i = parameters.keySet().iterator();
    int j = 0;
    while (i.hasNext())
    {
      names[j] = i.next();
      j++;
    }
    ParameterNameEnumeration parameternameenumeration = new ParameterNameEnumeration(
        names);
    return parameternameenumeration;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
   */
  @Override
  public String[] getParameterValues(String arg0)
  {
    String[] values = new String[parameters.size()];
    Iterator<String> i = parameters.values().iterator();
    int j = 0;
    while (i.hasNext())
    {
      values[j] = i.next();
      j++;
    }
    return values;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getReader()
   */
  @Override
  public BufferedReader getReader() throws IOException
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getCookies()
   */
  @Override
  public Cookie[] getCookies()
  {
    return cookies.toArray(new Cookie[] {});
  }

  /**
   * Adds the cookie.
   * 
   * @param name
   *          the name
   * @param value
   *          the value
   */
  public void addCookie(String name, String value)
  {
    cookies.add(new Cookie(name, value));
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getMethod()
   */
  @Override
  public String getMethod()
  {
    return method;
  }

  /**
   * Sets the method.
   * 
   * @param Method
   *          the new method
   */
  public void setMethod(String Method)
  {
    method = Method;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getQueryString()
   */
  @Override
  public String getQueryString()
  {
    int questionIndex = url.indexOf("?");
    if(questionIndex != -1)
      return url.substring(questionIndex+1);
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getRequestURI()
   */
  @Override
  public String getRequestURI()
  {
    return getRequestURL().substring(url.indexOf("/", url.indexOf("//") + 2));
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getRequestURL()
   */
  @Override
  public StringBuffer getRequestURL()
  {
    int questionIndex = url.indexOf("?");
    if(questionIndex != -1)
      return new StringBuffer(url.substring(0, url.indexOf("?")));
    return new StringBuffer(url);
  }

  /**
   * Sets the request url.
   * 
   * @param request
   *          the new request url
   */
  public void setRequestString(String request)
  {
    url = request;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getContentLength()
   */
  @Override
  public int getContentLength()
  {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
   */
  @Override
  public String getHeader(String arg0)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
   */
  @Override
  public Enumeration<String> getHeaderNames()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
   */
  @Override
  public Enumeration<String> getHeaders(String arg0)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getProtocol()
   */
  @Override
  public String getProtocol()
  {
    return url.substring(0, url.indexOf("://"));
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
   */
  @Override
  @Deprecated
  public String getRealPath(String arg0)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getRemoteAddr()
   */
  @Override
  public String getRemoteAddr()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getRemoteHost()
   */
  @Override
  public String getRemoteHost()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getRemotePort()
   */
  @Override
  public int getRemotePort()
  {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
   */
  @Override
  public RequestDispatcher getRequestDispatcher(String arg0)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getScheme()
   */
  @Override
  public String getScheme()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getServerName()
   */
  @Override
  public String getServerName()
  {
    int start = url.indexOf("://") + 3;
    int stop = url.indexOf(":", start);
    if(stop == -1)
      stop = url.indexOf("/", start);
    return url.substring(start, stop);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getServerPort()
   */
  @Override
  public int getServerPort()
  {
    int start = url.indexOf("://") + 3;
    int slashIndex = url.indexOf("/", start);
    int colonIndex = url.indexOf(":", start); 
    if(colonIndex < slashIndex && colonIndex != -1)
    {
      System.out.println(start);
      System.out.println(slashIndex);
      System.out.println(colonIndex);
      System.out.println(url);
      System.out.println(url.substring(start+colonIndex, slashIndex-colonIndex));
      return Integer.valueOf(url.substring(start+colonIndex, slashIndex-colonIndex));
    }
    if("https".equals(getProtocol()))
    {
      return 443;
    }
    return 80;
  }

  /**
   * Sets the server port.
   * 
   * @param Port
   *          the port
   */
  public void setServerPort(int Port)
  {
    port = Port;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#isSecure()
   */
  @Override
  public boolean isSecure()
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
   */
  @Override
  public void removeAttribute(String arg0)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#setAttribute(java.lang.String,
   * java.lang.Object)
   */
  @Override
  public void setAttribute(String arg0, Object arg1)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
   */
  @Override
  public void setCharacterEncoding(String arg0)
      throws UnsupportedEncodingException
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getAuthType()
   */
  @Override
  public String getAuthType()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getContextPath()
   */
  @Override
  public String getContextPath()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
   */
  @Override
  public long getDateHeader(String arg0)
  {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
   */
  @Override
  public int getIntHeader(String arg0)
  {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getPathInfo()
   */
  @Override
  public String getPathInfo()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
   */
  @Override
  public String getPathTranslated()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
   */
  @Override
  public String getRemoteUser()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
   */
  @Override
  public String getRequestedSessionId()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getServletPath()
   */
  @Override
  public String getServletPath()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getSession()
   */
  @Override
  public HttpSession getSession()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
   */
  @Override
  public HttpSession getSession(boolean arg0)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
   */
  @Override
  public Principal getUserPrincipal()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
   */
  @Override
  public boolean isRequestedSessionIdFromCookie()
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
   */
  @Override
  public boolean isRequestedSessionIdFromURL()
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
   */
  @Override
  @Deprecated
  public boolean isRequestedSessionIdFromUrl()
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
   */
  @Override
  public boolean isRequestedSessionIdValid()
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
   */
  @Override
  public boolean isUserInRole(String arg0)
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getLocalAddr()
   */
  @Override
  public String getLocalAddr()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getLocalName()
   */
  @Override
  public String getLocalName()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getLocalPort()
   */
  @Override
  public int getLocalPort()
  {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getLocale()
   */
  @Override
  public Locale getLocale()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getLocales()
   */
  @Override
  public Enumeration<String> getLocales()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
   */
  @Override
  public Object getAttribute(String arg0)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getAttributeNames()
   */
  @Override
  public Enumeration<String> getAttributeNames()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getCharacterEncoding()
   */
  @Override
  public String getCharacterEncoding()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletRequest#getContentType()
   */
  @Override
  public String getContentType()
  {
    return null;
  }

}
