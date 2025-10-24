CREATE DATABASE IF NOT EXISTS gestion_financiera;
USE gestion_financiera;


-- =============================================================
--  SISTEMA DE GESTIÓN FINANCIERA PARA IMPORTADORAS DE INSUMOS TECNOLÓGICOS
--  Autor: Juan Sebastián Vecchi
--  Materia: Proyecto Integral de Desarrollo de Software
--  Entrega: TP2
-- =============================================================


-- =============================================================
--  1. CREACIÓN DE TABLAS
-- =============================================================

CREATE TABLE MONEDA (
    cod_moneda CHAR(3) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE EMPRESA (
    id_empresa INT AUTO_INCREMENT PRIMARY KEY,
    razon_social VARCHAR(100) NOT NULL,
    cuit CHAR(13) NOT NULL,
    pais VARCHAR(50),
    cod_moneda_principal CHAR(3),
    FOREIGN KEY (cod_moneda_principal) REFERENCES MONEDA(cod_moneda)
);

CREATE TABLE USUARIO (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT,
    nombre VARCHAR(100),
    email VARCHAR(100),
    rol VARCHAR(30),
    pass_hash VARCHAR(255),
    FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id_empresa)
);

CREATE TABLE PROVEEDOR (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT,
    nombre VARCHAR(100),
    pais VARCHAR(50),
    cod_moneda CHAR(3),
    condiciones VARCHAR(100),
    plazo_pago_dias INT,
    FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id_empresa),
    FOREIGN KEY (cod_moneda) REFERENCES MONEDA(cod_moneda)
);

CREATE TABLE CLIENTE (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT,
    nombre VARCHAR(100),
    cuit CHAR(13),
    FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id_empresa)
);

CREATE TABLE CUENTA (
    id_cuenta INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT,
    nombre VARCHAR(100),
    tipo VARCHAR(30),
    cod_moneda CHAR(3),
    saldo_inicial DECIMAL(15,2),
    FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id_empresa),
    FOREIGN KEY (cod_moneda) REFERENCES MONEDA(cod_moneda)
);

CREATE TABLE TIPO_CAMBIO (
    id_tipo_cambio INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    cod_moneda CHAR(3),
    tc_oficial DECIMAL(10,2),
    tc_proyectado DECIMAL(10,2),
    FOREIGN KEY (cod_moneda) REFERENCES MONEDA(cod_moneda)
);

CREATE TABLE MOVIMIENTO (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT,
    id_cuenta INT,
    tipo ENUM('ingreso','egreso'),
    fecha DATE,
    monto DECIMAL(15,2),
    cod_moneda CHAR(3),
    categoria VARCHAR(100),
    id_proveedor INT,
    observaciones VARCHAR(255),
    FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id_empresa),
    FOREIGN KEY (id_cuenta) REFERENCES CUENTA(id_cuenta),
    FOREIGN KEY (cod_moneda) REFERENCES MONEDA(cod_moneda),
    FOREIGN KEY (id_proveedor) REFERENCES PROVEEDOR(id_proveedor)
);

CREATE TABLE ESCENARIO (
    id_escenario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    inflacion_anual DECIMAL(5,2),
    variacion_tc DECIMAL(5,2),
    horizonte_meses INT
);

CREATE TABLE PROYECCION (
    id_proyeccion INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT,
    cod_moneda CHAR(3),
    periodo CHAR(7),
    id_escenario INT,
    saldo_proyectado DECIMAL(15,2),
    FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id_empresa),
    FOREIGN KEY (cod_moneda) REFERENCES MONEDA(cod_moneda),
    FOREIGN KEY (id_escenario) REFERENCES ESCENARIO(id_escenario)
);

CREATE TABLE RECOMENDACION (
    id_recomendacion INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT,
    id_escenario INT,
    tipo VARCHAR(30),
    instrumento VARCHAR(100),
    monto_sugerido DECIMAL(15,2),
    horizonte_dias INT,
    justificacion VARCHAR(255),
    fecha DATE,
    FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id_empresa),
    FOREIGN KEY (id_escenario) REFERENCES ESCENARIO(id_escenario)
);

-- =============================================================
--  2. INSERCIÓN DE DATOS DE PRUEBA
-- =============================================================

INSERT INTO MONEDA VALUES ('ARS', 'Peso Argentino');
INSERT INTO MONEDA VALUES ('USD', 'Dólar Estadounidense');

