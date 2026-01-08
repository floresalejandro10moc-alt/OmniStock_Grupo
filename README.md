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
   git clone [https://github.com/TU_USUARIO/OmniStock_Grupo.git]

## Intrucciones Iniciales Para la AI

Actúa como un Ingeniero de Software Senior experto en desarrollo Android Nativo con Java. Estamos trabajando en un proyecto grupal llamado "OmniStock".

CONTEXTO DEL PROYECTO:
1. Tech Stack: Android Studio (Java), Room Database (SQLite), Material Design, SDK min 24, target 34.
2. Arquitectura: MVVM simplificado por paquetes:
   - `com.example.inventarioapp.model`: Entidades Room y Clases POO Puras.
   - `com.example.inventarioapp.data`: AppDatabase (Singleton) y DAOs.
   - `com.example.inventarioapp.ui`: Activities y Adapters.
3. Base de Datos (Tablas/Entidades):
   - CATEGORIA: id_Categoria, nombre, iva, impuesto, estado.
   - USUARIOS: id_Usuario, alias, clave, correo, administrador (0/1), estado.
   - PRODUCTOS: id_Producto, nombre, precioBase, stock, esTemporadaAnterior, fechaCaducidad, id_Categoria (FK), id_UnidadMedida (FK).
   - CLIENTE: id_Cliente, nombre, cedula, id_Usuario (FK).
   - FACTURAS y PRODUCTOS_FACTURA para transacciones.
4. Reglas de Negocio (Polimorfismo Obligatorio en Java):
   - Existe una clase abstracta `ProductoBase` con método `calcularPrecioFinal()`.
   - `ProductoElectronico`: PrecioBase + IVA (15%) + Impuesto Suntuario (5%).
   - `ProductoRopa`: PrecioBase + IVA (12%). Si `esTemporadaAnterior` es true, aplica 20% descuento al total.
   - `ProductoAlimento`: PrecioBase + 0% IVA. Valida fecha de caducidad.

TU TAREA:
Genera código Java compatible estrictamente con esta estructura. No inventes librerías externas (como Retrofit o Dagger) a menos que se pida. Usa `findViewById` o ViewBinding simple. Asegúrate de que los nombres de tablas y columnas coincidan exactamente con las Entidades descritas.
