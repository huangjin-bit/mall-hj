CREATE TABLE IF NOT EXISTS wms_ware_sku (
    id              BIGINT       NOT NULL,
    ware_id         BIGINT       NULL,
    sku_id          BIGINT       NULL,
    spu_id          BIGINT       NULL,
    stock           INT          NOT NULL DEFAULT 0,
    stock_locked    INT          NOT NULL DEFAULT 0,
    stock_total     INT          NULL DEFAULT 0,
    low_stock       INT          NULL DEFAULT 0,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS wms_ware_order_task (
    id              BIGINT       NOT NULL,
    order_sn        VARCHAR(64)  NULL,
    order_id        BIGINT       NULL,
    ware_id         BIGINT       NULL,
    task_status     INT          NOT NULL DEFAULT 0,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS wms_ware_order_task_detail (
    id                  BIGINT       NOT NULL,
    task_id             BIGINT       NULL,
    ware_id             BIGINT       NULL,
    sku_id              BIGINT       NULL,
    sku_name            VARCHAR(255) NULL,
    lock_quantity       INT          NULL,
    task_detail_status  INT          NOT NULL DEFAULT 0,
    create_time         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS wms_stock_log (
    id              BIGINT       NOT NULL,
    ware_id         BIGINT       NULL,
    sku_id          BIGINT       NULL,
    change_type     VARCHAR(32)  NULL,
    change_amount   INT          NULL,
    before_stock    INT          NULL,
    after_stock     INT          NULL,
    before_locked   INT          NULL,
    after_locked    INT          NULL,
    order_sn        VARCHAR(64)  NULL,
    purchase_id     BIGINT       NULL,
    remark          VARCHAR(500) NULL,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
