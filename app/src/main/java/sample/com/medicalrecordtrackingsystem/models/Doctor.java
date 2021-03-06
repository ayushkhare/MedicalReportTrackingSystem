package sample.com.medicalrecordtrackingsystem.models;

import java.util.List;

/**
 * Created by ayush on 28/3/17
 */
public class Doctor {

    private List<Doctor> doctorList;
    private String dept;
    private String details;
    private String hospitalId;
    private int id;
    private String name;
    private int phone;
    private int yearsOfExp;

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getYearsOfExp() {
        return yearsOfExp;
    }

    public void setYearsOfExp(int yearsOfExp) {
        this.yearsOfExp = yearsOfExp;
    }

}
