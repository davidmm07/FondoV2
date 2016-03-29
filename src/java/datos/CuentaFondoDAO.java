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
import negocio.Credito;
import negocio.CuentaFondo;
import negocio.Movimiento;
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
    
    // Se actualiza el valor de los aportes con la suma de todos los movimientos tipo aporte
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
    
    
    // Se suma el aporte de un socio a la cuenta del fondo
    public void sumarAporte(Movimiento movimiento) throws RHException{
        try{
            String strSQL = "UPDATE CUENTA_FONDO SET V_APORTES = V_APORTES + "
                    + "(SELECT V_MOV FROM MOVIMIENTO WHERE N_TIPO = 'APORTE' AND F_REGISTRO = TO_DATE(SYSDATE) "
                    + "AND CUENTA_K_IDCUENTA=?)";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, movimiento.getCuenta_k_idCuenta());
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("CuentaFondoDAO", "No se agregó el aporte del socio a la cuenta del fondo "+e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    //Se suma el valor de la cuota de un crédito a la cuenta del fondo
    public void sumarCuotaCredito() throws RHException{
        try{
            String strSQL = "UPDATE CUENTA_FONDO SET V_CREDITOS = V_CREDITOS - (SELECT SUM(V_MOV) FROM MOVIMIENTO "
                    + "WHERE N_TIPO='CUOTA DE CREDITO' AND F_REGISTRO=TO_DATE(SYSDATE))";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("CuentaFondoDAO", "No se sumo la cuota a la cuenta del fondo "+e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    public void otorgarCredito(Credito credito) throws RHException{
        try{
            String strSQL = "UPDATE CUENTA_FONDO SET V_CREDITOS = V_CREDITOS + (SELECT V_PRESTADO FROM CREDITO "
                    + "WHERE N_E_CREDITO_CK='APROBADO' AND SOCIO_K_IDSOCIO=?)";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, credito.getSocio_k_id_socio());
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("CuentaFondoDAO", " No se otorgo el crédito "+e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    
}
