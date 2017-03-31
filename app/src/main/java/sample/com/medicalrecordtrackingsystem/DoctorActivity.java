package sample.com.medicalrecordtrackingsystem;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.com.medicalrecordtrackingsystem.models.Doctor;
import sample.com.medicalrecordtrackingsystem.rest.ApiClient;
import sample.com.medicalrecordtrackingsystem.rest.ApiInterface;

/**
 * Created by ayush on 28/3/17
 */
public class DoctorActivity extends AppCompatActivity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.my_toolbar)
    Toolbar toolbar;

    private ApiInterface apiService;
    private List<Doctor> doctorList;
    private DoctorAdapter mAdapter;
    private int hospitalId;
    private String departmentValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_layout);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        hospitalId = bundle.getInt("hospital_id");
        departmentValue = bundle.getString("department_value");
        getApiClient();
        if (hospitalId != -1 && !departmentValue.equals(getString(R.string.select_one))) {
            getDoctors(hospitalId, departmentValue);
        } else {
            if (hospitalId != -1) {
                getDoctorsBasedOnHospitals(hospitalId);
            } else if (!departmentValue.equals(getString(R.string.select_one))) {
                getDoctorsBasedOnDepartment(departmentValue);
            }
        }

        setupToolbar();
        setupRecyclerView();
        setupSwipeToRefresh();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setTitle(getString(R.string.select_doctor));
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupRecyclerView() {
        if (doctorList == null) {
            doctorList = new ArrayList<>();
        }

        mAdapter = new DoctorAdapter(DoctorActivity.this, doctorList);
        recyclerView.hasFixedSize();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
    }

    private void setupSwipeToRefresh() {
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (hospitalId != -1 && departmentValue != null) {
                    getDoctors(hospitalId, departmentValue);
                } else if (hospitalId != -1) {
                    getDoctorsBasedOnHospitals(hospitalId);
                } else if (departmentValue != null) {
                    getDoctorsBasedOnDepartment(departmentValue);
                }
            }
        });
    }

    private void getDoctorsBasedOnDepartment(String departmentValue) {
        Call<List<Doctor>> call = apiService.getDoctorsBasedOnDepartment(departmentValue);
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                refreshLayout.setRefreshing(false);
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        doctorList = response.body();
                        mAdapter.setData(doctorList);
                    } else {
                        Toast.makeText(DoctorActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                t.printStackTrace();
                Toast.makeText(DoctorActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDoctorsBasedOnHospitals(int hospitalId) {
        Call<List<Doctor>> call = apiService.getDoctorsBasedOnHospital(hospitalId);
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                refreshLayout.setRefreshing(false);
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        doctorList = response.body();
                        mAdapter.setData(doctorList);
                    } else {
                        Toast.makeText(DoctorActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                t.printStackTrace();
                Toast.makeText(DoctorActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDoctors(int hospitalId, String departmentValue) {
        Call<List<Doctor>> call = apiService.getDoctors(hospitalId, departmentValue);
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                refreshLayout.setRefreshing(false);
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        doctorList = response.body();
                        mAdapter.setData(doctorList);
                    } else {
                        Toast.makeText(DoctorActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                t.printStackTrace();
                Toast.makeText(DoctorActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getApiClient() {
        apiService = ApiClient.getCient().create(ApiInterface.class);
    }

}
