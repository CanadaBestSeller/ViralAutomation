package com.viral.automation.authentication

import com.viral.automation.main.ChromeBrowserProvider
import com.viral.automation.main.Log
import geb.Browser
import geb.Page

class ViralLogin {

    static Browser launch(final String email, final String password) {
        Log.info "Logging in..."

        def window = ChromeBrowserProvider.get()
        window.drive {
            to ViralLoginPage
            waitFor { $("button.sign-in__button").isDisplayed() }
            Thread.sleep(3_000)

            try {
                if (annoyingPopup) {
                    waitFor(30, message: "Closing annoying login popup...") { annoyingPopupCloseButton.click() }
                }
            } catch (Throwable t) {
                Log.debug(t.toString())
            }

            $("input.sign-in__input", 0).value(email)
            $("input.sign-in__input", 1).value(password)

            waitFor(30, message:"Clicking login button...") { $("button.sign-in__button", 0).click() }
        }

        Log.success "Logged in!"

        return window
    }
}

class ViralLoginPage extends Page {
    static url = "https://viral-launch.com/sellers/signIn.html"

    static content = {
        annoyingPopup { $("div.listbuilder-popup-scale") }
        annoyingPopupCloseButton { $("div.sumome-react-wysiwyg-close-button", 0) }
    }
}

