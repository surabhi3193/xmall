package com.mindinfo.xchangemall.xchangemall.activities.job_Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getImageUri;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getRealPathFromURI;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.isMatch;
import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class ApplyJobActivity extends AppCompatActivity implements View.OnClickListener {

    Uri imageUri;
    private Typeface face;
    private EditText fullnameEt, phoneEt, emailEt, aboutmeEt, titleET1, titleET2, titleET3, schoolET1, addressET1, fromET1, toET1, descrET1, schoolET2, addressET2, fromET2, toET2, descrET2, schoolET3, addressET3, fromET3, toET3, descrET3, skillET1, skillET2, skillET3, skillET4,
            skillET5, extranoteET,
            job_titleET1, job_titleET2, job_titleET3, job_schoolET1, job_addressET1, job_fromET1, job_toET1, job_descrET1, job_schoolET2, job_addressET2, job_fromET2, job_toET2, job_descrET2;
    private LinearLayout edu2, edu3, postImageLay, work_lay2;
    private int educount = 1;
    private int skillcount = 1;
    private TextView fullnameheader, phone_header, email_header, aboutme_header, education_header, title_header1, title_header2, title_header3, school_header1, address_header1, duration_header1,
            from_TV1, to_TV1, descTV1, school_header2, address_header2, duration_header2,
            from_TV2, to_TV2, descTV2, school_header3, address_header3, duration_header3, from_TV3, to_TV3, descTV3, addeduTV, skill_head,
            addskillTV, addextraTV, attach_cvTV, attachIDTV, pageTitleTV, job_title_header1, job_title_header2, job_title_header3, job_school_header1, job_address_header1, job_duration_header1,
            job_from_TV1, job_to_TV1, job_descTV1, job_school_header2, job_address_header2, job_duration_header2,
            job_from_TV2, job_to_TV2, job_descTV2, job_school_header3, job_address_header3, job_duration_header3, job_from_TV3, job_to_TV3, job_descTV3, add_work, remove_edu, remove_work, rightTV;
    private String post_id;
    private int ATTACH_CV = 7;
    private File cvFile, idFile;
    private Button apply_job_btn;
    private ImageView back_btn;
    private String PathHolder;
    private String fullname = "", phone = "", email = "", aboutme = "", title1 = "", title2 = "",
            title3 = "", school1 = "", address1 = "", from1 = "", to1 = "", descr1 = "", school2 = "",
            address2 = "", from2 = "", to2 = "", descr2 = "", school3 = "", address3 = "", from3 = "",
            to3 = "", descr3 = "", skill1 = "", skill2 = "", skill3 = "", skill4 = "", skill5 = "",
            extranote = "", job_title1 = "", job_title2 = "",
            job_title3 = "", job_school1 = "", job_address1 = "", job_from1 = "", job_to1 = "", job_descr1 = "", job_school2 = "",
            job_address2 = "", job_from2 = "", job_to2 = "", job_descr2 = "", job_school3 = "", job_address3 = "", job_from3 = "",
            job_to3 = "", savedApp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);
        face = Typeface.createFromAsset(getAssets(),
                "fonts/estre.ttf");
        init();
        chagnefont(face);
        clickListners();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            post_id = bundle.getString("post_id", "");
        pageTitleTV.setText("Application Form");

        savedApp = getData(getApplicationContext(), "saved_job_application", "");

        System.out.println("=================== saved data ==============");
        System.out.println(savedApp);
        if (savedApp != null && savedApp.length() > 0)
            getSavedData(savedApp);

    }


    private void clickListners() {

        addeduTV.setOnClickListener(this);
        remove_edu.setOnClickListener(this);
        remove_work.setOnClickListener(this);
        addskillTV.setOnClickListener(this);
        add_work.setOnClickListener(this);

        attach_cvTV.setOnClickListener(this);
        attachIDTV.setOnClickListener(this);
        apply_job_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        rightTV.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        if (postImageLay.getVisibility() == View.VISIBLE)
            postImageLay.setVisibility(View.GONE);
        else {
            super.onBackPressed();
            finish();
        }
    }

    private void init() {


        edu2 = (LinearLayout) findViewById(R.id.edu2);
        edu3 = (LinearLayout) findViewById(R.id.edu3);
        work_lay2 = (LinearLayout) findViewById(R.id.job_edu2);
        postImageLay = (LinearLayout) findViewById(R.id.postImageLay);

        rightTV = (TextView) findViewById(R.id.rightTV);
        fullnameheader = (TextView) findViewById(R.id.fullname_header);
        phone_header = (TextView) findViewById(R.id.phone_header);
        email_header = (TextView) findViewById(R.id.email_header);
        aboutme_header = (TextView) findViewById(R.id.aboutme_header);
        duration_header1 = (TextView) findViewById(R.id.duration_header1);
        duration_header2 = (TextView) findViewById(R.id.duration_header2);
        duration_header3 = (TextView) findViewById(R.id.duration_header3);
        descTV1 = (TextView) findViewById(R.id.description_header1);
        descTV2 = (TextView) findViewById(R.id.description_header2);
        descTV3 = (TextView) findViewById(R.id.description_header3);
        to_TV1 = (TextView) findViewById(R.id.to_header1);
        to_TV2 = (TextView) findViewById(R.id.to_header2);
        to_TV3 = (TextView) findViewById(R.id.to_header3);
        from_TV1 = (TextView) findViewById(R.id.from_header1);
        from_TV2 = (TextView) findViewById(R.id.from_header2);
        from_TV3 = (TextView) findViewById(R.id.from_header3);
        address_header1 = (TextView) findViewById(R.id.address_header1);
        address_header2 = (TextView) findViewById(R.id.address_header2);
        address_header3 = (TextView) findViewById(R.id.address_header3);
        school_header1 = (TextView) findViewById(R.id.scool_header1);
        school_header2 = (TextView) findViewById(R.id.scool_header2);
        school_header3 = (TextView) findViewById(R.id.scool_header3);
        title_header1 = (TextView) findViewById(R.id.title_header1);
        title_header2 = (TextView) findViewById(R.id.title_header2);
        title_header3 = (TextView) findViewById(R.id.title_header3);
        education_header = (TextView) findViewById(R.id.education_header);
        addeduTV = (TextView) findViewById(R.id.addEduTV);
        remove_edu = (TextView) findViewById(R.id.removeedu);
        remove_work = (TextView) findViewById(R.id.removeWork);

        job_duration_header1 = (TextView) findViewById(R.id.job_duration_header1);
        job_duration_header2 = (TextView) findViewById(R.id.job_duration_header2);
        job_duration_header3 = (TextView) findViewById(R.id.job_duration_header3);
        job_descTV1 = (TextView) findViewById(R.id.job_description_header1);
        job_descTV2 = (TextView) findViewById(R.id.job_description_header2);
        job_descTV3 = (TextView) findViewById(R.id.job_description_header3);
        job_to_TV1 = (TextView) findViewById(R.id.job_to_header1);
        job_to_TV2 = (TextView) findViewById(R.id.job_to_header2);
        job_to_TV3 = (TextView) findViewById(R.id.job_to_header3);
        job_from_TV1 = (TextView) findViewById(R.id.job_from_header1);
        job_from_TV2 = (TextView) findViewById(R.id.job_from_header2);
        job_from_TV3 = (TextView) findViewById(R.id.job_from_header3);
        job_address_header1 = (TextView) findViewById(R.id.job_address_header1);
        job_address_header2 = (TextView) findViewById(R.id.job_address_header2);
        job_address_header3 = (TextView) findViewById(R.id.job_address_header3);
        job_school_header1 = (TextView) findViewById(R.id.job_scool_header1);
        job_school_header2 = (TextView) findViewById(R.id.job_scool_header2);
        job_school_header3 = (TextView) findViewById(R.id.job_scool_header3);
        job_title_header1 = (TextView) findViewById(R.id.job_title_header1);
        job_title_header2 = (TextView) findViewById(R.id.job_title_header2);
        job_title_header3 = (TextView) findViewById(R.id.job_title_header3);
        add_work = (TextView) findViewById(R.id.addWork);


        addskillTV = (TextView) findViewById(R.id.addSkillsTV);
        skill_head = (TextView) findViewById(R.id.skills_header1);
        addextraTV = (TextView) findViewById(R.id.extranoteHeader);
        pageTitleTV = (TextView) findViewById(R.id.pageTitleTV);

        attach_cvTV = (TextView) findViewById(R.id.attachCV);
        attachIDTV = (TextView) findViewById(R.id.attachID);

        fullnameEt = (EditText) findViewById(R.id.fullnameEt);
        phoneEt = (EditText) findViewById(R.id.phoneET);
        emailEt = (EditText) findViewById(R.id.emailET);
        aboutmeEt = (EditText) findViewById(R.id.aboutmeET);

        titleET1 = (EditText) findViewById(R.id.titleET1);
        schoolET1 = (EditText) findViewById(R.id.schoolET1);
        addressET1 = (EditText) findViewById(R.id.addressEt1);
        fromET1 = (EditText) findViewById(R.id.fromET1);
        toET1 = (EditText) findViewById(R.id.toET1);
        descrET1 = (EditText) findViewById(R.id.descET1);


        titleET2 = (EditText) findViewById(R.id.titleET2);
        schoolET2 = (EditText) findViewById(R.id.schoolET2);
        addressET2 = (EditText) findViewById(R.id.addressEt2);
        fromET2 = (EditText) findViewById(R.id.fromET2);
        toET2 = (EditText) findViewById(R.id.toET2);
        descrET2 = (EditText) findViewById(R.id.descET2);


        titleET3 = (EditText) findViewById(R.id.titleET3);
        schoolET3 = (EditText) findViewById(R.id.schoolET3);
        addressET3 = (EditText) findViewById(R.id.addressEt3);
        fromET3 = (EditText) findViewById(R.id.fromET3);
        toET3 = (EditText) findViewById(R.id.toET3);
        descrET3 = (EditText) findViewById(R.id.descET3);


        job_titleET1 = (EditText) findViewById(R.id.job_titleET1);
        job_schoolET1 = (EditText) findViewById(R.id.job_schoolET1);
        job_addressET1 = (EditText) findViewById(R.id.job_addressEt1);
        job_fromET1 = (EditText) findViewById(R.id.job_fromET1);
        job_toET1 = (EditText) findViewById(R.id.job_toET1);
        job_descrET1 = (EditText) findViewById(R.id.job_descET1);


        job_titleET2 = (EditText) findViewById(R.id.job_titleET2);
        job_schoolET2 = (EditText) findViewById(R.id.job_schoolET2);
        job_addressET2 = (EditText) findViewById(R.id.job_addressEt2);
        job_fromET2 = (EditText) findViewById(R.id.job_fromET2);
        job_toET2 = (EditText) findViewById(R.id.job_toET2);
        job_descrET2 = (EditText) findViewById(R.id.job_descET2);

        skillET1 = (EditText) findViewById(R.id.skillsTV1);
        skillET2 = (EditText) findViewById(R.id.skillsTV2);
        skillET4 = (EditText) findViewById(R.id.skillsTV4);
        skillET3 = (EditText) findViewById(R.id.skillsTV3);
        skillET5 = (EditText) findViewById(R.id.skillsTV5);

        apply_job_btn = (Button) findViewById(R.id.apply_job_btn);
        back_btn = (ImageView) findViewById(R.id.back_btn);

        extranoteET = (EditText) findViewById(R.id.extranoteET);
        postImageLay.setVisibility(View.GONE);
        rightTV.setVisibility(View.VISIBLE);


    }

    private void chagnefont(Typeface face) {

        fullnameheader.setTypeface(face);
        phone_header.setTypeface(face);
        email_header.setTypeface(face);
        aboutme_header.setTypeface(face);
        education_header.setTypeface(face);

        duration_header1.setTypeface(face);
        duration_header2.setTypeface(face);
        duration_header3.setTypeface(face);

        descTV1.setTypeface(face);
        descTV2.setTypeface(face);
        descTV3.setTypeface(face);

        to_TV1.setTypeface(face);
        to_TV2.setTypeface(face);
        to_TV3.setTypeface(face);
        from_TV1.setTypeface(face);
        from_TV2.setTypeface(face);
        from_TV3.setTypeface(face);

        address_header1.setTypeface(face);
        address_header2.setTypeface(face);
        address_header3.setTypeface(face);
        school_header1.setTypeface(face);
        school_header2.setTypeface(face);
        school_header3.setTypeface(face);
        title_header1.setTypeface(face);
        title_header2.setTypeface(face);
        title_header3.setTypeface(face);


        job_duration_header1.setTypeface(face);
        job_duration_header2.setTypeface(face);
        job_duration_header3.setTypeface(face);

        job_descTV1.setTypeface(face);
        job_descTV2.setTypeface(face);
        job_descTV3.setTypeface(face);

        job_to_TV1.setTypeface(face);
        job_to_TV2.setTypeface(face);
        job_to_TV3.setTypeface(face);
        job_from_TV1.setTypeface(face);
        job_from_TV2.setTypeface(face);
        job_from_TV3.setTypeface(face);

        job_address_header1.setTypeface(face);
        job_address_header2.setTypeface(face);
        job_address_header3.setTypeface(face);
        job_school_header1.setTypeface(face);
        job_school_header2.setTypeface(face);
        job_school_header3.setTypeface(face);
        job_title_header1.setTypeface(face);
        job_title_header2.setTypeface(face);
        job_title_header3.setTypeface(face);


        addextraTV.setTypeface(face);
        addeduTV.setTypeface(face);
        addskillTV.setTypeface(face);
        attachIDTV.setTypeface(face);
        attach_cvTV.setTypeface(face);
        skill_head.setTypeface(face);

        fullnameEt.setTypeface(face);
        phoneEt.setTypeface(face);
        emailEt.setTypeface(face);
        aboutmeEt.setTypeface(face);
        skillET4.setTypeface(face);
        skillET3.setTypeface(face);
        skillET2.setTypeface(face);
        skillET1.setTypeface(face);
        extranoteET.setTypeface(face);

        descrET1.setTypeface(face);
        fromET1.setTypeface(face);
        toET1.setTypeface(face);
        addressET1.setTypeface(face);
        schoolET1.setTypeface(face);
        titleET1.setTypeface(face);
        job_descrET1.setTypeface(face);
        job_fromET1.setTypeface(face);
        job_toET1.setTypeface(face);
        job_addressET1.setTypeface(face);
        job_schoolET1.setTypeface(face);
        job_titleET1.setTypeface(face);

        descrET2.setTypeface(face);
        fromET2.setTypeface(face);
        toET2.setTypeface(face);
        addressET2.setTypeface(face);
        schoolET2.setTypeface(face);
        titleET2.setTypeface(face);

        job_descrET2.setTypeface(face);
        job_fromET2.setTypeface(face);
        job_toET2.setTypeface(face);
        job_addressET2.setTypeface(face);
        job_schoolET2.setTypeface(face);
        job_titleET2.setTypeface(face);


        descrET3.setTypeface(face);
        fromET3.setTypeface(face);
        toET3.setTypeface(face);
        addressET3.setTypeface(face);
        schoolET3.setTypeface(face);
        titleET3.setTypeface(face);
        pageTitleTV.setTypeface(face);
        add_work.setTypeface(face);
        remove_edu.setTypeface(face);
        remove_work.setTypeface(face);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addEduTV:
                if (educount == 1) {
                    edu2.setVisibility(View.VISIBLE);
                    remove_edu.setVisibility(View.VISIBLE);
                    educount++;
                } else if (educount == 2) {
                    edu3.setVisibility(View.VISIBLE);
                    addeduTV.setVisibility(View.GONE);
                    remove_edu.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.addWork:

                remove_work.setVisibility(View.VISIBLE);
                work_lay2.setVisibility(View.VISIBLE);
                add_work.setVisibility(View.GONE);

                break;

            case R.id.addSkillsTV:
                switch (skillcount) {
                    case 1:
                        skillET2.setVisibility(View.VISIBLE);
                        skillcount++;
                        break;
                    case 2:
                        skillET3.setVisibility(View.VISIBLE);
                        skillcount++;
                        break;
                    case 3:
                        skillET4.setVisibility(View.VISIBLE);
                        skillcount++;
                        break;
                    case 4:
                        skillET5.setVisibility(View.VISIBLE);
                        addskillTV.setVisibility(View.GONE);
                        skillcount++;
                        break;
                }
                break;

            case R.id.attachCV:

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Select File")
                        , 7);
                break;

            case R.id.attachID:
                openCameraCLickOption();
                break;

            case R.id.apply_job_btn:
                validateData();
                break;

            case R.id.back_btn:
                onBackPressed();
                break;

            case R.id.rightTV:
                saveAppliation();
                break;


            case R.id.removeedu:
                removeEducation();
                break;


            case R.id.removeWork:
                remove_work.setVisibility(View.GONE);
                work_lay2.setVisibility(View.GONE);
                add_work.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void removeEducation() {
        if (edu3.getVisibility() == View.VISIBLE) {
            educount = 2;
            edu3.setVisibility(View.GONE);
            addeduTV.setVisibility(View.VISIBLE);
        } else if (edu2.getVisibility() == View.VISIBLE) {
            educount = 1;
            edu2.setVisibility(View.GONE);
            remove_edu.setVisibility(View.GONE);
            addeduTV.setVisibility(View.VISIBLE);
        }

    }


    private void openCameraCLickOption() {
        postImageLay.setVisibility(View.VISIBLE);

        TextView cancel_btn = (TextView) findViewById(R.id.cancel_btnIV);
        TextView cameraIV = (TextView) findViewById(R.id.cameraIV);
        TextView galleryIV = (TextView) findViewById(R.id.gallerIV);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postImageLay.setVisibility(View.GONE);
            }
        });

        cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileName = "Camera_Example.jpg";

                // Create parameters for Intent with filename

                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.TITLE, fileName);

                values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");

                // imageUri is the current activity attribute, define and save it for later usage

                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);

