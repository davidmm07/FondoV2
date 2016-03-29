/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import negocio.Movimiento;
import util.RHException;
import util.ServiceLocator;

/**
 *
 * @author David Morales
 */
public class MovimientoDAO {

    public MovimientoDAO() {

    }

    // Se agrega un nuevo movimiento que asocia la cuenta de un socio con la del fondo
    public void agregarMovimiento(Movimiento movimiento) throws RHException {
        try {
            String strSQL = "INSERT INTO MOVIMIENTO (K_IDMOV,N_TIPO,F_REGISTRO,V_MOV,"
                    + "N_MEDPAGO,CUENTA_K_IDCUENTA,CUENTA_FONDO_K_CTA_FONDO) "
                    + "VALUES(MOVIMIENTO_SEQ.NEXTVAL,?,TO_DATE(SYSDATE,'DD/MM/YY'),"
                    + "?,?,?,99000281)";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setString(1, movimiento.getN_tipo());
            prepStmt.setInt(2, movimiento.getV_mov());
            prepStmt.setString(3, movimiento.getN_medPago());
            prepStmt.setInt(4, movimiento.getCuenta_k_idCuenta());
            prepStmt.executeUpdate();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        } catch (SQLException e) {
            throw new RHException("MovimientoDAO", "No se registró el movimiento" + e.getMessage());
        } finally {
            ServiceLocator.getInstance().liberarConexion();
        }
    }

    // Se busca un movimiento teniendo como parámetro la cuenta de un socio
    public void buscarMovimiento(int cuenta_k_idCuenta) {
        try {
            Movimiento m = new Movimiento();
            String strSQL = "SELECT K_IDMOV,N_TIPO,F_REGISTRO,V_MOV,N_MEDPAGO,CUENTA_K_IDCUENTA FROM MOVIMIENTO "
                    + "WHERE CUENTA_K_IDCUENTA = ?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, cuenta_k_idCuenta);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                rs.getInt(1);
                rs.getString(2);
                rs.getDate(3);
                rs.getInt(4);
                rs.getString(5);
                m.setCuenta_k_idCuenta(rs.getInt(6));
            }
        } catch (SQLException e) {

        }
    }

    // Se buscan los movimientos tipo aporte
    public void buscarAportes(int cuenta_k_idCuenta) {
        try {
            Movimiento m = new Movimiento();
            String strSQL = "SELECT K_IDMOV,F_REGISTRO,V_MOV,N_MEDPAGO,CUENTA_K_IDCUENTA FROM MOVIMIENTO "
                    + "WHERE CUENTA_K_IDCUENTA = ? AND N_TIPO = 'APORTE'" ;
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, cuenta_k_idCuenta);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                rs.getInt(1);
                rs.getDate(2);
                rs.getInt(3);
                rs.getString(4);
                m.setCuenta_k_idCuenta(rs.getInt(5));
            }
        } catch (SQLException e) {

        }
    }
    
    //Se buscan las cuotas de un socio
    public void buscarCuotas(int cuenta_k_idCuenta) {
        try {
            Movimiento m = new Movimiento();
            String strSQL = "SELECT K_IDMOV,F_REGISTRO,V_MOV,N_MEDPAGO,CUENTA_K_IDCUENTA FROM MOVIMIENTO "
                    + "WHERE CUENTA_K_IDCUENTA = ? AND N_TIPO = 'CUOTA DE CREDITO'" ;
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, cuenta_k_idCuenta);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                rs.getInt(1);
                rs.getDate(2);
                rs.getInt(3);
                rs.getString(4);
                m.setCuenta_k_idCuenta(rs.getInt(5));
            }
        } catch (SQLException e) {

        }
    }
    
    
    
    
    // Se calcula el tiempo desde el último aporte para saber si el socio está el día con sus aportes
    public Double calcularTiempoDesdeUltimoAporte(int cuenta_k_idCuenta){
        try{
            Movimiento m = new Movimiento();
            double nm=-1;
            String strSQL = "SELECT MONTHS_BETWEEN(SYSDATE,MAX(F_REGISTRO)) FROM MOVIMIENTO"
                    + " WHERE N_TIPO='APORTE' AND CUENTA_K_IDCUENTA = ?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, cuenta_k_idCuenta);
            ResultSet rs = prepStmt.executeQuery();
            while(rs.next()){
                nm = rs.getDouble(1);
            }
            return nm;
           
        }catch(SQLException e){
            return -1.0;
        }
    }

}
