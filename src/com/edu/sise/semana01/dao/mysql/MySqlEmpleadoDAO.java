/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edu.sise.semana01.dao.mysql;

import com.edu.sise.semana01.dao.DAOException;
import com.edu.sise.semana01.dao.IEmpleadoDAO;
import com.edu.sise.semana01.entity.Empleado;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Carlos
 */
public class MySqlEmpleadoDAO implements IEmpleadoDAO{

    //SQL Precompilados
    final String INSERT = "INSERT INTO empleados(nombre, ape_pat, ape_mat, fnacimiento, sueldo) "
            + " VALUES(?, ?, ? ,?, ?)";
    final String GETALL = "SELECT * FROM empleados";
    //modificar
    final String UPDATE = "UPDATE empleados SET nombre = ?, ape_pat= ?, ape_mat = ?, "
            + " fnacimiento = ?, sueldo = ? "
            + " WHERE id_empleado = ?";
    //eliminar
    final String DELETE = "DELETE FROM empleados WHERE id_empleado = ?";
    
    //busqueda por nombre, ape_pat y ape_mat
    final String BUSQUEDA = "SELECT * FROM empleados " +
                    " WHERE nombre like ? || ape_pat like ? || ape_mat like ?";
    
    final String COLUMNAS = "SELECT * FROM empleados LIMIT 0";
    
    final String PAGINACION = "SELECT * FROM empleados LIMIT ?,?";
    
    final String COUNT = "SELECT count(*) as cantidad FROM empleados";

    //Conexión
    private Connection cn;

    public MySqlEmpleadoDAO(Connection cn) {
        this.cn = cn;
    }
    
