package io.github.junhuhdev.dracarys.jobrunr.storage.sql.mariadb;


import io.github.junhuhdev.dracarys.jobrunr.storage.sql.common.DefaultSqlStorageProvider;

import javax.sql.DataSource;

public class MariaDbStorageProvider extends DefaultSqlStorageProvider {

    public MariaDbStorageProvider(DataSource dataSource) {
        super(dataSource);
    }

}
