package com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.checkData;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.saveDatatoLocal;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.cross_imageView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.pageNo_textView;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;


public class Postyour4Add extends Fragment implements View.OnClickListener{

    public static Typeface face;
    ArrayList<String> imageSet = new ArrayList<String>();
    ArrayList<String> categoryids = new ArrayList<String>();
    Context context;
    private Button next_btn;
    private TextView  currencyTV;
    private ImageButton back_arrowImage;
    private EditText postTitleEditText, postDescriptionEditText, priceEditText, sizeEditText, conditionEditText;
    //Fragment Manager
    private FragmentManager fm;
    private String sub_cat_id = "", MainCatType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View v = inflater.inflate(R.layout.postyour4add, null);

        face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "fonts/estre.ttf");
        fm = getActivity().getSupportFragmentManager();

        context = getActivity().getApplicationContext();
        findItem(v);
        onClickOnItem(v);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sub_cat_id = bundle.getString("sub_cat_id");
            MainCatType = bundle.getString("MainCatType");
            imageSet = bundle.getStringArrayList("imageSet");
            categoryids = bundle.getStringArrayList("categories");


        }
        MainCatType=getData(getActivity().getApplicationContext(),"pcat_id","");
        return v;
    }

    //finditems
    private void findItem(View v) {

        next_btn = (Button) v.findViewById(R.id.next_btn);
        currencyTV = (TextView) v.findViewById(R.id.currencyTV);
        back_arrowImage = (ImageButton) v.findViewById(R.id.back_arrowImage);

        postTitleEditText = (EditText) v.findViewById(R.id.propertyDescEditText);
        postDescriptionEditText = (EditText) v.findViewById(R.id.postDescriptionEditText);
        priceEditText = (EditText) v.findViewById(R.id.priceEditText);
        sizeEditText = (EditText) v.findViewById(R.id.sizeEditText);
        conditionEditText = (EditText) v.findViewById(R.id.conditionEditText);

        pageNo_textView.setText("4of7");
        next_btn.setTypeface(face);
        postTitleEditText.setTypeface(face);
        postDescriptionEditText.setTypeface(face);
        priceEditText.setTypeface(face);
        sizeEditText.setTypeface(face);
        conditionEditText.setTypeface(face);
        currencyTV.setTypeface(face);


        Locale locale = Locale.getDefault();
        Log.d("IssuesEtc","the local is " + locale);

        Currency currency = Currency.getInstance(locale);

        System.out.println("******* currency *********");
        System.out.println(currency.getCurrencyCode());
        System.out.println(currency.getSymbol());

        String code,symbol;
        code=currency.getCurrencyCode();
        symbol = currency.getSymbol();

        saveData(getActivity().getApplicationContext(),"currency_code",code);
        saveData(getActivity().getApplicationContext(),"currency_symbol",symbol);

        currencyTV.setText(code+symbol);


        if (getData(getActivity().getApplicationContext(),"first_entry","")!=null) {

            if (getData(getActivity().getApplicationContext(), "first_entry", "").equals("false")) {


                checkData("title_data", postTitleEditText, context);
                checkData("desc_data", postDescriptionEditText, context);
                checkData("price_data", priceEditText, context);
                checkData("condition_data", conditionEditText, context);
                checkData("size_data", sizeEditText, context);
            }
        }
//        checkData("condition_data",currencyTV);
    }


    private void onClickOnItem(View v) {
        next_btn.setOnClickListener(this);
        back_arrowImage.setOnClickListener(this);
        cross_imageView.setOnClickListener(this);
        currencyTV.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                if (postTitleEditText.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter Post Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (postDescriptionEditText.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter post Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (priceEditText.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter price", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (conditionEditText.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter condition", Toast.LENGTH_SHORT).show();
                    return;
                }


                saveData(getActivity().getApplicationContext(),"first_entry","false");

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imageSet", imageSet);
                bundle.putString("postTitle", postTitleEditText.getText().toString());
                bundle.putString("postDes", postDescriptionEditText.getText().toString());
                bundle.putString("postPrice", priceEditText.getText().toString());
                bundle.putString("postSize", sizeEditText.getText().toString());
                bundle.putString("postCondition", conditionEditText.getText().toString());
                bundle.putString("sub_cat_id", sub_cat_id);
                bundle.putString("MainCatType", MainCatType);
                bundle.putStringArrayList("selectedcategories", categoryids);
                bundle.putString("pcat_name", "For Sale");

                Postyour5Add postyour5add = new Postyour5Add();
                postyour5add.setArguments(bundle);
                fm.beginTransaction().replace(R.id.allCategeriesIN, postyour5add).addToBackStack(null).commit();

                break;

            case R.id.back_arrowImage:

                saveData(getActivity().getApplicationContext(),"first_entry","false");

                saveValue();
                getActivity().onBackPressed();

                break;
            case R.id.cross_imageView:
                OpenMainActivity();

                break;


            case R.id.currencyTV:
                final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String mcode, String symbol, int flagDrawableResID) {
                        System.out.println("*************Selected currency is ******");
                        System.out.println(name + " name");
                        System.out.println(mcode + " code");
                        System.out.println(symbol + " symbol");
                        System.out.println(flagDrawableResID + " flag");

                        if (symbol.equals("0")) {
                            symbol = "₹";
                        }

                        currencyTV.setText(mcode + symbol);

                        saveData(getActivity().getApplicationContext(),"currency_code",mcode);
                        saveData(getActivity().getApplicationContext(),"currency_symbol",symbol);


                        picker.dismiss();
                    }
                });
                picker.show(getActivity().getSupportFragmentManager(), "CURRENCY_PICKER");
                break;
        }
    }

    private void saveValue() {
        saveDatatoLocal("title_data", postTitleEditText,context);
        saveDatatoLocal("desc_data", postDescriptionEditText,context);
        saveDatatoLocal("price_data", priceEditText,context);
        saveDatatoLocal("condition_data", conditionEditText,context);
        saveDatatoLocal("size_data", sizeEditText,context);
    }

    private void OpenMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getActivity().startActivity(i);
    }

}
