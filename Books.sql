create table AUTHORING_ENTITIES
(
    EMAIL VARCHAR(30) not null
        primary key,
    AUTHORING_ENTITY_TYPE VARCHAR(31),
    NAME VARCHAR(80) not null,
    HEAD_WRITER VARCHAR(80),
    YEAR_FORMED INTEGER
);

create table AD_HOC_TEAMS_MEMBER
(
    INDIVIDUAL_AUTHORS_EMAIL VARCHAR(30) not null
        constraint DHCTMSMNDVDLTHRSML
            references AUTHORING_ENTITIES,
    AD_HOC_TEAMS_EMAIL VARCHAR(30) not null
        constraint DHCTMSMMBRDHCTMSML
            references AUTHORING_ENTITIES,
    primary key (INDIVIDUAL_AUTHORS_EMAIL, AD_HOC_TEAMS_EMAIL)
);

create table PUBLISHERS
(
    NAME VARCHAR(80) not null
        primary key,
    EMAIL VARCHAR(80) not null
        unique,
    PHONE VARCHAR(24) not null
        unique
);

create table BOOKS
(
    ISBN VARCHAR(17) not null
        primary key,
    TITLE VARCHAR(80) not null,
    YEAR_PUBLISHED INTEGER not null,
    AUTHORING_ENTITY_NAME VARCHAR(30)
        constraint BKSTHRNGENTITYNAME
            references AUTHORING_ENTITIES,
    PUBLISHER_NAME VARCHAR(80)
        constraint BOOKSPUBLISHERNAME
            references PUBLISHERS,
    unique (TITLE, PUBLISHER_NAME),
    unique (TITLE, AUTHORING_ENTITY_NAME)
);