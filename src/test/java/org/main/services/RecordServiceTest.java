package org.main.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.main.entities.Doctor;
import org.main.entities.Record;
import org.main.entities.Role;
import org.main.entities.User;
import org.main.repository.RecordRepository;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecordServiceTest {

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordService recordService;

    private Record record;
    private User owner;
    private User admin;
    private User otherUser;

    @Before
    public void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setUsername("patient");
        owner.setRoles(new HashSet<>(Collections.singletonList(Role.USER)));

        admin = new User();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setRoles(new HashSet<>(Collections.singletonList(Role.ADMIN)));

        otherUser = new User();
        otherUser.setId(3L);
        otherUser.setUsername("other");
        otherUser.setRoles(new HashSet<>(Collections.singletonList(Role.USER)));

        Doctor doctor = new Doctor();
        doctor.setId(10L);

        record = new Record();
        record.setId(100L);
        record.setUser(owner);
        record.setDoctor(doctor);
    }

    @Test
    public void deleteByIdForUser_ownerCanDeleteOwnRecord() {
        when(recordRepository.findById(100L)).thenReturn(Optional.of(record));

        recordService.deleteByIdForUser(100L, owner);

        verify(recordRepository).deleteById(100L);
    }

    @Test
    public void deleteByIdForUser_adminCanDeleteAnyRecord() {
        when(recordRepository.findById(100L)).thenReturn(Optional.of(record));

        recordService.deleteByIdForUser(100L, admin);

        verify(recordRepository).deleteById(100L);
    }

    @Test(expected = AccessDeniedException.class)
    public void deleteByIdForUser_otherUserDenied() {
        when(recordRepository.findById(100L)).thenReturn(Optional.of(record));

        recordService.deleteByIdForUser(100L, otherUser);
    }

    @Test
    public void saveRecord_delegatesToRepository() {
        when(recordRepository.save(record)).thenReturn(record);

        Record saved = recordService.saveRecord(record);

        assertEquals(record, saved);
        verify(recordRepository).save(record);
    }

    @Test
    public void getRecordsByUserId_delegatesToRepository() {
        recordService.getRecordsByUserId(1L);
        verify(recordRepository).findAllByUserId(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteByIdForUser_whenRecordMissing_throws() {
        when(recordRepository.findById(999L)).thenReturn(Optional.empty());

        recordService.deleteByIdForUser(999L, owner);
    }

    @Test
    public void isDoctorSlotBusy_detectsOverlapWithAnyPatientAtSameDoctor() {
        Record existing = new Record();
        existing.setDate(LocalDate.of(2026, 5, 1));
        existing.setTime(LocalTime.of(10, 0));
        when(recordRepository.findAllByDoctorId(10L)).thenReturn(Collections.singletonList(existing));

        assertTrue(recordService.isDoctorSlotBusy(10L, LocalDate.of(2026, 5, 1), LocalTime.of(10, 0)));
        assertTrue(recordService.isDoctorSlotBusy(10L, LocalDate.of(2026, 5, 1), LocalTime.of(10, 14)));
    }

    @Test
    public void isDoctorSlotBusy_falseWhenOutsideWindowOrDifferentDay() {
        Record existing = new Record();
        existing.setDate(LocalDate.of(2026, 5, 1));
        existing.setTime(LocalTime.of(10, 0));
        when(recordRepository.findAllByDoctorId(10L)).thenReturn(Collections.singletonList(existing));

        assertFalse(recordService.isDoctorSlotBusy(10L, LocalDate.of(2026, 5, 2), LocalTime.of(10, 0)));
        assertFalse(recordService.isDoctorSlotBusy(10L, LocalDate.of(2026, 5, 1), LocalTime.of(11, 0)));
    }

    @Test
    public void isDoctorSlotBusy_skipsExcludedRecordWhenEditingSameSlot() {
        Record existing = new Record();
        existing.setId(50L);
        existing.setDate(LocalDate.of(2026, 5, 1));
        existing.setTime(LocalTime.of(10, 0));
        when(recordRepository.findAllByDoctorId(10L)).thenReturn(Collections.singletonList(existing));

        assertFalse(recordService.isDoctorSlotBusy(10L, LocalDate.of(2026, 5, 1), LocalTime.of(10, 0), 50L));
    }

    @Test
    public void getRecordById_returnsWhenPresent() {
        when(recordRepository.findById(100L)).thenReturn(Optional.of(record));

        assertSame(record, recordService.getRecordById(100L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRecordById_throwsWhenMissing() {
        when(recordRepository.findById(404L)).thenReturn(Optional.empty());

        recordService.getRecordById(404L);
    }

    @Test
    public void deleteById_delegatesToRepository() {
        recordService.deleteById(7L);

        verify(recordRepository).deleteById(7L);
    }

    @Test
    public void getAllRecords_delegatesToRepository() {
        when(recordRepository.findAll()).thenReturn(Collections.singletonList(record));

        assertEquals(1, recordService.getAllRecords().size());
        verify(recordRepository).findAll();
    }

    @Test
    public void getRecordsByDoctorId_delegatesToRepository() {
        recordService.getRecordsByDoctorId(10L);

        verify(recordRepository).findAllByDoctorId(10L);
    }

    @Test
    public void isBookingInPast_trueForYesterday() {
        assertTrue(recordService.isBookingInPast(LocalDate.now().minusDays(1), LocalTime.NOON));
    }

    @Test
    public void isBookingInPast_falseForDateFarInFuture() {
        assertFalse(recordService.isBookingInPast(LocalDate.now().plusYears(10), LocalTime.MIDNIGHT));
    }
}
