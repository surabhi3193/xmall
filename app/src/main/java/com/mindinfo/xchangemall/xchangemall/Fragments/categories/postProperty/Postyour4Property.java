package com.mindinfo.xchangemall.xchangemall.Fragments.categories.postProperty;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour5Add;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.OpenMainActivity;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.checkData;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.saveDatatoLocal;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.cross_imageView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.pageNo_textView;
import static com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity.iscatChecked;
import static com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity.isdogChecked;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;


public class Postyour4Property extends Fragment implements View.OnClickListener {

    public static Typeface face;
    ArrayList<String> imageSet = new ArrayList<String>();
    ArrayList<String> categoryids = new ArrayList<String>();
    Context context;
    private Button next_btn,cat_TextView;

    private TextView currencyTV;
    private ImageButton back_arrowImage;
    private EditText propertyDescEditText, unitsEditText, priceEditText, sizeEditText, bathroomEditText;
    //Fragment Manager
    private FragmentManager fm;
    private String sub_cat_id = "", MainCatType;
    private CheckBox dog_check, cat_check;
    private AppCompatSpinner spinnerRent;
    private String property_type="";
    String rent_ext = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        View v = inflater.inflate(R.layout.postyour4property, null);

        face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "fonts/estre.ttf");
        fm = getActivity().getSupportFragmentManager();
        context = getActivity().getApplicationContext();

        MainCatType = getData(getActivity().getApplicationContext(), "pcat_id", "");

        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            sub_cat_id = bundle.getString("sub_cat_id");
            MainCatType = bundle.getString("MainCatType");

            imageSet = bundle.getStringArrayList("imageSet");
            categoryids = bundle.getStringArrayList("categories");

            System.out.println("********** selected p cat id ****** " + MainCatType);
            System.out.println("********** selected  cat id ****** " + categoryids);

        }

        findItem(v);
        getLocale(currencyTV);
        onClickOnItem(v);

        System.out.println("********** selected p cat id saved ****** " + MainCatType);

        if (MainCatType.equals("101"))
            cat_TextView.setText(R.string.for_sale);
        else if (MainCatType.equals("102")) {
            spinnerRent.setVisibility(View.VISIBLE);
            cat_TextView.setText(R.string.property_rentals);
        }

        else if (MainCatType.equals("272"))
        {
            spinnerRent.setVisibility(View.GONE);
            cat_TextView.setText(R.string.property_for_sale);
        }


        return v;
    }

    private void getLocale(TextView currencyTV) {

        Locale locale = Locale.getDefault();
        Log.d("IssuesEtc", "the local is " + locale);

        Currency currency = Currency.getInstance(locale);

        System.out.println("******* currency *********");
        System.out.println(currency.getCurrencyCode());
        System.out.println(currency.getSymbol());

        String code, symbol;
        code = currency.getCurrencyCode();
        symbol = currency.getSymbol();

        saveData(getActivity().getApplicationContext(), "currency_code", code);
        saveData(getActivity().getApplicationContext(), "currency_symbol", symbol);

        currencyTV.setText(code + symbol);

        if (getData(getActivity().getApplicationContext(), "first_entry", "") != null) {

            if (getData(getActivity().getApplicationContext(), "first_entry", "").equals("false")) {


                checkData("title_data", propertyDescEditText, context);
                checkData("desc_data", unitsEditText, context);
                checkData("price_data", priceEditText, context);
                checkData("bathroom_data", bathroomEditText, context);
                checkData("size_data", sizeEditText, context);
            }
        }
    }

    //finditems
    private void findItem(View v) {
        final NestedScrollView scrollview = (NestedScrollView) v.findViewById(R.id.scroll_view);

        spinnerRent = (AppCompatSpinner) v.findViewById(R.id.spinnerSalary);

        next_btn = (Button) v.findViewById(R.id.next_btn);

        currencyTV = (TextView) v.findViewById(R.id.currencyTV);
        back_arrowImage = (ImageButton) v.findViewById(R.id.back_arrowImage);

        propertyDescEditText = (EditText) v.findViewById(R.id.propertyDescEditText);
        unitsEditText = (EditText) v.findViewById(R.id.unitsEditText);
        priceEditText = (EditText) v.findViewById(R.id.priceEditText);
        sizeEditText = (EditText) v.findViewById(R.id.sizeEditText);
        bathroomEditText = (EditText) v.findViewById(R.id.bathroomEditText);
        dog_check = (CheckBox) v.findViewById(R.id.dog_checked);
        cat_check = (CheckBox) v.findViewById(R.id.cat_checked);
        cat_TextView = (Button) v.findViewById(R.id.cat_TextView);

        pageNo_textView.setText(R.string.page4);

        next_btn.setTypeface(face);
        propertyDescEditText.setTypeface(face);
        unitsEditText.setTypeface(face);
        priceEditText.setTypeface(face);
        sizeEditText.setTypeface(face);
        bathroomEditText.setTypeface(face);
        currencyTV.setTypeface(face);

        getCheckBOxData();
        bathroomEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    System.out.println("=========== action done ==========");
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(dog_check.getWindowToken(), 0);


                    scrollview.fullScroll(View.FOCUS_DOWN);
                    bathroomEditText.setCursorVisible(false);
                    propertyDescEditText.setCursorVisible(false);
                    return true;
                }
                return false;
            }

        });

