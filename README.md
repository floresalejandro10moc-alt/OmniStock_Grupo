# 📱 OmniStock - Sistema de Gestión Comercial Móvil

Proyecto final para la materia de Programación Móvil. Aplicación Android nativa (Java) para la gestión de inventario, control de usuarios y facturación, implementando arquitectura MVVM y persistencia avanzada con Room.

---

## 👥 Equipo de Desarrollo y Roles

| Integrante | Rol / Módulo Asignado | Rama Git (Feature Branch) |
| :--- | :--- | :--- |
| ** ** | **Líder & Arquitectura**<br>Gestión de BD (Room), Menú Principal y Módulo de Clientes. | `feature/clientes_dashboard` |
| ** ** | **Seguridad & Acceso**<br>Login, Validación de Roles (Admin/Vendedor) y Sesión. | `feature/seguridad_login` |
| ** ** | **Inventario & UI**<br>Listado (RecyclerView), Diseño Dinámico (CardView) y Alta de Productos. | `feature/inventario_crud` |
| ** ** | **Ventas & Lógica POO**<br>Carrito de Compras, Cálculo de Impuestos (Polimorfismo) y Transacciones. | `feature/ventas_polimorfismo` |

---

## 🚀 Requerimientos Técnicos Implementados
Este proyecto cumple estrictamente con la rúbrica de evaluación:

1. **Arquitectura:** MVVM (Model-View-ViewModel).
2. **Base de Datos:** Room con relaciones 1:N (`@ForeignKey`) y validaciones `CHECK`.
3. **POO Pura:** Implementación de Herencia y Polimorfismo para reglas de negocio:
   - *Electrónicos:* Precio + IVA + Impuesto Suntuario.
   - *Ropa:* Precio + IVA (Descuento si es temporada anterior).
   - *Alimentos:* Sin IVA, validación de caducidad.
4. **Interfaz:** Uso de `RecyclerView`, `CardView` dinámicos y `Dialogs`.

---

## 🛠️ Instrucciones de Instalación
1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/TU_USUARIO/OmniStock_Grupo.git](https://github.com/TU_USUARIO/OmniStock_Grupo.git)

   Aquí tienes el archivo README.md completo y definitivo. He integrado la descripción del proyecto, la división de tareas para los 4 integrantes y las reglas sagradas de Git.

Copia y pega esto en el archivo README.md de tu repositorio.

Markdown

# 📱 OmniStock - Sistema de Gestión Comercial Móvil

Proyecto final para la materia de Programación Móvil. Aplicación Android nativa (Java) para la gestión de inventario, control de usuarios y facturación, implementando arquitectura MVVM y persistencia avanzada con Room.

---

## 👥 Equipo de Desarrollo y Roles

| Integrante | Rol / Módulo Asignado | Rama Git (Feature Branch) |
| :--- | :--- | :--- |
| **[TU NOMBRE]** | **Líder & Arquitectura**<br>Gestión de BD (Room), Menú Principal y Módulo de Clientes. | `feature/clientes_dashboard` |
| **[NOMBRE 2]** | **Seguridad & Acceso**<br>Login, Validación de Roles (Admin/Vendedor) y Sesión. | `feature/seguridad_login` |
| **[NOMBRE 3]** | **Inventario & UI**<br>Listado (RecyclerView), Diseño Dinámico (CardView) y Alta de Productos. | `feature/inventario_crud` |
| **[NOMBRE 4]** | **Ventas & Lógica POO**<br>Carrito de Compras, Cálculo de Impuestos (Polimorfismo) y Transacciones. | `feature/ventas_polimorfismo` |

---

## 🚀 Requerimientos Técnicos Implementados
Este proyecto cumple estrictamente con la rúbrica de evaluación:

1. **Arquitectura:** MVVM (Model-View-ViewModel).
2. **Base de Datos:** Room con relaciones 1:N (`@ForeignKey`) y validaciones `CHECK`.
3. **POO Pura:** Implementación de Herencia y Polimorfismo para reglas de negocio:
   - *Electrónicos:* Precio + IVA + Impuesto Suntuario.
   - *Ropa:* Precio + IVA (Descuento si es temporada anterior).
   - *Alimentos:* Sin IVA, validación de caducidad.
4. **Interfaz:** Uso de `RecyclerView`, `CardView` dinámicos y `Dialogs`.

---

## 🛠️ Instrucciones de Instalación

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/TU_USUARIO/OmniStock_Grupo.git](https://github.com/TU_USUARIO/OmniStock_Grupo.git)
Abrir en Android Studio: Selecciona la carpeta raíz del proyecto.

Sincronizar Gradle: Al abrir, Android Studio descargará las librerías de Room y Material Design automáticamente.

Ejecutar: Usar un emulador con API 33/34 (Android Tiramisu/UpsideDownCake). La base de datos se poblará automáticamente con datos de prueba de Ecuador (IVA 15%) en la primera ejecución.

⚠️ REGLAS DE ORO (Workflow de Git)
🚫 PROHIBIDO: Hacer commit o push directamente a la rama main.

1. Al iniciar el día (Actualizar)
Antes de escribir una sola línea de código, descarga lo que hicieron tus compañeros:

Bash

git checkout main
git pull origin main
2. Para trabajar (Crear Rama)
Crea tu rama según tu rol asignado (ver tabla arriba):

Bash

git checkout -b feature/nombre_de_tu_modulo
3. Guardar cambios
Bash

git add .
git commit -m "Descripción clara de lo que se programó"
4. Subir cambios a la nube
Bash

git push origin feature/nombre_de_tu_modulo
5. Integrar código
Ir a GitHub.com.

Crear un Pull Request (De tu rama hacia main).

Avisar al Líder para que revise y apruebe el Merge.

📂 Estructura del Proyecto
El código sigue el estándar de paquetes de Java:

com.example.inventarioapp.model:

Entidades de Room (Usuario, Producto, etc.).

Clases POO (ProductoBase, ProductoElectronico).

com.example.inventarioapp.data:

AppDatabase (Conexión Singleton).

DAOs (Consultas SQL).

com.example.inventarioapp.ui:

Activities (Pantallas).

Adapters (Para los RecyclerView).

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/TU_USUARIO/OmniStock_Grupo.git](https://github.com/TU_USUARIO/OmniStock_Grupo.git)
