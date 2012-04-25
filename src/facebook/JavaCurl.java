/*
 * File: javaCurl.java Author: Robert Bittle <guywithnose@gmail.com>
 */
package facebook;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The Class javaCurl.
 */
public class JavaCurl
{

    /**
     * Get Url.
     * 
     * @param url
     *            the url
     * @return the url
     */
    public static String getUrl(String url)
    {
        return getUrl(url, "GET", new HashMap<String, String>());
    }

    /**
     * Async get url.
     * 
     * @param url
     *            the url
     */
    public static void asyncGetUrl(String url)
    {
        asyncGetUrl(url, "GET", new HashMap<String, String>());
    }

    /**
     * Async get url.
     * 
     * @param url
     *            the url
     * @param method
     *            the method
     * @param params
     *            the params
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
                getUrl(urlASync, methodASync, paramsASync);
            }

            public aSync(String Url, String Method,
                    HashMap<String, String> Params)
            {
                this.urlASync = Url;
                this.methodASync = Method;
                this.paramsASync = Params;
                run();
            }

        }
        //Run as thread
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
     */
    public static String getUrl(String url, String method,
        HashMap<String, String> params)
    {
      return getUrl(url, method, params, new HashMap<String, String>());
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
     * @return the url
     */
    public static String getUrl(String url, String method,
        HashMap<String, String> params, HashMap<String, String> headers)
    {
        try
        {
            URL u = new URL(url);
            HttpURLConnection http = (HttpURLConnection) u.openConnection();
            http.setRequestMethod(method);
            http.setUseCaches(false);
            http.setReadTimeout(0);
            http.setConnectTimeout(0);
            for(String headerName : headers.keySet())
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

                DataOutputStream wr = new DataOutputStream(
                        http.getOutputStream());
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
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                stringBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line + "\n");
                }
            } catch (IOException e)
            {
                is = http.getErrorStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                stringBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line + "\n");
                }
                // System.err.println(u);
                // System.err.println(stringBuilder);
            }

            return stringBuilder.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

}
