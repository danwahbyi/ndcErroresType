package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.MyUtil;

import beans.BeanSheetExcel;

public class Resiber_SSE_ORM_9007 {
	
	private BeanSheetExcel bean = null;
	private String request = "";
	private String orderId = "";
	private String fchIni  = "";
	private String fchFin  = "";

	private void detalleCaso457() 
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptFlightPriceOrderID.sh" + " " + this.orderId + " " + this.fchIni + " " + this.fchFin);

			if (responseKibana != null)
			{
				JSONObject jsonResponse = new JSONObject(responseKibana.toString());
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				String comentarios = "\n" + "Compañias operadoras: ";
				if (hitsArray.length() != 0) 
				{
					JSONObject objArray = hitsArray.getJSONObject(0);
					JSONObject source = objArray.getJSONObject("_source");
					JSONObject kpi = source.getJSONObject("kpi_ndc-dist_flightPrice_soap_2");
					JSONObject response = kpi.getJSONObject("response");
					JSONObject dataLists = response.getJSONObject("dataLists");
					JSONObject flightSegmentList = dataLists.getJSONObject("flightSegmentList");
					JSONArray flightSegment = flightSegmentList.getJSONArray("flightSegment");
					
					for (int j=0; j<flightSegment.length(); j++)
					{
						JSONObject fsElement = flightSegment.getJSONObject(j);
						JSONObject operatingCarrier = fsElement.getJSONObject("operatingCarrier");
						JSONObject airlineID = operatingCarrier.getJSONObject("airlineID");
						JSONObject flightNumber = operatingCarrier.has("flightNumber")?operatingCarrier.getJSONObject("flightNumber"):null;
						
						comentarios = comentarios + "\n" + "F" + (j+1) + ": " + airlineID.getString("value") + (flightNumber!=null?flightNumber.getString("value"):"") + " - " + operatingCarrier.getString("name");
					}

					bean.setTipoError("CODE-SHARE");
					bean.setComentarios(this.bean.getComentarios() + comentarios);
					System.out.println(this.bean.getComentarios());
				}
				else
				{
					System.out.println("Resiber_SSE_ORM_9007.analizar(): Array de resultados vacío en scriptFlightPriceOrderID.sh");
				}
			}
		}
		catch (Exception e) {
			System.out.println("ERROR !!! -> Resiber_SSE_ORM_9007.detalleCaso457() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			e.printStackTrace();
		}
	}
	
	public void analizar(BeanSheetExcel bean) 
	{
		try
		{
			this.bean = bean;
			this.request = bean.getCodRequest();
			this.orderId = bean.getOrderId();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = bean.getTimeLocal();
			this.fchIni = sdf.format(c.getTime());
			this.fchFin = sdf.format(c.getTime());
			
			String responseKibana = MyUtil.ejecutarShellScript("scriptResiberRequestTktIn.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
					JSONObject kpi = source.getJSONObject("kpi_sse-orm_resiber_tkt-in_1");
					JSONObject parameters = kpi.getJSONObject("parameters");
					JSONObject message = parameters.getJSONObject("message");
					String payload = message.getString("payload");
					
					Document doc = MyUtil.convertStringToXMLDocument(payload);
					
					String codResiber = MyUtil.obtenerCodigoDeXML(doc);
					String descResiber = MyUtil.obtenerDescripcionDeXML(doc);
					
					bean.setComentarios((codResiber!=null?codResiber:"") + " - " + (descResiber!=null?descResiber:""));
					System.out.println("Resiber_SSE_ORM_9007: " + bean.getComentarios());
					
					switch(codResiber) {
						case "457": detalleCaso457(); break;
					}
					
				}
				else 
				{
					System.out.println("Resiber_SSE_ORM_9007.analizar(): Array de resultados vacío en scriptResiberRequestTktIn.sh");
				}
	        }
		}
		catch (Exception e) {
			System.out.println("ERROR !!! -> Resiber_SSE_ORM_9007.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			e.printStackTrace();
		}
	}
	
}