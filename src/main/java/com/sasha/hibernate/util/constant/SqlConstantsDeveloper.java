package com.sasha.hibernate.util.constant;

public final class SqlConstantsDeveloper {
    private SqlConstantsDeveloper() {
        throw new IllegalStateException("Utility class");
    }
    public static final String FIND_ALL_DEVELOPERS =
            "SELECT d FROM Developer d " +
                    "LEFT JOIN FETCH d.skills " +
                    "LEFT JOIN FETCH d.specialty " +
                    "WHERE d.status=: active";
    public static final String FIND_BY_ID_DEVELOPER =
            "SELECT d FROM Developer d " +
                    "LEFT JOIN FETCH d.skills " +
                    "LEFT JOIN FETCH d.specialty " +
                    "WHERE d.id=: id";
}
