package negocio;

import fact.util.CsvReader;
import in2test.util.Dates;
import in2test.util.sql.Data;
import in2test.util.sql.DataConnection;
import in2test.util.sql.DataException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;

public class Inscripcion {
  private Data db;

  public String fechaInscripcion;

  static final boolean DEFECTO1 = false;

  static final boolean DEFECTO2 = true;

  static final boolean DEFECTO3 = false;

  static final boolean DEFECTO4 = false;

  static final boolean DEFECTO6 = true;

  static final boolean DEFECTO7 = true;

  static final boolean DEFECTO8 = true;

  static final boolean DEFECTO9 = false;

  public String msgCabecera;

  public ArrayList<String> inscripcionesOK;

  public ArrayList<String> inscripcionesKO;

  public static final String PROVISIONAL = "Provisional";

  public static final String CONFIRMADA = "Confirmada";

  public static final String ANULADA = "Anulada";

  public static final String OK_PLAZO1 = "1";

  public static final String OK_PLAZO2 = "2";

  public static final String OK_PLAZO3 = "3";

  public static final String KO_PERIODO_NO_ABIERTO = "4";

  public static final String KO_PERIODO_CERRADO = "5";

  public static final String KO_MAXIMO_INSCRIPCIONES = "6";

  public static final String KO_YA_INSCRITO = "7";

  public static final String KO_MENOR_EDAD = "8";

  public static final String KO_PRUEBA_YA_CELEBRADA = "9";

  public static final String PERIODO_NO_ABIERTO = "Plazo inscripción no abierto";

  public static final String PERIODO_CERRADO = "Plazo inscripción cerrado";

  public static final String MAXIMO_INSCRIPCIONES = "No hay plazas disponibles";

  public static final String YA_INSCRITO = "Atleta ya inscrito en la prueba";

  public static final String MENOR_EDAD = "Atleta menor edad";

  public static final String PRUEBA_YA_CELEBRADA = "La prueba ya se celebró";

  public static final String OK = "";

  public static final String KO_ATLETA = "No existe el atleta";

  public static final String KO_COMPETICION = "No existe la competición";

  public static final String SENIOR_MASCULINO = "SM";

  public static final String VETERANO_A_MASCULINO = "M-35";

  public static final String VETERANO_B_MASCULINO = "M-40";

  public static final String VETERANO_C_MASCULINO = "M-45";

  public static final String VETERANO_D_MASCULINO = "M-50";

  public static final String VETERANO_E_MASCULINO = "M-55";

  public static final String VETERANO_F_MASCULINO = "M-60";

  public static final String VETERANO_G_MASCULINO = "M-65";

  public static final String VETERANO_H_MASCULINO = "M-70";

  public static final String SENIOR_FEMENINO = "SF";

  public static final String VETERANO_A_FEMENINO = "F-35";

  public static final String VETERANO_B_FEMENINO = "F-40";

  public static final String VETERANO_C_FEMENINO = "F-45";

  public static final String VETERANO_D_FEMENINO = "F-50";

  public static final String VETERANO_E_FEMENINO = "F-55";

  public static final String VETERANO_F_FEMENINO = "F-60";

  public static final String VETERANO_G_FEMENINO = "F-65";

  public static final String VETERANO_H_FEMENINO = "F-70";

  public static final double DESCUENTO_CLUB = 20.0D;

