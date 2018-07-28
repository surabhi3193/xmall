package com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postProperty.Postyour4Property;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY;
import com.mindinfo.xchangemall.xchangemall.beans.categories;

import java.util.ArrayList;
import java.util.Objects;

import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.cross_imageView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.pageNo_textView;
import static com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY.idarray;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class Postyour3Add extends Fragment implements View.OnClickListener {

    ArrayList<String> imageSet = new ArrayList<>();
    MULTIPLEsELECTIONcATEGORY postadapter;

    private FragmentManager fm;
    private ArrayList<categories> arrayList = new ArrayList<>();
    private String MainCatType;


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.postyour3add, null);

        fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MainCatType = bundle.getString("MainCatType");
            imageSet = new ArrayList<>();
            imageSet = bundle.getStringArrayList("imageSet");
        }
        MainCatType = getData(getActivity(), "pcat_id", "");

        ListView cat_sub_list_view = v.findViewById(R.id.cat_sub_list_view);
        Button next_btn = v.findViewById(R.id.next_btn);

        ImageButton back_arrowImage = v.findViewById(R.id.back_arrowImage);
        Button cat_TextView = v.findViewById(R.id.cat_TextView);


        pageNo_textView.setText("3of7");

        switch (MainCatType) {
            case "101":
                cat_TextView.setText("For Sale");
                break;
            case "102":
                cat_TextView.setText("Property Rent");
                break;
            case "272":
                cat_TextView.setText("Property Sale");
                break;
        }

        System.out.println("**** main cat type ***** " + MainCatType);

        next_btn.setOnClickListener(this);
        back_arrowImage.setOnClickListener(this);
        cross_imageView.setOnClickListener(this);

        if (getData(getActivity(), "first_entry_cat", "") != null) {

            if (getData(getActivity(), "first_entry_cat", "").equals("true")) {
                idarray.clear();
            }

        }
        postadapter = new MULTIPLEsELECTIONcATEGORY(arrayList, getActivity());
        cat_sub_list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        cat_sub_list_view.setAdapter(postadapter);

        String str = MainCatType;

        NetworkClass.getListData(str, arrayList, getActivity());

        postadapter.notifyDataSetChanged();


        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:

                System.out.println("**** main cat type on click ***** " + MainCatType);
                System.out.println("********* selected fields ***********");
                System.out.println(idarray);
                System.out.println(idarray.size());

                if (idarray.size() > 0) {
                    saveData(Objects.requireNonNull(getActivity()).getApplicationContext(), "first_entry_cat", "false");

                    Bundle bundle = new Bundle();
                    //bundle.putStringArray("imagess",str_image_arr);
                    bundle.putStringArrayList("imageSet", imageSet);
                    bundle.putStringArrayList("categories", idarray);
                    bundle.putString("MainCatType", MainCatType);

                    String category = idarray.toString().replace("[", "").replace("]", "")
                            .replace(", ", ",");
                    saveData(getActivity(), "categories", category);

                    switch (MainCatType) {
                        case "104":
                            Postyour4Add postyour4Add = new Postyour4Add();
                            postyour4Add.setArguments(bundle);
                            fm = getActivity().getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.allCategeriesIN, postyour4Add)
                                    .addToBackStack(null).commit();
                            break;
                        case "102": {
                            Postyour4Property postyour4add = new Postyour4Property();
                            postyour4add.setArguments(bundle);
                            fm = getActivity().getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.allCategeriesIN, postyour4add).addToBackStack(null).commit();

                            break;
                        }
                        case "272": {
                            Postyour4Property postyour4add = new Postyour4Property();

                            postyour4add.setArguments(bundle);
                            fm = getActivity().getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.allCategeriesIN, postyour4add).addToBackStack(null).commit();

                            break;
                        }
                    }


                } else {
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Please select atleast 1 category to proceed ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.back_arrowImage:
                saveData(Objects.requireNonNull(getActivity()).getApplicationContext(), "first_entry_cat", "false");

                getActivity().onBackPressed();
                break;
            case R.id.cross_imageView:
                OpenMainActivity();
                break;
        }
    }

    private void OpenMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Objects.requireNonNull(getActivity()).startActivity(i);
    }
}

