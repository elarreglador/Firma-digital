************************************
ACERCA DEL AUTOR
************************************

David M.

elarreglador@protonmail.cocm

https://github.com/elarreglador


************************************
ACERCA DE LA APLICACION
************************************

Veremos cómo podemos autenticar mediante firma digital al emisor y comprobar que el mensaje no 
ha sido alterado tras ser firmado.

El funcionamiento de la firma electrónica está basado en una clave pública y una clave privada.

El emisor tiene un par de claves: una se utiliza para cifrar y otra para descifrar.

El emisor mantiene en secreto la clave privada y pone a disposición del público la clave pública.

El emisor obtiene un resumen del mensaje a firmar con una función hash (resumen). El resumen es una
operación que se realiza sobre un conjunto de datos, de forma que el resultado obtenido es un
conjunto de datos de tamaño fijo, independientemente del tamaño original, y que tiene la propiedad 
de estar asociado unívocamente al mensaje inicial, es decir, es imposible encontrar dos mensajes 
distintos que generan el mismo resultado al aplicar la función hash.

El emisor cifra el resumen (hash) del mensaje con la clave privada. Ésta es la firma electrónica que
se añade al mensaje original.

El receptor, al recibir el mensaje, obtiene de nuevo su resumen mediante la función hash. Además, 
descifra la firma utilizando la clave pública del emisor obteniendo el resumen que el emisor
calculó. Si ambos coinciden, la firma es válida por lo que cumple los criterios
de autenticidad e integridad, además del de no repudio, ya que el emisor no puede negar haber
enviado el mensaje que lleva su firma.


************************************
DETALLE DE FUNCIONAMIENTO
************************************

Creamos objeto generador de claves con Digital Signature Algorithm (DSA):
<pre>
KeyPairGenerator KeyGen = KeyPairGenerator.getInstance("DSA");
</pre>

genera un numero aleatorio que usaremos en la generacion de las claves que tendran una longitud de 
1024 bits
<pre>
SecureRandom numero = SecureRandom.getInstance("SHA1PRNG");
KeyGen.initialize(1024, numero);
KeyPair par = KeyGen.generateKeyPair();
</pre>

Generamos la clave privada para cifrar y la publica compartirla y descifrar
<pre>
PrivateKey clavepriv = par.getPrivate(); // para cifrar
PublicKey clavepub = par.getPublic(); // para descifrar
</pre>

Ahora que tenemos la clave privada, podemos generar un objeto Signature a partir del mensaje
<pre>
Signature dsa = Signature.getInstance("SHA1withDSA");
dsa.initSign(clavepriv);
String mensaje = "Este es el contenido del mensaje que debe ser firmado.";
dsa.update(mensaje.getBytes());
byte[] signatura = dsa.sign();
</pre>

La firma obtenida es un array de bytes que no se pueden mostrar en texto plano de forma correcta, 
por lo que opcionalmente se puede transformar a octal para su correcta representacion.

El receptor recibe tres elementos: la clave publica del emisor, el mensaje y la firma del mensaje


                        ----------------NOTA------------------
                        La firma de cada mensaje es diferente, 
                        ya que contiene un hash que depende 
                        del contenido del mensaje.
                        --------------------------------------

El receptor crea un objeto de tipo Signature del mismo tipo que ha usado el emisor,
en este caso el hash es SHA1 y el algoritmo de firma es DSA (SHA1withDSA). A este objeto
le agrega la firma publica del emisor y el contenido del mensaje.
<pre>
Signature dsa2 = Signature.getInstance("SHA1withDSA");
dsa2.initVerify(clavepub);
dsa2.update(mensaje.getBytes());
</pre>

De forma interna se descifra la firma que contiene el hash generado por el emisor, a continuacion
se obtiene el hash a partir del mensaje recibido. Si ambos son iguales la firma es valida para 
el mensaje recibido
<pre>
boolean verifica = dsa2.verify(signatura);
</pre>

************************************
LANZAR LA APP 
************************************

Esta aplicacion requiere tener java instalado

Iniciamos la app:
<pre>
javac T5S2P2VerificaSignaturaDigitalFONTS.java
java T5S2P2VerificaSignaturaDigitalFONTS
</pre>



                                                                                David M.
                                                                 11 de diciembre de 2023


