CREATE TABLE "ILDA"."T_USER" (
    ID 						UUID primary key not null,
    SUB 					TEXT,
    NAME 					VARCHAR(50),
    VERSION_NUM				bigint,
	AUTEUR_CREATION    		varchar(50),
	AUTEUR_MAJ	    		varchar(50),
	DATE_CREATION			timestamp,
	DATE_MAJ				timestamp
);
