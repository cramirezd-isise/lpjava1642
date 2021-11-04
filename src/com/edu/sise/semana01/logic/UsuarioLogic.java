/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edu.sise.semana01.logic;

import com.edu.sise.semana01.dao.UsuarioDao;
import com.edu.sise.semana01.entity.Usuario;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos
 */
public class UsuarioLogic {
    
    UsuarioDao dao =  null;
    
    public Usuario obtenerUsuarioxId(int id){
        dao = new UsuarioDao();
        Usuario objUsuario =  null;
        if(id>0){
            objUsuario = dao.obtenerUsuarioxId(id);
        }        
        return objUsuario;
    }
    
     public Usuario validarAcceso(String nombre, String clave){
         dao = new UsuarioDao();
         return dao.validarAcceso(nombre, clave);
     }
     
     public DefaultTableModel listarTodos(){
         dao = new UsuarioDao();
         DefaultTableModel modelo = new DefaultTableModel();
         modelo.addColumn("ID");
         modelo.addColumn("NOMBRE");
         modelo.addColumn("CLAVE");
         
         List<Usuario> listaUsuarios = dao.listarTodos();
         Usuario objUsuario = null;
         for(int i=0; i< listaUsuarios.size();i++){
             objUsuario = listaUsuarios.get(i);
             Object data[] ={
                 objUsuario.getId_usuario(),
                 objUsuario.getNombre(),
                 objUsuario.getClave()
             };
             modelo.addRow(data);
         }
         
         return modelo;
     }
     
     public boolean agregarUsuario(Usuario usuario){
        dao = new UsuarioDao();
        return dao.agregarUsuario(usuario);
     }
     
     public void eliminarUsuario(int id){
         dao = new UsuarioDao();
         dao.eliminarUsuario(id);
     }
     
     public void modificarUsuario(Usuario usuario){
         dao = new UsuarioDao();
         dao.modificarUsuario(usuario);
     }
     
    public void imprimirTB(JTable jTable){
        jTable.setModel(listarTodos());
    }
    
    public void imprimirTB(JTable jTable, DefaultTableModel modelo){
        jTable.setModel(modelo);
    }
     
     
     public DefaultTableModel buscarxNombre(String nombre){
         dao = new UsuarioDao();
         DefaultTableModel modelo = new DefaultTableModel();
         modelo.addColumn("ID");
         modelo.addColumn("NOMBRE");
         modelo.addColumn("CLAVE");
         
         List<Usuario> listaUsuarios = dao.buscarxNombre(nombre);
         Usuario objUsuario = null;
         for(int i=0; i< listaUsuarios.size();i++){
             objUsuario = listaUsuarios.get(i);
             Object data[] ={
                 objUsuario.getId_usuario(),
                 objUsuario.getNombre(),
                 objUsuario.getClave()
             };
             modelo.addRow(data);
         }
         
         return modelo;
     }
}
