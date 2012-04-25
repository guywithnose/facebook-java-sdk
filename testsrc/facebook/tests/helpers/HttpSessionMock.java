/*
 * File: HttpServletRequestMock.java Author: Robert Bittle
 * <guywithnose@gmail.com>
 */
package facebook.tests.helpers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * The Class HttpSessionMock.
 */
@SuppressWarnings({ "unused", "deprecation" })
public class HttpSessionMock implements HttpSession
{

  /** The attributes. */
  HashMap<String,Object> attributes;
  
  /** The valid. */
  public boolean valid = true;
  
  /**
   * The Class nameEnumeration.
   */
  private class nameEnumeration implements Enumeration<String>
  {
    
    /** The names. */
    Set<String> names;
    
    /** The position. */
    Iterator<String> position;
    
    /**
     * Instantiates a new name enumeration.
     * 
     * @param Names
     *          the names
     */
    public nameEnumeration(Set<String> Names)
    {
      names = Names;
      position = names.iterator();
    }

    /* (non-Javadoc)
     * @see java.util.Enumeration#hasMoreElements()
     */
    @Override
    public boolean hasMoreElements()
    {
      return position.hasNext();
    }

    /* (non-Javadoc)
     * @see java.util.Enumeration#nextElement()
     */
    @Override
    public String nextElement()
    {
      return position.next();
    }
    
  }
  
  /**
   * Instantiates a new http session mock.
   */
  public HttpSessionMock()
  {
    attributes = new HashMap<String, Object>();
  }
  
  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
   */
  @Override
  public Object getAttribute(String arg0)
  {
    return attributes.get(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#getAttributeNames()
   */
  @Override
  public Enumeration<String> getAttributeNames()
  {
    return new nameEnumeration(attributes.keySet());
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#getCreationTime()
   */
  @Override
  public long getCreationTime()
  {
    return 0;
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#getId()
   */
  @Override
  public String getId()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#getLastAccessedTime()
   */
  @Override
  public long getLastAccessedTime()
  {
    return 0;
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
   */
  @Override
  public int getMaxInactiveInterval()
  {
    return 0;
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#getServletContext()
   */
  @Override
  public ServletContext getServletContext()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#getSessionContext()
   */
  @Override
  public HttpSessionContext getSessionContext()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
   */
  @Override
  public Object getValue(String arg0)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#getValueNames()
   */
  @Override
  public String[] getValueNames()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#invalidate()
   */
  @Override
  public void invalidate()
  {
    valid = false;
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#isNew()
   */
  @Override
  public boolean isNew()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
   */
  @Override
  public void putValue(String arg0, Object arg1)
  {
    //Do Nothing
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
   */
  @Override
  public void removeAttribute(String arg0)
  {
    attributes.remove(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
   */
  @Override
  public void removeValue(String arg0)
  {
    //Do Nothing
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
   */
  @Override
  public void setAttribute(String name, Object value)
  {
    attributes.put(name, value);
  }

  /* (non-Javadoc)
   * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
   */
  @Override
  public void setMaxInactiveInterval(int arg0)
  {
    //Do Nothing
  }
}
