/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.controldeaccesos.EncriptadorContrasena;
import com.mycompany.controldeaccesos.models.UsuarioModel;

public class GestorBaseDatos {

    private static GestorBaseDatos instancia;
    private static final String RUTA_ARCHIVO = "data/usuarios.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private boolean conectado = false;

    private GestorBaseDatos() {
    }

    public static synchronized GestorBaseDatos getInstancia() {
        if (instancia == null) {
            instancia = new GestorBaseDatos();
        }
        return instancia;
    }

    public void conectar() {
        try {
            Files.createDirectories(Paths.get("data"));
            File archivo = new File(RUTA_ARCHIVO);

            if (!archivo.exists()) {
                archivo.createNewFile();
                crearUsuarioAdministrador();
            }

            conectado = true;
            System.out.println("Conectado a la base de datos TXT en: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }

    public void desconectar() {
        conectado = false;
        System.out.println("Desconectado de la base de datos TXT");
    }

    // ========== MÉTODOS PÚBLICOS (PROXY) ==========
    public boolean autenticarUsuario(String username, String password) {
        if (!conectado || username == null || password == null) {
            return false;
        }

        UsuarioModel usuario = buscarUsuario(username);
        if (usuario == null || !usuario.isActivo()) {
            return false;
        }

        // Verificar si el usuario está bloqueado
        if (usuario.estaBloqueado()) {
            System.err.println("Proxy: Usuario " + username + " está bloqueado hasta "
                    + usuario.getFechaDesbloqueo().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            return false;
        }

        boolean valido = EncriptadorContrasena.verificarContrasena(password, usuario.getPasswordHash());

        if (valido) {
            // Login exitoso - resetear intentos fallidos
            usuario.resetearIntentosFallidos();
            usuario.setUltimoAcceso(LocalDateTime.now());
            actualizarUsuario(usuario);

            // REGISTRAR EL ACCESO EXITOSO
            registrarAcceso(username);

            System.out.println("Proxy: Login exitoso para " + username);
        } else {
            // Login fallido - incrementar intentos fallidos
            usuario.incrementarIntentosFallidos();
            actualizarUsuario(usuario);

            System.err.println("Proxy: Contraseña incorrecta para " + username
                    + " - Intentos fallidos: " + usuario.getIntentosFallidos());

            if (usuario.isBloqueado()) {
                System.err.println("Proxy: Usuario " + username + " BLOQUEADO por 15 minutos");
            }
        }

        return valido;
    }

    public boolean registrarUsuario(String username, String password, String nombreCompleto,
            String email, String preguntaSecreta, String respuestaSecreta) {
        if (!conectado) {
            return false;
        }

        if (usuarioExiste(username)) {
            throw new IllegalArgumentException("El usuario '" + username + "' ya existe");
        }

        if (emailExiste(email)) {
            throw new IllegalArgumentException("El email '" + email + "' ya está registrado");
        }

        // Encriptar contraseña
        String passwordHash = EncriptadorContrasena.encriptarContrasena(password);

        // Normalizar y encriptar respuesta secreta
        String respuestaNormalizada = respuestaSecreta.trim().toLowerCase();
        String respuestaHash = EncriptadorContrasena.encriptarContrasena(respuestaNormalizada);

        // Crear usuario
        UsuarioModel usuario = new UsuarioModel(username, passwordHash, email, nombreCompleto, "USUARIO");
        usuario.setPreguntaSecreta(preguntaSecreta.trim());
        usuario.setRespuestaSecretaHash(respuestaHash);

        // Guardar en archivo
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            writer.println(toCSV(usuario));
            System.out.println("Proxy: Usuario '" + username + "' registrado exitosamente");
            return true;
        } catch (IOException e) {
            System.err.println("Proxy: Error al registrar: " + e.getMessage());
            throw new IllegalArgumentException("Error al guardar en la base de datos: " + e.getMessage());
        }
    }

    public boolean eliminarUsuario(String username) {
        if (!conectado || username == null || username.trim().isEmpty()) {
            return false;
        }

        File file = new File(RUTA_ARCHIVO);
        if (!file.exists()) {
            return false;
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            List<String> kept = new ArrayList<>();
            boolean removed = false;

            for (String line : lines) {
                if (line == null || line.trim().isEmpty() || line.startsWith("#")) {
                    kept.add(line);
                    continue;
                }

                String[] parts = line.split(",");
                String userPart = parts.length > 0 ? parts[0].trim() : "";

                if (userPart.equals(username)) {
                    removed = true;
                    System.out.println("Proxy: Usuario eliminado - " + username);
                    continue;
                }

                kept.add(line);
            }

            if (removed) {
                Files.write(file.toPath(), kept);
            }

            return removed;
        } catch (IOException e) {
            System.err.println("Proxy: Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    public Map<String, Integer> getEstadisticasAccesos(int ultimosDias) {
        return obtenerAccesosPorFecha(ultimosDias);
    }

    // Método para obtener total de accesos hoy
    public int getAccesosHoy() {
        Map<String, Integer> accesosHoy = obtenerAccesosPorFecha(1);
        String hoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return accesosHoy.getOrDefault(hoy, 0);
    }

    public int obtenerTotalIntentosFallidosHoy() {
        if (!conectado) {
            return 0;
        }

        int total = 0;
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("#") || linea.trim().isEmpty()) {
                    continue;
                }

                UsuarioModel usuario = fromCSV(linea);
                if (usuario != null
                        && usuario.getUltimoIntentoFallido() != null
                        && usuario.getUltimoIntentoFallido().isAfter(inicioDia)) {
                    total += usuario.getIntentosFallidos();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al calcular intentos fallidos: " + e.getMessage());
        }

        return total;
    }

    public int obtenerUsuariosBloqueados() {
        if (!conectado) {
            return 0;
        }

        int bloqueados = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("#") || linea.trim().isEmpty()) {
                    continue;
                }

                UsuarioModel usuario = fromCSV(linea);
                if (usuario != null && usuario.estaBloqueado()) {
                    bloqueados++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al contar usuarios bloqueados: " + e.getMessage());
        }

        return bloqueados;
    }

    public List<UsuarioModel> obtenerUsuariosConIntentosFallidos() {
        List<UsuarioModel> usuariosConIntentos = new ArrayList<>();
        if (!conectado) {
            return usuariosConIntentos;
        }

        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("#") || linea.trim().isEmpty()) {
                    continue;
                }

                UsuarioModel usuario = fromCSV(linea);
                if (usuario != null
                        && usuario.getIntentosFallidos() > 0
                        && usuario.getUltimoIntentoFallido() != null
                        && usuario.getUltimoIntentoFallido().isAfter(inicioDia)) {
                    usuariosConIntentos.add(usuario);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al obtener usuarios con intentos fallidos: " + e.getMessage());
        }

        // Ordenar por número de intentos fallidos (mayor primero)
        usuariosConIntentos.sort((u1, u2) -> Integer.compare(u2.getIntentosFallidos(), u1.getIntentosFallidos()));

        return usuariosConIntentos;
    }

    public boolean desbloquearUsuario(String username) {
        UsuarioModel usuario = buscarUsuario(username);
        if (usuario == null) {
            return false;
        }

        usuario.resetearIntentosFallidos();
        return actualizarUsuario(usuario);
    }

    public boolean cambiarEstadoUsuario(String username, boolean activo) {
        UsuarioModel usuario = buscarUsuario(username);
        if (usuario == null) {
            return false;
        }

        usuario.setActivo(activo);
        return actualizarUsuario(usuario);
    }

    public boolean cambiarRolUsuario(String username, String nuevoRol) {
        UsuarioModel usuario = buscarUsuario(username);
        if (usuario == null) {
            return false;
        }

        usuario.setRol(nuevoRol.toUpperCase());
        return actualizarUsuario(usuario);
    }

    // ========== MÉTODOS DE CONSULTA ==========
    public boolean usuarioExiste(String username) {
        return buscarUsuario(username) != null;
    }

    public UsuarioModel getUsuario(String username) {
        return buscarUsuario(username);
    }

    public List<UsuarioModel> obtenerTodosUsuarios() {
        List<UsuarioModel> usuarios = new ArrayList<>();
        if (!conectado) {
            return usuarios;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("#") || linea.trim().isEmpty()) {
                    continue;
                }
                UsuarioModel u = fromCSV(linea);
                if (u != null) {
                    usuarios.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    public boolean cambiarPassword(String username, String nuevaPassword) {
        UsuarioModel usuario = buscarUsuario(username);
        if (usuario == null) {
            return false;
        }

        String nuevoHash = EncriptadorContrasena.encriptarContrasena(nuevaPassword);
        usuario.setPasswordHash(nuevoHash);
        return actualizarUsuario(usuario);
    }

    public boolean verificarRespuestaSecreta(String username, String respuesta) {
        UsuarioModel usuario = buscarUsuario(username);
        if (usuario == null) {
            return false;
        }

        String respuestaHash = usuario.getRespuestaSecretaHash();
        if (respuestaHash == null || respuestaHash.isEmpty()) {
            return false;
        }

        String respuestaNormalizada = respuesta.trim().toLowerCase();
        return EncriptadorContrasena.verificarContrasena(respuestaNormalizada, respuestaHash);
    }

    public String obtenerPreguntaSecreta(String username) {
        UsuarioModel usuario = buscarUsuario(username);
        return (usuario != null) ? usuario.getPreguntaSecreta() : null;
    }

    public boolean tienePreguntaSecreta(String username) {
        UsuarioModel usuario = buscarUsuario(username);
        return usuario != null && usuario.tienePreguntaSecreta();
    }

    public String generarCodigoRecuperacion(String username) {
        UsuarioModel usuario = buscarUsuario(username);
        if (usuario == null) {
            return null;
        }

        // Generar código numérico de 6 dígitos para cumplir con la validación de la UI
        java.security.SecureRandom random = new java.security.SecureRandom();
        String codigo = String.format("%06d", random.nextInt(1000000));

        usuario.setCodigoRecuperacion(codigo);
        usuario.setExpiracionCodigo(LocalDateTime.now().plusMinutes(15));

        return actualizarUsuario(usuario) ? codigo : null;
    }

    public boolean restablecerContrasena(String username, String nuevaContrasena, String codigoVerificacion) {
        UsuarioModel usuario = buscarUsuario(username);
        if (usuario == null) {
            return false;
        }

        // Verificar código
        String codigoAlmacenado = usuario.getCodigoRecuperacion();
        LocalDateTime expiracion = usuario.getExpiracionCodigo();

        if (codigoAlmacenado == null || !codigoAlmacenado.equals(codigoVerificacion)
                || expiracion.isBefore(LocalDateTime.now())) {
            return false;
        }

        // Cambiar contraseña
        String nuevoHash = EncriptadorContrasena.encriptarContrasena(nuevaContrasena);
        usuario.setPasswordHash(nuevoHash);
        usuario.setCodigoRecuperacion("");

        return actualizarUsuario(usuario);
    }

    public void crearBackup() throws IOException {
        LocalDateTime ahora = LocalDateTime.now();
        String timestamp = ahora.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFile = "data/backup/usuarios_backup_" + timestamp + ".txt";

        Files.createDirectories(Paths.get("data/backup"));
        Files.copy(Paths.get(RUTA_ARCHIVO), Paths.get(backupFile), StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Backup creado: " + backupFile);
    }

    public int contarBackups() {
        try {
            File backupDir = new File("data/backup");
            if (!backupDir.exists() || !backupDir.isDirectory()) {
                return 0;
            }

            File[] backupFiles = backupDir.listFiles((dir, name)
                    -> name.startsWith("usuarios_backup_") && name.endsWith(".txt")
            );

            return backupFiles != null ? backupFiles.length : 0;
        } catch (Exception e) {
            System.err.println("Error al contar backups: " + e.getMessage());
            return 0;
        }
    }

    public void cambiarBackups(int numeroBackups) {
        try {
            File backupOrigin = new File("data/backup");
            File backupDestiny = new File("data/usuarios.txt");
            if (!backupOrigin.exists() || !backupOrigin.isDirectory()) {
                System.err.println("El directorio de backup no existe o no es un directorio.");
            } else if (backupOrigin.exists() || !backupDestiny.isDirectory()) {
                System.err.println("El directorio de los usuarios no existe o no es un directorio.");
            } else if (backupOrigin.listFiles() == null || backupOrigin.listFiles().length == 0) {
                System.err.println("No hay archivos de backup disponibles.");
            } else {
                File[] backupFiles = backupOrigin.listFiles((dir, name)
                        -> name.startsWith("usuarios_backup_") && name.endsWith(".txt")
                );

                if (backupFiles != null) {
                    // Ordenar por fecha (más reciente primero)
                    Arrays.sort(backupFiles, (f1, f2) -> {
                        try {
                            return Long.compare(f2.lastModified(), f1.lastModified());
                        } catch (Exception e) {
                            return 0;
                        }
                    });

                    // Tomar el backup más reciente
                    File backupToRestore = backupFiles[numeroBackups + 1];
                    Files.copy(backupToRestore.toPath(), backupDestiny.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Backup restaurado: " + backupToRestore.getName());
                } else {
                    System.err.println("No se encontraron archivos de backup.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cambiar backups: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Número de backup inválido: " + numeroBackups);
        }
    }

    public List<String> obtenerListaBackups() {
        List<String> backups = new ArrayList<>();
        try {
            File backupDir = new File("data/backup");
            if (!backupDir.exists() || !backupDir.isDirectory()) {
                return backups;
            }

            File[] backupFiles = backupDir.listFiles((dir, name)
                    -> name.startsWith("usuarios_backup_") && name.endsWith(".txt")
            );

            if (backupFiles != null) {
                // Ordenar por fecha (más reciente primero)
                Arrays.sort(backupFiles, (f1, f2) -> {
                    try {
                        return Long.compare(f2.lastModified(), f1.lastModified());
                    } catch (Exception e) {
                        return 0;
                    }
                });

                for (File backup : backupFiles) {
                    backups.add(backup.getName() + " - "
                            + new java.util.Date(backup.lastModified()).toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar backups: " + e.getMessage());
        }

        return backups;
    }

    public boolean eliminarBackup(String nombreArchivo) {
        try {
            File backupFile = new File("data/backup/" + nombreArchivo);
            if (backupFile.exists()) {
                return backupFile.delete();
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al eliminar backup: " + e.getMessage());
            return false;
        }
    }

    // ========== MÉTODOS PRIVADOS ==========
    private UsuarioModel buscarUsuario(String username) {
        if (!conectado) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("#") || linea.trim().isEmpty()) {
                    continue;
                }

                UsuarioModel usuario = fromCSV(linea);
                if (usuario != null && usuario.getUsername().equals(username)) {
                    return usuario;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }

    private boolean emailExiste(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("#") || linea.trim().isEmpty()) {
                    continue;
                }

                UsuarioModel usuario = fromCSV(linea);
                if (usuario != null && usuario.getEmail().equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al buscar email: " + e.getMessage());
        }

        return false;
    }

    private void crearUsuarioAdministrador() {
        try {
            String adminHash = EncriptadorContrasena.encriptarContrasena("Admin123!");
            UsuarioModel admin = new UsuarioModel("admin", adminHash, "admin@sistema.com",
                    "Administrador del Sistema", "ADMIN");

            try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_ARCHIVO, true))) {
                writer.println(toCSV(admin));
                System.out.println("Usuario administrador creado: admin / Admin123!");
            }
        } catch (IOException e) {
            System.err.println("Error al crear admin: " + e.getMessage());
        }
    }

    public boolean actualizarUsuario(UsuarioModel usuarioActualizado) {
        List<String> lineas = new ArrayList<>();
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("#") || linea.trim().isEmpty()) {
                    lineas.add(linea);
                    continue;
                }

                UsuarioModel usuario = fromCSV(linea);
                if (usuario != null && usuario.getUsername().equals(usuarioActualizado.getUsername())) {
                    lineas.add(toCSV(usuarioActualizado));
                    encontrado = true;
                } else {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }

        if (!encontrado) {
            return false;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (String linea : lineas) {
                writer.println(linea);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar cambios: " + e.getMessage());
            return false;
        }
    }

    private String toCSV(UsuarioModel usuario) {
        return String.join(",",
                usuario.getUsername(),
                usuario.getPasswordHash(),
                usuario.getEmail(),
                usuario.getNombreCompleto(),
                usuario.getFechaRegistro().format(formatter),
                usuario.getUltimoAcceso().format(formatter),
                String.valueOf(usuario.isActivo()),
                usuario.getRol(),
                usuario.getPreguntaSecreta(),
                usuario.getRespuestaSecretaHash(),
                usuario.getCodigoRecuperacion(),
                usuario.getExpiracionCodigo().format(formatter),
                String.valueOf(usuario.getIntentosFallidos()),
                usuario.getUltimoIntentoFallido() != null ? usuario.getUltimoIntentoFallido().format(formatter) : "",
                String.valueOf(usuario.isBloqueado()),
                usuario.getFechaDesbloqueo() != null ? usuario.getFechaDesbloqueo().format(formatter) : ""
        );
    }

    private UsuarioModel fromCSV(String linea) {
        String[] datos = linea.split(",", -1); // -1 para conservar campos vacíos al final

        if (datos.length >= 7) {
            String username = datos[0];
            String passwordHash = datos[1];
            String email = datos[2];
            String nombreCompleto = datos[3];
            String rol = datos.length > 7 ? datos[7] : "USUARIO";

            UsuarioModel usuario = new UsuarioModel(username, passwordHash, email, nombreCompleto, rol);

            try {
                usuario.setFechaRegistro(LocalDateTime.parse(datos[4], formatter));
                usuario.setUltimoAcceso(LocalDateTime.parse(datos[5], formatter));
                usuario.setActivo(Boolean.parseBoolean(datos[6]));

                if (datos.length > 8) {
                    usuario.setPreguntaSecreta(datos[8]);
                }
                if (datos.length > 9) {
                    usuario.setRespuestaSecretaHash(datos[9]);
                }
                if (datos.length > 10) {
                    usuario.setCodigoRecuperacion(datos[10]);
                }
                if (datos.length > 11) {
                    usuario.setExpiracionCodigo(LocalDateTime.parse(datos[11], formatter));
                }
                if (datos.length > 12) {
                    usuario.setIntentosFallidos(Integer.parseInt(datos[12]));
                }
                if (datos.length > 13 && !datos[13].isEmpty()) {
                    usuario.setUltimoIntentoFallido(LocalDateTime.parse(datos[13], formatter));
                }
                if (datos.length > 14) {
                    usuario.setBloqueado(Boolean.parseBoolean(datos[14]));
                }
                if (datos.length > 15 && !datos[15].isEmpty()) {
                    usuario.setFechaDesbloqueo(LocalDateTime.parse(datos[15], formatter));
                }

            } catch (Exception e) {
                System.err.println("Error al parsear usuario: " + e.getMessage());
            }

            return usuario;
        }

        return null;
    }
    private static final String RUTA_REGISTRO_ACCESOS = "data/accesos_diarios.txt";

    private void registrarAcceso(String username) {
        try {
            LocalDateTime ahora = LocalDateTime.now();
            String fecha = ahora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Crear directorio si no existe
            Files.createDirectories(Paths.get("data"));

            File archivo = new File(RUTA_REGISTRO_ACCESOS);
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            // Leer líneas existentes
            List<String> lineas = Files.readAllLines(archivo.toPath());
            boolean encontrado = false;

            // Actualizar conteo para la fecha actual
            for (int i = 0; i < lineas.size(); i++) {
                String[] partes = lineas.get(i).split(",");
                if (partes.length >= 2 && partes[0].equals(fecha)) {
                    int contador = Integer.parseInt(partes[1]);
                    contador++;
                    lineas.set(i, fecha + "," + contador);
                    encontrado = true;
                    break;
                }
            }

            // Si no existe registro para esta fecha, crear uno nuevo
            if (!encontrado) {
                lineas.add(fecha + ",1");
            }

            // Escribir de vuelta al archivo
            Files.write(archivo.toPath(), lineas);

        } catch (IOException e) {
            System.err.println("Error al registrar acceso: " + e.getMessage());
        }
    }

// Método para obtener datos de accesos por fecha
    public Map<String, Integer> obtenerAccesosPorFecha(int ultimosDias) {
        Map<String, Integer> accesos = new LinkedHashMap<>();

        try {
            File archivo = new File(RUTA_REGISTRO_ACCESOS);
            if (!archivo.exists()) {
                return accesos;
            }

            List<String> lineas = Files.readAllLines(archivo.toPath());
            LocalDate fechaActual = LocalDate.now();

            // Inicializar últimos N días con 0
            for (int i = ultimosDias - 1; i >= 0; i--) {
                LocalDate fecha = fechaActual.minusDays(i);
                String fechaStr = fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                accesos.put(fechaStr, 0);
            }

            // Llenar con datos reales
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linea.split(",");
                if (partes.length >= 2) {
                    String fecha = partes[0];
                    int cantidad = Integer.parseInt(partes[1]);

                    if (accesos.containsKey(fecha)) {
                        accesos.put(fecha, cantidad);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer accesos: " + e.getMessage());
        }

        return accesos;
    }

}
