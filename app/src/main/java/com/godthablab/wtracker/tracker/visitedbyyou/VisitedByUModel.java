package com.godthablab.wtracker.tracker.visitedbyyou;

import java.io.File;

/**
 * Created by rao2cool on 19/4/17.
 */

public class VisitedByUModel {

private String imagePath;
private String name;
private File imageFile;

public String getImagePath() {
        return imagePath;
}

public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
}

public String getName() {
        return name;
}

public void setName(String name) {
        this.name = name;
}

public File getImageFile() {
        return imageFile;
}

public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
}

}
