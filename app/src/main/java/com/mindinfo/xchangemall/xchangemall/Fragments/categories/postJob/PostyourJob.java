package com.mindinfo.xchangemall.xchangemall.Fragments.categories.postJob;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour5Add;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.beans.categories;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.cross_imageView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.pageNo_textView;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class PostyourJob extends Fragment implements View.OnClickListener{

    public static  String job_responsbitity="", job_exp="" ,job_salary="",job_title="";
    ArrayList<String> imageSet = new ArrayList<String>();

    private Button next_btn;
    private AppCompatSpinner SpinnerCat,spinnerSalary,JopTypeSpinner;
    private TextView cat_TextView,currencyTV;
    private ImageButton back_arrowImage;
    private EditText EditTextJobDecs,EditTextJobRes,EditTextJobSkill,EditTextJobLoc,SalaryET;
    private FragmentManager fm;
    private String sub_cat_id = "",MainCatType,symbol;
    private ArrayList<categories> arrayList = new ArrayList<>();
    private NestedScrollView scrollview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.postyour4addforjob,null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        fm = getActivity().getSupportFragmentManager();

        findItem(v);
        onClickOnItem(v);
        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            MainCatType = bundle.getString("MainCatType");
            imageSet = new ArrayList<String>();
            imageSet = bundle.getStringArrayList("imageSet");

        }

        loadSpinner();
        return v;
    }

    //finditems
    private void findItem(View v)
    {
        scrollview = (NestedScrollView)v.findViewById(R.id.scrollView);

        SpinnerCat = (AppCompatSpinner) v.findViewById(R.id.SpinnerCat);
        spinnerSalary = (AppCompatSpinner) v.findViewById(R.id.spinnerSalary);
        JopTypeSpinner = (AppCompatSpinner) v.findViewById(R.id.JopTypeSpinner);
        next_btn = (Button) v.findViewById(R.id.next_btn);

        cat_TextView = (TextView) v.findViewById(R.id.cat_TextView);
        currencyTV = (TextView) v.findViewById(R.id.currencyTV);
        back_arrowImage = (ImageButton) v.findViewById(R.id.back_arrowImage);

        EditTextJobDecs = (EditText) v.findViewById(R.id.EditTextBusinessName);
        EditTextJobRes = (EditText) v.findViewById(R.id.EditTextAbout);
        EditTextJobSkill = (EditText) v.findViewById(R.id.EditTextDescription);
        EditTextJobLoc = (EditText) v.findViewById(R.id.EditTextSocialMedia);
        SalaryET = (EditText) v.findViewById(R.id.SalaryET);

        pageNo_textView.setText("4of7");
       Typeface face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "fonts/estre.ttf");

       next_btn.setTypeface(face);
        pageNo_textView.setTypeface(face);
        EditTextJobDecs.setTypeface(face);
        EditTextJobRes.setTypeface(face);
        EditTextJobSkill.setTypeface(face);
        EditTextJobLoc.setTypeface(face);
