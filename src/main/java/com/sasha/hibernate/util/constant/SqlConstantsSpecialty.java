package com.sasha.hibernate.util.constant;

public final class SqlConstantsSpecialty {
    private SqlConstantsSpecialty() {
        throw new IllegalStateException("Utility class");
    }
    public static final String FIND_ALL_DEVELOPERS_WITH_SPECIALTIES = "SELECT d FROM Developer d LEFT JOIN FETCH d.specialty WHERE d.status=:active";
    public static final String DELETE_ALL_SPECIALTIES =
            "UPDATE Specialty s SET s.status=: deleted WHERE s.status =: active";
    public static final String FIND_ALL_SPECIALTIES = "FROM Specialty s WHERE s.status=: active";
}
