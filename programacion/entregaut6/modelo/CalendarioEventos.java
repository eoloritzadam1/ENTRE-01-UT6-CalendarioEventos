package programacion.entregaut6.modelo;

import programacion.entregaut6.io.CalendarioIO;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;

/**
 * Esta clase modela un sencillo calendario de eventos.
 * 
 * Por simplicidad consideraremos que los eventos no se solapan
 * y no se repiten
 * 
 * El calendario guarda en un map los eventos de una serie de meses
 * Cada mes (la clave en el map, un enumerado Mes) tiene asociados 
 * en una colección ArrayList los eventos de ese mes
 * 
 * Solo aparecen los meses que incluyen algún evento
 * 
 * Las claves se recuperan en orden alfabético
 * 
 */
public class CalendarioEventos {
    private TreeMap<Mes, ArrayList<Evento>> calendario;

    /**
     * el constructor
     */
    public CalendarioEventos() {
        this.calendario = new TreeMap<>();
    }

    /**
     * añade un nuevo evento al calendario
     * Si la clave (el mes del nuevo evento) no existe en el calendario
     * se creará una nueva entrada con dicha clave y la colección formada
     * por ese único evento
     * Si la clave (el mes) ya existe se añade el nuevo evento insertándolo de forma
     * que quede ordenado por fecha y hora de inicio
     * 
     * Pista! Observa que en la clase Evento hay un método antesDe() que vendrá
     * muy bien usar aquí
     */
    public void addEvento(Evento nuevo) {
        ArrayList<Evento> eventos = new ArrayList<>();
        if (calendario.containsKey(nuevo.getMes())){
            calendario.put(nuevo.getMes(), insertarYOrdenar(calendario.get(nuevo.getMes()), nuevo));
        }
        else {
            eventos.add(nuevo);
            calendario.put(nuevo.getMes(), eventos);
        }
    }

    /**
     * Método privado para insertar y ordenar los eventos
     */
    private ArrayList<Evento> insertarYOrdenar (ArrayList<Evento> eventos, Evento evento) {
        ArrayList<Evento> al = new ArrayList<>();
        al.addAll(eventos);

        int i = 0;
        for (Evento e : eventos){
            if (e.antesDe(evento)){
                al.add(i, evento);
                return al;
            }
            else{
                i++;
            }
        }

        return al;
    }

    /**
     * Representación textual del calendario
     * Hacer de forma eficiente 
     * Usar el conjunto de entradas  
     */
    public String toString() {
        String str = "";
        Set<Map.Entry<Mes, ArrayList<Evento>>> entradas = calendario.entrySet();

        Iterator<Map.Entry<Mes, ArrayList<Evento>>> it = entradas.iterator();
        while (it.hasNext()){
            Map.Entry<Mes, ArrayList<Evento>> entrada = it.next();
            str += ("\n" + entrada.getKey().toString() + "\n");
            for (Evento e : entrada.getValue()){
                str += "\n" + e.toString();
            }
        }

        return str;
    }

    /**
     * Dado un mes devolver la cantidad de eventos que hay en ese mes
     * Si el mes no existe se devuelve 0
     */
    public int totalEventosEnMes(Mes mes) {
        int total = 0;

        if (calendario.containsKey(mes)){
            total = calendario.get(mes).size();
        }

        return total;
    }

    /**
     * Devuelve un conjunto (importa el orden) 
     * con los meses que tienen mayor nº de eventos
     * Hacer un solo recorrido del map con el conjunto de claves
     *  
     */
    public TreeSet<Mes> mesesConMasEventos() {
        TreeSet<Mes> masEventos = new TreeSet<>();
        int max = 0;

        for (Mes mes : calendario.keySet()){
            int tamaño = calendario.get(mes).size();
            if (max < tamaño){
                max = tamaño;
                masEventos.add(mes);
            }
        }

        return masEventos;
    }

    /**
     * Devuelve el nombre del evento de mayor duración en todo el calendario
     * Se devuelve uno solo (el primero encontrado) aunque haya varios
     */
    public String eventoMasLargo() {
        String nombre = "";
        int masLargo = 0;

        Set<Mes> meses = calendario.keySet();
        for (Mes mes : meses){
            ArrayList<Evento> eventos = calendario.get(mes);
            for(Evento evento: eventos){
                if (evento.getDuracion() > masLargo){
                    nombre = evento.getNombre();
                    masLargo = evento.getDuracion();
                }
            }
        }
        return nombre;
    }

    /**
     * Borrar del calendario todos los eventos de los meses indicados en el array
     * y que tengan lugar el día de la semana proporcionado (se entiende día de la
     * semana como 1 - Lunes, 2 - Martes ..  6 - Sábado, 7 - Domingo)
     * 
     * Si alguno de los meses del array no existe el el calendario no se hace nada
     * Si al borrar de un mes los eventos el mes queda con 0 eventos se borra la entrada
     * completa del map
     */
    public int cancelarEventos(Mes[] meses, int dia) {
        int borrados = 0;

        for(Mes mes : meses){
            if (calendario.containsKey(mes)){
                ArrayList<Evento> eventos = calendario.get(mes);

                // No entiendo por qué esto no funciona:

                // for (Evento evento : eventos){
                // if (evento.getDia() == dia){
                // eventos.remove(evento);
                // borrados++;
                // }
                // }

                Iterator<Evento> it = eventos.iterator();
                while (it.hasNext()){
                    Evento evento = it.next();
                    if (evento.getDia() == dia){
                        it.remove();
                        borrados++;
                    }
                }

                if (eventos.isEmpty()){
                    calendario.remove(mes);
                }
            }
        }

        return borrados;
    }

    /**
     * Código para testear la clase CalendarioEventos
     */
    public static void main(String[] args) {
        CalendarioEventos calendario = new CalendarioEventos();
        CalendarioIO.cargarEventos(calendario);
        System.out.println(calendario);

        System.out.println();

        Mes mes = Mes.FEBRERO;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        mes = Mes.MARZO;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        System.out.println("Mes/es con más eventos "
            + calendario.mesesConMasEventos());

        System.out.println();
        System.out.println("Evento de mayor duración: "
            + calendario.eventoMasLargo());

        System.out.println();
        Mes[] meses = {Mes.FEBRERO, Mes.MARZO, Mes.MAYO, Mes.JUNIO};
        int dia = 6;
        System.out.println("Cancelar eventos de " + Arrays.toString(meses));
        int cancelados = calendario.cancelarEventos(meses, dia);
        System.out.println("Se han cancelado " + cancelados +
            " eventos");
        System.out.println();
        System.out.println("Después de cancelar eventos ...");
        System.out.println(calendario);
    }

}
