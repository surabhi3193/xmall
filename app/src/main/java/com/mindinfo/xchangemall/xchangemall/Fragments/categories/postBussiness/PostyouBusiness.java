package com.mindinfo.xchangemall.xchangemall.Fragments.categories.postBussiness;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour5Add;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.beans.categories;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getListData;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.cross_imageView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.pageNo_textView;


public class PostyouBusiness extends Fragment implements View.OnClickListener {
    //Shard preferences

    ArrayList<String> postarr = new ArrayList<String>();
    TextView business_header, about_header, descripption_header, social_header, website_header, hours_header;
    int count = 1;
    private Button next_btn;
    private ImageButton back_arrowImage;
    private FragmentManager fm;
    private String MainCatType;
    private ArrayList<categories> arrayList = new ArrayList<>();
    private EditText EditTextBusinessName, EditTextAbout, EditTextDescription, EditTextSocialMedia, EditTextWebsite;
    private TextView clickTV;
    private Spinner SpinnerCat;
    private JSONArray hoursArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.postyour4addforbus, null);

        //Get fm
        fm = getActivity().getSupportFragmentManager();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MainCatType = bundle.getString("MainCatType");
            postarr = bundle.getStringArrayList("imageSet");


        }
        findItem(v);
        setOnClick(v);
        loadSpinner();
        return v;
    }

    //finditem
    private void findItem(View v) {
        SpinnerCat = v.findViewById(R.id.SpinnerCat);
        next_btn = (Button) v.findViewById(R.id.next_btn);

        hours_header = v.findViewById(R.id.TextViewHouseOfOpertion);
        clickTV = v.findViewById(R.id.clickTV);
        business_header = v.findViewById(R.id.business_name_header);
        descripption_header = v.findViewById(R.id.description_header_service);
        social_header = v.findViewById(R.id.social_header);
        about_header = v.findViewById(R.id.abount_header);
        website_header = v.findViewById(R.id.website_header);

        back_arrowImage = v.findViewById(R.id.back_arrowImage);
        EditTextBusinessName = v.findViewById(R.id.EditTextBusinessName);
        EditTextAbout = v.findViewById(R.id.EditTextAbout);
        EditTextDescription = v.findViewById(R.id.EditTextDescription);
        EditTextSocialMedia = v.findViewById(R.id.EditTextSocialMedia);
        EditTextWebsite = v.findViewById(R.id.EditTextWebsite);


        Typeface face = ResourcesCompat.getFont(Objects.requireNonNull(getActivity()), R.font.estre);
        pageNo_textView.setText("3of7");

        pageNo_textView.setTypeface(face);
        hours_header.setTypeface(face);
        business_header.setTypeface(face);
        descripption_header.setTypeface(face);
        about_header.setTypeface(face);
        social_header.setTypeface(face);
        website_header.setTypeface(face);
        hours_header.setTypeface(face);
        EditTextWebsite.setTypeface(face);
        EditTextSocialMedia.setTypeface(face);
        EditTextDescription.setTypeface(face);
        EditTextAbout.setTypeface(face);
        EditTextBusinessName.setTypeface(face);

    }

    //set on Click Listener
    private void setOnClick(View v) {
        next_btn.setOnClickListener(this);
        clickTV.setOnClickListener(this);
        back_arrowImage.setOnClickListener(this);
        cross_imageView.setOnClickListener(this);
    }

    //load spinner cat
    private void loadSpinner() {
        int catm = Integer.parseInt(MainCatType);
        getListData("101", arrayList, getActivity());
        ArrayAdapter<categories> dataAdapter = new ArrayAdapter<categories>(getActivity(),
                android.R.layout.simple_spinner_item, arrayList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerCat.setAdapter(dataAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                openNext();
                break;

            case R.id.clickTV:
                openHoursDialog();
                break;

            case R.id.back_arrowImage:
                getActivity().onBackPressed();
                break;
            case R.id.cross_imageView:
                OpenMainActivity();
                break;
        }
    }

    private void openHoursDialog() {

        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.hours_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();


        TextView cancel_btn = dialog.findViewById(R.id.cancel_btn);
        TextView save_btn = dialog.findViewById(R.id.save_btn);

        LinearLayout monday_open1 = dialog.findViewById(R.id.monday_open1);
        LinearLayout monday_close1 = dialog.findViewById(R.id.monday_close1);
        LinearLayout monday_open2 = dialog.findViewById(R.id.monday_open2);
        LinearLayout monday_close2 = dialog.findViewById(R.id.monday_close2);
        LinearLayout monday_layout = dialog.findViewById(R.id.monday_layout);
        ImageView monday_close = dialog.findViewById(R.id.monday_close);
        TextView add1 = dialog.findViewById(R.id.add1);
        TextView tv1 = dialog.findViewById(R.id.tv1);

        TextView monday_openTV1 = dialog.findViewById(R.id.monday_openTV1);
        TextView monday_closeTv1 = dialog.findViewById(R.id.monday_closeTv1);
        TextView monday_openTv2 = dialog.findViewById(R.id.monday_openTv2);
        TextView monday_closeTv2 = dialog.findViewById(R.id.monday_closeTv2);

        TableRow monday_row2 = dialog.findViewById(R.id.monday_row2);
        SwitchCompat switchMonday = dialog.findViewById(R.id.switchMonday);

        switchMonday.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                monday_layout.setVisibility(View.VISIBLE);
                tv1.setVisibility(View.GONE);

            } else {
                monday_layout.setVisibility(View.GONE);
                tv1.setVisibility(View.VISIBLE);
                 tv1.setText(R.string.closed);
            }
        });
        monday_close.setOnClickListener(v -> {
            monday_row2.setVisibility(View.GONE);
            add1.setVisibility(View.VISIBLE);
        });
        add1.setOnClickListener(v -> {
            monday_row2.setVisibility(View.VISIBLE);
            add1.setVisibility(View.GONE);
        });

        monday_open1.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    String time = getTime(selectedHour,selectedMinute);
                    monday_openTV1.setText(time);
                }
            }, 9, 00, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        });

        monday_close1.setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        monday_closeTv1.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        monday_open2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        monday_openTv2.setText(time);
                    }
                }, 9, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        monday_close2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        monday_closeTv2.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        LinearLayout tuesday_open1 = dialog.findViewById(R.id.tuesday_open1);
        LinearLayout tuesday_close1 = dialog.findViewById(R.id.tuesday_close1);
        LinearLayout tuesday_open2 = dialog.findViewById(R.id.tuesday_open2);
        LinearLayout tuesday_close2 = dialog.findViewById(R.id.tuesday_close2);
        LinearLayout tuesday_layout = dialog.findViewById(R.id.tuesday_layout);
        ImageView tuesday_close = dialog.findViewById(R.id.tuesday_close);
        TextView add2 = dialog.findViewById(R.id.add2);
        TextView tv2 = dialog.findViewById(R.id.tv2);

        TextView tuesday_openTV1 = dialog.findViewById(R.id.tuesday_openTV1);
        TextView tuesday_closeTv1 = dialog.findViewById(R.id.tuesday_closeTv1);
        TextView tuesday_openTv2 = dialog.findViewById(R.id.tuesday_openTv2);
        TextView tuesday_closeTv2 = dialog.findViewById(R.id.tuesday_closeTv2);

        TableRow tuesday_row2 = dialog.findViewById(R.id.tuesday_row2);
        SwitchCompat switchTuesday = dialog.findViewById(R.id.switchTuesday);

        switchTuesday.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                tuesday_layout.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.GONE);

            } else {
                tuesday_layout.setVisibility(View.GONE);
                tv2.setVisibility(View.VISIBLE);
                tv2.setText(R.string.closed);
            }
        });
        tuesday_close.setOnClickListener(v -> {
            tuesday_row2.setVisibility(View.GONE);
            add2.setVisibility(View.VISIBLE);
        });
        add2.setOnClickListener(v -> {
            tuesday_row2.setVisibility(View.VISIBLE);
            add2.setVisibility(View.GONE);
        });

        tuesday_open1.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    String time = getTime(selectedHour,selectedMinute);
                    tuesday_openTV1.setText(time);
                }
            }, 9, 00, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        });

        tuesday_close1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        tuesday_closeTv1.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        tuesday_open2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        tuesday_openTv2.setText(time);
                    }
                }, 9, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        tuesday_close2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        tuesday_closeTv2.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });



        LinearLayout wed_open1 = dialog.findViewById(R.id.wed_open1);
        LinearLayout wed_close1 = dialog.findViewById(R.id.wed_close1);
        LinearLayout wed_open2 = dialog.findViewById(R.id.wed_open2);
        LinearLayout wed_close2 = dialog.findViewById(R.id.wed_close2);
        LinearLayout wed_layout = dialog.findViewById(R.id.wed_layout);
        ImageView wed_close = dialog.findViewById(R.id.wed_close);
        TextView add3 = dialog.findViewById(R.id.add3);
        TextView tv3 = dialog.findViewById(R.id.tv3);

        TextView wed_openTV1 = dialog.findViewById(R.id.wed_openTV1);
        TextView wed_closeTv1 = dialog.findViewById(R.id.wed_closeTv1);
        TextView wed_openTv2 = dialog.findViewById(R.id.wed_openTv2);
        TextView wed_closeTv2 = dialog.findViewById(R.id.wed_closeTv2);

        TableRow wed_row2 = dialog.findViewById(R.id.wed_row2);
        SwitchCompat switchWed = dialog.findViewById(R.id.switchWed);

        switchWed.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                wed_layout.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.GONE);

            } else {
                wed_layout.setVisibility(View.GONE);
                tv3.setVisibility(View.VISIBLE);
                tv3.setText(R.string.closed);
            }
        });
        wed_close.setOnClickListener(v -> {
            wed_row2.setVisibility(View.GONE);
            add3.setVisibility(View.VISIBLE);
        });
        add3.setOnClickListener(v -> {
            wed_row2.setVisibility(View.VISIBLE);
            add3.setVisibility(View.GONE);
        });

        wed_open1.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String time = getTime(selectedHour,selectedMinute);
                    wed_openTV1.setText(time);
                }
            }, 9, 00, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        });

        wed_close1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        wed_closeTv1.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        wed_open2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        wed_openTv2.setText(time);
                    }
                }, 9, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        wed_close2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        wed_closeTv2.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });




        LinearLayout thur_open1 = dialog.findViewById(R.id.thur_open1);
        LinearLayout thur_close1 = dialog.findViewById(R.id.thur_close1);
        LinearLayout thur_open2 = dialog.findViewById(R.id.thur_open2);
        LinearLayout thur_close2 = dialog.findViewById(R.id.thur_close2);
        LinearLayout thur_layout = dialog.findViewById(R.id.thur_layout);
        ImageView thur_close = dialog.findViewById(R.id.thur_close);
        TextView add4 = dialog.findViewById(R.id.add4);
        TextView tv4 = dialog.findViewById(R.id.tv4);

        TextView thur_openTV1 = dialog.findViewById(R.id.thur_openTV1);
        TextView thur_closeTv1 = dialog.findViewById(R.id.thur_closeTv1);
        TextView thur_openTv2 = dialog.findViewById(R.id.thur_openTv2);
        TextView thur_closeTv2 = dialog.findViewById(R.id.thur_closeTv2);

        TableRow thur_row2 = dialog.findViewById(R.id.thur_row2);
        SwitchCompat switchThur = dialog.findViewById(R.id.switchThur);

        switchThur.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                thur_layout.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.GONE);

            } else {
                thur_layout.setVisibility(View.GONE);
                tv4.setVisibility(View.VISIBLE);
                tv4.setText(R.string.closed);
            }
        });
        thur_close.setOnClickListener(v -> {
            thur_row2.setVisibility(View.GONE);
            add4.setVisibility(View.VISIBLE);
        });
        add4.setOnClickListener(v -> {
            thur_row2.setVisibility(View.VISIBLE);
            add4.setVisibility(View.GONE);
        });

        thur_open1.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String time = getTime(selectedHour,selectedMinute);
                    thur_openTV1.setText(time);
                }
            }, 9, 00, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        });

        thur_close1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        thur_closeTv1.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        thur_open2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        thur_openTv2.setText(time);
                    }
                }, 9, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        thur_close2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);

                        thur_closeTv2.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        LinearLayout friday_open1 = dialog.findViewById(R.id.friday_open1);
        LinearLayout friday_close1 = dialog.findViewById(R.id.friday_close1);
        LinearLayout friday_open2 = dialog.findViewById(R.id.friday_open2);
        LinearLayout friday_close2 = dialog.findViewById(R.id.friday_close2);
        LinearLayout friday_layout = dialog.findViewById(R.id.friday_layout);
        ImageView friday_close = dialog.findViewById(R.id.friday_close);
        TextView add5 = dialog.findViewById(R.id.add5);
        TextView tv5 = dialog.findViewById(R.id.tv5);

        TextView friday_openTV1 = dialog.findViewById(R.id.friday_openTV1);
        TextView friday_closeTv1 = dialog.findViewById(R.id.friday_closeTv1);
        TextView friday_openTv2 = dialog.findViewById(R.id.friday_openTv2);
        TextView friday_closeTv2 = dialog.findViewById(R.id.friday_closeTv2);

        TableRow friday_row2 = dialog.findViewById(R.id.friday_row2);
        SwitchCompat switchFriday = dialog.findViewById(R.id.switchFriday);

        switchFriday.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                friday_layout.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.GONE);

            } else {
                friday_layout.setVisibility(View.GONE);
                tv5.setVisibility(View.VISIBLE);
                tv5.setText(R.string.closed);
            }
        });
        friday_close.setOnClickListener(v -> {
            friday_row2.setVisibility(View.GONE);
            add5.setVisibility(View.VISIBLE);
        });
        add5.setOnClickListener(v -> {
            friday_row2.setVisibility(View.VISIBLE);
            add5.setVisibility(View.GONE);
        });

        friday_open1.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String time = getTime(selectedHour,selectedMinute);
                    friday_openTV1.setText(time);
                }
            }, 9, 00, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        });

        friday_close1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        friday_closeTv1.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        friday_open2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        friday_openTv2.setText(time);
                    }
                }, 9, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        friday_close2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        friday_closeTv2.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        LinearLayout sat_open1 = dialog.findViewById(R.id.sat_open1);
        LinearLayout sat_close1 = dialog.findViewById(R.id.sat_close1);
        LinearLayout sat_open2 = dialog.findViewById(R.id.sat_open2);
        LinearLayout sat_close2 = dialog.findViewById(R.id.sat_close2);
        LinearLayout sat_layout = dialog.findViewById(R.id.sat_layout);
        ImageView sat_close = dialog.findViewById(R.id.sat_close);
        TextView add6 = dialog.findViewById(R.id.add6);
        TextView tv6 = dialog.findViewById(R.id.tv6);

        TextView sat_openTV1 = dialog.findViewById(R.id.sat_openTV1);
        TextView sat_closeTv1 = dialog.findViewById(R.id.sat_closeTv1);
        TextView sat_openTv2 = dialog.findViewById(R.id.sat_openTv2);
        TextView sat_closeTv2 = dialog.findViewById(R.id.sat_closeTv2);

        TableRow sat_row2 = dialog.findViewById(R.id.sat_row2);
        SwitchCompat switchSat = dialog.findViewById(R.id.switchSat);

        switchSat.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                sat_layout.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.GONE);

            } else {
                sat_layout.setVisibility(View.GONE);
                tv6.setVisibility(View.VISIBLE);
                tv6.setText(R.string.closed);
            }
        });
        sat_close.setOnClickListener(v -> {
            sat_row2.setVisibility(View.GONE);
            add6.setVisibility(View.VISIBLE);
        });
        add6.setOnClickListener(v -> {
            sat_row2.setVisibility(View.VISIBLE);
            add6.setVisibility(View.GONE);
        });

        sat_open1.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String time = getTime(selectedHour,selectedMinute);
                    sat_openTV1.setText(time);
                }
            }, 9, 00, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        });

        sat_close1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        sat_closeTv1.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        sat_open2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        sat_openTv2.setText(time);
                    }
                }, 9, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        sat_close2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        sat_closeTv2.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        LinearLayout sunday_open1 = dialog.findViewById(R.id.sunday_open1);
        LinearLayout sunday_close1 = dialog.findViewById(R.id.sunday_close1);
        LinearLayout sunday_open2 = dialog.findViewById(R.id.sunday_open2);
        LinearLayout sunday_close2 = dialog.findViewById(R.id.sunday_close2);
        LinearLayout sunday_layout = dialog.findViewById(R.id.sunday_layout);
        ImageView sunday_close = dialog.findViewById(R.id.sunday_close);
        TextView add7 = dialog.findViewById(R.id.add7);
        TextView tv7 = dialog.findViewById(R.id.tv7);

        TextView sunday_openTV1 = dialog.findViewById(R.id.sunday_openTV1);
        TextView sunday_closeTv1 = dialog.findViewById(R.id.sunday_closeTv1);
        TextView sunday_openTv2 = dialog.findViewById(R.id.sunday_openTv2);
        TextView sunday_closeTv2 = dialog.findViewById(R.id.sunday_closeTv2);

        TableRow sunday_row2 = dialog.findViewById(R.id.sunday_row2);
        SwitchCompat switchSunday = dialog.findViewById(R.id.switchSunday);

        switchSunday.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                sunday_layout.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.GONE);

            } else {
                sunday_layout.setVisibility(View.GONE);
                tv7.setVisibility(View.VISIBLE);
                tv7.setText(R.string.closed);
            }
        });
        sunday_close.setOnClickListener(v -> {
            sunday_row2.setVisibility(View.GONE);
            add7.setVisibility(View.VISIBLE);
        });
        add7.setOnClickListener(v -> {
            sunday_row2.setVisibility(View.VISIBLE);
            add7.setVisibility(View.GONE);
        });

        sunday_open1.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String time = getTime(selectedHour,selectedMinute);
                    sunday_openTV1.setText(time);
                }
            }, 9, 00, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        });

        sunday_close1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        sunday_closeTv1.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        sunday_open2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        sunday_openTv2.setText(time);
                    }
                }, 9, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        sunday_close2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = getTime(selectedHour,selectedMinute);
                        sunday_closeTv2.setText(time);
                    }
                }, 17, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 hoursArray = new JSONArray();
                addspinners("Monday",monday_openTV1.getText().toString(),
                        monday_closeTv1.getText().toString(),hoursArray,switchMonday,monday_row2,monday_openTv2.getText().toString(),
                        monday_closeTv2.getText().toString());
                addspinners("Tuesday",tuesday_openTV1.getText().toString(),
                        tuesday_closeTv1.getText().toString(),hoursArray,switchTuesday,tuesday_row2,
                        tuesday_openTv2.getText().toString(),tuesday_closeTv2.getText().toString());

                addspinners("Wednesday",wed_openTV1.getText().toString(),
                        wed_closeTv1.getText().toString(),hoursArray,switchWed,wed_row2,wed_openTv2.getText().toString(),
                        wed_closeTv2.getText().toString());

                addspinners("Thursday",thur_openTV1.getText().toString(),
                        thur_closeTv1.getText().toString(),hoursArray,switchThur,thur_row2,thur_openTv2.getText().toString(),thur_closeTv2.getText().toString());

                addspinners("Friday",friday_openTV1.getText().toString()
                        ,friday_closeTv1.getText().toString(),hoursArray,switchFriday,friday_row2,
                        friday_openTv2.getText().toString(),friday_closeTv2.getText().toString());


                addspinners("Saturday",sat_openTV1.getText().toString(),
                        sat_closeTv1.getText().toString(),hoursArray,switchSat,sat_row2,sat_openTv2.getText().toString(),sat_closeTv2.getText().toString()
                );


                addspinners("Sunday",sunday_openTV1.getText().toString(),
                        sunday_closeTv1.getText().toString(),hoursArray,switchSunday,sunday_row2,
                        sunday_openTv2.getText().toString(),sat_closeTv2.getText().toString());


                dialog.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 hoursArray = new JSONArray();
                addspinners("Monday",monday_openTV1.getText().toString(),
                        monday_closeTv1.getText().toString(),hoursArray,switchMonday,monday_row2,monday_openTv2.getText().toString(),
                        monday_closeTv2.getText().toString());
                addspinners("Tuesday",tuesday_openTV1.getText().toString(),
                        tuesday_closeTv1.getText().toString(),hoursArray,switchTuesday,tuesday_row2,
                        tuesday_openTv2.getText().toString(),tuesday_closeTv2.getText().toString());

                addspinners("Wednesday",wed_openTV1.getText().toString(),
                        wed_closeTv1.getText().toString(),hoursArray,switchWed,wed_row2,wed_openTv2.getText().toString(),
                        wed_closeTv2.getText().toString());

                addspinners("Thursday",thur_openTV1.getText().toString(),
                        thur_closeTv1.getText().toString(),hoursArray,switchThur,thur_row2,thur_openTv2.getText().toString(),thur_closeTv2.getText().toString());

                addspinners("Friday",friday_openTV1.getText().toString()
                        ,friday_closeTv1.getText().toString(),hoursArray,switchFriday,friday_row2,
                        friday_openTv2.getText().toString(),friday_closeTv2.getText().toString());


                addspinners("Saturday",sat_openTV1.getText().toString(),
                        sat_closeTv1.getText().toString(),hoursArray,switchSat,sat_row2,sat_openTv2.getText().toString(),sat_closeTv2.getText().toString()
                );


                addspinners("Sunday",sunday_openTV1.getText().toString(),
                        sunday_closeTv1.getText().toString(),hoursArray,switchSunday,sunday_row2,
                        sunday_openTv2.getText().toString(),sat_closeTv2.getText().toString());

                dialog.dismiss();
            }
        });
        
        if ( hoursArray!=null && hoursArray.length()>0)
        {
            String open_time;String close_time,open_time2,close_time2;

            System.out.println("------- hoursArray ---------");
            System.out.println(hoursArray);
            try {
                JSONObject obj1 = hoursArray.getJSONObject(0);
                 open_time=obj1.getString("open_time");
                 close_time=obj1.getString("close_time");
                 open_time2=obj1.getString("open_time2");
                 close_time2=obj1.getString("close_time2");

                if (!open_time.equalsIgnoreCase("close")) {
                    monday_openTV1.setText(open_time);
                    monday_closeTv1.setText(close_time);

                    System.out.println("----- opentime 2 for " + open_time2.length());
                    if (!open_time2.equalsIgnoreCase("close")) {
                        monday_row2.setVisibility(View.VISIBLE);
                        monday_openTv2.setText(open_time2);
                        monday_closeTv2.setText(close_time2);
                    }
                    else
                    {
                        monday_row2.setVisibility(View.GONE);
                    }
                }
                else
                    switchMonday.setChecked(false);



                JSONObject obj2 = hoursArray.getJSONObject(1);
                 open_time=obj2.getString("open_time");
                 close_time=obj2.getString("close_time");
                open_time2=obj2.getString("open_time2");
                close_time2=obj2.getString("close_time2");

                if (!open_time.equalsIgnoreCase("close")) {
                    tuesday_openTV1.setText(open_time);
                    tuesday_closeTv1.setText(close_time);
                    System.out.println("----- opentime 2 for " + open_time2.length());
                    if (!open_time2.equalsIgnoreCase("close")) {
                        tuesday_row2.setVisibility(View.VISIBLE);
                        tuesday_openTv2.setText(open_time2);
                        tuesday_closeTv2.setText(close_time2);
                    }
                    else
                    {
                        tuesday_row2.setVisibility(View.GONE);
                    }
                }
                else
                    switchTuesday.setChecked(false);


                JSONObject obj3 = hoursArray.getJSONObject(2);
                open_time=obj3.getString("open_time");
                close_time=obj3.getString("close_time");
                open_time2=obj3.getString("open_time2");
                close_time2=obj3.getString("close_time2");

                if (!open_time.equalsIgnoreCase("close")) {
                    wed_openTV1.setText(open_time);
                    wed_closeTv1.setText(close_time);

                    System.out.println("----- opentime 2 for " + open_time2.length());

                    if (!open_time2.equalsIgnoreCase("close")) {
                        wed_row2.setVisibility(View.VISIBLE);
                        wed_openTv2.setText(open_time2);
                        wed_closeTv2.setText(close_time2);
                    }
                    else
                    {
                        wed_row2.setVisibility(View.GONE);
                    }
                }
                else
                    switchWed.setChecked(false);


                JSONObject obj4 = hoursArray.getJSONObject(3);
                open_time=obj4.getString("open_time");
                close_time=obj4.getString("close_time");
                open_time2=obj4.getString("open_time2");
                close_time2=obj4.getString("close_time2");

                if (!open_time.equalsIgnoreCase("close")) {
                    thur_openTV1.setText(open_time);
                    thur_closeTv1.setText(close_time);

                    System.out.println("----- opentime 2 for " + open_time2.length());


                    if (!open_time2.equalsIgnoreCase("close")) {
                        thur_row2.setVisibility(View.VISIBLE);
                        thur_openTv2.setText(open_time2);
                        thur_closeTv2.setText(close_time2);
                    }
                    else
                    {
                        thur_row2.setVisibility(View.GONE);
                    }
                }
                else
                    switchThur.setChecked(false);


                JSONObject obj5 = hoursArray.getJSONObject(4);
                open_time=obj5.getString("open_time");
                close_time=obj5.getString("close_time");
                open_time2=obj5.getString("open_time2");
                close_time2=obj5.getString("close_time2");

                if (!open_time.equalsIgnoreCase("close")) {
                    friday_openTV1.setText(open_time);
                    friday_closeTv1.setText(close_time);

                    System.out.println("----- opentime 2 for " + open_time2.length());


                    if (!open_time2.equalsIgnoreCase("close")) {
                        friday_row2.setVisibility(View.VISIBLE);
                        friday_openTv2.setText(open_time2);
                        friday_closeTv2.setText(close_time2);
                    }
                    else
                    {
                        friday_row2.setVisibility(View.GONE);
                    }
                }
                else
                    switchFriday.setChecked(false);



                JSONObject obj6 = hoursArray.getJSONObject(5);
                open_time=obj6.getString("open_time");
                close_time=obj6.getString("close_time");
                open_time2=obj6.getString("open_time2");
                close_time2=obj6.getString("close_time2");

                if (!open_time.equalsIgnoreCase("close")) {
                    sat_openTV1.setText(open_time);
                    sat_closeTv1.setText(close_time);

                    System.out.println("----- opentime 2 for " + open_time2.length());

                    if (!open_time2.equalsIgnoreCase("close")) {
                        sat_row2.setVisibility(View.VISIBLE);
                        sat_openTv2.setText(open_time2);
                        sat_closeTv2.setText(close_time2);
                    }
                    else
                    {
                        sat_row2.setVisibility(View.GONE);
                    }
                }
                else
                    switchSat.setChecked(false);

                JSONObject obj7 = hoursArray.getJSONObject(6);
                open_time=obj7.getString("open_time");
                close_time=obj7.getString("close_time");
                open_time2=obj7.getString("open_time2");
                close_time2=obj7.getString("close_time2");

                if (!open_time.equalsIgnoreCase("close")) {
                    sunday_openTV1.setText(open_time);
                    sunday_closeTv1.setText(close_time);

                    System.out.println("----- opentime 2 for " + open_time2.length());

                    if (!open_time2.equalsIgnoreCase("close")) {
                        sunday_row2.setVisibility(View.VISIBLE);
                        sunday_openTv2.setText(open_time2);
                        sunday_closeTv2.setText(close_time2);
                    }
                    else
                    {
                        sunday_row2.setVisibility(View.GONE);
                    }
                }
                else
                    switchSunday.setChecked(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


//    private void openVideoRecorder() {
//         mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
//                        + "/myvideo.mp4");
//
//        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//
//         videoUri = Uri.fromFile(mediaFile);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
//        startActivityForResult(intent, VIDEO_CAPTURE);
//    }

    private void openNext() {
        //EditTextBusinessName,EditTextAbout,EditTextDescription,EditTextSocialMedia,EditTextWebsite;
        if (EditTextBusinessName.getText().length() == 0) {
            ShowToast_msg("Enter Business Name");
            return;
        }
        if (EditTextAbout.getText().length() == 0) {
            ShowToast_msg("Enter About");
            return;
        }
        if (EditTextDescription.getText().length() == 0) {
            ShowToast_msg("Enter Description");
            return;
        }
//        if (videoUri==null)
//        {
//            ShowToast_msg("Upload VIdeo");
//            return;
//        }
//        if (videoUri!=null)
//        {
//            if (videoUri.toString().length()==0)
//            {
//                ShowToast_msg("Upload VIdeo");
//                return;
//            }
//        }
        ArrayList<String> categoryids = new ArrayList<String>();
        categories cat = arrayList.get(SpinnerCat.getSelectedItemPosition());
        categoryids.add(cat.getId());
        cat.getTitle();
        cat.getId();

        System.err.println("========= selected cat for buss =======" + cat.getId() + cat.getTitle());


        Bundle bundle = new Bundle();
        JSONObject bussinessData = new JSONObject();
        try {
            bussinessData.put("business_name", EditTextBusinessName.getText().toString());
            bussinessData.put("postDes", EditTextDescription.getText().toString());
            bussinessData.put("about_business", EditTextAbout.getText().toString());
            bussinessData.put("social_media_link", EditTextSocialMedia.getText().toString());
            bussinessData.put("website_link", EditTextWebsite.getText().toString());
            bussinessData.put("hours_of_operation", hoursArray.toString());
//            bussinessData.put("video_file",videoUri.getPath());
            bussinessData.put("category", cat.getId());

            System.err.println("==== operation array =======");
            System.err.println(hoursArray.toString());
            bundle.putString("bussinessobj", bussinessData.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        //bundle.putStringArray("imagess",str_image_arr);
        bundle.putStringArrayList("imageSet", postarr);
        bundle.putString("MainCatType", "101");
        bundle.putString("sub_cat_id", cat.getId());
        bundle.putStringArrayList("selectedcategories", categoryids);


        Postyour5Add postyour5Add = new Postyour5Add();
        postyour5Add.setArguments(bundle);
        fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.allCategeriesIN, postyour5Add).addToBackStack(null).commit();
    }

    private void addspinners(String WeekName1, String spinnerAM1, String spinnerPM1, JSONArray operationArray,
                             SwitchCompat switch_btn,TableRow row,String spinnerAM2,String spinnerPM2) {
        try {
            JSONObject obj1 = new JSONObject();
            if (switch_btn.isChecked())
            {
                if (row.getVisibility()==View.VISIBLE)
                {
                    obj1.put("open_time2", spinnerAM2);
                    obj1.put("close_time2", spinnerPM2);
                }
                else
                {
                    obj1.put("open_time2", "closed");
                    obj1.put("close_time2", "closed");
                }

                obj1.put("days", WeekName1);
                obj1.put("open_time", spinnerAM1);
                obj1.put("close_time", spinnerPM1);
                obj1.put("office_status", "open");


            }
            else
            {
                obj1.put("days", WeekName1);
                obj1.put("open_time", "closed");
                obj1.put("close_time", "closed");
                obj1.put("open_time2", "closed");
                obj1.put("close_time2", "closed");
                obj1.put("office_status", "closed");
            }
            operationArray.put(obj1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OpenMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getActivity().startActivity(i);
    }


    private void ShowToast_msg(String str_msg) {
        Toast.makeText(getActivity(), str_msg, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == VIDEO_CAPTURE) {
//            if (resultCode == RESULT_OK) {
//                Toast.makeText(getActivity(), "Video saved to:\n" +
//                        data.getData(), Toast.LENGTH_LONG).show();
//                videoname.setVisibility(View.VISIBLE);
//                videoname.setText(videoUri.toString());
//            }
//            else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(getActivity(), "Video recording cancelled.",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(getActivity(), "Failed to record video",
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    public String getTime(int hourOfDay,int minute)
    {
       int hour = hourOfDay;
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12){
            timeSet = "PM";
        }else{
            timeSet = "AM";
        }

        String min = "";
        if (minute < 10)
            min = "0" + minute;
        else
            min = String.valueOf(minute);

        // Append in a StringBuilder

        return new StringBuilder().append(hour).append(':')
                .append(min ).append(" ").append(timeSet).toString();
    }
}


