Facebook Java SDK (v 0.1)
==========================

The [Facebook Platform](http://developers.facebook.com/) is
a set of APIs that make your app more social

This repository contains the open source Java SDK that allows you to access Facebook Platform from your Java app. 

Usage
-----

The minimal you'll need to have is:

    Facebook facebook = new Facebook(new JSONObject("{\"appId\": \"" + APP_ID + "\",\"secret\": \""
        + SECRET + "\"}"), HTTPrequest, HTTPresponse);
    
    long user = facebook.getUser();
    
    if (user != 0) {
      try {
        // Proceed knowing you have a logged in user who's authenticated.
        JSONObject user_profile = facebook.api("/me");
      } catch (FacebookApiException e) {
        e.printStackTrace();
        user = 0;
      }
    }

Login or logout url will be needed depending on current user state.

    if (user != 0) {
      String logoutUrl = facebook.getLogoutUrl();
    } else {
      String loginUrl = facebook.getLoginUrl();
    }

Tests
-----

In order to keep us nimble and allow us to bring you new functionality, without
compromising on stability, we have ensured full test coverage of the SDK.
We are including this in the open source repository to assure you of our
commitment to quality, but also with the hopes that you will contribute back to
help keep it stable. The easiest way to do so is to file bugs and include a
test case.

Contributing
===========
When commiting, keep all lines to less than 80 characters, and try to follow the existing style.

Add comments where needed, and provide ample explanation in the commit message.

Try to split up pull requests into single issues.  If a pull request does two things
it makes it difficult to accept if we only want one of those things.
