package com.cms.basic.unit;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.xml.sax.InputSource;

import java.io.*;
import java.sql.SQLException;

public class AbstractDbUnitTestCase {

    public static IDatabaseConnection dbunitCon;
    private File temFile;

    @BeforeClass
    public static void init() throws SQLException, DatabaseUnitException {
        dbunitCon = new DatabaseConnection(DbUtil.getConnection());
    }

    protected IDataSet createDataSet(String name) throws DataSetException {
        InputStream is = AbstractDbUnitTestCase.class.getClassLoader().getResourceAsStream(name + ".xml");
        Assert.assertNotNull("dbunit的基本数据不存在！",is);
        return new FlatXmlDataSet(new FlatXmlProducer(new InputSource(is)));
    }

    protected void backupAllTable() throws SQLException, IOException, DataSetException {
        IDataSet ds = dbunitCon.createDataSet();
        writeBackFile(ds);
    }

    private void writeBackFile(IDataSet is) throws IOException, DataSetException {
        temFile = File.createTempFile("back","xml");
        FlatXmlDataSet.write(is,new FileWriter(temFile));
    }

    protected void backupCustomTable(String[] tname) throws DataSetException, IOException {
        QueryDataSet ds = new QueryDataSet(dbunitCon);
        for (String s : tname) {
            ds.addTable(s);
        }
        writeBackFile(ds);

    }

    protected void resumeTable() throws FileNotFoundException, DatabaseUnitException, SQLException {
        IDataSet is = new FlatXmlDataSet(new FlatXmlProducer(new InputSource(new FileInputStream(temFile))));
        DatabaseOperation.CLEAN_INSERT.execute(dbunitCon,is);
    }

    @AfterClass
    public static void destory(){
        if (dbunitCon != null) {
            try {
                dbunitCon.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
