package com.object0r.toortools.phantomq;

import org.openqa.selenium.Cookie;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class PhantomJsJobResult
{
    String sourceCode;
    String content;

    Set<Cookie> cookiesSet = Collections.emptySet();

    public String getSourceCode()
    {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode)
    {
        this.sourceCode = sourceCode;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setCookiesSet(Set<Cookie> cookiesSet)
    {
        if (cookiesSet != null)
        {
            this.cookiesSet = cookiesSet;
        }
    }

    public Set<Cookie> getCookiesSet()
    {
        return cookiesSet;
    }

    public String getCookiesAsString()
    {
        StringBuilder cookies = new StringBuilder("");
        for (Cookie cookie : getCookiesSet())
        {
            cookies.append(cookie.getName()).append("=").append(cookie.getValue());
        }
        return cookies.toString();
    }

    public HashMap<String, String> getCookiesAsHashMap()
    {
        HashMap<String, String> cookies = new HashMap<String, String>();

        for (Cookie cookie : getCookiesSet())
        {
            cookies.put(cookie.getName(), cookie.getValue());
        }
        return cookies;
    }


}
