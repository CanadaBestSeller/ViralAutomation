package com.viral.automation

import geb.Browser
import org.openqa.selenium.chrome.ChromeDriver

class ChromeBrowserProvider {

    static Browser get() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver")
        System.setProperty("webdriver.chrome.args", "--disable-logging")
        System.setProperty("webdriver.chrome.silentOutput", "true")

        return new Browser(driver: new ChromeDriver())
    }
}
