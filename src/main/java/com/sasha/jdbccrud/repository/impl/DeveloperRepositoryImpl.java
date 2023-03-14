package com.sasha.jdbccrud.repository.impl;

import com.sasha.jdbccrud.model.Developer;
import com.sasha.jdbccrud.model.Skill;
import com.sasha.jdbccrud.model.Specialty;
import com.sasha.jdbccrud.model.Status;
import org.apache.commons.lang3.NotImplementedException;
import com.sasha.jdbccrud.repository.DeveloperRepository;

import java.sql.*;
import java.util.*;

import static com.sasha.jdbccrud.util.ConnectionUtil.getConnection;
import static com.sasha.jdbccrud.util.ConnectionUtil.getPreparedStatement;
import static java.lang.String.format;
import static com.sasha.jdbccrud.util.constant.Constants.*;
import static com.sasha.jdbccrud.util.constant.SqlConstantsDeveloper.*;

public class DeveloperRepositoryImpl implements DeveloperRepository {
    @Override
    public Optional<Developer> save(Developer developer) {
        String SQL_QUERY = getSqlQueryIfSpecialtyNullOrNotNull(developer, SAVE);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setString(1, developer.getFirstName());
            statement.setString(2, developer.getLastName());
            setSpecialtyIfNotNull(developer, statement);
            statement.executeUpdate();
            generateKey(developer, statement);
            addSkillIfSkillsListNotNull(developer, connection);
            connection.commit();
        } catch (SQLException e) {
            System.out.println(FAILED_TO_SAVE_DEVELOPER + e);
            return Optional.empty();
        }
        return Optional.of(developer);
    }

    @Override
    public void saveAll(List<Developer> developers) {
        throw new NotImplementedException();
    }

    //TODO: Help me
    @Override
    public Optional<Developer> update(Developer developer) {
        String SQL_QUERY = getSqlQueryIfSpecialtyNullOrNotNull(developer, UPDATE);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_QUERY)) {
            connection.setAutoCommit(false);
            statement.setString(1, developer.getFirstName());
            statement.setString(2, developer.getLastName());
            setSpecialtyIfNotNull(developer, statement);
            setDeveloperIdIfSpecialtyNullOrNotNull(developer, statement);
            statement.executeUpdate();
            addSkillIfSkillsListNotNull(developer, connection);
            connection.commit();
        } catch (SQLException e) {
            System.out.println(FAILED_TO_UPDATE_DEVELOPER + e);
            return Optional.empty();
        }
        return Optional.of(developer);
    }

    @Override
    public Optional<Developer> findById(Integer id) {
        Developer result;
        try (PreparedStatement statement = getPreparedStatement(FIND_BY_ID_DEVELOPER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                result = resultSet.next() ? mapResultSetToDeveloper(resultSet) : null;
            }
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_FIND_DEVELOPER_BY_ID, id) + e);
            return Optional.empty();
        }
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        try (PreparedStatement statement = getPreparedStatement(EXISTS_DEVELOPER)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_CHECK_IF_DEVELOPER_EXISTS_BY_ID, id) + e);
        }
        return false;
    }

    @Override
    public Optional<List<Developer>> findAll() {
        List<Developer> developers = new LinkedList<>();
        try (PreparedStatement statement = getPreparedStatement(FIND_ALL_DEVELOPERS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    developers.add(mapResultSetToDeveloper(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println(FAILED_TO_FIND_ALL_DEVELOPERS + e);
            return Optional.empty();
        }
        return Optional.of(developers);
    }

    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement statement = getPreparedStatement(DELETE_BY_ID_DEVELOPER)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_DELETE_DEVELOPER_BY_ID, id) + e);
        }
    }

    @Override
    public void delete(Developer entity) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        try (PreparedStatement statement = getPreparedStatement(DELETE_ALL_DEVELOPERS)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(FAILED_TO_DELETE_ALL_DEVELOPERS + e);
        }
    }

    private void addSkill(Integer developerId, Integer skillId, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(ADD_SKILL)) {
            statement.setInt(1, developerId);
            statement.setInt(2, skillId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_ADD_SKILL_BY_ID, skillId, developerId) + e);
        }
    }

    private void deleteSkill(Integer developerId, Integer skillId, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SKILL_FROM_DEVELOPER)) {
            statement.setInt(1, developerId);
            statement.setInt(2, skillId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_DELETE_SKILL_FROM_DEVELOPER, skillId, developerId) + e);
        }
    }

    private void addSpecialty(Integer developerId, Integer specialityId, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(ADD_SPECIALTY)) {
            statement.setInt(1, specialityId);
            statement.setInt(2, developerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_ADD_SPECIALTY_BY_ID, specialityId, developerId) + e);
        }
    }

    private void deleteSpecialty(Integer developerId, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SPECIALTY)) {
            statement.setInt(1, developerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_DELETE_SPECIALTY_FROM_DEVELOPER_BY_ID, developerId) + e);
        }
    }

    private Developer mapResultSetToDeveloper(ResultSet resultSet) throws SQLException {
        Developer developer = new Developer(
                resultSet.getInt("dev.id"),
                resultSet.getString("dev.first_name"),
                resultSet.getString("dev.last_name"),
                Status.valueOf(resultSet.getString("dev.status")));

        Specialty speciality = new Specialty();
        int specialtyId = resultSet.getInt("dev.specialty_id");
        if (specialtyId > 0) {
            speciality.setId(resultSet.getInt("dev.specialty_id"));
            speciality.setName(resultSet.getString("s.name"));
            speciality.setStatus(Status.valueOf(resultSet.getString("s.status")));
            developer.setSpecialty(speciality);
        }

        List<Skill> skills = new LinkedList<>();
        resultSet.previous();
        while (resultSet.next() && resultSet.getInt("dev.id") == developer.getId()) {
            int skillId = resultSet.getInt("sk.id");
            if (skillId > 0) {
                Skill skill = new Skill(
                        resultSet.getInt("sk.id"),
                        resultSet.getString("sk.name"),
                        Status.valueOf(resultSet.getString("sk.status"))
                );
                skills.add(skill);
                developer.setSkills(skills);
            }
        }
        resultSet.previous();
        return developer;
    }

    private static String getSqlQueryIfSpecialtyNullOrNotNull(Developer developer, String methodName) {
        String SQL_QUERY;
        switch (methodName) {
            case "save" -> {
                if (developer.getSpecialty() == null) {
                    SQL_QUERY = SAVE_DEVELOPER;
                } else {
                    SQL_QUERY = SAVE_DEVELOPER_WITH_SPECIALTY;
                }
                return SQL_QUERY;
            }
            case "update" -> {
                if (developer.getSpecialty() == null) {
                    SQL_QUERY = UPDATE_DEVELOPER;
                } else {
                    SQL_QUERY = UPDATE_DEVELOPER_WITH_SPECIALTY;
                }
                return SQL_QUERY;
            }
            default -> throw new NotImplementedException("No correct the methodName");
        }
    }

    private static void setSpecialtyIfNotNull(Developer developer, PreparedStatement statement) throws SQLException {
        if (developer.getSpecialty() != null) {
            statement.setInt(3, developer.getSpecialty().getId());
        }
    }
    private static void setDeveloperIdIfSpecialtyNullOrNotNull(Developer developer, PreparedStatement statement) throws SQLException {
        if (developer.getSpecialty() == null) {
            statement.setInt(3, developer.getId());
        }else {
            statement.setInt(4, developer.getId());
        }
    }
    private void addSkillIfSkillsListNotNull(Developer developer, Connection connection) {
        if (developer.getSkills() != null) {
            developer.getSkills().forEach(skill -> addSkill(developer.getId(), skill.getId(), connection));
        }
    }
    private static void generateKey(Developer developer, PreparedStatement statement) throws SQLException {
        ResultSet generatedKey = statement.getGeneratedKeys();
        if (generatedKey.next()) {
            developer.setId(generatedKey.getInt(1));
        }
    }

}
