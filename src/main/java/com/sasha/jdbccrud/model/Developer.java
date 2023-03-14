package com.sasha.jdbccrud.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Developer {
    private Integer id;
    private String firstName;
    private String lastName;
    private List<Skill> skills;
    private Specialty specialty;
    private Status status;

    public Developer() {
    }

    public Developer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Developer(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Developer(Integer id, String firstName, String lastName, Specialty specialty) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
    }

    public Developer(Integer id, String firstName, String lastName, List<Skill> skills, Specialty specialty) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.skills = skills;
        this.specialty = specialty;
    }

    public Developer(Integer id, String firstName, String lastName, Status status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
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
    public void addSkillToDeveloper(Skill skill){
        if (skills == null){
            skills = new LinkedList<>();
            skills.add(skill);
        }else {
            skills.add(skill);
        }
    }
}
