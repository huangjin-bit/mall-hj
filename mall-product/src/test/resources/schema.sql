CREATE TABLE IF NOT EXISTS pms_category (
    id      BIGINT       NOT NULL,
    name    VARCHAR(64)  NOT NULL,
    parent_id   BIGINT   NOT NULL DEFAULT 0,
    level   TINYINT      NOT NULL,
    icon    VARCHAR(255) NULL,
    sort    INT          NOT NULL DEFAULT 0,
    status  TINYINT      NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pms_brand (
    id              BIGINT       NOT NULL,
    name            VARCHAR(64)  NOT NULL,
    logo            VARCHAR(255) NULL,
    description     VARCHAR(255) NULL,
    sort            INT          NOT NULL DEFAULT 0,
    status          INT          NOT NULL DEFAULT 1,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pms_spu_info (
    id              BIGINT       NOT NULL,
    spu_name        VARCHAR(255) NOT NULL,
    spu_description VARCHAR(500) NULL,
    category_id     BIGINT       NULL,
    brand_id        BIGINT       NULL,
    weight          DECIMAL(10,2) NULL,
    publish_status  INT          NOT NULL DEFAULT 0,
    audit_status    INT          NOT NULL DEFAULT 0,
    new_status      INT          NULL DEFAULT 0,
    recommend_status INT         NULL DEFAULT 0,
    sort            INT          NOT NULL DEFAULT 0,
    create_by       BIGINT       NULL,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pms_sku_info (
    id              BIGINT       NOT NULL,
    spu_id          BIGINT       NOT NULL,
    sku_name        VARCHAR(255) NOT NULL,
    sku_desc        VARCHAR(500) NULL,
    price           DECIMAL(10,2) NULL,
    original_price  DECIMAL(10,2) NULL,
    sku_img         VARCHAR(255) NULL,
    weight          DECIMAL(10,2) NULL,
    publish_status  INT          NOT NULL DEFAULT 0,
    sort            INT          NOT NULL DEFAULT 0,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pms_spu_comment (
    id              BIGINT       NOT NULL,
    sku_id          BIGINT       NULL,
    spu_id          BIGINT       NULL,
    spu_name        VARCHAR(255) NULL,
    member_id       BIGINT       NULL,
    member_nickname VARCHAR(64)  NULL,
    member_avatar   VARCHAR(255) NULL,
    sku_attributes  VARCHAR(255) NULL,
    star            INT          NULL,
    content         VARCHAR(500) NULL,
    resources       VARCHAR(500) NULL,
    comment_type    INT          NULL DEFAULT 0,
    show_status     INT          NULL DEFAULT 1,
    likes_count     INT          NULL DEFAULT 0,
    reply_count     INT          NULL DEFAULT 0,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pms_comment_replay (
    id              BIGINT       NOT NULL,
    comment_id      BIGINT       NULL,
    reply_id        BIGINT       NULL,
    member_id       BIGINT       NULL,
    member_nickname VARCHAR(64)  NULL,
    member_avatar   VARCHAR(255) NULL,
    content         VARCHAR(500) NULL,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
