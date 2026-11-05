package net.kdt.pojavlaunch.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;

public class ProfileTypeSelectFragment extends Fragment {
    public static final String TAG = "ProfileTypeSelectFragment";
    public ProfileTypeSelectFragment() {
        super(R.layout.fragment_profile_type);
    }
    public ProfileTypeSelectFragment(int layout) {
        super(layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.vanilla_profile).setOnClickListener(v -> Tools.swapFragment(requireActivity(), ProfileEditorFragment.class,
                ProfileEditorFragment.TAG, new Bundle(1)));

        // NOTE: Special care needed! If you wll decide to add these to the back stack, please read
        // the comment in FabricInstallFragment.onDownloadFinished() and amend the code
        // in FabricInstallFragment.onDownloadFinished() and ModVersionListFragment.onDownloadFinished()
        view.findViewById(R.id.optifine_profile).setOnClickListener(v ->
                tryInstall(OptiFineInstallFragment.class, OptiFineInstallFragment.TAG));
        view.findViewById(R.id.modded_profile_fabric).setOnClickListener((v)->
                tryInstall(FabricInstallFragment.class, FabricInstallFragment.TAG));
        view.findViewById(R.id.modded_profile_forge).setOnClickListener((v)->
                tryInstall(ForgeInstallFragment.class, ForgeInstallFragment.TAG));
        view.findViewById(R.id.modded_profile_neoforge).setOnClickListener((v)->
                tryInstall(NeoForgeInstallFragment.class, NeoForgeInstallFragment.TAG));
        view.findViewById(R.id.modded_profile_modpack).setOnClickListener((v)->
                tryInstall(ModpackCreateFragment.class, ModpackCreateFragment.TAG));
        view.findViewById(R.id.modded_profile_lwjgl3ify).setOnClickListener((v)->
                tryInstall(LWJGL3ifyInstallFragment.class, LWJGL3ifyInstallFragment.TAG));
        view.findViewById(R.id.modded_profile_quilt).setOnClickListener((v)->
                tryInstall(QuiltInstallFragment.class, QuiltInstallFragment.TAG));
        view.findViewById(R.id.modded_profile_bta).setOnClickListener((v)->
                tryInstall(BTAInstallFragment.class, BTAInstallFragment.TAG));
    }

    private void tryInstall(Class<? extends Fragment> fragmentClass, String tag){
        if (Tools.isDemoProfile(requireContext())) {
            Toast.makeText(requireContext(), R.string.toast_not_available_demo, Toast.LENGTH_LONG).show();
            return;
        }
        if (Tools.isLocalProfile(requireContext())) {
            boolean supportedOfflineInstaller = fragmentClass == OptiFineInstallFragment.class
                    || fragmentClass == FabricInstallFragment.class
                    || fragmentClass == QuiltInstallFragment.class
                    || fragmentClass == ForgeInstallFragment.class
                    || fragmentClass == NeoForgeInstallFragment.class;
            if (!supportedOfflineInstaller) {
                Tools.dialog(requireContext(), getString(R.string.global_warning),
                        getString(R.string.zed_local_unsupported_installer));
                return;
            }
            if (!Tools.hasInstalledVanillaVersions()) {
                Tools.dialog(requireContext(), getString(R.string.global_warning),
                        getString(R.string.zed_local_no_vanilla_version));
                return;
            }
        }
        Tools.swapFragment(requireActivity(), fragmentClass, tag, null);
    }
}
