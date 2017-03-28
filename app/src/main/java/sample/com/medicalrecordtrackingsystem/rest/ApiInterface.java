package sample.com.medicalrecordtrackingsystem.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sample.com.medicalrecordtrackingsystem.models.Department;
import sample.com.medicalrecordtrackingsystem.models.Doctor;
import sample.com.medicalrecordtrackingsystem.models.Hospital;

/**
 * Created by ayush on 27/3/17
 */
public interface ApiInterface {

    @GET("hospitals")
    Call<List<Hospital>> getHospitals();

    @GET("doctors")
    Call<List<Doctor>> getDoctors();

    @GET("doctors")
    Call<List<Doctor>> getDoctorBasedOnHospital(@Query("hospitalId") String hospitalId);

    @GET("doctors/departments")
    Call<List<Department>> getDepartments();
}
