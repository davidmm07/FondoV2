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
import negocio.Fondo;
import util.RHException;
import util.ServiceLocator;

/**
 *
 * @author Cristian
 */
public class FondoDAO {
    
    public FondoDAO(){
        
    }
    
    // Se consulta el fondo con sus condiciones
    public void consultarFondo(){
        try{
            Fondo f = new Fondo();
            String strSQL = "SELECT K_IDFONDO,V_CAPITAL_TOT,V_CAPITAL_DISP FROM FONDO";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            ResultSet rs = prepStmt.executeQuery();
            while(rs.next()){
                rs.getInt(1);
                rs.getDouble(2);
                rs.getDouble(3);
            }
        }catch(SQLException e){
            
        }
    }
    
    // Se calcula el valor del capital total del fondo
    public void modificarCapitalTotFondo() throws RHException{
        try{
            String strSQL = "UPDATE FONDO SET V_CAPITAL_TOT = (SELECT V_APORTES+V_INTERESXCREDITO+V_RENDFINAN-V_GFINANCIERO "
                    + "FROM CUENTA_FONDO)";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("FondoDAO", "No se modificó el capital total "+e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    // Se calcula el valor del capital disponible del fondo
    public void modificarCapitalDispFondo() throws RHException{
        try{
            String strSQL = "UPDATE FONDO SET V_CAPITAL_DISP = (SELECT V_APORTES-V_CREDITOS FROM CUENTA_FONDO)";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("FondoDAO","No se modificó el capital disponible "+e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    
}
