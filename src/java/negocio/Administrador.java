/*
 * Obtiene los datos de SocioDAO, CreditoDAO, CuentaDAO.
 */
package negocio;

import datos.SocioDAO;
import datos.CreditoDAO;
import datos.CuentaDAO;
import datos.CuentaFondoDAO;
import datos.FondoDAO;
import datos.MovimientoDAO;
import datos.PlanPagoDAO;
import datos.RegFondoDAO;
import util.RHException;

/**
 *
 * @author ASUS
 */
public class Administrador {

    private SocioDAO socioDAO;
    private CuentaDAO cuentaDAO;
    private CreditoDAO creditoDAO;
    private FondoDAO fondoDAO;
    private MovimientoDAO movimientoDAO;
    private PlanPagoDAO planPagoDAO;
    private RegFondoDAO regFondoDAO;
    private CuentaFondoDAO cuentaFondoDAO;

    private Socio socio;
    private Cuenta cuenta;
    private Credito credito;
    private Fondo fondo;
    private Movimiento movimiento;
    private PlanPago planPago;
    private RegFondo regFondo;
    private CuentaFondo cuentaFondo;

    public Administrador() {
        socioDAO = new SocioDAO();
        creditoDAO = new CreditoDAO();
        fondoDAO = new FondoDAO();
        cuentaDAO = new CuentaDAO();
        movimientoDAO = new MovimientoDAO();
        planPagoDAO = new PlanPagoDAO();
        regFondoDAO = new RegFondoDAO();
        cuentaFondoDAO = new CuentaFondoDAO();
    }

    /**
     * Invoca el m�todo del DAO que permite incluir un nuevo socio
     *
     * @param idSocio
     * @param nombre
     * @param apellido
     * @param ocupacion
     * @param tarjetaPro
     * @param estadoCivil
     * @param sexo
     * @param dirDomicilio
     * @param dirTrabajo
     * @param email
     * @param telDomicilio
     * @param telTrabajo
     */
    public void agregarSocio(int idSocio, String nombre, String apellido, String ocupacion, String tarjetaPro, String estadoCivil, String sexo, String dirDomicilio, String dirTrabajo, String email, String telDomicilio, String telTrabajo) throws RHException {
        socio = new Socio();
        socio.setK_idSocio(idSocio);
        socio.setN_nomSocio(nombre);
        socio.setN_apeSocio(apellido);
        socio.setO_ocupacion(ocupacion);
        socio.setN_tarjProfesional(tarjetaPro);
        socio.setO_eCivil(estadoCivil);
        socio.setO_dir_domic(dirDomicilio);
        socio.setO_dir_trab(dirTrabajo);
        socio.setO_email(email);
        socio.setO_tel_domic(telDomicilio);
        socio.setO_tel_trab(telTrabajo);
        socioDAO.agregarSocio(socio);
    }

    public void buscarSocioPorID(int id) {
        socio = new Socio();
        socio.setK_idSocio(id);
        /*socio.getN_nomSocio();
        socio.getN_apeSocio();
        socio.getN_tarjProfesional();
        socio.getO_email();
        socio.getF_ingreso();*/
        socioDAO.buscarSocioPorID(id);
    }

