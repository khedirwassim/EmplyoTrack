package com.example.damproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EmployeeAdapter extends BaseAdapter {
    Context context;
    ArrayList<EmployeeClass> arrayList;

    public EmployeeAdapter(Context context, ArrayList<EmployeeClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        //to get the number  of elements
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //inflate
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.ellement_layout,null);
        ImageView img = convertView.findViewById(R.id.ImageView_image);
        TextView f_name = convertView.findViewById(R.id.tv2_f_name);
        TextView l_name = convertView.findViewById(R.id.tv2_l_name);
        TextView id = convertView.findViewById(R.id.tv2_id);
        //open the employeeclas  based on the array list
        EmployeeClass employeeClass = arrayList.get(position);
        //GET the image and texts from the arraylist
        String fname = employeeClass.f_name;
        String lname = employeeClass.l_name;
        String idD = employeeClass.id;
        byte[] image = employeeClass.image;
        //convert the image to bitmap to sure it
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0, image.length);
        f_name.setText(fname);
        l_name.setText(lname);
        id.setText(idD);
        img.setImageBitmap(bitmap);

        return convertView;
    }
}
