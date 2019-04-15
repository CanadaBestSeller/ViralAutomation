package com.viral.automation.authentication

import com.viral.automation.main.ChromeBrowserProvider
import com.viral.automation.main.Log
import geb.Browser

class ViralLogin {

    static Browser launch(final String email, final String password) {
        Log.info "Logging in..."

        def window = ChromeBrowserProvider.get()
        window.drive {
            go "https://viral-launch.com/sellers/signIn.html"
            waitFor { $("button.signIn__button").isDisplayed() }
            Thread.sleep(2_000)
            $("input.signIn__input.null", 0).value(email)
            $("input.signIn__input.null", 1).value(password)
            $("button.signIn__button", 0).click()
        }

        Log.success "Logged in!"

        return window
    }
}