    public void buscarSocioPorNombre(String nombre) {
        socio = new Socio();
        socio.setN_nomSocio(nombre);
        /*socio.getK_idSocio();
        socio.getN_apeSocio();
        socio.getN_tarjProfesional();
        socio.getO_email();
        socio.getF_ingreso();*/
        socioDAO.buscarSocioPorNombre(nombre);
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Socio getSocio() {
        return new Socio();
    }

    /*
    Gestión Cuenta
     */
    // después de crear el socio, se le asocia una cuenta
    public void agregarCuenta(int idSocio) throws RHException {
        cuenta = new Cuenta();
        cuenta.setSocio_k_idSocio(idSocio);
        cuentaDAO.agregarCuenta(cuenta);
    }

    public void buscarCuenta(int idSocio) {
        cuenta = new Cuenta();
        cuenta.setSocio_k_idSocio(idSocio);
        /*cuenta.getK_idCuenta();
        cuenta.getV_saldo();*/
        cuentaDAO.buscarCuenta(idSocio);
    }

    public void modificarSaldo(double saldoCuenta, int idCuenta) throws RHException {
        cuenta = new Cuenta();
        cuenta.setV_saldo(saldoCuenta);
        cuenta.setK_idCuenta(idCuenta);
        cuentaDAO.modificarSaldo(cuenta);
    }

    // para desembolsar un credito
    public void sumarCreditoASaldo(int idCuenta) throws RHException {
        cuenta = new Cuenta();
        credito = new Credito();
        cuenta.setK_idCuenta(idCuenta);
        credito.setCuenta_k_idCuenta(idCuenta);
        cuentaDAO.sumarCreditoASaldo(cuenta, credito);
    }

    public void restarAporteASaldo(int idCuenta) throws RHException {
        cuenta = new Cuenta();
        movimiento = new Movimiento();
        cuenta.setK_idCuenta(idCuenta);
        movimiento.setCuenta_k_idCuenta(idCuenta);
        cuentaDAO.restarSaldoPorAporte(cuenta, movimiento);
    }

    public void restarCuotaASaldo(int idCuenta) throws RHException {
        cuenta = new Cuenta();
        movimiento = new Movimiento();
        movimiento.setCuenta_k_idCuenta(idCuenta);
        cuenta.setK_idCuenta(idCuenta);
        cuentaDAO.restarSaldoPorCuota(cuenta, movimiento);
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Cuenta getCuenta() {
        return new Cuenta();
    }

    /*
    Gestión Crédito
     */
    /**
     *
     * @param idCredito
     * @param tasaInt
     * @param plazo
     * @param fechaAprob
     * @param fechaDesem
     * @param valPrestado
     * @param saldoPendiente
     * @param estadoCred
     * @param modCred
     * @param idSocio
     * @param idCuenta
     * @throws util.RHException
     */
    //Revisar tipo setDate recibe sólo el día del mes en CreditoDAO 
    public void agregarCredito(float tasaInt, float valPrestado, String modCred, int idSocio, int idCuenta) throws RHException {
        credito = new Credito();
        credito.setP_tasaInteres(tasaInt);
        credito.setV_prestado(valPrestado);
        credito.setN_modcredito_ck(modCred);
        credito.setSocio_k_id_socio(idSocio);
        credito.setCuenta_k_idCuenta(idCuenta);
        creditoDAO.agregarCredito(credito, idCuenta);
    }

    public void buscarCredito(int idSocio) {
        credito = new Credito();
        credito.setSocio_k_id_socio(idSocio);
        /*credito.getP_tasaInteres();
        credito.getF_plazo();
        credito.getV_prestado();
        credito.getSdo_pendiente();
        credito.getN_e_credito_ck();*/
        creditoDAO.buscarCredito(idSocio);
    }

    //Crédito paso 2
    public void desembolsarCredito(String f_plazo, int idSocio) throws RHException {
        credito = new Credito();
        credito.setF_plazo(f_plazo);
        credito.setSocio_k_id_socio(idSocio);
        creditoDAO.desembolsarCredito(credito);
    }

    public void cambiarEstadoCredito(String estadoCred, int idCredito) throws RHException {
        credito = new Credito();
        credito.setN_e_credito_ck(estadoCred);
        credito.setK_idcredito(idCredito);
        creditoDAO.cambiarEstadoCredito(credito);
    }

    public double consultarSaldoCredito(int idSocio) {
        credito = new Credito();
        credito.setSocio_k_id_socio(idSocio);
        //credito.getSdo_pendiente();
        return creditoDAO.consultarSaldoCredito(idSocio);
    }

    public void cancelarCredito(int idSocio) throws RHException {
        credito = new Credito();
        credito.setSocio_k_id_socio(idSocio);
        creditoDAO.cancelarCredito(credito);
    }

    public void descontarSaldo(int idCuenta) throws RHException {
        credito = new Credito();
        movimiento = new Movimiento();
        movimiento.setCuenta_k_idCuenta(idCuenta);
        credito.setCuenta_k_idCuenta(idCuenta);
        creditoDAO.descontarSaldoPend(credito, movimiento);
    }

    public void setCredito(Credito credito) {
        this.credito = credito;
    }

    public Credito getCredito() {
        return new Credito();
    }

    /*
    Gestión Fondo
     */
    public void consultarFondo(Fondo fondo) {
        fondo = new Fondo();
        /*fondo.getK_idfondo();
        fondo.getV_capital_tot();
        fondo.getV_capital_disp();*/
        fondoDAO.consultarFondo();
    }

    public void modificarCapitalTotFondo() throws RHException {
        fondo = new Fondo();
        fondoDAO.modificarCapitalTotFondo();
    }

    public void modificarCapitalDispFondo() throws RHException {
        fondo = new Fondo();
        fondoDAO.modificarCapitalDispFondo();
    }

    public void setFondo(Fondo fondo) {
        this.fondo = fondo;
    }

    public Fondo getFondo() {
        return new Fondo();
    }

    /*
    Gestión Movimiento
<<<<<<< Upstream, based on origin/master
     */
    public void agregarMovimiento(String tipoMov, int valMov, String medioPago, int idCuenta) throws RHException {

        movimiento = new Movimiento();
        movimiento.setN_tipo(tipoMov);
        movimiento.setV_mov(valMov);
        movimiento.setN_medPago(medioPago);

        movimientoDAO.agregarMovimiento(movimiento);
    }

    public void buscarMovimiento(int idCuenta) {
        movimiento = new Movimiento();
        movimiento.setCuenta_k_idCuenta(idCuenta);
        /*movimiento.getK_idMov();
        movimiento.getF_registro();
        movimiento.getV_mov();
        movimiento.getN_medPago();*/
        movimientoDAO.buscarMovimiento(idCuenta);
    }

    public void buscarAportes(int idCuenta) {
        movimiento = new Movimiento();
        movimiento.setCuenta_k_idCuenta(idCuenta);
        /*movimiento.getK_idMov();
        movimiento.getF_registro();
        movimiento.getV_mov();
        movimiento.getN_medPago();*/
        movimientoDAO.buscarAportes(idCuenta);
    }

    public void buscarCuotas(int idCuenta) {
        movimiento = new Movimiento();
        movimiento.setCuenta_k_idCuenta(idCuenta);
        /*movimiento.getK_idMov();
        movimiento.getF_registro();
        movimiento.getV_mov();
        movimiento.getN_medPago();*/
        movimientoDAO.buscarCuotas(idCuenta);
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public Movimiento getMovimiento() {
        return new Movimiento();
    }

    /*
    Gestión PlanPagoDAO
     */
    // credito paso 3
    public void agregarPlanPago(double valCapital, double valIntereses, int idCredito) throws RHException {
        planPago = new PlanPago();
        planPago.setV_capital(valCapital);
        planPago.setV_intereses(valIntereses);
        planPago.setCredito_k_idCredito(idCredito);
        planPagoDAO.agregarPlanPago(planPago);
    }

    public void buscarPlanPago(int idCredito) {
        planPago = new PlanPago();
        planPago.setCredito_k_idCredito(idCredito);
        /*planPago.getK_idconsig();
        planPago.getV_capital();
        planPago.getV_intereses();
        planPago.getQ_cuo_pagada();
        planPago.getN_tp_pago();*/
        planPagoDAO.buscarPlanPago(idCredito);
    }

    public void modificarPlanPago(int cuotaPagada, String tipoPago, int idCredito) throws RHException {
        planPago = new PlanPago();
        planPago.setQ_cuo_pagada(cuotaPagada);
        planPago.setN_tp_pago(tipoPago);
        planPago.setCredito_k_idCredito(idCredito);
        planPagoDAO.modificarPlanPago(planPago);
    }

    // credito paso 4
    public void calcularCapitalEInteres(int idCredito) throws RHException {
        planPago = new PlanPago();
        credito = new Credito();
        credito.setK_idcredito(idCredito);
        planPago.setCredito_k_idCredito(idCredito);
        planPagoDAO.calcularCapitalEIntereses(planPago, credito);
    }

    public void setPlanPago(PlanPago planPago) {
        this.planPago = planPago;
    }

    public PlanPago getPlanPago() {
        return new PlanPago();
    }

    /*
    Gestión RegFondoDAO
     */
    public void agregarRegFondo(String nomProceso, int idCuenta) throws RHException {
        regFondo = new RegFondo();
        regFondo.setN_proceso(nomProceso);
        regFondo.setCuenta_fondo_k_cta_fondo(idCuenta);
        regFondoDAO.agregarRegFondo(regFondo);
    }

    public void buscarRegFondo(int idRegistro) {
        regFondo = new RegFondo();
        regFondo.setK_registro(idRegistro);
        /*regFondo.getV_rendNetos();
        regFondo.getF_rendNetos();
        regFondo.getN_proceso();
        regFondo.getCuenta_fondo_k_cta_fondo();*/
        regFondoDAO.buscarRegFondo(idRegistro);
    }

    public void setRegFondo(RegFondo regFondo) {
        this.regFondo = regFondo;
    }

    public RegFondo getRegFondo() {
        return new RegFondo();
    }

    /*
    Gestión CuentaFondo
     */
    public void consultarCuentaFondo() {
        cuentaFondo = new CuentaFondo();
        /*cuentaFondo.getFondo_k_idfondo();
        cuentaFondo.getV_aportes();
        cuentaFondo.getV_interesXCredito();
        cuentaFondo.getV_rendFinan();
        cuentaFondo.getV_gFinanciero();
        cuentaFondo.getV_creditos();*/
        cuentaFondoDAO.consultarCuentaFondo();
    }

    public void calcularValorAportes(Movimiento movimiento) throws RHException {
        cuentaFondo = new CuentaFondo();
        //cuentaFondo.getV_aportes();
        cuentaFondoDAO.calcularValorAportes();
    }

    public void sumarAporte(int idCuenta) throws RHException {
        cuentaFondo = new CuentaFondo();
        movimiento = new Movimiento();
        movimiento.setCuenta_k_idCuenta(idCuenta);
        cuentaFondoDAO.sumarAporte(movimiento);
    }

    public void sumarCuotaCredito() throws RHException {
        cuentaFondo = new CuentaFondo();
        cuentaFondoDAO.sumarCuotaCredito();
    }

    // Crédito paso 1
    public void otorgarCredito(int idSocio) throws RHException {
        credito = new Credito();
        cuentaFondo = new CuentaFondo();
        credito.setSocio_k_id_socio(idSocio);
        cuentaFondoDAO.otorgarCredito(credito);
    }

    public void setCuentaFondo(CuentaFondo cuentaFondo) {
        this.cuentaFondo = cuentaFondo;
    }

    public CuentaFondo getCuentaFondo() {
        return new CuentaFondo();
    }
}
