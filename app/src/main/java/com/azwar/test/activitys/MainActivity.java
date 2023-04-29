package com.azwar.test.activitys;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.azwar.test.R;
import com.azwar.test.databases.ResultDatabase;
import com.azwar.test.models.ResultModel;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MainActivity extends AppCompatActivity {

    public MainActivity mainActivity;

    boolean doubleBackToExitPressedOnce = false;

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            ActivityCompat.finishAffinity(this);
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan kembali untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }


    EditText inputET;

    TextView resultTV;

    ImageView mPreviewIv;

    FloatingActionButton listItem;

    ResultDatabase resultDatabase;
    ResultModel resultModel;


    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    String[] cameraPermission;
    String[] storagePermission;

    Uri image_uri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        inputET = findViewById(R.id.inputET);
        resultTV = findViewById(R.id.resultTV);
        mPreviewIv = findViewById(R.id.mPreviewIV);
        listItem = findViewById(R.id.listItem);

        resultDatabase = ResultDatabase.getInstance(this);

        resultDatabase = ResultDatabase.getInstance(this);


        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};


        listItem.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ListResultActivity.class)));
    }

    //action bar menu *
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    //handle actionbar item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addImage) {
            showImageImportDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {
        String[] items = {" Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select Image!");
        dialog.setItems(items, (dialog1, which) -> {
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermissions();
                } else {
                    pickCamera();
                }

            }
            if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermissions();
                } else {
                    pickGallery();
                }

            }
        });
        dialog.create().show();
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {
        //intent to take image image from camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New_Picture"); //Title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image_To_Text");
        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    //Handle permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted) {
                        pickCamera();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (writeStorageAccepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //Got image from Gallery now Crop it
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);  // Enable Image Guidelines

            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //Got image from Camera now Crop it
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this);  //Enable Image Guidelines
            }
        }

        // Get Cropped Image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri(); //Get Image Uri

                //Set Image to Image view
                mPreviewIv.setImageURI(resultUri);
                Log.e("URI: ", resultUri.toString());

                //Get Drawable Bitmap for text Recognition
                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                Log.e("BitmapDrawable: ", bitmap.toString());


                TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    Log.e("FRAME: ", frame.toString());
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();

                    // Get Text Until there is No Text
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
//                        sb.append("\n");
                    }

                    // Set Text to Edit Text
                    inputET.setText(sb.toString());
                    Log.e("Input ", sb.toString());

                    ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("rhino");
                    Object answer = null;
                    try {
                        answer = scriptEngine.eval(sb.toString());
                    } catch (ScriptException e) {
                        Toast.makeText(this, "Oops, Operation Not Found!, Try Again with + * / -", Toast.LENGTH_SHORT).show();
                        Log.e("CATCH: ", e.toString());
                    }

                    Toast.makeText(this, "Sip, data Kamu sudah berhasil masuk database!", Toast.LENGTH_LONG).show();

                    resultTV.setText(String.valueOf(answer));
                    Log.d("ANSWER: ", String.valueOf(answer));

                    if (answer != null) {
                        resultModel = new ResultModel();
                        resultModel.setResult(answer.toString());
                        resultModel.setInput(sb.toString());
                        resultModel.setImage(resultUri.toString());

                        Long notifId = resultDatabase.resultDAO().userResult(resultModel);
                        Log.d("save to room db id " , String.valueOf(notifId));
                        resultModel = resultDatabase.resultDAO().findByIdResult(notifId);
                        Log.d("save to room db list " , resultModel.toString());
                    }

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //If There is An Error
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}