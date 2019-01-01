package com.zibann.littlehome.picker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.reactnative.ivpusic.imagepicker.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class PickerModule extends ReactContextBaseJavaModule {
    private Promise promise;

  public interface ImagePickerClient {
    void setImagePicker(PickerModule imagePicker);
  }

    private static final String E_PERMISSIONS_MISSING = "E_PERMISSION_MISSING";
  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";
    public static final int REQUEST_CODE_CHOOSE = 23;
    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_CALLBACK_ERROR = "E_CALLBACK_ERROR";

  public PickerModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }
    @Override
  public String getName() {
    return "ImagePicker";
  }


  private void initiatePicker(Activity activity) {
        Matisse.from(activity)
          .choose(MimeType.ofImage())
          .theme(R.style.Matisse_Dracula)
          .countable(false)
          // .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
          .maxSelectable(9)
          .originalEnable(true)
          .maxOriginalSize(10)
          .imageEngine(new PicassoEngine())
          .forResult(REQUEST_CODE_CHOOSE);

  }

    @ReactMethod
    public void openPicker(final ReadableMap options, final Promise promise) {

      final Activity activity = getCurrentActivity();
        ImagePickerClient client = (ImagePickerClient) activity;
        client.setImagePicker(this);
        this.promise = promise;

        if (activity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        permissionsCheck(activity, promise, Collections.singletonList(Manifest.permission.WRITE_EXTERNAL_STORAGE), new Callable<Void>() {
            @Override
            public Void call() {
                initiatePicker(activity);
                return null;
            }
        });



    }

  //https://stackoverflow.com/questions/21780252/how-to-use-onactivityresult-method-from-other-than-activity-class
  public void handleResponse(Intent data) {
    try {

      List<Uri> uris = Matisse.obtainResult(data);
      List<String> paths = Matisse.obtainPathResult(data);

      WritableNativeArray arrayResult = new WritableNativeArray();

      for (int i=0; i<uris.size(); i++) {
        Uri uri = uris.get(i);
        String path = paths.get(i);
        WritableMap image = getImage(uri, path);
        arrayResult.pushMap(image);
      }

      // for (int i=0; i<paths.size(); i++) {
      //   System.out.println(paths.get(i));
      // }


      promise.resolve(arrayResult);

    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  // 참조 ivpusic.imagepicker.PickerModule.getImage
  private WritableMap getImage(Uri uri, String path) {
    WritableMap image = new WritableNativeMap();

    image.putString("path", path);

    return image;
  }

    private void permissionsCheck(final Activity activity, final Promise promise, final List<String> requiredPermissions, final Callable<Void> callback) {

        List<String> missingPermissions = new ArrayList<>();

        for (String permission : requiredPermissions) {
            int status = ActivityCompat.checkSelfPermission(activity, permission);
            if (status != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (!missingPermissions.isEmpty()) {

            ((PermissionAwareActivity) activity).requestPermissions(missingPermissions.toArray(new String[missingPermissions.size()]), 1, new PermissionListener() {

                @Override
                public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                    if (requestCode == 1) {

                        for (int grantResult : grantResults) {
                            if (grantResult == PackageManager.PERMISSION_DENIED) {
                                promise.reject(E_PERMISSIONS_MISSING, "Required permission missing");
                                return true;
                            }
                        }

                        try {
                            callback.call();
                        } catch (Exception e) {
                            promise.reject(E_CALLBACK_ERROR, "Unknown error", e);
                        }
                    }

                    return true;
                }
            });

            return;
        }

        // all permissions granted
        try {
            callback.call();
        } catch (Exception e) {
            promise.reject(E_CALLBACK_ERROR, "Unknown error", e);
        }
    }


}
