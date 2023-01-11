package util;

import controller.DeveloperController;
import controller.SkillController;
import controller.SpecialtyController;
import repository.DeveloperRepository;
import repository.SkillRepository;
import repository.SpecialtyRepository;
import repository.impl.GsonDeveloperRepositoryImpl;
import repository.impl.GsonSkillRepositoryImpl;
import repository.impl.GsonSpecialtyRepositoryImpl;
import view.DeveloperView;
import view.SkillView;
import view.SpecialtyView;

import java.util.Scanner;

import static util.Constants.*;

public class DeveloperApllicationGenerator implements Runnable{
    private final DeveloperView developerView;
    private final SkillView skillView;
    private final SpecialtyView specialtyView;
    public DeveloperApllicationGenerator() {
        DeveloperRepository developerRepository = new GsonDeveloperRepositoryImpl();
        SkillRepository skillRepository = new GsonSkillRepositoryImpl();
        SpecialtyRepository specialtyRepository = new GsonSpecialtyRepositoryImpl();
        DeveloperController developerController = new DeveloperController(developerRepository, skillRepository, specialtyRepository);
        SkillController skillController = new SkillController(skillRepository, developerRepository);
        SpecialtyController specialtyController = new SpecialtyController(specialtyRepository, developerRepository);
        this.developerView = new DeveloperView(developerController);
        this.skillView = new SkillView(skillController);
        this.specialtyView = new SpecialtyView(specialtyController);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(DESCRIBE_APPLICATION);
        String s = "";
        while (!s.equals("stop")) {
            s = scanner.nextLine();
            switch (s) {
                case DEVELOPERS_ADD:
                    developerView.save(scanner);
                    break;
                case DEVELOPERS_FIND_BY_ID:
                    developerView.findById(scanner);
                    break;
                case DEVELOPERS_FIND_ALL:
                    developerView.findAll();
                    break;
                case DEVELOPERS_UPDATE:
                    developerView.update(scanner);
                    break;
                case DEVELOPERS_DELETE_BY_ID:
                    developerView.deleteById(scanner);
                    break;
                case DEVELOPERS_DELETE_ALL:
                    developerView.deleteAll();
                    break;
                case DEVELOPERS_ADD_SKILL:
                    developerView.addSkill(scanner);
                    break;
                case DEVELOPERS_DELETE_SKILL:
                    developerView.deleteSkill(scanner);
                    break;
                case DEVELOPERS_ADD_SPECIALTY:
                    developerView.addSpeciality(scanner);
                    break;
                case DEVELOPERS_DELETE_SPECIALTY:
                    developerView.deleteSpecialty(scanner);
                    break;
                case DEVELOPERS_FIND_SKILLS_BY_DEVELOPER_ID:
                    developerView.findSkillsByDeveloperId(scanner);
                    break;
                case SKILLS_ADD:
                    skillView.save(scanner);
                    break;
                case SKILLS_FIND_BY_ID:
                    skillView.findById(scanner);
                    break;
                case SKILLS_UPDATE:
                    skillView.update(scanner);
                    break;
                case SKILLS_FIND_ALL:
                    skillView.findAll();
                    break;
                case SKILLS_DELETE_BY_ID:
                    skillView.deleteById(scanner);
                    break;
                case SKILLS_DELETE_ALL:
                    skillView.deleteAll();
                    break;
                case SPECIALTIES_ADD:
                    specialtyView.save(scanner);
                    break;
                case SPECIALTIES_UPDATE:
                    specialtyView.update(scanner);
                    break;
                case SPECIALTIES_FIND_BY_ID:
                    specialtyView.findById(scanner);
                    break;
                case SPECIALTIES_FIND_ALL:
                    specialtyView.findAll();
                    break;
                case SPECIALTIES_DELETE_BY_ID:
                    specialtyView.deleteById(scanner);
                    break;
                case SPECIALTIES_DELETE_ALL:
                    specialtyView.deleteAll();
                    break;
                default:
                    System.out.println("No correct a request");
                    break;
            }
        }
    }
}