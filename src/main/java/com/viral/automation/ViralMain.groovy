package com.viral.automation

class ViralMain {

    static void main(String... args) {

        final String email = args[0]
        final String password = args[1]
        final String searchTerm = args[2]

        def loginWindow = ViralLogin.launch(email, password)
        def marketIntelligenceWindow = ViralMarketIntelligence.launch(searchTerm)

        // do stuff

        loginWindow.quit()
        marketIntelligenceWindow.quit()
    }
}

