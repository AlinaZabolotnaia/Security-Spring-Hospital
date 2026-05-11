package org.main.controllers;

import lombok.RequiredArgsConstructor;
import org.main.entities.Record;
import org.main.entities.User;
import org.main.services.DoctorService;
import org.main.services.RecordService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final DoctorService doctorService;
    private final RecordService recordService;

    private void fillBookingFormModel(User user, Map<String, Object> model) {
        model.put("records", recordService.getRecordsByUserId(user.getId()));
        model.put("doctors", doctorService.getAllDoctors());
        model.put("minBookingDate", LocalDate.now().toString());
    }

    @GetMapping("/")
    public String greeting(
            @RequestParam(value = "loginSuccess", required = false) String loginSuccess,
            Map<String, Object> model
    ) {
        if (loginSuccess != null) {
            model.put("loginSuccess", true);
        }
        return "greeting";
    }

    @GetMapping("/main")
    public String recordList(
            @RequestParam(value = "loginSuccess", required = false) String loginSuccess,
            Model model
    ) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (loginSuccess != null) {
            model.addAttribute("loginSuccess", true);
        }
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("records", recordService.getRecordsByUserId(user.getId()));
        model.addAttribute("minBookingDate", LocalDate.now().toString());
        return "main";
    }

    @GetMapping("/record/{id}")
    public String delete(@AuthenticationPrincipal User user, @PathVariable Long id,
                         Map<String, Object> model) {
        recordService.deleteByIdForUser(id, user);
        model.put("records", recordService.getRecordsByUserId(user.getId()));
        model.put("doctors", doctorService.getAllDoctors());
        return "redirect:/main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam String problem,
            @RequestParam Long doctorId,
            Map<String, Object> model
    ) {
        LocalDate localDate = LocalDate.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)), Integer.parseInt(date.substring(8, 10)));
        LocalTime localTime = LocalTime.of(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)));

        if (recordService.isBookingInPast(localDate, localTime)) {
            model.put("pastBooking", true);
            fillBookingFormModel(user, model);
            return "mainRecover";
        }

        if (recordService.isDoctorSlotBusy(doctorId, localDate, localTime)) {
            model.put("slotConflict", true);
            fillBookingFormModel(user, model);
            return "mainRecover";
        }

        Record record = new Record();
        record.setUser(user);
        record.setDoctor(doctorService.getDoctorById(doctorId));
        record.setTime(localTime);
        record.setDate(localDate);
        record.setProblem(problem);

        recordService.saveRecord(record);

        model.put("records", recordService.getRecordsByUserId(user.getId()));
        model.put("doctors", doctorService.getAllDoctors());
        model.put("minBookingDate", LocalDate.now().toString());
        return "main";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()))) {
            return "redirect:/admin";
        }
        return "redirect:/main";
    }

}
