/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supermercado;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author xabi,sergio,javier
 */
public class Supermercado {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        {
            // TODO code application logic here
            // comprobar el driver
            System.out.println("Comprobando el driver...");

            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("El driver no está disponible: " + ex);

            }
            System.out.println("El driver esta disponible");
            Connection conexion = null;

            //intentar conectarse a la base de datos "url", "usuario" , "contraseña"
            try {
                conexion = DriverManager.getConnection("jdbc:postgresql://localhost/super", "adminis", "admin");
            } catch (SQLException e) {
                System.out.println("Error al intentar conectarse a la base de datos");
            }
            if (conexion != null) {
                System.out.println("Se ha podido establecer la conexion a la base de datos");
            } else {
                System.out.println("Usuario o contraseña incorrectos");
            }

            Statement s = null;

            //ya conectado se comienzan a hacer las consultas
            try {
                s = conexion.createStatement();
            } catch (SQLException ex) {
                System.out.println("Probando la consulta para la conexion: " + ex);

            }
            ResultSet resultado = null;

            //consulta 1
            try {
                resultado = s.executeQuery("SELECT codigobarras, preciosiniva FROM productos");
                System.out.println("CONSULTA 1");
                while (resultado.next()) {
                    System.out.println("Codigo barras   y Precio sin IVA");
                    System.out.println(resultado.getString("codigobarras") + "  /  " + resultado.getString("preciosiniva"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 2
            try {
                resultado = s.executeQuery("SELECT nombre, CASE WHEN horas IS NULL THEN 'cajero' ELSE 'reponedor' END AS tipo FROM trabajadores FULL JOIN cajero ON cajero.ss_trabajadores = trabajadores.ss FULL JOIN reponedor ON reponedor.ss_trabajadores = trabajadores.ss");
                System.out.println("CONSULTA 2");
                while (resultado.next()) {
                    System.out.println("Nombre y Tipo");
                    System.out.println(resultado.getString("nombre") + "  /  " + resultado.getString("tipo"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 3 
            try {
                resultado = s.executeQuery("SELECT nombre FROM trabajadores INNER JOIN reponedor ON trabajadores.ss = reponedor.ss_trabajadores WHERE horas > 20");
                System.out.println("CONSULTA 3");
                while (resultado.next()) {
                    System.out.println("Nombre reponedores horas>20");
                    System.out.println(resultado.getString("nombre"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 4
            try {
                resultado = s.executeQuery("SELECT sum(precioiva * cantidad) FROM compra INNER JOIN productos ON productos.codigobarras = compra.codigobarras_productos");
                System.out.println("CONSULTA 4");
                while (resultado.next()) {
                    System.out.println("Total dinero facturado");
                    System.out.println(resultado.getString("sum"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 5
            try {
                resultado = s.executeQuery("SELECT numero, codigobarras_productos,descuento FROM contiene, cupones");
                System.out.println("CONSULTA 5");
                while (resultado.next()) {
                    System.out.println("Cupon, codigo barras producto y descuento");
                    System.out.println(resultado.getString("numero") + "    /   " + resultado.getString("codigobarras_productos") + "   /   " + resultado.getString("descuento"));

                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 6
            try {
                resultado = s.executeQuery("SELECT contiene.codigobarras_productos FROM cupones INNER JOIN contiene ON contiene.numero_cupones =  contiene.numero_cupones");
                System.out.println("CONSULTA 6");
                while (resultado.next()) {
                    System.out.println("Codigo de barras producto con descuento");
                    System.out.println(resultado.getString("codigobarras_productos"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 7
            try {
                Statement stmnt = conexion.createStatement();
                resultado = stmnt.executeQuery("SELECT round(AVG(nota)) FROM opinion");
                System.out.println("CONSULTA 7");
                while (resultado.next()) {
                    System.out.println("Grado satisfacción medio clientes");
                    System.out.println("" + resultado.getString("round"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 8
            try {
                resultado = s.executeQuery("SELECT count(num), ciudad_tienda,cajero FROM tickets GROUP BY cajero,ciudad_tienda ORDER BY count(num) DESC");
                System.out.println("CONSULTA 8");
                while (resultado.next()) {
                    System.out.println("Número/   Tienda         / Cajero");
                    System.out.println(resultado.getString("count") + "    /    " + resultado.getString("ciudad_tienda") + "    /    " + resultado.getString("cajero"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 9
            try {
                resultado = s.executeQuery("SELECT count(nombre),ciudad_tienda FROM trabajadores GROUP BY ciudad_tienda ORDER BY count(nombre)");
                System.out.println("CONSULTA 9");
                while (resultado.next()) {
                    System.out.println("Nombre y ciudad de la Tienda");
                    System.out.println(resultado.getString("count") + "   /    " + resultado.getString("ciudad_tienda"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 10
            try {
                resultado = s.executeQuery("SELECT nombre,fijo,movil FROM trabajadores \n"
                        + "WHERE trabajadores.puntuacion=(SELECT MAX(puntuacion)FROM trabajadores)");
                System.out.println("CONSULTA 10");
                while (resultado.next()) {
                    System.out.println("Nombre, Fijo y Móvil");
                    System.out.println(resultado.getString("nombre") + "   /   " + resultado.getString("fijo") + "   /   " + resultado.getString("movil"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 11
            try {
                resultado = s.executeQuery("SELECT * FROM tiene INNER JOIN ofertas ON semana_ofertas = semana_ofertas");
                System.out.println("CONSULTA 11");
                while (resultado.next()) {
                    System.out.println("Codigo barras producto, Ofertas, Semana, Descuento, Inicio, Fin");
                    System.out.println(resultado.getString("codigobarras_productos") + "   /   " + resultado.getString("semana_ofertas") + "   /   " + resultado.getString("semana") + "  /  " + resultado.getString("descuento") + "  /  " + resultado.getString("inicio") + "  /  " + resultado.getString("fin"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 12
            try {
                resultado = s.executeQuery("SELECT nombre FROM socio WHERE numsocio in (SELECT numsocio_socio FROM tickets INNER JOIN contiene ON num = num\n"
                        + "WHERE fecha >= '2019-05-27' AND fecha <= '2019-05-31' AND codigobarras_productos IN\n"
                        + "(SELECT codigobarras_productos FROM producto_oferta INNER JOIN ofertas ON semana_ofertas = semana_ofertas\n"
                        + "WHERE inicio >= '2019-05-24' AND fin <= '2019-05-31' ORDER BY inicio))");
                System.out.println("CONSULTA 12");
                while (resultado.next()) {
                    System.out.println("Nombre");
                    System.out.println(resultado.getString("nombre"));

                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 13
            try {
                resultado = s.executeQuery("SELECT nombre FROM trabajadores WHERE ciudad_tienda like  'M%' ORDER BY nombre");
                System.out.println("CONSULTA 13");
                while (resultado.next()) {
                    System.out.println("Nombre");
                    System.out.println(resultado.getString("Nombre"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 14
            try {
                resultado = s.executeQuery("SELECT email FROM socio WHERE saldo=(SELECT MAX(saldo)FROM socio) ");
                System.out.println("CONSULTA 14");
                while (resultado.next()) {
                    System.out.println("E-mail");
                    System.out.println(resultado.getString("email"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 15
            try {
                resultado = s.executeQuery("SELECT codigobarras_productos, SUM(cantidad) as total\n"
                        + "FROM devuelve\n"
                        + "GROUP BY codigobarras_productos HAVING SUM(cantidad) >= \n"
                        + "(SELECT MAX(total) FROM (SELECT SUM(cantidad) as total FROM devuelve GROUP BY \n"
                        + "codigobarras_productos) as total)\n"
                        + "ORDER BY total desc");
                System.out.println("CONSULTA 15");
                while (resultado.next()) {
                    System.out.println("Codigo barras producto y Total");
                    System.out.println(resultado.getString("codigobarras_productos") + "  /  " + resultado.getString("total"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 16
            try {
                resultado = s.executeQuery("SELECT cajero FROM tickets WHERE tickets.cajero=(SELECT MAX(cajero)FROM tickets)");
                System.out.println("CONSULTA 16");
                while (resultado.next()) {
                    System.out.println("Cajero");
                    System.out.println(resultado.getString("cajero"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 17
            try {
                resultado = s.executeQuery("SELECT nombre,nota FROM socio,opinion WHERE socio.numsocio = opinion.numsocio_socio AND opinion.nota = (SELECT MAX(opinion.nota) FROM opinion)");
                System.out.println("CONSULTA 17");
                while (resultado.next()) {
                    System.out.println("Nombre y Nota");
                    System.out.println(resultado.getString("nombre") + "   /  " + resultado.getString("nota"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 18
            try {
                resultado = s.executeQuery("SELECT num,saldo,cajero, ciudad_tienda FROM tickets WHERE cajero like 'A%' AND ciudad_tienda like 'M%'");
                System.out.println("CONSULTA 18");
                while (resultado.next()) {
                    System.out.println("Numero, Saldo, Cajero y la ciudad de la tienda");
                    System.out.println(resultado.getString("num") + "   /   " + resultado.getString("saldo") + "   /   " + resultado.getString("cajero") + "   /   " + resultado.getString("ciudad_tienda"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 19
            try {
                resultado = s.executeQuery("SELECT num, trabajadores.nombre, tienda.ciudad FROM tickets, cajero, trabajadores, tienda WHERE tickets.ss_trabajadores_cajero = cajero.ss_trabajadores AND cajero.ss_trabajadores = trabajadores.ss AND trabajadores.ciudad_tienda = tienda.ciudad AND tienda.ciudad = 'ALCALA DE HENARES'");
                System.out.println("CONSULTA 19");
                while (resultado.next()) {
                    System.out.println("Numero, trabajador y la ciudad de la tienda");
                    System.out.println(resultado.getString("num") + "   /   " + resultado.getString("nombre") + "   /   " + resultado.getString("ciudad"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //consulta 20
            try {
                resultado = s.executeQuery("SELECT num, trabajadores.nombre, tienda.ciudad, fecha FROM tickets, cajero, trabajadores, tienda \n"
                        + "           WHERE tickets.ss_trabajadores_cajero = cajero.ss_trabajadores AND\n"
                        + "           cajero.ss_trabajadores = trabajadores.ss AND trabajadores.ciudad_tienda = tienda.ciudad\n"
                        + "           AND tienda.ciudad = 'ALCALA DE HENARES' AND num NOT IN \n"
                        + "           ((SELECT num FROM tickets WHERE numsocio_socio NOT IN ( SELECT numsocio_socio FROM contiene INNER JOIN\n"
                        + "           ( SELECT numero, numsocio_socio FROM tiene INNER JOIN cupones ON numero = numero ) \n"
                        + "           AS cupones_socios ON cupones_socios.numero = contiene.numero_cupones ) )\n"
                        + "           INTERSECT\n"
                        + "           (SELECT num_tickets FROM compra INNER JOIN ( SELECT codigobarras_productos FROM contiene INNER JOIN\n"
                        + "           (SELECT numero, numsocio_socio FROM tiene INNER JOIN cupones ON numero = numero ) \n"
                        + "           AS cupones ON cupones.numero = contiene.numero_cupones ) as p \n"
                        + "           ON p.codigobarras_productos = compra.codigobarras_productos)\n"
                        + "           ) AND num NOT IN (SELECT DISTINCT num_tickets FROM tickets, ( SELECT * FROM productos, ofertas, compra, producto_oferta\n"
                        + "           WHERE producto_oferta.codigobarras_productos = productos.codigobarras\n"
                        + "           AND compra.codigobarras_productos = productos.codigobarras ) as fecha WHERE num_tickets = num_tickets\n"
                        + "           AND tickets.fecha BETWEEN fecha.inicio AND fecha.fin)");
                System.out.println("CONSULTA 20");
                while (resultado.next()) {
                    System.out.println("Numero, Nombre, Ciudad y Fecha");
                    System.out.println(resultado.getString("num") + "   /   " + resultado.getString("nombre") + "   /   " + resultado.getString("ciudad") + "   /   " + resultado.getString("fecha"));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }

}
