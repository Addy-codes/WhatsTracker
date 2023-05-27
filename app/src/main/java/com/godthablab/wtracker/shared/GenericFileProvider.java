package com.godthablab.wtracker.shared;

import androidx.core.content.FileProvider;

/**
 * For Apis target 27+
 * @see "https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed/42103911"
 *
 * Or it will throw FileExposedException
 */
public class GenericFileProvider extends FileProvider {}