    @Override
    public double calcularBonificacion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertar(Empleado o) throws DAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try { 
            ps = cn.prepareStatement(INSERT,PreparedStatement.RETURN_GENERATED_KEYS);
            int i=1;
            ps.setString(i++,o.getNombre());
            ps.setString(i++, o.getApe_pat());
            ps.setString(i++, o.getApe_mat());
            ps.setDate(i++, Date.valueOf(o.getFnacimiento()));
            ps.setDouble(i++, o.getSueldo());
            if(ps.executeUpdate()==0)
                throw new DAOException("No se pudo registrar el empleado!!!");
            rs = ps.getGeneratedKeys();
            if(rs.next()){
                o.setId_empleado(rs.getInt(1));
            }else{
                throw new DAOException("No se pudo generar el ID del empleado!!!");
            }
        } catch (SQLException ex) {
            throw new DAOException("Error de SQL", ex);
        }finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException ex){
                throw new DAOException("Error de SQL", ex);
            }
        }
    }

    @Override
    public void modificar(Empleado o) throws DAOException {
        PreparedStatement ps = null;
        try {
            ps = cn.prepareStatement(UPDATE);
            int i=1;
            ps.setString(i++, o.getNombre());
            ps.setString(i++, o.getApe_pat());
            ps.setString(i++, o.getApe_mat());
            ps.setDate(i++, Date.valueOf(o.getFnacimiento()));
            ps.setDouble(i++, o.getSueldo());
            ps.setInt(i++, o.getId_empleado());
            
            if(ps.executeUpdate() == 0)
                throw new DAOException("No se pudo modificar el empleado!!!");
        }catch(SQLException ex){
            throw new DAOException("Error de SQL", ex);
        }finally{
            try{
                if(ps!=null) ps.close();
            }catch(SQLException ex){
                throw new DAOException("Error de SQL", ex);
            }
        }
    }

    @Override
    public void eliminar(Empleado o) throws DAOException {
        PreparedStatement ps = null;
        try {
            ps = cn.prepareStatement(DELETE);
            int i = 1;
            ps.setInt(i++, o.getId_empleado());
            if(ps.executeUpdate()==0)
                throw new DAOException("No se pudo eliminar el empleado!!!");
        } catch (SQLException ex) {
            throw new DAOException("Error de SQL", ex);
        }finally{
            try{
                if(ps!=null) ps.close();
            }catch(SQLException ex){
                throw new DAOException("Error de SQL", ex);
            }
        }
    }

    @Override
    public List<Empleado> obtenerTodos() throws DAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Empleado> lista = new ArrayList<>();
        try {
            ps = cn.prepareStatement(GETALL);
            rs = ps.executeQuery();
            while(rs.next()){
                lista.add(getRS(rs));
            }
        } catch (SQLException ex) {
            throw new DAOException("Error de SQL", ex);
        }finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException ex){
                throw new DAOException("Error de SQL", ex);
            }
        }
        return lista;
    }

    @Override
    public Empleado obtenerxID(Integer id) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Empleado getRS(ResultSet rs) throws SQLException{
        return new Empleado(
                rs.getInt("id_empleado"),
                rs.getString("nombre"),
                rs.getString("ape_pat"),
                rs.getString("ape_mat"),
                rs.getDate("fnacimiento").toLocalDate(),
                rs.getDouble("sueldo")
            );
    }

    @Override
    public List<Empleado> busqueda(String valor) throws DAOException {
        PreparedStatement ps =  null;
        ResultSet rs = null;
        List<Empleado> lista  = new ArrayList<>();
        try{
            ps = cn.prepareStatement(BUSQUEDA);
            int i=1;
            ps.setString(i++, "%"+valor+"%");
            ps.setString(i++, "%"+valor+"%");
            ps.setString(i++, "%"+valor+"%");
            
            rs = ps.executeQuery();
            while(rs.next()){
                lista.add(getRS(rs));
            }
            
        } catch (SQLException ex) {
            throw new DAOException("Error de SQL", ex);
        }finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException ex){
                throw new DAOException("Error de SQL", ex);
            }
        }
        
        return lista;
    }

    @Override
    public int[] cargaMasiva(List<Empleado> lista) throws DAOException {
        PreparedStatement ps = null;
        int[] res;
        try{
            ps = cn.prepareStatement(INSERT);
            for(Empleado o: lista){
                int i=1;
                ps.setString(i++, o.getNombre());
                ps.setString(i++, o.getApe_pat());
                ps.setString(i++, o.getApe_mat());
                ps.setDate(i++, Date.valueOf(o.getFnacimiento()));
                ps.setDouble(i++, o.getSueldo());
                ps.addBatch();
            }
            
            res = ps.executeBatch();
            if(res.length==0)
                throw new DAOException("No se pudo realizar la carga masiva!!!");
            
        } catch (SQLException ex) {
            throw new DAOException("Error de SQL", ex);
        }finally{
            try{
                if(ps!=null) ps.close();
            }catch(SQLException ex){
                throw new DAOException("Error de SQL", ex);
            }
        }
        return res;
    }
    
    @Override
    public List<String> obtenerNombreColumnas() throws DAOException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        List<String> lista = new ArrayList<>();
        try {
            ps = cn.prepareStatement(COLUMNAS);
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            for(int i=1;i<=rsmd.getColumnCount();i++)
                lista.add(rsmd.getColumnName(i));
        } catch (SQLException ex) {
            throw new DAOException("Error de SQL", ex);
        }finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException ex){
                throw new DAOException("Error de SQL", ex);
            }
        }
        return lista;
    }

    @Override
    public List<Empleado> paginacion(Integer inicio, Integer fin) throws DAOException {
       PreparedStatement ps = null;
        ResultSet rs = null;
        List<Empleado> lista = new ArrayList<>();
        try {
            ps = cn.prepareStatement(PAGINACION);
            int i=1;
            ps.setInt(i++, inicio);
            ps.setInt(i++, fin);
            rs = ps.executeQuery();
            while(rs.next()){
                lista.add(getRS(rs));
            }
        } catch (SQLException ex) {
            throw new DAOException("Error de SQL", ex);
        }finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException ex){
                throw new DAOException("Error de SQL", ex);
            }
        }
        return lista;
    }

    @Override
    public Integer cantidadRegistros() throws DAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cantidad= 0;
        try {
            ps = cn.prepareStatement(COUNT);
            rs = ps.executeQuery();
            if(rs.next()){
                cantidad = rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Error de SQL", ex);
        }finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException ex){
                throw new DAOException("Error de SQL", ex);
            }
        }
        return cantidad;
    }
}
