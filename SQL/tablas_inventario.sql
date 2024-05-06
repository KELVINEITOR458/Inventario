
drop table if exists detalle_pedido;
drop table if exists detalle_ventas;
drop table if exists cabecera_ventas;
drop table if exists cabecera_pedido;
drop table if exists proveedores;
drop table if exists tipo_documento;
drop table if exists estados_pedido;
drop table if exists historial_stock;
drop table if exists productos;
drop table if exists categorias;
drop table if exists unidades_medida;
drop table if exists categorias_unidad_medida;
create table categorias(
	codigo_cat serial not null,
	nombre varchar(100) not null,
	categoria_padre int,
	constraint categorias_pk primary key(codigo_cat),
	constraint categorias_fk foreign key(categoria_padre)
	references categorias(codigo_cat)
);
insert into categorias(nombre, categoria_padre)
values('Materia Prima', null);
insert into categorias(nombre, categoria_padre)
values('Proteína', 1);
insert into categorias(nombre, categoria_padre)
values('Salsas', 1);
insert into categorias(nombre, categoria_padre)
values('Punto de venta', null);
insert into categorias(nombre, categoria_padre)
values('Bebidas', 4);
insert into categorias(nombre, categoria_padre)
values('Con alcohol', 5);
insert into categorias(nombre, categoria_padre)
values('Sin alcohol', 5);
select * from categorias;


create table categorias_unidad_medida(
	codigo_udm char(1) not null,
	nombre varchar(100) not null,
	constraint categorias_unidad_medida_pk primary key(codigo_udm)
);
insert into categorias_unidad_medida(codigo_udm, nombre)
values('U', 'Unidades');
insert into categorias_unidad_medida(codigo_udm, nombre)
values('V', 'Volumen');
insert into categorias_unidad_medida(codigo_udm, nombre)
values('P', 'Peso');
insert into categorias_unidad_medida(codigo_udm, nombre)
values('L', 'Longitud');
select * from categorias_unidad_medida;

create table unidades_medida(
	codigo_udm varchar not null,
	descripcion varchar(100) not null,
	categoria_udm char(1) not null,
	constraint unidades_medida_pk primary key(codigo_udm),
	constraint unidades_medida_fk foreign key(categoria_udm)
	references categorias_unidad_medida(codigo_udm)
);
insert into unidades_medida(codigo_udm, descripcion, categoria_udm)
values('ml', 'mililitro', 'V');
insert into unidades_medida(codigo_udm, descripcion, categoria_udm)
values('l', 'litros', 'V');
insert into unidades_medida(codigo_udm, descripcion, categoria_udm)
values('u', 'Unidad', 'U');
insert into unidades_medida(codigo_udm, descripcion, categoria_udm)
values('d', 'docenas', 'U');
insert into unidades_medida(codigo_udm, descripcion, categoria_udm)
values('g', 'gramos', 'P');
insert into unidades_medida(codigo_udm, descripcion, categoria_udm)
values('kg', 'kilogramos', 'P');
insert into unidades_medida(codigo_udm, descripcion, categoria_udm)
values('lb', 'libras', 'P');
select * from unidades_medida;

create table productos(
	codigo_prod int not null,
	nombre varchar(50) not null,
	udm char(2) not null,
	precio_venta money not null,
	tiene_IVA boolean not null,
	coste money not null,
	categoria int not null,
	stock int not null,
	constraint productos_pk primary key(codigo_prod),
	constraint ptoductos_udm_fk foreign key(udm)
	references unidades_medida(codigo_udm),
	constraint productos_categoria_fk foreign key(categoria)
	references categorias(codigo_cat)
);

insert into productos(codigo_prod, nombre, udm, precio_venta, tiene_IVA, coste, categoria, stock)
values(1, 'Coca-Cola pequeña', 'u', 0.58, true, 0.37, 7, 105);
insert into productos(codigo_prod, nombre, udm, precio_venta, tiene_IVA, coste, categoria, stock)
values(2, 'Salsa de tomate', 'kg', 0.95, true, 0.87, 3, 0);
insert into productos(codigo_prod, nombre, udm, precio_venta, tiene_IVA, coste, categoria, stock)
values(3, 'Mostaza', 'kg', 0.95, true, 0.89, 3, 0);
insert into productos(codigo_prod, nombre, udm, precio_venta, tiene_IVA, coste, categoria, stock)
values(4, 'FuzeTea', 'u', 0.8, true, 0.7, 7, 45);

select * from productos;

create table historial_stock(
	codigo serial not null,
	fecha TIMESTAMP not null,
	referencia varchar not null,
	producto int not null,
	 cantidad int not null,
	constraint historial_stock_pk primary key(codigo),
	constraint historial_producto_fk foreign key(producto)
	references productos(codigo_prod)
);
insert into historial_stock(fecha, referencia, producto, cantidad)
values('20/11/2023 19:59', 'Pedido 1', 1, 100);
insert into historial_stock(fecha, referencia, producto, cantidad)
values('20/11/2023 19:59', 'Pedido 1', 4, 50);
insert into historial_stock(fecha, referencia, producto, cantidad)
values('20/11/2023 20:00', 'Pedido 2', 1, 10);
insert into historial_stock(fecha, referencia, producto, cantidad)
values('20/11/2023 20:00', 'Venta 1', 1, -5);
insert into historial_stock(fecha, referencia, producto, cantidad)
values('20/11/2023 20:00', 'Venta 1', 4, -1);
select * from historial_stock;

