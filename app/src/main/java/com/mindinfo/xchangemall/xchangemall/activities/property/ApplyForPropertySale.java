package com.mindinfo.xchangemall.xchangemall.activities.property;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.isMatch;
import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.activities.main.SplashScreen.face;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class ApplyForPropertySale extends AppCompatActivity {
    private TextView tvfullname, tvphone, tvemail, tvrenatlhead, tvsettinghead, tvmovein, tvrenters, tvpets, tvsmokers, tvincome, tvcredit, tvmovingFrom,
            tvworkplace, tvjobtitle, tvalways,currencyTV,rightTV;
    private EditText etfullname, etphone, etemail, etmovein, etrenters, etpets, etsmokers, etincome, etcredit, etmovingFrom, etworkplace, etjobtitle;
    private String fullname="", phone="", email="", movein="", renters="", pets="",
            smokers="", income="", credit="", movingFrom="", workplace="",
            jobtitle="",post_id="",savedApp = "";;
    private Button requesst_btn;
    private ImageView back_btn;
    private ScrollView scrollview;
    private LinearLayout formLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_property_sale);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            post_id = bundle.getString("post_id", "");

        initui();
        changefont();

        requesst_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
getEntereData();
                validateForm();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etjobtitle.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    System.out.println("=========== action done ==========");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tvalways.getWindowToken(), 0);


                    scrollview.fullScroll(View.FOCUS_DOWN);

                    etjobtitle.setCursorVisible(false);
                    return true;
                }
                return false;
            }

        });
        currencyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        picker.dismiss();
                    }
                });
                picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
            }
        });


        rightTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAppliation();

            }
        });
        savedApp = getData(getApplicationContext(), "saved_rental_application", "");
        System.out.println("=================== saved data ==============");
        System.out.println(savedApp);
        if (savedApp != null && savedApp.length() > 0)
            getSavedData(savedApp);

    }


    private void initui() {
        formLay = (LinearLayout)findViewById(R.id.formLay);
        currencyTV = (TextView)findViewById(R.id.currencyTV);
        rightTV = (TextView) findViewById(R.id.rightTV);

        tvfullname = (TextView) findViewById(R.id.fullname_header);
        tvphone = (TextView) findViewById(R.id.phone_header);
        tvemail = (TextView) findViewById(R.id.email_header);
        tvrenatlhead = (TextView) findViewById(R.id.rental_header);
        tvmovein = (TextView) findViewById(R.id.TVmovein);
        tvrenters = (TextView) findViewById(R.id.Tvrenter);
        tvpets = (TextView) findViewById(R.id.TVpets);
        tvsmokers = (TextView) findViewById(R.id.TVsmokers);
        tvincome = (TextView) findViewById(R.id.Tvincome);
        tvcredit = (TextView) findViewById(R.id.TVcredit);
        tvmovingFrom = (TextView) findViewById(R.id.TVmoingfrom);
        tvworkplace = (TextView) findViewById(R.id.TVworkplace);
        tvjobtitle = (TextView) findViewById(R.id.TVjobtitle);
        tvsettinghead = (TextView) findViewById(R.id.setting_header);
        tvalways = (TextView) findViewById(R.id.alwaystv);
        TextView   pageTitleTV = (TextView) findViewById(R.id.pageTitleTV);

        etfullname = (EditText) findViewById(R.id.fullnameEt);
        etphone = (EditText) findViewById(R.id.phoneET);
        etemail = (EditText) findViewById(R.id.emailET);
        etmovein = (EditText) findViewById(R.id.Etmovein);
        etrenters = (EditText) findViewById(R.id.Etrenter);
        etpets = (EditText) findViewById(R.id.Etpets);
        etincome = (EditText) findViewById(R.id.Etincome);
        etcredit = (EditText) findViewById(R.id.Etcredit);
        etsmokers = (EditText) findViewById(R.id.Etsmokers);
        etworkplace = (EditText) findViewById(R.id.Etworkplace);
        etjobtitle = (EditText) findViewById(R.id.Etjobtitle);
        etmovingFrom = (EditText) findViewById(R.id.Etmovingfrom);
        requesst_btn = (Button) findViewById(R.id.send_Request_btn);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        scrollview=(ScrollView)findViewById(R.id.scrollview);


        pageTitleTV.setText("Application Form");
        pageTitleTV.setTypeface(face);
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
        currencyTV.setText(code+symbol);

    }


    private void changefont() {
        rightTV.setTypeface(face);
        tvfullname.setTypeface(face);
        tvphone.setTypeface(face);
        tvemail.setTypeface(face);
        tvrenatlhead.setTypeface(face);
        tvsettinghead.setTypeface(face);
        tvmovein.setTypeface(face);
        tvrenters.setTypeface(face);
        tvpets.setTypeface(face);
        tvsmokers.setTypeface(face);
        tvincome.setTypeface(face);
        tvcredit.setTypeface(face);
        tvmovingFrom.setTypeface(face);
        tvworkplace.setTypeface(face);
        tvjobtitle.setTypeface(face);
        tvalways.setTypeface(face);

        etfullname.setTypeface(face);
        etphone.setTypeface(face);
        etemail.setTypeface(face);
        etmovein.setTypeface(face);
        etrenters.setTypeface(face);
        etpets.setTypeface(face);
        etsmokers.setTypeface(face);
        etincome.setTypeface(face);
        etcredit.setTypeface(face);
        etmovingFrom.setTypeface(face);
        etworkplace.setTypeface(face);
        etjobtitle.setTypeface(face);

        requesst_btn.setTypeface(face);
        rightTV.setVisibility(View.VISIBLE);


    }

    private void validateForm() {

        String em = etemail.getText().toString();
        email=em;
        String empatt = "^[a-zA-Z0-9_.]+@[a-zA-Z]+\\.[a-zA-Z]+$";
        boolean b4 = isMatch(em, empatt);
        if (fullname.length() == 0) {
            etfullname.setError("Name Required");
            etfullname.requestFocus();

        }

        else if (phone.length() == 0) {
            etphone.setError("Phone Required");
            etphone.requestFocus();

        }

        else if (email.length() == 0 || !b4) {
            etemail.setError("Invalid Email");
            etemail.requestFocus();

        }
        else  if (movein.length() == 0) {
            etmovein.setError("Field Required");
            etmovein.requestFocus();
        }
        else if (renters.length() == 0) {
            etrenters.setError("Field Required");
            etrenters.requestFocus();
        }
        else if (pets.length() == 0) {
            etpets.setError("Field Required");
            etpets.requestFocus();
        }

        else if (smokers.length() == 0) {
            etsmokers.setError("Field Required");
            etsmokers.requestFocus();
        }

        else if (income.length() == 0) {
            etincome.setError("Field Required");
            etincome.requestFocus();
        }
        else if (credit.length() == 0) {
            etcredit.setError("Field Required");
            etcredit.requestFocus();
        }

        else if (movingFrom.length() == 0) {
            etmovingFrom.setError("Field Required");
            etmovingFrom.requestFocus();
        }

        else if (workplace.length() == 0) {
            etworkplace.setError("Field Required");
            etworkplace.requestFocus();
        }

        else if (jobtitle.length() == 0) {
            etjobtitle.setError("Field Required");
            etjobtitle.requestFocus();
        }

        else {
            String user_id = getData(getApplicationContext(), "user_id", "");
            requestRental(user_id, post_id);
        }

    }

    private void requestRental(String user_id, String post_id) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams category = new RequestParams();

        TimeZone timezone = TimeZone.getDefault();
        System.out.println("* timezone ****** " + timezone.getID());

        final ProgressDialog ringProgressDialog;

        ringProgressDialog = ProgressDialog.show(ApplyForPropertySale.this, "Please wait ...", "Applying...", true);
        ringProgressDialog.setCancelable(false);


        category.put("user_id", user_id);
        category.put("post_id", post_id);
        category.put("full_name", fullname);
        category.put("phone_no", phone);
        category.put("user_email", email);
        category.put("when_move", movein);
        category.put("how_many", renters);
        category.put("have_pets", pets);
        category.put("smoker", smokers);
        category.put("all_incomes", income);
        category.put("cradit_range", credit);
        category.put("moving_from", movingFrom);
        category.put("job_title", jobtitle);
        category.put("work_place", workplace);
        category.put("timezone", timezone.getID());

        System.out.println("***** requested params for request rental ****");
        System.out.println(category);
        client.post(BASE_URL_NEW + "add_rental_form", category, new JsonHttpResponseHandler() {


            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Bad Server Connection", Toast.LENGTH_SHORT).show();
                System.out.println(errorResponse);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  JSONObject response) {
                ringProgressDialog.dismiss();

                System.out.println("************* applied for rental successfully **********");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void saveAppliation() {
        Context context = getApplicationContext();
        getEntereData();
        email=etemail.getText().toString();

        JSONObject resumeObj = new JSONObject();

        try {
            resumeObj.put("fullname", fullname);
            resumeObj.put("phone", phone);
            resumeObj.put("email", email);

            resumeObj.put("movein", movein);
            resumeObj.put("renters", renters);
            resumeObj.put("pets", pets);
            resumeObj.put("smokers", smokers);
            resumeObj.put("income", income);
            resumeObj.put("credit", credit);
            resumeObj.put("movingFrom", movingFrom);
            resumeObj.put("workplace", workplace);
            resumeObj.put("jobtitle", jobtitle);

            System.out.println("********** saved application data *****");
            System.out.println(resumeObj);
            saveData(context, "saved_rental_application", resumeObj.toString());

            Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
            rightTV.setTextColor(getResources().getColor(R.color.green_shade));
            rightTV.setText("Save/Edit");
        } catch (JSONException e) {
            System.out.println("********** saved application data error  *****");
            e.printStackTrace();

        }

    }

    private void getEntereData() {
        fullname = etfullname.getText().toString();
        phone = etphone.getText().toString();
        movein = etmovein.getText().toString();
        renters = etrenters.getText().toString();
        pets = etpets.getText().toString();
        smokers = etsmokers.getText().toString();
        income = etincome.getText().toString();
        credit = etcredit.getText().toString();
        movingFrom = etmovingFrom.getText().toString();
        workplace = etworkplace.getText().toString();
        jobtitle = etjobtitle.getText().toString();

    }

    private void getSavedData(String savedApp) {
        try {
            JSONObject resumeObj = new JSONObject(savedApp);

            fullname = resumeObj.getString("fullname");
            phone = resumeObj.getString("phone");
            email = resumeObj.getString("email");

            movein = resumeObj.getString("email");
            renters = resumeObj.getString("renters");
            pets = resumeObj.getString("pets");
            smokers = resumeObj.getString("smokers");
            income = resumeObj.getString("income");
            credit = resumeObj.getString("credit");
            movingFrom = resumeObj.getString("movingFrom");
            workplace = resumeObj.getString("workplace");
            jobtitle = resumeObj.getString("jobtitle");

            setSavedData();


        } catch (JSONException e) {

            System.out.println("********* error on get json saved ******** ");
            e.printStackTrace();
        }
    }

    private void setSavedData() {
        rightTV.setTextColor(getResources().getColor(R.color.green_shade));
        rightTV.setText("Save/Edit");
        etfullname.setText(fullname);
        etphone.setText(phone);
        etemail.setText(email);

        etmovein.setText(movein);
        etrenters.setText(renters);
        etpets.setText(pets);
        etsmokers.setText(smokers);
        etincome.setText(income);
        etcredit.setText(credit);
        etmovingFrom.setText(movingFrom);
        etworkplace.setText(workplace);
        etjobtitle.setText(jobtitle);


    }


//    private void getEntereData() {
//        saveDatatoLocal("rental_fullname",etfullname,getApplicationContext());
//        saveDatatoLocal("rental_number",etphone,getApplicationContext());
//        saveDatatoLocal("rental_email",etemail,getApplicationContext());
//        saveDatatoLocal("rental_movein",etmovein,getApplicationContext());
//        saveDatatoLocal("rental_count",etrenters,getApplicationContext());
//        saveDatatoLocal("pets",etpets,getApplicationContext());
//        saveDatatoLocal("smoking",etsmokers,getApplicationContext());
//        saveDatatoLocal("income",etincome,getApplicationContext());
//        saveDatatoLocal("credit_range",etcredit,getApplicationContext());
//        saveDatatoLocal("rental_movingfrom",etmovingFrom,getApplicationContext());
//        saveDatatoLocal("rental_workplace",etworkplace,getApplicationContext());
//        saveDatatoLocal("rental_jobtitle",etjobtitle,getApplicationContext());
//    }
}
