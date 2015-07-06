package com.qitsoft.qitcommons.dao.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * The JUnit rule to execute DbUnits datasets. It executes the datasets declared by annotation {@link DataSet} on test
 * method level or on test class level. All datasets should be in the "dataSets" folder and with extension ".xml".
 */
public class DatabaseTestRule implements MethodRule {

    /**
     * The datasource where to apply datasets.
     */
    private DataSource dataSource;

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                DataSet dataSetOnClass = method.getAnnotation(DataSet.class);
                DataSet dataSetOnMethod = target.getClass().getAnnotation(DataSet.class);
                if (dataSetOnClass != null || dataSetOnMethod != null) {
                    DataSourceDatabaseTester databaseTester = new DataSourceDatabaseTester(dataSource);
                    databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);

                    FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
                    IDataSet[] dataSets = getDataSets(dataSetOnClass, dataSetOnMethod, builder);
                    CompositeDataSet compositeDataSet = new CompositeDataSet(dataSets);

                    databaseTester.setDataSet(compositeDataSet);
                    databaseTester.onSetup();
                }
                base.evaluate();
            }
        };
    }

    /**
     * Returns the datasets to execute retrieved from method of class level declaration.
     * @param dataSetOnClass the {@link DataSet} annotation on the class.
     * @param dataSetOnMethod the {@link DataSet} annotation on the method.
     * @param builder the dataset builder.
     * @return the array of datasets.
     * @throws DataSetException when an expcetion on dataset processing occured.
     * @throws FileNotFoundException when the any resource from the list is not found.
     */
    private IDataSet[] getDataSets(DataSet dataSetOnClass, DataSet dataSetOnMethod, FlatXmlDataSetBuilder builder) throws DataSetException, FileNotFoundException {
        Optional<String> resourceNotFound = Stream.concat(getDataSetStream(dataSetOnClass), getDataSetStream(dataSetOnMethod))
                .distinct()
                .filter(x -> Thread.currentThread().getContextClassLoader().getResource("dataSets/" + x + ".xml") == null)
                .findFirst();

        if (resourceNotFound.isPresent()) {
            throw new FileNotFoundException(MessageFormat.format("Cannot find resource \"{0}\" (dataSets/{0}.xml).", resourceNotFound.get()));
        }

        try {
            return Stream.concat(getDataSetStream(dataSetOnClass), getDataSetStream(dataSetOnMethod))
                                    .distinct()
                                    .map(x -> "dataSets/" + x + ".xml")
                                    .map(x -> Thread.currentThread().getContextClassLoader().getResource(x).getFile())
                                    .map(File::new)
                                    .map(x -> {
                                        try {
                                            return builder.build(x);
                                        } catch (MalformedURLException | DataSetException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .filter(Objects::nonNull)
                                    .toArray(IDataSet[]::new);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Returns the datasets stream from the annotation. If the annotation is null the returns empty stream.
     * @param dataSet
     * @return
     */
    private Stream<String> getDataSetStream(DataSet dataSet) {
        return Optional.ofNullable(dataSet).map(DataSet::value).map(Stream::of).orElseGet(Stream::empty);
    }

    /**
     * Getter for datasource.
     * @return data source.
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Setter for data source.
     * @param dataSource the data source.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
