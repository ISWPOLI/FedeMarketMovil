package qantica.com.conexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import qantica.com.fedemarket.R;
import qantica.com.fedemarket.Singleton;
import qantica.com.conf.RecursosRed;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class Descarga {

	/**
	 * constante del indentificador interno de la descarga
	 */
	final static String strPref_Download_ID = "PREF_DOWNLOAD_ID";

	/**
	 * instancia de las propiedades de preferencias
	 */
	static SharedPreferences preferenceManager;

	/**
	 * instancia de administrador de la descarga
	 */
	static DownloadManager downloadManager;

	static Activity _actividad;
	static Context _contexto;
	
	/**
	 * metodo principal que dara inicio a la descarga
	 * 
	 * @param actividad
	 *            --> actividad desde la que se realizara la descarga
	 * @param contexto
	 *            --> contexto de la actividad desde la que se realizara la
	 *            descarga
	 */
	public static void descargar(Activity actividad,
			Context contexto, String url) {

		_actividad=actividad;
		_contexto=contexto;
		
		// inicializacion de las preferencias
		preferenceManager = PreferenceManager
				.getDefaultSharedPreferences(actividad);

		// inicializacion del administrador de decarga con el servicio de
		// descargar (API 9 en adelante)
		downloadManager = (DownloadManager) actividad
				.getSystemService(contexto.DOWNLOAD_SERVICE);

		
		int aux= Integer.parseInt(url);
		String user = getName();
		
		Log.v("fed", RecursosRed.URL_DESCARGA+"?id_contenido="+ aux+"&id_usuario="+Singleton.getUid()+"&nombre_usuario="+user);
		
		// declaracion y asignacion de la url de descarga
		Uri downloadUri = Uri.parse(RecursosRed.URL_DESCARGA+"?id_contenido="+ aux+"&id_usuario="+Singleton.getUid()+"&nombre_usuario="+user);
		
		// declaracion e inicializacion de la respuesta de la descarga con la
		// url tratada
		DownloadManager.Request request = new DownloadManager.Request(
				downloadUri);

		// identificador de la descarga
		long id = downloadManager.enqueue(request);

		// preparacion del evento de descarga completa
		IntentFilter intentFilter = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);

		// asignacion de la accion al evento
		contexto.registerReceiver(downloadReceiver, intentFilter);

		// adicion de los valores definidos a las preferencias
		Editor PrefEdit = preferenceManager.edit();
		PrefEdit.putLong(strPref_Download_ID, id);
		PrefEdit.commit();

	}

	/**
	 * Metodo encargado de eliminar los espacios en los nombres para enviar la peticion
	 * @return
	 */
	private static String getName() {
		String name = Singleton.getUname();
		String tmp = "";
		
		for (int i = 0; i < name.length(); i++) {
			
			if(name.charAt(i)!=' ')
			{
				tmp=tmp+name.charAt(i);
			}
		}
		
		return tmp;
	}

	/**
	 * metodo que se encarga de gestionar la accion de descarga completa
	 */
	private static BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(preferenceManager.getLong(strPref_Download_ID,
					0));
			Cursor cursor = downloadManager.query(query);

			if (cursor.moveToFirst()) {
				int columnIndex = cursor
						.getColumnIndex(DownloadManager.COLUMN_STATUS);
				int status = cursor.getInt(columnIndex);
				
				if (status == DownloadManager.STATUS_SUCCESSFUL) {

					Log.i("fed", ">>> 1");
					
					// asignacion del nombre del archivo descargado
					String uriString = cursor.getString(cursor
							.getColumnIndex(DownloadManager.COLUMN_TITLE));

					Log.i("fed", ">>> 2");
					
					// Retrieve the saved request id
					long downloadID = preferenceManager.getLong(
							strPref_Download_ID, 0);

					Log.i("fed", ">>> 3");
					
					ParcelFileDescriptor file;
					try {

						file = downloadManager.openDownloadedFile(downloadID);
						FileInputStream fileInputStream = new ParcelFileDescriptor.AutoCloseInputStream(
								file);
				
						Log.i("fed", ">>> 4");
						
						FileOutputStream fos = new FileOutputStream(
								"sdcard/Fedecafe Market/" + uriString);

						Log.i("fed", ">>> 5");
						
						byte[] array = new byte[1000]; // buffer temporal de
														// lectura.
						int leido = fileInputStream.read(array);

						Log.i("fed", ">>> 6");
						
						while (leido > 0) {

							fos.write(array, 0, leido);
							leido = fileInputStream.read(array);
						}

						Log.i("fed", ">>> 7");
						
						fileInputStream.close();
						fos.close();

						Log.i("Fichero creado", "Fichero SD creado!");

						notificarDescarga("sdcard/Fedecafe Market/" + uriString);

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}

		private void notificarDescarga(String cadena) {
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager notManager = 
				(NotificationManager) _actividad.getSystemService(ns);
			
			//Configuramos la notificaci�n
			int icono = R.drawable.rating_2;
			CharSequence textoEstado = "Descarga Completa!";
			long hora = System.currentTimeMillis();

			Notification notif = new Notification(icono, textoEstado, hora);
			
			//Configuramos el Intent
			Context contexto = _contexto.getApplicationContext();
			CharSequence titulo = "Descarga Completa";
			CharSequence descripcion = "Archivo descargado correctamenta.";
			
			
			File file = new File(cadena); 
			Uri path = Uri.fromFile(file); 
            Intent noIntent = new Intent(Intent.ACTION_VIEW); 

            int dotPos = file.getName().lastIndexOf(".");
            String extension = file.getName().substring(dotPos);
    		
            Log.i("extension", "  "+getData(extension));
            
            switch ( getData(extension) ) {
            
			case 0:
				noIntent.setDataAndType(path, "application/vnd.android.package-archive");
				break;
			case 1:
				noIntent.setDataAndType(path, "image/jpeg");
				break;
			case 2:
				noIntent.setDataAndType(path, "image/png");
				break;

			default:
				break;
			}
            
            //noIntent.setDataAndType(path, "application/vnd.android.package-archive"); 
            noIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			
			PendingIntent contIntent = PendingIntent.getActivity(
					contexto, 0, noIntent, 0);

			notif.setLatestEventInfo(
					contexto, titulo, descripcion, contIntent);
			
			//AutoCancel: cuando se pulsa la notificai�n �sta desaparece
			notif.flags |= Notification.FLAG_AUTO_CANCEL;
			
			//Enviar notificaci�n
			notManager.notify(1, notif);
			
		}	
		
	};
	
	public static int getData(String cadena){
		

		
		if(cadena.equals(".apk")){
			return 0;
		}else
			if(cadena.equals(".jpg")){
				return 1;
			}else
				if(cadena.equals(".png")){
					return 2;
				}
		return 99;
	}
}
