package com.sasha.jdbccrud.util.constant;

public final class SqlConstantsSkill {
    private SqlConstantsSkill() {
        throw new IllegalStateException("Utility class");
    }
    public static final String SAVE_SKILL = "INSERT INTO skill (name, status) VALUES (?, 'ACTIVE')";
    public static final String UPDATE_SKILL = "UPDATE skill SET name = ? WHERE id = ?";
    public static final String FIND_ALL_SKILLS = "SELECT * FROM skill WHERE status = 'ACTIVE'";
    public static final String FIND_BY_ID_SKILL = "SELECT * FROM skill WHERE id = ?";
    public static final String DELETE_BY_ID_SKILL = "UPDATE skill s \n" +
            "LEFT JOIN developer_skill ds ON ds.skill_id = s.id \n" +
            "SET s.status = 'DELETED', ds.skill_id = NULL, ds.developer_id = NULL \n" +
            "WHERE s.id = ?";
    public static final String DELETE_ALL_SKILLS = "UPDATE skill s\n" +
            "LEFT JOIN developer_skill ds ON ds.skill_id = s.id \n" +
            "SET s.status = 'DELETED', ds.skill_id = NULL, ds.developer_id = NULL \n" +
            "WHERE s.status = 'ACTIVE'";

}
