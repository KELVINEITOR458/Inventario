select pro.codigo_prod, pro.nombre as nombre_producto,
udm.codigo_udm as nombre_udm, udm.descripcion as descripcion_udm,
cast(pro.precio_venta as decimal(6,2)), pro.tiene_iva, 
cast(pro.coste as decimal(5,2)),
pro.categoria, cat.nombre as nombre_categoria,
stock
from productos pro, unidades_medida udm, categorias cat
where pro.udm = udm.codigo_udm
and pro.categoria = cat.codigo_cat
and upper(pro.nombre) like '%MA%'

select *
from productos pro, unidades_medida udm, categorias cat
where pro.udm = udm.codigo_udm
and pro.categoria = cat.codigo_cat

