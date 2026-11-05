package net.kdt.pojavlaunch.fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.ExpandableListAdapter;

import net.kdt.pojavlaunch.JavaGUILauncherActivity;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.modloaders.ModloaderListenerProxy;
import net.kdt.pojavlaunch.modloaders.OptiFineDownloadTask;
import net.kdt.pojavlaunch.modloaders.OptiFineUtils;
import net.kdt.pojavlaunch.modloaders.OptiFineVersionListAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class OptiFineInstallFragment extends ModVersionListFragment<OptiFineUtils.OptiFineVersions> {
    public static final String TAG = "OptiFineInstallFragment";
    public OptiFineInstallFragment() {
        super(TAG);
    }
    @Override
    public int getTitleText() {
        return R.string.of_dl_select_version;
    }

    @Override
    public int getNoDataMsg() {
        return R.string.of_dl_failed_to_scrape;
    }
    @Override
    public OptiFineUtils.OptiFineVersions loadVersionList() throws IOException {
        OptiFineUtils.OptiFineVersions versions = OptiFineUtils.downloadOptiFineVersions();
        if (versions == null || !mLocalProfile || versions.minecraftVersions == null || versions.optifineVersions == null) return versions;

        OptiFineUtils.OptiFineVersions installedOnly = new OptiFineUtils.OptiFineVersions();
        installedOnly.minecraftVersions = new ArrayList<>();
        installedOnly.optifineVersions = new ArrayList<>();
        for (int i = 0; i < versions.minecraftVersions.size(); i++) {
            String gameVersion = versions.minecraftVersions.get(i);
            if (Tools.isMinecraftVersionInstalled(gameVersion)) {
                installedOnly.minecraftVersions.add(gameVersion);
                installedOnly.optifineVersions.add(versions.optifineVersions.get(i));
            }
        }
        return installedOnly;
    }

    @Override
    protected boolean isVersionListEmpty(OptiFineUtils.OptiFineVersions versions) {
        return versions != null && (versions.minecraftVersions == null || versions.minecraftVersions.isEmpty());
    }

    @Override
    public ExpandableListAdapter createAdapter(OptiFineUtils.OptiFineVersions versionList, LayoutInflater layoutInflater) {
        return new OptiFineVersionListAdapter(versionList, layoutInflater);
    }

    @Override
    public Runnable createDownloadTask(Object selectedVersion, ModloaderListenerProxy listenerProxy) {
        return new OptiFineDownloadTask((OptiFineUtils.OptiFineVersion) selectedVersion, listenerProxy, requireActivity());
    }

    @Override
    public void onDownloadFinished(Context context, File downloadedFile) {
        Intent modInstallerStartIntent = new Intent(context, JavaGUILauncherActivity.class);
        OptiFineUtils.addAutoInstallArgs(modInstallerStartIntent, downloadedFile);
        context.startActivity(modInstallerStartIntent);
    }
}
