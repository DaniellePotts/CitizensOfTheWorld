package com.example.danie.comcet325bg46ic.helpers;

import android.content.Intent;
import android.provider.MediaStore;

/**
 * Created by danie on 31/12/2016.
 */
public class ImageGetIntent {

    public static int ActivityCode = 0;

    public static Intent SetImageIntent(boolean camera, boolean fromGallery){
        Intent imageIntent = null;
        if(fromGallery){
            imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            ActivityCode = 3;
        }
        else if (camera){
            imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ActivityCode = 2;
        }

        return imageIntent;
    }
}
