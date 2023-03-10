package repository.impl;

import model.Skill;
import model.Status;
import org.apache.commons.lang3.NotImplementedException;
import repository.SkillRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static util.ConnectionUtil.getConnection;
import static util.constant.Constants.*;
import static util.constant.SqlConstantsSkill.*;

public class SkillRepositoryImpl implements SkillRepository {
    /*
     The method 'save' is saving new Skill with name, a field status is approving ACTIVE,
    A field 'Id' is approving auto increment into Data Base.
     */
    @Override
    public Skill save(Skill skill) {
        try (PreparedStatement statement = getConnection().prepareStatement(SAVE_SKILL)){
            statement.setString(1, skill.getName());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(FAILED_TO_SAVE_SKILL + e);
        }
        return skill;
    }

    @Override
    public void saveAll(List<Skill> entities) {
        throw new NotImplementedException();
    }

    /*
        The 'update' is updating data a skill by Id
     */
    @Override
    public Skill update(Skill skill) {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SKILL)){
            statement.setString(1, skill.getName());
            statement.setInt(2, skill.getId());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(format(FAILED_TO_UPDATE_SKILL_BY_ID, skill.getId()) + e);
        }
        return skill;
    }

    /*
    The method 'findById' is finding skill by Id if status 'ACTIVE' or 'DELETED' and throw NotFoundException if not find.
     */
    @Override
    public Optional<Skill> findById(Integer id) {
        Skill result = null;
        try (PreparedStatement statement = getConnection().prepareStatement(FIND_BY_ID_SKILL)){
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()){
                result = resultSet.next() ? parseResultSet(resultSet) : null;
            }
        }catch (SQLException e){
            System.out.println(format(FAILED_TO_FIND_SKILL_BY_ID, id) + e);
        }
        return Optional.ofNullable(result);
    }

    /*
        The method 'existsById' check is exists the skill by Id and status - 'ACTIVE' return true or false
     */
    @Override
    public boolean existsById(Integer id) {
        try (PreparedStatement statement = getConnection().prepareStatement(EXISTS_BY_ID_SKILL)){
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }catch (SQLException e){
            System.out.println(format(FAILED_TO_EXISTS_SKILL_BY_ID, id) + e);
        }
        return false;
    }

    /*
        The method 'findAll' is finding all skills with status - 'ACTIVE'
     */
    @Override
    public List<Skill> findAll() {
        List<Skill> skills = new LinkedList<>();
        try (ResultSet resultSet = getConnection().prepareStatement(FIND_ALL_SKILLS).executeQuery()){
            while (resultSet.next())
                skills.add(parseResultSet(resultSet));
        }catch (SQLException e){
            System.out.println(FAILED_TO_FIND_ALL_SKILLS + e);
        }
        return skills;
    }

    @Override
    public Long count() {
        throw new NotImplementedException();
    }

    /*
        The method 'deleteById' is deleting a skill and changes its status from 'ACTIVE' to 'DELETED' and delete from list's developer
     */
    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_BY_ID_SKILL)){
            statement.setInt(1, id);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(format(FAILED_TO_DELETE_SKILL_BY_ID, id) + e);
        }
    }

    @Override
    public void delete(Skill entity) {
        throw new NotImplementedException();
    }

    /*
            The method 'deleteAll' is deleting all skills and changes their status from 'ACTIVE' to 'DELETE' and deletes from list's developer
     */
    @Override
    public void deleteAll() {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_ALL_SKILLS)){
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(FAILED_TO_DELETE_ALL_SKILLS + e);
        }
    }

    /*
        Parse ResultSet to Skill
     */
    private Skill parseResultSet(ResultSet resultSet) throws SQLException {
        return new Skill(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                Status.valueOf(resultSet.getString("status"))
        );
    }
}
