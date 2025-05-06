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

