package main.website;

import java.util.ArrayList;

public class ProtectedWebsiteRunner extends WebsiteRunner {
    private int personalToken;
    private final ArrayList<Integer> usedTokens = new ArrayList<>();
    public ProtectedWebsiteRunner() {
        personalToken = (int) (1000 + Math.random() * 8999);
        int i = 0;

        while (i < usedTokens.size()) {
            if (personalToken == usedTokens.get(i)) {
                i = 0;
                personalToken = (int) (1000 + Math.random() * 8999);
            } else {
                i++;
            }
        }

        usedTokens.add(personalToken);
    }

    public int getPersonalToken() {
        return personalToken;
    }

    public boolean checkInputToken(int inputToken) {
        return (inputToken == personalToken);
    }

}
