package com.sasha.jdbccrud.view;

import com.sasha.jdbccrud.controller.DeveloperController;
import com.sasha.jdbccrud.exception.NotFoundException;
import com.sasha.jdbccrud.model.Developer;
import com.sasha.jdbccrud.model.Skill;
import com.sasha.jdbccrud.model.Specialty;
import com.sasha.jdbccrud.model.Status;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static com.sasha.jdbccrud.util.constant.Constants.*;

public class DeveloperView {
    private final DeveloperController controller;
    private final Scanner scanner;

    public DeveloperView(DeveloperController developerController, Scanner scanner) {
        this.controller = developerController;
        this.scanner = scanner;
    }

    public void save() {
        try {
            System.out.println(TEXT_INPUT_NAME);
            String firstName = scanner.nextLine();
            System.out.println(TEXT_INPUT_SURNAME);
            String lasName = scanner.nextLine();
            System.out.println(TEXT_INPUT_SPECIALITY_ID);
            Integer specialityId = scanner.nextInt();
            System.out.println(TEXT_INPUT_SKILL_ID);
            Integer skillId = scanner.nextInt();
            scanner.nextLine();
            Developer saveDeveloper = new Developer(firstName, lasName);
            setSpecialtyIfSpecialtyIdNotZero(specialityId, saveDeveloper);
            setSkillIfSkillIdNotZero(skillId, saveDeveloper);
            Developer savedDeveloper = controller.save(saveDeveloper);
            System.out.println(savedDeveloper + TEXT_SAVE_SUCCESSFULLY);
            scanner.reset();
        } catch (IllegalArgumentException | NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        }
    }

    public void findById() {
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            Developer foundDeveloper = controller.findById(id);
            System.out.println(foundDeveloper);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        }
    }

    public void findAll() {
        List<Developer> developers = controller.findAll();
        if (!developers.isEmpty()) {
            Comparator<Developer> sortById = Comparator.comparingInt(Developer::getId);
            controller.findAll().stream()
                    .sorted(sortById)
                    .forEach(developer -> System.out.print(developer + "\n"));
        } else {
            System.out.println(EMPTY_LIST);
        }
    }

    //TODO: Help me
    public void update() {
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer updateId = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_NEW_NAME);
            String newFirstName = scanner.nextLine();
            System.out.println(TEXT_INPUT_NEW_SURNAME);
            String newLastName = scanner.nextLine();
            System.out.println(TEXT_INPUT_SPECIALITY_ID);
            Integer newSpecialityId = scanner.nextInt();
            System.out.println(TEXT_INPUT_SKILL_ID);
            Integer newSkillId = scanner.nextInt();

            System.out.println(TEXT_INPUT_SPECIALITY_ID);
            Integer deleteSpecialityId = scanner.nextInt();
            System.out.println(TEXT_INPUT_SKILL_ID);
            Integer deleteSkillId = scanner.nextInt();

            Developer updateDeveloper = controller.findById(updateId);

            if (updateDeveloper.getStatus().equals(Status.ACTIVE)) {
                if (!updateDeveloper.getFirstName().equals(newFirstName)) {
                    updateDeveloper.setFirstName(newFirstName);
                }

                if (!updateDeveloper.getLastName().equals(newLastName)) {
                    updateDeveloper.setLastName(newLastName);
                }

                if (newSpecialityId != 0) {
                    updateDeveloper.setSpecialty(new Specialty(newSpecialityId));
                } else if (deleteSpecialityId != 0) {
                    updateDeveloper.setSpecialty(null);
                }

                if (updateDeveloper.getSkills() != null) {
                    updateDeveloper.getSkills().removeIf(skill -> (deleteSkillId != 0 && deleteSkillId.equals(skill.getId())));
                } else {
                    updateDeveloper.addSkillToDeveloper(new Skill(newSkillId));
                }

                if (updateDeveloper.getSkills() != null){
                    if (newSkillId != 0) {
                        updateDeveloper.getSkills().forEach(skill ->
                        {
                            if (!skill.getId().equals(newSkillId)) {
                                updateDeveloper.addSkillToDeveloper(new Skill(newSkillId));
                            }
                        });
                    }
                }
            } else {
                throw new NotFoundException(NOT_FOUND_DEVELOPER);
            }
            Developer updatedDeveloper = controller.update(updateDeveloper);
            System.out.println(updatedDeveloper + TEXT_UPDATED_SUCCESSFULLY);
            scanner.reset();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        }
    }

    public void deleteById() {
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            String deleted = controller.deleteById(id);
            System.out.println(deleted);
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAll() {
        String deletedAll = controller.deleteAll();
        System.out.println(deletedAll);
    }

    public void findSkillsByDeveloperId() {
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            List<Skill> skills = controller.findSkillsByDeveloperId(id);
            if (skills != null) {
                Comparator<Skill> sortById = Comparator.comparing(Skill::getId);
                controller.findSkillsByDeveloperId(id).stream()
                        .sorted(sortById)
                        .forEach(skill -> System.out.print(skill + "\n"));
            } else {
                System.out.println(EMPTY_LIST);
            }
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void setSpecialtyIfSpecialtyIdNotZero(Integer specialityId, Developer developer) {
        if (specialityId != 0) {
            developer.setSpecialty(new Specialty(specialityId));
        }
    }

    private static void setSkillIfSkillIdNotZero(Integer skillId, Developer developer) {
        if (skillId != 0) {
            developer.addSkillToDeveloper(new Skill(skillId));
        }
    }
}
