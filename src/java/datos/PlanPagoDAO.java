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
import negocio.PlanPago;
import util.RHException;
import util.ServiceLocator;

/**
 *
 * @author Cristian
 */
public class PlanPagoDAO {
    
    public PlanPagoDAO(){
        
    }
    
    // Se agrega un plan de pago asociado a un crédito
    public void agregarPlanPago(PlanPago plan) throws RHException{
        try{
            String strSQL = "INSERT INTO PLAN_PAGO(K_IDCONSIG,V_CAPITAL,V_INTERESES,CREDITO_K_IDCREDITO) "
                    + "VALUES(PLAN_PAGO_SEQ.NEXTVAL,?,?,?)";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setDouble(1, plan.getV_capital());
            prepStmt.setDouble(2, plan.getV_intereses());
            prepStmt.setInt(3, plan.getCredito_k_idCredito());
            prepStmt.executeUpdate();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("PlanPagoDAO", "No se agregó el plan de pago" + e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    // Se busca el plan de pago por el id del crédito
    public void buscarPlanPago(int credito_k_idCredito){
        try{
            PlanPago p = new PlanPago();
            String strSQL = "SELECT K_IDCONSIG,F_PAGO,V_CAPITAL,V_INTERESES,Q_CUO_PAGADA,N_TP_PAGO,CREDITO_K_IDCREDITO "
                    + "FROM PLAN_PAGO WHERE CREDITO_K_IDCREDITO = ?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, credito_k_idCredito);
            ResultSet rs = prepStmt.executeQuery();
            while(rs.next()){
                rs.getInt(1);
                rs.getDate(2);
                rs.getDouble(3);
                rs.getDouble(4);
                rs.getInt(5);
                rs.getString(6);
                p.setCredito_k_idCredito(rs.getInt(7));
            }
        }catch(SQLException e){
            
        }
    }
    public void modificarPlanPago(PlanPago planPago) throws RHException{
        try{
            String strSQL = "UPDATE PLAN_PAGO SET F_PAGO = TO_DATE(SYSDATE,'DD/MM/YY'), Q_CUO_PAGADA = ?, "
                    + "N_TP_PAGO = ? WHERE CREDITO_K_IDCREDITO = ?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, planPago.getQ_cuo_pagada());
            prepStmt.setString(2, planPago.getN_tp_pago());
            prepStmt.setInt(3, planPago.getCredito_k_idCredito());
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("PlanPagoDAO","No se pudo modificar el plan de pago "+ e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    //Se modifica el valor del capital y de los intereses teniendo en cuenta el valor prestado en el credito
    //y la tasa de interés
    public void calcularCapitalEIntereses(PlanPago planPago, Credito credito) throws RHException{
        try{
            String strSQL = "UPDATE PLAN_PAGO SET V_CAPITAL = (SELECT V_PRESTADO FROM CREDITO WHERE K_IDCREDITO = ?), "
                    + "V_INTERESES = (SELECT V_PRESTADO/100 FROM CREDITO WHERE K_IDCREDITO = ?)*"
                    + "(SELECT P_TASAINTERES FROM CREDITO WHERE K_IDCREDITO = ?) WHERE CREDITO_K_IDCREDITO=?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, credito.getK_idcredito());
            prepStmt.setInt(2, credito.getK_idcredito());
            prepStmt.setInt(3, planPago.getCredito_k_idCredito());
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw new RHException("PlanPagoDAO", "No se calcularon los campos "+e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
}
