/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edu.sise.semana01.dao.mysql;

import com.edu.sise.semana01.dao.Conexion;
import com.edu.sise.semana01.dao.IDAOManager;
import com.edu.sise.semana01.dao.IEmpleadoDAO;
import java.sql.Connection;

/**
 *
 * @author Carlos
 */
public class MySqlDAOManager implements IDAOManager{

    private static final MySqlDAOManager instancia = new MySqlDAOManager();
    private Connection cn;
    private IEmpleadoDAO empleadoDao = null;
    
    private MySqlDAOManager(){
        cn = new Conexion().getCnxMySQL();
    }
    
    public static MySqlDAOManager getInstancia(){
        return instancia;
    }
    
    @Override
    public IEmpleadoDAO getEmpleadoDAO() {
        if(empleadoDao == null){
            empleadoDao = new MySqlEmpleadoDAO(cn);
        }
        return empleadoDao;
    }
    
}
