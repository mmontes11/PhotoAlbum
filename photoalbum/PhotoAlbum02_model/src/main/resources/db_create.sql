DROP TABLE IF EXISTS TAG_ALBUM;
DROP TABLE IF EXISTS TAG_ARCHIVO;
DROP TABLE IF EXISTS TAG;
DROP TABLE IF EXISTS USUARIO;
DROP TABLE IF EXISTS ALBUM;
DROP TABLE IF EXISTS ARCHIVO;
DROP TABLE IF EXISTS SHARE_INFORMATION_ALBUMS;
DROP TABLE IF EXISTS SHARE_INFORMATION_ARCHIVOS;
DROP TABLE IF EXISTS COMMENT;
DROP TABLE IF EXISTS COMMENT_ARCHIVO;
DROP TABLE IF EXISTS COMMENT_ALBUM;
DROP TABLE IF EXISTS LIKE_TABLE;
DROP TABLE IF EXISTS LIKE_COMMENT;
DROP TABLE IF EXISTS LIKE_ARCHIVO;
DROP TABLE IF EXISTS LIKE_ALBUM;


CREATE TABLE IF NOT EXISTS USUARIO
    (
        ID INTEGER NOT NULL AUTO_INCREMENT,
        USERNAME VARCHAR(100) NOT NULL,
        EMAIL VARCHAR(100) NOT NULL,
        PASSWORD VARCHAR(100) NOT NULL,
        CONSTRAINT PK_USUARIO PRIMARY KEY (ID),
        CONSTRAINT USUARIO_UNIQUE_EMAIL UNIQUE (EMAIL),
        CONSTRAINT USUARIO_UNIQUE_USERNAME UNIQUE (USERNAME)
    );
	
CREATE TABLE IF NOT EXISTS ALBUM
    (
        ID INTEGER NOT NULL AUTO_INCREMENT,
        NAME VARCHAR(100) NOT NULL,
        ALBUM_DATE DATE NOT NULL,
        USER_ID INTEGER NOT NULL,
        CONSTRAINT PK_ALBUM PRIMARY KEY (ID),
        CONSTRAINT FK_ALBUM FOREIGN KEY (USER_ID) REFERENCES USUARIO (ID) ON DELETE CASCADE,
        CONSTRAINT ALBUM_UNIQUE_NAME UNIQUE (NAME, USER_ID)
    );
	
CREATE TABLE IF NOT EXISTS ARCHIVO
    (
        ID INTEGER NOT NULL AUTO_INCREMENT,
        NAME VARCHAR(100) NOT NULL,
        FILE BLOB(2147483647) NOT NULL,
        FILE_SMALL BLOB(2147483647) NOT NULL,
        FILE_DATE DATE NOT NULL,
        ALBUM_ID INTEGER NOT NULL,
        CONSTRAINT PK_ARCHIVO PRIMARY KEY (ID),
        CONSTRAINT FK_USUARIO FOREIGN KEY (ALBUM_ID) REFERENCES ALBUM (ID) ON DELETE CASCADE
    );
	
CREATE TABLE IF NOT EXISTS SHARE_INFORMATION_ALBUMS
    (
        ID INTEGER NOT NULL AUTO_INCREMENT,
        ALBUM_ID INTEGER NOT NULL,
        USER_ID INTEGER NOT NULL,
        CONSTRAINT PK_SHARE_INFORMATION_ALBUMS PRIMARY KEY (ID),
        CONSTRAINT FK_SHARE_INFORMATION_ALBUMS_ALBUM FOREIGN KEY (ALBUM_ID) REFERENCES ALBUM (ID) ON DELETE CASCADE,
        CONSTRAINT FK_SHARE_INFORMATION_ALBUMS_USUARIO FOREIGN KEY (USER_ID) REFERENCES USUARIO (ID) ON DELETE CASCADE,
        CONSTRAINT SALBUMS_UNIQUE_ALBUM_ID_USER_ID UNIQUE (ALBUM_ID, USER_ID)
    );
    
