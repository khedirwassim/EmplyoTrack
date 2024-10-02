package com.example.damproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //declaration d'un object de type EmployeeDataHelper
    EmployeeDataHelper employeeDb;
    ImageView image;
    ListView listView;
    ArrayList<EmployeeClass> arrayList;
    EmployeeAdapter adapter ;
    //variables to show f_name and L_name in the menu bare
    String did; //to put id here evrey time we want to delete
    String dname;
    String dname2;
    EmployeeClass employeeClass;
    TextView CentreText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // creation dun nouveau object
        employeeDb = new EmployeeDataHelper(this);
        //cast the listView
        listView= findViewById(R.id.listView);
        // create method to show data
        showEmployeeData();
        //when an item cliked
        //move from mainActivity to showMoreDataActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, showMoreDataActivity.class);
                intent.putExtra("POSITION", String.valueOf(position));
                startActivity(intent);
         }
        });
        //connect the list view to the abs option menu
        listView.setMultiChoiceModeListener(modeListener);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
    }

    private void showEmployeeData() {
        //pass to the method getEmployeedata
        arrayList= employeeDb.getEmployeeData();
        //define the adapterBase on the array list
        adapter = new EmployeeAdapter(this,arrayList);
        listView.setAdapter(adapter);
         //this line to update adapter
        adapter.notifyDataSetChanged();

        // every time we open the application all data will be shown in the list view
        //and when we insert  a new data we will just copy the methode and insert it
    }

    // CREATE OPTION MENU TO INSERT NEW EMPLOYEE DATA
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu it self
        getMenuInflater().inflate(R.menu.insertdatemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //ON OPTION ITEM SELECTED (TO GIVE AN ACTION TO THE BUTTON )

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        //connect the action insertdatamenu here and inflate a datainsertlayout
        LayoutInflater inflater= (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate this layout using view
        View view=  inflater.inflate(R.layout.datainsertlayout,null);
        final EditText ET_id = view.findViewById(R.id.ET_id);
        final EditText ET_f_name = view.findViewById(R.id.ET_f_name);
        final EditText ET_l_name = view.findViewById(R.id.ET_l_name);
        final EditText ET_phone = view.findViewById(R.id.ET_phone);
        final EditText ET_mail = view.findViewById(R.id.ET_mail);
         image = view.findViewById(R.id.selectedimage);
        //create on click listener for this image to add image on click
       image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               boolean pick = true;
               if (pick== true)
               {
                    if(!checkCameraPermession())
                    {
                        requestCameraPermision();
                    }
                    else
                    {
                        pickImage();
                    }
               }
               else
               {
                   if(!checkStoragePermession())
                   {
                       requestStoragePermision();
                   }
                   else
                   {
                       pickImage();
                   }
               }

           }
       });
        //insert this layout inside Alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // create  builder and insert  this view to inflate the insertdatalayour
        builder.setView(view)
                .setTitle("Ajouter un employe")
                .setIcon(R.drawable.ic_baseline_person_add_alt_1_24)
                .setPositiveButton("ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    String id = ET_id.getText().toString();
                    String fname = ET_f_name.getText().toString();
                    String lname = ET_l_name.getText().toString();
                    String phone = ET_phone.getText().toString();
                    String mail = ET_mail.getText().toString();
                    boolean res = employeeDb.insertIntoDataBase(id,fname,lname,phone,mail,ImageToByte(image));
                    if (res == true)
                    {
                        //by calling this method any new employee will be shown  in the list view
                        showEmployeeData();

                        Toast.makeText(MainActivity.this, "Ajoute avec succes", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "L\'operation a echoue", Toast.LENGTH_SHORT).show();
                    }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();



        return super.onOptionsItemSelected(item);
    }

    private byte[] ImageToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream stream = getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap= BitmapFactory.decodeStream(stream);
                    image.setImageBitmap(bitmap);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void pickImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);


    }

    private void requestStoragePermision() {
        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},100);


    }

    private void requestCameraPermision() {

        requestPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }


    private boolean checkStoragePermession() {
        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== getPackageManager().PERMISSION_GRANTED;
        return result1;
    }

    private boolean checkCameraPermession() {

        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== getPackageManager().PERMISSION_GRANTED;
        boolean result2= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== getPackageManager().PERMISSION_GRANTED;
      return result1 && result2;
    }

    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
            employeeClass= arrayList.get(position);

            did=employeeClass.getId();
            dname=employeeClass.getF_name();
            dname2=employeeClass.getL_name();
            //here we wll save emloye id,fname and lname
            actionMode.setTitle(dname+' '+ dname2);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.abs_menu,menu );
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }
        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if(menuItem.getTitle().equals("delete")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirmer suppression ")
                        .setMessage("Etes-vous sur que vous vouler supprimer " + dname + " " + dname2)
                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int result = employeeDb.deleteFromDataBase(did);
                                if (result > 0) {
                                    showEmployeeData();
                                    //to finich the abs option menu
                                    actionMode.finish();
                                    Toast.makeText(MainActivity.this, dname + " " + dname2 + " a ete supprimer", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "L'operation a echoue", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
            }
            else if (menuItem.getTitle().equals("update"))
            {
                //connect the action insertdatamenu here and inflate a datainsertlayout
                LayoutInflater inflater= (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                //inflate this layout using view
                View view=  inflater.inflate(R.layout.datainsertlayout,null);
                final EditText ET_id = view.findViewById(R.id.ET_id);
                final EditText ET_f_name = view.findViewById(R.id.ET_f_name);
                final EditText ET_l_name = view.findViewById(R.id.ET_l_name);
                final EditText ET_phone = view.findViewById(R.id.ET_phone);
                final EditText ET_mail = view.findViewById(R.id.ET_mail);
                image = view.findViewById(R.id.selectedimage);
                String oldId = employeeClass.getId();
                String oldF_name = employeeClass.getF_name();
                String oldL_name = employeeClass.getL_name();
                String oldPhone = employeeClass.getPhone();
                String oldMail = employeeClass.getMail();
                byte[] oldImage = employeeClass.getImage();
                ET_id.setText(did);
                ET_f_name.setText(dname);
                ET_l_name.setText(dname2);
                ET_phone.setText(oldPhone);
                ET_mail.setText(oldMail);
                Bitmap bitmap =BitmapFactory.decodeByteArray(oldImage,0,oldImage.length);
                image.setImageBitmap(bitmap);

                //create on click listener for this image to add image on click
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean pick = true;
                        if (pick== true)
                        {
                            if(!checkCameraPermession())
                            {
                                requestCameraPermision();
                            }
                            else
                            {
                                pickImage();
                            }
                        }
                        else
                        {
                            if(!checkStoragePermession())
                            {
                                requestStoragePermision();
                            }
                            else
                            {
                                pickImage();
                            }
                        }

                    }
                });
                //insert this layout inside Alertdialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // create  builder and insert  this view to inflate the insertdatalayour
                builder.setView(view)
                        .setTitle("Mettre a jour les informations d\'un employe")
                        .setIcon(R.drawable.ic_baseline_person_add_alt_1_24)
                        .setPositiveButton("Mis a jour", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String id = ET_id.getText().toString();
                                String fname = ET_f_name.getText().toString();
                                String lname = ET_l_name.getText().toString();
                                String phone = ET_phone.getText().toString();
                                String mail = ET_mail.getText().toString();
                                boolean res = employeeDb.updateData (did,fname,lname,phone,mail,ImageToByte(image));
                                if (res == true)
                                {
                                    //by calling this method any new employee will be shown  in the list view
                                    showEmployeeData();

                                    Toast.makeText(MainActivity.this, "mis a jour effectuer", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "L\'operation a echoue", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        };
    };
    void ifListVide()
    {

        if(employeeDb.countData()==0){
            CentreText.setVisibility(View.VISIBLE);
        }
    }
}

