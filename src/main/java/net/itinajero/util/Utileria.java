package net.itinajero.util;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class Utileria {

	public static String reemplazarVacio(String cadena) {
		char[] cadenaNombre = cadena.toCharArray();

		char[] cadenaCorregida = new char[cadena.length()];
		int conteo = 0;
		for (int i = 0; i < cadenaNombre.length; i++) {
			if (Character.compare(cadenaNombre[i], ' ') == 0) {

			} else {
				cadenaCorregida[conteo] = cadenaNombre[i];
				conteo++;
			}
		}
		String nuevaCadena = new String(cadenaCorregida);
		return nuevaCadena.trim();
	}

	public static String guardarArchivo(MultipartFile multiPart, String ruta) {
		// Obtenemos el nombre original del archivo.
		String nombreOriginal = multiPart.getOriginalFilename().trim();

		// Reemplazamos los espacios en blanco por -
		nombreOriginal = nombreOriginal.replace(' ', '-');
		String nombreFinal = randomAlphaNumeric(8) + nombreOriginal;
		try {
			// Formamos el nombre del archivo para guardarlo en el disco duro.
			File imageFile = new File(ruta + nombreFinal);
			System.out.println("Archivo: " + imageFile.getAbsolutePath());
			// Guardamos fisicamente el archivo en HD.
			multiPart.transferTo(imageFile);
			return nombreFinal;
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
			return null;
		}
	}

	/*
	 * Metodo para generar una cadena aleatoria de longitud N
	 * 
	 * @param count
	 * 
	 * @return
	 */
	public static String randomAlphaNumeric(int count) {
		String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * CARACTERES.length());
			builder.append(CARACTERES.charAt(character));
		}
		return builder.toString();
	}

}
