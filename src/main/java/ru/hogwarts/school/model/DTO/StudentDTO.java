package ru.hogwarts.school.model.DTO;

import java.util.Objects;


public class StudentDTO {
    private String name;
    private int age;
    private Long facultyId;
    private Long avatarId;

    public StudentDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDTO that = (StudentDTO) o;
        return age == that.age && Objects.equals(name, that.name) && Objects.equals(facultyId, that.facultyId) && Objects.equals(avatarId, that.avatarId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, facultyId, avatarId);
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", facultyId=" + facultyId +
                ", avatarId=" + avatarId +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }
}
