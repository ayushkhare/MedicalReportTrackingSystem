package sample.com.medicalrecordtrackingsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.com.medicalrecordtrackingsystem.models.Hospital;
import sample.com.medicalrecordtrackingsystem.rest.ApiClient;
import sample.com.medicalrecordtrackingsystem.rest.ApiInterface;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDataFromApi();
    }

    private void getDataFromApi() {
        ApiInterface apiService = ApiClient.getCient().create(ApiInterface.class);
        Call<Hospital> call = apiService.getHospitals();
        call.enqueue(new Callback<Hospital>() {
            @Override
            public void onResponse(Call<Hospital> call, Response<Hospital> response) {
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Hospital> hospitalList = response.body().getHospitalList();
                        //Use this list to extract other fields
                        displayHospitalData(hospitalList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Hospital> call, Throwable t) {
                //Handle api call failure message
            }
        });
    }

    private void displayHospitalData(List<Hospital> hospitalList) {
        for (Hospital hospital : hospitalList) {
            String name = hospital.getName();
            String address = hospital.getAddress();
        }
    }
}
