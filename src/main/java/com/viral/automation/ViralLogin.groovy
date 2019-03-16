package com.viral.automation

import geb.Browser

class ViralLogin {

    static Browser launch(final String email, final String password) {
        Log.info "Logging in..."

        def window = ChromeBrowserProvider.get()
        window.drive {
            go "https://viral-launch.com/sellers/signIn.html"
            waitFor { $("#signInButton").isDisplayed() }
            $("#signInEmail", 0).value(email)
            $("#signInPass", 0).value(password)
            $("#signInButton", 0).click()
        }

        Log.success "Logged in!"

        return window
    }
}

