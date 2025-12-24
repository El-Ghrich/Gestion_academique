DROP TABLE IF EXISTS eleve_cours CASCADE;
DROP TABLE IF EXISTS dossier CASCADE;
DROP TABLE IF EXISTS eleve CASCADE;
DROP TABLE IF EXISTS cours CASCADE;
DROP TABLE IF EXISTS filiere CASCADE;

CREATE TABLE filiere (
     id UUID PRIMARY KEY,
     code VARCHAR(50) NOT NULL UNIQUE,
     nom VARCHAR(100) NOT NULL,
     description TEXT
);

CREATE TABLE cours (
       id UUID PRIMARY KEY,
       code VARCHAR(50) NOT NULL UNIQUE,
       intitule VARCHAR(100) NOT NULL
);

CREATE TABLE eleve (
       id UUID PRIMARY KEY,
       matricule VARCHAR(50) NOT NULL UNIQUE,
       nom VARCHAR(50) NOT NULL,
       prenom VARCHAR(50) NOT NULL,
       email VARCHAR(100),
       status VARCHAR(20) NOT NULL,
       filiere_id UUID,
       CONSTRAINT fk_eleve_filiere FOREIGN KEY (filiere_id) REFERENCES filiere(id) ON DELETE SET NULL
);

CREATE TABLE dossieradministratif (
         id UUID PRIMARY KEY,
         numero_inscription VARCHAR(50) NOT NULL UNIQUE,
         date_creation DATE NOT NULL,
         eleve_id UUID UNIQUE NOT NULL, -- UNIQUE assure la relation 1-1
         CONSTRAINT fk_dossier_eleve FOREIGN KEY (eleve_id) REFERENCES eleve(id) ON DELETE CASCADE
);


CREATE TABLE eleve_cours (
         eleve_id UUID NOT NULL,
         cours_id UUID NOT NULL,
         PRIMARY KEY (eleve_id, cours_id),
         CONSTRAINT fk_association_eleve FOREIGN KEY (eleve_id) REFERENCES eleve(id) ON DELETE CASCADE,
         CONSTRAINT fk_association_cours FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE
);