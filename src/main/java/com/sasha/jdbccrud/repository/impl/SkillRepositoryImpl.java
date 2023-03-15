package com.sasha.jdbccrud.repository.impl;

import com.sasha.jdbccrud.model.Skill;
import com.sasha.jdbccrud.model.Status;
import com.sasha.jdbccrud.util.ConnectionUtil;
import com.sasha.jdbccrud.repository.SkillRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static com.sasha.jdbccrud.util.constant.Constants.*;
import static com.sasha.jdbccrud.util.constant.SqlConstantsSkill.*;

public class SkillRepositoryImpl implements SkillRepository {
    private ConnectionUtil connectionUtil = ConnectionUtil.getInstance();
    @Override
    public Optional<Skill> save(Skill skill) {
        try (PreparedStatement statement = connectionUtil.getPreparedStatementWithGeneratedKey(SAVE_SKILL, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, skill.getName());
            statement.executeUpdate();
            ResultSet generatedKey = statement.getGeneratedKeys();
            if (generatedKey.next()){
                skill.setId(generatedKey.getInt(1));
            }
        }catch (SQLException e){
            System.out.println(FAILED_TO_SAVE_SKILL + e);
            return Optional.empty();
        }
        return Optional.of(skill);
    }

    @Override
    public Optional<Skill> update(Skill skill) {
        try (PreparedStatement statement = connectionUtil.getPreparedStatement(UPDATE_SKILL)){
            statement.setString(1, skill.getName());
            statement.setInt(2, skill.getId());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(format(FAILED_TO_UPDATE_SKILL_BY_ID, skill.getId()) + e);
            return Optional.empty();
        }
        return Optional.of(skill);
    }

    @Override
    public Optional<Skill> findById(Integer id) {
        Skill result = null;
        try (PreparedStatement statement = connectionUtil.getPreparedStatement(FIND_BY_ID_SKILL)){
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()){
                result = resultSet.next() ? mapResultSetToSkill(resultSet) : null;
            }
        }catch (SQLException e){
            System.out.println(format(FAILED_TO_FIND_SKILL_BY_ID, id) + e);
            return Optional.empty();
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<List<Skill>> findAll() {
        List<Skill> skills = new LinkedList<>();
        try (ResultSet resultSet = connectionUtil.getPreparedStatement(FIND_ALL_SKILLS).executeQuery()){
            while (resultSet.next())
                skills.add(mapResultSetToSkill(resultSet));
        }catch (SQLException e){
            System.out.println(FAILED_TO_FIND_ALL_SKILLS + e);
            return Optional.empty();
        }
        return Optional.of(skills);
    }

    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement statement = connectionUtil.getPreparedStatement(DELETE_BY_ID_SKILL)){
            statement.setInt(1, id);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(format(FAILED_TO_DELETE_SKILL_BY_ID, id) + e);
        }
    }

    @Override
    public void deleteAll() {
        try (PreparedStatement statement = connectionUtil.getPreparedStatement(DELETE_ALL_SKILLS)){
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(FAILED_TO_DELETE_ALL_SKILLS + e);
        }
    }

    private Skill mapResultSetToSkill(ResultSet resultSet) throws SQLException {
        return new Skill(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                Status.valueOf(resultSet.getString("status"))
        );
    }
}
