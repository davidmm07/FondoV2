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
import negocio.RegFondo;
import util.RHException;
import util.ServiceLocator;

/**
 *
 * @author Cristian
 */
public class RegFondoDAO {
    
    public RegFondoDAO(){
        
    }
    
    // Se agrega un registro del estado del fondo
    public void agregarRegFondo(RegFondo regFondo) throws RHException{
        try{
            String strSQL = "INSERT INTO REGFONDO(K_REGISTRO,V_RENDNETOS,F_RENDNETOS,N_PROCESO,CUENTA_FONDO_K_CTA_FONDO) "
                    + "VALUES(REGFONDO_SEQ.NEXTVAL,(SELECT V_INTERESXCREDITO+V_RENDFINAN-V_GFINANCIERO FROM CUENTA_FONDO),"
                    + "TO_DATE(SYSDATE,'DD/MM/YY'),?,?)";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setString(1, regFondo.getN_proceso());
            prepStmt.setInt(2, regFondo.getCuenta_fondo_k_cta_fondo());
            prepStmt.executeUpdate();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        }catch(SQLException e){
            throw  new RHException("RegFondoDAO","No se pudo agregar registro del fondo "+ e.getMessage());
        }finally{
            ServiceLocator.getInstance().liberarConexion();
        }
    }
    
    public void buscarRegFondo(int k_registro){
        try{
            RegFondo r = new RegFondo();
            String strSQL = "SELECT K_REGISTRO,V_RENDNETOS,F_RENDNETOS,N_PROCESO,CUENTA_FONDO_K_CTA_FONDO FROM REGFONDO "
                    + "WHERE K_REGISTRO = ?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, k_registro);
            ResultSet rs = prepStmt.executeQuery();
            while(rs.next()){
                r.setK_registro(rs.getInt(1));
                rs.getDouble(2);
                rs.getDate(3);
                rs.getString(4);
                rs.getInt(5);
            }
        }catch(SQLException e){
            
        }
    }
    
}
