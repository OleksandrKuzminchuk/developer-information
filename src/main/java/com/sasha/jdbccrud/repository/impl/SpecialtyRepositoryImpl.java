package com.sasha.jdbccrud.repository.impl;

import com.sasha.jdbccrud.model.Specialty;
import com.sasha.jdbccrud.model.Status;
import org.apache.commons.lang3.NotImplementedException;
import com.sasha.jdbccrud.repository.SpecialtyRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.sasha.jdbccrud.util.ConnectionUtil.getPreparedStatement;
import static java.lang.String.format;
import static com.sasha.jdbccrud.util.constant.Constants.*;
import static com.sasha.jdbccrud.util.constant.SqlConstantsSpecialty.*;

public class SpecialtyRepositoryImpl implements SpecialtyRepository {
    @Override
    public Optional<Specialty> save(Specialty specialty) {
        try (PreparedStatement statement = getPreparedStatement(SAVE_SPECIALTY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, specialty.getName());
            statement.executeUpdate();
            ResultSet generatedKey = statement.getGeneratedKeys();
            if (generatedKey.next()){
                specialty.setId(generatedKey.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(FAILED_TO_SAVE_A_SPECIALTY + e);
            return Optional.empty();
        }
        return Optional.of(specialty);
    }

    @Override
    public void saveAll(List<Specialty> specialties) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Specialty> update(Specialty specialty) {
        try (PreparedStatement statement = getPreparedStatement(UPDATE_SPECIALTY)) {
            statement.setString(1, specialty.getName());
            statement.setInt(2, specialty.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_UPDATE_A_SPECIALTY_BY_ID, specialty.getId()) + e);
            return Optional.empty();
        }
        return Optional.of(specialty);
    }

    @Override
    public Optional<Specialty> findById(Integer id) {
        Specialty result = null;
        try (PreparedStatement statement = getPreparedStatement(FIND_BY_ID_SPECIALTY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                result = resultSet.next() ? mapResultSetToSpecialty(resultSet) : null;
            }
        } catch (SQLException e) {
            System.out.println(format(FAILED_TO_FIND_A_SPECIALTY_BY_ID, id) + e);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsById(Integer id) {
        try (PreparedStatement statement = getPreparedStatement(EXISTS_BY_ID_SPECIALTY)) {
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

    @Override
    public Optional<List<Specialty>> findAll() {
        List<Specialty> specialities = new LinkedList<>();
        try (ResultSet resultSet = getPreparedStatement(FIND_ALL_SPECIALTIES).executeQuery()) {
            while (resultSet.next()) {
                specialities.add(mapResultSetToSpecialty(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(FAILED_TO_FIND_ALL_SPECIALTIES + e);
            return Optional.empty();
        }
        return Optional.of(specialities);
    }

    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement statement = getPreparedStatement(DELETE_BY_ID_SPECIALTY)) {
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

    @Override
    public void deleteAll() {
        try (PreparedStatement statement = getPreparedStatement(DELETE_ALL_SPECIALTIES)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(FAILED_TO_DELETE_ALL_SPECIALTIES + e);
        }
    }

    private Specialty mapResultSetToSpecialty(ResultSet resultSet) throws SQLException {
        return new Specialty(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                Status.valueOf(resultSet.getString("status"))
        );
    }
}
