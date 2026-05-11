package org.main.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.main.entities.Doctor;
import org.main.entities.SPECIALIZATION;
import org.main.repository.DoctorRepository;
import org.main.repository.RecordRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    public void getAllDoctors_returnsRepositoryResult() {
        Doctor d1 = new Doctor();
        Doctor d2 = new Doctor();
        when(doctorRepository.findAll()).thenReturn(Arrays.asList(d1, d2));

        List<Doctor> all = doctorService.getAllDoctors();

        assertEquals(2, all.size());
        assertSame(d1, all.get(0));
        verify(doctorRepository).findAll();
    }

    @Test
    public void getDoctorById_returnsDoctorWhenPresent() {
        Doctor doctor = new Doctor();
        doctor.setId(5L);
        doctor.setFirstName("Ann");
        doctor.setLastName("Smith");
        doctor.setSpecialization(SPECIALIZATION.OCULIST);
        when(doctorRepository.findById(5L)).thenReturn(Optional.of(doctor));

        Doctor found = doctorService.getDoctorById(5L);

        assertSame(doctor, found);
    }

    @Test
    public void deleteDoctor_whenNoAppointments_deletes() {
        when(recordRepository.countByDoctor_Id(3L)).thenReturn(0L);

        doctorService.deleteDoctor(3L);

        verify(doctorRepository).deleteById(3L);
    }

    @Test(expected = IllegalStateException.class)
    public void deleteDoctor_whenHasAppointments_throws() {
        when(recordRepository.countByDoctor_Id(3L)).thenReturn(2L);

        doctorService.deleteDoctor(3L);
    }

    @Test
    public void saveDoctor_delegatesToRepository() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("A");
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor saved = doctorService.saveDoctor(doctor);

        assertSame(doctor, saved);
        verify(doctorRepository).save(doctor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDoctorById_throwsWhenMissing() {
        when(doctorRepository.findById(404L)).thenReturn(Optional.empty());

        doctorService.getDoctorById(404L);
    }
}
