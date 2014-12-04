package com.lasalara.lasalara;

import com.lasalara.lasalara.backend.constants.StringConstants;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class BackupHelper extends BackupAgentHelper {
	// The name of the SharedPreferences file
	static final String PREFERENCES = "userPreferences";
	// The SQLite database file
	static final String DATABASE_FILE = LasaLaraApplication.getCurrentContext().getDatabasePath(StringConstants.DATABASE_NAME).getPath();
	// Keys to uniquely identify the set of backup data
	static final String PREFERENCES_BACKUP_KEY = "userPreferencesBackup";
	static final String FILES_BACKUP_KEY = "filesBackup";

	@Override
	public void onCreate() {
		SharedPreferencesBackupHelper preferenceHelper = new SharedPreferencesBackupHelper(this, PREFERENCES);
		addHelper(PREFERENCES_BACKUP_KEY, preferenceHelper);
		FileBackupHelper filesHelper = new FileBackupHelper(this, DATABASE_FILE);
        addHelper(FILES_BACKUP_KEY, filesHelper);
	};
}
