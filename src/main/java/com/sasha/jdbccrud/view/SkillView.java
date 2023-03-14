package com.sasha.jdbccrud.view;

import com.sasha.jdbccrud.controller.SkillController;
import com.sasha.jdbccrud.exception.NotFoundException;
import com.sasha.jdbccrud.model.Skill;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.sasha.jdbccrud.util.constant.Constants.*;

public class SkillView {
    private final SkillController controller;
    private final Scanner scanner;

    public SkillView(SkillController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void save() {
        try {
            System.out.println(TEXT_INPUT_NAME);
            String name = scanner.nextLine();
            Skill saved = controller.save(new Skill(name));
            System.out.println(saved + TEXT_SAVE_SUCCESSFULLY);
            scanner.reset();
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findById() {
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            Skill skill = controller.findById(id);
            System.out.println(skill);
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update() {
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_NEW_NAME);
            String newName = scanner.nextLine();
            Skill updated = controller.update(new Skill(id, newName));
            System.out.println(updated + TEXT_UPDATED_SUCCESSFULLY);
            scanner.reset();
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
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
}
