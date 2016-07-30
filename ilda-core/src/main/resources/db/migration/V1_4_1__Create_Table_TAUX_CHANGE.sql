CREATE TABLE "ILDA"."T_TAUX_CHANGE" (
    ID 						UUID primary key constraint ck_txchange_id_nnull not null,
    VALEUR					numeric(15,5) constraint ck_txchange_val_nnull not null,
	DATE_EFFET				date,
	MOTIF_EFFET				VARCHAR(500),
	ID_DEVISE				UUID REFERENCES "ILDA"."T_DEVISE"(ID),

	VERSION_NUM				bigint,
	AUTEUR_CREATION    		varchar(50),
	AUTEUR_MAJ	    		varchar(50),
	DATE_CREATION			timestamp,
	DATE_MAJ				timestamp
);

-- Index
CREATE UNIQUE INDEX I_TX_CHANGE_CODE_DATE ON "ILDA"."T_TAUX_CHANGE" (ID_DEVISE, DATE_EFFET);