  protected Data getData() throws DataException {
    try {
      Connection conn = DriverManager.getConnection("jdbc:ucanaccess://TICKETRUN.accdb");
      DataConnection dc = new DataConnection(conn);
      this.db = new Data("TICKETRUN", dc);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "Error en la conexión a la base de datos");
      e.printStackTrace();
    }
    return this.db;
  }

  public Inscripcion() throws DataException {
    Dates hoy = new Dates();
    this.fechaInscripcion = hoy.toSQLString();
    this.db = getData();
  }

  public void setFechaInscripcion(String fecha) {
    this.fechaInscripcion = fecha;
  }

  public String getFechaInscripcion() {
    return this.fechaInscripcion;
  }

  public ResultSet leeCompeticionesTodas() throws DataException {
    String query = "SELECT idCompeticion,descripcion,fechaCelebracion,fechaInicio1,fechaInicio2,fechaCierre,numMaxInscripciones,precioReducido1,precioReducido2,precioInscripcion";
    query = String.valueOf(query) + " FROM Competicion";
    return this.db.query(query);
  }

  public ResultSet leeCompeticionesAbiertas() throws DataException {
    String query = "SELECT idCompeticion,descripcion,fechaCelebracion,fechaInicio1,fechaInicio2,fechaCierre,numMaxInscripciones,precioReducido1,precioReducido2,precioInscripcion ";
    query = String.valueOf(query) + "FROM Competicion WHERE ";
    query = String.valueOf(query) + "fechaCierre >= " + "#" + this.fechaInscripcion + "#" + " AND " + "fechaInicio1 <= " + "#" + this.fechaInscripcion + "#";
    return this.db.query(query);
  }

  public ResultSet leeCompeticionesCerradas() throws DataException {
    String query = "SELECT idCompeticion,descripcion,fechaCelebracion,fechaInicio1,fechaInicio2,fechaCierre,numMaxInscripciones,precioReducido1,precioReducido2,precioInscripcion ";
    query = String.valueOf(query) + "FROM Competicion WHERE ";
    query = String.valueOf(query) + "fechaCierre < " + "#" + this.fechaInscripcion + "#" + " OR " + "fechaInicio1 > " + "#" + this.fechaInscripcion + "#";
    return this.db.query(query);
  }

  public ResultSet leeInscritos(int id) throws DataException {
    String query = "SELECT Atleta.nif,Atleta.nombre,Atleta.apellido1,Atleta.sexo,Atleta.fechaNacimiento";
    query = String.valueOf(query) + ",Inscripcion.categoria,Inscripcion.fechaInscripcion,Inscripcion.tipo,Inscripcion.cantidad,Inscripcion.cantidadAbonada,Inscripcion.estado,Inscripcion.incidencias";
    query = String.valueOf(query) + " FROM Competicion INNER JOIN (Atleta INNER JOIN Inscripcion ON Atleta.idAtleta = Inscripcion.IdAtleta) ON Competicion.idCompeticion = Inscripcion.IdCompeticion ";
    query = String.valueOf(query) + "WHERE Competicion.idCompeticion = " + id;
    return this.db.query(query);
  }

  public ResultSet leeInscripcion(String nif, int idComp) throws DataException {
    String query = "SELECT Competicion.*, Inscripcion.*, Atleta.*";
    query = String.valueOf(query) + " FROM Competicion INNER JOIN (Atleta INNER JOIN Inscripcion ON Atleta.idAtleta = Inscripcion.IdAtleta) ON Competicion.idCompeticion = Inscripcion.IdCompeticion ";
    query = String.valueOf(query) + "WHERE Competicion.idCompeticion = " + idComp + " AND Atleta.nif = " + "'" + nif + "'";
    return this.db.query(query);
  }

  public ResultSet leeSoloInscripcion(String nif, int idComp) throws DataException {
    String query = "SELECT Inscripcion.*";
    query = String.valueOf(query) + " FROM Competicion INNER JOIN (Atleta INNER JOIN Inscripcion ON Atleta.idAtleta = Inscripcion.IdAtleta) ON Competicion.idCompeticion = Inscripcion.IdCompeticion ";
    query = String.valueOf(query) + "WHERE Competicion.idCompeticion = " + idComp + " AND Atleta.nif = " + "'" + nif + "'";
    return this.db.query(query);
  }

  public void insertarCompeticion(String query) throws DataException {
    this.db.execute(query);
  }

  public void modificaAtleta(String query) throws DataException {
    this.db.execute(query);
  }

  public void insertarAtleta(String query) throws DataException {
    this.db.execute(query);
  }

  public String nuevaInscripcion(String nifAtleta, int idComp, String fechaInscripcion, String tipo) throws Exception {
    ResultSet rsAtleta = leeAtleta(nifAtleta);
    ResultSet rsComp = leeCompeticion(idComp);
    String msg = "";
    if (rsAtleta.next()) {
      if (rsComp.next()) {
        Dates fecha = new Dates(fechaInscripcion);
        String codValidacion = validarInscripcion(rsAtleta, rsComp, fecha);
        if (codValidacion != "1" && codValidacion != "2" && codValidacion != "3") {
          if (codValidacion == "4") {
            msg = "Plazo inscripción no abierto";
          } else if (codValidacion == "5") {
            msg = "Plazo inscripción cerrado";
          } else if (codValidacion == "6") {
            msg = "No hay plazas disponibles";
          } else if (codValidacion == "7") {
            msg = "Atleta ya inscrito en la prueba";
          } else if (codValidacion == "8") {
            msg = "Atleta menor edad";
          } else if (codValidacion == "9") {
            msg = "La prueba ya se celebró";
          }
          return msg;
        }
        int idInscripcion = nuevaClave("Inscripcion", "idInscripcion");
        int idAtleta = rsAtleta.getInt("idAtleta");
        double cantidadPlazo1 = rsComp.getDouble("precioReducido1");
        double cantidadPlazo2 = rsComp.getDouble("precioReducido2");
        double cantidadPlazo3 = rsComp.getDouble("precioInscripcion");
        double cantidad = 0.0D;
        if (codValidacion == "1") {
          cantidad = cantidadPlazo1;
        } else if (codValidacion == "2") {
          cantidad = cantidadPlazo2;
        } else {
          cantidad = cantidadPlazo3;
        }
        Date fechaCompeticion = rsComp.getDate("fechaCelebracion");
        Calendar fCompeticion = Calendar.getInstance();
        fCompeticion.setTime(fechaCompeticion);
        Date fechaNacimiento = rsAtleta.getDate("fechaNacimiento");
        Calendar fNacimiento = Calendar.getInstance();
        fNacimiento.setTime(fechaNacimiento);
        int edadAtleta = edad(fCompeticion, fNacimiento);
        String categoria = calculaCategoria(edadAtleta, rsAtleta.getString("sexo"));
        String fechaInsc = "#" + fecha.toSQLString() + "#";
        String query = "INSERT INTO Inscripcion (IdInscripcion,IdAtleta,IdCompeticion,tipo,categoria,fechaInscripcion,cantidad) VALUES (";
        query = String.valueOf(query) + idInscripcion + "," + idAtleta + "," + idComp + "," + "'" + tipo + "'" + "," + "'" + categoria + "'" + "," + fechaInsc + "," + cantidad + ")";
        this.db.execute(query);
        msg = "";
      } else {
        msg = "No existe la competición";
      }
    } else {
      msg = "No existe el atleta";
    }
    return msg;
  }

  protected String validarInscripcion(ResultSet rsAtleta, ResultSet rsCompeticion, Dates fechaInscripcion) throws SQLException {
    int idAtleta = rsAtleta.getInt("idAtleta");
    int idComp = rsCompeticion.getInt("idCompeticion");
    int numMaxInscritos = rsCompeticion.getInt("numMaxInscripciones");
    String query = "SELECT * ";
    query = String.valueOf(query) + " FROM Inscripcion";
    query = String.valueOf(query) + " WHERE idAtleta = " + idAtleta + " AND idCompeticion = " + idComp + ";";
    ResultSet rsIns = this.db.query(query);
    if (rsIns.next()) {
      System.out.println("Ya estinscrito");
      return "7";
    }
    Date fechaInicio1 = rsCompeticion.getDate("fechaInicio1");
    Calendar fInicio1 = Calendar.getInstance();
    fInicio1.setTime(fechaInicio1);
    Date fechaInicio2 = rsCompeticion.getDate("fechaInicio2");
    Calendar fInicio2 = Calendar.getInstance();
    if (fechaInicio2 != null) {
      fInicio2.setTime(fechaInicio2);
    } else {
      fInicio2 = null;
    }
    Date fechaCierre = rsCompeticion.getDate("fechaCierre");
    Calendar fCierre = Calendar.getInstance();
    fCierre.setTime(fechaCierre);
    Date fechaCompeticion = rsCompeticion.getDate("fechaCelebracion");
    Calendar fCompeticion = Calendar.getInstance();
    fCompeticion.setTime(fechaCompeticion);
    Date fechaNacimiento = rsAtleta.getDate("fechaNacimiento");
    Calendar fNacimiento = Calendar.getInstance();
    fNacimiento.setTime(fechaNacimiento);
    Calendar fInscripcion = Calendar.getInstance();
    int[] a = fechaInscripcion.toAnyoMesDia();
    fInscripcion.set(a[0], a[1] - 1, a[2], 0, 0, 0);
    fInscripcion.set(14, fInscripcion.getActualMinimum(14));
    int numActualInscritos = 0;
    String query1 = "SELECT COUNT (*) AS inscritos";
    query1 = String.valueOf(query1) + " FROM Inscripcion";
    query1 = String.valueOf(query1) + " WHERE idCompeticion = " + idComp;
    rsIns = this.db.query(query1);
    if (rsIns.next())
      numActualInscritos = rsIns.getInt("inscritos");
    if (fInscripcion.before(fInicio1))
      return "4";
    if (fInscripcion.after(fCompeticion) || fInscripcion.equals(fCompeticion))
      return "9";
    if (numActualInscritos > numMaxInscritos || numActualInscritos == numMaxInscritos)
      return "6";
    if (nDias(fInscripcion, fCompeticion) >= 2) {
      if (edad(fInscripcion, fNacimiento) < 18)
        return "8";
      if (edad(fCompeticion, fNacimiento) < 18)
        return "8";
      if (fInicio2 != null) {
        if (fInscripcion.before(fInicio2))
          return "1";
        if (fInscripcion.before(fCierre) || fInscripcion.equals(fCierre))
          return "2";
      } else {
        return "3";
      }
      return "3";
    }
    return "5";
  }

  public void inscripcionClub(String fromFile, int idComp) throws Exception {
    System.out.println(">>>Carga de inscripciones de fichero : " + fromFile);
    CsvReader rdr = new CsvReader(fromFile);
    String[] cabecera = { "NIF", "Nombre", "Apellido", "FechaNacimiento", "Sexo", "Club" };
    String[] cabeceraCsv = null;
    if (rdr.readHeaders()) {
      cabeceraCsv = rdr.getHeaders();
      System.out.println(">>>INSCRIPCILeida cabecera csv : " + cabeceraCsv.length + " columnas");
    } else {
      throw new RuntimeException("El fichero " + fromFile + " no contiene cabecera (nombre de los campos): [NIF, Nombre, Apellido, FechaNacimiento, Sexo y Club]");
    }
    if (cabeceraCsv.length != cabecera.length)
      throw new RuntimeException("Error formato en " + fromFile + ": Ncolumnas incorrecto" +
          "Deberia ser:" + cabecera.length);
    for (int i = 0; i < cabecera.length; i++) {
      if (!cabecera[i].equals(cabeceraCsv[i]))
        throw new RuntimeException("Error formato en cabecera " + fromFile + ": Nombre columna " + cabeceraCsv[i] + " incorrecto" +
            "Deberia ser:" + cabecera[i]);
    }
    this.inscripcionesOK = new ArrayList<>();
    this.inscripcionesKO = new ArrayList<>();
    int rowsCount = 0;
    while (rdr.readRecord()) {
      String[] row = rdr.getValues();
      rowsCount++;
      System.out.println("Leida fila csv : " + rdr.getColumnCount() + " columnas");
      if (rdr.getColumnCount() != cabecera.length)
        throw new RuntimeException("Error formato en fila " + (rowsCount + 1) + " de " + fromFile + ": Ncolumnas incorrecto" +
            "Deberia ser:" + cabecera.length);
      String nif = row[0];
      if (nif.length() == 0)
        throw new RuntimeException("Fila: " + rowsCount + ".Dato vacio en columna NIF ");
      String nombre = row[1];
      if (nombre.length() == 0)
        throw new RuntimeException("Fila: " + rowsCount + ".Dato vacio en columna Nombre ");
      String apellido = row[2];
      if (apellido.length() == 0)
        throw new RuntimeException("Fila: " + rowsCount + ".Dato vacio en columna Apellido ");
      String fechaNacimiento = row[3];
      if (apellido.length() == 0)
        throw new RuntimeException("Fila: " + rowsCount + ".Dato vacio en columna FechaNacimiento ");
      if (!esFecha(fechaNacimiento))
        throw new RuntimeException("Fila: " + rowsCount + ".Error tipo de dato en columna FechaNa: " + fechaNacimiento + ". Debe ser fecha formato ISO (aaaa-mm-dd)");
      String sexo = row[4];
      if (sexo.length() == 0)
        throw new RuntimeException("Fila: " + rowsCount + ".Dato vacio en columna Sexo ");
      if (!sexo.equalsIgnoreCase("M") && !sexo.equalsIgnoreCase("F"))
        throw new RuntimeException("Fila: " + rowsCount + ".Error en dato en columna Sexo. Debe ser M F");
      String club = row[5];
      if (club.length() == 0)
        throw new RuntimeException("Fila: " + rowsCount + ".Dato vacio en columna Club ");
      ResultSet rs = leeAtleta(nif);
      if (rs.next()) {
        if (!nombre.equalsIgnoreCase(rs.getString("nombre")))
          throw new RuntimeException("Fila: " + rowsCount + ".No coincide el nombre " + nombre + " con los datos de la base de datos " + rs.getString("nombre") + " para el atleta de nif " + nif);
        if (!apellido.equalsIgnoreCase(rs.getString("apellido1")))
          throw new RuntimeException("Fila: " + rowsCount + ".No coincide el apellido " + apellido + " con los datos de la base de datos " + rs.getString("apellido1") + " para el atleta de nif " + nif);
        if (!fechaNacimiento.equals((new Dates(rs.getDate("fechaNacimiento"))).toSQLString()))
          throw new RuntimeException("Fila: " + rowsCount + ".No coincide la fecha de nacimiento " + fechaNacimiento + " con los datos de la base de datos " + rs.getString("fechaNacimiento") + "  para el atleta de nif " + nif);
        if (!sexo.equalsIgnoreCase(rs.getString("sexo")))
          throw new RuntimeException("Fila: " + rowsCount + ".No coincide el sexo " + sexo + "con los datos de la base de datos " + rs.getString("sexo") + "  para el atleta de nif " + nif);
      } else {
        int idAtleta = nuevaClave("Atleta", "idAtleta");
        String query = "INSERT INTO Atleta (idAtleta,NIF, nombre, apellido1, fechaNacimiento, sexo) VALUES (";
        query = String.valueOf(query) + idAtleta + "," + "'" + nif + "'" + "," + "'" + nombre + "'" + "," + "'" + apellido + "'" + "," + "#" + fechaNacimiento + "#" + "," + "'" + sexo + "'" + ")";
        insertarAtleta(query);
      }
      String msgInscripcion = nuevaInscripcion(nif, idComp, this.fechaInscripcion, "C");
      if (!msgInscripcion.equals("")) {
        String strKO = "NIF " + nif + " ";
        strKO = String.valueOf(strKO) + "NOMBRE Y APELLIDOS: " + " " + nombre + " " + apellido + " ";
        strKO = String.valueOf(strKO) + "FECHA NACIMIENTO: " + fechaNacimiento + " ";
        strKO = String.valueOf(strKO) + "CLUB: " + club + " ";
        strKO = String.valueOf(strKO) + "CAUSA: " + msgInscripcion;
        this.inscripcionesKO.add(strKO);
        System.out.println("InscripciKO: " + nif + " " + nombre + " " + apellido + " " + fechaNacimiento + " " + sexo + " " + club + " CAUSA: " + msgInscripcion);
        continue;
      }
      ResultSet r = leeInscripcion(nif, idComp);
      if (r.next()) {
        int localizador = r.getInt("idInscripcion");
        String nombreAtleta = r.getString("nombre");
        String apellidoAtleta = r.getString("apellido1");
        String nombreCompeticion = r.getString("descripcion");
        float cuota = r.getFloat("cantidad");
        String strOK = "LOCALIZADOR INSC.: " + localizador + " ";
        strOK = String.valueOf(strOK) + "NOMBRE Y APELLIDOS: " + nombreAtleta + " " + apellidoAtleta + " ";
        strOK = String.valueOf(strOK) + "COMPETICIÓN" + nombreCompeticion + " ";
        strOK = String.valueOf(strOK) + "CUOTA " + cuota;
        this.inscripcionesOK.add(strOK);
        continue;
      }
      throw new RuntimeException("Excepción indeterminada");
    }
    this.msgCabecera = "TOTAL INSCRIPCIONES PROCESADAS: " + rowsCount + '\n';
  }

  public ResultSet leeAtleta(String nif) throws DataException {
    String query = "SELECT *";
    query = String.valueOf(query) + " FROM Atleta";
    query = String.valueOf(query) + " WHERE nif= '" + nif + "'";
    return this.db.query(query);
  }

  public void mostrarAtleta(ResultSet rs) throws SQLException {
    System.out.println("Identificador Atleta: " + rs.getInt("idAtleta"));
    System.out.println("NIF: " + rs.getString("nif"));
    System.out.println("Fecha de Nacimiento: " + rs.getDate("fechaNacimiento"));
    System.out.println("Sexo: " + rs.getString("sexo"));
  }

  public ResultSet leeCompeticion(int idComp) throws DataException {
    String query = "SELECT *";
    query = String.valueOf(query) + " FROM Competicion";
    query = String.valueOf(query) + " WHERE idCompeticion= " + idComp;
    return this.db.query(query);
  }

  public String calculaCategoria(int edad, String sexo) {
    String categoria = "";
    String strEdad = "";
    if (sexo.equalsIgnoreCase("M")) {
      if (edad < 35) {
        categoria = String.valueOf(categoria) + "S" + sexo;
        return categoria;
      }
      categoria = String.valueOf(categoria) + sexo + "-";
      if (edad < 40) {
        categoria = "S" + sexo;
        return categoria;
      }
      if (edad < 45) {
        strEdad = String.valueOf(strEdad) + "40";
      } else if (edad < 50) {
        strEdad = String.valueOf(strEdad) + "45";
      } else if (edad < 55) {
        strEdad = String.valueOf(strEdad) + "50";
      } else if (edad < 60) {
        strEdad = String.valueOf(strEdad) + "55";
      } else if (edad < 65) {
        strEdad = String.valueOf(strEdad) + "60";
      } else if (edad < 70) {
        strEdad = String.valueOf(strEdad) + "65";
      } else {
        strEdad = String.valueOf(strEdad) + "70";
      }
    } else {
      if (edad < 40) {
        categoria = String.valueOf(categoria) + "S" + sexo;
        return categoria;
      }
      categoria = String.valueOf(categoria) + sexo + "-";
      if (edad < 45) {
        strEdad = String.valueOf(strEdad) + "40";
      } else if (edad < 50) {
        strEdad = String.valueOf(strEdad) + "45";
      } else if (edad < 55) {
        strEdad = String.valueOf(strEdad) + "50";
      } else if (edad < 60) {
        strEdad = String.valueOf(strEdad) + "55";
      } else if (edad < 70) {
        strEdad = String.valueOf(strEdad) + "60";
      } else {
        strEdad = String.valueOf(strEdad) + "70";
      }
    }
    return String.valueOf(categoria) + strEdad;
  }

  public int edad(Calendar fechaActual, Calendar fechaNacimiento) {
    int dif_anios = fechaActual.get(1) - fechaNacimiento.get(1);
    int dif_meses = fechaActual.get(2) - fechaNacimiento.get(2);
    int dif_dias = fechaActual.get(5) - fechaNacimiento.get(5);
    if (dif_meses < 0 || (dif_meses == 0 && dif_dias < 0))
      dif_anios--;
    return dif_anios;
  }

  public int nDias(Calendar desde, Calendar hasta) {
    Calendar cal = Calendar.getInstance();
    int dias = 0;
    if (desde.getTime().getTime() > hasta.getTime().getTime()) {
      dias = 0;
    } else {
      cal.setTimeInMillis(hasta.getTime().getTime() - desde.getTime().getTime());
      dias = cal.get(6) - 1;
    }
    return dias;
  }

  public int nuevaClave(String tabla, String col) throws DataException {
    int idNuevo = 1;
    try {
      ResultSet rs = this.db.query("SELECT " + col +
          " FROM " + tabla + " ORDER BY " + col + " DESC");
      if (rs.next())
        idNuevo = rs.getInt(col) + 1;
      return idNuevo;
    } catch (SQLException e) {
      throw new DataException(idNuevo, "NuevaClave");
    }
  }

  private boolean esNumero(String s) {
    try {
      Long n = Long.valueOf(Long.parseLong(s));
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean esDouble(String s) {
    try {
      Double n = Double.valueOf(Double.parseDouble(s));
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean esFecha(String s) {
    try {
      Dates f = new Dates(s);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
