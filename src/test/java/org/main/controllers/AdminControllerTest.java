package org.main.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.main.entities.Record;
import org.main.entities.SPECIALIZATION;
import org.main.repository.UserRepo;
import org.main.services.DoctorService;
import org.main.services.RecordService;
import org.springframework.ui.ExtendedModelMap;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

    @Mock
    private RecordService recordService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private AdminController adminController;

    @Test
    public void adminPanel_putsAllRecordsIntoModel() {
        Record r = new Record();
        when(recordService.getAllRecords()).thenReturn(Collections.singletonList(r));

        ExtendedModelMap model = new ExtendedModelMap();
        String view = adminController.adminPanel(null, model);

        assertEquals("admin", view);
        assertEquals("records", model.get("adminSection"));
        assertEquals(Collections.singletonList(r), model.get("records"));
        verify(recordService).getAllRecords();
    }

    @Test
    public void doctorsPage_putsSectionAndLoadsDoctors() {
        when(doctorService.getAllDoctors()).thenReturn(Collections.emptyList());

        ExtendedModelMap model = new ExtendedModelMap();
        String view = adminController.doctors(model);

        assertEquals("admin_doctors", view);
        assertEquals("doctors", model.get("adminSection"));
        assertEquals(Collections.emptyList(), model.get("doctors"));
        assertEquals(Arrays.asList(SPECIALIZATION.values()), model.get("specializations"));
        verify(doctorService).getAllDoctors();
    }
}
