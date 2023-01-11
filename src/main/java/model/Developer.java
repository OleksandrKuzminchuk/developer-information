package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static util.Constants.TWO_BILLION;

public class Developer {
    private Integer id;
    private String firstName;
    private String lastName;
    private List<Skill> skills;
    private Specialty specialty;
    private Status status;
    public Developer() {
    }

    public Developer(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.skills = new ArrayList<>();
        this.specialty = new Specialty(TWO_BILLION);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Developer developer)) return false;
        return Objects.equals(getId(), developer.getId()) && Objects.equals(getFirstName(), developer.getFirstName()) && Objects.equals(getLastName(), developer.getLastName()) && Objects.equals(getSkills(), developer.getSkills()) && Objects.equals(getSpecialty(), developer.getSpecialty()) && getStatus() == developer.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getSkills(), getSpecialty(), getStatus());
    }

    @Override
    public String toString() {
        return "Developer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", skills=" + skills +
                ", specialty=" + specialty +
                ", status=" + status +
                '}';
    }
}
