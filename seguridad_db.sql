
CREATE DATABASE mantenimientos_seguridad_db;
-- mantenimientos_seguridad_db.dbo.t_apps definition

-- Drop table

-- DROP TABLE ocb_mantenimientos_seguridad.dbo.t_apps;

CREATE TABLE t_apps (
                        id int IDENTITY(1,1) NOT NULL,
                        name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                        status varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                        execute_query_endpoint varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                        CONSTRAINT PK__t_apps__3213E83FEFE5EDC0 PRIMARY KEY (id)
);


-- ocb_mantenimientos_seguridad.dbo.t_modules definition

-- Drop table

-- DROP TABLE ocb_mantenimientos_seguridad.dbo.t_modules;

CREATE TABLE t_modules (
                           id int IDENTITY(1,1) NOT NULL,
                           name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                           status varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                           CONSTRAINT PK__t_module__3213E83F3DC3D966 PRIMARY KEY (id)
);


-- ocb_mantenimientos_seguridad.dbo.t_roles definition

-- Drop table

-- DROP TABLE ocb_mantenimientos_seguridad.dbo.t_roles;

CREATE TABLE t_roles (
                         id int IDENTITY(1,1) NOT NULL,
                         name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                         CONSTRAINT PK__t_roles__3213E83F482FF019 PRIMARY KEY (id)
);


-- ocb_mantenimientos_seguridad.dbo.t_users definition

-- Drop table

-- DROP TABLE ocb_mantenimientos_seguridad.dbo.t_users;

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
                         CONSTRAINT PK__t_users__3213E83FB6EE03AE PRIMARY KEY (id),
                         CONSTRAINT UQ__t_users__AB6E616414E13099 UNIQUE (email)
);


-- ocb_mantenimientos_seguridad.dbo.t_module_role definition

-- Drop table

-- DROP TABLE ocb_mantenimientos_seguridad.dbo.t_module_role;

CREATE TABLE t_module_role (
                               role_id int NOT NULL,
                               module_id int NOT NULL,
                               CONSTRAINT FK6cuwii0capru7k325cng3ae9i FOREIGN KEY (module_id) REFERENCES t_modules(id),
                               CONSTRAINT FK7apruiu5o8xk7v025fdkbki2q FOREIGN KEY (role_id) REFERENCES t_roles(id)
);


-- ocb_mantenimientos_seguridad.dbo.t_screens definition

-- Drop table

-- DROP TABLE ocb_mantenimientos_seguridad.dbo.t_screens;

CREATE TABLE t_screens (
                           id int IDENTITY(1,1) NOT NULL,
                           link varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                           name varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                           status varchar(255) COLLATE Modern_Spanish_CI_AS NULL,
                           module_id int NULL,
                           CONSTRAINT PK__t_screen__3213E83F231D0579 PRIMARY KEY (id),
                           CONSTRAINT FK6bk6a5ybfgboidb451640aasy FOREIGN KEY (module_id) REFERENCES t_modules(id)
);


-- ocb_mantenimientos_seguridad.dbo.t_user_app definition

-- Drop table

-- DROP TABLE ocb_mantenimientos_seguridad.dbo.t_user_app;

CREATE TABLE t_user_app (
                            user_id int NOT NULL,
                            app_id int NOT NULL,
                            CONSTRAINT FK9nrvg7nxk8hddb6rmei3cr2by FOREIGN KEY (user_id) REFERENCES t_users(id),
                            CONSTRAINT FKpp8f653gqao4k452olmldxdf4 FOREIGN KEY (app_id) REFERENCES t_apps(id)
);


-- ocb_mantenimientos_seguridad.dbo.t_user_role definition

-- Drop table

-- DROP TABLE ocb_mantenimientos_seguridad.dbo.t_user_role;

CREATE TABLE t_user_role (
                             user_id int NOT NULL,
                             role_id int NOT NULL,
                             CONSTRAINT FK3egxedenh4m4v816i0y8tvvd FOREIGN KEY (user_id) REFERENCES t_users(id),
                             CONSTRAINT FKeu3341s63d3junskh7qsnmf39 FOREIGN KEY (role_id) REFERENCES t_roles(id)
);
