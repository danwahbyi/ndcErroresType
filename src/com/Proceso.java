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
import casos.Payment_PMT_PPM_12005;
import casos.Payment_PMT_PPM_9017;
import casos.Resiber_SSE_ORM_9001;
import casos.Resiber_SSE_ORM_9007;
import casos.Sara_SSE_ORM_900606;

public class Proceso {
	
	private List<BeanSheetExcel> almacenarValores(String responseKibana, String codServ)
	{
		try
		{
			List<BeanSheetExcel> myList = new ArrayList<BeanSheetExcel>();
			
			JSONObject jsonResponse = new JSONObject(responseKibana.toString());
			JSONObject hitsObj = jsonResponse.getJSONObject("hits");
			JSONArray hitsArray = hitsObj.getJSONArray("hits");
						
			for (int i = 0; i < hitsArray.length(); i++) 
			{
				JSONObject objArray = hitsArray.getJSONObject(i);
				JSONObject source = objArray.getJSONObject("_source");
				
				//@timestamp 
				String fH = source.getString("@timestamp");
				//request
			    String cR = source.getString("request");
				
			    String[] cE  = new String[2];
			    String[] dE  = new String[2];
			    if (source.has("exception")) 
			    {
			    	JSONObject exception = source.getJSONObject("exception");
			    	//Código de error y descripción
					cE[0] = exception.getString("errorCode");
				    if (exception.has("errorDescription")) dE[0] = exception.getString("errorDescription");
			    } 
			    else 
			    {
			    	JSONObject kpi = source.getJSONObject("kpi");
					JSONObject response = kpi.getJSONObject("response");
					JSONObject errors = response.getJSONObject("errors");
					JSONArray error = errors.getJSONArray("error.list.object");
					
					//WARNING !!! -> puede devolver varios errores...
					for(int j=0; j<error.length(); j++)
					{
						JSONObject errorArray = error.getJSONObject(j);
						//Código de error y descripción
						cE[j] = errorArray.getString("shortText.string");
					    dE[j] = errorArray.getString("value.string");
					    
					    //Actualmente sólo admitimos 2 valores
					    if(j==1) break;
					}
			    }
			    
			    String oId = "";
			    //Obtenemos el orderID en OrderCreate
			    if ("OC".equals(codServ)) {
				    JSONObject kpi = source.getJSONObject("kpi");
					JSONObject parameters = kpi.getJSONObject("parameters");
					JSONObject parameter1 = parameters.getJSONObject("parameter1");
					JSONObject query = parameter1.getJSONObject("query");
					JSONObject orderItems = query.getJSONObject("orderItems");
					JSONObject shoppingResponse = orderItems.getJSONObject("shoppingResponse");
					JSONObject responseID = shoppingResponse.getJSONObject("responseID");
					oId = responseID.getString("value.string");
			    }
			    
			    BeanSheetExcel bSG = new BeanSheetExcel(fH, cR, cE, dE, oId);
			    myList.add(bSG);
			}
			
			if (myList.size()==999) {
				VentanaPrincipal.showWarning("WARNING !!! - TOTAL Resquest encontradas (max 999): " + myList.size());
			} else {
				VentanaPrincipal.showInfo("TOTAL Resquest encontradas (max 999): " + myList.size());
			}			
			
			return myList;
		} 
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError(e.getMessage());
			return null;
		}
	}
	
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
					case "SSE_ORM_1000601": 
						CodeShareOperatingCarrier csoc = new CodeShareOperatingCarrier();
						csoc.analizar(bean);  
					break;
					
					case "SSE_ORM_9001": 
						Resiber_SSE_ORM_9001 r9001 = new Resiber_SSE_ORM_9001();
						r9001.analizar(bean);  
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
					
					case "FRAM_B0006":
					{
						switch(bF.getCodServicio())
						{
						   case "ADI":
							   ADI_FRAM_B0006 adiFram = new ADI_FRAM_B0006();
							   adiFram.analizar(bean);
						   break;
						}
					}
					break;
				}	
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError(e.getMessage());
		}
	}
	
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
