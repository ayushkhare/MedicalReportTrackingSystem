package sample.com.medicalrecordtrackingsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.com.medicalrecordtrackingsystem.models.Hospital;
import sample.com.medicalrecordtrackingsystem.rest.ApiClient;
import sample.com.medicalrecordtrackingsystem.rest.ApiInterface;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.hospital_spinner)
    SearchableSpinner hospitalSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getDataFromApi();
    }

    private void getDataFromApi() {
        ApiInterface apiService = ApiClient.getCient().create(ApiInterface.class);
        Call<List<Hospital>> call = apiService.getHospitals();
        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Hospital> hospitalList = response.body();
                        populateList(hospitalList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {

            }
        });
    }

    private void populateList(List<Hospital> hospitalList) {
        List<String> hospitalNames = new ArrayList<>();
        for (Hospital hospital : hospitalList) {
            hospitalNames.add(hospital.getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hospitalNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitalSpinner.setAdapter(spinnerAdapter);
        hospitalSpinner.setTitle(getString(R.string.select_hospital));
        hospitalSpinner.setPositiveButton(getString(R.string.ok_button));
    }
}