INSERT INTO EMPRESA (razon_social, cuit, pais, cod_moneda_principal)
VALUES ('Gewer Tecnología S.A.', '30-71594236-9', 'Argentina', 'ARS');

INSERT INTO USUARIO (id_empresa, nombre, email, rol, pass_hash)
VALUES
(1, 'Juan Pérez', 'jperez@gewer.com', 'gerente', 'hash1'),
(1, 'María López', 'mlopez@gewer.com', 'contador', 'hash2'),
(1, 'Sofía Díaz', 'sdiaz@gewer.com', 'analista', 'hash3');

INSERT INTO PROVEEDOR (id_empresa, nombre, pais, cod_moneda, condiciones, plazo_pago_dias)
VALUES
(1, 'TechParts USA', 'Estados Unidos', 'USD', 'Pago a 30 días', 30),
(1, 'Componentes Latam', 'Brasil', 'USD', 'Pago anticipado', 0);

INSERT INTO CLIENTE (id_empresa, nombre, cuit)
VALUES
(1, 'ElectroMundo S.R.L.', '30-52987654-8'),
(1, 'Innovar Tech', '30-60214578-3');

INSERT INTO CUENTA (id_empresa, nombre, tipo, cod_moneda, saldo_inicial)
VALUES
(1, 'Cuenta Corriente Banco Nación', 'corriente', 'ARS', 500000),
(1, 'Cuenta USD Banco Galicia', 'corriente', 'USD', 15000);

INSERT INTO MOVIMIENTO (id_empresa, id_cuenta, tipo, fecha, monto, cod_moneda, categoria, id_proveedor, observaciones)
VALUES
(1, 1, 'egreso', '2025-10-05', 200000, 'ARS', 'Compra de insumos locales', NULL, 'Pago proveedores internos'),
(1, 2, 'egreso', '2025-10-10', 5000, 'USD', 'Importación', 1, 'Pago TechParts'),
(1, 1, 'ingreso', '2025-10-15', 300000, 'ARS', 'Venta local', NULL, 'Cobro de clientes'),
(1, 2, 'ingreso', '2025-10-18', 2500, 'USD', 'Cobro internacional', NULL, 'Transferencia recibida');

INSERT INTO ESCENARIO (nombre, inflacion_anual, variacion_tc, horizonte_meses)
VALUES
('Base', 150.00, 3.0, 6),
('Optimista', 120.00, 1.5, 6),
('Pesimista', 180.00, 6.0, 6);

INSERT INTO PROYECCION (id_empresa, cod_moneda, periodo, id_escenario, saldo_proyectado)
VALUES
(1, 'ARS', '2025-12', 1, 800000),
(1, 'USD', '2025-12', 1, 18000),
(1, 'ARS', '2025-12', 2, 850000),
(1, 'USD', '2025-12', 2, 20000),
(1, 'ARS', '2025-12', 3, 700000),
(1, 'USD', '2025-12', 3, 15000);

INSERT INTO RECOMENDACION (id_empresa, id_escenario, tipo, instrumento, monto_sugerido, horizonte_dias, justificacion, fecha)
VALUES
(1, 2, 'Inversión', 'Fondo Money Market', 300000, 30, 'Excedente en pesos bajo escenario optimista', '2025-10-20'),
(1, 3, 'Cobertura', 'Compra de dólares', 5000, 15, 'Escenario de alta devaluación esperada', '2025-10-22');

-- =============================================================
--  3. CONSULTAS SQL DE VALIDACIÓN Y ANÁLISIS
-- =============================================================

-- Flujo de caja total por moneda
SELECT 
    cod_moneda,
    SUM(monto * IF(tipo = 'ingreso', 1, -1)) AS flujo_total
FROM MOVIMIENTO
GROUP BY cod_moneda;

-- Saldo final por cuenta
SELECT 
    c.nombre AS cuenta,
    c.cod_moneda,
    c.saldo_inicial + 
    IFNULL(SUM(CASE WHEN m.tipo = 'ingreso' THEN m.monto ELSE -m.monto END), 0) AS saldo_final
FROM CUENTA c
LEFT JOIN MOVIMIENTO m ON c.id_cuenta = m.id_cuenta
GROUP BY c.id_cuenta;

-- Proyección de saldo según escenario económico
SELECT 
    e.nombre AS escenario,
    p.cod_moneda,
    p.periodo,
    p.saldo_proyectado
FROM PROYECCION p
JOIN ESCENARIO e ON p.id_escenario = e.id_escenario
ORDER BY p.cod_moneda, e.nombre;
