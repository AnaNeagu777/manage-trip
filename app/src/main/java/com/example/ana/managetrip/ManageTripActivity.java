package com.example.ana.managetrip;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class ManageTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int GALLERY_PERMISSION_CODE = 2;

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
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

//        if (view.getTag().equals("Start date")) {
            TextView textView = findViewById(R.id.start_date);
            textView.setText(currentDateString);
//        } else {
            TextView textView2 = findViewById(R.id.end_date);
            textView2.setText(currentDateString);
//        }
    }

    private void openCamera() {
        String perms = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(this, perms) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
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
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setType("image/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
}
