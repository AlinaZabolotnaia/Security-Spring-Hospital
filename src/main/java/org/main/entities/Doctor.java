package org.main.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "doctor")
public class Doctor {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false)
    private SPECIALIZATION specialization;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Doctor doctor = (Doctor) o;
        return id != null && Objects.equals(id, doctor.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        String spec = specialization != null ? specialization.getTitle() : "";
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")
                + (spec.isEmpty() ? "" : " (" + spec + ")");
    }
}
