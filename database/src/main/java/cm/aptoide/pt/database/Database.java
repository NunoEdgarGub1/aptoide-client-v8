/*
 * Copyright (c) 2016.
 * Modified by SithEngineer on 24/08/2016.
 */

package cm.aptoide.pt.database;

import android.content.Context;
import android.text.TextUtils;

import cm.aptoide.pt.database.realm.ExcludedAd;
import cm.aptoide.pt.database.realm.Installed;
import cm.aptoide.pt.database.realm.Rollback;
import cm.aptoide.pt.database.realm.Scheduled;
import cm.aptoide.pt.database.realm.Store;
import cm.aptoide.pt.database.realm.StoredMinimalAd;
import cm.aptoide.pt.database.realm.Update;
import cm.aptoide.pt.model.v7.GetAppMeta;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by sithengineer on 16/05/16.
 *
 * This is the main class responsible to offer {@link Realm} database instances
 */
public class Database {

	private static final String TAG = Database.class.getSimpleName();
	private static final String KEY = "KRbjij20wgVyUFhMxm2gUHg0s1HwPUX7DLCp92VKMCt";
	private static final String DB_NAME = "aptoide.realm.db";
	private static final AllClassesModule MODULE = new AllClassesModule();
	private static final RealmMigration MIGRATION = new RealmDatabaseMigration();

	private static boolean isInitialized = false;

	private static String extract(String str) {
		return TextUtils.substring(str, str.lastIndexOf('.'), str.length());
	}

	public static void initialize(Context context) {
		StringBuilder strBuilder = new StringBuilder(KEY);
		strBuilder.append(extract(cm.aptoide.pt.model.BuildConfig.APPLICATION_ID));
		strBuilder.append(extract(cm.aptoide.pt.utils.BuildConfig.APPLICATION_ID));
		strBuilder.append(extract(BuildConfig.APPLICATION_ID));
		strBuilder.append(extract(cm.aptoide.pt.preferences.BuildConfig.APPLICATION_ID));

		// Beware this is the app context
		// So always use a unique name
		// Always use explicit modules in library projects
		RealmConfiguration realmConfig;
		if (BuildConfig.DEBUG) {
			realmConfig = new RealmConfiguration.Builder(context).name(DB_NAME).modules(MODULE)
					// Must be bumped when the schema changes
					.schemaVersion(BuildConfig.VERSION_CODE).deleteRealmIfMigrationNeeded().build();
		} else {
			realmConfig = new RealmConfiguration.Builder(context).name(DB_NAME).modules(MODULE).encryptionKey(strBuilder.toString().substring(0, 64)
					.getBytes())
					// Must be bumped when the schema changes
					.schemaVersion(BuildConfig.VERSION_CODE)
					// Migration to run instead of throwing an exception
					.migration(MIGRATION).build();
		}

		if (BuildConfig.DELETE_DB) {
			Realm.deleteRealm(realmConfig);
		}
		Realm.setDefaultConfiguration(realmConfig);
		isInitialized = true;
	}

	public static Realm get() {
		if(isInitialized){
			return Realm.getDefaultInstance();
		}
		throw new IllegalStateException("You need to call Database.initialize(Context) first");
	}

	public static void save(RealmObject realmObject, Realm realm) {
		realm.beginTransaction();
		realm.copyToRealmOrUpdate(realmObject);
		realm.commitTransaction();
	}

	public static void save(Installed installed, Realm realm) {
		realm.beginTransaction();
//		installed.computeId();
		realm.copyToRealmOrUpdate(installed);
		realm.commitTransaction();
	}

	public static void save(Rollback rollback, Realm realm) {
		realm.beginTransaction();
//		rollback.computeId();
		realm.copyToRealmOrUpdate(rollback);
		realm.commitTransaction();
	}

	public static void delete(RealmObject realmObject, Realm realm) {
		realm.beginTransaction();
		realmObject.deleteFromRealm();
		realm.commitTransaction();
	}

	public static void dropTable(Class<? extends RealmModel> aClass, Realm realm) {
		realm.beginTransaction();
		realm.delete(aClass);
		realm.commitTransaction();
	}

	public static class InstalledQ {

		public static RealmResults<Installed> getAll(Realm realm) {
			return realm.where(Installed.class).findAll();
		}

		public static Installed get(String packageName, Realm realm) {
			return realm.where(Installed.class).equalTo(Installed.PACKAGE_NAME, packageName).findFirst();
		}

		public static void delete(String packageName, Realm realm) {
			Installed first = realm.where(Installed.class).equalTo(Installed.PACKAGE_NAME, packageName).findFirst();
			if (first != null) {
				realm.beginTransaction();
				first.deleteFromRealm();
				realm.commitTransaction();
			}
		}

		public static boolean isInstalled(String packageName, Realm realm) {
			return get(packageName, realm) != null;
		}
	}

