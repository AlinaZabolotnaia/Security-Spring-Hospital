package org.main.controllers;

import lombok.RequiredArgsConstructor;
import org.main.entities.Doctor;
import org.main.entities.Record;
import org.main.entities.Role;
import org.main.entities.SPECIALIZATION;
import org.main.entities.User;
import org.main.repository.UserRepo;
import org.main.services.DoctorService;
import org.main.services.RecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final RecordService recordService;
    private final DoctorService doctorService;
    private final UserRepo userRepo;

    @GetMapping({"", "/"})
    public String adminPanel(
            @RequestParam(value = "loginSuccess", required = false) String loginSuccess,
            Model model
    ) {
        if (loginSuccess != null) {
            model.addAttribute("loginSuccess", true);
        }
        model.addAttribute("adminSection", "records");
        model.addAttribute("records", recordService.getAllRecords());
        return "admin";
    }

    @GetMapping("/doctors")
    public String doctors(Model model) {
        model.addAttribute("adminSection", "doctors");
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("specializations", Arrays.asList(SPECIALIZATION.values()));
        return "admin_doctors";
    }

    @PostMapping("/doctors")
    public String createDoctor(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam SPECIALIZATION specialization,
            RedirectAttributes ra
    ) {
        Doctor d = new Doctor();
        d.setFirstName(firstName.trim());
        d.setLastName(lastName.trim());
        d.setSpecialization(specialization);
        doctorService.saveDoctor(d);
        ra.addFlashAttribute("doctorSaved", true);
        return "redirect:/admin/doctors";
    }

    @GetMapping("/doctors/{id}/edit")
    public String editDoctorForm(@PathVariable Long id, Model model) {
        model.addAttribute("adminSection", "doctors");
        model.addAttribute("doctor", doctorService.getDoctorById(id));
        model.addAttribute("specializations", Arrays.asList(SPECIALIZATION.values()));
        return "admin_doctor_edit";
    }

    @PostMapping("/doctors/{id}/edit")
    public String editDoctor(
            @PathVariable Long id,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam SPECIALIZATION specialization,
            RedirectAttributes ra
    ) {
        Doctor d = doctorService.getDoctorById(id);
        d.setFirstName(firstName.trim());
        d.setLastName(lastName.trim());
        d.setSpecialization(specialization);
        doctorService.saveDoctor(d);
        ra.addFlashAttribute("doctorSaved", true);
        return "redirect:/admin/doctors";
    }

    @PostMapping("/doctors/{id}/delete")
    public String deleteDoctor(@PathVariable Long id, RedirectAttributes ra) {
        try {
            doctorService.deleteDoctor(id);
            ra.addFlashAttribute("doctorDeleted", true);
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("doctorDeleteError", e.getMessage());
        }
        return "redirect:/admin/doctors";
    }

    @GetMapping("/records/{id}/edit")
    public String editRecordForm(@PathVariable Long id, Model model) {
        Record record = recordService.getRecordById(id);
        model.addAttribute("adminSection", "records");
        model.addAttribute("record", record);
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientsOnly());
        return "admin_record_edit";
    }

    @PostMapping("/records/{id}/edit")
    public String editRecord(
            @PathVariable Long id,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam String problem,
            @RequestParam Long doctorId,
            @RequestParam Long userId,
            Model model,
            RedirectAttributes ra
    ) {
        LocalDate localDate = LocalDate.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)), Integer.parseInt(date.substring(8, 10)));
        LocalTime localTime = LocalTime.of(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)));

        if (recordService.isDoctorSlotBusy(doctorId, localDate, localTime, id)) {
            model.addAttribute("slotConflict", true);
            model.addAttribute("record", recordService.getRecordById(id));
            model.addAttribute("adminSection", "records");
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("patients", patientsOnly());
            return "admin_record_edit";
        }

        User patient = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Record record = recordService.getRecordById(id);
        record.setUser(patient);
        record.setDoctor(doctorService.getDoctorById(doctorId));
        record.setDate(localDate);
        record.setTime(localTime);
        record.setProblem(problem.trim());
        recordService.saveRecord(record);
        ra.addFlashAttribute("recordUpdated", true);
        return "redirect:/admin";
    }

    @PostMapping("/records/{id}/delete")
    public String deleteRecord(@PathVariable Long id, RedirectAttributes ra) {
        recordService.deleteById(id);
        ra.addFlashAttribute("recordDeleted", true);
        return "redirect:/admin";
    }

    private List<User> patientsOnly() {
        return userRepo.findAll().stream()
                .filter(u -> u.getRoles() != null && u.getRoles().contains(Role.USER))
                .collect(Collectors.toList());
    }
}
