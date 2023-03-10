package repository.impl;

import model.Developer;
import model.Skill;
import model.Specialty;
import model.Status;
import org.apache.commons.lang3.NotImplementedException;
import repository.DeveloperRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.lang.String.format;
import static util.ConnectionUtil.getConnection;
import static util.constant.Constants.*;
import static util.constant.SqlConstantsDeveloper.*;

public class DeveloperRepositoryImpl implements DeveloperRepository {
    /*
    The method 'save' is saving new Developer with firstName and lastName, a field status is approving ACTIVE,
    A field 'Id' is approving auto increment into Data Base.
     */
    @Override
    public Developer save(Developer developer) {
        try (PreparedStatement statement = getConnection().prepareStatement(SAVE_DEVELOPER)) {
            statement.setString(1, developer.getFirstName());
            statement.setString(2, developer.getLastName());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(FAILED_TO_SAVE_DEVELOPER + e);
        }
        return developer;
    }

    @Override
    public void saveAll(List<Developer> developers) {
        throw new NotImplementedException();
    }

    /*
    The 'update' is updating data a developer by Id
     */
    @Override
    public Developer update(Developer developer) {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_DEVELOPER)) {
            statement.setString(1, developer.getFirstName());
            statement.setString(2, developer.getLastName());
            statement.setInt(3, developer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(FAILED_TO_UPDATE_DEVELOPER + e);
        }
        return developer;
    }

    /*
    The method 'findById' is finding developer by Id if status 'ACTIVE' or 'DELETED' and throw NotFoundException if not find.
     */
    @Override
    public Optional<Developer> findById(Integer id) {
        Developer result = null;
        try (PreparedStatement statement = getConnection().prepareStatement(FIND_BY_ID_DEVELOPER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                result = resultSet.next() ? parseResultSet(resultSet) : null;
            }
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_FIND_DEVELOPER_BY_ID, id) + e);
        }
        return Optional.ofNullable(result);
    }

    /*
    The method 'existsById' check is exists the developer by Id and status - 'ACTIVE' return true or false
     */
    @Override
    public boolean existsById(Integer id) {
        try (PreparedStatement statement = getConnection().prepareStatement(EXISTS_DEVELOPER)) {
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

    /*
    The method 'findAll' is finding all developers with status - 'ACTIVE', developer's specialty and all developer's skills
     */
    @Override
    public List<Developer> findAll() {
        List<Developer> developers = new LinkedList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(FIND_ALL_DEVELOPERS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    developers.add(parseResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println(FAILED_TO_FIND_ALL_DEVELOPERS + e);
        }
        return developers;
    }

    @Override
    public Long count() {
        throw new NotImplementedException();
    }

    /*
    The method 'deleteById' is deleting a developer and changes its status from 'ACTIVE' to 'DELETED'
     */
    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_BY_ID_DEVELOPER)) {
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

    /*
        The method 'deleteAll' is deleting all developers and changes their status from 'ACTIVE' to 'DELETE'
     */
    @Override
    public void deleteAll() {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_ALL_DEVELOPERS)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(FAILED_TO_DELETE_ALL_DEVELOPERS + e);
        }
    }

    /*
    The method 'addSkill' is adding a skill to developer's list
     */
    @Override
    public void addSkill(Integer developerId, Integer skillId) {
        try (PreparedStatement statement = getConnection().prepareStatement(ADD_SKILL)) {
            statement.setInt(1, developerId);
            statement.setInt(2, skillId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_ADD_SKILL_BY_ID, skillId, developerId) + e);
        }
    }

    /*
    The method 'deleteSkill' is deleting a skill from developer's list
     */
    @Override
    public void deleteSkill(Integer developerId, Integer skillId) {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_BY_ID_SKILL_FROM_DEVELOPER)){
            statement.setInt(1, developerId);
            statement.setInt(2, skillId);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(format(FAILED_TO_DELETE_SKILL_FROM_DEVELOPER, skillId, developerId) + e);
        }
    }

    /*
        The method 'addSpecialty' is adding a specialty to developer
     */
    @Override
    public void addSpecialty(Integer developerId, Integer specialityId) {
        try (PreparedStatement statement = getConnection().prepareStatement(ADD_SPECIALTY)) {
            statement.setInt(1, specialityId);
            statement.setInt(2, developerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_ADD_SPECIALTY_BY_ID, specialityId, developerId) + e);
        }
    }

    /*
        The method 'deleteSpecialty' is deleting a specialty from developer
     */
    @Override
    public void deleteSpecialty(Integer developerId) {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_SPECIALTY)) {
            statement.setInt(1, developerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_DELETE_SPECIALTY_FROM_DEVELOPER_BY_ID, developerId) + e);
        }
    }

    /*
    Parse ResultSet to Developer and if the specialty equal not null then return specialty Id and specialty Name, parse all skills from each developer.
    the method 'resultSet.previous()' is need to move the cursor to the previous row in this ResultSet object.
     */
    private Developer parseResultSet(ResultSet resultSet) throws SQLException {
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
}
