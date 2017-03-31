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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.com.medicalrecordtrackingsystem.event.ResetOtherRecyclerViewEvent;
import sample.com.medicalrecordtrackingsystem.models.Doctor;
import sample.com.medicalrecordtrackingsystem.models.Slots;
import sample.com.medicalrecordtrackingsystem.rest.ApiClient;
import sample.com.medicalrecordtrackingsystem.rest.ApiInterface;
import sample.com.medicalrecordtrackingsystem.utility.CircleTransform;

/**
 * Created by ayush on 31/3/17
 */
public class DoctorDetailsActivity extends AppCompatActivity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.details)
    TextView details;
    @Bind(R.id.dept)
    TextView dept;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.exp)
    TextView experience;

    private GenericAdapter mAdapter;
    private ApiInterface apiService;
    private int doctorId;
    private List<Items> itemsList;

    @Subscribe
    public void onReceieveRestEvent(ResetOtherRecyclerViewEvent event) {
        mAdapter.notifyItemChanged(event.getPosition() - 1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_details_layout);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        doctorId = bundle.getInt("id");
        setupToolbar();
        getApiClient();
        getDoctor(doctorId);
        setupSwipeToRefresh();
        setupRecyclerView();
        responseConverter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void setupSwipeToRefresh() {
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemsList.clear();
                mAdapter.notifyDataSetChanged();
                responseConverter();
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void getDoctor(int doctorId) {
        Call<Doctor> call = apiService.getDoctorFromId(doctorId);
        call.enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        Doctor doctor = response.body();
                        updateUI(doctor);
                    }else {
                        Toast.makeText(DoctorDetailsActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(DoctorDetailsActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(Doctor doctor) {
        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .crossFade()
                .transform(new CircleTransform(this))
                .into(image);
        name.setText(doctor.getName());
        dept.setText(doctor.getDept());
        details.setText(doctor.getDetails());
        phone.setText(String.valueOf(doctor.getPhone()));
        experience.setText(String.valueOf(doctor.getYearsOfExp()));
    }

    private void responseConverter() {
        Call<List<Slots>> call = apiService.getSlots(doctorId);
        call.enqueue(new Callback<List<Slots>>() {
            @Override
            public void onResponse(Call<List<Slots>> call, Response<List<Slots>> response) {
                refreshLayout.setRefreshing(false);
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Slots> slotsList = response.body();
                        for (Slots slot : slotsList) {
                            final Items items = new Items();
                            items.slot = slot;
                            items.type = Items.TYPE_SLOTS;
                            itemsList.add(items);
                            DoctorDetailsActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.setData(itemsList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(DoctorDetailsActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Slots>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                t.printStackTrace();
                Toast.makeText(DoctorDetailsActivity.this, getString(R.string.api_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getApiClient() {
        apiService = ApiClient.getCient().create(ApiInterface.class);
    }

    private void setupRecyclerView() {
        if (itemsList == null) {
            itemsList = new ArrayList<>();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new GenericAdapter(DoctorDetailsActivity.this, itemsList);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);
    }
}
