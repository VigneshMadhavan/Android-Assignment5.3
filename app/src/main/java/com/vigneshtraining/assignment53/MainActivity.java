package com.vigneshtraining.assignment53;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vigneshtraining.assignment53.adapter.SelectUserAdapter;
import com.vigneshtraining.assignment53.model.SelectUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    public static final int RequestPermissionCode = 1;
    public static final int RequestCallPermissionCode = 2;
    private ListView listView;
    private static final int CALL_REQUEST=101;
    private static final int SMS_REQUEST=102;
    private LoadContact loadContact;
    private SelectUser selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.contacts_list);

        listView.setOnItemLongClickListener(this);
        //
        EnableRuntimePermission();

        loadContacts();
        registerForContextMenu(listView);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle(R.string.contextMenu);

        getMenuInflater().inflate(R.menu.custom_menu, menu);


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        switch (item.getItemId()){

            case R.id.sms:
                sms();
                break;
            case R.id.call:
                call();
                break;



        }





        return super.onContextItemSelected(item);
    }

    private void call(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},CALL_REQUEST);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+selectedUser.getPhone())));
        }
    }

    private void sms(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},SMS_REQUEST);
        } else {


            Uri sms_uri = Uri.parse("smsto:"+selectedUser.getPhone());
            Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
            sms_intent.putExtra("sms_body","Type your message");
            startActivity(sms_intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CALL_REQUEST:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    call();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;
            case SMS_REQUEST:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    sms();
                } else {
                    Log.d("TAG", "SMS Permission Not Granted");
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void loadContacts() {
        loadContact = new LoadContact();
        loadContact.setPhones(getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null));
        loadContact.setResolver(this.getContentResolver());

        loadContact.setSelectUsers(new ArrayList<SelectUser>());
        loadContact.setListView(listView);
        loadContact.setAdapter(new SelectUserAdapter(loadContact.getSelectUsers(), this));
        loadContact.execute();
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {

            Toast.makeText(MainActivity.this, "CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {

        }else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CALL_PHONE}, RequestCallPermissionCode);
        }

    }



    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        selectedUser= loadContact.getSelectUsers().get(position);
        //Toast.makeText(MainActivity.this,loadContact.getSelectUsers().get(position).getName() , Toast.LENGTH_LONG).show();
        return false;
    }
}
