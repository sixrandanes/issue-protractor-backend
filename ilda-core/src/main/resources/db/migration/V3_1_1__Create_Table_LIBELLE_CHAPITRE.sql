CREATE TABLE "ILDA"."T_LIBELLE_CHAPITRE" (
    ID 						UUID primary key constraint ck_txchange_id_nnull not null,
    DESIGNATION				VARCHAR(500) constraint ck_libchapitre_desi_nnull not null,
	DATE_EFFET				date,
	MOTIF_EFFET				VARCHAR(500),
	ID_CHAPITRE				UUID REFERENCES "ILDA"."T_CHAPITRE"(ID),
	NOTES					text,

	VERSION_NUM				bigint,
	AUTEUR_CREATION    		varchar(50),
	AUTEUR_MAJ	    		varchar(50),
	DATE_CREATION			timestamp,
	DATE_MAJ				timestamp
);

-- Index
CREATE UNIQUE INDEX I_LIB_CHAP_CHAP_DESIGNATION ON "ILDA"."T_LIBELLE_CHAPITRE" (DESIGNATION);

ALTER TABLE "ILDA"."T_LIBELLE_CHAPITRE" ADD CONSTRAINT lib_chap_uk_desi UNIQUE (DESIGNATION);

ALTER TABLE "ILDA"."T_LIBELLE_CHAPITRE" ADD CONSTRAINT lib_chap_uk_dateeffet_chapitre UNIQUE (DATE_EFFET, ID_CHAPITRE);
