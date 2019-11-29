package com;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class MyUtil {
	
	public static String ventanaEnEjecucion = "VentanaPrincipal";

	public static void escribirTraza(String tipo, String mensaje) 
	{
		if ("VentanaPrincipal".equals(ventanaEnEjecucion))
		{
			if ("INFO".equals(tipo)) {
				VentanaPrincipal.showInfo(mensaje);
			} else if ("WARNING".equals(tipo)) {
				VentanaPrincipal.showWarning(mensaje);
			} else if ("ERROR".equals(tipo)) {
				VentanaPrincipal.showError(mensaje);
			}
		}
		else if ("VentanaPrincipalIberiaPay".equals(ventanaEnEjecucion))
		{
			if ("INFO".equals(tipo)) {
				VentanaPrincipalIberiaPay.showInfo(mensaje);
			} else if ("WARNING".equals(tipo)) {
				VentanaPrincipalIberiaPay.showWarning(mensaje);
			} else if ("ERROR".equals(tipo)) {
				VentanaPrincipalIberiaPay.showError(mensaje);
			}
		}
	}

	//Nombre que figuran en las pestañas
	public static String getNombrePestaniaExcel(String codigo) 
	{
		String nombre = null;
		
		switch(codigo) {
			//AirDocIssue         
			case "ADI": nombre = "AirDocIssue";   break;
			//CreateOrder
			case "OC":  nombre = "OrderCreate";   break;
			//FlightPrice
			case "FP":  nombre = "Fare";  	      break;
			//OrderCancel
			case "CA":	nombre = "OrderCancel";   break;
			//Seats
			case "SE":	nombre = "Seat"; 		  break;
			//Baggages
			case "BA":  nombre = "Baggage";       break;
			//OrderChange
			case "OCH":	nombre = "OrderChange";   break;
			//ItinReshop
			case "IR":	nombre = "ItinReshop";    break;
			//OrderRetrieve
			case "OR":	nombre = "OrderRetrieve"; break;
			
			default:
				escribirTraza("ERROR", "ERROR - getNombreLargoServicio(" + codigo + "): Nombre de servicio no encontrado");
		}
		
		return nombre;
	}
	
	public static String getNombreScript_AA(String codigo) 
	{
		String nombre = null;
		
		switch(codigo) {
			//AirDocIssue
			case "ADI": nombre = "AA_scriptAirDocIssue.sh";   break;
			//CreateOrder
			case "OC":  nombre = "AA_scriptOrderCreate.sh";   break;
			//FlightPrice
			case "FP":  nombre = "AA_scriptFlightPrice.sh";   break;
			//OrderCancel
			case "CA":	nombre = "AA_scriptOrderCancel.sh";   break;
			//Seats
			case "SE":	nombre = "AA_scriptSeat.sh"; 		  break;
			//Baggages
			case "BA":  nombre = "AA_scriptBaggage.sh";       break;
			//OrderChange
			case "OCH":	nombre = "AA_scriptOrderChange.sh";   break;
			//ItinReshop
			case "IR":	nombre = "AA_scriptItinReshop.sh";    break;
			//OrderRetrieve
			case "OR":	nombre = "AA_scriptOrderRetrieve.sh"; break;
			
			default:
				escribirTraza("ERROR", "getNombreScript(" + codigo + "): Nombre de servicio no encontrado");
		}
		
		return nombre;
	}
	
	public static String getNombreScript_BB(String codigo) 
	{
		String nombre = null;
		
		switch(codigo) {
			//AirDocIssue
			case "ADI": nombre = "BB_scriptAirDocIssue.sh";   break;
			//CreateOrder
			case "OC":  nombre = "BB_scriptOrderCreate.sh";   break;
			//FlightPrice
			case "FP":  nombre = "BB_scriptFlightPrice.sh";   break;
			//OrderCancel
			case "CA":	nombre = "BB_scriptOrderCancel.sh";   break;
			//Seats
			case "SE":	nombre = "BB_scriptSeat.sh"; 		  break;
			//Baggages
			case "BA":  nombre = "BB_scriptBaggage.sh";       break;
			//OrderChange
			case "OCH":	nombre = "BB_scriptOrderChange.sh";   break;
			//ItinReshop
			case "IR":	nombre = "BB_scriptItinReshop.sh";    break;
			//OrderRetrieve
			case "OR":	nombre = "BB_scriptOrderRetrieve.sh"; break;
			
			default:
				escribirTraza("ERROR", "getNombreScript(" + codigo + "): Nombre de servicio no encontrado");
		}
		
		return nombre;
	}	
	
	/*
	 * la columna se identificará por el servicio
	 * la fila se identificará por el día de la semana
	 * 4-> lunes
	 * 5-> martes
	 * ...
	 */
	public static String getUbicacionPestaniaCero(String codServ, String fch)
	{
		String columna = null;
		String fila = null;
		
		switch(codServ) {
			//AirDocIssue
			case "ADI": columna = "B"; break;
			//CreateOrder
			case "OC":  columna = "E"; break;
			//FlightPrice
			case "FP":  columna = "D"; break;
			//OrderCancel
			case "CA":	columna = "F"; break;
			//Seats
			case "SE":	columna = "G"; break;
			//Baggages
			case "BA":  columna = "H"; break;
			//OrderChange
			case "OCH":	columna = "I"; break;
			//ItinReshop
			case "IR":	columna = "J"; break;
			//OrderRetrieve
			case "OR":	columna = "K"; break;
			
			default:
				escribirTraza("ERROR", "getUbicacionPestaniaCero(" + codServ + "): Nombre de servicio no encontrado");
		}
		
		Calendar c = GregorianCalendar.getInstance();
	    c.set(Integer.parseInt(fch.substring(6,10)), Integer.parseInt(fch.substring(3,5))-1, Integer.parseInt(fch.substring(0,2)));
	    int nD=c.get(Calendar.DAY_OF_WEEK); 
	    switch (nD) {
	        case 1: fila = "10"; break;
	        case 2: fila =  "4"; break;
	        case 3: fila =  "5"; break;
	        case 4: fila =  "6"; break;
	        case 5: fila =  "7"; break;
	        case 6: fila =  "8"; break;
	        case 7: fila =  "9"; break;
	    }
	    		
		return columna + fila;
	}
	
	public static Document convertStringToXMLDocument(String xmlString)
    {
        try
        {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
            Document doc = documentBuilder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e) {
        	e.printStackTrace();
        	escribirTraza("ERROR", e.getMessage());
        }
        return null;
    }
	
	public static String ejecutarShellScript(String scriptMasParametros)
	{
		try
		{
			String path = ".\\src\\scripts\\";
		
			Process p = null;
			
			escribirTraza("INFO", "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
			escribirTraza("INFO", "MyUtil.ejecutarShellScript(): " + scriptMasParametros);
			System.out.println("PATH:" + "sh " + path + scriptMasParametros);
			p = Runtime.getRuntime().exec("sh " + path + scriptMasParametros);
			System.out.println("PATH1:" + "sh " + path + scriptMasParametros);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(),"UTF8"));
			BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			String line;
			StringBuilder sbResponse = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sbResponse.append(line);
			}
			br.close();
			while ((line = brError.readLine()) != null) {
				escribirTraza("INFO", line);
			}
			brError.close();
			int resultCode = p.waitFor();
			if (resultCode == 0) 
			{
				//Hay respuesta, correcta o no !!!
				return sbResponse.toString();
			}
			else
			{
				escribirTraza("INFO", "MyUtil.ejecutarShellScript(): ResultCode = " + resultCode);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			escribirTraza("ERROR", e.getMessage());
		}
		return null;
	}
	
	public static String obtenerCodigoDeXML(Document doc)
	{
		try
		{
			//http://www.latascadexela.es/2008/07/java-y-xml-dom-ii.html
			NodeList nlCode = doc.getElementsByTagName("code");
			Node nodoCode = nlCode.item(0);
			String s = nodoCode!=null?nodoCode.getTextContent():null;
			
			//Puede estar en castellano !!!
			if (s == null) {
				NodeList nlCodigo = doc.getElementsByTagName("codigo");
				Node nodoCodigo = nlCodigo.item(0);
				s = nodoCodigo!=null?nodoCodigo.getTextContent():null;
			}
			
			return s;
		}
		catch (Exception e) {
			e.printStackTrace();
			escribirTraza("ERROR", e.getMessage());
			return null;
		}
	}
	
	public static String obtenerDescripcionDeXML(Document doc)
	{
		try
		{
			NodeList nlDescription = doc.getElementsByTagName("description");
			Node nodoDesc = nlDescription.item(0);
			String s = nodoDesc!=null?nodoDesc.getTextContent():null;
			
			//Puede estar en castellano !!!
			if (s == null) {
				NodeList nlDescripcion = doc.getElementsByTagName("descripcion");
				Node nodoDescripcion = nlDescripcion.item(0);
				s = nodoDescripcion!=null?nodoDescripcion.getTextContent():null;
			}
			
			return s;
		}
		catch (Exception e) {
			e.printStackTrace();
			escribirTraza("ERROR", e.getMessage());
			return null;
		}
	}
	
	
}
