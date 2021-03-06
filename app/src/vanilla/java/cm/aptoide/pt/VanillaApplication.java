/*
 * Copyright (c) 2016.
 * Modified on 01/07/2016.
 */

package cm.aptoide.pt;

import android.os.Environment;
import cm.aptoide.pt.account.LoginPreferences;
import cm.aptoide.pt.view.configuration.FragmentProvider;
import cm.aptoide.pt.view.configuration.implementation.VanillaFragmentProvider;
import com.google.android.gms.common.GoogleApiAvailability;

public class VanillaApplication extends AptoideApplication {

  @Override public String getCachePath() {
    return Environment.getExternalStorageDirectory()
        .getAbsolutePath() + "/.aptoide/";
  }

  @Override public String getDefaultStore() {
    return "apps";
  }

  @Override public String getMarketName() {
    return "Aptoide";
  }

  @Override public LoginPreferences getLoginPreferences() {
    return new LoginPreferences(this, GoogleApiAvailability.getInstance());
  }

  @Override public String getFeedbackEmail() {
    return "support@aptoide.com";
  }

  @Override public String getImageCachePath() {
    return getCachePath() + "icons/";
  }

  @Override public String getAccountType() {
    return BuildConfig.APPLICATION_ID;
  }

  @Override public String getAutoUpdateUrl() {
    return "http://imgs.aptoide.com/latest_version_v8.xml";
  }

  @Override public String getPartnerId() {
    return null;
  }

  @Override public String getExtraId() {
    return null;
  }

  @Override public String getDefaultTheme() {
    return "default";
  }

  @Override public boolean isCreateStoreUserPrivacyEnabled() {
    return true;
  }

  @Override public FragmentProvider createFragmentProvider() {
    return new VanillaFragmentProvider();
  }
}