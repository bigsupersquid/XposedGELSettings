package de.theknut.xposedgelsettings.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import de.theknut.xposedgelsettings.R;
import de.theknut.xposedgelsettings.hooks.Common;

public class FragmentImExport extends FragmentBase {
	
	public FragmentImExport() { }
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	View rootView = inflater.inflate(R.layout.options_fragment, container, false);
        addPreferencesFromResource(R.xml.imexport_fragment);
        
        OnPreferenceClickListener BackupRestoreHomescreenListener = new OnPreferenceClickListener() {
        	
        	String copyCommand = "cat ";
        	
            @SuppressLint("WorldReadableFiles")
			public boolean onPreferenceClick(Preference preference) {
            	String path = Environment.getExternalStorageDirectory().getPath();
            	String sd_trebuchet = path + "/XposedGELSettings/Trebuchet/";
            	String data_trebuchet = "/data/data/" + Common.TREBUCHET_PACKAGE + "/";
            	String sd_gel = path + "/XposedGELSettings/GEL/";
            	String data_gel = "/data/data/" + Common.GEL_PACKAGE + "/";
            	File sdcard_xgels = new File(path + "/XposedGELSettings/" + Common.PREFERENCES_NAME + ".xml");
            	File sdcard_gels = new File(sd_gel + Common.PREFERENCES_NAME + ".xml");
            	File sdcard_trebuchet = new File(sd_trebuchet + Common.PREFERENCES_NAME + ".xml");
            	String sdcard_gel_launcherDB = sd_gel + "launcher.db";
            	String sdcard_gel_launcherDBJournal = sd_gel + "launcher.db-journal";
            	String data_gel_launcherDB = data_gel + "databases/launcher.db";
            	String data_gel_launcherDBJournal = data_gel + "databases/launcher.db-journal";
            	String sdcard_trebuchet_launcherDB = sd_trebuchet + "launcher.db";
            	String sdcard_trebuchet_launcherDBJournal = sd_trebuchet + "launcher.db-journal";
            	String data_trebuchet_launcherDB = data_trebuchet + "databases/launcher.db";
            	String data_trebuchet_launcherDBJournal = data_trebuchet + "databases/launcher.db-journal";
            	
            	sdcard_xgels.getParentFile().mkdirs();            	
            	
            	if (preference.getKey().contains("restorehomescreen")) {
            		
					if (isPackageInstalled(Common.GEL_PACKAGE, mContext)) {
						File launcherDBJournal = new File(data_gel_launcherDBJournal);
						
						if (launcherDBJournal.length() == 0) {
							// copy from sdcard to data
							String sd2data_1 = copyCommand + sdcard_gel_launcherDB + " > " + data_gel_launcherDB;
							copy(Common.GEL_PACKAGE, sd2data_1, true);
						}
						else {
							// copy from sdcard to data
							String sd2data_1 = copyCommand + sdcard_gel_launcherDB + " > " + data_gel_launcherDB;
							String sd2data_2 = copyCommand + sdcard_gel_launcherDBJournal + " > " + data_gel_launcherDBJournal;
							copy(Common.GEL_PACKAGE, sd2data_1, sd2data_2, true);
						}
					}
					
					if (isPackageInstalled(Common.TREBUCHET_PACKAGE, mContext)) {
						File launcherDBJournal = new File(data_trebuchet_launcherDBJournal);
						
						if (launcherDBJournal.length() == 0) {
							// copy from sdcard to data
							String sd2data_3 = copyCommand + sdcard_trebuchet_launcherDB + " > " + data_trebuchet_launcherDB;
							String sd2data_4 = copyCommand + sdcard_trebuchet_launcherDBJournal + " > " + data_trebuchet_launcherDBJournal;
							copy(Common.TREBUCHET_PACKAGE, sd2data_3, sd2data_4, true);
						}
						else {
							
						}
					}
					
					restartLauncher();
            	}
            	else if (preference.getKey().contains("backuphomescreen")) {
            		boolean success = false;
            		
					if (isPackageInstalled(Common.GEL_PACKAGE, mContext)) {
						File launcherDBJournal = new File(data_gel_launcherDBJournal);
						
						if (launcherDBJournal.length() == 0) {
							String data2sd_1 = copyCommand + data_gel_launcherDB + " > " + sdcard_gel_launcherDB;
							sdcard_gels.getParentFile().mkdirs();
							copy(Common.GEL_PACKAGE, data2sd_1, false);
						}
						else {
							String data2sd_1 = copyCommand + data_gel_launcherDB + " > " + sdcard_gel_launcherDB;
							String data2sd_2 = copyCommand + data_gel_launcherDBJournal + " > " + sdcard_gel_launcherDBJournal;
							sdcard_gels.getParentFile().mkdirs();
							copy(Common.GEL_PACKAGE, data2sd_1, data2sd_2, false);
						}
					}
					
					if (isPackageInstalled(Common.TREBUCHET_PACKAGE, mContext)) {
						File launcherDBJournal = new File(data_trebuchet_launcherDBJournal);
						
						if (launcherDBJournal.length() == 0) {
							String data2sd_1 = copyCommand + data_trebuchet_launcherDB + " > " + sdcard_trebuchet_launcherDB;
							sdcard_trebuchet.getParentFile().mkdirs();
							copy(Common.TREBUCHET_PACKAGE, data2sd_1, false);
						}
						else {
							String data2sd_3 = copyCommand + data_trebuchet_launcherDB + " > " + sdcard_trebuchet_launcherDB;
							String data2sd_4 = copyCommand + data_trebuchet_launcherDBJournal + " > " + sdcard_trebuchet_launcherDBJournal;
							sdcard_trebuchet.getParentFile().mkdirs();
							copy(Common.TREBUCHET_PACKAGE, data2sd_3, data2sd_4, false);
						}
					}
            	}
            	
            	return true;
            }
            
            private boolean isPackageInstalled(String packagename, Context context) {
                PackageManager pm = context.getPackageManager();
                try {
                    pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
                    return true;
                } catch (NameNotFoundException e) {
                    return false;
                }
            }

            public boolean restartLauncher() {
            	 Context context = mContext;
	       		 ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	          	 String msg = "Killed:\n";
	          	 
	          	 List<RunningAppProcessInfo> processes = am.getRunningAppProcesses();
	          	 for (RunningAppProcessInfo process : processes) {
	          		 if (Common.PACKAGE_NAMES.contains(process.processName)) {   			 
	          			 am.killBackgroundProcesses(process.processName);
	          			 msg += process.processName + "\n"; 
	          		 }                        			 
	          	 }
	          	 
	          	 if (msg.equals("Killed:\n")) {
	          		 msg = msg.substring(0, msg.lastIndexOf('\n')) + " " + context.getString(R.string.toast_reboot_failed_nothing_msg) + "... :(\n" + context.getString(R.string.toast_reboot_failed);
	          	 }
	          	 else {
	          		 msg = msg.substring(0, msg.lastIndexOf('\n'));
	          	 }
	          	 
	          	 Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	          	 return true;
            }
            
            public boolean copy(String packageName, String command1, String command2, boolean setPermissions) {
            	
            	String data_launcherDB = "/data/data/" + packageName + "/databases/launcher.db";
            	String data_launcherDBJournal = "/data/data/" + packageName + "/databases/launcher.db-journal";
            	
            	ArrayList<String> commands = new ArrayList<String>();
            	commands.add("su");
            	commands.add(command1);
            	commands.add(command2);

        	   if (setPermissions) {
        		   commands.add("chmod 0660 " + data_launcherDB);	
        		   commands.add("chmod 0700 " + data_launcherDBJournal);
    		   }
        	   
        	   CommonUI.openRootShell(commands.toArray(new String[commands.size()]));
        	   
        	   return true;
            }
            
            public boolean copy(String packageName, String command1, boolean setPermissions) {
 
            	String data_launcherDB = "/data/data/" + packageName + "/databases/launcher.db";
            	ArrayList<String> commands = new ArrayList<String>();
            	commands.add("su");
            	commands.add(command1);
            	
            	if (setPermissions) {
         		   commands.add("chmod 0660 " + data_launcherDB);	
     		   }
        	   
        	   CommonUI.openRootShell(commands.toArray(new String[commands.size()]));
        	   
        	   return true;
            }
        };
        
        this.findPreference("backuphomescreen").setOnPreferenceClickListener(BackupRestoreHomescreenListener);
        this.findPreference("restorehomescreen").setOnPreferenceClickListener(BackupRestoreHomescreenListener);
        
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        
        if (!InAppPurchase.isDonate) {        	
        	for (int i = 0; i < preferenceScreen.getPreferenceCount(); i++) {
        		Preference pref = preferenceScreen.getPreference(i);
        		
        		if (pref.hasKey() && pref.getKey().equals("needsDonate")) {
        			continue;
        		}
        		
        		pref.setEnabled(false);
        	}
        	
        	this.findPreference("needsDonate").setOnPreferenceClickListener(new OnPreferenceClickListener() {
				
				@Override
				public boolean onPreferenceClick(Preference preference) {
					CommonUI.needFullReboot = true;
					return false;
				}
			});
        }
        else {
        	preferenceScreen.removePreference(this.findPreference("needsDonate"));
        }
        
        rootView = CommonUI.setBackground(rootView, R.id.prefbackground);
        
        return rootView;
    }
}