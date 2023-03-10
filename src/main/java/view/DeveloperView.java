package view;

import controller.DeveloperController;
import exception.NotFoundException;
import exception.NotValidException;
import model.Developer;
import model.Skill;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static util.constant.Constants.*;

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
            Developer saved = controller.save(new Developer(firstName, lasName));
            System.out.println(saved + TEXT_SAVE_SUCCESSFULLY);
            scanner.reset();
        } catch (IllegalArgumentException e) {
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

    public void update() {
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_NEW_NAME);
            String newFirstName = scanner.nextLine();
            System.out.println(TEXT_INPUT_NEW_SURNAME);
            String newLastName = scanner.nextLine();
            Developer updated = controller.update(new Developer(id, newFirstName, newLastName));
            System.out.println(updated + TEXT_UPDATED_SUCCESSFULLY);
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

    public void addSkill() {
        try {
            System.out.println(TEXT_INPUT_DEVELOPER_ID);
            Integer developerId = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_SKILL_ID);
            Integer skillId = scanner.nextInt();
            scanner.nextLine();
            controller.addSkill(developerId, skillId);
            System.out.println(TEXT_ADDED_SUCCESSFULLY);
        } catch (NotFoundException | NotValidException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        }
    }

    public void deleteSkill() {
        try {
            System.out.println(TEXT_INPUT_DEVELOPER_ID);
            Integer developerId = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_SKILL_ID);
            Integer skillId = scanner.nextInt();
            scanner.nextLine();
            String deleted = controller.deleteSkill(developerId, skillId);
            System.out.println(deleted);
        } catch (NotValidException | NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        }
    }

    public void addSpeciality() {
        try {
            System.out.println(TEXT_INPUT_DEVELOPER_ID);
            Integer developerId = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_SPECIALITY_ID);
            Integer specialityId = scanner.nextInt();
            scanner.nextLine();
            controller.addSpecialty(developerId, specialityId);
            System.out.println(TEXT_ADDED_SUCCESSFULLY);
        } catch (NotValidException | NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        }
    }

    public void deleteSpecialty() {
        try {
            System.out.println(TEXT_INPUT_DEVELOPER_ID);
            Integer developerId = scanner.nextInt();
            scanner.nextLine();
            String deleted = controller.deleteSpecialty(developerId);
            System.out.println(deleted);
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        } catch (NotValidException | NotFoundException e) {
            System.out.println(e.getMessage());
        }
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
}
