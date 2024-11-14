package ikonek.services;

import ikonek.dao.HospitalDao;
import ikonek.models.Hospital;
import ikonek.utils.InputValidator;

import java.util.List;

public class HospitalService {

    private final HospitalDao hospitalDao;

    public HospitalService(HospitalDao hospitalDao) {
        this.hospitalDao = hospitalDao;
    }

    public int createHospital(String name, String city, String province, String contactNumber) {
        if (!InputValidator.isValidName(name) || !InputValidator.isValidName(city) ||
                !InputValidator.isValidName(province) || !InputValidator.isValidContactNumber(contactNumber)) {
            System.err.println("Invalid input. Please check your details.");
            return -1;
        }
        Hospital newHospital = new Hospital(name, city, province, contactNumber);
        return hospitalDao.createHospital(newHospital);
    }

    public Hospital getHospitalById(int hospitalId) {
        return hospitalDao.getHospitalById(hospitalId);
    }

    public boolean updateHospital(Hospital hospital) {
        return hospitalDao.updateHospital(hospital);
    }

    public List<Hospital> getAllHospitals() {
        return hospitalDao.getAllHospitals();
    }

    public boolean deleteHospital(int hospitalId) {
        return hospitalDao.deleteHospital(hospitalId);
    }
}