import java.security.*;

public class T5S2P2VerificaSignaturaDigitalFONTS {
	public static void main(String[] args) {
		try {

			// GENERAR PAR DE CLAVES
			// crea objeto generador de claves con Digital Signature Algorithm (DSA)
			KeyPairGenerator KeyGen = KeyPairGenerator.getInstance("DSA");

			// genera un numero aleatorio
			SecureRandom numero = SecureRandom.getInstance("SHA1PRNG");
			// indicamos que queremos una clave de 1024 bits basada en el numero aleatorio
			KeyGen.initialize(1024, numero);

			KeyPair par = KeyGen.generateKeyPair();

			// GUARDA LAS CLAVES PUBLICA Y PRIVADA
			PrivateKey clavepriv = par.getPrivate(); // para cifrar
			PublicKey clavepub = par.getPublic(); // para descifrar

			// EMISSOR FIRMA CON CLAVE PRIVADA
			// crea objeto firma con hash SHA1 bajo DSA
			Signature dsa = Signature.getInstance("SHA1withDSA");
			// agregamos la clave privada al objeto firma
			dsa.initSign(clavepriv);
			// pasamos el mensaje al objeto firma
			String mensaje = "Este es el contenido del mensaje que debe ser firmado.";
			dsa.update(mensaje.getBytes());
			// obtenemos la firma del mensaje
			byte[] signatura = dsa.sign();

			// array de bytes a hexadecimal para lectura humana
			StringBuffer hexSignatura = new StringBuffer();
			for (int i = 0; i < signatura.length; i++) {
				String hex = Integer.toHexString(0xff & signatura[i]);
				if (hex.length() == 1)
					hexSignatura.append('0');
				hexSignatura.append(hex);
			}

			System.out.println("*** El emisor firma el mensaje ***");
			System.out.println("  Mensjaje firmado: \n    " + mensaje);
			System.out.println("  Firma: \n    " + hexSignatura);
			System.out.println();










			// RECEPCION DEL MENSAJE POR PARTE DEL CLIENTE
			System.out.println("*** El receptor recibe clave publica, mensaje, y la firma ***");
			System.out.println("  Firma: \n    " + hexSignatura);
			System.out.println("  Mensaje: \n    " + mensaje);
			System.out.println();
			System.out.println("""
					El receptor crea un nuevo objeto firma del mismo tipo que el emisor, le pasa la clave publica,
					el mensaje y por ultimo verifica la firma
					""");
			// receptor descifra la firma utilizando la clave publica del emisor
			// + el mensaje, obteniendo asi el hash que el emisor calculo, si el
			// hash coincide, la firma es valida
			Signature dsa2 = Signature.getInstance("SHA1withDSA");
			dsa2.initVerify(clavepub);
			dsa2.update(mensaje.getBytes());
			boolean verifica = dsa2.verify(signatura);

			System.out.println(verifica ? "  El hash calculado y el recibido son iguales: La firma es válida"
					: "  El hash calculado y el recibido son diferentes: La firma no es válida");
			System.out.println();

		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException en main(): " + e);
		} catch (InvalidKeyException e) {
			System.out.println("InvalidKeyException en main(): " + e);
		} catch (SignatureException e) {
			System.out.println("SignatureException en main(): " + e);
		}
	}
}
