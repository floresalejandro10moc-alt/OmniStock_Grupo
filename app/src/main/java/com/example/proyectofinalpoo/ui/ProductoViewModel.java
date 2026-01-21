package com.example.proyectofinalpoo.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.proyectofinalpoo.data.AppDatabase;
import com.example.proyectofinalpoo.data.ProductoDao;
import com.example.proyectofinalpoo.model.ProductoConCategoria;
import java.util.List;

public class ProductoViewModel extends AndroidViewModel {
    private final ProductoDao productoDao;
    private final LiveData<List<ProductoConCategoria>> allProductos;

    public ProductoViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        productoDao = db.productoDao();
        allProductos = productoDao.obtenerTodosLosProductosConCategoria();
    }

    public LiveData<List<ProductoConCategoria>> getAllProductos() {
        return allProductos;
    }
}