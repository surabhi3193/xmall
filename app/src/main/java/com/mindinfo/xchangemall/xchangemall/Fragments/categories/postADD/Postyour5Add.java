package com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.cross_imageView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.pageNo_textView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour4Add.face;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class Postyour5Add extends Fragment implements View.OnClickListener{
    public static ArrayList<String> selectedData;
    final boolean[] checkedItems = {false, false, false,false};
    String obj;
    ArrayList<String> imageSet = new ArrayList<String>();
    ArrayList<String> categoryids = new ArrayList<String>();
    String pcat_name;
    Context context;
     String[]    animals = {"Call", "Video", "Chat","Text"};
    ListAdapter adapter;
    LinearLayout contact_lay;
    boolean iscallSelected= false,isVdoselected =false,isChatSelected=false,istextSelected=false;
    //next_btn
    private Button next_btn,ok_btn,cancel_btn;

    private TextView  headercategory, contactByTv;
    private CheckedTextView callTV,vdoTV,chatTV,textTV;
    private ImageButton back_arrowImage;
    //Fragment Manager
    private FragmentManager fm;
    private String sub_cat_id = "", MainCatType, postTitle, postDes, postPrice, postSize, postCondition, privacy_str, phone_str, Existence_str, contName_str, language_str;
    private AppCompatSpinner spinnerPrivacy, spinnerLanguage;
    private EditText phoneNumberEditText, ExtensionEditText, contactNameEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.postyour5add, null);

        fm = getActivity().getSupportFragmentManager();

        context = getActivity().getApplicationContext();


        findItem(v);
        onClickonItem(v);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sub_cat_id = bundle.getString("sub_cat_id");
            MainCatType = bundle.getString("MainCatType");
            imageSet = bundle.getStringArrayList("imageSet");
            if (MainCatType.equals("101")) {
                obj = bundle.getString("bussinessobj");
            }

            if (MainCatType.equals("102") || MainCatType.equals("272")) {
                obj = bundle.getString("prop_obj");
                categoryids = bundle.getStringArrayList("selectedcategories");

            } else {
                postTitle = bundle.getString("postTitle");
                postDes = bundle.getString("postDes");
                postPrice = bundle.getString("postPrice");
                postSize = bundle.getString("postSize");
                postCondition = bundle.getString("postCondition");
                pcat_name = bundle.getString("pcat_name");
                categoryids = bundle.getStringArrayList("selectedcategories");
            }


            MainCatType = getData(getActivity().getApplicationContext(), "pcat_id", "");
        }
        return v;
    }

    //findItem
    private void findItem(View v) {
        spinnerPrivacy = (AppCompatSpinner) v.findViewById(R.id.spinnerPrivacy);
        spinnerLanguage = (AppCompatSpinner) v.findViewById(R.id.spinnerLanguage);
        phoneNumberEditText = (EditText) v.findViewById(R.id.phoneNumberEditText);
        ExtensionEditText = (EditText) v.findViewById(R.id.ExtensionEditText);
        contactNameEditText = (EditText) v.findViewById(R.id.contactNameEditText);

        headercategory = (TextView) v.findViewById(R.id.headercategory);

        contactByTv = (TextView) v.findViewById(R.id.contactByTv);
        back_arrowImage = (ImageButton) v.findViewById(R.id.back_arrowImage);
        next_btn = (Button) v.findViewById(R.id.next_btn);
        ok_btn = (Button) v.findViewById(R.id.ok_btn);
        cancel_btn = (Button) v.findViewById(R.id.cancel_btn);
         contact_lay =(LinearLayout)v.findViewById(R.id.contact_lay);


         callTV =(CheckedTextView)v.findViewById(R.id.callTV);
         chatTV =(CheckedTextView)v.findViewById(R.id.chatTV);
         vdoTV =(CheckedTextView)v.findViewById(R.id.vdoTV);
        textTV =(CheckedTextView)v.findViewById(R.id.textTV);

        pageNo_textView.setText("5 of 7");

        contactByTv.setTypeface(face);
        pageNo_textView.setTypeface(face);
        phoneNumberEditText.setTypeface(face);
        ExtensionEditText.setTypeface(face);
        contactNameEditText.setTypeface(face);
        headercategory.setTypeface(face);


        callTV.setTypeface(face);
        chatTV.setTypeface(face);
        textTV.setTypeface(face);
        vdoTV.setTypeface(face);
        ok_btn.setTypeface(face);
        cancel_btn.setTypeface(face);



        headercategory.setText(pcat_name);


        if (getData(getActivity().getApplicationContext(), "first_entry_contact", "") != null)
        {
            String getData = getData(getActivity().getApplicationContext(), "first_entry_contact", "");

            System.out.println("********* new data length ******* " + getData.length());
            if (getData.length()>0)
            {
                try {
                    JSONObject contact_obj = new JSONObject(getData);

                    phoneNumberEditText.setText(contact_obj.getString("phone_data"));
                    ExtensionEditText.setText(contact_obj.getString("extension_data"));
                    contactNameEditText.setText(contact_obj.getString("contact_name"));
                    spinnerLanguage.setSelection(Integer.parseInt(contact_obj.getString("lang")));
                    contactByTv.setText(contact_obj.getString("contact_by"));
                } catch (JSONException e) {
e.printStackTrace();
                }
            }
            else
            {
                selectedData=null;
            }

        }



    }

    private void onClickonItem(View view) {
        back_arrowImage.setOnClickListener(this);
        cross_imageView.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        contactByTv.setOnClickListener(this);
    }

    private void saveValue() {

        JSONObject contact_obj = new JSONObject();

        try {

            String contact_by =contactByTv.getText().toString();
            String phone = phoneNumberEditText.getText().toString();
            String extension = ExtensionEditText.getText().toString();
            String contact_name=contactNameEditText.getText().toString();
            String lang= String.valueOf(spinnerLanguage.getSelectedItemPosition());


            contact_obj.put("contact_by",contact_by);
            contact_obj.put("phone_data",phone);
            contact_obj.put("extension_data",extension);
            contact_obj.put("contact_name",contact_name);
            contact_obj.put("lang",lang);
            saveData(getActivity().getApplicationContext(), "first_entry_contact", contact_obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                saveValue();
                nextClick();
                break;


            case R.id.contactByTv:

                openDialogBox(v);
                break;

            case R.id.back_arrowImage:
                saveValue();
                getActivity().onBackPressed();
                break;
            case R.id.cross_imageView:
                OpenMainActivity();
                break;
        }
    }

    private void openDialogBox(View v) {

        System.out.println("************ selected data contact ******* " + selectedData);

        if (selectedData != null) {
            System.out.println("************ selected data contact ******* " + selectedData.size());

            if (selectedData.size() > 0) {
                for (int k = 0; k < selectedData.size(); k++) {
                    if(selectedData.get(k).equals("Call"))
                    {
                        checkedItems[0] = true;
                        iscallSelected=true;
                        callTV.setChecked(true);

                    }
                    else if (selectedData.get(k).equals("Video"))
                    {
                        checkedItems[1] = true;
                        isVdoselected=true;
                        vdoTV.setChecked(true);
                    }

                    else if (selectedData.get(k).equals("Chat")) {
                        checkedItems[2] = true;
                        isChatSelected=true;
                        chatTV.setChecked(true);
                    }

                    else if (selectedData.get(k).equals("Text")) {
                        checkedItems[3] = true;
                        istextSelected=true;
                        textTV.setChecked(true);
                    }
                }
            }
        }


        contact_lay.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            System.out.println("************* imm is null ");
            imm.hideSoftInputFromWindow(contact_lay.getWindowToken(), 0);
        }

        callTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!iscallSelected)
                {
                    callTV.setChecked(true);
                    checkedItems[0]=true;
                    iscallSelected=true;
                }
                else
                {
                    callTV.setChecked(false);
                    checkedItems[0]=false;
                    iscallSelected=false;
                }

            }
        });



        vdoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isVdoselected)
                {
                    vdoTV.setChecked(true);
                    checkedItems[1]=true;
                    isVdoselected=true;
                }
                else
                {
                    vdoTV.setChecked(false);
                    checkedItems[1]=false;
                    isVdoselected=false;
                }

            }
        });



        chatTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isChatSelected)
                {
                    chatTV.setChecked(true);
                    checkedItems[2]=true;
                    isChatSelected=true;
                }
                else
                {
                    chatTV.setChecked(false);
                    checkedItems[2]=false;
                    isChatSelected=false;
                }

            }
        });
        textTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!istextSelected)
                {
                    textTV.setChecked(true);
                    checkedItems[3]=true;
                    istextSelected=true;
                }
                else
                {
                    textTV.setChecked(false);
                    checkedItems[3]=false;
                    istextSelected=false;
                }

            }
        });





        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedData = new ArrayList<>();

                System.out.println("selected option *********");
                System.out.println(checkedItems.toString());

                selectedData.clear();
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        selectedData.add(animals[i]);
                    }
                }

                String csv = selectedData.toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");

                System.out.println("************** selected contact option *******");
                System.out.println(selectedData.toString());
                contactByTv.setText(csv);
                saveData(getActivity().getApplicationContext(), "contact_by", csv);
                contact_lay.setVisibility(View.GONE);
            }
        });

