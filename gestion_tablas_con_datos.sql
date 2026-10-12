CREATE DATABASE IF NOT EXISTS gestion_tareas;
USE gestion_tareas;

CREATE TABLE areas (
    id_area INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE puestos (
    id_puesto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    estado VARCHAR(20) NOT NULL
);

CREATE TABLE trabajadores (
    id_trabajador INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_area INT NOT NULL,
    id_puesto INT NOT NULL,
    disponibilidad VARCHAR(30) NOT NULL,
    ubicacion_actual VARCHAR(150),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_area) REFERENCES areas(id_area),
    FOREIGN KEY (id_puesto) REFERENCES puestos(id_puesto)
);

CREATE TABLE habilidades (
    id_habilidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

CREATE TABLE trabajador_habilidades (
    id_trabajador INT NOT NULL,
    id_habilidad INT NOT NULL,
    nivel VARCHAR(50),
    PRIMARY KEY (id_trabajador, id_habilidad),
    FOREIGN KEY (id_trabajador) REFERENCES trabajadores(id_trabajador),
    FOREIGN KEY (id_habilidad) REFERENCES habilidades(id_habilidad)
);

CREATE TABLE tareas (
    id_tarea INT AUTO_INCREMENT PRIMARY KEY,
    descripcion TEXT NOT NULL,
    prioridad VARCHAR(50) NOT NULL,
    id_area INT NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    fecha_limite DATETIME,
    tiempo_estimado VARCHAR(50),
    estado VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_area) REFERENCES areas(id_area)
);

CREATE TABLE tarea_habilidades (
    id_tarea INT NOT NULL,
    id_habilidad INT NOT NULL,
    nivel_requerido VARCHAR(50),
    PRIMARY KEY (id_tarea, id_habilidad),
    FOREIGN KEY (id_tarea) REFERENCES tareas(id_tarea),
    FOREIGN KEY (id_habilidad) REFERENCES habilidades(id_habilidad)
);

CREATE TABLE asignaciones (
    id_asignacion INT AUTO_INCREMENT PRIMARY KEY,
    id_tarea INT NOT NULL,
    id_trabajador INT NOT NULL,
    fecha_asignacion DATETIME NOT NULL,
    fecha_inicio DATETIME,
    fecha_finalizacion DATETIME,
    puntuacion_generada INT,
    FOREIGN KEY (id_tarea) REFERENCES tareas(id_tarea),
    FOREIGN KEY (id_trabajador) REFERENCES trabajadores(id_trabajador)
);

CREATE TABLE incidencias (
    id_incidencia INT AUTO_INCREMENT PRIMARY KEY,
    id_tarea INT NOT NULL,
    descripcion TEXT NOT NULL,
    fecha DATETIME NOT NULL,
    estado VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_tarea) REFERENCES tareas(id_tarea)
);

-- =========================================================
-- DATOS DE PRUEBA
-- =========================================================

INSERT INTO areas (nombre) VALUES
('Ensamble'), ('Mantenimiento'), ('Empaque'), ('Calidad');

INSERT INTO puestos (nombre) VALUES
('Técnico de mantenimiento'), ('Operador de línea'), ('Supervisor de planta');

INSERT INTO usuarios (nombre, correo, contrasena, rol, estado) VALUES
('Laura Garza', 'laura.garza@planta.com', 'hash_pendiente_1', 'Supervisor', 'Activo'),
('Ricardo Peña', 'ricardo.pena@planta.com', 'hash_pendiente_2', 'Supervisor', 'Activo'),
('Marco Villarreal', 'marco.villarreal@planta.com', 'hash_pendiente_3', 'Trabajador', 'Activo'),
('Sandra Cavazos', 'sandra.cavazos@planta.com', 'hash_pendiente_4', 'Trabajador', 'Activo'),
('Jesús Flores', 'jesus.flores@planta.com', 'hash_pendiente_5', 'Trabajador', 'Activo');

INSERT INTO trabajadores (id_usuario, id_area, id_puesto, disponibilidad, ubicacion_actual) VALUES
(3, 2, 1, 'Disponible', 'Nave 2 - Mantenimiento'),
(4, 1, 2, 'Ocupado', 'Nave 1 - Línea A'),
(5, 3, 2, 'Disponible', 'Nave 3 - Empaque');

INSERT INTO habilidades (nombre, descripcion) VALUES
('Soldadura', 'Soldadura de estructuras metálicas'),
('Hidráulica', 'Diagnóstico y reparación de sistemas hidráulicos'),
('Electricidad industrial', 'Instalación y reparación eléctrica'),
('Manejo de montacargas', 'Operación certificada de montacargas');

INSERT INTO trabajador_habilidades (id_trabajador, id_habilidad, nivel) VALUES
(1, 2, 'Avanzado'),
(1, 3, 'Intermedio'),
(2, 4, 'Avanzado'),
(3, 4, 'Intermedio'),
(3, 1, 'Básico');

INSERT INTO tareas (descripcion, prioridad, id_area, fecha_creacion, fecha_limite, tiempo_estimado, estado) VALUES
('Reparar fuga en línea hidráulica de la prensa 3', 'Alta', 2, '2026-09-10 08:00:00', '2026-09-10 14:00:00', '2 horas', 'Pendiente'),
('Revisión eléctrica preventiva en línea A', 'Media', 1, '2026-09-09 09:00:00', '2026-09-12 18:00:00', '3 horas', 'En proceso'),
('Reubicar tarimas en almacén de empaque', 'Baja', 3, '2026-09-11 07:30:00', NULL, '1 hora', 'Pendiente');

INSERT INTO tarea_habilidades (id_tarea, id_habilidad, nivel_requerido) VALUES
(1, 2, 'Avanzado'),
(2, 3, 'Intermedio'),
(3, 4, 'Básico');

INSERT INTO asignaciones (id_tarea, id_trabajador, fecha_asignacion, fecha_inicio, fecha_finalizacion, puntuacion_generada) VALUES
(1, 1, '2026-09-10 08:15:00', '2026-09-10 08:30:00', NULL, 92),
(2, 1, '2026-09-09 09:10:00', '2026-09-09 09:30:00', '2026-09-09 12:00:00', 78);

INSERT INTO incidencias (id_tarea, descripcion, fecha, estado) VALUES
(2, 'Falta una pieza de repuesto, se pausó la tarea', '2026-09-09 11:00:00', 'Abierta');
