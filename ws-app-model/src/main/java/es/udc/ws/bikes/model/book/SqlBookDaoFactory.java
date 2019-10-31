package es.udc.ws.bikes.model.book;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlBookDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlSaleDaoFactory.className";
    private static SqlBookDao dao = null;

    private SqlBookDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlBookDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlBookDao) daoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlBookDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}
