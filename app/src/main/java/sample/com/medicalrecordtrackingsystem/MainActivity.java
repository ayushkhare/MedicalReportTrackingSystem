package sample.com.medicalrecordtrackingsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.com.medicalrecordtrackingsystem.adapter.DrawerAdapter;
import sample.com.medicalrecordtrackingsystem.models.Department;
import sample.com.medicalrecordtrackingsystem.models.Hospital;
import sample.com.medicalrecordtrackingsystem.models.User;
import sample.com.medicalrecordtrackingsystem.rest.ApiClient;
import sample.com.medicalrecordtrackingsystem.rest.ApiInterface;
import sample.com.medicalrecordtrackingsystem.utility.ItemClickSupport;

public class MainActivity extends AppCompatActivity {

    private static final String DUMMY_ID = "-1";
    private String navigationTitles[] = {"Book Appointment", "Appointments", "Logout"};
    String headerName = "";

    @Bind(R.id.hospital_spinner)
    SearchableSpinner hospitalSpinner;
    @Bind(R.id.department_spinner)
    SearchableSpinner departmentSpinner;
    @Bind(R.id.deparment_text_view)
    TextView departmentTextView;
    @Bind(R.id.proceed_btn)
    Button proceedButton;
    @Bind(R.id.drawer_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.my_toolbar)
    Toolbar toolbar;

    private ApiInterface apiService;
    private String selectedHospitalid;
    private String selectedDepartmentValue;
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        int userId = bundle.getInt("userId");
        getApiClient();
        getUserDetails(userId);
        getHospitalsFromApi();
        getDepartmentsFromApi();
        proceedButton.setEnabled(false);
    }

    private void getApiClient() {
        apiService = ApiClient.getCient().create(ApiInterface.class);
    }

    private void getUserDetails(int userId) {
        Call<User> call= apiService.getUserDetails(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        headerName = user.getUsername();
                        setupDrawer();
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDrawer() {
        setSupportActionBar(toolbar);
        recyclerView.setHasFixedSize(true);
        mAdapter = new DrawerAdapter(navigationTitles, headerName);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                switch (position) {
                    case 3:
                        /**
                         * Logout event
                         */
                        SharedPreferences sharedPreference = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreference.edit().clear();
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                }
            }
        });
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
                        Hospital dummyHospital = new Hospital();
                        dummyHospital.setName(getString(R.string.select_one));
                        dummyHospital.setId(DUMMY_ID);
                        hospitalList.add(0, dummyHospital);
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
     *
     * @param hospitalList
     */
    private void populateHospitalList(final List<Hospital> hospitalList) {
        List<String> hospitalNames = new ArrayList<>();
        for (Hospital hospital : hospitalList) {
            hospitalNames.add(hospital.getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hospitalNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitalSpinner.setAdapter(spinnerAdapter);
        hospitalSpinner.setTitle(getString(R.string.select_hospital));
        hospitalSpinner.setPositiveButton(getString(R.string.ok_button));

        hospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedHospitalid = hospitalList.get(position).getId();
                setButtonState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Function to get all the departments
     */
    private void getDepartmentsFromApi() {
        Call<List<Department>> call = apiService.getDepartments();
        call.enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Department> departmentList = response.body();
                        Department dummyDepartment = new Department();
                        dummyDepartment.setValue(getString(R.string.select_one));
                        departmentList.add(0, dummyDepartment);
                        populateDepartmentList(departmentList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {

            }
        });
    }

    /**
     * Function to populate department list into spinner
     * @param departmentList
     */
    private void populateDepartmentList(final List<Department> departmentList) {
        List<String> departmentNames = new ArrayList<>();
        for (Department department : departmentList) {
            departmentNames.add(department.getValue());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(spinnerAdapter);
        departmentSpinner.setTitle(getString(R.string.select_department));
        departmentSpinner.setPrompt(getString(R.string.select_department));
        departmentSpinner.setPositiveButton(getString(R.string.ok_button));

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedDepartmentValue = departmentList.get(position).getValue();
                setButtonState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setButtonState() {
        if (DUMMY_ID.equals(selectedHospitalid) && getString(R.string.select_one).equals(selectedDepartmentValue)) {
            proceedButton.setEnabled(false);
        } else {
            proceedButton.setEnabled(true);
        }
    }

    @OnClick(R.id.proceed_btn)
    public void onClickProceed() {
        Intent intent = new Intent(MainActivity.this, DoctorDetailsActivity.class);
        intent.putExtra("hospital_id", selectedHospitalid);
        intent.putExtra("department_value", selectedDepartmentValue);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

