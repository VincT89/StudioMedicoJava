package test;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;


public class StudioMedico {

	public static void main(String[] args) throws IOException {
		// Creazione del server
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		
		server.createContext("/", new AppuntamentiHandler());

		server.createContext("/riepilogoAppuntamenti", new RiepilogoAppuntamentiHandler());
		
		server.createContext("/informazioniAggiuntive", new InformazioniAggiuntive());

		server.createContext("/img", new StaticFileHandler());
		
		// Avvio del server
		server.start();
		System.out.println("Server in esecuzione sulla porta 8080");
	}
	
	static class AppuntamentiHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			if(exchange.getRequestMethod().equalsIgnoreCase("GET")) {
				String risposta = 
						  "<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>" 
						+ "<head>"
						+ "<meta name='viewport' content='width=device-width, initial-scale=1' />"
						+ "<meta charset='utf-8' />"
						+ "<meta name='description' content='Homepage Studio Medico BitCamp' />"
						+ "<meta name='keywords' content='studio medico, prenotazione appuntamenti, servizi medici' />"
						+ "<meta name='author' content='Classe BitCamp' />" 
						+ "<title>Studio Medico</title>" 
						+ "<style>"
						+ "body {" 
						+ "margin: 0;" 
						+ "padding:0;" 
						+ "font-family: Arial, sans-serif;"
						+ "background: url('./img/sfondo.jpg') fixed;"
						+ "background-size: cover;" 
						+ "color: #333;" 
						+ "}" 
						+ "header {"
						+ "text-align: center;"
						+ "background: rgba(0,0,0,0.7);" 
						+ "color:#fff;" 
						+ "padding: 50px;" 
						+ "}" 
						+ "nav {"
						+ "text-align: center;" 
						+ "background: #333;" 
						+ "padding: 10px;" 
						+ "}" 
						+ "nav a {" 
						+ "color: #fff;"
						+ "text-decoration: none;" 
						+ "margin: 0 15px;" 
						+ "}" 
						+ "form {" 
						+ "max-width: 600px;"
						+ "margin: 50px auto;" 
						+ "padding: 20px;" 
						+ "background: rgba(255,255,255,0.8);"
						+ "border-radius: 10px;" 
						+ "}" 
						+ "form label {" 
						+ "display: block;" 
						+ "margin-bottom: 8px;" 
						+ "}"
						+ "form input, form select {" 
						+ "width: 90%;" 
						+ "padding: 10px;" 
						+ "margin-bottom: 20px;"
						+ "border: 1px solid #ccc;" 
						+ "border-radius: 5px;" 
						+ "}" 
						+ "form button {" 
						+ "background: #3498db;"
						+ "color: #fff;" 
						+ "padding: 10px 15px;" 
						+ "border: none;" 
						+ "border-radius: 5px;"
						+ "cursor: pointer;" 
						+ "}" 
						+ "section {" 
						+ "text-align: center;" 
						+ "padding: 50px;"
						+ "background: rgba(255,255,255,0.8);" 
						+ "border-radius: 10px;" 
						+ "margin: 20px;" 
						+ "}"
						+ "section h2 {" 
						+ "color: #333;" 
						+ "}" 
						+ "section ul {" 
						+ "list-style: none;" 
						+ "padding: 0;" 
						+ "}"
						+ "section li {" 
						+ "margin-bottom: 10px;" 
						+ "}" 
						+ "footer {" 
						+ "text-align:center;"
						+ "padding: 20px;" 
						+ "background: #333;" 
						+ "color: #fff;" 
						+ "}" 
						+ "</style>" 
						+ "</head>" 
						+ "<body>"
						+ "<header>" 
						+ "<h1>Studio Medico BitCamp</h1>" 
						+ "<p>La tua salute è la nostra priorità</p>"
						+ "</header>" 
						+ "<nav>" 
						+ "<a href='#'>Home</a>"
						+ "<a href='#'>Chi Siamo</a>" 
						+ "<a href='#'>Servizi</a>"
						+ "<a href='/informazioniAggiuntive'>Informazioni Aggiuntive</a>" 
						+ "<a href='#'>Contatti</a>"
						+ "<a href='/riepilogoAppuntamenti'>Riepilogo Appuntamenti</a>"
						+ "</nav>" 
						+ "<form id='appuntamentiForm' method='POST' action='/>"
						+ "<label for='data'>Data dell'appuntamento:</label>"
						+ "<input type='date' id='data' name='data' required />"
						+ "<label for='ora'>Ora dell'appuntamento:</label>"
						+ "<input type='time' id='ora' name='ora' required />"
						+ "<button type='submit'>Prenota Appuntamento</button>" 
						+ "</form>"
						+ "<section>" 
						+ "<h2>Servizi Offerti</h2>"
						+ "<ul>" 
						+ "<li>Medicina Generale</li>" 
						+ "<li>Pediatria</li>" 
						+ "<li>Cardiologia</li>"
						+ "<li>Dermatologia</li>" 
						+ "<li>Ortopedia</li>" 
						+ "</ul>" 
						+ "</section>"
						+ "<section>" 
						+ "<h2>Chi Siamo</h2>"
						+ "<p>Lo Studio Medico BitCamp si impegna a fornire servizi medici di alta qualità per migliorare la salute e il benessere dei propri studenti di programmazione.</p>"
						+ "</section>" 
						+ "<section>" 
						+ "<h2>Contatti</h2>"
						+ "<p>Indirizzo: Via delle Cure, 123 - 00123 Roma</p>" 
						+ "<p>Email: info@studiomedicobit.it</p>"
						+ "<p>Telefono: 0123-456789</p>" 
						+ "</section>" 
						+ "<footer>"
						+ "<p>&copy; 2024 Studio Medico BitCamp. Tutti i diritti riservati.</p>" 
						+ "</footer>" 
						+ "</body>"
						+ "</html>";
				
