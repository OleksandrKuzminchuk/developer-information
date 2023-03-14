package com.sasha.jdbccrud.util.constant;

public final class SqlConstantsDeveloper {
    private SqlConstantsDeveloper() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SAVE_DEVELOPER = "INSERT INTO developer (first_name, last_name, status) "
            + "VALUES (?, ?, 'ACTIVE')";
    public static final String SAVE_DEVELOPER_WITH_SPECIALTY = "INSERT INTO developer (first_name, last_name, specialty_id, status) "
            + "VALUES (?, ?, ?, 'ACTIVE')";
    public static final String SAVE_DEVELOPER_WITH_SKILLS = "INSERT INTO developer (first_name, last_name, status) "
            + "VALUES (?, ?, 'ACTIVE')";
    public static final String SAVE_DEVELOPER_WITH_SPECIALTY_SKILLS = "INSERT INTO developer (first_name, last_name, status) "
            + "VALUES (?, ?, 'ACTIVE')";
    public static final String FIND_ALL_DEVELOPERS =
    "SELECT dev.id, dev.first_name, dev.last_name, dev.status, dev.specialty_id, s.name, s.status, sk.id, sk.name, sk.status \n" +
            "FROM developer dev\n" +
            "LEFT JOIN specialty s ON s.id = dev.specialty_id\n" +
            "LEFT JOIN developer_skill ds ON ds.developer_id = dev.id\n" +
            "LEFT JOIN skill sk ON sk.id = ds.skill_id\n" +
            "WHERE dev.status = 'ACTIVE'";

    public static final String UPDATE_DEVELOPER = "UPDATE developer SET first_name = ?, last_name = ? WHERE id = ?";
    public static final String UPDATE_DEVELOPER_WITH_SPECIALTY =
            "UPDATE developer SET first_name = ?, last_name = ?, specialty_id = ? WHERE id = ?";
    public static final String EXISTS_DEVELOPER = "SELECT COUNT(*) as count FROM developer WHERE id = ? AND status != 'DELETED'";
    public static final String FIND_BY_ID_DEVELOPER =
            "SELECT dev.id, dev.first_name, dev.last_name, dev.status, dev.specialty_id, s.name, s.status, sk.id, sk.name, sk.status \n" +
                    "FROM developer dev\n" +
                    "LEFT JOIN specialty s ON s.id = dev.specialty_id\n" +
                    "LEFT JOIN developer_skill ds ON ds.developer_id = dev.id\n" +
                    "LEFT JOIN skill sk ON sk.id = ds.skill_id\n" +
                    "WHERE dev.id = ?";
    public static final String DELETE_BY_ID_DEVELOPER = "UPDATE developer SET status = 'DELETED' WHERE id = ? AND status = 'ACTIVE'";
    public static final String DELETE_ALL_DEVELOPERS = "UPDATE developer SET status = 'DELETED'";
    public static final String ADD_SPECIALTY = "UPDATE developer SET specialty_id = ? WHERE id = ?";
    public static final String DELETE_SPECIALTY = "UPDATE developer SET specialty_id = null WHERE id = ?";
    public static final String ADD_SKILL = "INSERT INTO developer_skill (developer_id, skill_id) VALUES (?, ?)";
    public static final String DELETE_BY_ID_SKILL_FROM_DEVELOPER = "UPDATE developer_skill \n" +
            "SET skill_id = NULL, developer_id = NULL \n" +
            "WHERE developer_id = ? AND skill_id = ?";

}
