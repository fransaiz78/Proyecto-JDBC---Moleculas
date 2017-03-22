DROP TABLE Elementos cascade constraint;
DROP TABLE Moleculas cascade constraint;
DROP TABLE Composicion cascade constraint;
DROP SEQUENCE seq_molId;

CREATE TABLE Elementos (
  simbolo varchar(3),
  nombre varchar(20) UNIQUE NOT NULL,
  pesoAtomico integer NOT NULL,
  PRIMARY KEY(simbolo)
);

CREATE TABLE Moleculas (
  id integer,
  nombre varchar(20) UNIQUE NOT NULL,
  pesoMolecular integer NOT NULL,
  formula varchar(20) UNIQUE NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE Composicion (
  simbolo varchar(3),
  idMolecula integer,
  nroAtomos integer NOT NULL,  
  PRIMARY KEY(simbolo, idMolecula),
  FOREIGN KEY(simbolo) REFERENCES Elementos(simbolo),
  FOREIGN KEY(IdMolecula) REFERENCES Moleculas(id)
);

CREATE SEQUENCE seq_molId; 


insert into Elementos(simbolo, nombre, pesoAtomico) values ('H','Hidrogeno', 1);
insert into Elementos(simbolo, nombre, pesoAtomico) values('O','Oxigeno', 18);

insert into Moleculas(id, nombre, pesoMolecular, formula) values(seq_molId.nextval, 'Agua', 20, 'H2O');

insert into Composicion(simbolo, idMolecula, nroAtomos) values('H', seq_molId.currval, 2);
insert into Composicion(simbolo, idMolecula, nroAtomos) values('O', seq_molId.currval, 1);


/*DELETE FROM COMPOSICION WHERE idMolecula=1;
DELETE FROM Moleculas WHERE id=1;
delete from Moleculas where nombre='Agua';*/

--SELECT elementos.simbolo, elementos.nombre, elementos.pesoatomico, composicion.nroatomos FROM Elementos inner join Composicion ON Elementos.simbolo = composicion.simbolo

--update Composicion set nroatomos=5 where idmolecula=962 and simbolo='H';

/*
select * from Elementos;
select * from Moleculas;
select * from Composicion;

select seq_molId.nextval from Composicion;

*/

exit;