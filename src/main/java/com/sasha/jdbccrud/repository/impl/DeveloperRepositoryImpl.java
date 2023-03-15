package com.sasha.jdbccrud.repository.impl;

import com.sasha.jdbccrud.model.Developer;
import com.sasha.jdbccrud.model.Skill;
import com.sasha.jdbccrud.model.Specialty;
import com.sasha.jdbccrud.model.Status;
import com.sasha.jdbccrud.util.ConnectionUtil;
import com.sasha.jdbccrud.repository.DeveloperRepository;

import java.sql.*;
import java.util.*;

import static java.lang.String.format;
import static com.sasha.jdbccrud.util.constant.Constants.*;
import static com.sasha.jdbccrud.util.constant.SqlConstantsDeveloper.*;

public class DeveloperRepositoryImpl implements DeveloperRepository {
    private ConnectionUtil connectionUtil = ConnectionUtil.getInstance();
    @Override
    public Optional<Developer> save(Developer developer) {
        try (PreparedStatement statement = connectionUtil.getPreparedStatementWithGeneratedKey(SAVE_DEVELOPER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, developer.getFirstName());
            statement.setString(2, developer.getLastName());
            statement.setObject(3, developer.getSpecialty() == null ? null : developer.getSpecialty().getId(), Types.INTEGER);
            statement.executeUpdate();
            generateKey(developer, statement);
            processDevelopersSkills(developer);
        } catch (SQLException e) {
            System.out.println(FAILED_TO_SAVE_DEVELOPER + e);
            return Optional.empty();
        }
        return Optional.of(developer);
    }

    @Override
    public Optional<Developer> update(Developer developer) {
        try (PreparedStatement statement = connectionUtil.getPreparedStatement(UPDATE_DEVELOPER)) {
            statement.setString(1, developer.getFirstName());
            statement.setString(2, developer.getLastName());
            setDeveloperIdIfSpecialtyNullOrNotNull(developer, statement);
            statement.executeUpdate();
            processDevelopersSkills(developer);
        } catch (SQLException e) {
            System.out.println(FAILED_TO_UPDATE_DEVELOPER + e);
            return Optional.empty();
        }
        return Optional.of(developer);
    }

    @Override
    public Optional<Developer> findById(Integer id) {
        Developer result;
        try (PreparedStatement statement = connectionUtil.getPreparedStatementWithResultSetType(FIND_BY_ID_DEVELOPER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
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
    public Optional<List<Developer>> findAll() {
        List<Developer> developers = new LinkedList<>();
        try (PreparedStatement statement = connectionUtil.getPreparedStatementWithResultSetType(FIND_ALL_DEVELOPERS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
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
        try (PreparedStatement statement = connectionUtil.getPreparedStatement(DELETE_BY_ID_DEVELOPER)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_DELETE_DEVELOPER_BY_ID, id) + e);
        }
    }

    @Override
    public void deleteAll() {
        try (PreparedStatement statement = connectionUtil.getPreparedStatement(DELETE_ALL_DEVELOPERS)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(FAILED_TO_DELETE_ALL_DEVELOPERS + e);
        }
    }

    private void processDevelopersSkills(Developer developer) {
        if (developer.getSkills() != null) {
            try (PreparedStatement statement = connectionUtil.getPreparedStatement(ADD_SKILL)) {
                for (Skill skill : developer.getSkills()) {
                    statement.setInt(1, developer.getId());
                    statement.setInt(2, skill.getId());
                    statement.addBatch();
                }
                statement.executeBatch();
            } catch (SQLException e) {
                System.out.println(FAILED_TO_ADD_SKILL_BY_ID + e);
            }
        }
    }

    private Developer mapResultSetToDeveloper(ResultSet resultSet) throws SQLException {
        Developer developer = new Developer(
                resultSet.getInt(1),
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

    private static void setDeveloperIdIfSpecialtyNullOrNotNull(Developer developer, PreparedStatement statement) throws SQLException {
        if (developer.getSpecialty() == null) {
            statement.setObject(3, null, Types.INTEGER);
            statement.setInt(4, developer.getId());
        } else {
            statement.setInt(3, developer.getSpecialty().getId());
            statement.setInt(4, developer.getId());
        }
    }

    private static void generateKey(Developer developer, PreparedStatement statement) throws SQLException {
        ResultSet generatedKey = statement.getGeneratedKeys();
        if (generatedKey.next()) {
            developer.setId(generatedKey.getInt(1));
        }
    }

}
