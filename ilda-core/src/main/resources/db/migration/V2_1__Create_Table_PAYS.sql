CREATE TABLE "ILDA"."T_PAYS" (
    ID                      UUID primary key constraint ck_pays_id_nnull not null,
    CODE2                   VARCHAR(2) constraint ck_pays_code2_nnull not null,
    CODE3                   VARCHAR(3) constraint ck_pays_code3_nnull not null,
    DESIGNATION             VARCHAR(50) constraint ck_pays_des_nnull not null,
    DATE_EFFET              date,
    DATE_FIN_EFFET          date,

    VERSION_NUM             bigint,
    AUTEUR_CREATION         varchar(50),
    AUTEUR_MAJ              varchar(50),
    DATE_CREATION           timestamp,
    DATE_MAJ                timestamp
);

-- Index
CREATE UNIQUE INDEX I_PAYS_CODE2_DATE ON "ILDA"."T_PAYS" (CODE2, DATE_EFFET, DATE_FIN_EFFET);
CREATE UNIQUE INDEX I_PAYS_CODE3_DATE ON "ILDA"."T_PAYS" (CODE3, DATE_EFFET, DATE_FIN_EFFET);