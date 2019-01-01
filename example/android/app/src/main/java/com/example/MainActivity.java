package com.example;

import android.content.Intent;

import com.facebook.react.ReactActivity;
import com.zibann.littlehome.picker.PickerModule;

public class MainActivity extends ReactActivity implements PickerModule.ImagePickerClient {

  PickerModule imagePicker;

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "example";
    }


  public void setImagePicker(PickerModule imagePicker) {
    this.imagePicker = imagePicker;
  }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PickerModule.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            imagePicker.handleResponse(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
