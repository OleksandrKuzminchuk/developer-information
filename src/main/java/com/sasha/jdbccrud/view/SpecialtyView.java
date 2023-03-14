package com.sasha.jdbccrud.view;

import com.sasha.jdbccrud.controller.SpecialtyController;
import com.sasha.jdbccrud.exception.NotFoundException;
import com.sasha.jdbccrud.model.Specialty;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.sasha.jdbccrud.util.constant.Constants.*;

public class SpecialtyView {
    private final SpecialtyController controller;
    private final Scanner scanner;


    public SpecialtyView(SpecialtyController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void save() {
        try {
            System.out.println(TEXT_INPUT_NAME);
            String name = scanner.nextLine();
            Specialty saved = controller.save(new Specialty(name));
            System.out.println(saved + TEXT_SAVE_SUCCESSFULLY);
            scanner.reset();
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        } catch (IllegalArgumentException e) {
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
            Specialty updated = controller.update(new Specialty(id, newName));
            System.out.println(updated + TEXT_UPDATED_SUCCESSFULLY);
            scanner.reset();
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findById() {
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            Specialty found = controller.findById(id);
            System.out.println(found);
        } catch (InputMismatchException e) {
            System.out.println(EXCEPTION_MISMATCH);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findAll() {
        Comparator<Specialty> sortById = Comparator.comparing(Specialty::getId);
        controller.findAll().stream()
                .sorted(sortById)
                .forEach(specialty -> System.out.print(specialty + "\n"));
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
