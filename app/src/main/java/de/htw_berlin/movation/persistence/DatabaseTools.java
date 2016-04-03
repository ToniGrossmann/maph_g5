package de.htw_berlin.movation.persistence;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.List;

import de.htw_berlin.movation.persistence.model.Assignment;
import de.htw_berlin.movation.persistence.model.Discount;
import de.htw_berlin.movation.persistence.model.DiscountType;
import de.htw_berlin.movation.persistence.model.Goal;
import de.htw_berlin.movation.persistence.model.GoalCategory;
import de.htw_berlin.movation.persistence.model.Movatar;
import de.htw_berlin.movation.persistence.model.MovatarClothes;
import de.htw_berlin.movation.persistence.model.Mrg_User_MovatarClothes;
import de.htw_berlin.movation.persistence.model.User;

@EBean
public class DatabaseTools {
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Assignment, Long> assignmentDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Discount, Long> discountDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<DiscountType, Long> discountTypeDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Goal, Long> goalDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<GoalCategory, Long> goalCategoryDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Movatar, Long> movatarDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<MovatarClothes, Long> movatarClothesDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<User, Long> userDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Mrg_User_MovatarClothes, Long> userMovatarClothesDao;

    void addUserMovatarClothes(final User mergeUser, final MovatarClothes mergeMovatarClothes) throws
                                                                                   SQLException {
        userMovatarClothesDao.createIfNotExists(new Mrg_User_MovatarClothes(){{user = mergeUser; movatarClothes = mergeMovatarClothes;}});
    }
    List<Assignment> getUserAssignments(User user) throws SQLException {
        return assignmentDao.queryBuilder().where().eq("user_id", user.id).query();
    }
    List<MovatarClothes> getUserMovatarClothes(User user) throws SQLException{
        return movatarClothesDao.queryBuilder().join(userMovatarClothesDao.queryBuilder()).where().eq("user_id", user.id).query();
    }
}
