package repository.impl;

import model.Specialty;
import model.Status;
import org.apache.commons.lang3.NotImplementedException;
import repository.SpecialtyRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static util.ConnectionUtil.getConnection;
import static util.constant.Constants.*;
import static util.constant.SqlConstantsSpecialty.*;

public class SpecialtyRepositoryImpl implements SpecialtyRepository {

    /*
     The method 'save' is saving new Specialty with name, a field status is approving ACTIVE,
    A field 'Id' is approving auto increment into Data Base.
     */
    @Override
    public Specialty save(Specialty specialty) {
        try (PreparedStatement statement = getConnection().prepareStatement(SAVE_SPECIALTY)) {
            statement.setString(1, specialty.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(FAILED_TO_SAVE_A_SPECIALTY + e);
        }
        return specialty;
    }

    @Override
    public void saveAll(List<Specialty> specialties) {
        throw new NotImplementedException();
    }

    /*
        The 'update' is updating data a specialty by Id
     */
    @Override
    public Specialty update(Specialty specialty) {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SPECIALTY)) {
            statement.setString(1, specialty.getName());
            statement.setInt(2, specialty.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_UPDATE_A_SPECIALTY_BY_ID, specialty.getId()) + e);
        }
        return specialty;
    }

    /*
        The method 'findById' is finding specialty by Id if status 'ACTIVE' or 'DELETED' and throw NotFoundException if not find.
     */
    @Override
    public Optional<Specialty> findById(Integer id) {
        Specialty result = null;
        try (PreparedStatement statement = getConnection().prepareStatement(FIND_BY_ID_SPECIALTY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                result = resultSet.next() ? parseResultSet(resultSet) : null;
            }
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_FIND_A_SPECIALTY_BY_ID, id) + e);
        }
        return Optional.ofNullable(result);
    }

    /*
        The method 'existsById' check is exists the specialty by Id and status - 'ACTIVE' return true or false
     */
    @Override
    public boolean existsById(Integer id) {
        try (PreparedStatement statement = getConnection().prepareStatement(EXISTS_BY_ID_SPECIALTY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_EXISTS_SPECIALTY_BY_ID, id) + e);
        }
        return false;
    }

    /*
    The method 'findAll' is finding all specialties with status - 'ACTIVE'
 */
    @Override
    public List<Specialty> findAll() {
        List<Specialty> specialities = new LinkedList<>();
        try (ResultSet resultSet = getConnection().prepareStatement(FIND_ALL_SPECIALTIES).executeQuery()) {
            while (resultSet.next()) {
                specialities.add(parseResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(FAILED_TO_FIND_ALL_SPECIALTIES + e);
        }
        return specialities;
    }

    @Override
    public Long count() {
        throw new NotImplementedException();
    }

    /*
        The method 'deleteById' is deleting a specialty, changes its status from 'ACTIVE' to 'DELETED' and deletes a specialty to table 'developer'
        where there are developers with such specialty.
     */
    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_BY_ID_SPECIALTY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_DELETE_A_SPECIALTY_BY_ID, id) + e);
        }
    }

    @Override
    public void delete(Specialty specialty) {
        throw new NotImplementedException();
    }

    /*
            The method 'deleteAll' is deleting all specialties, changes their status from 'ACTIVE' to 'DELETE' and deletes all specialties to table 'developer'
     */
    @Override
    public void deleteAll() {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_ALL_SPECIALTIES)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(FAILED_TO_DELETE_ALL_SPECIALTIES + e);
        }
    }

    /*
        Parse ResultSet to Specialty
     */
    private Specialty parseResultSet(ResultSet resultSet) throws SQLException {
        return new Specialty(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                Status.valueOf(resultSet.getString("status"))
        );
    }
}
