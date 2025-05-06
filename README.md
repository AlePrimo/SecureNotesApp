# 🛡️ SecureNotesApp

Aplicación web para tomar notas protegida con autenticación y autorización basada en roles.  
Ideal para aprender y practicar **Spring Boot**, **Spring Security con JWT**, **roles y permisos**, y diseño limpio en capas.

---

## 🚀 ¿Qué hace esta app?

SecureNotesApp permite a los usuarios:

- 📝 Crear, editar, ver y eliminar **notas personales**.
- 👤 Registrarse y loguearse con un **token JWT**.
- 🔐 Controlar el acceso a funciones según su **rol de usuario**.

---

## 🧩 Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Security con JWT
- PostgreSQL o MySQL (configurable)
- JPA / Hibernate
- Maven
- Lombok

---

## 🧪 Roles y permisos

El sistema está basado en 3 roles principales:

| Rol        | Descripción                                                                 |
|------------|-----------------------------------------------------------------------------|
| `USER`     | Puede crear y gestionar **solo sus propias notas**.                        |
| `ADMIN`    | Puede ver y editar **todas las notas**. No puede crear usuarios `ADMIN`.   |
| `DEVELOPER`| Tiene todos los permisos. Solo ellos pueden crear usuarios `ADMIN` o `DEVELOPER`. |

> ⚠️ Los permisos se cargan automáticamente al iniciar la app.

---

## 🔑 Seguridad implementada

- Autenticación con JWT.
- Validación del token en cada request.
- Acceso a endpoints protegido por roles.
- Las notas **solo pueden ser vistas o editadas** por su dueño, o por un admin/developer.
- Solo los `DEVELOPER` pueden crear otros usuarios `ADMIN` o `DEVELOPER`.

---

## 📂 Estructura del proyecto

src/main/java/com/app
│
├── controllers/ // Controladores REST
├── services/ // Lógica de negocio (interfaz + implementación)
├── persistence/ // DAOs que acceden a base de datos
├── entities/ // Entidades JPA: User, Role, Permission, Note
├── security/ // Filtro JWT, utils, configuración de seguridad
├── dtos/ // Objetos para recibir o enviar datos
└── config/ // Configuración de seguridad y aplicación


🔐 Autenticación
Método	Endpoint	Descripción
POST	/api/auth/sign-up	Registro de usuario (excepto ADMIN/DEV)
POST	/api/auth/log-in	Login y obtención de token
POST	/api/auth/create-admin	Crear usuario ADMIN (solo DEVELOPER)
POST	/api/auth/create-developer	Crear DEVELOPER (solo DEVELOPER)


📝 Notas
Método	Endpoint	Descripción
GET	/api/notes/findAll	Devuelve todas las notas (según el rol)
GET	/api/notes/findById/{id}	Devuelve una nota por ID (según permisos)
POST	/api/notes/saveNote	Crea una nueva nota (autenticado)
PUT	/api/notes/updateNote/{id}	Actualiza una nota (si sos dueño o admin)
DELETE	/api/notes/deleteById/{id}	Borra una nota (solo admin o developer)