////
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, 1);

//              startCameraActivity();
            }
        });

        galleryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 8);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("resultcode : " + resultCode);
        System.out.println("requestcode : " + requestCode);
        System.out.println("data : " + data);
        switch (requestCode) {

            case 7:
                postImageLay.setVisibility(View.GONE);

                if (resultCode == RESULT_OK) {

                    PathHolder = data.getData().getPath();
                    System.out.println("********* file path before upgrade *********");

                    if (PathHolder.startsWith("/document"))
                        PathHolder = "content://com.android.providers.downloads.documents" + PathHolder;


                    System.out.println("********* file path upgrade*********");
                    System.out.println(PathHolder);


                    cvFile = new java.io.File(PathHolder);
                    attach_cvTV.setText("  " + cvFile.getName());
                    attach_cvTV.setTextColor(getResources().getColor(R.color.green));
//                    Toast.makeText(ApplyJobActivity.this, PathHolder, Toast.LENGTH_LONG).show();

                }
                break;


            case 8:
                postImageLay.setVisibility(View.GONE);

                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                            Uri uri = getImageUri(getApplicationContext(), bitmap);
                            String path = getRealPathFromURI(uri, getApplicationContext());
                            System.out.println("*********** attached image from galley *******");
                            System.out.println(path);

                            idFile = new File(path);

                            String imageName = idFile.getName();
                            System.out.println("*********** "+imageName+"*******");

                            attachIDTV.setText("  " + imageName);
                            attachIDTV.setTextColor(getResources().getColor(R.color.green));
                            attachIDTV.setText(imageName);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(ApplyJobActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                }

                break;

            case 1:
                postImageLay.setVisibility(View.GONE);
                if (data != null) {
                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//                        Uri uri = getImageUri(getApplicationContext(), bitmap);
                        String path = getRealPathFromURI(imageUri, getApplicationContext());
                        System.out.println("*********** attached image from camera *******");
                        System.out.println(path);

                        idFile = new File(path);
                        String imageName = idFile.getName();

                        System.out.println("*********** "+imageName+"*******");

                        attachIDTV.setText("  " + imageName);
                        attachIDTV.setTextColor(getResources().getColor(R.color.green));
                        attachIDTV.setText(imageName);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;

        }
    }


    private void applyJob(String user_id, String post_id) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams category = new RequestParams();

        TimeZone timezone = TimeZone.getDefault();
        System.out.println("* timezone ****** " + timezone.getID());

        final ProgressDialog ringProgressDialog;

        ringProgressDialog = ProgressDialog.show(ApplyJobActivity.this, "Please wait ...", "Applying...", true);
        ringProgressDialog.setCancelable(false);


        JSONObject eduObj1 = new JSONObject();
        JSONObject eduObj2 = new JSONObject();
        JSONObject eduObj3 = new JSONObject();
        JSONObject workObj1 = new JSONObject();
        JSONObject workObj2 = new JSONObject();

        addVauesTOJsOn(eduObj1, title1, school1, address1, from1, to1, descr1);
        addVauesTOJsOn(eduObj2, title2, school2, address2, from2, to2, descr2);
        addVauesTOJsOn(eduObj3, title3, school3, address3, from3, to3, descr3);

        addWorkVauesTOJsOn(workObj1, job_title1, job_school1, job_address1, job_from1, job_to1, job_descr1);
        addWorkVauesTOJsOn(workObj2, job_title2, job_school2, job_address2, job_from2, job_to2, job_descr2);

        JSONArray education_array = new JSONArray();
        education_array.put(eduObj1);
        education_array.put(eduObj2);
        education_array.put(eduObj3);


        JSONArray exp_array = new JSONArray();
        exp_array.put(workObj1);
        exp_array.put(workObj2);

        category.put("user_id", user_id);
        category.put("post_id", post_id);
        category.put("user_name", fullname);
        category.put("user_phone", phone);
        category.put("user_email", email);
        category.put("user_about", aboutme);
        category.put("user_skills", skill1 + "," + skill2 + "," + skill3 + "," + skill4 + "," + skill5);
        category.put("educations_details", education_array);
        category.put("experience_details", exp_array);
        category.put("user_add_something", extranote);

        category.put("timezone", timezone.getID());
        try {
            category.put("job_attached", idFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            category.put("job_attached", "attached id");


        }
        try {

            category.put("job_resume", cvFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            category.put("job_resume", "attached_ resume");


        }
        System.out.println("***** requested params for apply ****");
        System.out.println(category);
        client.post(BASE_URL_NEW + "applied_job", category, new JsonHttpResponseHandler() {


            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Bad Server Connection", Toast.LENGTH_SHORT).show();
                System.out.println(errorResponse);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  JSONObject response) {
                ringProgressDialog.dismiss();

                System.out.println("************* applied successfully **********");
                System.out.println(response);
                Toast.makeText(getApplicationContext(), "Successfully Applied", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Bad Server Connection", Toast.LENGTH_SHORT).show();
                System.out.println(responseString);
            }
        });
    }

    private void addVauesTOJsOn(JSONObject eduObj, String title, String school, String address, String from, String to, String descr) {

        try {
            eduObj.put("education_title", title);
            eduObj.put("education_collage", school);
            eduObj.put("education_address", address);
            eduObj.put("education_from", from);
            eduObj.put("education_to", to);
            eduObj.put("education_desc", descr);

        } catch (JSONException e) {
            System.out.println("******** error in obj **** " + eduObj);
            e.printStackTrace();
        }
    }

    private void addWorkVauesTOJsOn(JSONObject eduObj, String title, String school, String address, String from, String to, String descr) {
        try {
            eduObj.put("experience_title", title);
            eduObj.put("experience_employer", school);
            eduObj.put("experience_address", address);
            eduObj.put("experience_from", from);
            eduObj.put("experience_to", to);
            eduObj.put("experience_desc", descr);

        } catch (JSONException e) {
            System.out.println("******** error in obj **** " + eduObj);
            e.printStackTrace();
        }
    }

    private void validateData() {
        getEntereData();

        String em = emailEt.getText().toString();
        email=em;
        String empatt = "^[a-zA-Z0-9_.]+@[a-zA-Z]+\\.[a-zA-Z]+$";
        boolean b4 = isMatch(em, empatt);

        if (fullname.length() == 0) {
            fullnameEt.setError("Field required");
            fullnameEt.requestFocus();

        } else if (phone.length() == 0) {
            phoneEt.setError("Field required");
            phoneEt.requestFocus();

        } else if (email.length() == 0 || !b4) {
            emailEt.setError("Invalid Email ID");
            emailEt.requestFocus();


        } else if (aboutme.length() == 0) {
            aboutmeEt.setError("Field required");
            aboutmeEt.requestFocus();

        } else if (title1.length() == 0) {
            titleET1.setError("Field required");
            titleET1.requestFocus();

        } else if (school1.length() == 0) {
            schoolET1.setError("Field required");
            schoolET1.requestFocus();

        } else if (address1.length() == 0) {
            addressET1.setError("Field required");
            addressET1.requestFocus();

        } else if (from1.length() == 0) {
            fromET1.setError("Field required");
            fromET1.requestFocus();


        } else if (to1.length() == 0) {
            toET1.setError("Field required");
            toET1.requestFocus();

        } else if (descr1.length() == 0) {
            descrET1.setError("Field required");
            descrET1.requestFocus();

        } else if (job_title1.length() == 0) {
            job_titleET1.setError("Field required");
            job_titleET1.requestFocus();

        } else if (job_school1.length() == 0) {
            job_schoolET1.setError("Field required");
            job_schoolET1.requestFocus();

        } else if (job_address1.length() == 0) {
            job_addressET1.setError("Field required");
            job_addressET1.requestFocus();

        } else if (from1.length() == 0) {
            job_fromET1.setError("Field required");
            job_fromET1.requestFocus();


        } else if (to1.length() == 0) {
            job_toET1.setError("Field required");
            job_toET1.requestFocus();

        } else if (descr1.length() == 0) {
            job_descrET1.setError("Field required");
            job_descrET1.requestFocus();

        }
//        else if (cvFile==null)
//        {
//            Toast.makeText(getApplicationContext(),"attach your resume",Toast.LENGTH_SHORT).show();
//
//        } else if (idFile==null)
//        {
//            Toast.makeText(getApplicationContext(),"attach your id ",Toast.LENGTH_SHORT).show();
//        }
        else {
            String user_id = getData(getApplicationContext(), "user_id", "");

            applyJob(user_id, post_id);
        }

    }

    private void getEntereData() {
        fullname = fullnameEt.getText().toString();
        phone = phoneEt.getText().toString();
        email = emailEt.getText().toString();
        aboutme = aboutmeEt.getText().toString();

        title1 = titleET1.getText().toString();
        title2 = titleET2.getText().toString();
        title3 = titleET3.getText().toString();

        job_title1 = job_titleET1.getText().toString();
        job_title2 = job_titleET2.getText().toString();


        job_school1 = job_schoolET1.getText().toString();
        job_address1 = job_addressET1.getText().toString();
        job_from1 = job_fromET1.getText().toString();
        job_to1 = job_toET1.getText().toString();
        job_descr1 = job_descrET1.getText().toString();


        job_school2 = job_schoolET2.getText().toString();
        job_address2 = job_addressET2.getText().toString();
        job_from2 = job_fromET2.getText().toString();
        job_to2 = job_toET2.getText().toString();
        job_descr2 = job_descrET2.getText().toString();


        school1 = schoolET1.getText().toString();
        address1 = addressET1.getText().toString();
        from1 = fromET1.getText().toString();
        to1 = toET1.getText().toString();
        descr1 = descrET1.getText().toString();

        school2 = schoolET2.getText().toString();
        address2 = addressET2.getText().toString();
        from2 = fromET2.getText().toString();
        to2 = toET2.getText().toString();
        descr2 = descrET2.getText().toString();

        school3 = schoolET3.getText().toString();
        address3 = addressET3.getText().toString();
        from3 = fromET3.getText().toString();
        to3 = toET3.getText().toString();
        descr3 = descrET3.getText().toString();

        skill1 = skillET1.getText().toString();
        skill2 = skillET2.getText().toString();
        skill3 = skillET4.getText().toString();
        skill4 = skillET4.getText().toString();
        skill5 = skillET5.getText().toString();
        extranote = extranoteET.getText().toString();

    }

    private void saveAppliation() {
        Context context = getApplicationContext();
getEntereData();
        email=emailEt.getText().toString();

            JSONObject resumeObj = new JSONObject();

            try {

                resumeObj.put("fullname", fullname);
                resumeObj.put("phone", phone);
                resumeObj.put("email", email);
                resumeObj.put("aboutme", aboutme);
                resumeObj.put("title1", title1);
                resumeObj.put("title2", title2);
                resumeObj.put("title3", title3);
                resumeObj.put("school1", school1);

                resumeObj.put("address1", address1);
                resumeObj.put("from1", from1);
                resumeObj.put("to1", to1);
                resumeObj.put("descr1", descr1);
                resumeObj.put("school2", school2);
                resumeObj.put("address2", address2);
                resumeObj.put("from2", from2);
                resumeObj.put("to2", to2);
                resumeObj.put("descr2", descr2);
                resumeObj.put("school3", school3);
                resumeObj.put("address3", address3);
                resumeObj.put("from3", from3);
                resumeObj.put("to3", to3);
                resumeObj.put("descr3", descr3);
                resumeObj.put("skill1", skill1);
                resumeObj.put("skill2", skill2);
                resumeObj.put("skill3", skill3);
                resumeObj.put("skill4", skill4);
                resumeObj.put("skill5", skill5);
                resumeObj.put("extranote", extranote);
                resumeObj.put("job_title1", job_title1);
                resumeObj.put("job_title2", job_title2);
                resumeObj.put("job_title3", job_title3);
                resumeObj.put("job_school1", job_school1);
                resumeObj.put("job_address1", job_address1);
                resumeObj.put("job_from1", job_from1);
                resumeObj.put("job_to1", job_to1);
                resumeObj.put("job_descr1", job_descr1);
                resumeObj.put("job_school2", job_school2);
                resumeObj.put("job_address2", job_address2);
                resumeObj.put("job_from2", job_from2);
                resumeObj.put("job_to2", job_to2);
                resumeObj.put("job_descr2", job_descr2);

                System.out.println("********** saved application data *****");
                System.out.println(resumeObj);
                saveData(context, "saved_job_application", resumeObj.toString());

                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                rightTV.setTextColor(getResources().getColor(R.color.green_shade));
                rightTV.setText("Save/Edit");
            } catch (JSONException e) {
                System.out.println("********** saved application data error  *****");
                e.printStackTrace();

            }

    }

    private void getSavedData(String savedApp) {
        try {
            JSONObject resumeObj = new JSONObject(savedApp);

            fullname = resumeObj.getString("fullname");
            phone = resumeObj.getString("phone");
            email = resumeObj.getString("email");
            aboutme = resumeObj.getString("aboutme");
            title1 = resumeObj.getString("title1");
            title2 = resumeObj.getString("title2");
            title3 = resumeObj.getString("title3");
            school1 = resumeObj.getString("school1");

            address1 = resumeObj.getString("address1");
            from1 = resumeObj.getString("from1");
            to1 = resumeObj.getString("to1");
            descr1 = resumeObj.getString("descr1");
            school2 = resumeObj.getString("school2");
            address2 = resumeObj.getString("address2");
            from2 = resumeObj.getString("from2");
            to2 = resumeObj.getString("to2");
            descr2 = resumeObj.getString("descr2");
            school3 = resumeObj.getString("school3");
            address3 = resumeObj.getString("address3");
            from3 = resumeObj.getString("from3");
            to3 = resumeObj.getString("to3");
            descr3 = resumeObj.getString("descr3");
            skill1 = resumeObj.getString("skill1");
            skill2 = resumeObj.getString("skill2");
            skill3 = resumeObj.getString("skill3");
            skill4 = resumeObj.getString("skill4");
            skill5 = resumeObj.getString("skill5");
            extranote = resumeObj.getString("extranote");
            job_title1 = resumeObj.getString("job_title1");
            job_title2 = resumeObj.getString("job_title2");

            job_title3 = resumeObj.getString("job_title3");
            job_school1 = resumeObj.getString("job_school1");
            job_address1 = resumeObj.getString("job_address1");
            job_from1 = resumeObj.getString("job_from1");
            job_to1 = resumeObj.getString("job_to1");
            job_descr1 = resumeObj.getString("job_descr1");
            job_school2 = resumeObj.getString("job_school2");
            job_address2 = resumeObj.getString("job_address2");
            job_from2 = resumeObj.getString("job_from2");
            job_to2 = resumeObj.getString("job_to2");
            job_descr2 = resumeObj.getString("job_descr2");

            setSavedData();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSavedData() {
        rightTV.setTextColor(getResources().getColor(R.color.green_shade));
        rightTV.setText("Save/Edit");
        if (title2.length()>0)
            edu2.setVisibility(View.VISIBLE);
        else
            edu2.setVisibility(View.GONE);

        if (title3.length()>0)
            edu3.setVisibility(View.VISIBLE);
        else
            edu3.setVisibility(View.GONE);


        if (job_title2.length()>0)
            work_lay2.setVisibility(View.VISIBLE);
        else
            work_lay2.setVisibility(View.GONE);

        fullnameEt.setText(fullname);
        phoneEt.setText(phone);
        emailEt.setText(email);
        aboutmeEt.setText(aboutme);
        titleET1.setText(title1);
        titleET2.setText(title2);
        titleET3.setText(title3);
        job_titleET1.setText(job_title1);
        job_titleET2.setText(job_title2);
        job_schoolET1.setText(job_school1);
        job_addressET1.setText(job_address1);
        job_fromET1.setText(job_from1);
        job_toET1.setText(job_to1);
        job_descrET1.setText(job_descr1);
        job_schoolET2.setText(job_school2);
        job_addressET2.setText(job_address2);
        job_fromET2.setText(job_from2);
        job_toET2.setText(job_to2);
        job_descrET2.setText(job_descr2);


        schoolET1.setText(school1);
        addressET1.setText(address1);
        fromET1.setText(from1);
        toET1.setText(to1);
        descrET1.setText(descr1);

        schoolET2.setText(school2);
        addressET2.setText(address2);
        fromET2.setText(from2);
        toET2.setText(to2);
        descrET2.setText(descr2);

        schoolET3.setText(school3);
        addressET3.setText(address3);
        fromET3.setText(from3);
        toET3.setText(to3);
        descrET3.setText(descr3);

        skillET1.setText(skill1);
        skillET2.setText(skill2);
        skillET4.setText(skill3);
         skillET4.setText(skill4);
        skillET5.setText(skill5);
        extranoteET.setText(extranote);

    }


}
