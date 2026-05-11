package org.main.services;

import lombok.RequiredArgsConstructor;
import org.main.entities.Doctor;
import org.main.repository.DoctorRepository;
import org.main.repository.RecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final RecordRepository recordRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    /**
     * Удалить врача, только если нет записей на приём.
     */
    @Transactional
    public void deleteDoctor(Long doctorId) {
        if (recordRepository.countByDoctor_Id(doctorId) > 0) {
            throw new IllegalStateException("У врача есть записи пациентов, удаление невозможно");
        }
        doctorRepository.deleteById(doctorId);
    }
}
