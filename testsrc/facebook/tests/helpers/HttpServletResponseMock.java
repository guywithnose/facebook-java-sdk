/*
 * File: HttpServletResponseMock.java Author: JSON.org <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class HttpServletRequestMock.
 */
@SuppressWarnings("unused")
public class HttpServletResponseMock implements HttpServletResponse
{

  /**
   * The Class StringServletOutputStream.
   */
  class StringServletOutputStream extends ServletOutputStream
  {

    /** The sb. */
    StringBuilder sb = new StringBuilder();

    /*
     * (non-Javadoc)
     * 
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(int b) throws IOException
    {
      sb.append((char) b);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return sb.toString();
    }

  }

  /** The stringservletoutputstream. */
  private ServletOutputStream stringservletoutputstream = new StringServletOutputStream();

  /** The outputstreamwriter. */
  private PrintWriter outputstreamwriter = null;

  /** The cookies. */
  private ArrayList<Cookie> cookies = new ArrayList<Cookie>();

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#getBufferSize()
   */
  @Override
  public int getBufferSize()
  {

    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#getCharacterEncoding()
   */
  @Override
  public String getCharacterEncoding()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#getContentType()
   */
  @Override
  public String getContentType()
  {

    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#getLocale()
   */
  @Override
  public Locale getLocale()
  {

    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#isCommitted()
   */
  @Override
  public boolean isCommitted()
  {

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#reset()
   */
  @Override
  public void reset()
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#resetBuffer()
   */
  @Override
  public void resetBuffer()
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#setBufferSize(int)
   */
  @Override
  public void setBufferSize(int arg0)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
   */
  @Override
  public void setCharacterEncoding(String arg0)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#setContentLength(int)
   */
  @Override
  public void setContentLength(int arg0)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
   */
  @Override
  public void setContentType(String arg0)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
   */
  @Override
  public void setLocale(Locale arg0)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie
   * )
   */
  @Override
  public void addCookie(Cookie arg0)
  {
    cookies.add(arg0);
  }

  public String getCookie(String name)
  {
    for(Cookie cookie : cookies)
    {
      if(cookie.getName().equals(name))
        return cookie.getValue();
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String,
   * long)
   */
  @Override
  public void addDateHeader(String arg0, long arg1)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String,
   * java.lang.String)
   */
  @Override
  public void addHeader(String arg0, String arg1)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String,
   * int)
   */
  @Override
  public void addIntHeader(String arg0, int arg1)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
   */
  @Override
  public boolean containsHeader(String arg0)
  {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String )
   */
  @Override
  public String encodeRedirectURL(String arg0)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String )
   */
  @Deprecated
  @Override
  public String encodeRedirectUrl(String arg0)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
   */
  @Override
  public String encodeURL(String arg0)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
   */
  @Deprecated
  @Override
  public String encodeUrl(String arg0)
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#sendError(int)
   */
  @Override
  public void sendError(int arg0) throws IOException
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#sendError(int,
   * java.lang.String)
   */
  @Override
  public void sendError(int arg0, String arg1) throws IOException
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
   */
  @Override
  public void sendRedirect(String arg0) throws IOException
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String,
   * long)
   */
  @Override
  public void setDateHeader(String arg0, long arg1)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String,
   * java.lang.String)
   */
  @Override
  public void setHeader(String arg0, String arg1)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String,
   * int)
   */
  @Override
  public void setIntHeader(String arg0, int arg1)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#setStatus(int)
   */
  @Override
  public void setStatus(int arg0)
  {
    // Do Nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.http.HttpServletResponse#setStatus(int,
   * java.lang.String)
   */
  @Deprecated
  @Override
  public void setStatus(int arg0, String arg1)
  {
    // Do Nothing
  }

  @Override
  public void flushBuffer() throws IOException
  {
    // Do Nothing
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException
  {
    return stringservletoutputstream;
  }

  @Override
  public PrintWriter getWriter() throws IOException
  {
    if(outputstreamwriter == null)
      outputstreamwriter = new PrintWriter(stringservletoutputstream);
    return outputstreamwriter;
  }

  @Override
  public String toString()
  {
    outputstreamwriter.flush();
    return stringservletoutputstream.toString();
  }

}
