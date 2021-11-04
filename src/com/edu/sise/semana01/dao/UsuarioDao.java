/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edu.sise.semana01.dao;

import com.edu.sise.semana01.entity.Usuario;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos
 */
public class UsuarioDao {
    
    Connection cn =  null;
    
    public Usuario obtenerUsuarioxId(int id){
        cn = new Conexion().getCnxMySQL();
        String sql = "select * from usuarios where id_usuario = "+ id;
        Usuario objUsuario = null;
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                objUsuario = new Usuario();
                objUsuario.setId_usuario(rs.getInt("id_usuario"));
                objUsuario.setNombre(rs.getString("nombre"));
                objUsuario.setClave(rs.getString("clave"));
            }
            
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar sentencia SQL: " +  ex);
        }finally{
            if(cn!=null){
                try {
                    cn.close();
                } catch (SQLException ex) {
                    System.out.println("Error al cerrar la conexión: " + ex);
                }
            }
        }
        
        return objUsuario;
    }
    
    public Usuario validarAcceso(String nombre, String clave){
        cn = new Conexion().getCnxMySQL();
        String sql = "select * from usuarios where nombre = '"+ nombre +"' and clave ='" + clave+"' ";
        Usuario objUsuario =  null;
        
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                objUsuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("clave")
                );
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }finally{
            if(cn!=null){
                try {
                    cn.close();
                } catch (SQLException ex) {
                    System.out.println("Error al cerrar conexión: "+ ex);
                }
            }
        }
        return objUsuario;
    }
    
    public List listarTodos(){
        cn = new Conexion().getCnxMySQL();
        String sql = "select * from usuarios";
        List listaUsuarios = new ArrayList<Usuario>();
        Usuario objUsuario = null;
        
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                objUsuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("clave")
                );
                listaUsuarios.add(objUsuario);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }finally{
            if(cn!=null){
                try {
                    cn.close();
                } catch (SQLException ex) {
                    System.out.println("Error al cerrar conexión: "+ ex);
                }
            }
        }
        
        return listaUsuarios;
    }
    
    public boolean agregarUsuario(Usuario usuario){
        cn = new Conexion().getCnxMySQL();
        //conexión interna para SQL SERVER
        //Connection cnSQLSERVER = new Conexion().getCnxSQLSERVER();
        
        String sql = "insert into usuarios(nombre, clave) "
                + "values ('"+usuario.getNombre()+"','"+usuario.getClave() +"') ";
        //boolean rpta = false;
        int cant = 0;
        try {
            //MYSQL
            Statement st = cn.createStatement();
             cant= st.executeUpdate(sql);
             
             //SQLSERVER
             
             //Statement stSQLSERVER = cnSQLSERVER.createStatement();
             //stSQLSERVER.executeUpdate(sql);
//            if(cant>0) 
//                rpta= true;
//            else
//                rpta = false;
            //rpta = (cant>0)?true:false;
            //rpta = (cant>0);
            System.out.println("Cantidad de registros agregados: " + cant);
            
        } catch (SQLException ex) {
            System.out.println("Error Statement: " + ex);
        }finally{
            if(cn!=null){
                try {
                    cn.close();
                } catch (SQLException ex) {
                    System.out.println("Error conexión: " + ex);
                }
            }
        }
        return (cant>0);
    }
    
    public void eliminarUsuario(int id){
        cn = new Conexion().getCnxMySQL();
        String sql = "delete from usuarios where id_usuario="+id;
        
        try {
            Statement st = cn.createStatement();
            int cant = st.executeUpdate(sql);
            System.out.println("Cantidad de registros eliminados: " + cant);
        } catch (SQLException ex) {
            System.out.println("Error Statement: " + ex);
        }finally{
            if(cn!=null){
                try {
                    cn.close();
                } catch (SQLException ex) {
                    System.out.println("Error conexión: " + ex);
                }
            }
        } 
    }
    
    public void modificarUsuario(Usuario usuario){
        cn = new Conexion().getCnxMySQL();
        String sql = "update usuarios "
                + "set nombre = '"+usuario.getNombre()+"', "
                + "    clave = '"+usuario.getClave()+"' "
                + "    where id_usuario = "+usuario.getId_usuario();
        
        try {
            Statement st = cn.createStatement();
            int cant = st.executeUpdate(sql);
            System.out.println("Cantidad de registros modificados: " + cant);
        } catch (SQLException ex) {
            System.out.println("Error Statement: " + ex);
        }finally{
            if(cn!=null){
                try {
                    cn.close();
                } catch (SQLException ex) {
                    System.out.println("Error conexión: " + ex);
                }
            }
        }
        
    }
    
    public List buscarxNombre(String nombre){
        cn = new Conexion().getCnxMySQL();
        String sql = "select * from usuarios where nombre like '"+nombre+"%'";
        Statement st = null;
        ResultSet rs = null;
        List listaUsuarios = new ArrayList<Usuario>();
        Usuario objUsuario = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                objUsuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("clave")
                );
                listaUsuarios.add(objUsuario);
            }
            
        } catch (SQLException ex) {
           System.out.println("Error: " + ex);
        }finally{
            try {
                if(rs !=null) rs.close();
                if(st!=null)  st.close();
                if(cn!=null)  cn.close();
            } catch (SQLException ex) {
                    System.out.println("Error al cerrar conexión: "+ ex);
            }
        }
        return listaUsuarios;
    }
}
