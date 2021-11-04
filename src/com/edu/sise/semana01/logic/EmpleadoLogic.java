/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edu.sise.semana01.logic;

import com.edu.sise.semana01.dao.DAOException;
import com.edu.sise.semana01.dao.IEmpleadoDAO;
import com.edu.sise.semana01.dao.mysql.MySqlDAOManager;
import com.edu.sise.semana01.entity.Empleado;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Carlos
 */
public class EmpleadoLogic {
  
    MySqlDAOManager factory = MySqlDAOManager.getInstancia();
    IEmpleadoDAO dao = factory.getEmpleadoDAO();
    
    public DefaultTableModel obtenerTodos() throws Exception{
        DefaultTableModel modelo = getModelo();
        List<Empleado> lista = dao.obtenerTodos();
        llenarModelo(lista, modelo);
        return modelo;
    }
    
    public void imprimirTB(JTable jTable) throws Exception{
        jTable.setModel(obtenerTodos());
    }
    
    public void imprimirTB(JTable jTable, DefaultTableModel modelo) throws Exception{
        jTable.setModel(modelo);
    }
    
    public void insertar(Empleado o)throws Exception{
        dao.insertar(o);
    }
    
    public void eliminar(Empleado o) throws Exception{
        dao.eliminar(o);
    }
    
    public void modificar(Empleado o) throws Exception{
        dao.modificar(o);
    }
    
    
    public DefaultTableModel busqueda(String valor) throws Exception{
        DefaultTableModel modelo = getModelo();
        List<Empleado> lista = dao.busqueda(valor);
        llenarModelo(lista, modelo);
        return modelo;
    }
    
    private DefaultTableModel getModelo()throws Exception{
        DefaultTableModel modelo = new DefaultTableModel();
        List<String> listaColumna = dao.obtenerNombreColumnas();
        for(String columna : listaColumna)
            modelo.addColumn(columna.replace('_', ' ').toUpperCase());  
        return modelo;
    }
    
    private void llenarModelo(List<Empleado> lista, DefaultTableModel modelo){
        for(Empleado obj : lista){
            Object data[] = {
                obj.getId_empleado(),
                obj.getNombre(),
                obj.getApe_pat(),
                obj.getApe_mat(),
                obj.getFnacimiento(),
                obj.getSueldo()
            };
            modelo.addRow(data);
        }
    }
    
    public int cargaMasiva(String rutaArchivo) throws Exception{
        FileInputStream archivo = new FileInputStream(rutaArchivo);
        DataInputStream entrada = new DataInputStream(archivo);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
        List<Empleado> lista = new ArrayList<>();
        String linea;
        while((linea=buffer.readLine())!=null){
            String[] campos = linea.split("\\|");
            lista.add(new Empleado(
                    0,
                    campos[0],
                    campos[1],
                    campos[2],
                    LocalDate.parse(campos[3]),
                    Double.parseDouble(campos[4])
            ));
        }
        
        entrada.close();
        
        //trabajar con el dao
        int[] resultados = dao.cargaMasiva(lista);
        
        int registros_afectados = 0;
        for(int i=0;i<resultados.length;i++){
            registros_afectados = registros_afectados + resultados[i];
        }
        return registros_afectados;
    }
    
    public DefaultTableModel paginacion(Integer inicio, Integer fin) throws Exception{
        DefaultTableModel modelo = getModelo();
        List<Empleado> lista = dao.paginacion(inicio, fin);
        llenarModelo(lista, modelo);
        return modelo;
    }
    
    public Integer cantidadRegistros() throws Exception{
        return dao.cantidadRegistros();
    }

}
