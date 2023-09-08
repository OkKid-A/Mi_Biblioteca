--Creacion del usuario para la base de datos--

    CREATE USER 'rootdba'@'localhost'
IDENTIFIED WITH mysql_native_password BY 'abc123';

GRANT ALL PRIVILEGES 
ON 'Mi_Biblioteca'*
TO 'rootdba'@'localhost';

--creando la base de datos--

CREATE SCHEMA IF NOT EXISTS 'Mi_Biblioteca' ;

--seleccionamos la base de datos--
USE 'Mi_Biblioteca' ;

--creacion de las tablas--

create table if not exists biblioteca
(
    id_biblioteca int unsigned not null,
    nombre        varchar(45)  not null,
    direccion     varchar(255) not null,
    primary key (id_biblioteca)
);

create table if not exists genero
(
    id       int         not null,
    nombre   varchar(45) not null,
    sinopsis text        not null,
    primary key (id),
    constraint genero_pk
        unique (nombre)
);

create table if not exists libro
(
    autor     varchar(25)  not null,
    isbn      int unsigned not null,
    precio    double       not null,
    genero_id int          not null,
    nombre    varchar(45)  not null,
    primary key (isbn),
    constraint fk_Libro_genero
        foreign key (genero_id) references genero (id)
);

create table if not exists existencia
(
    id_biblioteca int unsigned not null,
    cantidad      int unsigned not null,
    isbn          int unsigned not null,
    disponibles   int unsigned not null,
    primary key (id_biblioteca, isbn),
    constraint fk_biblioteca
        foreign key (id_biblioteca) references biblioteca (id_biblioteca),
    constraint fk_libro_existencia
        foreign key (isbn) references libro (isbn)
);

create index fk_libro_idx
    on existencia (isbn);

create index fk_Libro_genero_idx
    on libro (genero_id);

create table if not exists parametros
(
    nombre varchar(45) not null,
    valor  varchar(25) not null,
    primary key (nombre)
);

create table if not exists usuario
(
    id       int auto_increment
        primary key,
    username varchar(45) not null,
    nombre   varchar(60) not null,
    password varchar(32) not null,
    tipo     int         not null,
    email    varchar(45) not null,
    constraint id_UNIQUE
        unique (id, username)
);

create table if not exists admin
(
    admin_id   int not null,
    usuario_id int not null,
    primary key (admin_id),
    constraint admin_usuario_id_fk
        foreign key (usuario_id) references usuario (id)
);

create table if not exists cliente
(
    id_cliente int auto_increment,
    saldo      double unsigned zerofill  null,
    suspendido tinyint unsigned zerofill not null,
    subscrito  tinyint unsigned zerofill not null,
    usuario_id int                       not null,
    primary key (id_cliente, usuario_id),
    constraint id_cliente_UNIQUE
        unique (id_cliente),
    constraint cliente_usuario_id_fk
        foreign key (usuario_id) references usuario (id)
);

create definer = rootdba@localhost trigger trans_creator
    after update
    on cliente
    for each row
BEGIN
    IF OLD.saldo<>NEW.saldo THEN
    INSERT INTO transaccion (valor, id_cliente, fecha)
        VALUES (new.saldo - OLD.saldo, OLD.id_cliente,CURDATE());
    END IF;
END;

create table if not exists recepcionista
(
    id_recepcionista  int          not null,
    puesto_biblioteca int unsigned not null,
    usuario_id        int          not null,
    suspendido        tinyint(1)   not null,
    primary key (id_recepcionista, puesto_biblioteca),
    constraint recepcionista_pk
        unique (usuario_id),
    constraint recepcionista_biblioteca_id_biblioteca_fk
        foreign key (puesto_biblioteca) references biblioteca (id_biblioteca),
    constraint recepcionista_usuario_id_fk
        foreign key (usuario_id) references usuario (id)
);

create index fk_biblio_idx
    on recepcionista (puesto_biblioteca);

create table if not exists resumir
(
    id          int auto_increment
        primary key,
    cliente_id  int         not null,
    descripcion text        not null,
    estado      varchar(15) not null,
    constraint resumir_cliente_id_cliente_fk
        foreign key (cliente_id) references cliente (id_cliente)
);

create table if not exists suspensiones
(
    id_cliente   int  not null,
    fecha_inicio date not null,
    fecha_final  date not null,
    numero       int auto_increment
        primary key,
    constraint suspensiones_cliente_id_cliente_fk
        foreign key (id_cliente) references cliente (id_cliente)
);

create table if not exists transaccion
(
    valor      double not null,
    id_cliente int    not null,
    id         int auto_increment
        primary key,
    fecha      date   not null,
    constraint transaccion_cliente_id_cliente_fk
        foreign key (id_cliente) references cliente (id_cliente)
);

