package beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.VentanaPrincipal;


public class BeanSheetExcel {
	
	private String   timeStamp        = "";
	private Calendar timeLocal        = null;
	private String   fechaLocal       = "";
	private String   codRequest       = "";
	private String[] codError         = new String[2];
	private String[] descripcionError = new String[2];
	private Integer  numDeCasos       = 0;
	private String   tipoError        = "";
	private String   comentarios      = "";
	private String   orderId          = "";

	public BeanSheetExcel() {
		
	}
	
	public BeanSheetExcel(String tS, String cR, String[] cE, String[] dE, String oId) 
	{
		this.timeStamp = tS;
		this.codRequest = cR;
		this.codError[0] = cE[0];
		this.codError[1] = cE[1];
		this.descripcionError[0] = dE[0];
		this.descripcionError[1] = dE[1];
		
		this.numDeCasos = 1;

		//UTC = GMT = Zulu
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar c = GregorianCalendar.getInstance(timeZone);
		//tS = YYYY-MM-DDTHH:MM:SS.SSSZ
		//-> Advertencia !!! En este campo perdemos los milisegundos
		c.set(Integer.parseInt(tS.substring(0,4)),    //A�o
              Integer.parseInt(tS.substring(5,7))-1,  //Mes
              Integer.parseInt(tS.substring(8,10)),   //Dia
              Integer.parseInt(tS.substring(11,13)),  //Hora
              Integer.parseInt(tS.substring(14,16)),  //Minutos
              Integer.parseInt(tS.substring(17,19))); //Segundos
		this.timeLocal  = c;
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
		this.fechaLocal = sdf.format(c.getTime()).substring(0,23);
		
		this.orderId = oId; 

		//VentanaPrincipal.showInfo("Request: " + this.codRequest + " - UTC/GMT/Zulu: " + tS + " - Local: " + this.fechaLocal);
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String getCodRequest() {
		return codRequest;
	}
	
	public void setCodRequest(String codRequest) {
		this.codRequest = codRequest;
	}

	public Integer getNumDeCasos() {
		return numDeCasos;
	}

	public void setNumDeCasos(Integer numDeCasos) {
		this.numDeCasos = numDeCasos;
	}

	public Calendar getTimeLocal() {
		return timeLocal;
	}

	public void setTimeLocal(Calendar timeLocal) {
		this.timeLocal = timeLocal;
	}

	public String getFechaLocal() {
		return fechaLocal;
	}

	public void setFechaLocal(String fechaLocal) {
		this.fechaLocal = fechaLocal;
	}

	public String getTipoError() {
		return tipoError;
	}

	public void setTipoError(String tipoError) {
		this.tipoError = tipoError;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String[] getCodError() {
		return codError;
	}

	public void setCodError(String[] codError) {
		this.codError = codError;
	}

	public String[] getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(String[] descripcionError) {
		this.descripcionError = descripcionError;
	}
}