				//Imposta l'intestazione della risposta
				exchange.getResponseHeaders().set("content-Type", "text/html");
				
				//Imposta lunghezza della risposta e status code
				//exchange.sendResponseHeaders(200, risposta.length());
				
				int contentLength = risposta.getBytes("UTF-8").length;
				exchange.sendResponseHeaders(200, contentLength);
				
				//Scrive la risposta
				OutputStream os = exchange.getResponseBody();
				os.write(risposta.getBytes());
				os.close();
				
			}else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
	            // Recupera i dati inviati dal form
	            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
	            BufferedReader br = new BufferedReader(isr);
	            String formData = br.readLine();

	            if (formData != null) {
	                String[] formDataArray = formData.split("&");

	                String dataString = URLDecoder.decode(formDataArray[0].split("=")[1], "UTF-8");
	                String oraString = URLDecoder.decode(formDataArray[1].split("=")[1], "UTF-8");

	           
	                //System.out.println("Data: " + dataString);
	                //System.out.println("Ora: " + oraString);

	                try {
	                    // Conversione in LocalDate e LocalTime
	                    LocalDate localDate = LocalDate.parse(dataString);
	                    LocalTime localTime = LocalTime.parse(oraString);

	                    // Conversione a java.sql.Date e java.sql.Time
	                    Date sqlDate = Date.valueOf(localDate);
	                    Time sqlTime = Time.valueOf(localTime);

	                    // Connessione al database e inserimento appuntamento
	                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studioMedico", "root", "root");
	                    String query = "INSERT INTO appuntamenti (data, ora) VALUES (?, ?)";
	                    PreparedStatement pstmt = conn.prepareStatement(query);
	                    pstmt.setDate(1, sqlDate);
	                    pstmt.setTime(2, sqlTime);

	                    pstmt.executeUpdate();
	                    pstmt.close();
	                    conn.close();

	                    // Reindirizzamento alla homepage dopo l'inserimento
	                    exchange.getResponseHeaders().set("Location", "/");
	                    exchange.sendResponseHeaders(302, -1);
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else {
	            exchange.sendResponseHeaders(405, -1); // Metodo non permesso
	        }
	    }
	}

