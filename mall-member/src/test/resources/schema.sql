CREATE TABLE IF NOT EXISTS ums_member (
    id              BIGINT       NOT NULL,
    level_id        BIGINT       NOT NULL DEFAULT 1,
    username        VARCHAR(64)  NULL,
    nickname        VARCHAR(64)  NULL,
    phone           VARCHAR(20)  NULL,
    email           VARCHAR(64)  NULL,
    avatar          VARCHAR(255) NULL,
    gender          INT          NULL DEFAULT 0,
    birthday        DATE         NULL,
    integration     INT          NOT NULL DEFAULT 0,
    growth          INT          NOT NULL DEFAULT 0,
    age_group       VARCHAR(32)  NULL,
    occupation      VARCHAR(64)  NULL,
    price_sensitivity INT        NULL,
    favorite_categories VARCHAR(255) NULL,
    shopping_preference VARCHAR(64) NULL,
    status          INT          NOT NULL DEFAULT 1,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ums_member_auth (
    id              BIGINT       NOT NULL,
    member_id       BIGINT       NOT NULL,
    identity_type   VARCHAR(32)  NOT NULL,
    identifier      VARCHAR(128) NOT NULL,
    credential      VARCHAR(255) NULL,
    verified        INT          NOT NULL DEFAULT 0,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ums_member_level (
    id                      BIGINT       NOT NULL,
    name                    VARCHAR(64)  NOT NULL,
    growth_point            INT          NOT NULL DEFAULT 0,
    default_status          INT          NOT NULL DEFAULT 0,
    free_freight            DECIMAL(10,2) NULL DEFAULT 0,
    comment_growth_point    INT          NULL DEFAULT 0,
    priviledge_free_freight DECIMAL(10,2) NULL DEFAULT 0,
    priviledge_member_price DECIMAL(10,2) NULL DEFAULT 0,
    priviledge_birthday     INT          NULL DEFAULT 0,
    note                    VARCHAR(255) NULL,
    create_time             DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time             DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ums_member_receive_address (
    id              BIGINT       NOT NULL,
    member_id       BIGINT       NOT NULL,
    name            VARCHAR(64)  NOT NULL,
    phone           VARCHAR(20)  NOT NULL,
    province        VARCHAR(64)  NULL,
    city            VARCHAR(64)  NULL,
    district        VARCHAR(64)  NULL,
    detail_address  VARCHAR(255) NOT NULL,
    postal_code     VARCHAR(20)  NULL,
    default_status  INT          NOT NULL DEFAULT 0,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ums_integration_record (
    id              BIGINT       NOT NULL,
    member_id       BIGINT       NOT NULL,
    change_amount   INT          NOT NULL,
    current_amount  INT          NOT NULL,
    source_type     VARCHAR(32)  NULL,
    source_id       BIGINT       NULL,
    remark          VARCHAR(255) NULL,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ums_growth_record (
    id              BIGINT       NOT NULL,
    member_id       BIGINT       NOT NULL,
    change_amount   INT          NOT NULL,
    current_amount  INT          NOT NULL,
    source_type     VARCHAR(32)  NULL,
    source_id       BIGINT       NULL,
    remark          VARCHAR(255) NULL,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