create table tipo_documento(
	codigo varchar not null,
	descripcion varchar(30) not null,
	constraint tipo_documento_pk primary key(codigo)
);
insert into tipo_documento(codigo, descripcion)
values('C', 'CEDULA');
insert into tipo_documento(codigo, descripcion)
values('R', 'RUC');
select * from tipo_documento;

create table estados_pedido(
	codigo varchar not null,
	descripcion varchar(30) not null,
	constraint estados_pedido_pk primary key(codigo)
);
insert into estados_pedido(codigo, descripcion)
values('S', 'Solicitado');
insert into estados_pedido(codigo, descripcion)
values('R', 'Recibido');
select * from estados_pedido;

create table proveedores(
	identificador varchar not null,
	tipo_documento varchar not null,
	nombre varchar(50) not null,
	telefono char(10) not null,
	correo varchar(50) not null,
	direccion varchar(70) not null,
	constraint proveeedores_pk primary key(identificador),
	constraint proveeedores_fk foreign key(tipo_documento)
	references tipo_documento(codigo)
);
insert into proveedores(identificador, tipo_documento, nombre, telefono, correo, direccion)
values('124589632145', 'R', 'Alvaro Loaiza', '0987451263', 'alvaro2000@gmail.com', 'Calderón');
insert into proveedores(identificador, tipo_documento, nombre, telefono, correo, direccion)
values('1778961236', 'C', 'Snacks.SA', '0984187545', 'snacksSA@gmail.com', 'Carapungo');
select * from proveedores;

create table cabecera_pedido(
	numero serial not null,
	proveedor varchar not null,
	fecha TIMESTAMP not null,
	estado varchar not null,
	constraint cabecera_pk primary key(numero),
	constraint cabecera_proveedor_fk foreign key(proveedor)
	references proveedores(identificador),
	constraint cabecera_estado_fk foreign key(estado)
	references estados_pedido(codigo)
);
insert into cabecera_pedido(proveedor, fecha, estado)
values('124589632145', '30/11/2023', 'R');
insert into cabecera_pedido(proveedor, fecha, estado)
values('124589632145', '30/11/2023', 'R');
select * from cabecera_pedido;

create table cabecera_ventas(
	codigo int not null,
	fecha TIMESTAMP NOT NULL,
	total_sin_IVA money not null,
	IVA money not null,
	total money not null,
	constraint cabecera_ventas_pk primary key(codigo) 
);
insert into cabecera_ventas(codigo, fecha, total_sin_IVA, IVA, total)
values(1, '20/11/2023 20:00', 3.26, 0.39, 3.65);
select * from cabecera_ventas;

create table detalle_ventas(
	codigo serial not null,
	cabecera_ventas int not null,
	codigo_producto int not null,
	cantidad int not null,
	precio_venta money not null,
	subtotal money not null,
	subtotal_IVA money not null,
	constraint detalle_ventas_pk primary key(codigo),
	constraint detalle_cabecera_fk foreign key(cabecera_ventas)
	references cabecera_ventas(codigo),
	constraint detalle_producto_fk foreign key(codigo_producto)
	references productos(codigo_prod)
);
insert into detalle_ventas(cabecera_ventas, codigo_producto, cantidad, precio_venta, subtotal, subtotal_IVA)
values(1, 1, 5, 0.58, 2.9, 3.25);
insert into detalle_ventas(cabecera_ventas, codigo_producto, cantidad, precio_venta, subtotal, subtotal_IVA)
values(1, 4, 1, 0.36, 0.36, 0.4);
select * from detalle_ventas;

create table detalle_pedido(
	codigo serial not null,
	cabecera_pedido int not null,
	producto int not null,
	cantidad int not null,
	subtotal money not null,
	cantidad_Recibida int not null,
	constraint detalle_pedido_pk primary key(codigo),
	constraint detalle_cabecera_fk foreign key(cabecera_pedido)
	references cabecera_pedido(numero),
	constraint detalle_producto_fk foreign key(producto)
	references productos(codigo_prod)
);
insert into detalle_pedido( cabecera_pedido, producto, cantidad, subtotal, cantidad_recibida)
values(1,4,100,37.39,100);
insert into detalle_pedido(cabecera_pedido, producto, cantidad, subtotal, cantidad_recibida)
values(1,4,50,11.8,50);
insert into detalle_pedido(cabecera_pedido, producto, cantidad, subtotal, cantidad_recibida)
values(2,1,10,3.73,10);
select * from detalle_pedido;

