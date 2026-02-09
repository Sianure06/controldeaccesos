[README.md](https://github.com/user-attachments/files/25191473/README.md)
# 🔐 SICA v2.0 - Sistema Inteligente de Control de Accesos

El programa SICA es un sistema robusto de gestión de identidades y control de usuarios desarrollado en Java. SICA v2.0 implementa estándares modernos de seguridad para la protección de credenciales y una arquitectura modular basada en roles.

### 🎮 Funcionalidades Principales

•	Sistema de Autenticación Segura (Login)
Implementado en VentanaPrincipal.java y SicaEngine.java. Valida credenciales de usuario mediante el analisis de una base de datos local, gestionando sesiones activas.
•	Cifrado de Contraseñas con BCrypt
Usa la clase EncriptadorContrasena.java para aplicar hashing a las contraseñas. No se guardan textos planos, lo que protege el sistema contra filtraciones de datos.
•	Gestión de Roles y Permisos (RBAC)
A través de FabricaVentanasUsuario.java, el sistema identifica si el usuario es Admin, Supervisor, Auditor o Normal, restringiendo botones y funciones según el nivel de acceso.
•	Bloqueo Automático de Seguridad
En UsuarioModel.java, el sistema cuenta los intentos fallidos. Al llegar a 3 intentos, bloquea la cuenta automáticamente por 15 minutos, registrando la fechaDesbloqueo.
•	Recuperación de Cuenta mediante Preguntas Secretas
Lógica presente en PreguntasSeguridad.java y SicaEngine.java. Permite restablecer el acceso validando respuestas predefinidas que también están hasheadas para mayor seguridad.
•	Auditoría y Registro de Accesos (Logs)
El GestorBaseDatos.java registra cada entrada al sistema en un archivo de texto, permitiendo un historial de quién entró y en qué fecha.
•	Generación de Estadísticas Visuales
En VentanaAdministrador.java, existe lógica para renderizar gráficos (probablemente de barras o líneas) basados en los datos de acceso de los últimos días.
•	Motor de Copias de Seguridad (Backup)
Permite a los administradores crear respaldos de la base de datos de usuarios (usuarios.txt) y gestionarlos desde la interfaz.
•	Restauración de Sistema
•	Funcionalidad para revertir la base de datos a un estado anterior seleccionando un archivo de backup previo, implementado en el motor de datos.
•	Gestión de Usuarios (CRUD)
Permite crear nuevos usuarios, editar sus perfiles, cambiar roles y eliminar registros (funcionalidad completa en el panel de Administrador).


### 🛠️ Características Técnicas

* **Arquitectura**: Utilizo los Patrones: Singleton, Factory Method, proxy,  y Facade.
* **Persistencia**: Manejo eficiente de archivos planos para máxima portabilidad.
* **Análisis de Datos**: Generación de reportes y estadísticas de acceso por fechas.
* **Normalización**: Procesamiento de entradas para evitar errores de duplicidad y sensibilidad a mayúsculas.


## 🖼️ Pantallas
<img width="626" height="548" alt="Captura de pantalla 2026-02-08 191653" src="https://github.com/user-attachments/assets/a1e39508-cbb1-4813-8514-e35010b88ab2" />

<img width="1566" height="875" alt="Captura de pantalla 2026-02-08 191339" src="https://github.com/user-attachments/assets/cf4c1f02-08e7-468a-b4ca-c1c89abf0d29" />


## ⚙️ Requisitos

### Requisitos del Sistema

* **Java JDK**: 17 o superior.
* **Librerías**: `jbcrypt.jar` (necesaria para el cifrado).
* **Recursos**: Carpeta `icons/` con los activos gráficos y carpeta `data/` para la persistencia.
* **Sistema Operativo**: 

---

## 🚀 Instalación y Uso

1. **Clonar o descargar** el repositorio.
2. **Importar** el proyecto en tu IDE favorito (NetBeans, IntelliJ o Eclipse).
3. **Compilar y Ejecutar** la clase `Controldeaccesos.java`.

### Guía de Uso Rápido

1. **Inicio**: Espera a que el Splash Screen termine de cargar los módulos.
2. **Login**: Ingresa con las credenciales de administrador (o el usuario registrado).
3. **Seguridad**: Configura tus preguntas secretas en el primer inicio para habilitar la recuperación.
4. **Administración**: Desde el panel principal, gestiona usuarios o realiza copias de seguridad de la base de datos.

---

## 📁 Estructura del Proyecto

```text
src/
├── core/
│   ├── SicaEngine.java         # Fachada de lógica de negocio
│   └── SicaConfig.java         # Configuraciones globales
├── data/
│   └── GestorBaseDatos.java    # Motor de persistencia CSV
├── models/
│   └── UsuarioModel.java       # Entidad de usuario
├── ui/
│   ├── FabricaVentanas.java    # Patrón Factory para UI
│   ├── VentanaPrincipal.java   # Login y arranque
│   └── VentanaAdministrador.java # Panel de control principal
└── resources/                  # Iconos y multimedia

```

---

## 🛠️ Tecnologías

* **Lenguaje**: Java 17
* **Interfaz Gráfica**: Java Swing / AWT
* **Algoritmo de Hash**: BCrypt (Blowfish)
* **Persistencia**: Archivos Planos (.txt)

---

## 👤 Autor

**Orlando Cabrera** - *Desarrollador*
**Samuel González** - *Desarrollador*
