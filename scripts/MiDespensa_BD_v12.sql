CREATE DATABASE IF NOT EXISTS midespensa_bd CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'product'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON midespensa_bd.* TO 'product'@'%';

use midespensa_bd;

DROP TABLE IF EXISTS pantry;
DROP TABLE IF EXISTS shopping_list;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS product_recipe;
DROP TABLE IF EXISTS recipe_tag;
DROP TABLE IF EXISTS recipe_ingredient;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS ingredient;
DROP TABLE IF EXISTS difficulty;
DROP TABLE IF EXISTS recipe_category;

create table users (
	id int(11) not null AUTO_INCREMENT,
	email varchar(100) UNIQUE,
	pass VARCHAR(255) NOT NULL,
	last_notification date,
    last_connection date,
    role_ varchar(10) NOT NULL,
    token VARCHAR(255) NULL UNIQUE, -- Ajusta la longitud si usas tokens más largos
	token_expiry_date DATETIME NULL,
    validated_email boolean null,
	
	PRIMARY KEY (id)
);

create table difficulty (
	id int(11) not null AUTO_INCREMENT,
	title varchar(100) not null,
	
	PRIMARY KEY (id)
);

create table tag (
	id int(11) not null AUTO_INCREMENT,
	name varchar(100) NOT NULL,
	
	PRIMARY KEY (id)
);

create table ingredient (
	id int(11) not null AUTO_INCREMENT,
	name varchar(100) NOT NULL,
	
	PRIMARY KEY (id)
);

create table recipe_category (
	id int(11) not null AUTO_INCREMENT,
	title varchar(100) NOT NULL,
	
	PRIMARY KEY (id)
);

create table product ( -- Productos que se obtienen de https://world.openfoodfacts.org 
	id int(11) not null AUTO_INCREMENT,
    barcode varchar(100) NOT NULL,
    id_user int(11) NOT NULL,
	title varchar(100) NOT NULL,
	brand varchar(100),
	amount varchar(100),
    image varchar(100),
    date_update date, #fecha de actualizacion del producto desde la API
	
	PRIMARY KEY (id),
    FOREIGN KEY (id_user) references users(id) ON DELETE CASCADE
);

create table pantry ( -- Productos en la despensa de cada usuario
	id int(11) NOT NULL,
	id_user int(11) NOT NULL,
    unity int(11),
    date_update date, #fecha de actualizacion
	
    FOREIGN KEY (id) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE,
	PRIMARY KEY (id,id_user)
); 

create table shopping_list ( -- Lista de la compra usuarios
	id int(11) NOT NULL,
	id_user int(11) NOT NULL,
    unity int,
    date_update date, #fecha de actualizacion
	
	PRIMARY KEY (id, id_user),
	FOREIGN KEY (id) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE
);

