package cm.aptoide.pt.downloadmanager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cm.aptoide.pt.database.realm.Download;
import cm.aptoide.pt.model.v7.GetAppMeta;
import rx.Observable;

/**
 * Created by trinkes on 5/18/16.
 */
public class DownloadService extends Service {

	IBinder binder = new LocalBinder();

	@Override
	public void onCreate() {
		super.onCreate();
		AptoideDownloadManager.getInstance().init(this);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public Observable<Download> startDownload(GetAppMeta.App appToDownload) {
		return AptoideDownloadManager.getInstance().startDownload(appToDownload);
	}

	public class LocalBinder extends Binder {

		public DownloadService getService() {
			// Return this instance of LocalService so clients can call public methods
			return DownloadService.this;
		}
	}
}
