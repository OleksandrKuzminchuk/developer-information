package util;

import controller.DeveloperController;
import controller.SkillController;
import controller.SpecialtyController;
import repository.DeveloperRepository;
import repository.SkillRepository;
import repository.SpecialtyRepository;
import repository.impl.*;
import service.DeveloperService;
import service.SkillService;
import service.SpecialtyService;
import service.impl.DeveloperServiceImpl;
import service.impl.SkillServiceImpl;
import service.impl.SpecialtyServiceImpl;
import view.DeveloperView;
import view.SkillView;
import view.SpecialtyView;

import java.util.Scanner;

import static util.LiquibaseMigration.migrate;
import static util.constant.Constants.*;

public class DeveloperApllicationGenerator implements Runnable{
    private final DeveloperView developerView;
    private final SkillView skillView;
    private final SpecialtyView specialtyView;
    private static final Scanner SCANNER = new Scanner(System.in);
    public DeveloperApllicationGenerator() {
        SkillRepository skillRepository = new SkillRepositoryImpl();
        SpecialtyRepository specialtyRepository = new SpecialtyRepositoryImpl();
        DeveloperRepository developerRepository = new DeveloperRepositoryImpl();
        SkillService skillService = new SkillServiceImpl(skillRepository);
        SpecialtyService specialtyService = new SpecialtyServiceImpl(specialtyRepository);
        DeveloperService developerService = new DeveloperServiceImpl(developerRepository, specialtyRepository, skillRepository);
        SkillController skillController = new SkillController(skillService);
        SpecialtyController specialtyController = new SpecialtyController(specialtyService);
        DeveloperController developerController = new DeveloperController(developerService);
        this.developerView = new DeveloperView(developerController, SCANNER);
        this.skillView = new SkillView(skillController, SCANNER);
        this.specialtyView = new SpecialtyView(specialtyController, SCANNER);
    }

    @Override
    public void run() {
        migrate();
        System.out.println(DESCRIBE_APPLICATION);
        String userInput = "";
        while (!userInput.equals("stop")) {
            userInput = SCANNER.nextLine();
            switch (userInput) {
                case DEVELOPERS_ADD -> developerView.save();
                case DEVELOPERS_FIND_BY_ID -> developerView.findById();
                case DEVELOPERS_FIND_ALL -> developerView.findAll();
                case DEVELOPERS_UPDATE -> developerView.update();
                case DEVELOPERS_DELETE_BY_ID -> developerView.deleteById();
                case DEVELOPERS_DELETE_ALL -> developerView.deleteAll();
                case DEVELOPERS_ADD_SKILL -> developerView.addSkill();
                case DEVELOPERS_DELETE_SKILL -> developerView.deleteSkill();
                case DEVELOPERS_ADD_SPECIALTY -> developerView.addSpeciality();
                case DEVELOPERS_DELETE_SPECIALTY -> developerView.deleteSpecialty();
                case DEVELOPERS_FIND_SKILLS_BY_DEVELOPER_ID -> developerView.findSkillsByDeveloperId();
                case SKILLS_ADD -> skillView.save();
                case SKILLS_FIND_BY_ID -> skillView.findById();
                case SKILLS_UPDATE -> skillView.update();
                case SKILLS_FIND_ALL -> skillView.findAll();
                case SKILLS_DELETE_BY_ID -> skillView.deleteById();
                case SKILLS_DELETE_ALL -> skillView.deleteAll();
                case SPECIALTIES_ADD -> specialtyView.save();
                case SPECIALTIES_UPDATE -> specialtyView.update();
                case SPECIALTIES_FIND_BY_ID -> specialtyView.findById();
                case SPECIALTIES_FIND_ALL -> specialtyView.findAll();
                case SPECIALTIES_DELETE_BY_ID -> specialtyView.deleteById();
                case SPECIALTIES_DELETE_ALL -> specialtyView.deleteAll();
                default -> System.out.println("No correct a request");
            }
        }
    }
}