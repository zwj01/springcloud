package com.cms.basic.dao;

import com.cms.basic.model.User;
import com.cms.basic.unit.AbstractDbUnitTestCase;
import com.cms.basic.unit.DbUtil;
import com.cms.basic.unit.EntitiesHelper;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestUserDao extends AbstractDbUnitTestCase {

    @Inject
    private IUserDao iUserDao;

    @Before
    public void setUp() throws SQLException, IOException, DataSetException {
        //this.backupAllTable();
    }

    @Test
    public void testLoad() throws DatabaseUnitException, SQLException {
        //IDatabaseConnection con = new DatabaseConnection(DbUtil.getConnection(), "t_user");
        IDataSet is = createDataSet("t_user");
        DatabaseOperation.CLEAN_INSERT.execute(dbunitCon,is);
        User u = iUserDao.load(1);
        EntitiesHelper.assertUser(u);
    }

    @After
    public void tearDown() throws FileNotFoundException, DatabaseUnitException, SQLException {
        //this.resumeTable();
    }
}
