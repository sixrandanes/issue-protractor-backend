CREATE TABLE "ILDA"."T_INCOTERM" (
    ID 						UUID primary key constraint ck_incoterm_id_nnull not null,
    CODE 					VARCHAR(3) constraint ck_incoterm_code_nnull not null,
    DESIGNATION				VARCHAR(50) constraint ck_incoterm_des_nnull not null,
	DATE_EFFET				date,
	MOTIF_EFFET				VARCHAR(500),
	DATE_FIN_EFFET			date,
	MOTIF_FIN_EFFET			VARCHAR(500),

	VERSION_NUM				bigint,
	AUTEUR_CREATION    		VARCHAR(50),
	AUTEUR_MAJ	    		VARCHAR(50),
	DATE_CREATION			timestamp,
	DATE_MAJ				timestamp
);

-- Index
CREATE UNIQUE INDEX I_INCOTERM_CODE_DATE ON "ILDA"."T_INCOTERM" (CODE, DATE_EFFET, DATE_FIN_EFFET);
CREATE UNIQUE INDEX I_INCOTERM_DESIGNATION_DATE ON "ILDA"."T_INCOTERM" (DESIGNATION, DATE_EFFET, DATE_FIN_EFFET);
