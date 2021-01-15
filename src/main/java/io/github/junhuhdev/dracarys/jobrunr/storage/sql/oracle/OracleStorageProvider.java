package io.github.junhuhdev.dracarys.jobrunr.storage.sql.oracle;


import io.github.junhuhdev.dracarys.jobrunr.storage.sql.common.DefaultSqlStorageProvider;

import javax.sql.DataSource;

public class OracleStorageProvider extends DefaultSqlStorageProvider {

    public OracleStorageProvider(DataSource dataSource) {
        super(dataSource);
    }

}
