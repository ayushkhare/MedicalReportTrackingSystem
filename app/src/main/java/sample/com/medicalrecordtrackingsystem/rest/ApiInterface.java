package sample.com.medicalrecordtrackingsystem.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import sample.com.medicalrecordtrackingsystem.models.Hospital;

/**
 * Created by ayush on 27/3/17
 */
public interface ApiInterface {

    @GET("professionals/discovery")
    Call<Hospital> getHospitals();

}
