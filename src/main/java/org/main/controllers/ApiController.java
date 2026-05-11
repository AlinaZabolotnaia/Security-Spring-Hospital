package org.main.controllers;

import lombok.RequiredArgsConstructor;
import org.main.entities.Doctor;
import org.main.entities.Record;
import org.main.entities.Role;
import org.main.entities.User;
import org.main.services.DoctorService;
import org.main.services.RecordService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final DoctorService doctorService;
    private final RecordService recordService;

    @GetMapping("/doctors")
    public List<Doctor> listDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/me")
    public Map<String, Object> currentUser(@AuthenticationPrincipal User user) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("username", user.getUsername());
        body.put("roles", user.getRoles().stream().map(Role::name).collect(Collectors.toList()));
        body.put("active", user.isActive());
        return body;
    }

    @GetMapping("/records/my")
    public List<Map<String, Object>> myRecords(@AuthenticationPrincipal User user) {
        return recordService.getRecordsByUserId(user.getId()).stream()
                .map(this::toRecordSummary)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/records")
    public List<Map<String, Object>> allRecordsAdmin() {
        return recordService.getAllRecords().stream()
                .map(this::toRecordSummary)
                .collect(Collectors.toList());
    }

    private Map<String, Object> toRecordSummary(Record r) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", r.getId());
        m.put("date", r.getDate());
        m.put("time", r.getTime());
        m.put("problem", r.getProblem());
        m.put("patient", r.getUser() != null ? r.getUser().getUsername() : null);
        m.put("doctor", r.getDoctor() != null ? r.getDoctor().toString() : null);
        return m;
    }
}
