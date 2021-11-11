package com.example.dataquery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_STORAGE = 101;
    private static boolean READ_STORAGE_GRANTED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int hasReadContactPremission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(hasReadContactPremission == PackageManager.PERMISSION_GRANTED)
            getContacts();
        else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_READ_STORAGE);
                }
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_READ_STORAGE) {

            if (grantResults.length > 0 ) {
                READ_STORAGE_GRANTED = true;
                //Toast.makeText(this, "разрешения!!", Toast.LENGTH_LONG).show();
            }
        }

        if(READ_STORAGE_GRANTED)
            getContacts();
        else
            Toast.makeText(this, "Требуется установить разрешения", Toast.LENGTH_LONG).show();
    }

    private void getContacts(){
        ContentResolver contentResolver = getContentResolver();


        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);

        ArrayList<String> contacts = new ArrayList<>();

        if(cursor!=null){
            while (cursor.moveToNext()){
                @SuppressLint("Range")
                String contact = cursor.getString(
                        cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                contacts.add(contact);
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, contacts);

        ListView contactList = findViewById(R.id.lstView);

        contactList.setAdapter(adapter);

    }
}