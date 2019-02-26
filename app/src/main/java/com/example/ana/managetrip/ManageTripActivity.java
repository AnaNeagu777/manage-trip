package com.example.ana.managetrip;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class ManageTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int GALLERY_PERMISSION_CODE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final int REQUEST_IMAGE_GET = 4;

    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Button button1 = (Button) findViewById(R.id.start_date_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Start date");
            }
        });

        Button button2 = (Button) findViewById(R.id.end_date_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"End date");
            }
        });

        Button buttonOpenCamera = findViewById(R.id.open_camera);
        buttonOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        Button buttonOpenGallery = findViewById(R.id.open_gallery);
        buttonOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Button saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripEntity e = new TripEntity();
                e.setName(((EditText)findViewById(R.id.editText)).getText().toString());
                e.setRating(((RatingBar)findViewById(R.id.ratingBar)).getRating());
                e.setCountry(((EditText)findViewById(R.id.editText3)).getText().toString());
                e.setStartDate(((TextView)findViewById(R.id.start_date)).getText().toString());
                e.setEndDate(((TextView)findViewById(R.id.end_date)).getText().toString());
                e.setPrice(((SeekBar)findViewById(R.id.seekBar)).getProgress());
                e.setImage(encodedImage);
                Intent intent = new Intent();
                intent.putExtra("entity", e);
                if (getIntent().hasExtra("position")) {
                    intent.putExtra("position", getIntent().getIntExtra("position", 0));
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        TripEntity e = (TripEntity) getIntent().getSerializableExtra("entity");
        if (e != null) {
            ((EditText)findViewById(R.id.editText)).setText(e.getName());
            ((EditText)findViewById(R.id.editText3)).setText(e.getCountry());
            ((RatingBar) findViewById(R.id.ratingBar)).setRating((float)e.getRating());
            ((TextView)findViewById(R.id.start_date)).setText(e.getStartDate());
            ((TextView)findViewById(R.id.end_date)).setText(e.getEndDate());
            ((SeekBar) findViewById(R.id.seekBar)).setProgress(e.getPrice());
            encodedImage = e.getImage();
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = findViewById(R.id.start_date);
        textView.setText(currentDateString);

        TextView textView2 = findViewById(R.id.end_date);
        textView2.setText(currentDateString);
    }

    private void openCamera() {
        String perms = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(this, perms) == PackageManager.PERMISSION_GRANTED) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, perms)) {
                Toast.makeText(this, "Camera access is required for this task.", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{perms},
                    CAMERA_PERMISSION_CODE);
        }
    }

    private void openGallery() {
        String perms = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, perms) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_GET);
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, perms)) {
                Toast.makeText(this, "External access is required for this task.", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{perms},
                    GALLERY_PERMISSION_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else if (requestCode == GALLERY_PERMISSION_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_IMAGE_GET )
                && resultCode == RESULT_OK && data != null) {

            Bitmap bitmap = data.getParcelableExtra("data");

            if (bitmap == null) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bitmap != null) {
                bitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, false);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();

                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            }

        }
    }
}