//        cat_TextView.setTypeface(face);
        currencyTV.setTypeface(face);

        String code = getData(getActivity().getApplicationContext(),"currency_code","");
         symbol = getData(getActivity().getApplicationContext(),"currency_symbol","");
        currencyTV.setText(code+symbol);

        EditTextJobLoc.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    System.out.println("=========== action done ==========");
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(EditTextJobLoc.getWindowToken(), 0);

                    scrollview.fullScroll(View.FOCUS_DOWN);
                    EditTextJobLoc.requestFocus();
                    EditTextJobLoc.setCursorVisible(true);
                    return true;
                }
                return false;
            }

        });

    }

    private void onClickOnItem(View v)
    {
        currencyTV.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        back_arrowImage.setOnClickListener(this);
        cross_imageView.setOnClickListener(this);
    }

    //load spinner cat
    private void loadSpinner()
    {
        int catm = Integer.parseInt(MainCatType);
        NetworkClass.getListData("103", arrayList, getActivity().getApplicationContext());

        // postAdapter.notifyDataSetChanged();
        ArrayAdapter<categories> dataAdapter = new ArrayAdapter<categories>(getActivity(),
                android.R.layout.simple_spinner_item, arrayList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerCat.setAdapter(dataAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.next_btn:
                String jobtype = JopTypeSpinner.getSelectedItem().toString();
                if(SalaryET.getText().length() == 0)
                {
                    Toast.makeText(getActivity(), "Enter Salary", Toast.LENGTH_SHORT).show();
                    SalaryET.requestFocus();
                    return;
                }
                if(EditTextJobLoc.getText().length() == 0)
                {
                    Toast.makeText(getActivity(), "Enter Location", Toast.LENGTH_SHORT).show();
                    EditTextJobLoc.requestFocus();
                    return;
                }

                if(jobtype.equals("Select"))
                {
                    Toast.makeText(getActivity(), "Enter Job type", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(EditTextJobRes.getText().length() == 0)
                {
                    Toast.makeText(getActivity(), "Enter Job responsibility", Toast.LENGTH_SHORT).show();
                    EditTextJobRes.requestFocus();
                    return;
                }


                if(EditTextJobSkill.getText().length() == 0)
                {
                    Toast.makeText(getActivity(), "Enter Skills", Toast.LENGTH_SHORT).show();
                    EditTextJobSkill.requestFocus();
                    return;
                }

                if(EditTextJobDecs.getText().length()==0)
                {
                    Toast.makeText(getActivity(), "Enter Post Description", Toast.LENGTH_SHORT).show();
                    EditTextJobDecs.requestFocus();

                    return;
                }


                ArrayList<String> categoryids = new ArrayList<String>();
                categories cat = arrayList.get(SpinnerCat.getSelectedItemPosition());

                String postDescr = EditTextJobDecs.getText().toString();
                String salary_As_per = SalaryET.getText().toString();
                String salary_ext = spinnerSalary.getSelectedItem().toString();
                String salary_symbol = currencyTV.getText().toString();

                job_salary =symbol+salary_As_per+salary_ext;
                System.out.println("**********job salry **** " + job_salary);
                job_exp = EditTextJobSkill.getText().toString();
                job_title = SpinnerCat.getSelectedItem().toString();
                job_responsbitity = EditTextJobRes.getText().toString();

                categoryids.add(cat.getId());
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imageSet",imageSet);
                bundle.putString("sub_cat_id",cat.getId());
                bundle.putString("MainCatType","103");
                bundle.putString("pcat_name","Jobs");
                saveData(getActivity().getApplicationContext(),"MainCatType","103");
                bundle.putString("postDes",postDescr);
                bundle.putStringArrayList("selectedcategories",categoryids);

                saveData(getActivity().getApplicationContext(),"job_type",jobtype);
                Postyour5Add postyour4add = new Postyour5Add();
                postyour4add.setArguments(bundle);
                fm.beginTransaction().replace(R.id.allCategeriesIN,postyour4add).addToBackStack(null).commit();

                break;

            case R.id.back_arrowImage:
                getActivity().onBackPressed();
                break;
            case R.id.cross_imageView:
                OpenMainActivity();
                break;

            case R.id.currencyTV:
                final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        System.out.println("*************Selected currency is ******");
                        System.out.println(name + " name");
                        System.out.println(code + " code");
                        System.out.println(symbol + " symbol");
                        System.out.println(flagDrawableResID + " flag");

                        if (symbol.equals("0"))
                            symbol="₹";

                        currencyTV.setText(code + symbol);

                        saveData(getActivity().getApplicationContext(),"currency_code",code);
                        saveData(getActivity().getApplicationContext(),"currency_symbol",symbol);

                        picker.dismiss();
                    }
                });
                picker.show(getActivity().getSupportFragmentManager(), "CURRENCY_PICKER");
                break;

        }
    }

    private void OpenMainActivity()
    {
        Intent i = new Intent(getActivity(), MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getActivity().startActivity(i);
    }

}