cancel_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        contact_lay.setVisibility(View.GONE);

        System.out.println("** on cancel ******");

        if (selectedData!=null)
        {
            if (selectedData.size() > 0) {
                System.out.println(selectedData.size());
                for (int k = 0; k < selectedData.size(); k++)
                {
                    if (selectedData.get(k).equals("Call")) {
                        checkedItems[0] = false;

                    }

                    else if (selectedData.get(k).equals("Video")) {
                        checkedItems[1] = false;


                    }

                    else if (selectedData.get(k).equals("Chat")) {
                        checkedItems[2] = false;


                    }

                    else if (selectedData.get(k).equals("Text")) {
                        checkedItems[3] = false;


                    }
                }
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        selectedData.add(animals[i]);
                    }
                }

                String csv = selectedData.toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");

                System.out.println("************** selected contact option *******");
                System.out.println(selectedData.toString());
                contactByTv.setText(csv);

            }

        }
        else
        {        contact_lay.setVisibility(View.GONE);


            contactByTv.setText("Select");
        }
    }
});






    }


    private void nextClick() {

        if (contactByTv.getText().toString().equals("Select")) {
            Toast.makeText(getActivity(), "Select contact type", Toast.LENGTH_SHORT).show();


            contactByTv.requestFocus();
            return;
        }
        if (phoneNumberEditText.getText().length() == 0) {
            Toast.makeText(getActivity(), "Enter phone no.", Toast.LENGTH_SHORT).show();
            phoneNumberEditText.requestFocus();
            return;
        }

        if (contactNameEditText.getText().length() == 0) {
            Toast.makeText(getActivity(), "Enter contact name", Toast.LENGTH_SHORT).show();
            contactNameEditText.requestFocus();
            return;
        }
        if (spinnerLanguage.getSelectedItem().equals("Select")) {
            Toast.makeText(getActivity(), "Enter language", Toast.LENGTH_SHORT).show();
            return;
        }


        Bundle bundle = new Bundle();
        // bundle.putString("sub_cat_id",str);
        bundle.putString("privacy_str", spinnerPrivacy.getSelectedItem().toString());
        bundle.putString("phone_str", phoneNumberEditText.getText().toString());
        bundle.putString("Existence_str", ExtensionEditText.getText().toString());
        bundle.putString("contName_str", contactNameEditText.getText().toString());
        bundle.putString("language_str", spinnerLanguage.getSelectedItem().toString());
        bundle.putString("sub_cat_id", sub_cat_id);
        bundle.putString("MainCatType", MainCatType);
        bundle.putString("pcat_name", pcat_name);
        bundle.putStringArrayList("imageSet", imageSet);

        if (MainCatType.equals("101")) {
            bundle.putString("bussinessobj", obj);
        }

            else if (MainCatType.equals("102") || MainCatType.equals("272")) {

            bundle.putString("prop_obj", obj);
        }
        else {
            bundle.putStringArrayList("selectedcategories", categoryids);
            bundle.putString("postTitle", postTitle);
            bundle.putString("postDes", postDes);
            bundle.putString("postPrice", postPrice);
            bundle.putString("postSize", postSize);
            bundle.putString("postCondition", postCondition);

        }

        saveData(getActivity().getApplicationContext(),"pcat_id",MainCatType);
        Postyour6Add postyour6add = new Postyour6Add();
        postyour6add.setArguments(bundle);
        fm.beginTransaction().replace(R.id.allCategeriesIN, postyour6add).addToBackStack(null).commit();

    }

    private void OpenMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getActivity().startActivity(i);
    }

}
