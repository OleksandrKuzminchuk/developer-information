package com.sasha.jdbccrud.util;

import com.sasha.jdbccrud.controller.DeveloperController;
import com.sasha.jdbccrud.controller.SkillController;
import com.sasha.jdbccrud.controller.SpecialtyController;
import com.sasha.jdbccrud.repository.DeveloperRepository;
import com.sasha.jdbccrud.repository.SkillRepository;
import com.sasha.jdbccrud.repository.SpecialtyRepository;
import com.sasha.jdbccrud.repository.impl.*;
import com.sasha.jdbccrud.service.DeveloperService;
import com.sasha.jdbccrud.service.SkillService;
import com.sasha.jdbccrud.service.SpecialtyService;
import com.sasha.jdbccrud.service.impl.DeveloperServiceImpl;
import com.sasha.jdbccrud.service.impl.SkillServiceImpl;
import com.sasha.jdbccrud.service.impl.SpecialtyServiceImpl;
import com.sasha.jdbccrud.view.DeveloperView;
import com.sasha.jdbccrud.view.SkillView;
import com.sasha.jdbccrud.view.SpecialtyView;

import java.util.Scanner;

import static com.sasha.jdbccrud.util.LiquibaseMigration.migrate;
import static com.sasha.jdbccrud.util.constant.Constants.*;

public class DeveloperApplicationGenerator implements Runnable{
    private final DeveloperView developerView;
    private final SkillView skillView;
    private final SpecialtyView specialtyView;
    private static final Scanner SCANNER = new Scanner(System.in);
    public DeveloperApplicationGenerator() {
        SkillRepository skillRepository = new SkillRepositoryImpl();
        SpecialtyRepository specialtyRepository = new SpecialtyRepositoryImpl();
        DeveloperRepository developerRepository = new DeveloperRepositoryImpl();
        SkillService skillService = new SkillServiceImpl(skillRepository);
        SpecialtyService specialtyService = new SpecialtyServiceImpl(specialtyRepository);
        DeveloperService developerService = new DeveloperServiceImpl(developerRepository);
        SkillController skillController = new SkillController(skillService);
        SpecialtyController specialtyController = new SpecialtyController(specialtyService);
        DeveloperController developerController = new DeveloperController(developerService);
        this.developerView = new DeveloperView(developerController, skillController, specialtyController, SCANNER);
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