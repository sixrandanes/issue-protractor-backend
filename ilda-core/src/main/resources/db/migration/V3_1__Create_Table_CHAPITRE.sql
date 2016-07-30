CREATE TABLE "ILDA"."T_SECTION" (
    ID 						UUID primary key constraint ck_chapitre_id_nnull not null,
    NUMERO_SECTION 			VARCHAR(3),
    DATE_EFFET				date,
    DATE_FIN_EFFET			date,
    MOTIF_EFFET				VARCHAR(500),
    MOTIF_FIN_EFFET			VARCHAR(500),
    
	VERSION_NUM				bigint,
	AUTEUR_CREATION    		VARCHAR(50),
	AUTEUR_MAJ	    		VARCHAR(50),
	DATE_CREATION			timestamp,
	DATE_MAJ				timestamp
);

CREATE TABLE "ILDA"."T_CHAPITRE" (
    ID 						UUID primary key constraint ck_chapitre_id_nnull not null,
    NUMERO_CHAPITRE 		smallint constraint ck_chapitre_numero_nnull not null,
	DATE_EFFET				date,
	MOTIF_EFFET				VARCHAR(500),
	DATE_FIN_EFFET			date,
	MOTIF_FIN_EFFET			VARCHAR(500),
	ID_SECTION				UUID REFERENCES "ILDA"."T_SECTION"(ID),


	VERSION_NUM				bigint,
	AUTEUR_CREATION    		varchar(50),
	AUTEUR_MAJ	    		varchar(50),
	DATE_CREATION			timestamp,
	DATE_MAJ				timestamp
);

-- Index
CREATE UNIQUE INDEX I_CHAPITRE_NUM_DATE ON "ILDA"."T_CHAPITRE" (ID_SECTION, NUMERO_CHAPITRE, DATE_EFFET);