	static class RiepilogoAppuntamentiHandler implements HttpHandler {
	    @Override
	    public void handle(HttpExchange exchange) throws IOException {
	        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
	            StringBuilder response = new StringBuilder();

	            // Inizio della struttura HTML, con stile e layout della homepage
	            response.append("<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>")
	                    .append("<head>")
	                    .append("<meta name='viewport' content='width=device-width, initial-scale=1' />")
	                    .append("<meta charset='utf-8' />")
	                    .append("<meta name='description' content='Riepilogo Appuntamenti Studio Medico BitCamp' />")
	                    .append("<meta name='keywords' content='studio medico, appuntamenti, riepilogo' />")
	                    .append("<meta name='author' content='Classe BitCamp' />")
	                    .append("<title>Riepilogo Appuntamenti</title>")
	                    .append("<style>")
	                    .append("body {margin: 0; padding:0; font-family: Arial, sans-serif; background: url('/img/sfondo.jpg') fixed; background-size: cover; color: #333; }")
	                    .append("header { text-align: center; background: rgba(0,0,0,0.7); color:#fff; padding: 50px; }")
	                    .append("nav { text-align: center; background: #333; padding: 10px; }")
	                    .append("nav a { color: #fff; text-decoration: none; margin: 0 15px; }")
	                    .append("section { text-align: center; padding: 50px; background: rgba(255,255,255,0.7); border-radius: 10px; margin: 50px auto; width:50%; height:46.6vh }")
	                    .append("footer { text-align:center; padding: 20px; background: #333; color: #fff; }")
	                    .append("table { width: 80%; margin: 0 auto; border-collapse: collapse; }")
	                    .append("th, td { padding: 15px; border: 1px solid #ddd; text-align: center; }")
	                    .append("th { background-color: #333; color: white; }")
	                    .append("</style>")
	                    .append("</head>")
	                    .append("<body>")
	                    .append("<header><h1>Studio Medico BitCamp</h1><p>La tua salute e' la nostra priorita'</p></header>")
	                    .append("<nav><a href='/'>Home</a><a href='#'>Chi Siamo</a><a href='#'>Servizi</a><a href='/informazioniAggiuntive'>Informazioni Aggiuntive</a><a href='#'>Contatti</a><a href='/riepilogoAppuntamenti'>Riepilogo Appuntamenti</a></nav>")
	                    // Inizio del corpo contenente la tabella degli appuntamenti
	                    .append("<section><h2> Riepilogo degli Appuntamenti</h2><table><tr><th>Data</th><th>Ora</th></tr>");

	            // Recupero dei dati dal database e popolamento della tabella
	            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studioMedico", "root", "root");
	                 Statement stmt = conn.createStatement();
	                 ResultSet rs = stmt.executeQuery("SELECT * FROM appuntamenti")) {

	                while (rs.next()) {
	                    Date data = rs.getDate("data");
	                    Time ora = rs.getTime("ora");
	                    response.append("<tr><td>").append(data).append("</td><td>").append(ora).append("</td></tr>");
	                }
	            } catch (SQLException e) {
	                response.append("<tr><td colspan='2'>Errore nel recupero degli appuntamenti</td></tr>");
	                e.printStackTrace();
	            }

	            // Chiusura della tabella e del corpo
	            response.append("</table></section>");
	            // Footer
	            response.append("<footer><p>&copy; 2024 Studio Medico BitCamp. Tutti i diritti riservati.</p></footer>");
	            response.append("</body></html>");

