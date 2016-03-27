/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import negocio.CuentaFondo;
import negocio.Socio;
import util.RHException;
import util.ServiceLocator;

/**
 *
 * @author Cristian
 */
public class CuentaFondoDAO {
    
    public CuentaFondoDAO(){
        
    }
    
    public void consultarCuentaFondo(){
        try{
            String strSQL = "SELECT K_CTA_FONDO,V_APORTES,V_INTERESXCREDITO,V_RENDFINAN,V_GFINANCIERO,V_CREDITOS "
                    + "FROM CUENTA_FONDO";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            ResultSet rs = prepStmt.executeQuery();
            while(rs.next()){
                rs.getInt(1);
                rs.getDouble(2);
                rs.getDouble(3);
                rs.getDouble(4);
                rs.getDouble(5);
                rs.getDouble(6);
            }
        }catch(SQLException e){
            
        }
    }
    
    public void calcularValorAportes() throws RHException{
        try{
            String strSQL = "UPDATE CUENTA_FONDO SET V_APORTES = (SELECT SUM(V_MOV) FROM MOVIMIENTO "
                    + "WHERE N_TIPO = 'APORTE')";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("CuentaFondoDAO","No se calculó el valor de los aportes"+e.getMessage());     
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    
}
