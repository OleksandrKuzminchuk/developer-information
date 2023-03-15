package com.sasha.jdbccrud.util.constant;

public final class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
    public static final String CHANGELOG_FILE = "changelog.xml";
    public static final String PROPERTIES_FILE = "db.properties";
    public static final String NOT_FOUND_DEVELOPER = "Can't find a developer or a developer has been delete or the Id is already taken";
    public static final String NOT_FOUND_SKILL = "Can't find a skill or a skill has been delete or the Id is already taken";
    public static final String NOT_FOUND_SPECIALITY = "Can't find a speciality or a speciality has been delete or the Id is already taken";
    public static final String RESPONSE_OK = "SUCCESSFULLY";
    public static final String TEXT_INPUT_ID = "Please, input id";
    public static final String TEXT_INPUT_NAME = "Please, input name (0 to nothing)";
    public static final String TEXT_INPUT_NEW_NAME = "Please, input new name (0 to nothing)";
    public static final String TEXT_INPUT_NEW_SURNAME = "Please, input new surname (0 to nothing)";
    public static final String TEXT_SAVE_SUCCESSFULLY = " SAVED SUCCESSFULLY";
    public static final String TEXT_UPDATED_SUCCESSFULLY = " UPDATED SUCCESSFULLY";
    public static final String TEXT_INPUT_SPECIALITY_ID = "Please, input Speciality - id (0 to nothing and -1 to stop)";
    public static final String TEXT_INPUT_SKILL_ID = "Enter required skills id (-1 to finish): ";
    public static final String DESCRIBE_APPLICATION = "Hello, this app is developer info\n" +
            "How to use the application:\n" +
            "1. Objects - there are three objects (Developer, Skill and Specialty), you can create a developer by giving him an ID (must be unique), first name and last name (skill and specialty are also created). After creation, you can add a specialty and a list of skills to the developer.\n" +
            "2. Commands - input the text of the command for the action, for each command there will be hints on what to enter and a description of the error what is wrong.\n" +
            "3. Command List:\n" +
            "Developer: \"developers/add\" - add, \"developers/find\" - find by ID, \"developers\" - find everyone, \"developers/update\" - update name and/or last name,\n" +
            "\"developers/delete\" - delete by ID, \"developers/delete/all\" - delete everyone, \"developers/skills\" - find skills.\n" +
            "Skill: \"skills/add\" - add, \"skills/find\" - find by ID, \"skills/update\" - update, \"skills\" - find all, \"skills/delete\" - delete by ID, \"skills/delete/ all\" - delete all.\n" +
            "Specialty: \"specialties/add\" - add, \"specialties/update\" - update, \"specialties/find\" - find by id, \"specialties\" - find all, \"specialties/delete\" - delete by id, \"specialties/delete/ all\" - delete all.\n" +
            "4. To stop the application, input the word \"stop\" in a blank line.";
    public static final String DEVELOPERS_ADD = "developers/add";
    public static final String DEVELOPERS_FIND_BY_ID = "developers/find";
    public static final String DEVELOPERS_FIND_ALL = "developers";
    public static final String DEVELOPERS_UPDATE = "developers/update";
    public static final String DEVELOPERS_DELETE_BY_ID = "developers/delete";
    public static final String DEVELOPERS_DELETE_ALL = "developers/delete/all";
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
    public static final String EXCEPTION_MISMATCH = "Id must be a number";
    public static final String EMPTY_LIST = "List is empty";
    public static final String FAILED_TO_SAVE_A_SPECIALTY = "Failed to save a specialty: ";
    public static final String FAILED_TO_UPDATE_A_SPECIALTY_BY_ID = "Failed to update a specialty by id - [%d]: ";
    public static final String FAILED_TO_FIND_A_SPECIALTY_BY_ID = "Failed to find a specialty by id - [%d]: ";
    public static final String FAILED_TO_FIND_ALL_SPECIALTIES = "Failed to find all specialties: ";
    public static final String FAILED_TO_DELETE_A_SPECIALTY_BY_ID = "Failed to delete a specialty by id - [%d]: ";
    public static final String FAILED_TO_DELETE_ALL_SPECIALTIES = "Failed to delete all specialties: ";
    public static final String FAILED_TO_SAVE_DEVELOPER = "Failed to save developer: ";
    public static final String FAILED_TO_UPDATE_DEVELOPER = "Failed to update developer: ";
    public static final String FAILED_TO_FIND_DEVELOPER_BY_ID = "Failed to find developer by id - [%d]";
    public static final String FAILED_TO_FIND_ALL_DEVELOPERS = "Failed to find all developers: ";
    public static final String FAILED_TO_DELETE_DEVELOPER_BY_ID = "Failed to delete developer by id - [%d]: ";
    public static final String FAILED_TO_DELETE_ALL_DEVELOPERS = "Failed to delete all developers: ";
    public static final String FAILED_TO_ADD_SKILL_BY_ID = "Failed to add skill by id to developer by id: ";
    public static final String FAILED_TO_SAVE_SKILL = "Failed to save skill: ";
    public static final String FAILED_TO_UPDATE_SKILL_BY_ID = "Failed to update a skill by id - [%d]: ";
    public static final String FAILED_TO_FIND_SKILL_BY_ID = "Failed to find a skill by id - [%d]: ";
    public static final String FAILED_TO_FIND_ALL_SKILLS = "Faied to find all skills: ";
    public static final String FAILED_TO_DELETE_SKILL_BY_ID = "Failed to delete skill by id - [%d]: ";
    public static final String FAILED_TO_DELETE_ALL_SKILLS = "Failed to delete all skills: ";
}
