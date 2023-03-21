package com.sasha.hibernate.util;

import com.sasha.hibernate.controller.DeveloperController;
import com.sasha.hibernate.controller.SkillController;
import com.sasha.hibernate.controller.SpecialtyController;
import com.sasha.hibernate.repository.DeveloperRepository;
import com.sasha.hibernate.repository.SkillRepository;
import com.sasha.hibernate.repository.SpecialtyRepository;
import com.sasha.hibernate.repository.impl.DeveloperRepositoryImpl;
import com.sasha.hibernate.repository.impl.SkillRepositoryImpl;
import com.sasha.hibernate.repository.impl.SpecialtyRepositoryImpl;
import com.sasha.hibernate.service.DeveloperService;
import com.sasha.hibernate.service.SkillService;
import com.sasha.hibernate.service.SpecialtyService;
import com.sasha.hibernate.service.impl.SpecialtyServiceImpl;
import com.sasha.hibernate.util.constant.Constants;
import com.sasha.hibernate.view.SkillView;
import com.sasha.hibernate.view.SpecialtyView;
import com.sasha.hibernate.service.impl.DeveloperServiceImpl;
import com.sasha.hibernate.service.impl.SkillServiceImpl;
import com.sasha.hibernate.view.DeveloperView;

import java.util.Scanner;

import static com.sasha.hibernate.util.constant.Constants.NO_CORRECT_RESULT;
import static com.sasha.hibernate.util.constant.Constants.STOP_WORD;

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
        FlywayMigration.migrate();
        System.out.println(Constants.DESCRIBE_APPLICATION);
        String userInput = "";
        while (!userInput.equals(STOP_WORD)) {
            userInput = SCANNER.nextLine();
            switch (userInput) {
                case Constants.DEVELOPERS_ADD -> developerView.save();
                case Constants.DEVELOPERS_FIND_BY_ID -> developerView.findById();
                case Constants.DEVELOPERS_FIND_ALL -> developerView.findAll();
                case Constants.DEVELOPERS_UPDATE -> developerView.update();
                case Constants.DEVELOPERS_DELETE_BY_ID -> developerView.deleteById();
                case Constants.DEVELOPERS_DELETE_ALL -> developerView.deleteAll();
                case Constants.DEVELOPERS_FIND_SKILLS_BY_DEVELOPER_ID -> developerView.findSkillsByDeveloperId();
                case Constants.SKILLS_ADD -> skillView.save();
                case Constants.SKILLS_FIND_BY_ID -> skillView.findById();
                case Constants.SKILLS_UPDATE -> skillView.update();
                case Constants.SKILLS_FIND_ALL -> skillView.findAll();
                case Constants.SKILLS_DELETE_BY_ID -> skillView.deleteById();
                case Constants.SKILLS_DELETE_ALL -> skillView.deleteAll();
                case Constants.SPECIALTIES_ADD -> specialtyView.save();
                case Constants.SPECIALTIES_UPDATE -> specialtyView.update();
                case Constants.SPECIALTIES_FIND_BY_ID -> specialtyView.findById();
                case Constants.SPECIALTIES_FIND_ALL -> specialtyView.findAll();
                case Constants.SPECIALTIES_DELETE_BY_ID -> specialtyView.deleteById();
                case Constants.SPECIALTIES_DELETE_ALL -> specialtyView.deleteAll();
                default -> System.out.println(NO_CORRECT_RESULT);
            }
        }
    }
}