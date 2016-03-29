/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import negocio.Credito;
import negocio.Cuenta;
import negocio.Movimiento;
import util.RHException;
import util.ServiceLocator;

/**
 *
 * @author Cristian
 */
public class CuentaDAO {

    public CuentaDAO() {

    }

    
    // Se le agrega una única cuenta a un socio
    public void agregarCuenta(Cuenta cuenta) throws RHException {
        try {
            String strSQL = "INSERT INTO CUENTA(K_IDCUENTA,V_SALDO,SOCIO_K_IDSOCIO) "
                    + "VALUES (CUENTA_SEQ.NEXTVAL,40000,?)";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, cuenta.getSocio_k_idSocio());
            prepStmt.executeUpdate();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        } catch (SQLException e) {
            throw new RHException("CuentaDAO", "No se agregó la cuenta" + e.getMessage());
        } finally {
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    // Se consulta la cuenta teniendo en cuenta la identificación el socio
    public void buscarCuenta(int socio_k_idSocio){
        try{
            Cuenta c = new Cuenta();
            String strSQL = "SELECT K_IDCUENTA,V_SALDO,SOCIO_K_IDSOCIO FROM CUENTA WHERE SOCIO_K_IDSOCIO = ?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, socio_k_idSocio);
            ResultSet rs = prepStmt.executeQuery();
            while(rs.next()){
                rs.getInt(1);
                rs.getDouble(2);
                rs.getInt(3);
            }
        }catch(SQLException e){
            
        }
    }
    // Agregar/restar al saldo de la cuenta
    public void modificarSaldo(Cuenta cuenta) throws RHException{
        try{
            String strSQL = "UPDATE CUENTA SET V_SALDO = V_SALDO+? WHERE K_IDCUENTA=?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setDouble(1, cuenta.getV_saldo());
            prepStmt.setInt(2, cuenta.getK_idCuenta());
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("CuentaDAO", "No se pudo modificar el saldo" + e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    // Al desembolsar un crédito, se le suma el valor prestado a la cuenta de un socio
    public void sumarCreditoASaldo(Cuenta cuenta, Credito credito) throws RHException{
        try{
            String strSQL = "UPDATE CUENTA SET V_SALDO = V_SALDO + (SELECT V_PRESTADO FROM CREDITO WHERE CUENTA_K_IDCUENTA = ?) "
                    + "WHERE K_IDCUENTA = ?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, credito.getCuenta_k_idCuenta());
            prepStmt.setInt(2, cuenta.getK_idCuenta());
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("CuentaDAO", "No se sumo el créito al saldo de la cuenta "+e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    //Restar aporte realizado por cheque al saldo en la cuenta del socio
    public void restarSaldoPorAporte(Cuenta cuenta, Movimiento movimiento) throws RHException{
        try{
            String strSQL = "UPDATE CUENTA SET V_SALDO=V_SALDO-(SELECT V_MOV FROM MOVIMIENTO "
                    + "WHERE N_TIPO = 'APORTE' AND  CUENTA_K_IDCUENTA = ?) "
                    + "WHERE K_IDCUENTA = ?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, movimiento.getCuenta_k_idCuenta());
            prepStmt.setInt(2, cuenta.getK_idCuenta());
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("CuentaDAO", "No se resto el valor del aporte al saldo "+e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    // Restar saldo de la cuenta de un socio por concepto de pago de una cuota de crédito
    public void restarSaldoPorCuota(Cuenta cuenta, Movimiento movimiento) throws RHException{
        try{
            String strSQL = "UPDATE CUENTA SET V_SALDO = V_SALDO-(SELECT V_MOV FROM MOVIMIENTO "
                    + "WHERE N_TIPO = 'CUOTA DE CREDITO' AND CUENTA_K_IDCUENTA = ?) "
                    + "WHERE K_IDCUENTA=?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, movimiento.getCuenta_k_idCuenta());
            prepStmt.setInt(2, cuenta.getK_idCuenta());
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("CuentaDAO", "No se resto del saldo la cuota del crédito "+e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
}