create table if not exists transportista
(
    transportista_id int        not null,
    usuario_id       int        not null,
    suspendido       tinyint(1) null,
    primary key (transportista_id, usuario_id),
    constraint transportista_usuario_id_fk
        foreign key (usuario_id) references usuario (id)
);

create table if not exists encargo
(
    numero_encargo   int auto_increment
        primary key,
    estado           varchar(15)  not null,
    transportista_id int          not null,
    fecha            date         not null,
    biblio_origen    int unsigned not null,
    tipo_encargo     varchar(12)  null,
    constraint encargo_transportista_transportista_id_fk
        foreign key (transportista_id) references transportista (transportista_id),
    constraint fk_origen
        foreign key (biblio_origen) references biblioteca (id_biblioteca)
);

create index fk_driver_idx
    on encargo (transportista_id);

create index fk_origen_idx
    on encargo (biblio_origen);

create table if not exists prestamo
(
    id_prestamo      int auto_increment
        primary key,
    cliente_id       int          not null,
    isbn             int unsigned not null,
    biblio_origen    int unsigned not null,
    dias_reservados  int          not null,
    fecha_creacion   date         not null,
    recepcionista_id int          null,
    tipo_entrega     varchar(15)  null,
    transportista_id int          null,
    estado           varchar(35)  null,
    constraint id_prestamo_UNIQUE
        unique (id_prestamo),
    constraint fk_biblioteca_origen
        foreign key (biblio_origen) references biblioteca (id_biblioteca),
    constraint fk_libro_prestado
        foreign key (isbn) references libro (isbn),
    constraint fk_recepcionista_id
        foreign key (recepcionista_id) references recepcionista (id_recepcionista),
    constraint prestamo_cliente_id_cliente_fk
        foreign key (cliente_id) references cliente (id_cliente),
    constraint prestamo_transportista_transportista_id_fk
        foreign key (transportista_id) references transportista (transportista_id)
);

create table if not exists entrega
(
    id_prestamo    int          not null,
    numero_encargo int          not null,
    isbn           int unsigned not null,
    cliente_id     int          not null,
    primary key (id_prestamo, numero_encargo),
    constraint entrega_cliente_id_cliente_fk
        foreign key (cliente_id) references cliente (id_cliente),
    constraint entrega_encargo_numero_encargo_fk
        foreign key (numero_encargo) references encargo (numero_encargo),
    constraint entrega_libro_isbn_fk
        foreign key (isbn) references libro (isbn),
    constraint fk_prestamo
        foreign key (id_prestamo) references prestamo (id_prestamo)
);

create index fk_encargo_idx
    on entrega (numero_encargo);

create index fk_biblioteca_idx
    on prestamo (biblio_origen);

create index fk_cliente_idx
    on prestamo (cliente_id);

create index fk_libro_idx
    on prestamo (isbn);

create index fk_recepcionista_idx
    on prestamo (recepcionista_id);

create index fk_transportista_idx
    on prestamo (transportista_id);

create table if not exists renta
(
    id_renta     int auto_increment
        primary key,
    multa        double unsigned null,
    fecha_inicio date            not null,
    tipo_multa   varchar(12)     null,
    id_prestamo  int             not null,
    constraint renta_prestamo_id_prestamo_fk
        foreign key (id_prestamo) references prestamo (id_prestamo)
);

create table if not exists traslado
(
    numero_encargo   int          not null,
    recepcionista_id int          not null,
    biblio_destino   int unsigned not null,
    primary key (numero_encargo, biblio_destino),
    constraint numero_traslado_UNIQUE
        unique (numero_encargo),
    constraint fk_biblio
        foreign key (biblio_destino) references biblioteca (id_biblioteca),
    constraint traslado_encargo_numero_encargo_fk
        foreign key (numero_encargo) references encargo (numero_encargo)
);

create index fk_biblio_idx
    on traslado (biblio_destino);

create table if not exists traslado_detalle
(
    id_traslado int          not null,
    isbn        int unsigned not null,
    cantidad    int          not null,
    primary key (id_traslado),
    constraint traslado_detalle_libro_isbn_fk
        foreign key (isbn) references libro (isbn),
    constraint traslado_detalle_traslado_numero_encargo_fk
        foreign key (id_traslado) references traslado (numero_encargo)
);

INSERT INTO parametros (nombre, valor) VALUES ('Dias de suspension por incidencia',25);
INSERT INTO parametros (nombre, valor) VALUES ('Fecha actual','2023-09-08');
INSERT INTO parametros (nombre, valor) VALUES ('Limite de dias de prestamo premium',15);
INSERT INTO parametros (nombre, valor) VALUES ('Limite de dias del prestamo',8);
INSERT INTO parametros (nombre, valor) VALUES ('Limite de libros',5);
INSERT INTO parametros (nombre, valor) VALUES ('Limite de libros premium',10);