//        checkData("condition_data",currencyTV);
    }

    private void getCheckBOxData() {
        if (isdogChecked)
            dog_check.setChecked(true);
        else
            dog_check.setChecked(false);

        if (iscatChecked)
            cat_check.setChecked(true);
        else
            cat_check.setChecked(false);
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


                if (propertyDescEditText.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter Property Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (unitsEditText.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter Units", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (priceEditText.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter price", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bathroomEditText.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter Washroom units", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((spinnerRent.getVisibility()==View.VISIBLE))
                {
                    rent_ext = spinnerRent.getSelectedItem().toString();
                    if (rent_ext.equalsIgnoreCase("select") || rent_ext.length() < 0) {
                        Toast.makeText(getActivity(), "Select rental type", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                saveData(getActivity().getApplicationContext(), "first_entry", "false");

                JSONObject propObj = new JSONObject();
                try {

                    String unit = unitsEditText.getText().toString();
                    String desc = propertyDescEditText.getText().toString();
                    String price = priceEditText.getText().toString();
                    String size = sizeEditText.getText().toString();
                    String b_unit = bathroomEditText.getText().toString();

                    propObj.put("rent_ext", rent_ext);
                    propObj.put("room_unit", unit);
                    propObj.put("property_desc", desc);
                    propObj.put("property_price", price+rent_ext);
                    propObj.put("prop_size", size);
                    propObj.put("bathroom_unit", b_unit);
                    System.out.println("** prop obj in 4 frag *****" + propObj);
                    saveData(getActivity().getApplicationContext(), "prop_obj", propObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                setCheckBoxData();

                System.out.println("==============dogchecked================= " + isdogChecked);
                System.out.println("================Catchecked ============" + iscatChecked);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imageSet", imageSet);
                bundle.putString("prop_obj", propObj.toString());
                bundle.putString("sub_cat_id", sub_cat_id);
                bundle.putString("MainCatType", MainCatType);
                bundle.putStringArrayList("selectedcategories", categoryids);
                bundle.putString("pcat_name", "property_Rent");

                Postyour5Add postyour4add = new Postyour5Add();
                postyour4add.setArguments(bundle);
                fm.beginTransaction().replace(R.id.allCategeriesIN, postyour4add).addToBackStack(null).commit();

                break;

            case R.id.back_arrowImage:


                setCheckBoxData();
                saveData(getActivity().getApplicationContext(), "first_entry", "false");

                saveValue();
                getActivity().onBackPressed();

                break;
            case R.id.cross_imageView:
                OpenMainActivity(getActivity());
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

                        saveData(getActivity().getApplicationContext(), "currency_code", mcode);
                        saveData(getActivity().getApplicationContext(), "currency_symbol", symbol);


                        picker.dismiss();
                    }
                });
                picker.show(getActivity().getSupportFragmentManager(), "CURRENCY_PICKER");
                break;
        }
    }

    private void saveValue() {
        saveDatatoLocal("title_data", propertyDescEditText, context);
        saveDatatoLocal("desc_data", unitsEditText, context);
        saveDatatoLocal("price_data", priceEditText, context);
        saveDatatoLocal("bathroom_data", bathroomEditText, context);
        saveDatatoLocal("size_data", sizeEditText, context);
    }

    private void setCheckBoxData()

    {
        if (dog_check.isChecked()) {
            isdogChecked = true;

        }
        else
        {
            isdogChecked=false;
        }
        if (cat_check.isChecked()) {
            iscatChecked = true;
        }
        else
        {
            iscatChecked=false;
        }
    }

}
