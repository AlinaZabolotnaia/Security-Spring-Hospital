package org.main.services;

import org.main.entities.Record;
import org.main.entities.Role;
import org.main.entities.User;
import org.main.repository.RecordRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class RecordService {

    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    public List<Record> getRecordsByUserId(Long id) {
        return recordRepository.findAllByUserId(id);
    }

    public void deleteById(Long id) {
        recordRepository.deleteById(id);
    }

    public void deleteByIdForUser(Long recordId, User user) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));
        boolean admin = user.getRoles().stream().anyMatch(r -> r == Role.ADMIN);
        if (!admin && (record.getUser() == null || !record.getUser().getId().equals(user.getId()))) {
            throw new AccessDeniedException("Cannot delete this record");
        }
        recordRepository.deleteById(recordId);
    }

    public List<Record> getRecordsByDoctorId(Long id) {
        return recordRepository.findAllByDoctorId(id);
    }

    /**
     * У выбранного врача на эту дату время попадает в чужой слот: ±15 минут от существующего приёма (любого пациента).
     * {@code excludeRecordId} — запись, которую при редактировании не учитывать (та же запись).
     */
    public boolean isDoctorSlotBusy(Long doctorId, LocalDate date, LocalTime requestedTime, Long excludeRecordId) {
        for (Record existing : recordRepository.findAllByDoctorId(doctorId)) {
            if (excludeRecordId != null && excludeRecordId.equals(existing.getId())) {
                continue;
            }
            if (!existing.getDate().equals(date)) {
                continue;
            }
            LocalTime windowEnd = existing.getTime().plusMinutes(15);
            LocalTime windowStart = existing.getTime().minusMinutes(15);
            if (requestedTime.isAfter(windowStart) && requestedTime.isBefore(windowEnd)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDoctorSlotBusy(Long doctorId, LocalDate date, LocalTime requestedTime) {
        return isDoctorSlotBusy(doctorId, date, requestedTime, null);
    }

    /** true, если дата и время приёма уже в прошлом относительно текущего момента */
    public boolean isBookingInPast(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time).isBefore(LocalDateTime.now());
    }

    public Record saveRecord(Record record) {
        return recordRepository.save(record);
    }

    public Record getRecordById(Long id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));
    }
}