create table recipe (
	id int(11) NOT NULL AUTO_INCREMENT,
    id_user int(11) NOT NULL,
    id_category int(11) NOT NULL,
    id_difficulty int(11) NOT NULL,
    shared boolean,
    title varchar(100) NOT NULL,
    elaboration text NOT NULL,
    prep_time int(11),
    cook_time int(11),
    image varchar(100),
    date_update date, #fecha de actualizacion
    
    PRIMARY KEY (id),
    FOREIGN KEY (id_category) REFERENCES recipe_category(id),
    FOREIGN KEY (id_difficulty) REFERENCES difficulty(id),
    FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE recipe_tag (
    recipe_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (recipe_id, tag_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
);

CREATE TABLE recipe_ingredient (
    recipe_id INT NOT NULL,
    ingredient_id INT NOT NULL,
    PRIMARY KEY (recipe_id, ingredient_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE
);

-- Datos iniciales
INSERT INTO `difficulty` VALUES (1,'Fácil'),(2,'Media'),(3,'Difícil');
INSERT INTO `ingredient` VALUES (1,'manzana'),(2,'fruta'),(3,'azúcar'),(4,'hojaldre'),(5,'sopa'),(6,'ajo'),(7,'Harina'),(8,'Aceite'),(9,'Levadura'),(10,'Lechuga'),(11,'queso'),(12,'pollo'),(13,'cebolla'),(14,'Leche'),(15,'huevo'),(16,'cacao'),(19,'carne'),(22,'horno'),(25,'chocolate'),(30,'1 yogur'),(31,'1 medida de yogur de aceite de girasol'),(32,'2 medidas de yogur de azúcar'),(33,'3 medidas de yogur de harina'),(34,'3 huevos'),(35,'1 sobre de levadura'),(36,'60 gr harina avena'),(37,'1/2 platano'),(38,'1 huevo'),(39,'1 puñado de chips de chocolate'),(40,'5 lomos de merluza'),(41,'2 cucharadas de harina'),(42,'perejil picado al gusto'),(43,'vaso y medio de vino blanco'),(44,'200 g guisantes'),(45,'sal'),(46,'aceite de oliva'),(47,'1 pieza de 0’7 kg de lomo de cerdo'),(48,'1 pimiento rojo'),(49,'1 pimiento verde'),(50,'1 puerro'),(51,'Bizcocho'),(52,'Peras'),(53,'Arroz'),(54,'Lentejas'),(55,'Mozzarela'),(56,'massa'),(57,'tomate'),(58,'judias'),(59,'Pechuga de pollo'),(60,'picatostes'),(61,'salsa cesar'),(62,'merienda'),(63,'melon'),(64,'platano');
INSERT INTO `users` VALUES (1,'admin@admin.com','$2a$10$.zxi9GQUKRyb4jRxA6fo5upyNMGyYiIkZ8kx8jGUkzZtzDBHbvp8O',NULL,'2025-05-07','ADMIN',NULL,NULL,1),(2,'user@user.com','$2a$10$qJaUy.JpGA3I6bR6zE2Ld.Wk.01JMB0OwznCTz0BxzM.yrDreSoHe',NULL,'2025-05-07','USER',NULL,NULL,1),(48,'ruben.touceda@gmail.com','$2a$10$UgBN.HBCBTJ6BDkMv3e4IOvlHo5VtqxrVfDGFMSrVUq/MdPw.qbhu',NULL,'2025-05-06','USER',NULL,NULL,1),(49,'pumarescalvos@gmail.com','$2a$10$L/K/sMRasMnf/fUNFXXt6.5KRQyGxnssdV62K4ges.FJsHhBMo.MW',NULL,'2025-04-30','USER',NULL,NULL,1);
INSERT INTO `product` VALUES (5,'8480000062567',1,'Fideo mediano','Hacendado','500 g','https://images.openfoodfacts.org/images/products/848/000/006/2567/front_es.19.200.jpg','2025-05-05'),(7,'8480000228369',1,'Crema de cacahuete Amendoim','Hacendado','500 g','https://images.openfoodfacts.org/images/products/848/000/022/8369/front_en.36.200.jpg','2025-04-10'),(8,'8410376040010',1,'Coca-Cola Zero Azúcar','Coca-Cola','2 l',NULL,'2025-04-10'),(9,'8410500006112',1,'Yogur natural azucarado','Danone','500 g (125 g x 4)','https://images.openfoodfacts.org/images/products/841/050/000/6112/front_es.47.200.jpg','2025-02-23'),(10,'8431876260421',1,'Aceite de oliva virgen extra','Carbonell','1 l',NULL,'2025-01-20'),(12,'8410041920505',1,'Arroz redondo','Brillante','1 kg',NULL,'2025-04-10'),(13,'8410184043734',1,'Tomate frito estilo casero','Orlando','350 g',NULL,'2025-01-14'),(14,'8410203204004',1,'Chocolate con leche','Nestlé','200 g',NULL,'2025-04-10'),(16,'8412300300554',1,'Pasta Macarrones','Gallo','500 g',NULL,'2025-04-10'),(17,'8410179011728',1,'Atún claro en aceite de oliva','Calvo','3x80 g',NULL,'2025-04-10'),(41,'8480000104892',1,'Leche Semidesnatada','Hacendado','1 l','https://images.openfoodfacts.org/images/products/848/000/010/4892/front_es.25.200.jpg','2025-04-17'),(42,'8480000158444',1,'chocolate','Hacendado','250 g','https://images.openfoodfacts.org/images/products/848/000/015/8444/front_es.51.200.jpg','2025-04-17'),(43,'8480012012659',1,'Digestive','Eliges','2 x 400 g','https://images.openfoodfacts.org/images/products/848/001/201/2659/front_es.3.200.jpg','2025-04-17'),(44,'5449000169327',1,'Coca-Cola Zero Zero fria','Coca-Cola','330 ml','https://images.openfoodfacts.org/images/products/544/900/016/9327/front_en.61.200.jpg','2025-04-17'),(45,'8412598001758',1,'Estrella Galicia','Estrella Galicia','21cl','https://images.openfoodfacts.org/images/products/841/259/800/1758/front_es.3.200.jpg','2025-04-17'),(46,'8480000102751',1,'Nata montar','Hacendado','200 ml','https://images.openfoodfacts.org/images/products/848/000/010/2751/front_en.59.200.jpg','2025-04-17'),(47,'5901044031522',1,'Street food wraps&kebabs',NULL,NULL,'https://images.openfoodfacts.org/images/products/590/104/403/1522/front_es.3.200.jpg','2025-04-17'),(48,'8410066070060',1,'Ketchup','Orlando','','https://images.openfoodfacts.org/images/products/841/006/607/0060/front.3.200.jpg','2025-04-17'),(49,'8480012013885',1,'Agua mineral natural','Eliges','','https://images.openfoodfacts.org/images/products/848/001/201/3885/front_es.3.200.jpg','2025-04-17'),(50,'8480000341310',1,'Hierbas provenzales','Hacendado','25 g','https://images.openfoodfacts.org/images/products/848/000/034/1310/front_en.21.200.jpg','2025-04-17'),(51,'8480012036105',1,'Pimienta blanca molida','Eliges',NULL,'https://images.openfoodfacts.org/images/products/848/001/203/6105/front_es.3.200.jpg','2025-04-17'),(52,'8480000866561',1,'Ajo','Hacendado','115g ℮','https://images.openfoodfacts.org/images/products/848/000/086/6561/front_es.24.200.jpg','2025-04-17'),(53,'8413700091261',1,'Pimienta negra molinillo','Carmencita','2','https://images.openfoodfacts.org/images/products/841/370/009/1261/front_es.4.200.jpg','2025-04-17'),(54,'8412206000715',1,'Italia','Toque','40 g','https://images.openfoodfacts.org/images/products/841/220/600/0715/front_es.12.200.jpg','2025-04-17'),(55,'8480000341730',1,'Nuez moscada','Hacendado','58 g','https://images.openfoodfacts.org/images/products/848/000/034/1730/front_es.15.200.jpg','2025-04-17'),(56,'8480000341846',1,'Pimentón dulce','Hacendado','56 g','https://images.openfoodfacts.org/images/products/848/000/034/1846/front_es.54.200.jpg','2025-04-17'),(57,'8480012018248',1,'Cinco pimientas','Eliges, Ifa Eliges','40g',NULL,'2025-04-17'),(58,'8480012017951',1,'Azúcar vainillado',NULL,NULL,'https://images.openfoodfacts.org/images/products/848/001/201/7951/front_es.3.200.jpg','2025-04-17'),(59,'20462338',1,'Épice chili con carne',NULL,'65 g','https://images.openfoodfacts.org/images/products/000/002/046/2338/front_fr.561.200.jpg','2025-04-17'),(60,'8480010109184',1,'Hierbas provenzales','Eroski','22g','https://images.openfoodfacts.org/images/products/848/001/010/9184/front_es.12.200.jpg',NULL),(61,'8480012017944',1,'Canela molida','Eliges','','https://images.openfoodfacts.org/images/products/848/001/201/7944/front_es.7.200.jpg','2025-04-17'),(62,'8480012018064',1,'Comino molido','Eliges','','https://images.openfoodfacts.org/images/products/848/001/201/8064/front_es.3.200.jpg','2025-04-17'),(63,'8410000005349',1,'Royal','Royal','8pcs','https://images.openfoodfacts.org/images/products/841/000/000/5349/front_en.3.200.jpg',NULL),(64,'8412206000708',1,'Ibérico','Toque','40 g','https://images.openfoodfacts.org/images/products/841/220/600/0708/front_es.6.200.jpg','2025-04-29'),(65,'8480000802934',1,'Impulsor','Hacendado','15g','https://images.openfoodfacts.org/images/products/848/000/080/2934/front_es.3.200.jpg','2025-04-17'),(66,'84100559',1,'Cuajada sabor lácteo','Royal','4 sobres x 12g/16 porciones','https://images.openfoodfacts.org/images/products/000/008/410/0559/front_en.23.200.jpg',NULL),(90,'8934563207403',1,'Noodles de arroz','Acecook','200 g','https://images.openfoodfacts.org/images/products/893/456/320/7403/front_es.455.200.jpg',NULL),(96,'8480000331908',1,'Alcaparras','Hacendado','135g','https://images.openfoodfacts.org/images/products/848/000/033/1908/front_es.61.200.jpg','2025-04-30'),(97,'8480000295408',1,'Gurken','Hacendado','420g','https://images.openfoodfacts.org/images/products/848/000/029/5408/front_de.28.200.jpg','2025-04-30'),(98,'8425837005158',1,'Leche Campo Nuestro','Campo Nuestro','1 Litro','https://images.openfoodfacts.org/images/products/842/583/700/5158/front_en.21.200.jpg','2025-04-30'),(99,'8414825338743',1,'Vino rosado mencía espumoso','Alma Atlántica','75 cl','https://images.openfoodfacts.org/images/products/841/482/533/8743/front_es.11.200.jpg','2025-04-30'),(100,'56004861',1,'Cerveja Green 0,33LT (6X4)','super bock','33 cl','https://images.openfoodfacts.org/images/products/000/005/600/4861/front_fr.4.200.jpg','2025-04-30'),(101,'8480012027554',1,'Vino blanco','Eliges','','https://images.openfoodfacts.org/images/products/848/001/202/7554/front_es.20.200.jpg','2025-04-30'),(102,'8480000173621',1,'Salsa Teriyaki','Hacendado','250 ml','https://images.openfoodfacts.org/images/products/848/000/017/3621/front_es.15.200.jpg','2025-04-30'),(107,'8480000173607',1,'Salsa soja','Hacendado','250ml','https://images.openfoodfacts.org/images/products/848/000/017/3607/front_es.33.200.jpg','2025-05-05'),(108,'8710605030044',1,'Sauce Sriracha Mayo','GoTan','100 gram','https://images.openfoodfacts.org/images/products/871/060/503/0044/front_fr.8.200.jpg','2025-05-05'),(110,'8480000340689',1,'Vainilla en rama','Hacendado',NULL,'https://images.openfoodfacts.org/images/products/848/000/034/0689/front_es.3.200.jpg','2025-05-05'),(122,'14101458',1,'Allioli','Chovi','150ml','https://images.openfoodfacts.org/images/products/000/001/410/1458/front_de.7.200.jpg','2025-05-05'),(139,'8480000809421',1,'Tortillas de trigo integrales','Hacendado','360 g','https://images.openfoodfacts.org/images/products/848/000/080/9421/front_en.32.200.jpg','2025-05-05'),(140,'8480000291745',1,'Harina De Espelta Integral','Nurture','1 Kg','https://images.openfoodfacts.org/images/products/848/000/029/1745/front_es.9.200.jpg','2025-05-05'),(141,'8480000349217',1,'Almendra Molida','Casa Pons, Hacendado','125 g','https://images.openfoodfacts.org/images/products/848/000/034/9217/front_es.26.200.jpg','2025-05-05'),(142,'8402001026270',1,'Cous cous mediano',NULL,NULL,NULL,'2025-05-05'),(143,'8410127041145',1,'Sopa de pollo','Knorr','63g','https://images.openfoodfacts.org/images/products/841/012/704/1145/front_es.38.200.jpg','2025-05-05'),(144,'8410069015044',1,'Canelones','Gallo','160 g','https://images.openfoodfacts.org/images/products/841/006/901/5044/front_fr.11.200.jpg','2025-05-05'),(150,'8480000233653',1,'Anacardo caju al natural','Hacendado','200g','https://images.openfoodfacts.org/images/products/848/000/023/3653/front_es.14.200.jpg','2025-05-05'),(151,'3574660533361',48,'Intense Repair','Neutrogena','400ml',NULL,NULL),(152,'8480012017043',1,'Sal gruesa cocina marina','Eliges','','https://images.openfoodfacts.org/images/products/848/001/201/7043/front_es.11.200.jpg','2025-05-05'),(153,'8480000490735',1,'Bolsas congelación','Bosque verde','20uds',NULL,'2025-05-07'),(154,'8480000492241',1,'Bolsas Congelación medianas','Bosque Verde','40 uds',NULL,'2025-05-07'),(155,'8480012026816',1,'Cúrcuma molida','Eliges','','https://images.openfoodfacts.org/images/products/848/001/202/6816/front_es.3.200.jpg','2025-05-05'),(156,'8402001002021',1,'Mezcla de semillas','Hacendado',NULL,'https://images.openfoodfacts.org/images/products/840/200/100/2021/front_en.3.200.jpg','2025-05-05'),(158,'8480000341860',1,'Pimentón dulce','Hacendado','75 g','https://images.openfoodfacts.org/images/products/848/000/034/1860/front_es.38.200.jpg','2025-05-05'),(159,'8480000342423',1,'Pimentón de la Vera picante','Hacendado','75g','https://images.openfoodfacts.org/images/products/848/000/034/2423/front_es.14.200.jpg','2025-05-05'),(160,'8470007148999',1,'Optialerg',NULL,'',NULL,'2025-05-06'),(161,'8480000505439',1,'Tierno de vaca','Entrepinares','300 g','https://images.openfoodfacts.org/images/products/848/000/050/5439/front_es.14.200.jpg','2025-05-07'),(162,'8480000184085',1,'Filetes de anchoa en aceite de oliva','Hacendado','120 g','https://images.openfoodfacts.org/images/products/848/000/018/4085/front_es.21.200.jpg','2025-05-07'),(163,'8484000403436',49,'Suavizante amaciador concentrado','Bosque verde ','2L',NULL,NULL);
INSERT INTO `pantry` VALUES (5,1,9,'2025-04-10'),(41,1,1,'2025-04-30'),(42,1,1,'2025-04-17'),(43,1,1,'2025-04-17'),(44,1,1,'2025-04-17'),(44,2,1,'2025-05-07'),(45,1,1,'2025-04-17'),(46,1,2,'2025-04-17'),(46,2,1,'2025-05-07'),(47,1,2,'2025-04-17'),(47,2,1,'2025-05-07'),(48,1,2,'2025-04-17'),(48,2,1,'2025-05-07'),(49,1,1,'2025-04-17'),(50,1,1,'2025-04-17'),(51,1,1,'2025-04-17'),(52,1,1,'2025-04-17'),(53,1,1,'2025-04-17'),(54,1,2,'2025-04-17'),(55,1,1,'2025-04-17'),(56,1,1,'2025-04-17'),(57,1,1,'2025-04-17'),(58,1,2,'2025-04-17'),(59,1,1,'2025-04-17'),(60,1,1,'2025-04-17'),(61,1,1,'2025-04-17'),(62,1,1,'2025-04-17'),(63,1,2,'2025-04-17'),(64,1,1,'2025-04-17'),(65,1,2,'2025-04-17'),(66,1,13,'2025-04-17'),(90,1,1,'2025-04-30'),(96,2,1,'2025-05-07'),(108,1,1,'2025-05-05'),(108,2,1,'2025-05-07'),(110,1,1,'2025-05-05'),(122,1,21,'2025-05-05'),(139,49,1,'2025-05-07'),(140,49,1,'2025-05-07'),(141,49,1,'2025-05-07'),(150,48,1,'2025-05-06'),(152,48,1,'2025-05-06'),(153,48,1,'2025-05-06'),(154,48,1,'2025-05-06'),(155,48,1,'2025-05-06'),(156,48,1,'2025-05-06'),(160,48,1,'2025-05-06'),(163,49,1,'2025-05-07');
INSERT INTO `recipe_category` VALUES (1,'Entrante'),(2,'Plato principal'),(3,'Postre');
INSERT INTO `shopping_list` VALUES (10,1,1,'2025-04-10'),(42,1,1,'2025-04-29'),(43,1,1,'2025-04-29'),(45,1,1,'2025-04-29'),(46,1,1,'2025-04-29'),(47,1,1,'2025-04-29'),(48,1,1,'2025-04-29'),(98,2,1,'2025-05-07'),(101,2,1,'2025-05-07'),(141,49,1,'2025-05-07'),(151,48,2,'2025-05-05'),(158,48,1,'2025-05-05'),(159,48,1,'2025-05-05'),(161,2,1,'2025-05-07'),(162,2,1,'2025-05-07'),(163,49,1,'2025-05-07');
INSERT INTO `tag` VALUES (2,'azúcar'),(3,'horno'),(4,'ajo'),(5,'caldo'),(6,'flan'),(7,'fria'),(8,'chocolate'),(12,'Bizcocho'),(13,'bizcochodeyogur'),(14,'bizcochofacil'),(15,'recetas'),(16,'recetasricas'),(17,'Pancakes'),(18,'chipsdechocolate'),(19,'desayuno'),(20,'merienda'),(21,'merluza'),(22,'salsaverde'),(23,'pescado'),(24,'Carne'),(25,'lomoensalsa'),(26,'bizcocho facil'),(27,'recetas ricas'),(28,'bizcocho de yogur'),(29,'chips de chocolate'),(30,'Hierro');
INSERT INTO `recipe` VALUES (3,1,3,3,1,'Tarta de manzana','Hornear una base de masa quebrada.\r\nAñadir manzanas, azúcar y hornear.',21,45,'pich72vqdckeyocprrvl','2025-05-21'),(4,2,1,1,0,'Sopa de ajo','Cocer ajos, cebolla, caldo de pollo y huevos batidos.',15,30,'onfkmma1zondeapyteyp','2025-05-21'),(5,1,2,3,1,'Paella de mariscos','Cocer arroz con mariscos, caldo de pescado, tomate y especias.',25,60,'qvuffc5vnl9eyti82ghj','2025-05-21'),(6,1,3,1,0,'Flan casero','Hervir leche con azúcar y huevo, verter en molde y hornear.',10,45,'of8acazvj1zp2zlphcv0','2025-05-21'),(8,48,3,2,0,'Brownie de chocolate','Mezclar chocolate, mantequilla, azúcar y hornear hasta que quede esponjoso.',15,40,'ahxy5tlvqmrw6ukbimec','2025-06-11'),(9,1,2,2,1,'Empanada','Amasar e meter o forno',60,40,'d3jlnpaumuvuw0t3bflw','2025-05-21'),(35,1,3,1,1,'Bizcocho de yogur ','1. Precalentar el horno a 180º\r\n2. Mezclar todos los ingredientes en un recipiente y mezclar hasta conseguir una masa homogénea.\r\n3. Vaciar la mezcla en un molde apto para horno y hornear 35 minutos.',10,35,'tiq0bqcpjmfh7i6ve4f9','2025-05-21'),(36,1,3,1,1,'Pancakes de plátano y chocolate','\r\n1. Batimos todos los ingredientes excepto los chips de chocolate\r\n2. Una vez batido todo, añadimos los chips y removemos. \r\n3. Ponemos una sartén antiadherente al fuego y ponemos la masa, dejamos cocinar por un lado, le damos la vuelta y dejamos que se cocine totalmente.\r\n',20,10,'kljt1bveb3ielz8mw3ro','2025-05-21'),(37,1,2,1,1,'Merluza en salsa verde','1. Cocer los guisantes con una pizca de sal y triturarlos con un poco del agua en donde los cocimos.\r\n2. Cortar el ajo y la cebolla en cuadraditos pequeños y pocharlo con un chorro de AOVE.\r\n3. Añadir la harina de arroz y remover para que junte todo. \r\n4. Añadir el vaso y medio de vino blanco. Dejar evaporar\r\n5. Añadir el perejil picado y los guisantes triturados. Dejar cocinar unos 5’ \r\n6. Añadir los lomos de merluza con la piel para abajo y tapar. Ir moviendo para que no se pegue. Sazonar. En unos 8’ la merluza ya estará cocinada.\r\n',30,30,'vvbujvxkg4xxxx3wduiq','2025-05-21'),(38,1,2,1,1,'Lomo en salsa ','1. Precalentar el horno a 180º\r\n2. Hacemos el adobo para el lomo; en el vaso de la batidora, ponemos dos dientes de ajo pelados, medio vasito de AOVE y un vaso de vino blanco y batimos.\r\n3. Untamos el adobo por todo el lomo y sazonamos. \r\n4. Metemos en el horno durante 40’ más o menos.\r\n5. Para hacer la salsa, picamos todos las verduras y las pochamos. El corte de las verduras da igual, ya que luego vamos a triturarlo todo. \r\n6. Cuando las verduras estén pochadas echamos un vasito de agua (para que la salsa sea más líquida), y batimos. \r\n7. Sacamos el lomo del horno y dejamos enfriar. Una vez frío lo cortamos en rodajas. \r\n8. Ponemos las rodajas en la olla donde tengamos la salsa ya batida, dejarmos cocinar 5 minutos para que la carne coja el sabor de la salsa y... LISTO!! ',60,60,'jmunn49eu3g4havms2ej','2025-05-21'),(46,1,1,1,1,'Ensalada cesar','Mezclar y alinear',5,5,'r5kr3exj14fjns9zxskb','2025-05-21');
INSERT INTO `recipe_tag` VALUES (3,2),(8,2),(3,3),(9,3),(4,4),(4,5),(6,6),(8,8),(35,12),(35,15),(36,17),(36,19),(36,20),(37,21),(37,22),(37,23),(38,24),(38,25),(35,26),(35,27),(35,28),(36,29);
INSERT INTO `recipe_ingredient` VALUES (3,1),(3,2),(3,3),(6,3),(3,4),(4,5),(4,6),(38,6),(8,7),(9,7),(9,8),(9,9),(46,10),(38,13),(46,13),(6,14),(6,15),(8,16),(35,30),(35,31),(37,31),(35,32),(37,32),(38,32),(35,33),(35,34),(35,35),(36,36),(36,37),(36,38),(36,39),(37,40),(37,41),(37,42),(38,42),(37,43),(38,43),(37,44),(37,45),(38,45),(37,46),(38,46),(38,47),(38,48),(38,49),(38,50),(5,53),(5,58),(46,59),(46,60),(46,61);

-- Consultas iniciales
SELECT * FROM product;
SELECT * FROM users;
SELECT * FROM pantry;
SELECT * FROM shopping_list;
SELECT * FROM recipe;
SELECT * FROM recipe_category;
SELECT * FROM difficulty;
SELECT * FROM tag;
SELECT * FROM recipe_tag;
SELECT * FROM ingredient;
SELECT * FROM recipe_ingredient;