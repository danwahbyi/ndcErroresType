package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.MyUtil;
import com.VentanaPrincipal;

import beans.BeanSheetExcel;

public class Resiber_SSE_ORM_9337 {
	
	private BeanSheetExcel bean = null;
	private String request = "";
	private String fchIni  = "";
	private String fchFin  = "";

	private void detalleCaso12000039()
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptResiberTktOut.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null) 
			{
				//Hay respuesta (correcta o no) !!!
				JSONObject jsonResponse = new JSONObject(responseKibana);
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				if (hitsArray.length() != 0) 
				{
					JSONObject objArray = hitsArray.getJSONObject(0);
					JSONObject source = objArray.getJSONObject("_source");
					JSONObject kpi = source.getJSONObject("kpi");
					JSONObject parameters = kpi.getJSONObject("parameters");
					JSONObject message = parameters.getJSONObject("message");
					String payload = message.getString("payload.string");
					
					Document doc = MyUtil.convertStringToXMLDocument(payload);
					
					NodeList nlDocs = doc.getElementsByTagName("doc_id");
					String annadir = "";
					for(int i=0; i<nlDocs.getLength(); i++) {  
					   Node nodo = nlDocs.item(i);  
					   if (nodo != null && nodo instanceof Element) {
						   String incluir = "Doc " + (i + 1) + " : "  + nodo.getTextContent();
						   annadir += "\n" + incluir;
						   VentanaPrincipal.showInfo(incluir);
					   }  
					}
					
					this.bean.setTipoError("INCORRECT INPUT");
					this.bean.setComentarios(bean.getComentarios() + annadir);
				}
				else
				{
					VentanaPrincipal.showInfo("Resiber_SSE_ORM_9001.detalleCaso12000039(): Array de resultados vacío en scriptResiberTktOut.sh");
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError("Resiber_SSE_ORM_9001.detalleCaso12000039() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
		}
	}
	
	private void detalleCaso11200003()
	{
		try
		{
			this.bean.setTipoError("FUNCIONAL");
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}
	
	private void detalleCaso11000011()
	{
		try
		{
			this.bean.setTipoError("RESIBER");
			this.bean.setComentarios(this.bean.getComentarios() + "\n" + "> INC000001265280");
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}
	
	private void detalleCaso14500001()
	{
		try
		{
			this.bean.setTipoError("RESIBER");
			this.bean.setComentarios(this.bean.getComentarios() + "\n" + "> INC000001265285");
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}
	
	private boolean obtenerCodDesDeResiberResIn()
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptResiberResIn.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null) 
			{
				//Hay respuesta (correcta o no) !!!
				JSONObject jsonResponse = new JSONObject(responseKibana);
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				if (hitsArray.length() != 0) 
				{
					JSONObject objArray = hitsArray.getJSONObject(0);
					JSONObject source = objArray.getJSONObject("_source");
					JSONObject kpi = source.getJSONObject("kpi");
					JSONObject parameters = kpi.getJSONObject("parameters");
					JSONObject message = parameters.getJSONObject("message");
					String payload = message.getString("payload.string");
					
					Document doc = MyUtil.convertStringToXMLDocument(payload);
					
					String codResiber = MyUtil.obtenerCodigoDeXML(doc);
					String descResiber = MyUtil.obtenerDescripcionDeXML(doc);
					
					this.bean.setComentarios((codResiber!=null?codResiber:"null") + " - " + (descResiber!=null?descResiber:"null"));
					VentanaPrincipal.showInfo(this.bean.getComentarios());
					
					if(codResiber != null) {
						return true;
					}
				}
				else {
					VentanaPrincipal.showInfo("Resiber_SSE_ORM_9001.analizar(): Array de resultados vacío en scriptResiberResIn.sh");
				}
			}
			
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError("Resiber_SSE_ORM_9001.obtenerCodDesDeResiberResIn() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
			return false;
		}
		
	}
	
	public boolean obtenerCodDesDeResiberTktIn() 
	{
		try
		{
			this.bean = bean;
			this.request = bean.getCodRequest();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = bean.getTimeLocal();
			this.fchIni = sdf.format(c.getTime());
			this.fchFin = sdf.format(c.getTime());
			                                                    
			String responseKibana = MyUtil.ejecutarShellScript("scriptResiberTktIn.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null) 
			{
				//Hay respuesta (correcta o no) !!!
				JSONObject jsonResponse = new JSONObject(responseKibana);
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				if (hitsArray.length() != 0) 
				{
					JSONObject objArray = hitsArray.getJSONObject(0);
					JSONObject source = objArray.getJSONObject("_source");
					JSONObject kpi = source.getJSONObject("kpi");
					JSONObject parameters = kpi.getJSONObject("parameters");
					JSONObject message = parameters.getJSONObject("message");
					String payload = message.getString("payload.string");
					
					Document doc = MyUtil.convertStringToXMLDocument(payload);
					
					String codResiber = MyUtil.obtenerCodigoDeXML(doc);
					String descResiber = MyUtil.obtenerDescripcionDeXML(doc);
					
					this.bean.setComentarios((codResiber!=null?codResiber:"null") + " - " + (descResiber!=null?descResiber:"null"));
					VentanaPrincipal.showInfo(bean.getComentarios());
					
					if(codResiber != null) 
					{
						switch(codResiber) 
						{
					    	case "11000011": detalleCaso11000011(); break;
					    	case "11200003": detalleCaso11200003(); break;
					    	case "12000039": detalleCaso12000039(); break;
					    	case "14500001": detalleCaso14500001(); break;
						}
						
						return true;
					}
				}
				else {
					System.out.println("Resiber_SSE_ORM_9001.obtenerCodDesDeResiberTktIn(): Array de resultados vacío en scriptResiberTktIn.sh");
				}
	        }
			
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError("Resiber_SSE_ORM_9001.obtenerCodDesDeResiberTktIn() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
			
			return false;
		}
	}
	
	public void analizar(BeanSheetExcel bean) 
	{
		try
		{
			this.bean = bean;
			this.request = bean.getCodRequest();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = bean.getTimeLocal();
			this.fchIni = sdf.format(c.getTime());
			this.fchFin = sdf.format(c.getTime());
						
			if (obtenerCodDesDeResiberTktIn()) {
				//OK
			} else if(obtenerCodDesDeResiberResIn()) {
				//OK
			} else {
				//¿?
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError("Resiber_SSE_ORM_9001.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
		}
	}
	
}
