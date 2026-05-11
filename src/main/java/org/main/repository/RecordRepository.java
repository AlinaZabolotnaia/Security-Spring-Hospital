package org.main.repository;

import org.main.entities.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record,Long> {
    List<Record> findAllByUserId(Long userId);
    List<Record> findAllByDoctorId(Long doctorId);
    long countByDoctor_Id(Long doctorId);
}
