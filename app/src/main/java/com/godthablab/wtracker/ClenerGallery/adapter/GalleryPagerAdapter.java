package com.godthablab.wtracker.ClenerGallery.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.godthablab.wtracker.ClenerGallery.GalleryFragment;
import com.godthablab.wtracker.shared.GallerySetting;

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {

        private GallerySetting gallerySetting;

        public GalleryPagerAdapter(@NonNull FragmentManager fm, GallerySetting gallerySetting) {
                super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                this.gallerySetting=gallerySetting;
        }

        @Override
        public int getCount() {
                return gallerySetting.getTitleStripsName().length;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
                return GalleryFragment.getInstance( gallerySetting.getAssetType()[position],
                        gallerySetting.isReadOnly(),
                        gallerySetting.isMenuDownload(),
                        gallerySetting.isMenuDelete(),
                        gallerySetting.getFileReadPaths()[position],
                        gallerySetting.getFileSavePaths()[position],
                        gallerySetting.getEmptyListMessage()
                );
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
                return gallerySetting.getTitleStripsName()[position];
        }

}

