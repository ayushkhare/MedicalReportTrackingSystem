package sample.com.medicalrecordtrackingsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.com.medicalrecordtrackingsystem.models.Doctor;
import sample.com.medicalrecordtrackingsystem.models.Hospital;
import sample.com.medicalrecordtrackingsystem.rest.ApiClient;
import sample.com.medicalrecordtrackingsystem.rest.ApiInterface;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.hospital_spinner)
    SearchableSpinner hospitalSpinner;
    @Bind(R.id.doctor_spinner)
    SearchableSpinner doctorSpinner;

    private ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getApiClient();
        getHospitalsFromApi();
        getDoctorsFromApi();
    }

    private void getApiClient() {
        apiService = ApiClient.getCient().create(ApiInterface.class);
    }

    /**
     * Function to get all the hospitals
     */
    private void getHospitalsFromApi() {
        Call<List<Hospital>> call = apiService.getHospitals();
        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Hospital> hospitalList = response.body();
                        populateHospitalList(hospitalList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
                Toast.makeText(MainActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    /**
     * Function to populate hospital list into spinner
     * @param hospitalList
     */
    private void populateHospitalList(List<Hospital> hospitalList) {
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

    /**
     * Function to get all the doctors
     */
    private void getDoctorsFromApi() {
        Call<List<Doctor>> call = apiService.getDoctors();
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Doctor> doctorList = response.body();
                        populateDoctorList(doctorList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Toast.makeText(MainActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void populateDoctorList(List<Doctor> doctorList) {
        List<String> doctorNames = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            doctorNames.add(doctor.getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, doctorNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpinner.setAdapter(spinnerAdapter);
        doctorSpinner.setTitle(getString(R.string.select_doctor));
        doctorSpinner.setPositiveButton(getString(R.string.ok_button));
    }
}

