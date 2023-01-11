package model;

import java.util.Objects;

public class Specialty {
    private Integer id;
    private String name;
    private Status status;
    public Specialty() {
    }
    public Specialty(Integer id) {
        this.id = id;
    }
    public Specialty(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(o instanceof Specialty specialty)) return false;
        return Objects.equals(getId(), specialty.getId()) && Objects.equals(getName(), specialty.getName()) && getStatus() == specialty.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getStatus());
    }

    @Override
    public String toString() {
        return "Specialty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
