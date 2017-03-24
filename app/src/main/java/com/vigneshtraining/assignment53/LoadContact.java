package com.vigneshtraining.assignment53;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;


import com.vigneshtraining.assignment53.adapter.SelectUserAdapter;
import com.vigneshtraining.assignment53.model.SelectUser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by vimadhavan on 3/19/2017.
 */

public class LoadContact extends AsyncTask{

    private Cursor phones;
    private ContentResolver resolver;
    private SelectUserAdapter adapter;

    public SelectUserAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(SelectUserAdapter adapter) {
        this.adapter = adapter;
    }



    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    private ListView listView;

    public Cursor getPhones() {
        return phones;
    }

    public void setPhones(Cursor phones) {
        this.phones = phones;
    }

    public ContentResolver getResolver() {
        return resolver;
    }

    public void setResolver(ContentResolver resolver) {
        this.resolver = resolver;
    }

    private ArrayList<SelectUser> selectUsers;

    public ArrayList<SelectUser> getSelectUsers() {
        return selectUsers;
    }

    public void setSelectUsers(ArrayList<SelectUser> selectUsers) {
        this.selectUsers = selectUsers;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        if (phones != null) {

            Log.e("count", "" + phones.getCount());
            if (phones.getCount() == 0) {
                Log.d("Debug","No contacts in your contact list.");
                for (int i=1;i<=10;i++){
                    SelectUser selectUser = new SelectUser();

                    selectUser.setName("Name "+i);
                    selectUser.setPhone("829700026"+i);


                    selectUsers.add(selectUser);
                }
            }

            while (phones.moveToNext()) {
                Bitmap bit_thumb = null;
                String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                try {
                    if (image_thumb != null) {
                        bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                    } else {
                        Log.e("No Image Thumb", "--------------");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                SelectUser selectUser = new SelectUser();
                selectUser.setThumb(bit_thumb);
                selectUser.setName(name);
                selectUser.setPhone(phoneNumber);
                selectUser.setEmail(EmailAddr);

                selectUsers.add(selectUser);
            }
            phones.close();
        } else {
            Log.e("Cursor close 1", "----------------");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        listView.setAdapter(adapter);
        Log.d("onPostExecute", "----------------");
    }
}
