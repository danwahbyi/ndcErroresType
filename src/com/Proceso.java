package com;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import beans.BeanFormulario;
import beans.BeanSheetExcel;
import casos.ADI_FRAM_B0006;
import casos.CodeShareOperatingCarrier;
import casos.OC_FRAM_B0006;
import casos.PMT_PPM_8006;
import casos.Payment_PMT_PPM_12005;
import casos.Payment_PMT_PPM_9017;
import casos.Payment_SSE_ORM_9021;
import casos.Resiber_SSE_ORM_9001;
import casos.Resiber_SSE_ORM_9002;
import casos.Resiber_SSE_ORM_9007;
import casos.Resiber_SSE_ORM_9337;
import casos.Sara_SSE_ORM_900606;

/**
 * 
 * @author 0004611
 *
 */
public class Proceso {
	
	
	/**
	 * 
	 * @param responseKibana
	 * @param codServ
	 * @return
	 */
	private List<BeanSheetExcel> almacenarValores(String responseKibana, String codServ)
	{	
		//@timestamp 
		String fechaS;
		//request
	    String requestS;
	    //PNR
	    String pnrS;
	    try 	{
			List<BeanSheetExcel> myList = new ArrayList<BeanSheetExcel>();
			
			JSONObject jsonResponse = new JSONObject(responseKibana.toString());
			JSONObject hitsObj = jsonResponse.getJSONObject("hits");
			JSONArray hitsArray = hitsObj.getJSONArray("hits");
						
			for (int i = 0; i < hitsArray.length(); i++) 
			{
				JSONObject objArray = hitsArray.getJSONObject(i);
				JSONObject source = objArray.getJSONObject("_source");
				
				//@timestamp 
				fechaS = source.getString("@timestamp");
				//request
			    requestS = source.getString("request");
			    String[] codigoE  = new String[2];
			    String[] descripcionE  = new String[2];
			    if (source.has("exception"))  {
			    	JSONObject exception = source.getJSONObject("exception");
			    	//Código de error y descripción
			    	codigoE[0] = exception.getString("errorCode");
				    if (exception.has("errorDescription")) descripcionE[0] = exception.getString("errorDescription");
			    } else  {
			    	JSONObject kpi = source.getJSONObject("kpi");
					JSONObject response = kpi.getJSONObject("response");
					JSONObject errors = response.getJSONObject("errors");
					JSONArray error = errors.getJSONArray("error.list.object");
					
					//WARNING !!! -> puede devolver varios errores...
					for(int j=0; j<error.length(); j++)
					{
						JSONObject errorArray = error.getJSONObject(j);
						//Código de error y descripción
						codigoE[j] = errorArray.getString("shortText.string");
						descripcionE[j] = errorArray.getString("value.string");
					    
					    //Actualmente sólo admitimos 2 valores
					    //DWI: Se comenta: if(j==1) break;
					}
			    }
			    
			    String orderIdS = "";
			    //Obtenemos el orderID en OrderCreate
			    if ("OC".equals(codServ)) {
				    JSONObject kpi = source.getJSONObject("kpi");
					JSONObject parameters = kpi.getJSONObject("parameters");
					JSONObject parameter1 = parameters.getJSONObject("parameter1");
					JSONObject query = parameter1.getJSONObject("query");
					JSONObject orderItems = query.getJSONObject("orderItems");
					JSONObject shoppingResponse = orderItems.getJSONObject("shoppingResponse");
					JSONObject responseID = shoppingResponse.getJSONObject("responseID");
					orderIdS = responseID.getString("value.string");
			    }
			    
			    BeanSheetExcel bSG = new BeanSheetExcel(fechaS, requestS, codigoE, descripcionE, orderIdS);
			    myList.add(bSG);
			}
			
			if (myList.size()==999) {
				VentanaPrincipal.showWarning("WARNING !!! - TOTAL Resquest encontradas (max 999): " + myList.size());
			} else {
				VentanaPrincipal.showInfo("TOTAL Resquest encontradas (max 999): " + myList.size());
			}			
			
			return myList;
		} catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 
	 * @param lista
	 */
	private void agruparPorCodigoYDescripcion(List<BeanSheetExcel> lista)
	{

		for (int i=0; i<lista.size(); i++) {
			
			BeanSheetExcel beanRef = lista.get(i);
			
			String codRef  = beanRef.getCodError()[0];
			String descRef = beanRef.getDescripcionError()[0];
			
			for (int j=i+1; j<lista.size();) {
				
				BeanSheetExcel beanCom = lista.get(j);
				
				String codCom = beanCom.getCodError()[0];
				String descCom = beanCom.getDescripcionError()[0];
				
				if (codRef.equals(codCom) && descRef.equals(descCom)) {
					Integer n = beanRef.getNumDeCasos();
					beanRef.setNumDeCasos(n+1);
					lista.remove(j);
					j--;
				}
				
				j++;
			}
		}
		
		Collections.sort(lista, new Comparator<BeanSheetExcel>() {
		    @Override
		    public int compare(BeanSheetExcel o1, BeanSheetExcel o2) {
		        return o1.getCodError()[0].compareTo(o2.getCodError()[0]);
		    }
		});
	}
	
	/**
	 * 
	 * @param lista
	 * @param bF
	 */
	private void analizarCasos(List<BeanSheetExcel> lista, BeanFormulario bF)
	{
		try
		{
			for (BeanSheetExcel bean: lista) 
			{
				//Analizaremos el código de error que primero se produzca... (parece que se almacenan con un PUSH)
				String codError = bean.getCodError()[1]!=null?bean.getCodError()[1]:bean.getCodError()[0];
				switch(codError) 
				{
					case "SSE_ORM_9337": 
						Resiber_SSE_ORM_9337 r9337 = new Resiber_SSE_ORM_9337();
						r9337.analizar(bean);  
					break;
					case "SSE_ORM_1000601": 
						CodeShareOperatingCarrier csoc = new CodeShareOperatingCarrier();
						csoc.analizar(bean);  
					break;
					//DWI: Resiber
					case "SSE_ORM_9001": 
						Resiber_SSE_ORM_9001 r9001 = new Resiber_SSE_ORM_9001();
						r9001.analizar(bean);  
					break;
					//DWI: Resiber
					case "SSE_ORM_9002": 
						Resiber_SSE_ORM_9002 r9002 = new Resiber_SSE_ORM_9002();
						r9002.analizar(bean);  
					break;
					case "SSE_ORM_9007": 
						Resiber_SSE_ORM_9007 r9007 = new Resiber_SSE_ORM_9007();
						r9007.analizar(bean);  
					break;
					
					case "SSE_ORM_900606":
						Sara_SSE_ORM_900606 sara = new Sara_SSE_ORM_900606();
						sara.analizar(bean, bF);
					break;
					
					case "PMT_PPM_9017":
						Payment_PMT_PPM_9017 pay = new Payment_PMT_PPM_9017();
						pay.analizar(bean);
					break;
					
					case "PMT_PPM_12005":
						Payment_PMT_PPM_12005 payNetplus = new Payment_PMT_PPM_12005();
						payNetplus.analizar(bean);
					break;
					
					//DWI: TIME_OUT
					case "PMT_PPM_8006":
					PMT_PPM_8006 payPPM = new PMT_PPM_8006();
					payPPM.analizar(bean);
					break;
					
					//DWI:
					case "SSE_ORM_9021":
						Payment_SSE_ORM_9021 paySSE = new Payment_SSE_ORM_9021();
						paySSE.analizar(bean);
					break;
					
					case "FRAM_B0006":
					{
						switch(bF.getCodServicio())
						{
						   case "ADI":
							   ADI_FRAM_B0006 adiFram = new ADI_FRAM_B0006();
							   adiFram.analizar(bean);
						   case "OC":
							   OC_FRAM_B0006 ocFram = new OC_FRAM_B0006();
							   ocFram.analizar(bean);
						   break;
						}
					}
					default:
						
						break;
				}	
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param listCheckServ
	 */
	public void ejecutar(List<BeanFormulario> listCheckServ) 
	{
		try
		{
			for (BeanFormulario beanFormulario: listCheckServ) 
			{
				String codServ = beanFormulario.getCodServicio();
				boolean agrupar = beanFormulario.isRespuestaAgrupada();
				boolean analizar = beanFormulario.isAnalizar();
				String fch = beanFormulario.getFecha();
				String hIni = beanFormulario.getHoraInicio();
				String hFin = beanFormulario.getHoraFin();
				
				String responseKibana = MyUtil.ejecutarShellScript(MyUtil.getNombreScript_AA(codServ) + " " + fch + " " + hIni + " " + hFin);
				
				if (responseKibana != null) 
				{
					List<BeanSheetExcel> listaBeans = almacenarValores(responseKibana.toString(), codServ);

					if (agrupar) {
						agruparPorCodigoYDescripcion(listaBeans);
					} else if (analizar) {
						analizarCasos(listaBeans, beanFormulario);
					}
					
					if (listaBeans != null && !listaBeans.isEmpty()) {
						//VentanaPrincipal.showInfo("Y SE ENVÍA A GOOGLE DRIVE...");
						//SendToGoogle.toSheet(listaBeans, beanFormulario);
						VentanaPrincipal.showInfo("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
						VentanaPrincipal.showInfo("1) Preparando datos para Excel...");
						SendToLocalExcel stle = new SendToLocalExcel();
						stle.toExcel(listaBeans, beanFormulario);
					} else {
						VentanaPrincipal.showWarning("WARNING - Ejecutar(" + codServ + ") / " + MyUtil.getNombrePestaniaExcel(codServ) + ": No hay resultados.");
					}
					
	            } else {
	            	
	            }			
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError(e.getMessage());
		}
	}

}