CREATE TABLE IF NOT EXISTS SHARE_INFORMATION_ARCHIVOS
    (
        ID INTEGER NOT NULL AUTO_INCREMENT,
        ARCHIVO_ID INTEGER NOT NULL,
        USER_ID INTEGER NOT NULL,
        CONSTRAINT PK_SHARE_INFORMATION_ARCHIVOS PRIMARY KEY (ID),
        CONSTRAINT FK_SHARE_INFORMATION_ARCHIVOS_ALBUM FOREIGN KEY (ARCHIVO_ID) REFERENCES ARCHIVO (ID) ON DELETE CASCADE,
        CONSTRAINT FK_SHARE_INFORMATION_ARCHIVOS_USUARIO FOREIGN KEY (USER_ID) REFERENCES USUARIO (ID) ON DELETE CASCADE,
        CONSTRAINT ARCHIVOS_UNIQUE_ALBUM_ID_USER_ID UNIQUE (ARCHIVO_ID, USER_ID)
    );
    
CREATE TABLE IF NOT EXISTS TAG
	(
		ID INTEGER NOT NULL AUTO_INCREMENT,
		NAME VARCHAR(100) NOT NULL,
		CONSTRAINT PK_TAG PRIMARY KEY (ID),
		CONSTRAINT TAG_UNIQUE_NAME UNIQUE (NAME)
	);
	
    
CREATE TABLE IF NOT EXISTS TAG_ALBUM
	(
		ID INTEGER NOT NULL AUTO_INCREMENT,
		TAG_ID INTEGER NOT NULL,
		ALBUM_ID INTEGER NOT NULL,
		CONSTRAINT PK_TAG_ALBUM PRIMARY KEY (ID),
		CONSTRAINT FK_TAG_ALBUM_TAG FOREIGN KEY (TAG_ID) REFERENCES TAG (ID) ON DELETE CASCADE,
		CONSTRAINT FK_TAG_ALBUM_ALBUM FOREIGN KEY (ALBUM_ID) REFERENCES ALBUM (ID) ON DELETE CASCADE,
		CONSTRAINT TAG_ALBUM_UNIQUE_TAG_ID_ALBUM_ID UNIQUE (TAG_ID, ALBUM_ID)
	);

CREATE TABLE IF NOT EXISTS TAG_ARCHIVO
	(
		ID INTEGER NOT NULL AUTO_INCREMENT,
		TAG_ID INTEGER NOT NULL,
		ARCHIVO_ID INTEGER NOT NULL,
		CONSTRAINT PK_TAG_ARCHIVO PRIMARY KEY (ID),
		CONSTRAINT FK_TAG_ARCHIVO_TAG FOREIGN KEY (TAG_ID) REFERENCES TAG (ID) ON DELETE CASCADE,
		CONSTRAINT FK_TAG_ARCHIVO_ARCHIVO FOREIGN KEY (ARCHIVO_ID) REFERENCES ARCHIVO (ID) ON DELETE CASCADE,
		CONSTRAINT TAG_ARCHIVO_UNIQUE_TAG_ID_ARCHIVO_ID UNIQUE (TAG_ID, ARCHIVO_ID)
	);
	
CREATE TABLE IF NOT EXISTS COMMENT
	(
		ID INTEGER NOT NULL AUTO_INCREMENT,
		COMMENT_TEXT VARCHAR(255) NOT NULL,
		COMMENT_DATE DATE NOT NULL,
		USER_ID INTEGER NOT NULL,
		CONSTRAINT PK_COMMENT PRIMARY KEY (ID),
		CONSTRAINT FK_COMMENT_USER FOREIGN KEY (USER_ID) REFERENCES USUARIO (ID) ON DELETE CASCADE
	);
	
CREATE TABLE IF NOT EXISTS COMMENT_ARCHIVO
	(
		ID INTEGER NOT NULL AUTO_INCREMENT,
		COMMENT_ID INTEGER NOT NULL,
		ARCHIVO_ID INTEGER NOT NULL,
		CONSTRAINT PK_COMMENT_ARCHIVO PRIMARY KEY(ID),
		CONSTRAINT FK_COMMENT_ARCHIVO_COMMENT FOREIGN KEY (COMMENT_ID) REFERENCES COMMENT (ID) ON DELETE CASCADE, 
		CONSTRAINT FK_COMMENT_ARCHIVO_ARCHIVO FOREIGN KEY (ARCHIVO_ID) REFERENCES ARCHIVO (ID) ON DELETE CASCADE
	);
	
