package com.example.iir_ai_hospital;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;

public class MedicalCardFragment extends Fragment {

    @BindView(R.id.listview_card) ListView listView;


    @OnClick(R.id.imgBtn_back) void onBackClick() {
        JumpNextFragment(MedicalNumberFragment.newInstance(), "MedicalNumber", "rl");
    }

    private JSONArray jsonArray;
    public static MedicalCardFragment newInstance(Bundle args) {
        MedicalCardFragment f = new MedicalCardFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("medical number card", getArguments().getString("patientProfile"));
        try{
            jsonArray = new JSONArray(getArguments().getString("patientProfile"));
            Log.d("chart non", jsonArray.getJSONObject(0).getString("Chart_No"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_medical_card, container, false);
        ButterKnife.bind(this, v);

        ListAddapter listAddapter = new ListAddapter();
        listView.setAdapter(listAddapter);
        return v;
    }

    private class ListAddapter extends BaseAdapter {

        ImageView imageView;
        Button button;
        TextView patient_name;
        TextView patient_chart_no;
        TextView patient_birth;

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myview = inflater.inflate(R.layout.medical_number_card, viewGroup, false);

            imageView = myview.findViewById(R.id.imageView);
            button = myview.findViewById(R.id.button);
            patient_name = myview.findViewById(R.id.tv_card_name);
            patient_birth = myview.findViewById(R.id.tv_card_birth);
            patient_chart_no = myview.findViewById(R.id.tv_card_chart_no);

            String name, birth, _id, sex, chart_no;
            try {
                name = jsonArray.getJSONObject(i).get("Patient_Name").toString();
                birth = jsonArray.getJSONObject(i).get("Birth").toString();
                _id = jsonArray.getJSONObject(i).get("id_number").toString();
                sex = jsonArray.getJSONObject(i).get("Sex").toString();
                chart_no = jsonArray.getJSONObject(i).get("Chart_No").toString();

                if(sex.equals("男")){
                    imageView.setImageResource(R.drawable.man);
                }
                else {
                    imageView.setImageResource(R.drawable.woman);
                }


                patient_name.setText(name);
                patient_birth.setText(birth);
                patient_chart_no.setText(chart_no);

                button.setOnClickListener(view1 -> {
                    Log.d("patient_name", name);
                    Log.d("Chart_No", chart_no);
                    Bundle bundle = new Bundle();
                    bundle.putString("Patient_Name", name);
                    bundle.putString("Patient_Birth", birth);
                    bundle.putString("Patient_Id", _id);
                    bundle.putString("Chart_No", chart_no);
                    bundle.putString("sex", sex);
                    JumpNextFragment(LoginFragment.newInstance(bundle), "Login", "lr");
                });

            }
            catch (JSONException e) {
                e.printStackTrace();
            }




            return myview;
        }

        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public Object getItem(int i) {
            try{
                return jsonArray.get(i);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    }
}
