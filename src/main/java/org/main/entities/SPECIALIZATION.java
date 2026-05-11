package org.main.entities;

public enum SPECIALIZATION {
    OCULIST("Окулист"),
    SURGEON("Хирург"),
    DENTIST("Стоматолог"),
    CARDIOLOGIST("Кардиолог"),
    THERAPIST("Терапевт");

    private final String title;

    SPECIALIZATION(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
