# CryptoFiles
![](md_images/preview3.jpeg)
<br/>

En el proyecto se realizó la interfaz gráfica usando JavaFX, que a través de un FileChooser seleccionar un archivo,
y un campo de texto que permite ingresar un passphrase, usando el algoritmo PBKDF2, convirtiendolo en una llave para el algoritmo AES.
El programa permite dos modos: Encripción y desencripción.

## Encripción
- Toma el archivo y se calcula su [SHA1](src/main/kotlin/util/Utils.kt) correspondiente.
- Convierte el archivo en un Fileinputstream
- Se crea un outputstream con un archivo que será el destino del cifrado con extensión .encrypt.
- Se almacena en este archivo la SHA1 del archivo desencriptado.
- Usando un Cipheroutputstream se cifra el outputstream del archivo cifrado.
- Se pasa la información del Fileinputstream al Cipheroutputstream


## Desencripción
- Se lee la SHA1 almacenada en el archivo cifrado.
- Se pasa el Inputstream del archivo cifrado al método de desencripción.
- Se desencripta el archivo usando un Cipheroutputstream.
- Se calcula el [SHA1](src/main/kotlin/util/Utils.kt) del archivo desencriptado y se compara.


## Problemas encontrados

- No se calculaba el SHA1 de los archivos, solucionado con [esto](src/main/kotlin/util/Utils.kt)
- Debido a que se cargaba todo el archivo a memoria cuando se encriptaba,
   con archivos muy grandes el programa dejaba el computador inutilizable. por lo que el metodo de encripción ahora usa un stream.
- Debido a que se presentaba la misma situación en la desencripción, se optó por usar un stream.
- No habia feedback que reflejara que ya habian sido terminados los procesos. Lo solucionamos poniendo dialogos al final de cada proceso y también, pusimos una barra de progreso.
- Los botones no se bloqueaban durante el proceso de encripción o desencripción, así que la persona podía poner a encriptar o desencriptar varias veces el mismo archivo. Los solucionamos bloqueando los botones durante los procesos. 
- Solo podemos cifrar archivos cifrados con nuestro programa, estos tienen una extensión especial (.encrypt) dado esto y para simplificar la interfaz dejamos un solo botón que cambia segun si el archivo es .encrypt o no y efectua el proceso de encriptar o desencriptar. 
