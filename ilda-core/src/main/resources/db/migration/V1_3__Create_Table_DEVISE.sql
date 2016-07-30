CREATE TABLE "ILDA"."T_DEVISE" (
    ID 						UUID primary key constraint ck_devise_id_nnull not null,
    CODE 					VARCHAR(3) constraint ck_devise_code_nnull not null,
    DESIGNATION				VARCHAR(50) constraint ck_devise_des_nnull not null,
	DATE_EFFET				date,
	DATE_FIN_EFFET			date,

	VERSION_NUM				bigint,
	AUTEUR_CREATION    		varchar(50),
	AUTEUR_MAJ	    		varchar(50),
	DATE_CREATION			timestamp,
	DATE_MAJ				timestamp
);

-- Index
CREATE UNIQUE INDEX I_DEVISE_CODE_DATE ON "ILDA"."T_DEVISE" (CODE, DATE_EFFET, DATE_FIN_EFFET);
