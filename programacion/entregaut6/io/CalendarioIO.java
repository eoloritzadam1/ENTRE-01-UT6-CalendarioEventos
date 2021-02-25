package programacion.entregaut6.io;

import programacion.entregaut6.modelo.Evento;
import programacion.entregaut6.modelo.CalendarioEventos;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * La clase contiene un método estático que permite
 * cargar el calendario de eventos con los datos leídos desde
 * un fichero
 */
public class CalendarioIO {
    
	/**
	*  carga el calendario de eventos
	*/
	public static void cargarEventos(CalendarioEventos calendario) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("eventos.csv"));
			while (sc.hasNextLine()) {
				String linea = sc.nextLine();
				Evento evento = parsearLinea(linea);
				calendario.addEvento(evento);

			}

		} catch (IOException e) {
			System.out.println("Error al leer del fichero");
		} finally {
			if (sc != null) {
				sc.close();
			}
		}

	}

	/**
	 *  
	 */
	private static Evento parsearLinea(String linea) {
		String[] datos = linea.split(",");
		return new Evento(datos[0], datos[1], datos[2], datos[3]);

	}

}