	public static class StoreQ {

		public static Store get(long storeId, Realm realm) {
			return realm.where(Store.class).equalTo(Store.STORE_ID, storeId).findFirst();
		}

		public static Store get(String storeName, Realm realm) {
			return realm.where(Store.class).equalTo(Store.STORE_NAME, storeName).findFirst();
		}

		public static boolean contains(String storeName, Realm realm) {
			return realm.where(Store.class).equalTo(Store.STORE_NAME, storeName).count()>0;
		}

		public static RealmResults<Store> getAll(Realm realm) {
			return realm.where(Store.class).findAll();
		}

		public static void delete(long storeId, Realm realm) {
			realm.beginTransaction();
			realm.where(Store.class).equalTo(Store.STORE_ID, storeId).findFirst().deleteFromRealm();
			realm.commitTransaction();
		}
	}

	public static class UpdatesQ {

		public static RealmResults<Update> getAll(Realm realm) {
			// to cope with previously API calls
			return getAll(realm, false);
		}

		public static RealmResults<Update> getAll(Realm realm, boolean excluded) {
			return realm.where(Update.class).equalTo(Update.EXCLUDED, excluded).findAll();
		}

//		public static boolean contains(String packageName, Realm realm) {
//			return realm.where(Update.class).equalTo(Update.PACKAGE_NAME, packageName).findFirst() != null;
//		}

		public static boolean contains(String packageName, boolean excluded, Realm realm) {
			return realm
					.where(Update.class)
					.equalTo(Update.PACKAGE_NAME, packageName)
					.equalTo(Update.EXCLUDED, excluded)
					.findFirst() != null;
		}

		public static void delete(String packageName, Realm realm) {
			Update first = realm.where(Update.class).equalTo(Update.PACKAGE_NAME, packageName).findFirst();
			if (first != null) {
				realm.beginTransaction();
				first.deleteFromRealm();
				realm.commitTransaction();
			}
		}

		public static Update get(String packageName, Realm realm) {
			return realm.where(Update.class).equalTo(Update.PACKAGE_NAME, packageName).findFirst();
		}

		public static void setExcluded(String packageName, boolean excluded, Realm realm) {
			Update update = realm.where(Update.class).equalTo(Update.PACKAGE_NAME, packageName).findFirst();
			if(update!=null) {
				realm.beginTransaction();
				update.setExcluded(excluded);
				realm.commitTransaction();
			} else {
				throw new RuntimeException("Update with package name '"+ packageName +"' not found");
			}
		}
	}

	public static class RollbackQ {

		public static RealmResults<Rollback> getAll(Realm realm) {
			return realm.where(Rollback.class).findAll();
		}

		public static Rollback get(String packageName, Rollback.Action action, Realm realm) {
			RealmResults<Rollback> allSorted = realm.where(Rollback.class)
					.equalTo(Rollback.PACKAGE_NAME, packageName)
					.equalTo(Rollback.ACTION, action.name())
					.findAllSorted(Rollback.TIMESTAMP);

			if (allSorted.size() > 0) {
				return allSorted.get(allSorted.size() - 1);
			} else {
				return null;
			}
		}

		public static void deleteAll(Realm realm) {
			realm.beginTransaction();
			realm.delete(Rollback.class);
			realm.commitTransaction();
		}

		public static void upadteRollbackWithAction(Realm realm, Rollback rollback, Rollback.Action action) {
			realm.beginTransaction();
			rollback.setAction(action.name());
			realm.copyToRealmOrUpdate(rollback);
			realm.commitTransaction();
		}

		public static void upadteRollbackWithAction(Realm realm, String md5, Rollback.Action action) {
			Rollback rollback = realm.where(Rollback.class).equalTo(Rollback.MD5, md5).findFirst();
			upadteRollbackWithAction(realm, rollback, action);
		}

		public static void addRollbackWithAction(Realm realm, GetAppMeta.App app, Rollback.Action action) {
			Rollback rollback = new Rollback(app, action);
			realm.beginTransaction();
			realm.copyToRealmOrUpdate(rollback);
			realm.commitTransaction();
		}
	}

	public static class ExcludedAdsQ {
		public static RealmResults<ExcludedAd> getAll(Realm realm) {
			return realm.where(ExcludedAd.class).findAll();
		}
	}

	public static class ScheduledQ {

		public static RealmResults<Scheduled> getAll(Realm realm) {
			return realm.where(Scheduled.class).findAll();
		}

		public static void delete(Realm realm, Scheduled scheduled) {
			realm.beginTransaction();
			scheduled.deleteFromRealm();
			realm.commitTransaction();
		}
	}

	public static class ReferrerQ {

		public static StoredMinimalAd get(String packageName, Realm realm) {
			return realm.where(StoredMinimalAd.class).equalTo(StoredMinimalAd.PACKAGE_NAME, packageName).findFirst();
		}
	}
}
