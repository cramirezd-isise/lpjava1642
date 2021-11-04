/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edu.sise.semana01.dao;

import com.edu.sise.semana01.entity.Empleado;
import java.util.List;

/**
 *
 * @author Carlos
 */
public interface IEmpleadoDAO extends DAO<Empleado,Integer>{
    double calcularBonificacion();
    List<Empleado> busqueda(String valor) throws DAOException;
    int[] cargaMasiva(List<Empleado> lista) throws DAOException;
    List<String> obtenerNombreColumnas() throws DAOException;
}
