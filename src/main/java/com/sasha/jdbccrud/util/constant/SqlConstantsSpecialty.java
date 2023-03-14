package com.sasha.jdbccrud.util.constant;

public final class SqlConstantsSpecialty {
    private SqlConstantsSpecialty() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SAVE_SPECIALTY = "INSERT INTO specialty (name, status) VALUES (?, 'ACTIVE')";
    public static final String UPDATE_SPECIALTY = "UPDATE specialty SET name = ? WHERE id = ?";
    public static final String EXISTS_BY_ID_SPECIALTY = "SELECT COUNT(*) as count FROM specialty WHERE id = ? AND status = 'ACTIVE'";
    public static final String FIND_ALL_SPECIALTIES = "SELECT * FROM specialty WHERE status = 'ACTIVE'";
    public static final String DELETE_BY_ID_SPECIALTY = "UPDATE specialty s\n" +
            "LEFT JOIN developer d ON d.specialty_id = s.id \n" +
            "SET s.status = 'DELETED', d.specialty_id = NULL \n" +
            "WHERE s.id = ?";
    public static final String DELETE_ALL_SPECIALTIES = "UPDATE specialty s\n" +
            "LEFT JOIN developer d ON d.specialty_id = s.id \n" +
            "SET s.status = 'DELETED', d.specialty_id = NULL";
    public static final String FIND_BY_ID_SPECIALTY = "SELECT * FROM specialty WHERE id = ?";
}
