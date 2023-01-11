package util;

import static java.lang.String.format;

public final class Constants {
    public static final String FILE_DEVELOPERS_PATH = "C:\\Users\\Kuzminchuk_Alexandr\\IdeaProjects\\developer-information\\src\\main\\resources\\db\\developers.json";
    public static final String FILE_SKILLS_PATH = "C:\\Users\\Kuzminchuk_Alexandr\\IdeaProjects\\developer-information\\src\\main\\resources\\db\\skills.json";
    public static final String FILE_SPECIALTIES_PATH = "C:\\Users\\Kuzminchuk_Alexandr\\IdeaProjects\\developer-information\\src\\main\\resources\\db\\specialties.json";
    public static final String NOT_FOUND_DEVELOPER = "Can't find a developer or a developer has been delete or the Id is already taken";
    public static final String NOT_FOUND_SKILL = "Can't find a skill or a skill has been delete or the Id is already taken";
    public static final String NOT_FOUND_SPECIALITY = "Can't find a speciality or a speciality has been delete or the Id is already taken";
    public static final String NOT_IMPLEMENTED_COUNT = "The method 'count' not implemented";
    public static final String NOT_IMPLEMENTED_DELETE = "The method 'delete' not implemented";
    public static final String DEVELOPER_HAS_SKILL = "The developer already has the skill";
    public static final String DEVELOPER_HAS_NOT_SKILL = "The developer has not the skill";
    public static final String DEVELOPER_HAS_SPECIALITY = "The developer already has the speciality";
    public static final String DEVELOPER_HAS_NOT_SPECIALITY = "The developer has not the speciality";
    public static final String RESPONSE_OK = "SUCCESSFULLY";
    public static final Integer TWO_BILLION = 2_000_000_000;
    public static final String ERROR_IO = "Error input-output or Parse Json: ";
    public static final String TEXT_INPUT_ID = "Please, input id";
    public static final String TEXT_INPUT_NAME = "Please, input name";
    public static final String TEXT_INPUT_NEW_NAME = "Please, input new name";
    public static final String TEXT_INPUT_SURNAME = "Please, input surname";
    public static final String TEXT_INPUT_NEW_SURNAME = "Please, input new surname";
    public static final String TEXT_SAVE_SUCCESSFULLY = " SAVED SUCCESSFULLY";
    public static final String TEXT_UPDATED_SUCCESSFULLY = " UPDATED SUCCESSFULLY";
    public static final String TEXT_ADDED_SUCCESSFULLY = " ADDED SUCCESSFULLY";
    public static final String NOT_NULL = "Can not be 'null': ";
    public static final String TEXT_INPUT_DEVELOPER_ID = "Please, input Developer - id";
    public static final String TEXT_INPUT_SKILL_ID = "Please, input Skill - id";
    public static final String TEXT_INPUT_SPECIALITY_ID = "Please, input Speciality - id";
    public static final String DESCRIBE_APPLICATION = "Hello, this app is developer info\n" +
            "How to use the application:\n" +
            "1. Objects - there are three objects (Developer, Skill and Specialty), you can create a developer by giving him an ID (must be unique), first name and last name (skill and specialty are also created). After creation, you can add a specialty and a list of skills to the developer.\n" +
            "2. Commands - input the text of the command for the action, for each command there will be hints on what to enter and a description of the error what is wrong.\n" +
            "3. Command List:\n" +
            "Developer: \"developers/add\" - add, \"developers/find\" - find by ID, \"developers\" - find everyone, \"developers/update\" - update name and/or last name,\n" +
            "\"developers/delete\" - delete by ID, \"developers/delete/all\" - delete everyone, \"developers/add/skill\" - add skill, \"developers/delete/skill\" - delete all skills, \"developers/add/specialty \" - add a specialty, \"developers/delete/specialty\" - delete a specialty, \"developers/skills\" - find skills.\n" +
            "Skill: \"skills/add\" - add, \"skills/find\" - find by ID, \"skills/update\" - update, \"skills\" - find all, \"skills/delete\" - delete by ID, \"skills/delete/ all\" - delete all.\n" +
            "Specialty: \"specialties/add\" - add, \"specialties/update\" - update, \"specialties/find\" - find by id, \"specialties\" - find all, \"specialties/delete\" - delete by id, \"specialties/delete/ all\" - delete all.\n" +
            "4. To stop the application, input the word \"stop\" in a blank line.";
    public static final String DEVELOPERS_ADD = "developers/add";
    public static final String DEVELOPERS_FIND_BY_ID = "developers/find";
    public static final String DEVELOPERS_FIND_ALL = "developers";
    public static final String DEVELOPERS_UPDATE = "developers/update";
    public static final String DEVELOPERS_DELETE_BY_ID = "developers/delete";
    public static final String DEVELOPERS_DELETE_ALL = "developers/delete/all";
    public static final String DEVELOPERS_ADD_SKILL = "developers/add/skill";
    public static final String DEVELOPERS_DELETE_SKILL = "developers/delete/skill";
    public static final String DEVELOPERS_ADD_SPECIALTY = "developers/add/specialty";
    public static final String DEVELOPERS_DELETE_SPECIALTY = "developers/delete/specialty";
    public static final String DEVELOPERS_FIND_SKILLS_BY_DEVELOPER_ID = "developers/skills";
    public static final String SKILLS_ADD = "skills/add";
    public static final String SKILLS_FIND_BY_ID = "skills/find";
    public static final String SKILLS_UPDATE = "skills/update";
    public static final String SKILLS_FIND_ALL = "skills";
    public static final String SKILLS_DELETE_BY_ID = "skills/delete";
    public static final String SKILLS_DELETE_ALL = "skills/delete/all";
    public static final String SPECIALTIES_ADD = "specialties/add";
    public static final String SPECIALTIES_UPDATE = "specialties/update";
    public static final String SPECIALTIES_FIND_BY_ID = "specialties/find";
    public static final String SPECIALTIES_FIND_ALL = "specialties";
    public static final String SPECIALTIES_DELETE_BY_ID = "specialties/delete";
    public static final String SPECIALTIES_DELETE_ALL = "specialties/delete/all";
}
