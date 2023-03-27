package com.sasha.hibernate.view;

import com.sasha.hibernate.pojo.Skill;
import com.sasha.hibernate.util.constant.Constants;
import com.sasha.hibernate.controller.SkillController;
import com.sasha.hibernate.exception.NotFoundException;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SkillView {
    private final SkillController controller;
    private final Scanner scanner;

    public SkillView(SkillController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void save() {
        try {
            System.out.println(Constants.TEXT_INPUT_NAME);
            String name = scanner.nextLine();
            Skill saved = controller.save(new Skill(name));
            System.out.println(saved + Constants.TEXT_SAVE_SUCCESSFULLY);
            scanner.reset();
        } catch (InputMismatchException e) {
            System.out.println(Constants.EXCEPTION_MISMATCH);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findById() {
        try {
            System.out.println(Constants.TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            Skill skill = controller.findById(id);
            System.out.println(skill);
        } catch (InputMismatchException e) {
            System.out.println(Constants.EXCEPTION_MISMATCH);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update() {
        try {
            System.out.println(Constants.TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            System.out.println(Constants.TEXT_INPUT_NEW_NAME);
            String newName = scanner.nextLine();
            Skill updated = controller.update(new Skill(id, newName));
            System.out.println(updated + Constants.TEXT_UPDATED_SUCCESSFULLY);
            scanner.reset();
        } catch (InputMismatchException e) {
            System.out.println(Constants.EXCEPTION_MISMATCH);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findAll() {
        Comparator<Skill> sortById = Comparator.comparing(Skill::getId);
        controller.findAll().stream()
                .sorted(sortById)
                .forEach(skill -> System.out.print(skill + "\n"));
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
}
