package com.sasha.hibernate.util.constant;

public final class SqlConstantsSkill {
    private SqlConstantsSkill() {
        throw new IllegalStateException("Utility class");
    }
    public static final String FIND_ALL_SKILLS = "FROM Skill s WHERE s.status=: active";
    public static final String FIND_DEVELOPERS_WITH_SKILLS = "SELECT d FROM Developer d LEFT JOIN FETCH d.skills WHERE d.status=:active";
    public static final String DELETE_ALL_SKILLS =
            "UPDATE Skill s SET s.status=: deleted WHERE s.status=: active";

}
