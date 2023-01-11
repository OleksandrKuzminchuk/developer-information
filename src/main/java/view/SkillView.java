package view;

import controller.SkillController;
import model.Skill;

import java.util.Comparator;
import java.util.Scanner;

import static util.Constants.*;

public class SkillView {
    private final SkillController controller;

    public SkillView(SkillController controller) {
        this.controller = controller;
    }
    public void save(Scanner scanner){
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_NAME);
            String name = scanner.nextLine();
            Skill saved = controller.save(new Skill(id, name));
            System.out.println(saved + TEXT_SAVE_SUCCESSFULLY);
            scanner.reset();
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }
    public void findById(Scanner scanner){
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            Skill skill = controller.findById(id);
            System.out.println(skill);
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }
    }
    public void update(Scanner scanner){
        try {
            System.out.println(TEXT_INPUT_ID);
            Integer id = scanner.nextInt();
            scanner.nextLine();
            System.out.println(TEXT_INPUT_NEW_NAME);
            String newName = scanner.nextLine();
            Skill updated = controller.update(new Skill(id, newName));
            System.out.println(updated + TEXT_UPDATED_SUCCESSFULLY);
            scanner.reset();
        }catch (NullPointerException e){
            System.out.println(NOT_NULL + e);
        }
    }
    public void findAll(){
        Comparator<Skill> sortById = Comparator.comparing(Skill::getId);
        controller.findAll().stream()
                .sorted(sortById)
                .forEach(skill -> System.out.print(skill + "\n"));
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
}
