-- DROP SCHEMA dbo;

CREATE DATABASE queries_mantenimiento;
USE queries_mantenimiento;
-- queries_mantenimiento.dbo.t_actions definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_actions;

CREATE TABLE t_actions (
                           id int IDENTITY(1,1) NOT NULL,
                           name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                           CONSTRAINT PK__t_action__3213E83FC5FC246D PRIMARY KEY (id)
);


-- queries_mantenimiento.dbo.t_apps definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_apps;

CREATE TABLE t_apps (
                        id int IDENTITY(1,1) NOT NULL,
                        execute_query_endpoint varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                        name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                        status varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                        CONSTRAINT PK__t_apps__3213E83FBBED1B61 PRIMARY KEY (id)
);


-- queries_mantenimiento.dbo.t_audit_log definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_audit_log;

CREATE TABLE t_audit_log (
                             id bigint IDENTITY(1,1) NOT NULL,
    [action] varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
    data_json varchar(MAX) COLLATE Modern_Spanish_CI_AS NULL,
	[date] datetime2 NULL,
	user_id int NOT NULL,
	CONSTRAINT PK__t_audit___3213E83F88BCF7A1 PRIMARY KEY (id)
);


-- queries_mantenimiento.dbo.t_modules definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_modules;

CREATE TABLE t_modules (
                           id int IDENTITY(1,1) NOT NULL,
                           name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                           status varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                           CONSTRAINT PK__t_module__3213E83F2F8F88AE PRIMARY KEY (id)
);


-- queries_mantenimiento.dbo.t_roles definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_roles;

CREATE TABLE t_roles (
                         id int IDENTITY(1,1) NOT NULL,
                         name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                         CONSTRAINT PK__t_roles__3213E83FD90C64A5 PRIMARY KEY (id)
);


-- queries_mantenimiento.dbo.t_users definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_users;

CREATE TABLE t_users (
                         id int IDENTITY(1,1) NOT NULL,
                         created_at datetime DEFAULT sysdatetime() NULL,
                         email nvarchar(320) COLLATE Modern_Spanish_CI_AS NOT NULL,
                         enabled bit DEFAULT 1 NULL,
                         failed_login_attempts int NOT NULL,
                         last_login datetime2 NULL,
                         lastname varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                         name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                         password varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                         password_updated_at datetime DEFAULT sysdatetime() NULL,
                         status nvarchar(50) COLLATE Modern_Spanish_CI_AS DEFAULT 'active' NULL,
                         CONSTRAINT PK__t_users__3213E83F5D44C227 PRIMARY KEY (id),
                         CONSTRAINT UQ__t_users__AB6E6164C5A4AC24 UNIQUE (email)
);


-- queries_mantenimiento.dbo.t_module_role definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_module_role;

CREATE TABLE t_module_role (
                               role_id int NOT NULL,
                               module_id int NOT NULL,
                               CONSTRAINT FK6cuwii0capru7k325cng3ae9i FOREIGN KEY (module_id) REFERENCES t_modules(id),
                               CONSTRAINT FK7apruiu5o8xk7v025fdkbki2q FOREIGN KEY (role_id) REFERENCES t_roles(id)
);


-- queries_mantenimiento.dbo.t_screens definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_screens;

CREATE TABLE t_screens (
                           id int IDENTITY(1,1) NOT NULL,
                           link varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                           name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                           status varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                           module_id int NULL,
                           CONSTRAINT PK__t_screen__3213E83FDA232BBC PRIMARY KEY (id),
                           CONSTRAINT FK6bk6a5ybfgboidb451640aasy FOREIGN KEY (module_id) REFERENCES t_modules(id)
);


-- queries_mantenimiento.dbo.t_tables definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_tables;

CREATE TABLE t_tables (
                          id bigint IDENTITY(1,1) NOT NULL,
                          app_id int NULL,
                          name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                          CONSTRAINT PK__t_tables__3213E83F91B6A4F4 PRIMARY KEY (id),
                          CONSTRAINT FK7dw6s91v0n0isvximg5nyfrdp FOREIGN KEY (app_id) REFERENCES t_apps(id)
);


-- queries_mantenimiento.dbo.t_user_app definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_user_app;

CREATE TABLE t_user_app (
                            user_id int NOT NULL,
                            app_id int NOT NULL,
                            CONSTRAINT FK9nrvg7nxk8hddb6rmei3cr2by FOREIGN KEY (user_id) REFERENCES t_users(id),
                            CONSTRAINT FKpp8f653gqao4k452olmldxdf4 FOREIGN KEY (app_id) REFERENCES t_apps(id)
);


-- queries_mantenimiento.dbo.t_user_role definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_user_role;

CREATE TABLE t_user_role (
                             user_id int NOT NULL,
                             role_id int NOT NULL,
                             CONSTRAINT FK3egxedenh4m4v816i0y8tvvd FOREIGN KEY (user_id) REFERENCES t_users(id),
                             CONSTRAINT FKeu3341s63d3junskh7qsnmf39 FOREIGN KEY (role_id) REFERENCES t_roles(id)
);


-- queries_mantenimiento.dbo.t_fields definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_fields;

CREATE TABLE t_fields (
                          id bigint IDENTITY(1,1) NOT NULL,
                          name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                          table_id bigint NULL,
    [type] varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
    CONSTRAINT PK__t_fields__3213E83F926E688D PRIMARY KEY (id),
    CONSTRAINT FKqmej2vnju91cx1291d14bvss3 FOREIGN KEY (table_id) REFERENCES t_tables(id)
    );


-- queries_mantenimiento.dbo.t_queries definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_queries;

CREATE TABLE t_queries (
                           id bigint IDENTITY(1,1) NOT NULL,
                           authorized_at datetime2 NULL,
                           parameters varchar(MAX) COLLATE Modern_Spanish_CI_AS NULL,
	requested_at datetime2 NULL,
	response varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
	status varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
	where_condition varchar(MAX) COLLATE Modern_Spanish_CI_AS NULL,
	action_id int NULL,
	authorized_by int NULL,
	requested_by int NULL,
	table_id bigint NULL,
	CONSTRAINT PK__t_querie__3213E83FBECA8ACC PRIMARY KEY (id),
	CONSTRAINT FK1nqg06936olyyoxw9oeavcqjw FOREIGN KEY (requested_by) REFERENCES t_users(id),
	CONSTRAINT FK1xfdpn0buofun1sym3cp2aui8 FOREIGN KEY (authorized_by) REFERENCES t_users(id),
	CONSTRAINT FK49qvq5boaa39f8y72h9jeuvc9 FOREIGN KEY (action_id) REFERENCES t_actions(id),
	CONSTRAINT FKmg6wl6yht5y2v0ba42xykxf13 FOREIGN KEY (table_id) REFERENCES t_tables(id)
);


-- queries_mantenimiento.dbo.t_table_actions definition

-- Drop table

-- DROP TABLE queries_mantenimiento.dbo.t_table_actions;

CREATE TABLE t_table_actions (
                                 table_id bigint NOT NULL,
                                 action_id int NOT NULL,
                                 CONSTRAINT FK6jetmvdt27n37kexas0390u6i FOREIGN KEY (action_id) REFERENCES t_actions(id),
                                 CONSTRAINT FKtipac8gkcwdqfhaghie2qbh2a FOREIGN KEY (table_id) REFERENCES t_tables(id)
);
