package com.sasha.hibernate.view;

import com.sasha.hibernate.controller.DeveloperController;
import com.sasha.hibernate.controller.SkillController;
import com.sasha.hibernate.controller.SpecialtyController;
import com.sasha.hibernate.exception.NotFoundException;
import com.sasha.hibernate.pojo.Developer;
import com.sasha.hibernate.pojo.Skill;
import com.sasha.hibernate.pojo.Specialty;
import com.sasha.hibernate.pojo.Status;
import com.sasha.hibernate.util.constant.Constants;

import java.util.*;

public class DeveloperView {
    private final DeveloperController controller;
    private final SkillController skillController;
    private final SpecialtyController specialtyController;
    private final Scanner scanner;

    public DeveloperView(DeveloperController controller, SkillController skillController, SpecialtyController specialtyController, Scanner scanner) {
        this.controller = controller;
        this.skillController = skillController;
        this.specialtyController = specialtyController;
        this.scanner = scanner;
    }

    public void save() {
        try {
            Developer saveDeveloper = new Developer();
            setFirstNameLastNameToUpdateFromConsole(saveDeveloper);
            saveDeveloper.setSpecialty(setSpecialtyToUpdateFromConsole(saveDeveloper));
            saveDeveloper.setSkills(getSkillsToUpdateFromConsole(saveDeveloper));
            Developer savedDeveloper = controller.save(saveDeveloper);
            System.out.println(savedDeveloper + Constants.TEXT_SAVE_SUCCESSFULLY);
            scanner.reset();
        } catch (IllegalArgumentException | NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println(Constants.EXCEPTION_MISMATCH);
        }
    }

    public void findById() {
        try {
            System.out.println(Constants.TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            Developer foundDeveloper = controller.findById(id);
            System.out.println(foundDeveloper);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println(Constants.EXCEPTION_MISMATCH);
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
            System.out.println(Constants.EMPTY_LIST);
        }
    }

    public void update() {
        try {
            System.out.println(Constants.TEXT_INPUT_ID);
            Integer updateId = scanner.nextInt();
            scanner.nextLine();
            Developer updateDeveloper = controller.findById(updateId);
            if (updateDeveloper.getStatus().equals(Status.ACTIVE)) {
                setFirstNameLastNameToUpdateFromConsole(updateDeveloper);
                updateDeveloper.setSpecialty(setSpecialtyToUpdateFromConsole(updateDeveloper));
                updateDeveloper.setSkills(getSkillsToUpdateFromConsole(updateDeveloper));
            } else {
                throw new NotFoundException(Constants.NOT_FOUND_DEVELOPER);
            }
            Developer updatedDeveloper = controller.update(updateDeveloper);
            System.out.println(updatedDeveloper + Constants.TEXT_UPDATED_SUCCESSFULLY);
            scanner.reset();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println(Constants.EXCEPTION_MISMATCH);
        }
    }

    public void deleteById() {
        try {
            System.out.println(Constants.TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            String deleted = controller.deleteById(id);
            System.out.println(deleted);
        } catch (InputMismatchException e) {
            System.out.println(Constants.EXCEPTION_MISMATCH);
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
            System.out.println(Constants.TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            List<Skill> skills = controller.findSkillsByDeveloperId(id);
            if (skills != null) {
                Comparator<Skill> sortById = Comparator.comparing(Skill::getId);
                controller.findSkillsByDeveloperId(id).stream()
                        .sorted(sortById)
                        .forEach(skill -> System.out.print(skill + "\n"));
            } else {
                System.out.println(Constants.EMPTY_LIST);
            }
        } catch (InputMismatchException e) {
            System.out.println(Constants.EXCEPTION_MISMATCH);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setFirstNameLastNameToUpdateFromConsole(Developer developer) {
        System.out.println(Constants.TEXT_INPUT_NEW_NAME);
        String newFirstName = scanner.nextLine();
        if ((developer.getFirstName() == null && !Objects.equals(newFirstName, "0"))
                || (developer.getFirstName() != null && !developer.getFirstName().equals(newFirstName) && !Objects.equals(newFirstName, "0"))) {

            developer.setFirstName(newFirstName);
        }
        System.out.println(Constants.TEXT_INPUT_NEW_SURNAME);
        String newLastName = scanner.nextLine();
        if ((developer.getLastName() == null && !Objects.equals(newLastName, "0"))
                || (developer.getFirstName() != null && !developer.getLastName().equals(newLastName) && !Objects.equals(newLastName, "0"))) {

            developer.setLastName(newLastName);
        }
    }

    private List<Skill> getSkillsToUpdateFromConsole(Developer developer) {
        List<Skill> allSkills = skillController.findAll();
        List<Skill> result = new ArrayList<>();
        allSkills.removeIf(currentSkill -> developer.getSkills() != null && developer.getSkills().contains(currentSkill));

        System.out.println(Constants.TEXT_INPUT_SKILL_ID);

        Integer selectedId = scanner.nextInt();

        while (selectedId != -1) {
            Integer finalSelectedId = selectedId;
            Skill skillToAdd = allSkills.stream().filter(s -> s.getId().equals(finalSelectedId)).findFirst().orElse(null);

            if (skillToAdd != null) {
                result.add(skillToAdd);
                allSkills.remove(skillToAdd);
            }

            System.out.println(skillToAdd);
            selectedId = scanner.nextInt();
        }
        return result;
    }

    private Specialty setSpecialtyToUpdateFromConsole(Developer developer) {
        List<Specialty> specialties = specialtyController.findAll();
        Specialty result = new Specialty();

        specialties.removeIf(specialty -> developer.getSpecialty() != null && developer.getSpecialty().equals(specialty));

        System.out.println(Constants.TEXT_INPUT_SPECIALITY_ID);
        Integer specialityId = scanner.nextInt();

        if (specialityId != 0) {
            while (specialityId != -1) {
                Integer finalSelectedId = specialityId;
                Specialty specialtyToAdd = specialties.stream()
                        .filter(specialty -> specialty.getId().equals(finalSelectedId))
                        .findFirst().orElse(null);

                if (specialtyToAdd != null) {
                    result.setId(specialtyToAdd.getId());
                    result.setName(specialtyToAdd.getName());
                    result.setStatus(specialtyToAdd.getStatus());
                }

                System.out.println(specialtyToAdd);
                specialityId = scanner.nextInt();
            }
        } else {
            return null;
        }
        return result;
    }
}
