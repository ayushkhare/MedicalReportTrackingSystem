package sample.com.medicalrecordtrackingsystem.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sample.com.medicalrecordtrackingsystem.models.Department;
import sample.com.medicalrecordtrackingsystem.models.Doctor;
import sample.com.medicalrecordtrackingsystem.models.Hospital;
import sample.com.medicalrecordtrackingsystem.models.User;

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

    @POST("patients/signin")
    Call<User> signInUser(@Query("username") String username, @Query("password") String password);

    @GET("patients/{id}")
    Call<User> getUserDetails(@Path("id") int id);

}
