package com.example.l6;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity
{
    Button but,readContact;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        but = (Button) findViewById(R.id.but1);
        but.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v)
            {
                Intent int1 = new Intent();
                PendingIntent pi1 = PendingIntent.getActivity(MainActivity.this, 0, int1, 0);
                Notification not1 = new Notification.Builder(MainActivity.this)
                        .setTicker("Ticker Title")
                        .setContentTitle("Your Notification")
                        .setContentText("This is Notification Example")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .addAction(R.mipmap.ic_launcher, "Action 1", pi1)
                        .addAction(R.mipmap.ic_launcher_round, "Action 2", pi1)
                        .setContentIntent(pi1).getNotification();
                not1.flags = Notification.FLAG_AUTO_CANCEL;
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, not1);
            }
        });

        readContact = (Button) findViewById(R.id.but2);
        readContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode)
        {
            case (1):
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst())
                    {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1"))
                        {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();
                            //setCn(cNumber);
                        }
                    }
                }
        }
    }
}