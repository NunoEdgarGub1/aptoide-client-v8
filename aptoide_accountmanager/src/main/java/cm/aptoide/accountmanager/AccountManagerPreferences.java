package cm.aptoide.accountmanager;

import cm.aptoide.accountmanager.ws.LoginMode;
import cm.aptoide.pt.preferences.secure.SecurePreferences;

/**
 * Created by trinkes on 5/2/16.
 */
public class AccountManagerPreferences {

  static String getAccessToken() {
    return SecurePreferences.getString(SecureKeys.ACCESS_TOKEN);
  }

  static void setAccessToken(String accessToken) {
    SecurePreferences.putString(SecureKeys.ACCESS_TOKEN, accessToken);
  }

  static void removeAccessToken() {
    SecurePreferences.remove(SecureKeys.ACCESS_TOKEN);
  }

  static String getRefreshToken() {
    return SecurePreferences.getString(SecureKeys.REFRESH_TOKEN);
  }

  static void setRefreshToken(String refreshToken) {
    SecurePreferences.putString(SecureKeys.REFRESH_TOKEN, refreshToken);
  }

  static void removeRefreshToken() {
    SecurePreferences.remove(SecureKeys.REFRESH_TOKEN);
  }

  static String getUserEmail() {
    return SecurePreferences.getString(SecureKeys.USER_EMAIL);
  }

  static void setUserEmail(String userName) {
    SecurePreferences.putString(SecureKeys.USER_EMAIL, userName);
  }

  static void removeUserEmail() {
    SecurePreferences.remove(SecureKeys.USER_EMAIL);
  }

  static String getQueueName() {
    return SecurePreferences.getString(SecureKeys.QUEUE_NAME);
  }

  static void setQueueName(String queueName) {
    SecurePreferences.putString(SecureKeys.QUEUE_NAME, queueName);
  }

  static String getUserId() {
    return SecurePreferences.getString(SecureKeys.USER_ID);
  }

  static void setUserId(int userId) {
    SecurePreferences.putString(SecureKeys.USER_ID, String.valueOf(userId));
  }

  static void removeQueueName() {
    SecurePreferences.remove(SecureKeys.QUEUE_NAME);
  }

  static String getUserAvatar() {
    return SecurePreferences.getString(SecureKeys.USER_AVATAR);
  }

  static void setUserAvatar(String userAvatar) {
    SecurePreferences.putString(SecureKeys.USER_AVATAR, userAvatar);
  }

  static void removeUserAvatar() {
    SecurePreferences.remove(SecureKeys.USER_AVATAR);
  }

  public static String getUserRepo() {
    return SecurePreferences.getString(SecureKeys.USER_REPO);
  }

  static void setUserRepo(String userRepo) {
    SecurePreferences.putString(SecureKeys.USER_REPO, userRepo);
  }

  static void removeUserRepo() {
    SecurePreferences.remove(SecureKeys.USER_REPO);
  }

  static String getUserNickName() {
    return SecurePreferences.getString(SecureKeys.USER_NICK_NAME);
  }

  static void setUserNickName(String userNickName) {
    SecurePreferences.putString(SecureKeys.USER_NICK_NAME, userNickName);
  }

  static void removeUserNickName() {
    SecurePreferences.remove(SecureKeys.USER_NICK_NAME);
  }

  static boolean getMatureSwitch() {
    return SecurePreferences.getBoolean(SecureKeys.MATURE_SWITCH);
  }

  static void setMatureSwitch(boolean matureSwitch) {
    SecurePreferences.putBoolean(SecureKeys.MATURE_SWITCH, matureSwitch);
  }

  static void removeMatureSwitch() {
    SecurePreferences.remove(SecureKeys.MATURE_SWITCH);
  }

  static String getRepoAvatar() {
    return SecurePreferences.getString(SecureKeys.REPO_AVATAR);
  }

  static void setRepoAvatar(String repoAvatar) {
    SecurePreferences.putString(SecureKeys.REPO_AVATAR, repoAvatar);
  }

  static void removeRepoAvatar() {
    SecurePreferences.remove(SecureKeys.REPO_AVATAR);
  }

  public static LoginMode getLoginMode() {
    final String loginModeName = SecurePreferences.getString(SecureKeys.LOGIN_MODE);
    if (loginModeName != null) {
      return LoginMode.valueOf(loginModeName);
    } else {
      return null;
    }
  }

  public static void setLoginMode(LoginMode loginMode) {
    SecurePreferences.putString(SecureKeys.LOGIN_MODE, loginMode.name());
  }

  static String getRepoTheme() {
    return SecurePreferences.getString(SecureKeys.REPO_THEME);
  }

  static void setRepoTheme(String repoTheme) {
    SecurePreferences.putString(SecureKeys.REPO_THEME, repoTheme);
  }
}