CREATE TABLE IF NOT EXISTS COMMENT_ALBUM
	(
		ID INTEGER NOT NULL AUTO_INCREMENT,
		COMMENT_ID INTEGER NOT NULL,
		ALBUM_ID INTEGER NOT NULL,
		CONSTRAINT PK_COMMENT_ALBUM PRIMARY KEY(ID),
		CONSTRAINT FK_COMMENT_ALBUM_COMMENT FOREIGN KEY (COMMENT_ID) REFERENCES COMMENT (ID) ON DELETE CASCADE,
		CONSTRAINT FK_COMMENT_ALBUM_ALBUM FOREIGN KEY (ALBUM_ID) REFERENCES ALBUM (ID) ON DELETE CASCADE
	);
	
CREATE TABLE IF NOT EXISTS LIKE_TABLE
	(
		ID INTEGER NOT NULL AUTO_INCREMENT,
		MEGUSTA INTEGER NOT NULL,
		USER_ID INTEGER NOT NULL,
		CONSTRAINT PK_LIKE_TABLE PRIMARY KEY (ID),
		CONSTRAINT FK_LIKE_TABLE_USER FOREIGN KEY (USER_ID) REFERENCES USUARIO (ID),
	);
	
CREATE TABLE IF NOT EXISTS LIKE_COMMENT 
	(
		ID INTEGER NOT NULL AUTO_INCREMENT,
		LIKE_ID INTEGER NOT NULL,
		COMMENT_ID INTEGER NOT NULL,
		CONSTRAINT PK_LIKE_COMMENT PRIMARY KEY(ID),
		CONSTRAINT FK_LIKE_COMMENT_LIKE_TABLE FOREIGN KEY (LIKE_ID) REFERENCES LIKE_TABLE(ID) ON DELETE CASCADE,		
		CONSTRAINT FK_LIKE_COMMENT_COMMENT FOREIGN KEY (COMMENT_ID) REFERENCES COMMENT (ID) ON DELETE CASCADE,
		CONSTRAINT UNIQUE_LIKE_COMMENT UNIQUE (LIKE_ID,COMMENT_ID)
	);
	
CREATE TABLE IF NOT EXISTS LIKE_ARCHIVO 
	(
		ID INTEGER NOT NULL AUTO_INCREMENT,
		LIKE_ID INTEGER NOT NULL,
		ARCHIVO_ID INTEGER NOT NULL,
		CONSTRAINT PK_LIKE_ARCHIVO PRIMARY KEY(ID),
		CONSTRAINT FK_LIKE_ARCHIVO_LIKE_TABLE FOREIGN KEY (LIKE_ID) REFERENCES LIKE_TABLE(ID) ON DELETE CASCADE,		
		CONSTRAINT FK_LIKE_ARCHIVO_COMMENT FOREIGN KEY (ARCHIVO_ID) REFERENCES ARCHIVO (ID) ON DELETE CASCADE,
		CONSTRAINT UNIQUE_LIKE_ARCHIVO UNIQUE (LIKE_ID,ARCHIVO_ID)
	);
	
CREATE TABLE IF NOT EXISTS LIKE_ALBUM 
	(
		ID INTEGER NOT NULL AUTO_INCREMENT,
		LIKE_ID INTEGER NOT NULL,
		ALBUM_ID INTEGER NOT NULL,
		CONSTRAINT PK_LIKE_ALBUM PRIMARY KEY(ID),
		CONSTRAINT FK_LIKE_ALBUM_LIKE_TABLE FOREIGN KEY (LIKE_ID) REFERENCES LIKE_TABLE(ID) ON DELETE CASCADE,		
		CONSTRAINT FK_LIKE_ALBUM_COMMENT FOREIGN KEY (ALBUM_ID) REFERENCES ALBUM (ID) ON DELETE CASCADE,
		CONSTRAINT UNIQUE_LIKE_ALBUM UNIQUE (LIKE_ID,ALBUM_ID)
	);		
	
		
--Constant
--Warning: Hay un test que busca a public por el id 1
INSERT INTO USUARIO VALUES(1,'public','public','bff29fe2c3269812851d6fda69b3472d');

--Password: 12345678aA	
INSERT INTO USUARIO VALUES(2,'a','a@a.es','bff29fe2c3269812851d6fda69b3472d');
INSERT INTO USUARIO VALUES(3,'b','b@b.es','bff29fe2c3269812851d6fda69b3472d');