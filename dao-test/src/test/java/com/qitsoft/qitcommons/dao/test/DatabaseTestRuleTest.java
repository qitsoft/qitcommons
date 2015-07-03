package com.qitsoft.qitcommons.dao.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.qitsoft.qitcommons.test.UnitTestHelper;
import org.dbunit.dataset.DataSetException;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTestRuleTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DatabaseTestRule testRule = new DatabaseTestRule();

    private File dbFile;

    @Mock
    private FrameworkMethod method;

    @Mock
    private Statement statement;

    @Mock
    private DataSet dataSet;

    private TestWithoutDatasetAnnotation testWithoutDatasetAnnotation = new TestWithoutDatasetAnnotation();

    private TestWithDatasetAnnotation testWithDatasetAnnotation = new TestWithDatasetAnnotation();

    private JdbcDataSource dataSource;

    @Before
    public void setUp() throws IOException, SQLException, URISyntaxException {
        dataSource = new JdbcDataSource();
        dbFile = File.createTempFile("test-d2-db-", "");
        dbFile.delete();
        dataSource.setURL("jdbc:h2:" + dbFile.getAbsolutePath());
        dataSource.setUser("sa");
        dataSource.setPassword("sa");
        testRule.setDataSource(dataSource);

        Connection connection = dataSource.getConnection();

        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("create-test-tables.sql"));
        connection.createStatement().execute(properties.getProperty("test"));
        connection.close();
    }

    @After
    public void tearDown() throws IOException {
        Files.list(dbFile.getParentFile().toPath())
                .filter(x -> x.getFileName().toString().startsWith(dbFile.getName())).forEach((path) -> {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testExecuteDatasourceOnMethodLevel() throws Throwable {
        when(method.getAnnotation(DataSet.class)).thenReturn(dataSet);
        when(dataSet.value()).thenReturn(new String[]{"test1"});

        testRule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Connection connection = dataSource.getConnection();
                ResultSet resultSet = connection.createStatement().executeQuery("select id, name, age from test order by id");
                assertThat(resultSet.next(), equalTo(true));
                assertThat(resultSet.getInt(1), equalTo(1));
                assertThat(resultSet.getString(2), equalTo("Vasya"));
                assertThat(resultSet.getInt(3), equalTo(28));
                assertThat(resultSet.next(), equalTo(true));
                assertThat(resultSet.getInt(1), equalTo(2));
                assertThat(resultSet.getString(2), equalTo("Petya"));
                assertThat(resultSet.getInt(3), equalTo(23));
                assertThat(resultSet.next(), equalTo(false));

                resultSet.close();
                connection.close();
            }
        }, method, testWithoutDatasetAnnotation).evaluate();
    }

    @Test
    public void testExecuteDatasourceOnClassLevel() throws Throwable {
        when(method.getAnnotation(DataSet.class)).thenReturn(null);

        testRule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Connection connection = dataSource.getConnection();
                ResultSet resultSet = connection.createStatement().executeQuery("select id, name, age from test order by id");
                assertThat(resultSet.next(), equalTo(true));
                assertThat(resultSet.getInt(1), equalTo(1));
                assertThat(resultSet.getString(2), equalTo("Vasya"));
                assertThat(resultSet.getInt(3), equalTo(28));
                assertThat(resultSet.next(), equalTo(true));
                assertThat(resultSet.getInt(1), equalTo(2));
                assertThat(resultSet.getString(2), equalTo("Petya"));
                assertThat(resultSet.getInt(3), equalTo(23));
                assertThat(resultSet.next(), equalTo(false));

                resultSet.close();
                connection.close();
            }
        }, method, testWithDatasetAnnotation).evaluate();
    }

    @Test
    public void testVerifyExecuted() throws Throwable {
        testRule.apply(statement, method, testWithoutDatasetAnnotation).evaluate();
        verify(statement).evaluate();
    }

    @Test
    public void testExecuteDatasourceClearsPreviousData() throws Throwable {
        when(method.getAnnotation(DataSet.class)).thenReturn(dataSet);
        when(dataSet.value()).thenReturn(new String[]{"test1"}).thenReturn(new String[]{"test2"});

        testRule.apply(statement, method, testWithoutDatasetAnnotation).evaluate();

        testRule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Connection connection = dataSource.getConnection();
                ResultSet resultSet = connection.createStatement().executeQuery("select id, name, age from test order by id");
                assertThat(resultSet.next(), equalTo(true));
                assertThat(resultSet.getInt(1), equalTo(3));
                assertThat(resultSet.getString(2), equalTo("Tolya"));
                assertThat(resultSet.getInt(3), equalTo(35));
                assertThat(resultSet.next(), equalTo(true));
                assertThat(resultSet.getInt(1), equalTo(4));
                assertThat(resultSet.getString(2), equalTo("Misha"));
                assertThat(resultSet.getInt(3), equalTo(18));
                assertThat(resultSet.next(), equalTo(false));

                resultSet.close();
                connection.close();
            }
        }, method, testWithoutDatasetAnnotation).evaluate();
    }

    @Test
    public void testExecuteWithoutAnnotation() throws Throwable {
        testRule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Connection connection = dataSource.getConnection();
                ResultSet resultSet = connection.createStatement().executeQuery("select id, name, age from test order by id");
                assertThat(resultSet.next(), equalTo(false));

                resultSet.close();
                connection.close();
            }
        }, method, new Object()).evaluate();
    }

    @Test
    public void testExecuteCannotFindResource() throws Throwable {
        when(method.getAnnotation(DataSet.class)).thenReturn(dataSet);
        when(dataSet.value()).thenReturn(new String[]{"test-not-found"});

        expectedException.expect(FileNotFoundException.class);
        expectedException.expectMessage("Cannot find resource \"test-not-found\" (dataSets/test-not-found.xml).");

        testRule.apply(statement, method, testWithoutDatasetAnnotation).evaluate();
    }

    @Test
    public void testExecuteInvalidDataSet() throws Throwable {
        when(method.getAnnotation(DataSet.class)).thenReturn(dataSet);
        when(dataSet.value()).thenReturn(new String[]{"invalid"});

        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(instanceOf(DataSetException.class));

        testRule.apply(statement, method, testWithoutDatasetAnnotation).evaluate();
    }

    @Test
    public void testExecuteInvalidDataSetWithNPE() throws Throwable {
        when(method.getAnnotation(DataSet.class)).thenReturn(dataSet);
        when(dataSet.value()).thenReturn(new String[]{"invalid-with-npe"});

        expectedException.expect(NullPointerException.class);

        testRule.apply(statement, method, testWithoutDatasetAnnotation).evaluate();
    }

    @Test
    public void testProperties() throws Exception {
        UnitTestHelper.testProperties(testRule);
    }

    private static class TestWithoutDatasetAnnotation {}

    @DataSet("test1")
    private static class TestWithDatasetAnnotation {}
}