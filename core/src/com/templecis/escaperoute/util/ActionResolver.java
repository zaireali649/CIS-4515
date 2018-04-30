package com.templecis.escaperoute.util;

/**
 * Created by Ziggy on 4/29/2018.
 */

public interface ActionResolver {
    public boolean getSignedInGPGS();
    public void loginGPGS();
    public void submitScoreGPGS(int score);
    public void unlockAchievementGPGS(String achievementId);
    public void getLeaderboardGPGS();
    public void getAchievementsGPGS();
}