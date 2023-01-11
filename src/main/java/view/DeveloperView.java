package view;

import controller.DeveloperController;
import exception.NotFoundException;
import exception.NotValidException;
import model.Developer;
import model.Skill;
import model.Specialty;

import java.util.Comparator;
import java.util.Scanner;

import static util.Constants.*;

public class DeveloperView {
    private final DeveloperController controller;
    public DeveloperView(DeveloperController developerController) {
        this.controller = developerController;
    }

    public void save(Scanner scanner){
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_NAME);
            String firstName = scanner.nextLine();
            System.out.println(TEXT_INPUT_SURNAME);
            String lasName = scanner.nextLine();
            Developer saved = controller.save(new Developer(id, firstName, lasName));
            System.out.println(saved + TEXT_SAVE_SUCCESSFULLY);
            scanner.reset();
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }
    }
    public void findById(Scanner scanner){
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            Developer foundDeveloper = controller.findById(id);
            System.out.println(foundDeveloper);
        }catch (NotFoundException e){
            System.out.println(e.getMessage());
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }
    }
    public void findAll(){
        Comparator<Developer> sortById = Comparator.comparingInt(Developer::getId);
        controller.findAll().stream()
                .sorted(sortById)
                .forEach(developer -> System.out.print(developer + "\n"));
    }
    public void update(Scanner scanner){
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
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }
    }
    public void deleteById(Scanner scanner){
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            String deleted = controller.deleteById(id);
            System.out.println(deleted);
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }
    }
    public void deleteAll(){
        String deletedAll = controller.deleteAll();
        System.out.println(deletedAll);
    }
    public void addSkill(Scanner scanner){
        try{
            System.out.println(TEXT_INPUT_DEVELOPER_ID);
            Integer developerId = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_SKILL_ID);
            Integer skillId = scanner.nextInt();
            scanner.nextLine();
            Skill skill = controller.addSkill(developerId, skillId);
            System.out.println(skill + TEXT_ADDED_SUCCESSFULLY);
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }catch (NotValidException e){
            System.out.println(e.getMessage());
        }
    }
    public void deleteSkill(Scanner scanner){
        try {
            System.out.println(TEXT_INPUT_DEVELOPER_ID);
            Integer developerId = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_SKILL_ID);
            Integer skillId = scanner.nextInt();
            scanner.nextLine();
            String deleted = controller.deleteSkill(developerId, skillId);
            System.out.println(deleted);
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }catch (NotValidException e){
            System.out.println(e.getMessage());
        }
    }
    public void addSpeciality(Scanner scanner){
        try {
            System.out.println(TEXT_INPUT_DEVELOPER_ID);
            Integer developerId = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_SPECIALITY_ID);
            Integer specialityId = scanner.nextInt();
            scanner.nextLine();
            Specialty specialty = controller.addSpecialty(developerId, specialityId);
            System.out.println(specialty + TEXT_ADDED_SUCCESSFULLY);
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }catch (NotValidException e){
            System.out.println(e.getMessage());
        }
    }
    public void deleteSpecialty(Scanner scanner){
        try {
            System.out.println(TEXT_INPUT_DEVELOPER_ID);
            Integer developerId = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_SPECIALITY_ID);
            Integer specialityId = scanner.nextInt();
            scanner.nextLine();
            String deleted = controller.deleteSpecialty(developerId, specialityId);
            System.out.println(deleted);
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }catch (NotValidException e){
            System.out.println(e.getMessage());
        }
    }
    public void findSkillsByDeveloperId(Scanner scanner){
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            Comparator<Skill> sortById = Comparator.comparing(Skill::getId);
            controller.findSkillsByDeveloperId(id).stream()
                    .sorted(sortById)
                    .forEach(skill -> System.out.print(skill + "\n"));
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }
    }
}