	            // Imposta l'intestazione della risposta
	            exchange.getResponseHeaders().set("Content-Type", "text/html");
	            int contentLength = response.length();
	            exchange.sendResponseHeaders(200, contentLength);
	            OutputStream os = exchange.getResponseBody();
	            os.write(response.toString().getBytes());
	            os.close();
	        } else {
	            exchange.sendResponseHeaders(405, -1); 
	        }
	    }
	}
	
	static class InformazioniAggiuntive implements HttpHandler{
		@Override
		public void handle(HttpExchange exchange) throws IOException{
			String risposta = 
							  "<html lang='en' xmlns='http://www.w3.org/1999/xhtml'>" 
							+ "<head>" 
							+ "<meta name='viewport' content='width=device-width, initial-scale=1' />" 
							+ "<meta name='description' content='Pagina Secondaria Studio Medico' />" 
							+ "<meta name='keywords' content='studio medico, informazioni aggiuntive' />" 
							+ "<meta name='author' content='Classe BitCamp' />" 
							+ "<meta charset='utf-8' />" 
							+ "<title>Informazioni Aggiuntive  - Studio Medico</title>" 
							+ "<style>" 
							+ "body {" 
							+ "margin: 0;" 
							+ "padding: 0;" 
							+ "font-family: Arial, sans-serif;" 
							+ "background: url('img/sfondo.jpg') fixed;" 
							+ "background-size: cover;" 
							+ "color: #333;" 
							+ "}" 
							+ "header {" 
							+ "text-align: center;" 
							+ "background: rgba(0,0,0,0.7);" 
							+ "color: #fff;" 
							+ "padding: 50px;" 
							+ "}" 
							+ "nav {" 
							+ "text-align: center;" 
							+ "background: #333;" 
							+ "padding: 10px;" 
							+ "}" 
							+ "nav a {" 
							+ "color: #fff;" 
							+ "text-decoration: none;" 
							+ "margin: 0 15px;" 
							+ "}" 
							+ "form {" 
							+ "max-width: 600px;" 
							+ "margin: 50px auto;" 
							+ "padding: 20px;" 
							+ "background: rgba(255,255,255,0.8);" 
							+ "border-radius: 10px;" 
							+ "}" 
							+ "form label {" 
							+ "display: block;" 
							+ "margin-bottom: 8px;" 
							+ "}" 
							+ "form input, form select {" 
							+ "width: 90%;" 
							+ "padding: 10px;" 
							+ "margin-bottom: 20px;" 
							+ "border: 1px solid #ccc;" 
							+ "border-radius: 5px;" 
							+ "}" 
							+ "form button {" 
							+ "background: #3498db;" 
							+ "color: #fff;" 
							+ "padding: 10px 15px;" 
							+ "border: none;" 
							+ "border-radius: 5px;" 
							+ "cursor: pointer;" 
							+ "}" 
							+ "section {" 
							+ "text-align: center;" 
							+ "padding: 20px;" 
							+ "background: rgba(255,255,255,0.8);" 
							+ "border-radius: 10px;" 
							+ "margin: 50px auto;" 
							+ "height:52.5vh;"
							+ "width:50%;"
							+ "}" 
							+ "section h2 {" 
							+ "color: #333;" 
							+ "}" 
							+ "section ul {" 
							+ "list-style: none;" 
							+ "padding: 0;" 
							+ "}" 
							+ "section li {" 
							+ "margin-bottom: 10px;" 
							+ "}" 
							+ "footer {" 
							+ "text-align: center;"
							+ "padding: 20px;" 
							+ "background: #333;" 
							+ "color: #fff;" 
							+ "}" 
							+ "</style>" 
							+ "</head>" 
							+ "<body>" 
							+ "<header>" 
							+ "<h1>Informazioni Aggiuntive</h1>" 
							+ "<p>Scopri di più su Studio Medico BitCamp</p>" 
							+ "</header>" 
							+ "<nav>" 
							+ "<a href='/'>Home</a>" 
							+ "<a href='#'>Servizi</a>" 
							+ "<a href='#'>Contatti</a>" 
							+ "<a href='/riepilogoAppuntamenti'>Riepilogo Appuntamenti</a>"
							+ "</nav>" 
							+ "<section>" 
							+ "<h2>Ulteriori Dettagli</h2>" 
							+ "<p>Lo Studio Medico BitCamp offre una vasta gamma di servizi specialistici tra cui:</p>" 
							+ "<ul>" 
							+ "<li>Ginecologia</li>" 
							+ "<li>Oncologia</li>" 
							+ "<li>Neurologia</li>" 
							+ "<li>Psicologia</li>" 
							+ "</ul>" 
							+ "<p>Per ulteriori informazioni, non esitare a contattarci.</p>" 
							+ "</section>" 
							+ "<footer>" 
							+ "<p>&copy; 2024 Studio Medico BitCamp. Tutti i diritti riservati.</p>" 
							+ "</footer>" 
							+ "</body>" 
							+ "</html>" ;
			
			//Imposta l'intestazione della risposta
			exchange.getResponseHeaders().set("content-Type", "text/html");
			
			//Imposta lunghezza della risposta e status code
			//exchange.sendResponseHeaders(200, risposta.length());
			
			int contentLength = risposta.getBytes("UTF-8").length;
			exchange.sendResponseHeaders(200, contentLength);
			
			//Scrive la risposta
			OutputStream os = exchange.getResponseBody();
			os.write(risposta.getBytes());
			os.close();

		}
	}
	
	static class StaticFileHandler implements HttpHandler {
	    @Override
	    public void handle(HttpExchange exchange) throws IOException {
	       
	        String filePath = "C:/Users/vince/OneDrive/Desktop/Java/StudioMedico/src/test" + exchange.getRequestURI().getPath();

	        File file = new File(filePath);
	        if (file.exists() && !file.isDirectory()) {
	            // Imposta il tipo di contenuto corretto
	            exchange.getResponseHeaders().set("Content-Type", "image/jpeg");
	            exchange.sendResponseHeaders(200, file.length());

	            // contenuto del file nella risposta
	            OutputStream os = exchange.getResponseBody();
	            Files.copy(file.toPath(), os);
	            os.close();
	        } else {
	            // Se il file non esiste, ritorna un errore
	            exchange.sendResponseHeaders(404, -1);
	        }
	    }
	}

}
