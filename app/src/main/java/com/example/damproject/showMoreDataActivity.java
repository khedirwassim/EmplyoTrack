package com.example.damproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class showMoreDataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more_data);
        ImageView d_image = findViewById(R.id.imageViewDataShow);
        TextView d_id = findViewById(R.id.tVSR1);
        TextView d_f_name = findViewById(R.id.tVSR2);
        TextView d_l_name = findViewById(R.id.tVSR3);
        TextView d_phone = findViewById(R.id.tVSR4);
        TextView d_mail = findViewById(R.id.tVSR5);
        Button buttonCall=findViewById(R.id.buttoncall);
        Button buttonSMS=findViewById(R.id.buttonsms);
        Button buttonMAIL=findViewById(R.id.buttonM);
        EmployeeDataHelper employeeDb = new EmployeeDataHelper(this);
        ArrayList<EmployeeClass> arrayList = employeeDb.getEmployeeData();
        //we need to get the item positin from the main activity (Intent)
        Intent intent = getIntent();
        String pos = intent.getStringExtra("POSITION");
        int position = Integer.parseInt(pos);
        EmployeeClass employeeClass = arrayList.get(position);
        //now we can get the data of sepesific employee
        String id = employeeClass.getId();
        String f_name = employeeClass.getF_name();
        String l_name = employeeClass.getL_name();
        String phone = employeeClass.getPhone();
        String mail = employeeClass.getMail();
        byte[] image = employeeClass.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        d_id.setText(id);
        d_f_name.setText(f_name);
        d_l_name.setText(l_name);
        d_phone.setText(phone);
        d_mail.setText(mail);
        d_image.setImageBitmap(bitmap);

        //setOnClickListener to call button by creating an INTENT
        buttonCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                //send phone number  variable
                //and select only app that can make call
                intent1.setData(Uri.parse("tel:"+phone));
                // lance activity
                startActivity(intent1);
            }
        });
//setOnClickListener to sms button by creating an INTENT
        buttonSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent
                Intent intent2 = new Intent(Intent.ACTION_SENDTO);
                //send phone number  variable
                //and select only app that can send sms
                intent2.setData(Uri.parse("sms:"+phone));
                // lance activity
                startActivity(intent2);

            }
        });
//setOnClickListener to mail button by creating an INTENT
        buttonMAIL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent
                Intent intent3 = new Intent(Intent.ACTION_SENDTO);
                //send email variable
                //and select only app that can send email
                intent3.setData(Uri.parse("mailto:"+mail));
                // lance activity
                startActivity(intent3);

            }
        });
    }
}
