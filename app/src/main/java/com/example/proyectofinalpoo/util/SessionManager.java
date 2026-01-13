package com.example.proyectofinalpoo.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "OmniStockSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_ALIAS = "userAlias";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void crearSesionLogin(int id, int rol, String alias) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, id);
        editor.putInt(KEY_USER_ROLE, rol);
        editor.putString(KEY_USER_ALIAS, alias);
        editor.apply(); // Guardar cambios
    }

    public boolean estaLogueado() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getRolUsuario() {
        return pref.getInt(KEY_USER_ROLE, -1);
    }

    public String getAliasUsuario() {
        return pref.getString(KEY_USER_ALIAS, "Usuario");
    }

    public void cerrarSesion() {
        editor.clear();
        editor.apply();
    }
}
