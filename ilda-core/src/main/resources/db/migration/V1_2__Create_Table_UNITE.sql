CREATE TABLE "ILDA"."T_UNITE" (
    ID 						UUID primary key constraint ck_unite_id_nnull not null,
    CODE 					VARCHAR(5) constraint ck_unite_code_nnull not null,
    DESIGNATION				VARCHAR(50) constraint ck_unite_des_nnull not null,
	DATE_EFFET				date,
	DATE_FIN_EFFET			date,

	VERSION_NUM				bigint,
	AUTEUR_CREATION    		varchar(50),
	AUTEUR_MAJ	    		varchar(50),
	DATE_CREATION			timestamp,
	DATE_MAJ				timestamp
);

-- Index
CREATE UNIQUE INDEX I_UNITE_CODE_DATE ON "ILDA"."T_UNITE" (CODE, DATE_EFFET, DATE_FIN_EFFET);
