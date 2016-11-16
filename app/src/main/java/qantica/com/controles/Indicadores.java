package qantica.com.controles;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.util.Log;

public class Indicadores {

	/**
	 * Extraer el valor del dolar y del caf� de la p�gina del DANE 
	 */

	public static ArrayList<String> array = new ArrayList<String>();

	static char numeros[] = {'1','2','3','4','5','6','7','8','9','0'};

	//Café (US cent/lb)&nbsp; 161,25
	public static String pruebaCafe(ArrayList<String> n){
		String resultado = null;
		String buscar = "Caf� (US cent/lb)&nbsp";		
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).contains(buscar)){				
				resultado = array.get(i).toString();
			}
		}		
		return resultado;
	}

	public static boolean esNumero(char a){
		boolean resultado = false;
		for (int i = 0; i < numeros.length; i++) {
			if (a == numeros[i]){
				resultado = true;
			}
		}
		return resultado;
	}



	public static String extraer(String n){
		String resultado = "";		
		if(n != null){
			String a = n.toUpperCase();
			char arreglo[] = a.toCharArray();
			for (int i = 0; i < arreglo.length; i++) {
				if(i < arreglo.length-2){
					if(esNumero(arreglo[i])&& esNumero(arreglo[i+1])&& esNumero(arreglo[i+2])){
						resultado = resultado + arreglo[i] + "," +arreglo[i+1]+arreglo[i+2];
					}
				}

			}
		}
		return resultado;
	}
	
	//T.R.M.:&nbsp; $2.522,71
		public static String pruebaDolar(ArrayList<String> n){
			String resultado = null;
			String buscar = "T.R.M";		
			for (int i = 0; i < array.size(); i++) {
				if (array.get(i).contains(buscar)){
					System.out.println(array.get(i));
					resultado = array.get(i);
				}
			}		
			return resultado;
		}
		
		public static String extraerDolar(String n){
			String resultado = "";
			String a = n.toUpperCase();
			char arreglo[] = a.toCharArray();
			for (int i = 0; i < arreglo.length; i++) {
				
					if(i < arreglo.length-3){
						if(esNumero(arreglo[i])&& arreglo[i+1] == '.' && esNumero(arreglo[i+2])&& esNumero(arreglo[i+3])){
							resultado = resultado + arreglo[i] +arreglo[i+1]+arreglo[i+2] + arreglo[i+3] + arreglo[i+4];
						}
					}			
			}
			return resultado;
		}
		
		


	public static void leer() {
		try {			
			URL url = new URL("http://www.cenicafe.org/es/index.php/inicio/indicadores_economicos");
			HttpURLConnection conexion = (HttpURLConnection)url.openConnection();
			conexion.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
			URLConnection con = url.openConnection();
			InputStream s = con.getInputStream();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
			String line = "";
			int n = 1;
			while ((line = bufferedReader.readLine()) != null) {
				n++;
				if(n >= 200 )
					array.add(line);				
			}		

		} catch (ConnectException ex){						
			Log.e("fed", "error");
			ex.printStackTrace();
		}catch (Exception ex){
			Log.i("fed", "Excepci�n 2");
			ex.printStackTrace();
		}
	}
}
