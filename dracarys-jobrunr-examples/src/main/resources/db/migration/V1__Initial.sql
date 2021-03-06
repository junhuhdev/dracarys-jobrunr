CREATE TABLE FAMILY (
    ID SERIAL NOT NULL PRIMARY KEY,
    SSN CHARACTER VARYING NOT NULL,
    TYPE CHARACTER VARYING NOT NULL,
    CREATED timestamp without time zone DEFAULT now()
);

ALTER SEQUENCE FAMILY_ID_SEQ RESTART WITH 101;
ALTER TABLE FAMILY REPLICA IDENTITY FULL;

CREATE TABLE ASSET (
    ID SERIAL NOT NULL PRIMARY KEY,
    MSISDN CHARACTER VARYING NOT NULL,
    CREATED timestamp without time zone DEFAULT now()
);

ALTER SEQUENCE ASSET_ID_SEQ RESTART WITH 1001;
ALTER TABLE ASSET REPLICA IDENTITY FULL;

CREATE TABLE COMMAND (
    ID SERIAL NOT NULL PRIMARY KEY,
    COMMAND_CLASS VARCHAR NOT NULL,
    COMMAND VARCHAR NOT NULL,
    REFERENCE_ID VARCHAR NOT NULL,
    JOB_ID VARCHAR,
    HISTORY TEXT,
    RETRY_COUNT NUMERIC,
    STATUS VARCHAR NOT NULL,
    CREATED timestamp without time zone DEFAULT now()
);

ALTER SEQUENCE COMMAND_ID_SEQ RESTART WITH 2001;
ALTER TABLE COMMAND REPLICA IDENTITY FULL;

CREATE TABLE USERS (
    EMAIL CHARACTER VARYING NOT NULL PRIMARY KEY,
    NAME CHARACTER VARYING NOT NULL,
    PASSWORD CHARACTER VARYING NOT NULL,
    CREATED timestamp without time zone DEFAULT now()
);

CREATE TABLE ACCOUNT (
    EMAIL CHARACTER VARYING NOT NULL PRIMARY KEY,
    AMOUNT decimal(12,2) NOT NULL,
    CREATED timestamp without time zone DEFAULT now()
);